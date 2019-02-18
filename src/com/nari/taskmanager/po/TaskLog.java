package com.nari.taskmanager.po;

import org.springframework.stereotype.Component;

@Component
public class TaskLog {
	private int taskId;
	private int stepId;
	private String logTime;
	private String logLevel;
	private String hostIp;
	private String hostName;
	private String logDetial;
	
	@Override
	public String toString() {
		return "taskId:"+taskId+" stepId:"+stepId+" level:"+logLevel+" hostIp:"+hostIp+" hostName:"+hostName+" detail:"+logDetial;
	}
	
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getLogDetial() {
		return logDetial;
	}
	public void setLogDetial(String logDetial) {
		if(logDetial.length()>2000)
		{
			logDetial = logDetial.substring(0,2000);
		}
		this.logDetial = logDetial;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

}
