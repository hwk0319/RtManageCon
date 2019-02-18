package com.nari.monitormgt.monihome.servers.po;

import java.security.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IndexDataPo {
	private Integer id;
	private Integer index_id;
	private String uid;
	private String value;
	private Timestamp record_time;
	
	private Integer index_type;
	private String description;
	private String remark;
	//该字段主要用来判断是否在告警表产生告警信息
	//1-表示有告警，0-表示没有告警信息
	private Integer iswarn;
	@JsonIgnore
	private String DBTYPE;

	public Integer getIndex_id() {
		return index_id;
	}
	public void setIndex_id(Integer index_id) {
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
	public Timestamp getRecord_time() {
		return record_time;
	}
	public void setRecord_time(Timestamp record_time) {
		this.record_time = record_time;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIndex_type() {
		return index_type;
	}
	public void setIndex_type(Integer index_type) {
		this.index_type = index_type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIswarn() {
		return iswarn;
	}
	public void setIswarn(Integer iswarn) {
		this.iswarn = iswarn;
	}
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = dBTYPE;
	}
	
}
