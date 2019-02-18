package com.nari.module.deviceindex.po;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;

public class DeviceindexPo extends BasePo{
	// 编号
	private Integer id;
	// 统一ID
	@Size(min=1,max=64,message="设备UID长度不能超过64个字符！")
	private String uid;
	// 指标分类ID
	private Integer indextype_id;
	// 指标分类名称
	private String diname;
	// 采集方式
	private Integer collect;
	//采集方式名称
	private String cname;
	// 周期
	@Size(min=1,max=64,message="周期长度不能超过64个字符！")
	private String period;
	// 设备名称
	private String brand;
	// 设备类型
	private String devicetype;
	// 创建者
	@JsonIgnore
	private Integer create_by;
	// 创建日期
	@JsonIgnore
	private Date create_date;
	// 修改者
	@JsonIgnore
	private Integer update_by;
	// 修改日期
	@JsonIgnore
	private Date update_date;
	// 备注
	private String remark;
	// 使用标识
	@JsonIgnore
	private Boolean use_flag;
	// uid取值类型：设备表（mgt_device）/软件系统管理表（mgt_system）
	private String type;
	//主机名
	private String device_name;
	@JsonIgnore
	private String DBTYPE;
	private String tabname;
	private String mid;
	private String mindextype_id;
	private String mdata;
	private String mcollect;
	private String isBol;
	//位置信息
	private String position;
	
	public String getTabname() {
		return tabname;
	}
	public void setTabname(String tabname) {
		this.tabname = tabname;
	}

	private String uid_str;
	private String id_str;
	private List<String> uids;
	
	public List<String> getUids() {
		return uids;
	}
	public void setUids(List<String> uids) {
		this.uids = uids;
	}
	public String getUid_str() {
		return uid_str;
	}
	public void setUid_str(String uid_str) {
		this.uid_str = uid_str;
	}
	public String getId_str() {
		return id_str;
	}
	public void setId_str(String id_str) {
		this.id_str = id_str;
	}
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	/**
	 * 非本表字段-start
	 */
	private String name;
	private String ret_arr;//存储设备/软件系统信息
	/**
	 * 非本表字段-end
	 */
	
	public String getName() {
		return name;
	}
	public String getRet_arr() {
		return ret_arr;
	}
	public void setRet_arr(String ret_arr) {
		this.ret_arr = ret_arr;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getUse_flag() {
		return use_flag;
	}

	public void setUse_flag(Boolean use_flag) {
		this.use_flag = use_flag;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getCollect() {
		return collect;
	}

	public void setCollect(Integer collect) {
		this.collect = collect;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Integer getIndextype_id() {
		return indextype_id;
	}

	public void setIndextype_id(Integer indextype_id) {
		this.indextype_id = indextype_id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}

	public String getDiname() {
		return diname;
	}

	public void setDiname(String diname) {
		this.diname = diname;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
	@Override
	public String toString() {
		return "DeviceindexPo [id=" + id + ", uid=" + uid + ", indextype_id="
				+ indextype_id + ", diname=" + diname + ", collect=" + collect
				+ ", cname=" + cname + ", period=" + period + ", brand="
				+ brand + ", devicetype=" + devicetype + ", create_by="
				+ create_by + ", create_date=" + create_date + ", update_by="
				+ update_by + ", update_date=" + update_date + ", remark="
				+ remark + ", use_flag=" + use_flag + ", type=" + type
				+ ", device_name=" + device_name + ", DBTYPE=" + DBTYPE
				+ ", name=" + name + ", ret_arr=" + ret_arr + "]";
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMindextype_id() {
		return mindextype_id;
	}
	public void setMindextype_id(String mindextype_id) {
		this.mindextype_id = mindextype_id;
	}
	public String getMcollect() {
		return mcollect;
	}
	public void setMcollect(String mcollect) {
		this.mcollect = mcollect;
	}
	public String getMdata() {
		return mdata;
	}
	public void setMdata(String mdata) {
		this.mdata = mdata;
	}
	public String getIsBol() {
		return isBol;
	}
	public void setIsBol(String isBol) {
		this.isBol = isBol;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
