package com.nari.taskmanager.po;

import org.springframework.stereotype.Component;

@Component
public class TaskParam extends BaseTask {
	private String value;
	private int required;
	private String validateStr;
	private String mData;
	
	/**
	 * 如果为模板-对应的模板ID 如果为任务-对应任务ID
	 */
	private int taskId;
	
	public TaskParam(String name,String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public TaskParam(){};
	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRequired() {
		return required;
	}

	public void setRequired(int required) {
		this.required = required;
	}

	public String getValidateStr() {
		return validateStr;
	}

	public void setValidateStr(String validateStr) {
		this.validateStr = validateStr;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		StringBuilder paramStr = new StringBuilder();
		paramStr.append("taskId:").append(taskId).append(" id:").append(getId());
		if(getName()!=null ){
			paramStr.append(" name:").append(getName());
			if( getName().contains("passwd") || getName().contains("PASSWD") || getName().contains("password") ||  getName().contains("PASSWORD"))
			{
				paramStr.append(" value:").append("******");
			}
			else
			{
				paramStr.append(" value:").append(getValue());
			}
		}
		return paramStr.toString();
	}

	public String getmData() {
		return mData;
	}

	public void setmData(String mData) {
		this.mData = mData;
	}
	
	
}
