package com.nari.module.doublemgt.po;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;

/**
 * 
 * @author sz
 * @date 2017年5月12日
 * @Description
 *
 */
public class DoublemgtPo extends BasePo{
    //编号
	private Integer id;
	//统一ID
	private String uid;
	//名称
	@Size(min=1,max=60,message="名称请输入1到60个字符！")
	private String name;
	//描述
	@Size(min=0,max=100,message="描述请输入0到100个字符！")
	private String description;
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
	private String remark;
	//组ID
	private Integer group_id;
	//具体类型
	private Integer doubtype;
	//中心标示
	private String core_tagging;
	//中心名称
	private String core;
	//组名称
	private String group_name;
	//组备注
	private String group_description;
	//双活ID
	private Integer double_id;
	//中心ID
	private Integer core_id;
	@JsonIgnore
	private String DBTYPE;
	private String mData;
	//第二个中心名称
	@Size(min=1,max=60,message="第二个中心名称请输入1到60个字符！")
	private String core_r;
	//第一个中心名称
	@Size(min=1,max=60,message="第一个中心名称请输入1到60个字符！")
	private String core_l;
	private String group_ids_L;
	private String group_ids_R;
	
	private Integer id_L;
	private Integer id_R;
	//位置信息
	private String position;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public Integer getCore_id() {
		return core_id;
	}
	public void setCore_id(Integer core_id) {
		this.core_id = core_id;
	}
	public Integer getDouble_id() {
		return double_id;
	}
	public void setDouble_id(Integer double_id) {
		this.double_id = double_id;
	}
	public String getCore_tagging() {
		return core_tagging;
	}
	public void setCore_tagging(String core_tagging) {
		this.core_tagging = core_tagging;
	}
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getGroup_description() {
		return group_description;
	}
	public void setGroup_description(String group_description) {
		this.group_description = group_description;
	}
	public Integer getDoubtype() {
		return doubtype;
	}
	public void setDoubtype(Integer doubtype) {
		this.doubtype = doubtype;
	}
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	@Override
	public String toString() {
		return "DoublemgtPo [id=" + id + ", uid=" + uid + ", name=" + name
				+ ", description=" + description + ", use_flag=" + use_flag
				+ ", create_by=" + create_by + ", create_date=" + create_date
				+ ", update_by=" + update_by + ", update_date=" + update_date
				+ ", remark=" + remark + ", group_id=" + group_id
				+ ", doubtype=" + doubtype + ", core_tagging=" + core_tagging
				+ ", core=" + core + ", group_name=" + group_name
				+ ", group_description=" + group_description + ", double_id="
				+ double_id + ", core_id=" + core_id + ", DBTYPE=" + DBTYPE
				+ "]";
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public String getCore_r() {
		return core_r;
	}
	public void setCore_r(String core_r) {
		this.core_r = core_r;
	}
	public String getCore_l() {
		return core_l;
	}
	public void setCore_l(String core_l) {
		this.core_l = core_l;
	}
	public String getGroup_ids_L() {
		return group_ids_L;
	}
	public void setGroup_ids_L(String group_ids_L) {
		this.group_ids_L = group_ids_L;
	}
	public String getGroup_ids_R() {
		return group_ids_R;
	}
	public void setGroup_ids_R(String group_ids_R) {
		this.group_ids_R = group_ids_R;
	}
	public Integer getId_R() {
		return id_R;
	}
	public void setId_R(Integer id_R) {
		this.id_R = id_R;
	}
	public Integer getId_L() {
		return id_L;
	}
	public void setId_L(Integer id_L) {
		this.id_L = id_L;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
