package com.nari.module.indextype.po;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;

public class IndexPo extends BasePo{

	//编号
	private Integer id;
	//分类编号
	private Integer index_type;
	//阈值上限
	private Integer upper_limit;
	//描述
	@Size(min=1,max=60,message="描述请输入1到60个字符！")
	private String description;
	//阈值下限
	private Integer lower_limit;
	//标准值
	@Size(min=0,max=60,message="标准值请输入0到60个字符！")
	private String std_value;
	//指标项ID
	private Integer index_id;
	//指标类ID
	private Integer indextype_id;
	//使用标示
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
	@Size(min=0,max=30,message="详细阐述请输入0到200个字符！")
	private String remark;
	//指标名称
	private String name;
	private Integer warn_rule;
	private String mData;
	private String index_arr;
	@JsonIgnore
	private String DBTYPE;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public String getStd_value() {
		return std_value;
	}
	public void setStd_value(String std_value) {
		this.std_value = std_value;
	}
	public Integer getWarn_rule() {
		return warn_rule;
	}
	public void setWarn_rule(Integer warn_rule) {
		this.warn_rule = warn_rule;
	}
	private Integer warnrule_id; //warnrule_id
	public Integer getWarnrule_id() {
		return warnrule_id;
	}
	public void setWarnrule_id(Integer warnrule_id) {
		this.warnrule_id = warnrule_id;
	}
	public Integer getWarn_rule(Integer warn_rule) {
		return warn_rule;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIndextype_id() {
		return indextype_id;
	}
	public void setIndextype_id(Integer indextype_id) {
		this.indextype_id = indextype_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getIndex_id() {
		return index_id;
	}
	public void setIndex_id(Integer index_id) {
		this.index_id = index_id;
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public String getIndex_arr() {
		return index_arr;
	}
	public void setIndex_arr(String index_arr) {
		this.index_arr = index_arr;
	}
	
}
