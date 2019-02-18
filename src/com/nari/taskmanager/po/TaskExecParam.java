package com.nari.taskmanager.po;

import org.springframework.stereotype.Component;

import com.nari.common.po.RowBounds;

@Component
public class TaskExecParam extends RowBounds{
	private static final long serialVersionUID = 1L;
	private String ip;
	private String passwd;
	private String userName;
	private String groupName;
	private String uid;
	private int id;//中心或双活的id
	
	@Override
	public String toString()
	{
		return " ip:"+ip+" userName:"+userName+" passwd:******";
	}
	
	public TaskExecParam(){
		
	}
	public TaskExecParam(int id){
		this.id = id;
	}
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

}
