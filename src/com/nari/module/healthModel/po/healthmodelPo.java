package com.nari.module.healthModel.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.util.MyPropertiesPersist;

/**
 * 健康模型
 * @date：2017年7月7日
 */
public class healthmodelPo {
	private Integer model_id;//模型id
	private String model_name;//模型名称
	private String model_desc;//描述
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
	private Integer status;//状态，0等待开始，1正在进行评估，2评估完成
	
	private String model_item_name;//分项名称
	@JsonIgnore
	private String DBTYPE;
	private String index_ids;
	private List<String> indexIdsList = new ArrayList<>();
	private String index_type;
	private String tabname;
	private String modeStr;
	private String item_metric;
	private String del_item_arr;
	private String del_metric_arr;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public String getModel_item_name() {
		return model_item_name;
	}

	public void setModel_item_name(String model_item_name) {
		this.model_item_name = model_item_name;
	}

	public Integer getModel_id() {
		return model_id;
	}

	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getModel_desc() {
		return model_desc;
	}

	public void setModel_desc(String model_desc) {
		this.model_desc = model_desc;
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
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIndex_ids() {
		return index_ids;
	}
	public void setIndex_ids(String index_ids) {
		this.index_ids = index_ids;
	}
	public String getIndex_type() {
		return index_type;
	}
	public void setIndex_type(String index_type) {
		this.index_type = index_type;
	}
	public String getTabname() {
		return tabname;
	}
	public void setTabname(String tabname) {
		this.tabname = tabname;
	}
	public String getModeStr() {
		return modeStr;
	}
	public void setModeStr(String modeStr) {
		this.modeStr = modeStr;
	}
	public String getItem_metric() {
		return item_metric;
	}
	public void setItem_metric(String item_metric) {
		this.item_metric = item_metric;
	}
	public String getDel_item_arr() {
		return del_item_arr;
	}
	public void setDel_item_arr(String del_item_arr) {
		this.del_item_arr = del_item_arr;
	}
	public String getDel_metric_arr() {
		return del_metric_arr;
	}
	public void setDel_metric_arr(String del_metric_arr) {
		this.del_metric_arr = del_metric_arr;
	}
	public List<String> getIndexIdsList() {
		return indexIdsList;
	}
	public void setIndexIdsList(List<String> indexIdsList) {
		this.indexIdsList = indexIdsList;
	}
}
