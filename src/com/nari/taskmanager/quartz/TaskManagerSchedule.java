package com.nari.taskmanager.quartz;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;

public class TaskManagerSchedule implements InitializingBean {
	private static Scheduler scheduler;
	private static Logger logger = Logger.getLogger(TaskManagerSchedule.class);
	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		TaskManagerSchedule.scheduler = scheduler;
	}

	public void destroy() {

	}

	public void afterPropertiesSet() throws Exception {

		Assert.notNull(scheduler, "quartz scheduler is null");
		logger.info(">>>>>>>>> init quartz scheduler success.");
	}

	public void init() {

	}

/**
 * 新增加一个定时任务</hr>
 * 
 * @param exec
 * @return
 * @throws SchedulerException
 */
	public static boolean addTaskJob(TaskOperation operation) throws SchedulerException {
		String jobName = operation.getName();
		String jobGroup = String.valueOf(operation.getId());
		if (checkExists(jobName, jobGroup)) {
			logger.info((">>>>>>>>> add scheduleJob fail, job already exist, jobName:{" + jobName + "}, jobGroup:{"
					+ jobGroup + "}"));
			return false;
		}

		String cronExpress = operation.getCronExpress();
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress);
		JobDetail jobDetail = JobBuilder.newJob(TaskOperationJob.class).withIdentity(jobName, jobGroup).build();
		// 记录下执行器，在定时任务执行的时候会取这个执行器，调用他的run方法。
		// OperationExector operationExector = new OperationExector(operation);
		jobDetail.getJobDataMap().put("operation", operation);
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder)
				.build();
		logger.info((">>>>>>>>> add scheduleJob success, jobName:{" + jobName + "}, jobGroup:{"
				+ jobGroup + "}"));
		Date date = trigger.getNextFireTime();
		if(null != date)
		{
			operation.setNextTime(new Timestamp(date.getTime()));
		}
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.error(">>>>>>>>>>> add scheduleJob error, jobName:{" + jobName + "}, jobGroup:{"+ jobGroup + "}",e);
			throw e;
		}
		return true;

	}

	// check if exists
	public static boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		return scheduler.checkExists(triggerKey);
	}

	/**
	 * 移除一个定时任务
	 * @param operation
	 * @return
	 * @throws SchedulerException
	 */
	public static boolean removeJob(TaskOperation operation) throws SchedulerException {
		String jobName = operation.getName();
		String jobGroup = String.valueOf(operation.getId());
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		boolean result = false;
		if (checkExists(jobName, jobGroup)) {
			try {
				result = scheduler.unscheduleJob(triggerKey);
			} catch (Exception e) {
				logger.error(">>>>>>>>>>> unschedule error, triggerKey:{" + triggerKey + "}, result [{" + result + "}]");
			}
			logger.info(">>>>>>>>>>>unschedule success, triggerKey:{" + triggerKey + "}, result [{" + result + "}]");
		}
		return result;
	}
	
	/**
	 * 立即触发一次
	 * @param operation
	 * @return
	 * @throws SchedulerException
	 */
	public static boolean triggerJob(TaskOperation operation) throws SchedulerException
	{
		String jobName = operation.getName();
		String jobGroup = String.valueOf(operation.getId());
		JobKey jobKey = new JobKey(jobName,jobGroup);
		if(!checkExists(jobName, jobGroup))
		{
			addTaskJob(operation);
		}
		
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			logger.error((">>>>>>>>> triggerJob error, jobName:{" + jobName + "}, jobGroup:{"+ jobGroup + "}"),e);
			throw e;
		}
		logger.info((">>>>>>>>> triggerJob success, jobName:{" + jobName + "}, jobGroup:{"+ jobGroup + "}"));
		return true;
	}

	/**
	 * 停止一个任务。</hr>
	 * 直接删除这个定时任务</hr>
	 * 因为使用scheduler.pauseJob(jobKey) 在回复后,如果中间触发过，会立即触发。
	 * @param operation
	 * @throws SchedulerException 
	 */
	public static void pauseJob(TaskOperation operation) throws SchedulerException
	{
		String jobName = operation.getName();
		String jobGroup = String.valueOf(operation.getId());
		//JobKey jobKey = new JobKey(jobName,jobGroup);
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		try {
			//scheduler.pauseJob(jobKey);
			scheduler.unscheduleJob(triggerKey);
		} catch (SchedulerException e) {
			logger.error(">>>>>>>>> pauseJob error,jobName:{" + jobName + "}, jobGroup:{"+ jobGroup + "}",e);
			throw e;
		}
		logger.info(">>>>>>>>> pauseJob success,jobName:{" + jobName + "}, jobGroup:{"+ jobGroup + "}");
	}
	
	/**
	 * 恢复一个停止的任务。
	 * @param operation
	 * @throws SchedulerException 
	 */
	public static void resumeJob(TaskOperation operation) throws SchedulerException
	{
		try {
			//scheduler.resumeJob(jobKey);
			addTaskJob(operation);
		} catch (SchedulerException e) {
			throw e;
		}
	}
	
	
	public static void loadSystemTask() throws SchedulerException {

		String jobName = "Log warn";
		String jobGroup = "666666";

		String cronExpress = TaskConfig.getPropertiesContext().get("log_warn_cron");
		if (!CronExpression.isValidExpression(cronExpress)) {
			logger.error("log warn cronExpress error, set to default 0 0 0/2 * * ? ");
			cronExpress = "0 0 0/2 * * ? ";
		}
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress);
		JobDetail jobDetail = JobBuilder.newJob(LogWarnJob.class).withIdentity(jobName, jobGroup).build();
		// 记录下执行器，在定时任务执行的时候会取这个执行器，调用他的run方法。
		// OperationExector operationExector = new OperationExector(operation);
		// jobDetail.getJobDataMap().put("operation", operation);
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder)
				.build();
		logger.info((">>>>>>>>> add scheduleJob success, jobName:{" + jobName + "}, jobGroup:{" + jobGroup + "}"));
		Date date = trigger.getNextFireTime();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.error(">>>>>>>>>>> add scheduleJob error, jobName:{" + jobName + "}, jobGroup:{" + jobGroup + "}",
					e);
			throw e;
		}

	}

}
