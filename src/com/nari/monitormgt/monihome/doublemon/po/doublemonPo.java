package com.nari.monitormgt.monihome.doublemon.po;

import java.util.Date;

public class doublemonPo {
	private int id;
	private int index_id;
	private String uid;
	private String value;
	private Date record_time;
	private Integer usernumber;
	
	private int double_id;
	private int type;
	private String standby_a;
	private String standby_b;
	private int group_id_a;
	private int group_id_b;
	private String swMode;
	private String core_tagging;
	private String core;
	private String name;
	private String position;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIndex_id() {
		return index_id;
	}
	public void setIndex_id(int index_id) {
		this.index_id = index_id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getRecord_time() {
		return record_time;
	}
	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}
	public Integer getUsernumber() {
		return usernumber;
	}
	public void setUsernumber(Integer usernumber) {
		this.usernumber = usernumber;
	}
	public int getDouble_id() {
		return double_id;
	}
	public void setDouble_id(int double_id) {
		this.double_id = double_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getStandby_a() {
		return standby_a;
	}
	public void setStandby_a(String standby_a) {
		this.standby_a = standby_a;
	}
	public String getStandby_b() {
		return standby_b;
	}
	public void setStandby_b(String standby_b) {
		this.standby_b = standby_b;
	}
	public int getGroup_id_a() {
		return group_id_a;
	}
	public void setGroup_id_a(int group_id_a) {
		this.group_id_a = group_id_a;
	}
	public int getGroup_id_b() {
		return group_id_b;
	}
	public void setGroup_id_b(int group_id_b) {
		this.group_id_b = group_id_b;
	}
	public String getSwMode() {
		return swMode;
	}
	public void setSwMode(String swMode) {
		this.swMode = swMode;
	}
	public String getCore_tagging() {
		return core_tagging;
	}
	public void setCore_tagging(String core_tagging) {
		this.core_tagging = core_tagging;
	}
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
