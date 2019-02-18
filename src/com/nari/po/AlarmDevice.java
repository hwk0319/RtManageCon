package com.nari.po;

public class AlarmDevice {
	private int id;
	private String alarm_id;
	private String user_id;
	private int warm_method;
	private String rule_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAlarm_id() {
		return alarm_id;
	}
	public void setAlarm_id(String alarm_id) {
		this.alarm_id = alarm_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getWarm_method() {
		return warm_method;
	}
	public void setWarm_method(int warm_method) {
		this.warm_method = warm_method;
	}
	public String getRule_id() {
		return rule_id;
	}
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}
}
