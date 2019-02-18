package com.nari.module.common.po;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 
 * @author 黄文凯
 * @date 2017年5月2日
 * @Description 数据字典
 *
 */
public class CommonDictPo {
	private Integer id;//编号
	private String type;//类型
	private List<String> types; //类型集合，防止SQL注入
	private String value;//值
	private String name;//名称
	private String label;//标签
	private BigDecimal sort;//排序
	private String description;//描述
	@JsonIgnore
	private Date update_date;//修改日期
	@JsonIgnore
	private boolean use_flag;//1-在用 ,0-已删除
	@JsonIgnore
	private Integer create_by;//创建者
	@JsonIgnore
	private Date create_date;//创建日期
	@JsonIgnore
	private Integer update_by;//修改者
	private String remark;//备注
	private Integer index_type;
	private Integer warn_rule;	//warn_rule
	private Integer warnrule_id; //warnrule_id
	public Integer getWarnrule_id() {
		return warnrule_id;
	}
	public void setWarnrule_id(Integer warnrule_id) {
		this.warnrule_id = warnrule_id;
	}
	public Integer getWarn_rule() {
		return warn_rule;
	}
	public void setWarn_rule(Integer warn_rule) {
		this.warn_rule = warn_rule;
	}
	public Integer getIndex_type() {
		return index_type;
	}
	public void setIndex_type(Integer index_type) {
		this.index_type = index_type;
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
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public BigDecimal getSort() {
		return sort;
	}
	public void setSort(BigDecimal sort) {
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
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
}
