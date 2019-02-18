package com.nari.taskmanager.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.dao.TaskOperationDao;
import com.nari.taskmanager.dao.TaskStepDao;
import com.nari.taskmanager.po.TaskBoundStep;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskStep;
import com.nari.taskmanager.service.shell.ShellBuilderFactory;
import com.nari.taskmanager.service.shell.ShellCommandService;
import com.nari.taskmanager.service.shell.ShellExecDispacheService;


@Service
@Scope("prototype")
public class OperationExector implements Runnable ,BeanFactoryAware {
	private TaskOperation operation;
	private BeanFactory factory;
	@Autowired
	private  TaskOperationDao taskOperationDao;

	@Autowired
	private  TaskStepDao taskStepDao;
	
	@Autowired
	private  TaskManageService taskManageService;
	
	//@Autowired
	//private  ShellExecDispacheService shellExecDispacheService;
	
	@Autowired
	private  StateManageService stateManageService;
	
	@Autowired
	private  ShellCommandService shellCommandService;
	
	@Autowired
	private  TaskRunningConfigService taskRunningConfigService;

	private  Logger logger = Logger.getLogger(OperationExector.class);
	
	public OperationExector() {

	}
	public OperationExector(TaskOperation operation) {
		this.operation = operation;
		stateManageService.register(this);
	}

