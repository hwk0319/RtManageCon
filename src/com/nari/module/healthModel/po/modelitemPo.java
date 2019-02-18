package com.nari.module.healthModel.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.util.MyPropertiesPersist;
/**
 * 模型分项指标
 * @date：2017年7月7日
 */
public class modelitemPo {
	private Integer model_item_id;//分项id
	private String model_item_name;//分项名称
	private String model_item_desc;//分享描述
	private Integer total_score;//总分
	private Integer model_id;//模型id
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
	private Integer totalScore;
	private String metric_ids;
	private List<String> metricIdsList = new ArrayList<>();
	private String wid;
	
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
	public String getModel_item_name() {
		return model_item_name;
	}
	public void setModel_item_name(String model_item_name) {
		this.model_item_name = model_item_name;
	}
	public String getModel_item_desc() {
		return model_item_desc;
	}
	public void setModel_item_desc(String model_item_desc) {
		this.model_item_desc = model_item_desc;
	}
	public Integer getTotal_score() {
		return total_score;
	}
	public void setTotal_score(Integer total_score) {
		this.total_score = total_score;
	}
	public Integer getModel_id() {
		return model_id;
	}
	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
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
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public String getMetric_ids() {
		return metric_ids;
	}
	public void setMetric_ids(String metric_ids) {
		this.metric_ids = metric_ids;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public List<String> getMetricIdsList() {
		return metricIdsList;
	}
	public void setMetricIdsList(List<String> metricIdsList) {
		this.metricIdsList = metricIdsList;
	}
	@Override
	public String toString() {
		return "modelitemPo [model_item_id=" + model_item_id
				+ ", model_item_name=" + model_item_name + ", model_item_desc="
				+ model_item_desc + ", total_score=" + total_score
				+ ", model_id=" + model_id + ", use_flag=" + use_flag
				+ ", create_by=" + create_by + ", create_date=" + create_date
				+ ", update_by=" + update_by + ", update_date=" + update_date
				+ ", DBTYPE=" + DBTYPE + ", totalScore=" + totalScore + "]";
	}
}
