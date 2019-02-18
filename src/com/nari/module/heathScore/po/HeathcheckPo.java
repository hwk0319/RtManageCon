package com.nari.module.heathScore.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;

/**
 * 
 * @author Administrator
 * @date 2017年7月7日
 * @Description 健康评分
 *
 */
public class HeathcheckPo extends BasePo{
	private Integer id;
	//id(table id = health_check_id)
	private Integer health_check_id;
	//评估目标id
	private String target_id;
	//模型id
	private Integer model_id;
	//模型名称
	private String model_name;
	//评估起点时间
	private Date begin_time;
	//评估截止时间
	private Date end_time;
	//周期
	private String cron;
	//评估得分
	private Integer total_score;
	//描述
	private String description;
	//状态，0等待开始，1正在进行评估，2评估完成
	private Integer status;
	//更新时间
	@JsonIgnore
	private Date update_time;
	@JsonIgnore
	private boolean use_flag;
	private String begintime;
	private String endtime;
	//评估目标搜索
	private String targetText;
	//评估目标名称
	private String system_name;
	//总分
	private Integer totalScore;
	//s时间
	private String time;
	//等级得分
//	private Integer perf_score;
	//最高分
	private Integer maxScore;
	//最低分
	private Integer minScore;
	//平局分
	private double averageScore;
	
	//性能评分id
	private Integer eva_id;
	//性能得分
	private Integer perf_score;
	private Integer perfScore;
	private String start;
	private String end;
	@JsonIgnore
	private String DBTYPE;
	private String str;
	private String mData;
	private String ids;
	private String record_time;
	private String model_item_id;
	private String metric_id;
	//位置信息
	private String position;
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public Integer getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}
	public Integer getMinScore() {
		return minScore;
	}
	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
	}
	public double getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}
	public Integer getHealth_check_id() {
		return health_check_id;
	}
	public void setHealth_check_id(Integer health_check_id) {
		this.health_check_id = health_check_id;
	}
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public Integer getModel_id() {
		return model_id;
	}
	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
	}
	public Date getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public Integer getTotal_score() {
		return total_score;
	}
	public void setTotal_score(Integer total_score) {
		this.total_score = total_score;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public boolean isUse_flag() {
		return use_flag;
	}
	public void setUse_flag(boolean use_flag) {
		this.use_flag = use_flag;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getSystem_name() {
		return system_name;
	}
	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public String getTargetText() {
		return targetText;
	}
	public void setTargetText(String targetText) {
		this.targetText = targetText;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "HeathcheckPo [id=" + id + ", health_check_id="
				+ health_check_id + ", target_id=" + target_id + ", model_id="
				+ model_id + ", model_name=" + model_name + ", begin_time="
				+ begin_time + ", end_time=" + end_time + ", cron=" + cron
				+ ", total_score=" + total_score + ", description="
				+ description + ", status=" + status + ", update_time="
				+ update_time + ", use_flag=" + use_flag + ", begintime="
				+ begintime + ", endtime=" + endtime + ", targetText="
				+ targetText + ", system_name=" + system_name + ", totalScore="
				+ totalScore + ", time=" + time + "]";
	}
	public Integer getPerf_score() {
		return perf_score;
	}
	public void setPerf_score(Integer perf_score) {
		this.perf_score = perf_score;
	}
	public Integer getEva_id() {
		return eva_id;
	}
	public void setEva_id(Integer eva_id) {
		this.eva_id = eva_id;
	}
	public Integer getPerfScore() {
		return perfScore;
	}
	public void setPerfScore(Integer perfScore) {
		this.perfScore = perfScore;
	}
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = dBTYPE;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
	public String getModel_item_id() {
		return model_item_id;
	}
	public void setModel_item_id(String model_item_id) {
		this.model_item_id = model_item_id;
	}
	public String getMetric_id() {
		return metric_id;
	}
	public void setMetric_id(String metric_id) {
		this.metric_id = metric_id;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
}
