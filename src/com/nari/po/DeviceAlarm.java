package com.nari.po;

import com.nari.common.persistence.BaseEntity;

public class DeviceAlarm extends BaseEntity<DeviceAlarm>{
	private String device_code;
	private String state_field;
	private String alarm_level;
	private String alarm_time;
	private String alarm_code;
	private String status;
	private String alarm_info;
	private String device_categ;
	private String description;
	private String process_user;
	private String process_time;
	private String alarm_content  ;       
	private String cur_value;
	private String top_value;
	private String alarm_type;
	
	
	public String getAlarm_type() {
		return alarm_type;
	}

	public void setAlarm_type(String alarm_type) {
		this.alarm_type = alarm_type;
	}

	public String getCur_value() {
		return cur_value;
	}

	public void setCur_value(String cur_value) {
		this.cur_value = cur_value;
	}

	public String getTop_value() {
		return top_value;
	}

	public void setTop_value(String top_value) {
		this.top_value = top_value;
	}

	public String getAlarm_content() {
		return alarm_content;
	}

	public void setAlarm_content(String alarm_content) {
		this.alarm_content = alarm_content;
	}

	private String device_name;
	private String alarm_time_end;

	public String getAlarm_time_end() {
		return alarm_time_end;
	}

	public void setAlarm_time_end(String alarm_time_end) {
		this.alarm_time_end = alarm_time_end;
	}


	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getState_field() {
		return state_field;
	}

	public void setState_field(String state_field) {
		this.state_field = state_field;
	}

	public String getAlarm_level() {
		return alarm_level;
	}

	public void setAlarm_level(String alarm_level) {
		this.alarm_level = alarm_level;
	}

	public String getAlarm_time() {
		return alarm_time;
	}

	public void setAlarm_time(String alarm_time) {
		this.alarm_time = alarm_time;
	}

	public String getAlarm_code() {
		return alarm_code;
	}

	public void setAlarm_code(String alarm_code) {
		this.alarm_code = alarm_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAlarm_info() {
		return alarm_info;
	}

	public void setAlarm_info(String alarm_info) {
		this.alarm_info = alarm_info;
	}

	public String getDevice_categ() {
		return device_categ;
	}

	public void setDevice_categ(String device_categ) {
		this.device_categ = device_categ;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcess_user() {
		return process_user;
	}

	public void setProcess_user(String process_user) {
		this.process_user = process_user;
	}

	public String getProcess_time() {
		return process_time;
	}

	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

}
