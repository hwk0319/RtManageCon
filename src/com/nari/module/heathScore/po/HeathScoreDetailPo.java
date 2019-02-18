package com.nari.module.heathScore.po;
/**
 * 
 * @author Administrator
 * @date 2017年7月14日
 * @Description 健康评估明细表
 *
 */
public class HeathScoreDetailPo {
	private Integer health_check_id;
	private Integer model_id;
	private Integer metric_id;
	private Integer model_item_id;
	//指标重要性等级
	private Integer severity;
	private Integer deduct;
	private String record_time;
	//建议
	private String advise;
	private String metric_value;
	private String model_name;
	private String model_item_name;
	private String mon_indexname;
	
	public Integer getHealth_check_id() {
		return health_check_id;
	}
	public void setHealth_check_id(Integer health_check_id) {
		this.health_check_id = health_check_id;
	}
	public Integer getMetric_id() {
		return metric_id;
	}
	public void setMetric_id(Integer metric_id) {
		this.metric_id = metric_id;
	}
	public Integer getSeverity() {
		return severity;
	}
	public void setSeverity(Integer severity) {
		this.severity = severity;
	}
	public String getAdvise() {
		return advise;
	}
	public void setAdvise(String advise) {
		this.advise = advise;
	}
	public Integer getModel_item_id() {
		return model_item_id;
	}
	public void setModel_item_id(Integer model_item_id) {
		this.model_item_id = model_item_id;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public Integer getModel_id() {
		return model_id;
	}
	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
	}
	public Integer getDeduct() {
		return deduct;
	}
	public void setDeduct(Integer deduct) {
		this.deduct = deduct;
	}
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
	public String getMetric_value() {
		return metric_value;
	}
	public void setMetric_value(String metric_value) {
		this.metric_value = metric_value;
	}
	public String getModel_item_name() {
		return model_item_name;
	}
	public void setModel_item_name(String model_item_name) {
		this.model_item_name = model_item_name;
	}
	public String getMon_indexname() {
		return mon_indexname;
	}
	public void setMon_indexname(String mon_indexname) {
		this.mon_indexname = mon_indexname;
	}
	@Override
	public String toString() {
		return "HeathScoreDetailPo [health_check_id=" + health_check_id
				+ ", model_id=" + model_id + ", metric_id=" + metric_id
				+ ", model_item_id=" + model_item_id + ", severity=" + severity
				+ ", deduct=" + deduct + ", record_time=" + record_time
				+ ", advise=" + advise + ", metric_value=" + metric_value
				+ ", model_name=" + model_name + ", model_item_name="
				+ model_item_name + ", mon_indexname=" + mon_indexname
				+ "]";
	}
	
}
