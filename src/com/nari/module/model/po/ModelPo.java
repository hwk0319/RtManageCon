package com.nari.module.model.po;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;

public class ModelPo extends BasePo{
	
	//编号
	private Integer id; 
	//设备类型 1:服务器 2:交换机 3:IB卡 4:IP卡 5:SSD 6:磁盘 7:RAID卡
	private Integer devicetype ;
	//型号
	@Size(min=1,max=60,message="型号请输入1到60个字符！")
	private String model;
	//端口数
	private Integer portnum;
	//容量
	private Integer capacity;
	//创建者
	@JsonIgnore
	private Integer create_by;
	//创建日期
	@JsonIgnore
	private Date create_date ;
	//修改者
	@JsonIgnore
	private Integer update_by;
	//修改日期
	@JsonIgnore
	private Date update_date;	
	//备注
	private String remark;
	//使用标识
	@JsonIgnore
	private Boolean use_flag;
	//厂商
	private String factory;
	private Integer model_id;
	@JsonIgnore
	private String DBTYPE;
	private String mData;
	private String mid;
	private String mdevicetype;
	private String mportnum;
	private String mcapacity;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public Integer getModel_id() {
		return model_id;
	}
	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(Integer devicetype) {
		this.devicetype = devicetype;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getPortnum() {
		return portnum;
	}
	public void setPortnum(Integer portnum) {
		this.portnum = portnum;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
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
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMdevicetype() {
		return mdevicetype;
	}
	public void setMdevicetype(String mdevicetype) {
		this.mdevicetype = mdevicetype;
	}
	public String getMportnum() {
		return mportnum;
	}
	public void setMportnum(String mportnum) {
		this.mportnum = mportnum;
	}
	public String getMcapacity() {
		return mcapacity;
	}
	public void setMcapacity(String mcapacity) {
		this.mcapacity = mcapacity;
	}
}
