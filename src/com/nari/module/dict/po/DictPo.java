package com.nari.module.dict.po;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.util.MyPropertiesPersist;


/**
 * 
 * @author 黄文凯
 * @date 2017年5月2日
 * @Description 数据字典
 *
 */
public class DictPo {
	private Integer id;//编号
	@Size(min=1,max=50,message="类型长度不能超过50个字符！")
	private String type;//类型
	private List<String> types;
	@Size(min=1,max=50,message="值长度不能超过50个字符！")
	private String value;//值
	@Size(min=1,max=50,message="名称长度不能超过50个字符！")
	private String name;//名称
	@Size(min=1,max=50,message="标签长度不能超过50个字符！")
	private String label;//标签
	private double sort;//排序
	@Size(min=0,max=50,message="描述长度不能超过100个字符！")
	private String description;//描述
	@JsonIgnore
	private boolean use_flag;//1-在用 ,0-已删除
	private String remark;//备注
	@JsonIgnore
	private String create_by;//创建者
	@JsonIgnore
	private Date create_date;//创建日期
	@JsonIgnore
	private String update_by;//修改者
	@JsonIgnore
	private Date update_date;//修改日期
	@JsonIgnore
	private String DBTYPE;
	private String mData;
	private String jwt;
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getSort() {
		return sort;
	}
	public void setSort(double sort) {
		this.sort = sort;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public boolean isUse_flag() {
		return use_flag;
	}
	public void setUse_flag(boolean use_flag) {
		this.use_flag = use_flag;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "DictPo [id=" + id + ", type=" + type + ", value=" + value
				+ ", name=" + name + ", label=" + label + ", sort=" + sort
				+ ", description=" + description + ", update_date="
				+ update_date + ", use_flag=" + use_flag + ", create_by="
				+ create_by + ", create_date=" + create_date + ", update_by="
				+ update_by + ", remark=" + remark + "]";
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
}
