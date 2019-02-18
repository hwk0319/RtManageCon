package com.nari.taskmanager.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.dao.TaskLogDao;
import com.nari.taskmanager.dao.TaskOperationDao;
import com.nari.taskmanager.dao.TaskStepDao;
import com.nari.taskmanager.po.TaskLog;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskParam;
import com.nari.taskmanager.po.TaskStep;
import com.nari.taskmanager.quartz.TaskManagerSchedule;
import com.nari.taskmanager.threadpool.ThreadPoolExector;
import com.nari.taskmanager.util.TaskFileUtil;
import com.nari.taskmanager.util.ValidateUtil;
import com.nari.util.SysConstant;
import com.nari.util.Util;

@Service
public class TaskManageService implements BeanFactoryAware {
	
	private BeanFactory factory;
	
	@Autowired
	TaskStepDao taskStepDao;

	@Autowired
	public TaskOperationDao taskOperationDao;

	@Autowired
	TaskLogDao taskLogDao;
	
	@Resource(name="taskManagerSchedule") 
	TaskManagerSchedule taskManagerSchedule;

	@Autowired
	TaskRunningConfigService taskRunningConfigService;
	
	@Autowired
	StateManageService stateManageService;
	
	//@Resource(name="operationExector")
	//OperationExector operationExector;

	private static Logger logger = Logger.getLogger(TaskManageService.class);

	/**
	 * 系统启动时，从数据库加载所有任务到内存。
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {

	}
	/**
	 * 获得模板任务
	 * 
	 * @param operation
	 * @return
	 */
	public List<TaskOperation> getTaskTemplate(TaskOperation operation) {
		List<TaskOperation> operations = taskOperationDao.getTemplateTask(operation);
		return operations;
	}

