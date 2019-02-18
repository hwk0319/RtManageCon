package com.nari.module.systemmgt.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SystemDevicePo {
	private Integer sys_id;
	private Integer dev_id;
	@JsonIgnore
	private boolean use_flag;
	@JsonIgnore
	private Integer create_by;
	@JsonIgnore
	private Date create_date;
	@JsonIgnore
	private Integer update_by;
	@JsonIgnore
	private Date update_date;
	private String remark;
	public Integer getSys_id() {
		return sys_id;
	}
	public void setSys_id(Integer sys_id) {
		this.sys_id = sys_id;
	}
	public Integer getDev_id() {
		return dev_id;
	}
	public void setDev_id(Integer dev_id) {
		this.dev_id = dev_id;
	}
	public boolean isUse_flag() {
		return use_flag;
	}
	public void setUse_flag(boolean use_flag) {
		this.use_flag = use_flag;
	}
	public Integer getCreate_by() {
		return create_by;
	}
	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Integer getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(Integer update_by) {
		this.update_by = update_by;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "SystemDevicePo [sys_id=" + sys_id + ", dev_id=" + dev_id
				+ ", use_flag=" + use_flag + ", create_by=" + create_by
				+ ", create_date=" + create_date + ", update_by=" + update_by
				+ ", update_date=" + update_date + ", remark=" + remark + "]";
	}
	
	
}
