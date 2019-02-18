package com.nari.taskmanager.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.dao.TaskOperationDao;
import com.nari.taskmanager.dao.TaskStepDao;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskResult;
import com.nari.taskmanager.po.TaskStep;

/**
 * 任务运行状态监控中心。 任务在启动时进行注册，同时把任务的状态修改为初始状态。</br>
 * 任务在运行时接受返回消息，根据返回消息修改任务状态,并且去数据库修改状态。</br>
 * 
 * @author wanghe/2017年5月5日
 *
 */
@Service
public class StateManageService {

	@Autowired
	TaskOperationDao taskOperationDao;
	
	@Autowired
	TaskStepDao taskStepDao;
	/**
	 * 保存所有正在执行的exector
	 */
	protected static ConcurrentHashMap<Integer, OperationExector> registedOperationMap = new ConcurrentHashMap<Integer, OperationExector>();
	protected static Map<String,Map<String,String>> processValueMap= new HashMap<String,Map<String,String>>();
	
	private static Logger logger = Logger.getLogger(StateManageService.class);

	/**
	 * 用户在界面上点击启动之后，注册运行任务。并把任务状态置为初始化。
	 * 
	 * @param exector
	 */
	protected  void register(OperationExector exector) {
		TaskOperation operation = exector.getOperation();
		registedOperationMap.put(operation.getId(), exector);
		logger.info("register   task [ task_id:" + operation.getId() + " ]");
	}

	protected  void unRegister(OperationExector exector) {
		TaskOperation operation = exector.getOperation();
		int taskID = operation.getId();
		registedOperationMap.remove(taskID);
		logger.info("unregister task [ task_id:" + taskID + " ]");
	}

	protected  void unRegister(int taskId) {
		registedOperationMap.remove(taskId);
		logger.info("unregister task [ task_id:" + taskId + " ]");
	}