	/**
	 * 任务执行后先去StateManageService进行注册，注册后才能接受shell的返回值。</br>
	 * 任务结束后去StateManageService取消注册。</br>
	 */
	public void run() {
		int taskId = operation.getId();   
		Thread.currentThread().setName("task_thread_"+taskId);
		taskRunningConfigService.insertTaskLog(taskId,0,"127.0.0.1","info","start task taskName:"+operation.getName());
		logger.info("taskName:"+operation.getName()+" start!");
		operation.setResultDesc("分发任务");
 		taskOperationDao.updateTaskOperation(operation);
 		operation.setLastTime(new Timestamp(System.currentTimeMillis()));
 		taskRunningConfigService.insertTaskLog(taskId,0,"127.0.0.1","info","start dispacher task.");
 		//任务开始前先分发任务。
 		//ShellExecDispacheService shellExecDispacheService = new ShellExecDispacheService();
 		ShellExecDispacheService shellExecDispacheService = (ShellExecDispacheService)factory.getBean("shellExecDispacheService");
		shellExecDispacheService.exec(operation);
		if(operation.getState()==TaskConfig.TASK_STATE_ERROR){
			logger.error("run init script error!");
			taskRunningConfigService.insertTaskLog(taskId,0,"127.0.0.1","info","dispacher task error,stop the task.");
			taskOperationDao.updateTaskOperation(operation);
			stateManageService.unRegister(this);
			return;
		}
		else if(operation.getState()==TaskConfig.TASK_STATE_STOPPINF)
		{
			logger.info("stop task at step,stepName:"+"dispache task"+" stepId:"+0);
			stopTask(0);
			return;
		}
		
		taskRunningConfigService.insertTaskLog(taskId,0,"127.0.0.1","info","dispacher task success.");
		operation.setResultDesc("分发任务完成");
		taskOperationDao.updateTaskOperation(operation);
		logger.info("Start operate info [" + operation.toString() + "]");
		List<TaskStep> steps = operation.getSteps();
		List<TaskBoundStep> bounds = taskRunningConfigService.buildBoundStep(steps);
		TaskStep temp = null;
		try {
			for(TaskBoundStep bound : bounds)
			{
				int maxStepOrder = operation.getMaxStep();
				int currentStepOrder = bound.getBoundStepOrder();
				List<TaskStep> boundStep = bound.getSteps();
				for (TaskStep step : boundStep) {
					operation.setResultDesc("("+currentStepOrder+"/"+maxStepOrder+")正在执行");
					taskOperationDao.updateTaskOperation(operation);
					temp = step;
					step.setLastTime(new Timestamp(System.currentTimeMillis()));
					taskRunningConfigService.insertTaskLog(operation.getId(),step.getId(),"127.0.0.1","info","start step name:"+step.getName());
					
					//如果步骤状态为success，说明为软件正常运行时被停止，重启后跳过。
					if(step.getState() == TaskConfig.TASK_STATE_SUCCESS)
					{
						logger.info("start step, step status success!  stepName:"+step.getName()+" taskId:"+taskId+" stepId:"+step.getId());
						step.setResultDesc("执行成功");
						step.setPercent(100);
						taskStepDao.updateTaskStep(step);
						continue;
					}
 					else if (step.getState() == TaskConfig.TASK_STATE_RUNNING)//如果步骤状态为RUnning，说明为软件正常运行时被停止，重启后继续等待。
					{
						logger.info("start step, step status continue!  stepName:"+step.getName()+" taskId:"+taskId+" stepId:"+step.getId());
						step.setResultDesc("正在执行");
						taskStepDao.updateTaskStep(step);
					}
					//如果步骤状态为init，说明是由页面点击手动触发，
					else if(step.getState() == TaskConfig.TASK_STATE_INIT)
					{
						logger.info("start step !  stepName:"+step.getName()+" taskId:"+taskId+" stepId:"+step.getId());
						step.setState(TaskConfig.TASK_STATE_RUNNING);
						step.setResultDesc("开始执行");
						taskStepDao.updateTaskStep(step);
						// 调用shell脚本，执行脚本任务。
						ShellCommandService shellCommandService = (ShellCommandService)factory.getBean("shellCommandService");
						shellCommandService.setOperation(operation);
						shellCommandService.setShellKey(ShellBuilderFactory.STEP_SHELL);
						shellCommandService.setStep(step);
						Thread shellThread = new Thread(shellCommandService);
						shellThread.start();//
					}
					
				}
				
				waitForBoundStep(bound);
				
				for(TaskStep step : boundStep)
				{
					//在等待结束之后判断该步是否手动点击过停止后，如果点击过，直接返回。
					if(operation.getState()==TaskConfig.TASK_STATE_STOPPINF)
					{
						logger.info("stop task at step,stepName:"+step.getName()+" stepId:"+step.getId());
						stopTask(step.getStepOrder());
						return;
					}
					
					checkStepState(step);
					
					if (TaskConfig.TASK_STATE_SUCCESS != step.getState()) {
						setErrorMsg(step);
						stateManageService.unRegister(this);
						return;
					}
					step.setCostTime((System.currentTimeMillis() - step.getLastTime().getTime()) / 1000 + 1);
					step.setState(TaskConfig.TASK_STATE_SUCCESS);
					taskStepDao.updateTaskStep(step);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if (null != temp) {
				temp.setState(TaskConfig.TASK_STATE_ERROR);
				temp.setCostTime((System.currentTimeMillis() - temp.getLastTime().getTime()) / 1000 + 1);
				taskStepDao.updateTaskStep(temp);
			}
			operation.setState(TaskConfig.TASK_STATE_ERROR);
			operation.setCostTime((System.currentTimeMillis() - operation.getLastTime().getTime()) / 1000 + 1);
			taskOperationDao.updateTaskOperation(operation);
			stateManageService.unRegister(this);
			deleteZKTask();
			return;
		}
		
		operation.setState(TaskConfig.TASK_STATE_SUCCESS);
		operation.setResultDesc("执行成功！");
		operation.setCostTime((System.currentTimeMillis() - operation.getLastTime().getTime()) / 1000 + 1);
		taskOperationDao.updateTaskOperation(operation);
		stateManageService.unRegister(this);
		taskRunningConfigService.insertTaskLog(operation.getId(),0,"127.0.0.1","info","task success.");
	}

	/**
	 * stepOrder相同的步骤绑定为bound，只有当绑定为bound的步骤都完成时才算当前bound完成。
	 * @param bound
	 * @throws InterruptedException
	 */
	private void waitForBoundStep(TaskBoundStep bound) throws InterruptedException {
		List<TaskStep> steps =  bound.getSteps();
		List<Thread> threads = new ArrayList<Thread>();
		for(final TaskStep step : steps)
		{
			Thread t1 = new Thread(){
				@Override
				public void run() {
					synchronized (step) {
						logger.info("waiting for shell response ,taskId: " + operation.getId() + " step_order:"+step.getStepOrder() + "  ! " + step.getTimeOut() + "S");
						try {
							step.wait(step.getTimeOut() * 1000);
						} catch (InterruptedException e) {
							logger.error(e.getMessage(),e);
						}
						logger.info("waiting end, taskId: " + operation.getId() + " step_order:" +step.getStepOrder());
					}
				}
			};
			//t1.start();
			threads.add(t1);
		}
		
		for(Thread thread :threads)
		{
			thread.start();
			thread.join();
		}
	}
	private void stopTask(int stepOrder) {
		operation.setState(TaskConfig.TASK_STATE_STOPPED);
		int maxStepOrder = operation.getMaxStep();
		int currentStepOrder = stepOrder;
		operation.setResultDesc("("+currentStepOrder+"/"+maxStepOrder+")  stopped!");
		taskOperationDao.updateTaskOperation(operation);
		stateManageService.unRegister(this);
	}

	public TaskOperation getOperation() {
		return operation;
	}

	private void checkStepState(TaskStep step) {
		int maxStepOrder = operation.getMaxStep();
		int currentStepOrder = step.getStepOrder();
		int stepState = step.getState();
		logger.info("taskId:" + operation.getId());
		if (stepState == TaskConfig.TASK_STATE_INIT) {
			logger.info(String.format("operationname : %s , stepname : %s ,id : %s ，state: %s msg: %s",
					operation.getName(), step.getName(), operation.getId(), "未启动", step.getResultDesc()));
			step.setResultDesc("程序未启动！");
			taskRunningConfigService.insertTaskLog(operation.getId(),step.getId(),"127.0.0.1","info","step not start.  name:"+step.getName());
		} else if (stepState == TaskConfig.TASK_STATE_RUNNING) {
			logger.info(String.format("stepname : %s ，state: %s msg: %s", step.getName(), "超时", step.getResultDesc()));
			step.setResultDesc("执行超时！");
			operation.setResultDesc("("+currentStepOrder+"/"+maxStepOrder+") 执行超时！!");
			taskRunningConfigService.insertTaskLog(operation.getId(),step.getId(),"127.0.0.1","error","step out of time.  name:"+step.getName());
		} else if (stepState == TaskConfig.TASK_STATE_SUCCESS) {
			logger.info(String.format("stepname : %s ，state: %s msg: %s", step.getName(), "成功", step.getResultDesc()));
			step.setResultDesc("("+currentStepOrder+"/"+maxStepOrder+")执行成功！");
			taskRunningConfigService.insertTaskLog(operation.getId(),step.getId(),"127.0.0.1","info","step success.  name:"+step.getName());
		} else if (stepState == TaskConfig.TASK_STATE_ERROR) {
			logger.info(String.format("stepname : %s ，state: %s msg: %s", step.getName(), "错误", step.getResultDesc()));
			step.setResultDesc("("+currentStepOrder+"/"+maxStepOrder+")执行失败！");
			taskRunningConfigService.insertTaskLog(operation.getId(),step.getId(),"127.0.0.1","info","step error.  name:"+step.getName());
			taskRunningConfigService.insertTaskLog(operation.getId(),step.getId(),"127.0.0.1","info","step error. "+step.getResultDesc());
		} else  {
			logger.info(String.
					format("stepname : %s ，state: %s msg: %s", step.getName(), "未知错误", step.getResultDesc()));
			step.setResultDesc("("+currentStepOrder+"/"+maxStepOrder+")未知错误！");
			taskRunningConfigService.insertTaskLog(operation.getId(),step.getId(),"127.0.0.1","info","step error.  name:"+step.getName());
		}
	}

	public void setOperation(TaskOperation operation) {
		this.operation = operation;
	}

	private void setErrorMsg(TaskStep step) {
		step.setState(TaskConfig.TASK_STATE_ERROR);
		operation.setState(TaskConfig.TASK_STATE_ERROR);
		operation.setCostTime((System.currentTimeMillis() - operation.getLastTime().getTime()) / 1000 + 1);
		step.setCostTime((System.currentTimeMillis() - step.getLastTime().getTime()) / 1000 + 1);
		taskOperationDao.updateTaskOperation(operation);
		for(TaskStep nstep:operation.getSteps()){
			taskStepDao.updateTaskStep(nstep);
		}
	}

	/*	public TaskOperationDao getTaskOperationDao() {
		return taskOperationDao;
	}

	public static void setTaskOperationDao(TaskOperationDao taskOperationDao) {
		OperationExector.taskOperationDao = taskOperationDao;
	}


	public static void setTaskStepDao(TaskStepDao taskStepDao) {
		OperationExector.taskStepDao = taskStepDao;
	}

	public static void setTaskManageService(TaskManageService taskManageService) {
		OperationExector.taskManageService = taskManageService;
	}

	public static TaskRunningConfigService getTaskRunningConfigService() {
		return taskRunningConfigService;
	}
	public static void setTaskRunningConfigService(TaskRunningConfigService taskRunningConfigService) {
		OperationExector.taskRunningConfigService = taskRunningConfigService;
	}*/
	private void deleteZKTask()
	{
		if(operation.getType() == TaskConfig.TASK_TYPE_ZK){
			taskManageService.removeTask(operation);
		}
	}
	public void setTaskOperationDao(TaskOperationDao taskOperationDao) {
		this.taskOperationDao = taskOperationDao;
	}
	public void setTaskStepDao(TaskStepDao taskStepDao) {
		this.taskStepDao = taskStepDao;
	}
	public void setTaskManageService(TaskManageService taskManageService) {
		this.taskManageService = taskManageService;
	}
	public void setStateManageService(StateManageService stateManageService) {
		this.stateManageService = stateManageService;
	}
	public void setShellCommandService(ShellCommandService shellCommandService) {
		this.shellCommandService = shellCommandService;
	}
	public void setTaskRunningConfigService(TaskRunningConfigService taskRunningConfigService) {
		this.taskRunningConfigService = taskRunningConfigService;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.factory = beanFactory;
		
	}



}
