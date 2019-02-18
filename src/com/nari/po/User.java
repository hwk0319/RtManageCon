package com.nari.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nari.util.MyPropertiesPersist;

public class User implements Serializable{
	private int id;
	private String username;
	private String mm;
	private String mmM;
	private Integer roleid;
	private String rolename;
	private String email;
	private String firstlogin;
	private Date pwdtime;
	private String online;
	private String start_ip;
	private String end_ip;
	private String start_time;
	private String end_time;
	private String status;
	private int err_num;
	private String passed;
	private String ipaddr;
	private String pwd_new;
	
	private String funcs;
	private List<String> permissions;

	private String DBTYPE;
	private String sql;
	private String text;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public String getFuncs() {
		return funcs;
	}
	public void setFuncs(String funcs) {
		this.funcs = funcs;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	public String getIpaddr() {
		return ipaddr;
	}
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd_new() {
		return pwd_new;
	}
	public void setPwd_new(String pwd_new) {
		this.pwd_new = pwd_new;
	}
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstlogin() {
		return firstlogin;
	}
	public void setFirstlogin(String firstlogin) {
		this.firstlogin = firstlogin;
	}
	public Date getPwdtime() {
		return pwdtime;
	}
	public void setPwdtime(Date pwdtime) {
		this.pwdtime = pwdtime;
	}
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getStart_ip() {
		return start_ip;
	}
	public void setStart_ip(String start_ip) {
		this.start_ip = start_ip;
	}
	public String getEnd_ip() {
		return end_ip;
	}
	public void setEnd_ip(String end_ip) {
		this.end_ip = end_ip;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getErr_num() {
		return err_num;
	}
	public void setErr_num(int err_num) {
		this.err_num = err_num;
	}
	public String getPassed() {
		return passed;
	}
	public void setPassed(String passed) {
		this.passed = passed;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getMmM() {
		return mmM;
	}
	public void setMmM(String mmM) {
		this.mmM = mmM;
	}
	
	
	
	
}
