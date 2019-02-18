package com.nari.module.warnrule.po;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;
/**
 * 
 * @author Administrator
 * @date 2017年5月17日
 * @Description 告警规则
 *
 */
public class WarnrulePo extends BasePo{
	//编号
	private Integer id;
	//指标项id
	private Integer index_id;
	//统一ID
	private String uid;
	//阈值上限
	private Integer upper_limit;
	//阈值下限
	private Integer lower_limit;
	//告警类型
	private Integer type;
	//告警等级
	private Integer level;
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
	//指标分类ID
	private Integer indextype_id;
	//index名称
	private String indexname;
	//indextype名称
	private String indextypename;
	//设备名称
	private String brand;
	//指标项关联id
	private Integer index_warn_id;
	//标准值
	@Size(min=0,max=60,message="标准值长度不能超过60个字符！")
	private String std_value;
	//描述
	private String description;
	//任务Id
	private Integer task_id;
	//任务名称
	private String task_name;
	@JsonIgnore
	private String DBTYPE;
	private String mData;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public Integer getTask_id() {
		return task_id;
	}
	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStd_value() {
		return std_value;
	}
	public void setStd_value(String std_value) {
		this.std_value = std_value;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Integer getUpper_limit() {
		return upper_limit;
	}
	public void setUpper_limit(Integer upper_limit) {
		this.upper_limit = upper_limit;
	}
	public Integer getLower_limit() {
		return lower_limit;
	}
	public void setLower_limit(Integer lower_limit) {
		this.lower_limit = lower_limit;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
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
	public Integer getIndextype_id() {
		return indextype_id;
	}
	public void setIndextype_id(Integer indextype_id) {
		this.indextype_id = indextype_id;
	}
	public String getIndexname() {
		return indexname;
	}
	public void setIndexname(String indexname) {
		this.indexname = indexname;
	}
	public String getIndextypename() {
		return indextypename;
	}
	public void setIndextypename(String indextypename) {
		this.indextypename = indextypename;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getIndex_warn_id() {
		return index_warn_id;
	}
	public void setIndex_warn_id(Integer index_warn_id) {
		this.index_warn_id = index_warn_id;
	}
	@Override
	public String toString() {
		return "WarnrulePo [id=" + id + ", index_id=" + index_id + ", uid="
				+ uid + ", upper_limit=" + upper_limit + ", lower_limit="
				+ lower_limit + ", type=" + type + ", level=" + level
				+ ", use_flag=" + use_flag + ", create_by=" + create_by
				+ ", create_date=" + create_date + ", update_by=" + update_by
				+ ", update_date=" + update_date + ", remark=" + remark + "]";
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	
}