	/**
	 * 删除一个任务模板
	 * 
	 * @param tplTaskId
	 */
	synchronized public JSONObject removeTemplateTask(TaskOperation operation) {

		taskOperationDao.removeTaskTemplate(operation);
		taskStepDao.removeTaskStepTpls(operation.getId());
		taskStepDao.removeTaskParamTpls(operation.getId());
		// 删除本地脚本文件
		String filePath = TaskConfig.TASK_TPL_BASE_PATH+operation.getId();
		try {
			TaskFileUtil.delDirOrFile(filePath);
		} catch (Exception e) {
			logger.error("delete taskTpl error,delete local file error!filePath:"+filePath);
		}
		logger.info("remove taskTemplate id:" + operation.getId());
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	/**
	 * 创建任务模板，根据用户的输入保存到数据库。
	 * </hr>
	 * 在本地硬盘创建相应的文件夹，用于保存脚本模板与参数模板。
	 * 
	 * @param taskTemplate
	 */
	public JSONObject addTaskTemplate(TaskOperation taskTemplate) {
		// 获取自增序列的值
		int taskID = taskOperationDao.getTemplateSeq(taskTemplate);

		taskTemplate.setId(taskID);
		// String basePath = TaskState.TASK_ROOT_PATH +
		// TaskState.TASK_TEMPLATE_PATH;
		// String taskTplDirName = taskTemplate.getId() + "";

		// 创建文件路径
		 TaskFileUtil.createDir(TaskConfig.TASK_TPL_BASE_PATH + taskID);
		// System.out.println("create template dir path:" + basePath +
		// taskTplDirName);
		// taskTemplate.setPath(basePath + taskTplDirName);

		taskOperationDao.insertTaskTemplate(taskTemplate);
		return buildReturnObject(TaskConfig.SUCCESS, taskTemplate);
	}

	public int getTaskTotalCount(TaskOperation operation) {
		int totalCount = taskOperationDao.getTaskTotalCount(operation);
		return totalCount;
	}

	public int getTaskTplTotalCount(TaskOperation operation) {
		int totalCount = taskOperationDao.getTaskTplTotalCount(operation);
		return totalCount;
	}

	/**
	 * 获得模板任务
	 * 
	 * @param operation
	 * @return
	 */
	public List<TaskOperation> getTask(TaskOperation operation) {
		List<TaskOperation> operations = taskOperationDao.getTask(operation);
		return operations;
	}

	/**
	 * 从模板表复制一个任务到value表， 包含step与param ，并设置一些初始状态
	 * 
	 * @param id
	 * @return taskId 创建后任务的taskId
	 * 
	 */
	synchronized public JSONObject creatTaskFromTemplate(TaskOperation task) {
		logger.info("creat task from tpl, tplTaskId:"+task.getId());
		TaskOperation taskOpera = new TaskOperation();
		int taskTplId = task.getId();
		TaskOperation operation;
		if (0 != taskTplId) {
			taskOpera.setId(taskTplId);
			List<TaskOperation> operations = getTaskTplWithStepAndParam(taskOpera);
			if (null == operations || operations.isEmpty() || operations.size() > 1) {
				return buildReturnObject(TaskConfig.ERROR, "未找到模板！,taskId:" + task.getId());
			}
			operation = operations.get(0);
		} else {
			// id=0 说明未选着模板，而是直接创建的
			operation = new TaskOperation();
		}

		int taskID = taskOperationDao.getOperationSeq(operation);
		operation.setId(taskID);
		operation.setName(task.getName());
		operation.setType(task.getType());
		operation.setPoDesc(task.getPoDesc());
		operation.setCronExpress(task.getCronExpress());
		operation.setState(TaskConfig.TASK_STATE_CREATING);
		operation.setCreateTime(new Timestamp(System.currentTimeMillis()));
		operation.setPath(TaskConfig.TASK_SHELL_BASE_PATH+taskID);
		operation.setUserId(taskID+"");
		operation.setPosition(task.getPosition());
		for (TaskStep step : operation.getSteps()) {
			step.setTaskId(operation.getId());
			step.setState(TaskConfig.TASK_STATE_CREATING);
			step.setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		//克隆脚本到新任务
		String sourcePath = TaskConfig.TASK_TPL_BASE_PATH+taskTplId;
		String destPath = TaskConfig.TASK_SHELL_BASE_PATH+taskID;
		
		try {
			TaskFileUtil.copyFolder(sourcePath, destPath);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error("copy tpl shell to task error!");
		}
		
		for (TaskParam param : operation.getParams()) {
			param.setTaskId(operation.getId());
		}
		taskOperationDao.insertTask(operation);

		List<TaskStep> steps = operation.getSteps();
		if (!steps.isEmpty()) {
			for(TaskStep step:steps)
			{
				taskStepDao.addTaskStep(step);
			}
		}
		List<TaskParam> params = operation.getParams();
		if (!params.isEmpty()) {
			for(TaskParam param:params)
			{
				taskStepDao.addTaskParam(param);
			}
		}
		JSONObject obj = new JSONObject();
		obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.SUCCESS);
		obj.put(TaskConfig.TASK_JSON_VALUE, taskID);
		return obj;
	}

	/**
	 * 修改value中的task的状态， 由0-creating 改为1-init 保存参数与脚本文件到本地。
	 * 
	 * @param id
	 * @return
	 */
	public JSONObject saveTaskFromCreating(TaskOperation taskOperation) {
		int id = taskOperation.getId();
		List<TaskOperation> operations = getTaskwithStepAndParam(taskOperation);
		if (null == operations || operations.isEmpty() || operations.size() > 1) {
			return buildReturnObject(TaskConfig.ERROR, "未找到任务 taskid:" + id);
		}
		TaskOperation operation = operations.get(0);
		operation.setState(TaskConfig.TASK_STATE_INIT);
		taskOperationDao.updateTaskOperation(operation);
		for(TaskStep step:operation.getSteps())
		{
			taskStepDao.updateTaskStep(step);
		}
		// 保存脚本文件与参数配置文件。
		try {
			TaskFileUtil.writeParamToFile(operation);
		} catch (Exception e) {
			logger.error("save param file error !",e);
			return buildReturnObject(TaskConfig.ERROR, e.getMessage());
		}

		// 如果为定时任务添加到定时任务。
		if (TaskConfig.TASK_TYPE_SCHEDULE == operation.getType()) {
			//OperationExector operationExector = new OperationExector(operation);
			try {
				TaskManagerSchedule.addTaskJob(operation);
			} catch (SchedulerException e) {
				logger.error("add schedule task error,"+e.getMessage(),e);
				return buildReturnObject(TaskConfig.ERROR, "定时任务启动失败!");
			}
		}

		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	/**
	 * 从任务列表克隆一个任务。
	 * @param taskId
	 * @param cloneType
	 * @return
	 * @throws Exception
	 */
	@Transactional(value="transactionManager",rollbackFor=java.lang.Exception.class)
	public TaskOperation cloneTask(int taskId, int cloneType) throws Exception {
		logger.info("clone task by taskId:" + taskId);
		TaskOperation sobj = new TaskOperation();
		sobj.setId(taskId);
		List<TaskOperation> operations = getTaskwithStepAndParam(sobj);
		if (!ValidateUtil.checkNotEmpty(operations)) {
			logger.error("can not find task taskID:" + taskId);
			throw new Exception("task not found!");
		}
		TaskOperation operation = operations.get(0);
		int newId = taskOperationDao.getOperationSeq(operation);
		operation.setPresetId(taskId);
		operation.setUserId("");
		operation.setId(newId);
		// 设置克隆后的任务状态为初始状态,定时任务在后面设置为stopped。
		operation.setState(TaskConfig.TASK_STATE_INIT);
		operation.setCreateTime(new Timestamp(System.currentTimeMillis()));
		operation.setResultDesc("");
		operation.setPercent(0);
		operation.setLastTime(null);
		operation.setCloneType(cloneType);
		operation.setPath(TaskConfig.TASK_SHELL_BASE_PATH+newId);
		operation.setType(TaskConfig.TASK_TYPE_NORMAL);
		List<TaskStep> steps = operation.getSteps();
		for (TaskStep step : steps) {
			step.setTaskId(newId);
			step.setState(TaskConfig.TASK_STATE_INIT);
			step.setCreateTime(new Timestamp(System.currentTimeMillis()));
			step.setResultDesc("");
			step.setPercent(0);
		}

		List<TaskParam> params = operation.getParams();
		for (TaskParam param : params) {
			param.setTaskId(newId);
		}

		// 如果为定时任务设置任务状态为已停止。
		if (TaskConfig.TASK_TYPE_SCHEDULE == operation.getType()) {
			operation.setState(TaskConfig.TASK_STATE_STOPPED);
		}

		// 保存克隆后任务到数据库
		taskOperationDao.insertTask(operation);
		if (ValidateUtil.checkNotEmpty(steps)) {
			for(TaskStep step:steps)
			{
				taskStepDao.addTaskStep(step);
			}
		}
		if (ValidateUtil.checkNotEmpty(params)) {
			for(TaskParam param : params)
			{
				taskStepDao.addTaskParam(param);
			}
		}

		// update克隆后的步骤的shellPath，
		TaskStep step = new TaskStep();
		step.setTaskId(newId);
		steps = taskStepDao.getTaskStep(step);
		operation.setSteps(steps);
		operation.setParams(params);
		//保存参数到本地
		TaskFileUtil.writeParamToFile(operation);
		//复制脚本到新任务
		String sourcePath = TaskConfig.TASK_SHELL_BASE_PATH+taskId;
		String destPath = TaskConfig.TASK_SHELL_BASE_PATH+newId;
		try {
			TaskFileUtil.copyFolder(sourcePath, destPath);
		} catch (Exception e) {
			removeTask(operation);
			throw e;
		}
		return operation;
	}
	
	/**
	 * 从任务模板克隆一个任务。<br>
	 * 克隆后的任务为普通任务.<br>
	 * @param taskTplId
	 * @param cloneType
	 * @return
	 * @throws Exception
	 */
	public TaskOperation cloneTaskFormTaskTemplate(int taskTplId, int cloneType) throws Exception
	{
		logger.info("clone task from taskTemplate by taskId:" + taskTplId);
		TaskOperation sobj = new TaskOperation();
		sobj.setId(taskTplId);
		List<TaskOperation> operations = getTaskTplWithStepAndParam(sobj);
		if (!ValidateUtil.checkNotEmpty(operations)) {
			logger.error("can not find taskTemplate taskTplId:" + taskTplId);
			throw new Exception("未找到任务模板!");
		}
		TaskOperation operation = operations.get(0);
		int newId = taskOperationDao.getOperationSeq(operation);
		operation.setPresetId(taskTplId);
		operation.setId(newId);
		
		// 设置克隆后的任务状态为初始状态,定时任务在后面设置为stopped。
		operation.setState(TaskConfig.TASK_STATE_INIT);
		operation.setCreateTime(new Timestamp(System.currentTimeMillis()));
		operation.setResultDesc("");
		operation.setPercent(0);
		operation.setLastTime(null);
		operation.setCloneType(cloneType);
		operation.setType(TaskConfig.TASK_TYPE_NORMAL);
		operation.setPath(TaskConfig.TASK_SHELL_BASE_PATH+newId);
		operation.setPosition(Util.getInOrOut());
		List<TaskStep> steps = operation.getSteps();
		for (TaskStep step : steps) {
			step.setTaskId(newId);
			step.setState(TaskConfig.TASK_STATE_INIT);
			step.setCreateTime(new Timestamp(System.currentTimeMillis()));
			step.setResultDesc("");
			step.setPercent(0);
		}

		List<TaskParam> params = operation.getParams();
		for (TaskParam param : params) {
			param.setTaskId(newId);
		}

		// 保存克隆后任务到数据库
		taskOperationDao.insertTask(operation);
		if (ValidateUtil.checkNotEmpty(steps)) {
			for(TaskStep step:steps){
				taskStepDao.addTaskStep(step);
			}
		}
		if (ValidateUtil.checkNotEmpty(params)) {
			for(TaskParam param : params)
			{
				taskStepDao.addTaskParam(param);
			}
		}

		// update克隆后的步骤的shellPath，
		TaskStep step = new TaskStep();
		step.setTaskId(newId);
		steps = taskStepDao.getTaskStep(step);
		operation.setSteps(steps);
		operation.setParams(params);

		//保存参数到本地
		TaskFileUtil.writeParamToFile(operation);
		//复制脚本到新任务
		String sourcePath = TaskConfig.TASK_TPL_BASE_PATH+taskTplId;
		String destPath = TaskConfig.TASK_SHELL_BASE_PATH+newId;
		TaskFileUtil.copyFolder(sourcePath, destPath);
		return operation;
	}
	
	/**
	 * 删除一个已经创建完成的任务。 同时删除其关联的步骤与参数。
	 * 
	 * @param opera
	 * @return
	 */
	synchronized public JSONObject removeTask(TaskOperation opera) {
		List<TaskOperation> ops = taskOperationDao.getTask(opera);
		if (!ValidateUtil.checkNotEmpty(ops)) {
			logger.warn("remove taskOperation error,the task not exist,  taskId:" + opera.getId());
		}
		// TaskOperation operation = ops.get(0);
		for (TaskOperation operation : ops) {
			// 对于stopping的强制从内存中删除,
			TaskOperation runningOperation = StateManageService.getRegisterTaskByTaskId(opera.getId());
			if (null != runningOperation) {
				// OperationExector operationExector =
				// (OperationExector)applicationContext.getBean("operationExector");
				OperationExector operationExector = (OperationExector) factory.getBean("operationExector");
				logger.warn("the task is still running force to remove it,taskId:" + opera.getId() + " taskName:"
						+ runningOperation.getName());
				stateManageService.notifyExector(runningOperation);
				stateManageService.unRegister(operationExector);
			}
			// 从数据库删除-================================
			taskOperationDao.removeTask(operation);
			taskStepDao.removeTaskSteps(operation.getId());
			taskStepDao.removeTaskParams(operation.getId());
			taskLogDao.clearTaskLog(operation.getId());
			// ==============================================
			logger.info("remove taskOperation taskId:" + operation.getId() + " name:" + operation.getName());

			// 删除任务文件夹
			String taskPath = TaskConfig.TASK_SHELL_BASE_PATH + operation.getId();
			if (0 == operation.getId() || TaskConfig.TASK_SHELL_BASE_PATH.equals(taskPath)) {
				logger.warn("remove task,task File path is null,maybe not created success.");
			} else {
				try {
					TaskFileUtil.delDirOrFile(taskPath);
					logger.info("delete local file success. taskId+" + operation.getId() + " taskName:"
							+ operation.getName());
				} catch (Exception e) {
					logger.error("delete file " + taskPath + " fail!");
					logger.error(e.getMessage());
				}
			}

			// 普通任务到这里已经删除成功，定时任务还需要从定时任务中删除。
			if (operation.getType() == TaskConfig.TASK_TYPE_SCHEDULE) {
				try {
					TaskManagerSchedule.removeJob(operation);
					logger.info("remove task,remove schedule success.");
				} catch (SchedulerException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		logger.info("remove task success." + opera.toString());
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	/**
	 * 普通任务-- 手动启动一个普通任务。</br>
	 * 启动后设置任务的状体为running，设置Step的状态为init。进度设为0，描述设为空。</br>
	 * 会清空以前的日志。</br>
	 * 
	 * 定时任务--</br>
	 * 状态试设置与普通任务相同，同时会立即触发。</br>
	 * 
	 * @param taskOperation
	 * @param fitted
	 *            参数是否已经装配</br>
	 *            true-已经装配，直接运行。</br>
	 *            false-所传的参数只包含基本查询信息，需要从数据库获取。</br>
	 * @return "status" : "value" :
	 */
	synchronized public JSONObject startTask(TaskOperation taskOperation, boolean fitted) {
		TaskOperation operation = taskOperation;
		if (!fitted) {
			// 1--校验任务状态
			try {
				operation = getTaskwithStepAndParam(taskOperation).get(0);
			} catch (IndexOutOfBoundsException e1) {
				logger.error("can not find the task. taskId"+taskOperation.getId());
				return buildReturnObject(TaskConfig.ERROR, "未找到任务！");
			}
		}
		if (!ValidateUtil.checkNotEmpty(operation.getSteps())) {
			logger.info("the step num of the task is 0");
			return buildReturnObject(TaskConfig.ERROR, "任务为空,无法正常启动.");
		}
		
		
		setTaskInitState(operation);
		// 2--设置任务启动状态，RUNNING percen--》0
		operation.setState(TaskConfig.TASK_STATE_RUNNING);
		operation.setLastTime(new Timestamp(System.currentTimeMillis()));
		taskOperationDao.updateTaskOperation(operation);
		// taskStepDao.updateTaskSteps(operation.getSteps());
		for (TaskStep step : operation.getSteps()) {
			taskStepDao.updateTaskStep(step);
		}
		logger.info("start task id:" + operation.getId() + " name:" + operation.getName());
		//OperationExector operationExector = (OperationExector)applicationContext.getBean("operationExector");
		OperationExector operationExector = (OperationExector)factory.getBean("operationExector");
		operationExector.setOperation(operation);
		stateManageService.register(operationExector);
		//OperationExector operationExector = new OperationExector(operation);
		ThreadPoolExector.fixedThreadPool.execute(operationExector);
		return buildReturnObject(TaskConfig.SUCCESS, operation.getId());
	}
	
	synchronized public JSONObject continueTask(TaskOperation operation) {
		logger.info("continue task id:" + operation.getId() + " name:" + operation.getName());
		//OperationExector operationExector = new OperationExector(operation);
		//OperationExector operationExector = (OperationExector)applicationContext.getBean("operationExector");
		OperationExector operationExector = (OperationExector)factory.getBean("operationExector");
		operationExector.setOperation(operation);
		stateManageService.register(operationExector);
		ThreadPoolExector.fixedThreadPool.execute(operationExector);
		return buildReturnObject(TaskConfig.SUCCESS, operation.getId());
	}

	/**
	 * 根据模板taskId获取，该模板的Param
	 * @param id
	 * @return
	 */
	public List<TaskParam> getTaskParamTemplate(TaskParam taskParam) {
		List<TaskParam> templateParams = taskStepDao.getTaskParamTemplate(taskParam);
		return templateParams;
	}

	public List<TaskStep> getTaskStepTemplat(TaskStep stepTemplate) {
		List<TaskStep> templateSteps = taskStepDao.getTaskStepTemplate(stepTemplate);
		// 获取脚本文件
		try {
			for (TaskStep step : templateSteps) {
				String shellText;
				if (step.getShellName() != null && !"".equals(step.getShellName())) {
					shellText = TaskFileUtil.getStepTplShell(step);
					if(null == shellText){
						step.setShellTxt("");
					}else{
						step.setShellTxt(shellText);
					}
						
				}
			}
		} catch (IOException e) {
			logger.error("get step tpl shell file error!");
		}
		return templateSteps;
	}
	
	public List<TaskStep> getTaskStepTemplatWithOutShell(TaskStep stepTemplate) {
		List<TaskStep> templateSteps = taskStepDao.getTaskStepTemplate(stepTemplate);
		return templateSteps;
	}

	public JSONObject addTaskStepTemplate(TaskStep stepTemplate) {
		logger.info("add taskstep template :"+stepTemplate.getName());
		if(stepTemplate.getStepOrder() == 0)
		{
			logger.error("can not add a tasktemplate with steoOrder 0!");
			return buildReturnObject(TaskConfig.ERROR, "步骤号不能为0");
		}
		taskStepDao.saveTaskStepTemplate(stepTemplate);
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	public JSONObject addTaskParamTemplate(TaskParam taskParamTemplate) {
		logger.info("add taskpara template :"+taskParamTemplate.getName());
		taskStepDao.saveTaskParamTemplate(taskParamTemplate);
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	public JSONObject removeTaskStepTemplate(TaskStep stepTemplate) {
		logger.info("remove taskStepTemplate:"+stepTemplate.getName());
		taskStepDao.removeTaskStepTemplate(stepTemplate);
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	public JSONObject updateTaskOperationTemplate(TaskOperation operationTpl) {
		logger.info("update taskoperaitontemplate:"+operationTpl.getName());
		taskOperationDao.updateTaskOperationTemplate(operationTpl);
		return buildReturnObject(TaskConfig.SUCCESS, "");

	}

	/**
	 * 修改一个步骤模板，如果有修改脚本，会保存脚本到本地。
	 * @param stepTemplateTpl
	 * @return
	 */
	public JSONObject updateTaskStepTemplate(TaskStep stepTemplateTpl) {
		logger.info("update taskstep template:"+stepTemplateTpl.getName());
		if(stepTemplateTpl.getStepOrder() == 0)
		{
			logger.error("can not update a tasktemplate with steoOrder 0!");
			return buildReturnObject(TaskConfig.ERROR, "步骤号不能为0");
		}
		//保存修改到数据库
		taskStepDao.updateTaskStepTemplate(stepTemplateTpl);
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	public JSONObject updateTaskParamTemplate(TaskParam paramTemplate) {
		logger.info("update template param:"+paramTemplate.getName());
		taskStepDao.updateTaskParamTemplate(paramTemplate);
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	public JSONObject removeTaskParamTemplate(TaskParam paramTemplate) {
		logger.info("remove template param "+paramTemplate.getName());
		taskStepDao.removeTaskParamTemplate(paramTemplate);
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	/**
	 * 增加一个步骤。
	 * @param step
	 * @return
	 */
	public JSONObject addTaskStep(TaskStep step) {
		step.setCreateTime(new Timestamp(System.currentTimeMillis()));
		//step.setShellPath(String.valueOf(step.getId()));
		if(step.getStepOrder() ==0){
			logger.error("can not add a step with stepOrder 0!");
			return buildReturnObject(TaskConfig.ERROR, "步骤号不能为0");
		}
		taskStepDao.addTaskStep(step);
		logger.info("add task step success, stepName:"+step.getName());
		return returnSuccess();
	}

	/**
	 * 新增一个参数到任务。<br>
	 * 会增加到数据库。
	 * @param param
	 * @return
	 */
	public JSONObject addTaskParam(TaskParam param) {
		logger.info("add param " + param.toString());
		taskStepDao.addTaskParam(param);
		return returnSuccess();
	}

	public JSONObject updateTaskStep(TaskStep step) {
		logger.info("edit shellscript, stepId:" + step.getId());
		if(step.getStepOrder() ==0){
			logger.error("cannot update stepOrder to 0! stepId:"+step.getId());
			return buildReturnObject(TaskConfig.ERROR, "步骤号不能为0");
		}
		taskStepDao.updateTaskStep(step);
		
		return returnSuccess();
	}

	public JSONObject updateTaskParam(TaskParam param) {
		logger.info("update param " + param.toString());
		taskStepDao.updateTaskParam(param);
		return returnSuccess();
	}

	public JSONObject removeTaskStep(TaskStep step) {
		logger.info("remove taskStep ,stepId" + step.getId());
		taskStepDao.removeTaskStep(step);
		TaskStep reStep = null;
		
		TaskOperation searchOperation = new TaskOperation();
		searchOperation.setId(step.getTaskId());
		TaskOperation taskOperation = getTask(searchOperation).get(0);
		try {
			List<TaskStep> steps = taskStepDao.getTaskStep(step);
			if(steps.size() > 0 && steps != null){
				reStep = steps.get(0);
				if (null != reStep.getShellPath()) {
					TaskFileUtil.delDirOrFile(TaskConfig.TASK_SHELL_BASE_PATH + taskOperation.getId()+File.separator+reStep.getShellName());
				}
			}
		} catch (Exception e) {
			logger.error("cannt remove shellScript file, stepId:" + step.getId() + " shellFile:"
					+ TaskConfig.TASK_SHELL_BASE_PATH + reStep.getTaskId()+File.separator+reStep.getShellName());
			logger.error(e.getMessage(), e);
		}
		logger.info("step remove success, id:" + step.getId());
		return returnSuccess();
	}

	/**
	 * 删除指定的参数
	 * 
	 * @param param
	 * @return
	 */
	public JSONObject removeTaskParam(TaskParam param) {
		logger.info("remove param " + param.toString());
		taskStepDao.removeTaskParam(param);
		return returnSuccess();
	}
	
	/**
	 * 删除一个任务下的所有参数项</br>
	 * 会从数据库删除。
	 * @param taskId
	 * @return
	 */
	public JSONObject removeTaskParams(int taskId)
	{
		taskStepDao.removeTaskParams(taskId);
		logger.info("remove all params by taskId" + taskId);
		return returnSuccess();
	}
	

	/*
	 * 根据taskId 从数据库template表，组装一个Task
	 */
	List<TaskOperation> getTaskTplWithStepAndParam(TaskOperation taskOperation) {
		List<TaskOperation> operations = taskOperationDao.getTemplateTask(taskOperation);
		if (null == operations & operations.isEmpty()) {
			return null;
		} else {
			for (TaskOperation operation : operations) {
				int id = operation.getId();
				TaskStep taskStep = new TaskStep();
				taskStep.setTaskId(id);
				List<TaskStep> steps = taskStepDao.getTaskStepTemplate(taskStep);
				TaskParam taskParam = new TaskParam();
				taskParam.setTaskId(id);
				List<TaskParam> params = taskStepDao.getTaskParamTemplate(taskParam);

				operation.setSteps(steps);
				operation.setParams(params);
			}
			return operations;
		}
	}

	/**
	 * 从数据库获取完整的任务。
	 * 
	 * @param id
	 * @return
	 */
	public List<TaskOperation> getTaskwithStepAndParam(TaskOperation taskOperation) {
		List<TaskOperation> operations = taskOperationDao.getTask(taskOperation);
		if (null == operations & operations.isEmpty()) {
			return null;
		} else {
			for (TaskOperation operation : operations) {
				int id = operation.getId();
				TaskStep taskStep = new TaskStep();
				taskStep.setTaskId(id);
				List<TaskStep> steps = taskStepDao.getTaskStep(taskStep);
				TaskParam taskParam = new TaskParam();
				taskParam.setTaskId(id);
				List<TaskParam> params = taskStepDao.getTaskParam(taskParam);
				operation.setSteps(steps);
				operation.setParams(params);
			}
			return operations;
		}
	}

	public List<TaskStep> getTaskStep(TaskStep taskStep) {
		List<TaskStep> steps = taskStepDao.getTaskStep(taskStep);
		try {
			for (TaskStep step : steps) {
				String shellText = TaskFileUtil.getStepShell(step);
				if(null == shellText){
					step.setShellTxt("");
				}else{
					step.setShellTxt(shellText);
				}
			}
		} catch (IOException e) {
			logger.error("get step shell error! stepId:"+taskStep.getId());
		}
		return steps;
	}

	public List<TaskParam> getTaskParam(TaskParam taskParam) {
		List<TaskParam> params = taskStepDao.getTaskParam(taskParam);
		for(TaskParam param :params)
		{
			if(null != param.getName() && param.getValue() != null)
			{
				if(param.getName().toLowerCase().contains("passwd") ||param.getName().toLowerCase().contains("password"))
				{
					String p = "";
					String[] value = param.getValue().split(",");
					for(int i=0;i<value.length;i++)
					{
						p += "******,";
					}
					param.setValue(p);
				}
			}
		}
		return params;
	}

	/**
	 * 上传文件分四种情况。
	 * </hr>
	 * 1-shellTemplate
	 * </hr>
	 * 步骤模板的脚本文件，解析脚本文件保存到模板数据库，覆盖之前的记录。
	 * </hr>
	 * 2-shell
	 * </hr>
	 * 步骤任务的脚本文件，解析脚本文件保存到任务数据库，覆盖之前的记录。
	 * </hr>
	 * 3-paramTemplate
	 * </hr>
	 * </hr>
	 * 4-param
	 * </hr>
	 * </hr>
	 * 
	 * @param id
	 * @param upLoadType
	 * @param file
	 * @return
	 */
	public JSONObject uploadFile(int id, String upLoadType, String filetype, MultipartFile file) {
		try {
			if (TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM_TPL.equals(upLoadType)) {
				// 模板的参数
				if(checkFileType(filetype, file)){
					return parseParamFile(id, TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM_TPL, filetype, file);
				}else{
					return buildReturnObject(TaskConfig.ERROR, SysConstant.FILEUPLOAD_FILE);
				}
			} else if (TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM.equals(upLoadType)) {
				// 任务的参数
				if(checkFileType(filetype, file)){
					return parseParamFile(id, TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM, filetype, file);
				}else{
					return buildReturnObject(TaskConfig.ERROR, SysConstant.FILEUPLOAD_FILE);
				}
			} else if (TaskConfig.TASK_FILEUPLOAD_TYPE_SHELL.equals(upLoadType)) {
				// 任务的脚本
				if(checkFileType(filetype, file)){
					return parseShellFile(id, TaskConfig.TASK_FILEUPLOAD_TYPE_SHELL, file);
				}else{
					return buildReturnObject(TaskConfig.ERROR, SysConstant.FILEUPLOAD_FILE);
				}
			} else if (TaskConfig.TASK_FILEUPLOAD_TYPE_SHELL_TPL.equals(upLoadType)) {
				// 模板的脚本
				if(checkFileType(filetype, file)){
					return parseShellFile(id, TaskConfig.TASK_FILEUPLOAD_TYPE_SHELL_TPL, file);
				}else{
					return buildReturnObject(TaskConfig.ERROR, SysConstant.FILEUPLOAD_FILE);
				}
			} else {
				return buildReturnObject(TaskConfig.ERROR, "Error,无法解析文件类型。");
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return buildReturnObject(TaskConfig.ERROR, "Error,文件解析失败！");
	}

	/**
	 * 前台传送过来的文件直接保存到本地。
	 * </hr>
	 * 模板文件保存到模板文件夹下。
	 * </hr>
	 * 任务文件保存到任务文件加下。
	 * </hr>
	 * 
	 * @param id
	 *            对应数据库表中的ID，每个脚本文件自己的唯一ID，非TaskId.
	 * @param fileType
	 * @param file
	 */
	private JSONObject parseShellFile(int id, String upLoadType, MultipartFile file) {
		//TO DO
		
		String fileName = file.getOriginalFilename();
		// 1--解析文件
		InputStream inputStream = null;
		StringBuilder shellTxt = new StringBuilder();
		try {
			inputStream = file.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			while ((line = reader.readLine()) != null) {
				shellTxt.append(line + TaskConfig.LINESEPARATOR);
			}

		} catch (IOException e) {
			logger.error(e);
			return buildReturnObject(TaskConfig.ERROR, "读取文件异常");
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 2--保存文件
		if (TaskConfig.TASK_FILEUPLOAD_TYPE_SHELL.equals(upLoadType)) {
			// 任务页面上传的脚本保存到 step的任务 数据库。
			TaskStep step = new TaskStep();
			step.setId(id);
			List<TaskStep> steps = taskStepDao.getTaskStep(step);
			if (null == steps || steps.isEmpty() || steps.size() > 1) {
				return buildReturnObject(TaskConfig.ERROR, "任务步骤异常！");
			}
			step = steps.get(0);
			step.setShellName(fileName);
			step.setShellTxt(shellTxt.toString().replaceAll("\r", ""));
			updateTaskStep(step);
			
			TaskOperation searchOperation = new TaskOperation();
			searchOperation.setId(step.getTaskId());
			TaskOperation taskOperation = getTask(searchOperation).get(0);
			
			try {
				TaskFileUtil.savefile(TaskConfig.TASK_SHELL_BASE_PATH +taskOperation.getId() + File.separator + fileName,
						shellTxt.toString());
				logger.info("save shellFile success, stepId:" + step.getId() + " fileName:" + fileName);
			} catch (Exception e) {
				logger.error("save shellFile error, " + step.toString());
				logger.error(e.getMessage(), e);
			}

		} else if (TaskConfig.TASK_FILEUPLOAD_TYPE_SHELL_TPL.equals(upLoadType)) {
			// 模板页面上传的脚本保存到 step的模板 数据库。
			TaskStep stepTpl = new TaskStep();
			stepTpl.setId(id);
			List<TaskStep> steps = taskStepDao.getTaskStepTemplate(stepTpl);
			if (null == steps || steps.isEmpty() || steps.size() > 1) {
				return buildReturnObject(TaskConfig.ERROR, "模板步骤异常！");
			}
			stepTpl = steps.get(0);
			stepTpl.setShellTxt(shellTxt.toString());
			stepTpl.setShellName(fileName);
			
			try {
				updateTaskStepTemplate(stepTpl);
			} catch (Exception e) {
				return buildReturnObject(TaskConfig.ERROR, e.getMessage());
			}
			//保存脚本
			TaskOperation searchOperation = new TaskOperation();
			searchOperation.setId(stepTpl.getTaskId());
			TaskOperation taskOperation = getTaskTemplate(searchOperation).get(0);
			try {
				TaskFileUtil.savefile(TaskConfig.TASK_TPL_BASE_PATH +taskOperation.getId() + File.separator + fileName,
						shellTxt.toString());
				logger.info("save tpl shellFile success, tplStepId:" + stepTpl.getId() + " fileName:" + fileName);
			} catch (Exception e) {
				logger.error("save tpl shellFile error, tplStepId:" + stepTpl.toString());
				logger.error(e.getMessage(), e);
			}
		} else {

		}
		return returnSuccess();

	}

	/**
	 * 解析参数的配置。 包含参数文件与脚本文件。
	 * 
	 * @param id
	 *            为模板或任务的taskId
	 * @param upLoadType
	 * @param fileType
	 *            文件类型ini或sql
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private JSONObject parseParamFile(int id, String upLoadType, String fileType, MultipartFile file)
			throws IOException {
		if (TaskConfig.UPLOAD_SUPPORT_TYPE_INI.equals(fileType)) {
			return paserParamFile(id, upLoadType, file);
		} else {
			return parseSQLFile(id, upLoadType, file);
		}

	}

	private JSONObject paserParamFile(int id, String upLoadType, MultipartFile file) throws IOException {
		Properties prop = new Properties();
		List<TaskParam> params = new ArrayList<TaskParam>();
		prop.load(file.getInputStream());
		for (Entry<Object, Object> entry : prop.entrySet()) {
			TaskParam param = new TaskParam();
			param.setName((String) entry.getKey());
			param.setValue((String) entry.getValue());
			param.setTaskId(id);
			params.add(param);
		}

		if (TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM_TPL.equals(upLoadType)) {
			// 1--校验是否有重复
			TaskParam paramTpl = new TaskParam();
			paramTpl.setTaskId(id);
			List<TaskParam> existParams = taskStepDao.getTaskParamTemplate(paramTpl);
			StringBuffer sb = new StringBuffer();
			for (TaskParam existParam : existParams) {
				for (TaskParam param : params) {
					if (param.getName().equals(existParam.getName())) {
						sb.append("name:" + param.getName()).append("\n\r");
					}
				}
			}

			if (sb.length() > 0) {
				return buildReturnObject(TaskConfig.ERROR, "数据有重复：\n\r" + sb.toString());
			}

			// 2--保存数据到数据库
			for (TaskParam param : params) {
				taskStepDao.saveTaskParamTemplate(param);
			}

		} else if (TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM.equals(upLoadType)) {
			// 1--校验是否有重复
			TaskParam taskParam = new TaskParam();
			taskParam.setTaskId(id);
			List<TaskParam> existParams = taskStepDao.getTaskParam(taskParam);
			StringBuffer sb = new StringBuffer();
			for (TaskParam existParam : existParams) {
				for (TaskParam param : params) {
					if (param.getName().equals(existParam.getName())) {
						sb.append("name:" + param.getName()).append("\n\r");
					}
				}
			}

			if (sb.length() > 0) {
				return buildReturnObject(TaskConfig.ERROR, "数据有重复：\n\r" + sb.toString());
			}

			// 2--保存数据到数据库
			for (TaskParam param : params) {
				taskStepDao.addTaskParam(param);
			}

		} else {

		}
		return returnSuccess();
	}

	private JSONObject parseSQLFile(int id, String upLoadType, MultipartFile file) throws IOException {
		byte[] buf = file.getBytes();
		String sbf = new String(buf);
		TaskOperation taskOperation = new TaskOperation();
		taskOperation.setId(id);
		if (TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM.equals(upLoadType)) {
			TaskOperation task = taskOperationDao.getTask(taskOperation).get(0);
			task.setSql(sbf.toString());
			taskOperationDao.updateTaskOperation(task);
			return returnSuccess();
		} else if (TaskConfig.TASK_FILEUPLOAD_TYPE_PARAM_TPL.equals(upLoadType)) {
			TaskOperation task = taskOperationDao.getTemplateTask(taskOperation).get(0);
			task.setSql(sbf.toString());
			taskOperationDao.updateTaskOperationTemplate(taskOperation);
			return returnSuccess();
		}

		return returnError();
	}

	public JSONObject updateTaskStepShellTemplateTxt(TaskStep stepTemplate) {
		logger.info("update taskstep shellScript stepId:" + stepTemplate.getId());
		List<TaskStep> steps = taskStepDao.getTaskStepTemplate(stepTemplate);
		if (steps == null || steps.isEmpty() || steps.size() > 1) {
			logger.error("step invalide step count error, step.id=" + stepTemplate.getId());
			return buildReturnObject(TaskConfig.ERROR, "步骤异常！");
		}
		TaskStep myStepTemplate = steps.get(0);
		//taskStepDao.updateTaskStepTemplate(myStepTemplate);
		try {
			String shellFilePath = TaskConfig.TASK_TPL_BASE_PATH+ myStepTemplate.getTaskId() + File.separator
					+ myStepTemplate.getShellName();
			TaskFileUtil.savefile(shellFilePath, stepTemplate.getShellTxt());
			return returnSuccess();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("save the tpl shell file to local error! taskId:"+myStepTemplate.getTaskId()+" tplStepId:"+myStepTemplate.getId()+" shellName:"+myStepTemplate.getShellName());
		}
		return returnSuccess();
	}

	/**
	 * 去数据库修改一个Operation的信息，不会修改其状态与percent。</br>
	 * 只修改名称描述等这些在页面上修改的内容。</br>
	 * @param operation
	 * @return
	 * @throws Exception 
	 */
	public JSONObject updateTaskOperation(TaskOperation operation) throws Exception {
		TaskOperation t = new TaskOperation();
		t.setId(operation.getId());
		List<TaskOperation> operations = taskOperationDao.getTask(t);
		if(!ValidateUtil.checkNotEmpty(operations)){
			logger.error("update taskoperation error,cannot find task id:"+operation.getId());
			throw new Exception("can not find taskOperation.");
		}
		TaskOperation task = taskOperationDao.getTask(t).get(0);
		operation.setState(task.getState());
		operation.setPercent(task.getPercent());
		taskOperationDao.updateTaskOperation(operation);
		return returnSuccess();
	}

	/**
	 * 校验步骤模板的step号。
	 * 
	 * @param stepTemplate
	 * @return
	 */
	public JSONObject checkStepTplOrder(TaskStep stepTemplate) {
		int newStepOrder = stepTemplate.getStepOrder();
		List<TaskStep> stepTpls = taskStepDao.getTaskStepTemplate(stepTemplate);
		for (TaskStep stepTpl : stepTpls) {
			if (stepTpl.getStepOrder() == newStepOrder) {
				logger.info("check stepTplOrder Error, duplicate ,stepOrder:" + newStepOrder);
				return buildReturnObject(TaskConfig.ERROR, "步骤号冲突:" + newStepOrder);
			}
		}
		return returnSuccess();
	}

	/**
	 * 校验任务步骤的step号。
	 * 
	 * @param stepTemplate
	 * @return
	 */
	public JSONObject checkStepOrder(TaskStep step) {

		int newStepOrder = step.getStepOrder();
		List<TaskStep> steps = taskStepDao.getTaskStep(step);
		for (TaskStep taskStep : steps) {
			if (taskStep.getStepOrder() == newStepOrder) {
				logger.info("check stepTplOrder Error, duplicate ,stepOrder:" + newStepOrder);
				return buildReturnObject(TaskConfig.ERROR, "步骤号冲突:" + newStepOrder);
			}
		}

		return returnSuccess();
	}

	public JSONObject checkParamTpl(TaskParam newParam) {
		String newParamName = newParam.getName();
		TaskParam param = new TaskParam();
		param.setTaskId(newParam.getTaskId());
		List<TaskParam> params = taskStepDao.getTaskParamTemplate(param);
		for (TaskParam oldParam : params) {
			if (newParamName.equals(oldParam.getName())) {
				logger.info("check paramName Error, duplicate ,paramName:" + newParamName + " taskId:"
						+ newParam.getTaskId());
				return buildReturnObject(TaskConfig.ERROR, "参数名称冲突:" + newParamName);
			}
		}

		return returnSuccess();
	}

	public JSONObject checkParam(TaskParam newParam) {
		String newParamName = newParam.getName();
		TaskParam param = new TaskParam();
		param.setTaskId(newParam.getTaskId());
		List<TaskParam> params = taskStepDao.getTaskParam(param);
		for (TaskParam oldParam : params) {
			if (newParamName.equals(oldParam.getName())) {
				logger.info("check paramName Error, duplicate ,paramName:" + newParamName + " taskId:"
						+ newParam.getTaskId());
				return buildReturnObject(TaskConfig.ERROR, "参数名称冲突:" + newParamName);
			}
		}
		return returnSuccess();
	}


	/**
	 * 停止一个任务。 如果为普通任务，则在当前任务步骤执行完成后停止。
	 * </hr>
	 * 如果为定时任务
	 * 如果正在运行，则在当前任务步骤执行完成后停止运行，如果没有运行，会停止定时，且在下次重新启动时不会重新启动定时（任务还在，时间到了不会运行，
	 * 除非恢复）。
	 * </hr>
	 * 
	 * @param id
	 * @return
	 */
	synchronized public JSONObject stopTask(TaskOperation taskOperation) {
		OperationExector exec = StateManageService.registedOperationMap.get(taskOperation.getId());

		List<TaskOperation> operations = taskOperationDao.getTask(taskOperation);
		if (null != operations && !operations.isEmpty() ) {
			TaskOperation opera = operations.get(0);
			// 正在运行的任务
			if (null != exec) {
				exec.getOperation().setState(TaskConfig.TASK_STATE_STOPPINF);
				opera.setState(TaskConfig.TASK_STATE_STOPPINF);
			} else {
				opera.setState(TaskConfig.TASK_STATE_STOPPED);
			}
			taskOperationDao.updateTaskOperation(opera);
			int taskType = opera.getType();
			if (TaskConfig.TASK_TYPE_SCHEDULE == taskType) {
				try {
					TaskManagerSchedule.pauseJob(opera);
				} catch (SchedulerException e) {
					return buildReturnObject(TaskConfig.ERROR, "停止定时任务失败！");
				}
			}
			return buildReturnObject(TaskConfig.SUCCESS, "");
		}

		return buildReturnObject(TaskConfig.ERROR, "未找到任务！taskId:" + taskOperation.getId());
	}

	/**
	 * 恢复一个定时任务。</br>
	 * 
	 * @return
	 */
	public JSONObject resumeScheduleJob(TaskOperation taskOperation) {
		List<TaskOperation> operas = getTaskwithStepAndParam(taskOperation);
		if (null != operas && !operas.isEmpty()) {
			TaskOperation opera = operas.get(0);
			if (opera.getType() == TaskConfig.TASK_TYPE_SCHEDULE) {
				if(opera.getState() != TaskConfig.TASK_STATE_STOPPED)
				{
					return buildReturnObject(TaskConfig.ERROR, "恢复定时任务失败,只能恢复停止状态的任务。");
				}
				opera.setState(TaskConfig.TASK_STATE_INIT);
				for (TaskStep step : opera.getSteps()) {
					step.setState(TaskConfig.TASK_STATE_INIT);
				}
				taskOperationDao.updateTaskOperation(opera);
				for(TaskStep step:opera.getSteps())
				{
					taskStepDao.updateTaskStep(step);
				}
				try {
					//OperationExector operationExector = new OperationExector(opera);
					TaskManagerSchedule.resumeJob(opera);
				} catch (SchedulerException e) {
					return buildReturnObject(TaskConfig.ERROR, "恢复定时任务失败！");
				}
				return buildReturnObject(TaskConfig.SUCCESS, "");
			}
			return buildReturnObject(TaskConfig.ERROR, "只能恢复定时任务！");
		}
		return buildReturnObject(TaskConfig.ERROR, "未找到该定时任务！");
	}

	protected JSONObject buildReturnObject(String status, Object value) {
		JSONObject obj = new JSONObject();
		obj.put(TaskConfig.TASK_JSON_STATUS, status);
		obj.put(TaskConfig.TASK_JSON_VALUE, value);
		return obj;
	}

	public JSONObject returnSuccess() {
		return buildReturnObject(TaskConfig.SUCCESS, "");
	}

	public JSONObject returnError() {
		return buildReturnObject(TaskConfig.ERROR, "未知错误！");
	}

	/**
	 * 增加一个任务到数据库。 如果任务为定时任务，那么在创建后就以生效（到时间就会触发）。
	 * 
	 * @param taskOperation
	 */
	public JSONObject addTask(TaskOperation taskOperation) {
		int taskID = taskOperationDao.getOperationSeq(taskOperation);
		taskOperation.setId(taskID);
		taskOperation.setCreateTime(new Timestamp(System.currentTimeMillis()));
		taskOperation.setState(TaskConfig.TASK_STATE_INIT);
		taskOperation.setPresetId(0);
		taskOperation.setPercent(0);
		taskOperation.setPath(TaskConfig.TASK_SHELL_BASE_PATH+taskID);
		taskOperation.setCloneType(TaskConfig.TASK_STRATEGY_ALL_PARALLEL);
		taskOperation.setUserId(taskID+"");
		for (TaskStep step : taskOperation.getSteps()) {
			step.setId(taskID);
			step.setState(TaskConfig.TASK_STATE_INIT);
		}

		for (TaskParam param : taskOperation.getParams()) {
			param.setId(taskID);
		}

		// 保存脚本参数配置文件。
		try {
			TaskFileUtil.writeParamToFile(taskOperation);
		} catch (IOException e1) {
			logger.error(e1.getMessage(), e1);
			return buildReturnObject(TaskConfig.ERROR, "保存参数文件到本地失败");
		}
		// 新增加的任务在设置好初始参数后入库保存。
		taskOperationDao.insertTask(taskOperation);
		// 保存到内存
		// TaskCache.put(taskOperation.getId(), taskOperation);
		// 普通任务到这里已经配置结束。
		// 如果任务为定时任务，还需要把任务添加到定时任务中。
		if (TaskConfig.TASK_TYPE_SCHEDULE == taskOperation.getType()) {
			//OperationExector operationExector = new OperationExector(taskOperation);
			try {
				TaskManagerSchedule.addTaskJob(taskOperation);
				taskOperationDao.updateTaskOperation(taskOperation);
			} catch (SchedulerException e) {
				logger.error(e.getMessage(), e);
				return buildReturnObject(TaskConfig.ERROR, e.getMessage());
			}
		}
		return returnSuccess();
	}

	/**
	 * 设置启动任务前任务的初始状态。
	 * 
	 * @param operation
	 */
	public void setTaskInitState(TaskOperation operation) {
		// 2--设置日志
		int taskId = operation.getId();
		// 清空之前的日志
		taskRunningConfigService.clearTaskLog(taskId);
		for (TaskStep step : operation.getSteps()) {
			step.setResultDesc("等待执行");
			step.setPercent(0);
			step.setState(TaskConfig.TASK_STATE_INIT);
		}
		operation.setState(TaskConfig.TASK_STATE_INIT);
		operation.setPercent(0);
		operation.setResultDesc("");
	}

	public JSONObject saveStepShellFile(TaskStep step) {
		List<TaskStep> steps = taskStepDao.getTaskStep(step);
		if (!ValidateUtil.checkNotEmpty(steps) || steps.size() > 1) {
			logger.error("get task error, task:" + step.toString());
			return returnError();
		}
		TaskStep oldStep = steps.get(0);
		oldStep.setShellTxt(step.getShellTxt());
		step = oldStep;
		taskStepDao.updateTaskStep(step);
		logger.info("save task step shell file. taskId:" + step.getTaskId() + " stepName:" + step.getName()
				+ " shellName:" + step.getShellName());
		
		TaskOperation searchOperation = new TaskOperation();
		searchOperation.setId(step.getTaskId());
		TaskOperation taskOperation = getTask(searchOperation).get(0);
		
		try {
			String shellFilePath = TaskConfig.TASK_SHELL_BASE_PATH+ taskOperation.getId() + File.separator
					+ step.getShellName();
			TaskFileUtil.savefile(shellFilePath, step.getShellTxt());
			return returnSuccess();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("save the shell file to local error! taskId:"+taskOperation.getId()+" stepId:"+step.getId()+" shellName:"+step.getShellName());
		}
		return buildReturnObject(TaskConfig.ERROR, "保存文件失败");
	}

	public JSONObject updateParamFile(TaskParam param) {
		logger.info("edit paramFile paramId:" + param.getId());
		List<TaskParam> params = taskStepDao.getTaskParam(param);
		if (!ValidateUtil.checkNotEmpty(params)) {
			logger.error("get params error, param:" + param.toString());
			return returnError();
		}
		TaskOperation opera = new TaskOperation();
		opera.setId(param.getTaskId());
		try {
			TaskOperation operation = getTaskwithStepAndParam(opera).get(0);
			TaskFileUtil.writeParamToFile(operation);
			return returnSuccess();
		} catch (IndexOutOfBoundsException e) {
			logger.error("cannot find taskOperation taskId:" + opera.getId());
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return buildReturnObject(TaskConfig.ERROR, "保存参数到本地失败");
	}

	public JSONObject cloneTaskOperationTemplate(TaskOperation operation) {
		logger.info("clone task template ,taskId:" + operation.getId());
		JSONObject obi = new JSONObject();
		TaskOperation oldOperationTpl = getTaskTplWithStepAndParam(operation).get(0);
		int newTaskId = taskOperationDao.getTemplateSeq(oldOperationTpl);
		oldOperationTpl.setId(newTaskId);
		taskOperationDao.insertTaskTemplate(oldOperationTpl);
		for(TaskStep step:oldOperationTpl.getSteps())
		{
			step.setTaskId(newTaskId);
			taskStepDao.saveTaskStepTemplate(step);
		}
		
		for(TaskParam param: oldOperationTpl.getParams())
		{
			param.setTaskId(newTaskId);
			taskStepDao.saveTaskParamTemplate(param);
		}
		
		//复制脚本文件到新模板
		String sourcePath = TaskConfig.TASK_TPL_BASE_PATH+operation.getId();
		String destPath = TaskConfig.TASK_SHELL_BASE_PATH+newTaskId;
		try {
			TaskFileUtil.copyFolder(sourcePath, destPath);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("clone task template error!,copy shell file error!");
			return buildReturnObject(TaskConfig.ERROR, "克隆模板文件失败");
		}
		
		obi.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.SUCCESS);
		obi.put(TaskConfig.TASK_JSON_VALUE, newTaskId);
		return obi;
	}

	public List<TaskLog> getTaskLogByTaskId(int taskId) {
		return taskLogDao.getTaskLogByTaskId(taskId);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.factory = beanFactory;
		
	}

	/**
	 * 验证上传的文件类型是否和文件参数类型一致，一致返回true，不一致返回false
	 * @return
	 */
	public boolean checkFileType(String filetype, MultipartFile file){
		boolean res = false;
		String fileName = file.getOriginalFilename();
		if(filetype.equals("sh")){
			fileName = fileName.substring(fileName.length()-2, fileName.length());
		}else if(filetype.equals("sql")){
			fileName = fileName.substring(fileName.length()-3, fileName.length());
		}else if(filetype.equals("ini")){
			fileName = fileName.substring(fileName.length()-3, fileName.length());
		}
		if(fileName.equals(filetype)){
			res = true;
		}
		return res;
	}
	public List<TaskOperation> searchTotal(TaskOperation po) {
		return taskOperationDao.searchTotal(po);
	}
	public List<TaskOperation> search1(TaskOperation po) {
		// TODO Auto-generated method stub
		return taskOperationDao.search1(po);
	}

}
