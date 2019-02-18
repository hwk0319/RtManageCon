package com.nari.monitormgt.monihome.group.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.monitormgt.monihome.servers.po.PagePo;


/**
 * 
 * @author Administrator
 * @date 2017年5月4日
 * @Description
 *
 */
public class GroupsPo extends PagePo{
	//编号
	private Integer id;
	//统一ID
	private String uid;
	//名称
	private String name;
	//描述
	private String description;
	//1-在用0-已删除
	@JsonIgnore
	private boolean use_flag;
	//创建者
	@JsonIgnore
	private Integer create_by;
	//创建日期
	@JsonIgnore
	private Date create_date;
	//修改者
	@JsonIgnore
	private Integer update_by;
	//修改日期
	@JsonIgnore
	private Date update_date;
	//备注
	private String remark;
	//双活ID
	private Integer double_id;
	//集群ID
	private Integer cluster_id;
	//搜索
	private String searchText;
	private Integer searchId;
	private String searchType;
	@JsonIgnore
	private String DBTYPE;
	private String position;
	
	public Integer getCluster_id() {
		return cluster_id;
	}
	public void setCluster_id(Integer cluster_id) {
		this.cluster_id = cluster_id;
	}
	public Integer getDouble_id() {
		return double_id;
	}
	public void setDouble_id(Integer double_id) {
		this.double_id = double_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		return "GroupPo [id=" + id + ", uid=" + uid + ", name=" + name
				+ ", description=" + description + ", use_flag=" + use_flag
				+ ", create_by=" + create_by + ", create_date=" + create_date
				+ ", update_by=" + update_by + ", update_date=" + update_date
				+ ", remark=" + remark + "]";
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public Integer getSearchId() {
		return searchId;
	}
	public void setSearchId(Integer searchId) {
		this.searchId = searchId;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = dBTYPE;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
}
