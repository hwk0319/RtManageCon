package com.nari.module.general.po;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GeneralPo {
	private int id;
	private String module;// 操作模块
	private String oprt_type;// 操作类型
	private Date oprt_time;// -- 操作时间
	private String oprt_user;//- 操作人员
	private String oprt_content;//操作内容
	private String ip;//- 操作人IP地址
	private Integer oprt_id; //操作数据ID
	@JsonIgnore
	private List<String> roleIds;
	private String tabname;
	@JsonIgnore
	private String roleid;
	
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
	public Date getOprt_time() {
		return oprt_time;
	}
	public void setOprt_time(Date oprt_time) {
		this.oprt_time = oprt_time;
	}
	public String getOprt_user() {
		return oprt_user;
	}
	public void setOprt_user(String oprt_user) {
		this.oprt_user = oprt_user;
	}
	public String getOprt_content() {
		return oprt_content;
	}
	public void setOprt_content(String oprt_content) {
		this.oprt_content = oprt_content;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getOprt_id() {
		return oprt_id;
	}
	public void setOprt_id(Integer oprt_id) {
		this.oprt_id = oprt_id;
	}
	public List<String> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}
	public String getTabname() {
		return tabname;
	}
	public void setTabname(String tabname) {
		this.tabname = tabname;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	
}
