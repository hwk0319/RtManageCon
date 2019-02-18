package com.nari.module.thresholdmgt.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.util.MyPropertiesPersist;

/**
 * 
 * @author sz
 * @date 2017年5月9日
 * @Description
 *
 */
public class ThresholdPo {
	//编号
	private Integer id;
	//编号
	private Integer rule_id;
	//指标ID
	private Integer metric_id;
	//模型ID
	private Integer model_id;
	//模型分项ID
	private Integer model_item_id;
	//名称
	private String rule_name;
	//告警等级
	private Integer warn_level;
	//一级计算方法
	private String method1;
	//二级计算方法
	private String method2;
	//严重计算方法
	private String method3;
	//一级条件值
	private String metric_value1;
	//二级条件值
	private String metric_value2;
	//严重条件值
	private String metric_value3;
	//一级扣分值
	private Integer deduct1;
	//二级扣分值
	private Integer deduct2;
	//严重扣分值
	private Integer deduct3;
	//使用标示
	@JsonIgnore
	private boolean use_flag;
	//创建者
	@JsonIgnore
	private String create_by;
	//创建日期
	@JsonIgnore
	private Date create_date;
	//修改者
	@JsonIgnore
	private String update_by;
	//修改日期
	@JsonIgnore
	private Date update_date;
	//模型名称
	private String model_name;
	//模型分项名称
	private String model_item_name;
	//模型ID对应状态
	private Integer status;
	//指标项ID
	private Integer index_id;
	//指标项描述
	private String description;
	//模型分项总分
	private Integer total_score;
	//指标项总分
	private Integer index_total_score;
	@JsonIgnore
	private String sql;
	private String DBTYPE;
	private String oper;
	
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
	public Integer getRule_id() {
		return rule_id;
	}
	public void setRule_id(Integer rule_id) {
		this.rule_id = rule_id;
	}
	public Integer getMetric_id() {
		return metric_id;
	}
	public void setMetric_id(Integer metric_id) {
		this.metric_id = metric_id;
	}
	public Integer getModel_id() {
		return model_id;
	}
	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
	}
	public Integer getModel_item_id() {
		return model_item_id;
	}
	public void setModel_item_id(Integer model_item_id) {
		this.model_item_id = model_item_id;
	}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public Integer getWarn_level() {
		return warn_level;
	}
	public void setWarn_level(Integer warn_level) {
		this.warn_level = warn_level;
	}
	public String getMethod1() {
		return method1;
	}
	public void setMethod1(String method1) {
		this.method1 = method1;
	}
	public String getMethod2() {
		return method2;
	}
	public void setMethod2(String method2) {
		this.method2 = method2;
	}
	public String getMethod3() {
		return method3;
	}
	public void setMethod3(String method3) {
		this.method3 = method3;
	}
	
	public String getMetric_value1() {
		return metric_value1;
	}
	public void setMetric_value1(String metric_value1) {
		this.metric_value1 = metric_value1;
	}
	public String getMetric_value2() {
		return metric_value2;
	}
	public void setMetric_value2(String metric_value2) {
		this.metric_value2 = metric_value2;
	}
	public String getMetric_value3() {
		return metric_value3;
	}
	public void setMetric_value3(String metric_value3) {
		this.metric_value3 = metric_value3;
	}
	public Integer getDeduct1() {
		return deduct1;
	}
	public void setDeduct1(Integer deduct1) {
		this.deduct1 = deduct1;
	}
	public Integer getDeduct2() {
		return deduct2;
	}
	public void setDeduct2(Integer deduct2) {
		this.deduct2 = deduct2;
	}
	public Integer getDeduct3() {
		return deduct3;
	}
	public void setDeduct3(Integer deduct3) {
		this.deduct3 = deduct3;
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
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getModel_item_name() {
		return model_item_name;
	}
	public void setModel_item_name(String model_item_name) {
		this.model_item_name = model_item_name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIndex_id() {
		return index_id;
	}
	public void setIndex_id(Integer index_id) {
		this.index_id = index_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getTotal_score() {
		return total_score;
	}
	public void setTotal_score(Integer total_score) {
		this.total_score = total_score;
	}
	public Integer getIndex_total_score() {
		return index_total_score;
	}
	public void setIndex_total_score(Integer index_total_score) {
		this.index_total_score = index_total_score;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	@Override
	public String toString() {
		return "ThresholdPo [id=" + id + ", rule_id=" + rule_id
				+ ", metric_id=" + metric_id + ", model_id=" + model_id
				+ ", model_item_id=" + model_item_id + ", rule_name="
				+ rule_name + ", warn_level=" + warn_level + ", method1="
				+ method1 + ", method2=" + method2 + ", method3=" + method3
				+ ", metric_value1=" + metric_value1 + ", metric_value2="
				+ metric_value2 + ", metric_value3=" + metric_value3
				+ ", deduct1=" + deduct1 + ", deduct2=" + deduct2
				+ ", deduct3=" + deduct3 + ", use_flag=" + use_flag
				+ ", create_by=" + create_by + ", create_date=" + create_date
				+ ", update_by=" + update_by + ", update_date=" + update_date
				+ ", model_name=" + model_name + ", model_item_name="
				+ model_item_name + ", status=" + status + ", index_id="
				+ index_id + ", description=" + description + ", total_score="
				+ total_score + ", index_total_score=" + index_total_score
				+ ", sql=" + sql + ", DBTYPE=" + DBTYPE + "]";
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	
}
