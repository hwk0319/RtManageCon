package com.nari.taskmanager.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nari.module.doublemgt.po.DoublemgtPo;
import com.nari.module.group.po.GroupPo;
import com.nari.taskmanager.config.PresetTaskConfig;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.dao.TaskExecDao;
import com.nari.taskmanager.po.TaskExecParam;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskParam;
import com.nari.taskmanager.util.TaskFileUtil;
import com.nari.taskmanager.util.ValidateUtil;

/**
 * 页面触发任务执行服务类。</br>
 * 
 * @author wanghe
 */
@Service
public class PreSetTaskExecService {
	private static Logger logger = Logger.getLogger(PreSetTaskExecService.class);
	@Resource(name = "taskExecDao")
	private TaskExecDao taskExecDao;
	@Autowired
	private TaskManageService taskManageService;

	/**
	 * 数据库双机切换(switch over)<br> 如果数据库组rac在做dg的话，选择其中一个主机操作即可。<br>
	 * 根据双活中心组的ID,获取组中设备IP,密码。然后克隆一个任务启动。</br> @param primaryId
	 * 双活主节点的group_id @param standbyId 双活备节点的group_id @param doubleId
	 * 双活的Id。 @return
	 * 
	 * @exception
	 * 
	 */
	synchronized public JSONObject OracleDgSwitch(int primaryId, int standbyId, int doubleId) {
		logger.info("switch oracle dg. primaryGroupId: " + primaryId + " standbyGroupId: " + standbyId + " doubleId:" + doubleId);
		// 初始化参数。
		List<TaskExecParam> primaryParams = taskExecDao.getDeviceInfoByGroupId(new TaskExecParam(primaryId));
		List<TaskExecParam> standbyParams = taskExecDao.getDeviceInfoByGroupId(new TaskExecParam(standbyId));
		

		if (!(ValidateUtil.checkNotEmpty(primaryParams) || ValidateUtil.checkNotEmpty(standbyParams))) {
			logger.error("the params num 0, double oracle param number error,primaryUid:" + primaryId + " standbyUid" + standbyId);
			logger.error("run oracle dg switch error!");
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常");
			return obj;
		}
		
		if(primaryParams.size() != standbyParams.size())
		{
			logger.error("the host num is not equals.");
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "双机主机数量不一致");
			return obj;
		}
		
		// 如果主机信息有多个，说明组了rac，获取其中一个主机的信息即可。
		TaskExecParam primaryHostInfo = primaryParams.get(0);
		logger.info("the primary group device num:" + primaryParams.size() + "the selected device uid:" + primaryHostInfo.getUid());
		TaskExecParam standbyHostInfo = standbyParams.get(0);
		logger.info("the standby group device num:" + standbyParams.size() + "the selected device uid:" + standbyHostInfo.getUid());
		
		//选择用于操作的节点后，其他节点的信息保存到 otherNodeInfo
		List<TaskExecParam> otherNodeInfo = new ArrayList<TaskExecParam>();
		if(primaryParams.size()>1){
			for(int i=1;i<primaryParams.size();i++)
			{
				otherNodeInfo.add(primaryParams.get(i));
			}
		}
		
		if(standbyParams.size()>1){
			for(int i=1;i<standbyParams.size();i++)
			{
				otherNodeInfo.add(standbyParams.get(i));
			}
		}
		//如果其他节点信息数量大于零，克隆任务100004，单机切换 100000
		int cloneTaskTemplateId = PresetTaskConfig.ORACLE_DG_SWITCH_TASKID;
		if(otherNodeInfo.size()>0){
			cloneTaskTemplateId = PresetTaskConfig.ORACLE_RDG_SWITCH_TASKID;
		}
		
		// 双活信息
		DoublemgtPo doubleLivePo = taskExecDao.getDoubleLivePo(new TaskExecParam(doubleId));
		String doubleLiveName = doubleLivePo.getName();
		String doubleAliveUid = doubleLivePo.getUid();
		if (!(ValidateUtil.isNullString(doubleAliveUid) || ValidateUtil.isNullString(doubleLiveName))) {
			logger.error("the double live msg in db error! doubleId：" + doubleId + " uid:" + doubleAliveUid + " name:" + doubleLiveName);
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常");
			return obj;
		}