	/**
	 * 校验当前任务是否已经执行。<br>
	 * 
	 * @param operation<br>
	 * @return <br>
	 *         true 正在运行。<br>
	 *         false 没有运行。<br>
	 */
	public static boolean checkTheTaskIsRunning(TaskOperation operation) {
		if (operation.getCloneType() == TaskConfig.TASK_STRATEGY_USER_PARALLEL) {
			if (null == getRegisterTaskByUserId(operation.getUserId())) {
				return false;
			} else {
				return true;
			}
		} else {
			if (null == getRegisterTaskByTaskId(operation.getId())) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 根据任务创建者在注册的任务中查找任务<br>
	 * 如果找到返回，taskOperation。</br>
	 * 如果没有找到返回null</br>
	 * 
	 * @param operationName
	 * @return
	 */
	public static TaskOperation getRegisterTaskByUserId(String suid) {
		for (Entry<Integer, OperationExector> entry : registedOperationMap.entrySet()) {
			TaskOperation operation = ((OperationExector) entry.getValue()).getOperation();
			String uid = operation.getUserId();
			if (null != uid && suid.equals(uid)) {
				return operation;
			}
		}
		return null;
	}

	/**
	 * 根据任务创建者在注册的任务中查找任务<br>
	 * 如果找到返回，taskOperation。</br>
	 * 如果没有找到返回null</br>
	 * 
	 * @param operationName
	 * @return
	 */
	public static TaskOperation getRegisterTaskByTaskId(int staskId) {
		for (Entry<Integer, OperationExector> entry : registedOperationMap.entrySet()) {
			TaskOperation operation = ((OperationExector) entry.getValue()).getOperation();
			int taskId = operation.getId();
			if (taskId == staskId) {
				return operation;
			}
		}
		return null;
	}

	public  void notifyExector(TaskStep step) {
		synchronized (step) {
			step.notify();
		}
	}

	public  void notifyExector(TaskOperation operation) {
		synchronized (operation) {
			for (TaskStep step : operation.getSteps()) {
				if (step.getState() == TaskConfig.TASK_STATE_RUNNING) {
					notifyExector(step);
				}
			}
			operation.notify();
		}
	}

	/**
	 * 此方法供shell脚本回调。 根据shell返回的参数，设置任务状态，并通知任务继续执行。
	 * 
	 * @param result
	 * @throws Exception
	 */
	public void dealShellResult(TaskResult result) {

		TaskOperation operation = null;
		TaskStep step = null;
		try {
			operation = getResultOperation(result);
			step = getResultStep(result);
		} catch (Exception e) {
			logger.error("deal shell result error!");
			return;
		}

		// 处理init的服务，初始化的一些脚本。
		// stepId=0 说明是没有开始步骤之前的操作，也就是任务分发的操作。
		if (result.getStepId() == 0) {
			dealTaskDispacherResult(result.getMsgType(), operation);
			return;
		}

		// 开始步骤之后
		if (TaskConfig.TASK_SHELL_RESULT_RUNNING == result.getMsgType()) {
			dealTaskRunningResult(operation, step, result);
		} else if (TaskConfig.TASK_SHELL_RESULT_SUCCESS == result.getMsgType()) {
			dealTaskSuccessResult(operation, step, result);
		} else if (TaskConfig.TASK_SHELL_RESULT_EXCEPTION == result.getMsgType()) {
			dealTaskErrorResult(operation, step, result);
		} else if (TaskConfig.TASK_SHELL_RESULT_VALUE == result.getMsgType()) {
			dealTaskResultValue(operation, step, result);
		} else if (TaskConfig.TASK_SHELL_RESULT_OPERA == result.getMsgType()) {
			dealTaskResultCheck(operation, step, result);
		}
	}

	private  TaskStep getResultStep(TaskResult result) throws Exception {
		TaskOperation operation = getResultOperation(result);
		for (TaskStep step : operation.getSteps()) {
			if (step.getId() == result.getStepId()) {
				return step;
			}
		}
		return null;
	}

	private  TaskOperation getResultOperation(TaskResult result) throws Exception {
		OperationExector operationExec = registedOperationMap.get(result.getTaskId());
		if (null == operationExec) {
			//高可用环境下，如果切换到另一台主机，重新加载任务。
			logger.error("invalid msg, the task maybe not running, detial:" + result.toString());
			throw new Exception("cannot find task exception!");
		}
		TaskOperation operation = operationExec.getOperation();
		return operation;
	}

	/*public static void setTaskOperationDao(TaskOperationDao taskOperationDao) {
		StateManageService.taskOperationDao = taskOperationDao;
	}

	public static void setTaskStepDao(TaskStepDao taskStepDao) {
		StateManageService.taskStepDao = taskStepDao;
	}*/

	/**
	 * 处理脚本分发任务的结果</br>
	 * 在脚本步骤开始之前，会先分发脚本到各主机。</br>
	 * 分发脚本的步骤的stepId为0</br>
	 * 
	 * @param msgType
	 * @param operation
	 */
	private void dealTaskDispacherResult(int msgType, TaskOperation operation) {
		if (msgType == 2) {
			logger.info("get dispacher task success msg! taskId:" + operation.getId() + " taskName:" + operation.getName());
			int waitHostNumber = operation.getDstHostNum() - 1;
			operation.setDstHostNum(waitHostNumber);
			if (waitHostNumber > 0) // 如果还有主机未成功，直接返回，不通知operation执行。
			{
				operation.setResultDesc("任务分发执行主机数:" + waitHostNumber);
				taskOperationDao.updateTaskOperation(operation);
				return;
			}
			operation.setResultDesc("任务分发成功.");
			taskOperationDao.updateTaskOperation(operation);
		} else if (msgType == 3) {
			logger.error("task dispacher eror! taskId:" + operation.getId() + " taskName:" + operation.getName());
			operation.setResultDesc("任务分发错误！");
			operation.setState(TaskConfig.TASK_STATE_ERROR);
			taskOperationDao.updateTaskOperation(operation);
		}
		logger.info("task dispacher end ! notify task continue, taskId:" + operation.getId() + " taskName:" + operation.getName());
		notifyExector(operation);
		return;
	}

	private void dealTaskRunningResult(TaskOperation operation, TaskStep step, TaskResult result) {
		step.setPercent(result.getPercent());
		step.setResultDesc(result.getMsg());
		int maxStepOrder = operation.getMaxStep();
		int currentStepOrder = step.getStepOrder();
		operation.setResultDesc("(" + currentStepOrder + "/" + maxStepOrder + ") " + result.getMsg());
		taskOperationDao.updateTaskOperation(operation);
		taskStepDao.updateTaskStep(step);
	}

	/**
	 * 处理成功的返回</br>
	 * 设置状态，通知step继续。</br>
	 * 
	 * 对于一个步骤在多个主机上运行的情况，当最后一个主机返回成功才算成功。</br>
	 * 
	 * @param operation
	 * @param step
	 * @param result
	 */
	private void dealTaskSuccessResult(TaskOperation operation, TaskStep step, TaskResult result) {
		int waitHostNumber = operation.getDstHostNum() - 1;
		operation.setDstHostNum(waitHostNumber);
		logger.info("the taskStep taskId:" + operation.getId() + " wating node response num " + waitHostNumber);
		if (waitHostNumber <= 0) {
			step.setPercent(result.getPercent());
			step.setResultDesc(result.getMsg());
			int maxStepOrder = operation.getMaxStep();
			int currentStepOrder = step.getStepOrder();
			operation.setResultDesc("(" + currentStepOrder + "/" + maxStepOrder + ") " + result.getMsg());
			step.setState(TaskConfig.TASK_STATE_SUCCESS);

			taskOperationDao.updateTaskOperation(operation);
			taskStepDao.updateTaskStep(step);

			logger.info("task success notify continue. taskName:" + operation.getName() + " taskId:" + operation.getId() + " stepId:" + step.getId() + " stepName:" + step.getName());
			notifyExector(step);
		}

	}

	/**
	 * 处理错误的返回。
	 * 
	 * @param operation
	 * @param step
	 * @param result
	 */
	private void dealTaskErrorResult(TaskOperation operation, TaskStep step, TaskResult result) {
		step.setPercent(result.getPercent());
		step.setResultDesc(result.getMsg());
		int maxStepOrder = operation.getMaxStep();
		int currentStepOrder = step.getStepOrder();
		operation.setResultDesc("(" + currentStepOrder + "/" + maxStepOrder + ") " + result.getMsg());
		step.setState(TaskConfig.TASK_STATE_ERROR);
		operation.setState(TaskConfig.TASK_STATE_ERROR);
		taskOperationDao.updateTaskOperation(operation);
		taskStepDao.updateTaskStep(step);
		logger.info("task error notify continue. taskName:" + operation.getName() + " taskId:" + operation.getId() + " stepId:" + step.getId() + " stepName:" + step.getName());
		notifyExector(step);
	}
	/**
	 * 缓存脚本的返回值
	 * @param operation
	 * @param step
	 * @param result
	 */
	private void dealTaskResultValue(TaskOperation operation, TaskStep step, TaskResult result) {
		String[] values = result.getMsg().split(":");
		logger.info("cache shell return value:"+values);
		if(values.length != 2)
		{
			logger.error("get msg from shell error,msgType:4,"+ result.getMsg());
			result.setMsg("获取脚本返回值错误!");
			dealTaskErrorResult(operation,step,result);
		}
		if(null == processValueMap.get(operation.getId()+"")){
			Map<String,String> map = new HashMap<String,String>();
			processValueMap.put(operation.getId()+"",map);
		}
		processValueMap.get(operation.getId()+"").put(values[0], values[1]);
	}
	
	
	/**
	 * 缓存脚本的返回值
	 * @param operation
	 * @param step
	 * @param result
	 */
	private void dealTaskResultCheck(TaskOperation operation, TaskStep step, TaskResult result) {
		String value = result.getMsg();
		logger.info("check shell msg,  "+value);
		if(value.equals("redoThread:check")){
			//数据库切换switchover
			Map<String,String> map = processValueMap.get(operation.getId()+"");
			if(map.size()==0 || map.size()%2 != 0)
			{
				logger.error("get msg from shell error, msg num is "+map.size());
				result.setMsg("获取脚本返回值错误!");
				dealTaskErrorResult(operation,step,result);
				return;
			}
			for(int i=1;i<=map.size()/2;i++){
				if(!map.get("primaryRedo-"+i).equals(map.get("standbyRedo-"+i))){
					//logger.error("get msg from shell error, msg num is "+map.size());
					logger.error("the archived_log  sequence is not same.");
					result.setMsg("数据库redo未完成，请在redo完成后再执行。");
					dealTaskErrorResult(operation,step,result);
					return;
				}
			}
			result.setMsg("备机检测通过。");
			result.setPercent(100);
			dealTaskSuccessResult(operation,step,result);
		} else if(value.equals("change:check")){
			//数据库切换switchover
			Map<String,String> map = processValueMap.get(operation.getId()+"");
			if(map.size()==0 || map.size()%2 != 0)
			{
				logger.error("get msg from shell error, msg num is "+map.size());
				result.setMsg("获取脚本返回值错误!");
				dealTaskErrorResult(operation,step,result);
				return;
			}
			
			try {
				int pri_first = Integer.valueOf(map.get("pri_first_change"));
				int pri_next = Integer.valueOf(map.get("pri_next_change"));
				int sta_first = Integer.valueOf(map.get("sta_first_change"));
				int sta_next = Integer.valueOf(map.get("sta_next_change"));
				if(pri_first <= 0 || pri_next<= 0 || sta_first<= 0 || sta_next<=0)
				{
					logger.error("the value of first_change or next_chang can not be 0.");
					result.setMsg("SQL: select max(first_change#),max(next_change#) from v$archived_log 所得值不能为0.");
					dealTaskErrorResult(operation,step,result);
					return;
				}
				
				if(pri_first != sta_first || pri_next != sta_next){
					logger.error("the value of first_change or next_chang should be same by the primary and stadby.");
					logger.error(String.format("pri_first:%s pri_next:%s sta_first:%s sta_next:%s",pri_first,pri_next,sta_first,sta_next));
					result.setMsg("主备归档SCN号不一致.");
					dealTaskErrorResult(operation,step,result);
					return;
				}
			} catch (NumberFormatException e) {
				logger.error("get value by SQL \' select max(first_change#),max(next_change#) from v$archived_log \' error");
				logger.error(e.getMessage(),e);
				result.setMsg("SQL: select max(first_change#),max(next_change#) from v$archived_log 所得值为大于0的整数.");
				dealTaskErrorResult(operation,step,result);
				return;
			}
			
			result.setMsg("备机检测通过。");
			result.setPercent(100);
			dealTaskSuccessResult(operation,step,result);
		}else{
			logger.error("can not find the cache msg by msgvalue:"+value);
			result.setMsg("the shell return msg error:check the ");
			dealTaskErrorResult(operation,step,result);
			return;
		}
			
	}
	
}
