package com.nari.taskmanager.soap;


import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nari.taskmanager.po.TaskLog;
import com.nari.taskmanager.po.TaskResult;
import com.nari.taskmanager.service.StateManageService;
import com.nari.taskmanager.service.TaskManageService;
import com.nari.taskmanager.service.TaskRunningConfigService;

@Service
public class TaskSoapService {
	private static Logger logger = Logger.getLogger(TaskSoapService.class);
	
	@Autowired
	TaskManageService taskManageService;
	@Autowired
	TaskRunningConfigService taskRunningConfigService;
	@Autowired
	StateManageService stateManageService;

	public String taskRunResult(TaskResult result) {
		MessageContext mc = MessageContext.getCurrentMessageContext();
		HttpServletRequest request = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		logger.info("receive task state msg from remote ip:  " + request.getRemoteAddr());
		logger.info("msg detail :["+result.toString()+"]");
/*		if(null != StateManageService.getRegisterTaskByTaskId(result.getTaskId()))
		{
			TaskLog log = new TaskLog();
			log.setHostIp(request.getRemoteAddr());
			log.setLogLevel("info");
			log.setLogTime(new Timestamp(System.currentTimeMillis()).toString());
			log.setStepId(result.getStepId());
			log.setTaskId(result.getTaskId());
			log.setLogDetial(result.getMsg() +",percent:"+result.getPercent());
			taskRunningConfigService.insertTaskLog(log);
		}*/
		stateManageService.dealShellResult(result);
		return "OK";
	}
	
	
	public String saveTaskLog(TaskLog log) {
		MessageContext mc = MessageContext.getCurrentMessageContext();
		HttpServletRequest request = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		logger.info("receive log msg from remote ip:  " + request.getRemoteAddr());
		if(null == log.getHostIp() || "".equals(log.getHostIp()))
		{
			log.setHostIp(request.getRemoteAddr());
		}
		logger.info("msg detail :["+log.toString()+"]");
		taskRunningConfigService.insertTaskLog(log);
		return "OK";
	}
}