		// 1,判断当前任务是否已经注册执行。
		if (null != StateManageService.getRegisterTaskByUserId(doubleAliveUid)) {
			int presetId = StateManageService.getRegisterTaskByUserId(doubleAliveUid).getPresetId();
			if (PresetTaskConfig.ORACLE_DG_SWITCH_TASKID == presetId) {
				logger.info("the dg switch under the double is running.");
				JSONObject obj = new JSONObject();
				obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
				obj.put(TaskConfig.TASK_JSON_VALUE, "已有任务启动");
				return obj;
			}
		}
		// 2,获取预设任务,每次执行都会克隆一个新任务。
		TaskOperation operation = null;
		try {
			operation = taskManageService.cloneTaskFormTaskTemplate(cloneTaskTemplateId, TaskConfig.TASK_STRATEGY_USER_PARALLEL);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "获取任务出错!");
			return obj;
		}
		operation.setName(doubleLiveName + "_数据库切换_" + operation.getId());
		operation.setUserId(doubleAliveUid);
		operation.setPoDesc("切换数据库双机:"+doubleLiveName + "_" + operation.getId()+"");

		// 删除之前运行时的参数，添加重新获取的参数到数据库。
		int taskId = operation.getId();
		taskManageService.removeTaskParams(taskId);
		//脚本所执行主机IP的参数
		List<TaskParam> params = getOracleSwitchParams(primaryHostInfo, standbyHostInfo,otherNodeInfo);
		for (TaskParam param : params) {
			param.setTaskId(taskId);
			taskManageService.addTaskParam(param);
		}
		operation.setParams(params);
		
		//脚本执行时所需要的参数。
		int racnum=primaryParams.size();
		TaskParam param = new TaskParam("racnum",""+racnum);
		try {
			TaskFileUtil.appendParamToFile(param, operation);
		} catch (IOException e) {
			logger.error("创建任务出错，write param to file error");
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "创建任务出错!");
			return obj;
		}
		try {
			taskManageService.updateTaskOperation(operation);
		} catch (Exception e) {
			logger.error(e);
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "创建任务出错!");
			return obj;
		}
		// 启动
		JSONObject obj = taskManageService.startTask(operation, true);
		return obj;
	}
	
	/**
	 * 数据库failOver
	 * @param primaryId
	 * @param standbyId
	 * @param doubleId
	 * @return
	 */
	public JSONObject oracleFaileOver(int primaryId, int standbyId, int doubleId) {
		logger.info("switch oracle dg. primaryGroupId: " + primaryId + " standbyGroupId: " + standbyId + " doubleId:" + doubleId);
		// 初始化参数。
		List<TaskExecParam> primaryParams = taskExecDao.getDeviceInfoByGroupId(new TaskExecParam(primaryId));
		List<TaskExecParam> standbyParams = taskExecDao.getDeviceInfoByGroupId(new TaskExecParam(standbyId));

		if (!(ValidateUtil.checkNotEmpty(primaryParams) || ValidateUtil.checkNotEmpty(standbyParams))) {
			logger.error("the params num 0, double oracle param number error,primaryUid:" + primaryId + " standbyUid" + standbyId);
			logger.error("run oracle dg switch error!");
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常");
			return obj;
		}
		// 如果主机信息有多个，说明组了rac，获取其中一个主机的信息即可。
		TaskExecParam primaryHostInfo = primaryParams.get(0);
		logger.info("the primary group device num:" + primaryParams.size() + "the selected device uid:" + primaryHostInfo.getUid());
		TaskExecParam standbyHostInfo = standbyParams.get(0);
		logger.info("the standby group device num:" + standbyParams.size() + "the selected device uid:" + standbyHostInfo.getUid());
		// 双活信息
		DoublemgtPo doubleLivePo = taskExecDao.getDoubleLivePo(new TaskExecParam(doubleId));
		String doubleLiveName = doubleLivePo.getName();
		String doubleAliveUid = doubleLivePo.getUid();
		if (!(ValidateUtil.isNullString(doubleAliveUid) || ValidateUtil.isNullString(doubleLiveName))) {
			logger.error("the double live msg in db error! doubleId：" + doubleId + " uid:" + doubleAliveUid + " name:" + doubleLiveName);
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常");
			return obj;
		}

		// 1,判断当前任务是否已经注册执行。
		if (null != StateManageService.getRegisterTaskByUserId(doubleAliveUid)) {
			int presetId = StateManageService.getRegisterTaskByUserId(doubleAliveUid).getPresetId();
			if (PresetTaskConfig.ORACLE_DG_SWITCH_TASKID == presetId) {
				logger.info("the dg switch under the double is running.");
				JSONObject obj = new JSONObject();
				obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
				obj.put(TaskConfig.TASK_JSON_VALUE, "已有任务启动");
				return obj;
			}
		}
		
		int taskTemplateId=PresetTaskConfig.ORACLE_DG_FAILOVER_TASKID;
		List<TaskExecParam> otherNodeInfo = new ArrayList<TaskExecParam>();//被节点除了操作节点以外的其他节点。
		if(standbyParams.size()>1){
			for(int i=1;i<standbyParams.size();i++)
			{
				otherNodeInfo.add(standbyParams.get(i));
			}
			taskTemplateId = PresetTaskConfig.ORACLE_RDG_FAILOVER_TASKID;
		}
		
		
		
		// 2,获取预设任务,每次执行都会克隆一个新任务。
		TaskOperation operation = null;
		try {
			operation = taskManageService.cloneTaskFormTaskTemplate(taskTemplateId, TaskConfig.TASK_STRATEGY_USER_PARALLEL);
		} catch (Exception e) {
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "获取任务出错!");
			return obj;
		}
		operation.setPresetId(PresetTaskConfig.ORACLE_DG_SWITCH_TASKID);
		operation.setName(doubleLiveName + "_数据库切换_" + operation.getId());
		operation.setUserId(doubleAliveUid);
		operation.setPoDesc("数据库双机failover:"+doubleLiveName + "_数据库failover_" + operation.getId()+"");

		// 删除之前运行时的参数，添加重新获取的参数到数据库。
		int taskId = operation.getId();
		taskManageService.removeTaskParams(taskId);
		List<TaskParam> params = getOracleFailoverParams(primaryHostInfo, standbyHostInfo,otherNodeInfo);
		for (TaskParam param : params) {
			param.setTaskId(taskId);
			taskManageService.addTaskParam(param);
		}
		operation.setParams(params);
		try {
			taskManageService.updateTaskOperation(operation);
		} catch (Exception e) {
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "创建任务出错!");
			return obj;
		}

		// 启动
		JSONObject obj = taskManageService.startTask(operation, true);
		return obj;
	}
	
	
	

	// 启动一体机
	synchronized public JSONObject startOrStopReturnAllInOne(int groupId, String userOpera) {
		logger.info(userOpera + " return by group id:" + groupId);
		List<TaskExecParam> calculateNodeInfoList = taskExecDao.getDeviceInfoByGroupId(new TaskExecParam(groupId));
		// 校验参数
		if (!ValidateUtil.checkNotEmpty(calculateNodeInfoList)) {
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常!");
			logger.error("device number 0,get calculate Node device info error! groupId:" + groupId);
			return obj;
		}
		GroupPo groupInfo = taskExecDao.getGroupInfoById(groupId);
		String groupUid = groupInfo.getUid();

		if (!ValidateUtil.validateUid(groupUid)) {
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常");
			logger.error("group uid error " + " groupUid:" + groupUid + " groupId:" + groupId);
			return obj;
		}

		// 校验Ip,uid
		for (TaskExecParam deviceInfo : calculateNodeInfoList) {
			String ip = deviceInfo.getIp();
			String uid = deviceInfo.getUid();
			// String passwd = deviceInfo.getPasswd();
			if (!(ValidateUtil.checkIpV4(ip) && ValidateUtil.validateUid(uid))) {
				JSONObject obj = new JSONObject();
				obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
				obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常");
				logger.error("ip or uid error ip:" + ip + " deviceUid:" + uid + " groupId:" + groupId);
				return obj;
			}
		}

		// 校验是否可执行。
		// 判断当前任务是否已经注册执行。
		if (null != StateManageService.getRegisterTaskByUserId(groupUid)) {
			logger.warn("the return task maybe is running . groupid:" + groupId);
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "已有任务启动");
			return obj;
		}
		int presetTaskId = 0;
		if (PresetTaskConfig.USER_OPERATION_START.equals(userOpera)) {
			presetTaskId = PresetTaskConfig.RETURN_ALLINONE_START_TASKID;
		} else if (PresetTaskConfig.USER_OPERATION_STOP.equals(userOpera)) {
			presetTaskId = PresetTaskConfig.RETURN_ALLINONE_STOP_TASKID;
		}

		// 2,获取预设任务,每次执行都会克隆一个新任务。
		TaskOperation operation = null;
		try {
			operation = taskManageService.cloneTaskFormTaskTemplate(presetTaskId, TaskConfig.TASK_STRATEGY_ONE_PARALLEL);
		} catch (Exception e) {
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "获取任务出错!");
			return obj;
		}
		operation.setPresetId(PresetTaskConfig.RETURN_ALLINONE_START_TASKID);
		operation.setName(groupInfo.getName() + "_" +userOpera+"_"+ operation.getId());
		operation.setUserId(groupInfo.getUid());
		// 删除之前运行时的参数，添加重新获取的参数到数据库。
		int taskId = operation.getId();
		taskManageService.removeTaskParams(taskId);

		// 选着其中的一个主机进行操作就可以
		TaskExecParam nodeInfo = calculateNodeInfoList.get(0);
		String ip = nodeInfo.getIp();
		String passwd = nodeInfo.getPasswd();
		if (!ValidateUtil.checkIpV4(ip)) {
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "数据库数据异常");
			logger.error("ip error ip:" + ip + " groupId:" + groupId);
			return obj;
		}

		List<TaskParam> params = null;
		if (PresetTaskConfig.USER_OPERATION_START.equals(userOpera)) {
			params = getReturnStartParams(ip, passwd);
		} else if (PresetTaskConfig.USER_OPERATION_STOP.equals(userOpera)) {
			params = getReturnStopParams(ip, passwd);
		}

		if(params != null){
			for (TaskParam param : params) {
				param.setTaskId(taskId);
				taskManageService.addTaskParam(param);
			}
		}

		operation.setParams(params);
		try {
			taskManageService.updateTaskOperation(operation);
		} catch (Exception e) {
			logger.error(e);
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "创建任务出错!");
			return obj;
		}
		JSONObject obj = taskManageService.startTask(operation, true);
		return obj;
	}

	private List<TaskParam> getOracleSwitchParams(TaskExecParam primaryParam, TaskExecParam standbyParam,List<TaskExecParam> otherNodeInfo) {
		List<TaskParam> params = new ArrayList<TaskParam>();
		if(otherNodeInfo.size() == 0){
		// 第一步 主机执行
		TaskParam param1Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_1, primaryParam.getIp());
		TaskParam param1Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_1, primaryParam.getPasswd());
		// 第二部 备机执行
		TaskParam param2Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_2, standbyParam.getIp());
		TaskParam param2Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX+ PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_2, standbyParam.getPasswd());
		// 第三步 主机切换
		TaskParam param3Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_3, primaryParam.getIp());
		TaskParam param3Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX  + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_3, primaryParam.getPasswd());
		// 第四步 备机切换
		TaskParam param4Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_4, standbyParam.getIp());
		TaskParam param4Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_4, standbyParam.getPasswd());
		// 第五步 备机执行redo
		TaskParam param5Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_5, primaryParam.getIp());
		TaskParam param5Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.ORACLE_SWITCH_SHELLNAME_5, primaryParam.getPasswd());
		params.add(param1Ip);
		params.add(param1Passwd);
		params.add(param2Ip);
		params.add(param2Passwd);
		params.add(param3Ip);
		params.add(param3Passwd);
		params.add(param4Ip);
		params.add(param4Passwd);
		params.add(param5Ip);
		params.add(param5Passwd);
		}
		else
		{
			// 第一步 主机执行
			TaskParam param1Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_1, primaryParam.getIp());
			TaskParam param1Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_1, primaryParam.getPasswd());
			// 第二部 备机执行
			TaskParam param2Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_2, standbyParam.getIp());
			TaskParam param2Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX+ PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_2, standbyParam.getPasswd());
			
			//第三步  关闭其他节点主机
			String ipInfo="";
			String passwdInfo="";
			for(TaskExecParam otherNode:otherNodeInfo){
				ipInfo+=otherNode.getIp()+",";
				passwdInfo+=otherNode.getPasswd()+",";
			}
			TaskParam param3Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_3, ipInfo);
			TaskParam param3Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX+ PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_3, passwdInfo);
		
			// 第四步 主机切换
			TaskParam param4Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_4, primaryParam.getIp());
			TaskParam param4Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX  + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_4, primaryParam.getPasswd());
			
			// 第五步 备机切换
			TaskParam param5Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_5, standbyParam.getIp());
			TaskParam param5Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_5, standbyParam.getPasswd());
			
			// 第六步 备机执行redo
			TaskParam param6Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_6, primaryParam.getIp());
			TaskParam param6Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_6, primaryParam.getPasswd());
			
			//第七步 启动其他节点
			TaskParam param7Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_7, ipInfo);
			TaskParam param7Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.ORACLE_RDGSWITCH_SHELLNAME_7,passwdInfo);
			
			params.add(param1Ip);
			params.add(param1Passwd);
			params.add(param2Ip);
			params.add(param2Passwd);
			params.add(param3Ip);
			params.add(param3Passwd);
			params.add(param4Ip);
			params.add(param4Passwd);
			params.add(param5Ip);
			params.add(param5Passwd);
			params.add(param6Ip);
			params.add(param6Passwd);
			params.add(param7Ip);
			params.add(param7Passwd);
		}
		return params;
	}
	/**
	 * 
	 * @param primaryParam
	 * @param standbyParam  备库执行操作的节点。
	 * @param standbyHosts  备库非操作节点。
	 * @return
	 */
	private List<TaskParam> getOracleFailoverParams(TaskExecParam primaryParam, TaskExecParam standbyParam,List<TaskExecParam> standbyHosts) {
		List<TaskParam> params = new ArrayList<TaskParam>();
		if(standbyHosts.size() ==0)//单机(非RAC切换)
		{
			//单机的数据库切换
			TaskParam param2Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_DG_FAILOVER_SHELLNAME_1, standbyParam.getIp());
			TaskParam param2Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX+ PresetTaskConfig.ORACLE_DG_FAILOVER_SHELLNAME_1, standbyParam.getPasswd());
			params.add(param2Ip);
			params.add(param2Passwd);
		}
		else
		{
			//RAC切换
			//1.校验操作节点状态
			TaskParam param3Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDG_FAILOVER_SHELLNAME_1, standbyParam.getIp());
			TaskParam param3Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX+ PresetTaskConfig.ORACLE_RDG_FAILOVER_SHELLNAME_1, standbyParam.getPasswd());
			params.add(param3Ip);
			params.add(param3Passwd);
			
			
			//2. shutdown非操作节点。
			String ipInfo="";
			String passwdInfo="";
			for(TaskExecParam otherNode:standbyHosts){
				ipInfo+=otherNode.getIp()+",";
				passwdInfo+=otherNode.getPasswd()+",";
			}
			TaskParam param1Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDG_FAILOVER_SHELLNAME_2, ipInfo);
			TaskParam param1Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX+ PresetTaskConfig.ORACLE_RDG_FAILOVER_SHELLNAME_2, passwdInfo);
			params.add(param1Ip);
			params.add(param1Passwd);
			//3.切换。
			TaskParam param2Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.ORACLE_RDG_FAILOVER_SHELLNAME_3, standbyParam.getIp());
			TaskParam param2Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX+ PresetTaskConfig.ORACLE_RDG_FAILOVER_SHELLNAME_3, standbyParam.getPasswd());
			params.add(param2Ip);
			params.add(param2Passwd);
		}
		return params;
	}

	private List<TaskParam> getReturnStartParams(String ip, String passwd) {
		List<TaskParam> params = new ArrayList<TaskParam>();
		TaskParam param1Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.RETURN_START_SHELLNAME_1, ip);
		TaskParam param1Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.RETURN_START_SHELLNAME_1, passwd);
		TaskParam param2Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.RETURN_START_SHELLNAME_2, ip);
		TaskParam param2Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.RETURN_START_SHELLNAME_2, passwd);
		TaskParam param3Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.RETURN_START_SHELLNAME_3, ip);
		TaskParam param3Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.RETURN_START_SHELLNAME_3, passwd);

		params.add(param1Ip);
		params.add(param1Passwd);
		params.add(param2Ip);
		params.add(param2Passwd);
		params.add(param3Ip);
		params.add(param3Passwd);
		return params;
	}

	private List<TaskParam> getReturnStopParams(String ip, String passwd) {
		List<TaskParam> params = new ArrayList<TaskParam>();
		TaskParam param1Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.RETURN_STOP_SHELLNAME_1, ip);
		TaskParam param1Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.RETURN_STOP_SHELLNAME_1, passwd);
		TaskParam param2Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.RETURN_STOP_SHELLNAME_2, ip);
		TaskParam param2Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.RETURN_STOP_SHELLNAME_2, passwd);
		TaskParam param3Ip = new TaskParam(TaskConfig.USER_TASK_REMOTEIP_SUFFIX + PresetTaskConfig.RETURN_STOP_SHELLNAME_3, ip);
		TaskParam param3Passwd = new TaskParam(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX + PresetTaskConfig.RETURN_STOP_SHELLNAME_3, passwd);

		params.add(param1Ip);
		params.add(param1Passwd);
		params.add(param2Ip);
		params.add(param2Passwd);
		params.add(param3Ip);
		params.add(param3Passwd);
		return params;
	}
}
