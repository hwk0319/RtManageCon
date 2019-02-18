package com.nari.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.nari.taskmanager.config.PresetTaskConfig;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.service.PreSetTaskExecService;

@Controller
@RequestMapping("/execTask")
public class TaskExecController {

	@Autowired
	PreSetTaskExecService preSetTaskExecService;
	/**
	 * 双机数据库切换。
	 * @return
	 */
	@RequestMapping("/switchOracleDg")
	public JSONObject doOracleSwitch(int primaryId,int standbyId,int doubleId) {
		JSONObject obj = preSetTaskExecService.OracleDgSwitch(primaryId, standbyId,doubleId);
		return obj;
	}
	/**
	 * 双机数据库切换。
	 * @return
	 */
	@RequestMapping("/failOverOracleDg")
	public JSONObject doOraclefaileOver(int primaryId,int standbyId,int doubleId) {
		JSONObject obj = preSetTaskExecService.oracleFaileOver(primaryId, standbyId,doubleId);
		return obj;
	}
	
	/**
	 * 启动一体机
	 * @return
	 */
	@RequestMapping("/startOrStopReturnAIO")
	public JSONObject startReturnAllInOne(int groupId,String opera)
	{
		if(PresetTaskConfig.USER_OPERATION_START.equals(opera))
		{
			JSONObject obj = preSetTaskExecService.startOrStopReturnAllInOne(groupId,PresetTaskConfig.USER_OPERATION_START);
			return obj;
		}
		else if(PresetTaskConfig.USER_OPERATION_STOP.equals(opera))
		{
			JSONObject obj = preSetTaskExecService.startOrStopReturnAllInOne(groupId,PresetTaskConfig.USER_OPERATION_STOP);
			return obj;
		}
		else
		{
			JSONObject obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, "不支持的操作");
			return obj;
		}
	}
	
}
