package com.nari.taskmanager.quartz;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.service.OperationExector;
import com.nari.taskmanager.service.TaskManageService;

/**
 * 定时任务,需要手动注入taskManageService。</br>
 * operation为创建定时任务时就实例化好的。</br>
 * @author admin
 *
 */
@Component
@DisallowConcurrentExecution
public class TaskOperationJob implements Job {
	private static Logger logger = Logger.getLogger(TaskOperationJob.class);
	private static TaskManageService taskManageService ;
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail detail = context.getJobDetail();
		Date date = context.getNextFireTime();
		JobDataMap jodDataMap = detail.getJobDataMap();
		TaskOperation operation = (TaskOperation) jodDataMap.get("operation");
		TaskOperation newTask= null;
		try {
			newTask = taskManageService.cloneTask(operation.getId(), TaskConfig.TASK_STRATEGY_ALL_PARALLEL);
		} catch (Exception e) {
			logger.error("schedule task clone new task error! taskId:"+operation.getId()+" taskName:"+operation.getName());
			operation.setResultDesc("创建任务失败");
			try {
				taskManageService.updateTaskOperation(operation);
			} catch (Exception e1) {
			}
			return;
		}
		newTask.setName(operation.getName()+"_schedule_clone");
		operation.setNextTime(new Timestamp(date.getTime()));
		if(TaskConfig.TASK_STATE_RUNNING == operation.getState()){
			logger.error("error start schedule task,the task is running!");
			return;
		}else{
			taskManageService.setTaskInitState(newTask);
		}
		taskManageService.startTask(newTask, true);
	}
	
	public static void setTaskManageService(TaskManageService service )
	{
		taskManageService = service;
	}
}
