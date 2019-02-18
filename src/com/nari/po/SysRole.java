package com.nari.po;

import com.nari.util.MyPropertiesPersist;

public class SysRole {
	private int roleid;
	private String rolename;
	private String roledesc;
	private String rolemenu;
	private String roleacl;
	private int roletype;
	private String DBTYPE;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getRoledesc() {
		return roledesc;
	}
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}
	public String getRolemenu() {
		return rolemenu;
	}
	public void setRolemenu(String rolemenu) {
		this.rolemenu = rolemenu;
	}
	public String getRoleacl() {
		return roleacl;
	}
	public void setRoleacl(String roleacl) {
		this.roleacl = roleacl;
	}
	public int getRoletype() {
		return roletype;
	}
	public void setRoletype(int roletype) {
		this.roletype = roletype;
	}
	
}
