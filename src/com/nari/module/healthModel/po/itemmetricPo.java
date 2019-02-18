package com.nari.module.healthModel.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.util.MyPropertiesPersist;
/**
 * 模型分项
 * @date：2017年7月7日
 */
public class itemmetricPo {
	private Integer model_item_id;//分项id
	private Integer metric_id;//指标id
	private List<String> metricIdList = new ArrayList<String>();
	private String metric_id_name;//指标名称
	private Integer warn_level_1;//告警等级1扣分值
	private Integer warn_level_2;//告警等级2扣分值
	private Integer warn_level_3;//告警等级3扣分值
	private Integer warn_level_4;//告警等级4扣分值
	private Integer total_score;//总分
	@JsonIgnore
	private Boolean use_flag;
	@JsonIgnore
	private String create_by;
	@JsonIgnore
	private Date create_date;
	@JsonIgnore
	private String update_by;
	@JsonIgnore
	private Date update_date;
	@JsonIgnore
	private String DBTYPE;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public Integer getModel_item_id() {
		return model_item_id;
	}
	public void setModel_item_id(Integer model_item_id) {
		this.model_item_id = model_item_id;
	}
	public Integer getMetric_id() {
		return metric_id;
	}
	public void setMetric_id(Integer metric_id) {
		this.metric_id = metric_id;
	}
	
	public String getMetric_id_name() {
		return metric_id_name;
	}
	public void setMetric_id_name(String metric_id_name) {
		this.metric_id_name = metric_id_name;
	}
	public Integer getWarn_level_1() {
		return warn_level_1;
	}
	public void setWarn_level_1(Integer warn_level_1) {
		this.warn_level_1 = warn_level_1;
	}
	public Integer getWarn_level_2() {
		return warn_level_2;
	}
	public void setWarn_level_2(Integer warn_level_2) {
		this.warn_level_2 = warn_level_2;
	}
	public Integer getWarn_level_3() {
		return warn_level_3;
	}
	public void setWarn_level_3(Integer warn_level_3) {
		this.warn_level_3 = warn_level_3;
	}
	public Integer getWarn_level_4() {
		return warn_level_4;
	}
	public void setWarn_level_4(Integer warn_level_4) {
		this.warn_level_4 = warn_level_4;
	}
	public Integer getTotal_score() {
		return total_score;
	}
	public void setTotal_score(Integer total_score) {
		this.total_score = total_score;
	}
	public Boolean getUse_flag() {
		return use_flag;
	}
	public void setUse_flag(Boolean use_flag) {
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
	public List<String> getMetricIdList() {
		return metricIdList;
	}
	public void setMetricIdList(List<String> metricIdList) {
		this.metricIdList = metricIdList;
	}
	
}
