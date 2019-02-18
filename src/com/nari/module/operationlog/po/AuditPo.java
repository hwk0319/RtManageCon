package com.nari.module.operationlog.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AuditPo {
	private Integer id;
	private String name;
	private String erjiname;
	private String isAudit;
	private String auditThose;
	@JsonIgnore
	private String useFlag;
	@JsonIgnore
	private String createBy;
	@JsonIgnore
	private Date createdate;
	@JsonIgnore
	private String updateBy;
	@JsonIgnore
	private Date updateDate;
	private String remark;
	private String mData;
	private String jwt;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}
	public String getAuditThose() {
		return auditThose;
	}
	public void setAuditThose(String auditThose) {
		this.auditThose = auditThose;
	}
	public String getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getErjiname() {
		return erjiname;
	}
	public void setErjiname(String erjiname) {
		this.erjiname = erjiname;
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
}
