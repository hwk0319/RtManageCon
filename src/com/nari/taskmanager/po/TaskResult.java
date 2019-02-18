package com.nari.taskmanager.po;

public class TaskResult {
	/**
	 * 1--执行成功
	 * 2--执行失败
	 * 3--正在执行（上报执行消息或进度）。
	 */
	private int msgType;
	private int taskId;
	private int stepId;
	private String msg;
	private int percent;
	private String timestamp;
	private String hostFlag;
	

	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("msgType:").append(msgType).append(" stakId:").append(taskId).append(" stepID:").append(stepId).append(" msg:").append(msg).append(" percent:").append(percent);
		return res.toString();
	}


	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}


	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
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


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public String getHostFlag() {
		return hostFlag;
	}


	public void setHostFlag(String hostFlag) {
		this.hostFlag = hostFlag;
	}

}
