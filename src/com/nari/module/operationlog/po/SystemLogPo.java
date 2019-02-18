package com.nari.module.operationlog.po;

/**
 * 系统日志
 * 2018年5月18日13:23:41
 * @author Administrator
 *
 */
public class SystemLogPo {
	private String date;
	private String systemLogType;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSystemLogType() {
		return systemLogType;
	}
	public void setSystemLogType(String systemLogType) {
		this.systemLogType = systemLogType;
	}
}
