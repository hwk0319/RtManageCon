package com.nari.taskmanager.po;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class TaskStep extends BaseTask {

	private String shellName;
	private int timeOut;
	private String shellPath;
	private String shellTxt;
	private int stepOrder;
	private Timestamp createTime;
	private long costTime;
	private String resultDesc;// 记录step的执行结果描述
	private int percent;
	private String mData;
	/**
	 * 如果为模板-对应的模板ID 如果人任务-对应任务ID
	 */
	private int taskId;

	private Timestamp lastTime;

	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("step_name: ").append(this.getName()).append(" | ").append("task_id: ").append(this.getTaskId())
				.append(" | ").append("id: ").append(this.getId()).append(" | ").append("step_state: ")
				.append(this.getState()).append(" | ").append("step_stepOrder: ").append(this.getStepOrder())
				.append(" | ")
				.append("step_desc: ").append(this.getPoDesc()).append(" | ").append("step_shellName: ")
				.append(this.getShellName()).append(";");
		return sbuf.toString();
	}

	public String getShellName() {
		return shellName;
	}

	public void setShellName(String shellName) {
		this.shellName = shellName;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getShellPath() {
		return shellPath;
	}

	public void setShellPath(String shellPath) {
		this.shellPath = shellPath;
	}

	public int getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(int stepOrder) {
		this.stepOrder = stepOrder;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public String getShellTxt() {
		return shellTxt;
	}

	public void setShellTxt(String shellTxt) {
		this.shellTxt = shellTxt;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getmData() {
		return mData;
	}

	public void setmData(String mData) {
		this.mData = mData;
	}
}
