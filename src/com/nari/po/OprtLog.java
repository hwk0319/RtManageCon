package com.nari.po;

public class OprtLog {
	private int id;
	private String module;
	private String oprt_type;
	private String oprt_time;
	private String oprt_user;
	private String oprt_info;
	private String oprt_content;
	private String oprt_time_end;
	private int result;
	private String position;
	
	private String sql;
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	private String ip;

	public String getOprt_time_end() {
		return oprt_time_end;
	}

	public void setOprt_time_end(String oprt_time_end) {
		this.oprt_time_end = oprt_time_end;
	}

	public String getOprt_content() {
		return oprt_content;
	}

	public void setOprt_content(String oprt_content) {
		this.oprt_content = oprt_content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOprt_type() {
		return oprt_type;
	}

	public void setOprt_type(String oprt_type) {
		this.oprt_type = oprt_type;
	}

	public String getOprt_time() {
		return oprt_time;
	}

	public void setOprt_time(String oprt_time) {
		this.oprt_time = oprt_time;
	}

	public String getOprt_user() {
		return oprt_user;
	}

	public void setOprt_user(String oprt_user) {
		this.oprt_user = oprt_user;
	}

	public String getOprt_info() {
		return oprt_info;
	}

	public void setOprt_info(String oprt_info) {
		this.oprt_info = oprt_info;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
