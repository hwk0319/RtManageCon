package com.nari.module.device.po;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.monitormgt.monihome.servers.po.PagePo;
import com.nari.util.MyPropertiesPersist;
/**
 * 
 * @author Administrator
 * @date 2017年5月8日
 * @Description设备管理
 *
 */
public class DevicesPo extends PagePo{
	//编号
	private Integer id;
	//统一ID
	private String uid;
	//类型
	@Max(value = 9999, message = "设备类型值不能大于9999")
	@Min(value = 0, message = "设备类型值不能小于0")
	private Integer devicetype;
	//厂商信息
	@NotNull(message="请选择厂商！")
	@Pattern(regexp = "^[0-9]*$", message = "厂商类型错误！")
	@Size(min=1,max=10,message="厂商长度不能超过10个字符！")
	private String factory;
	//SN码
	@NotNull(message="SN码不能为空！")
	@Size(min=1,max=60,message="SN码长度不能超过60个字符！")
	private String sn;
	//设备型号
	private Integer model_id;
	//型号
	@NotNull(message="请选择型号！")
	@Size(min=1,max=20,message="型号长度不能超过20个字符！")
	private String model;
	//应用IP
	@NotNull(message="应用IP不能为空！")
	@Pattern(regexp = "^(?:(?:2[0-4][0-9].)|(?:25[0-5].)|(?:1[0-9][0-9].)|(?:[1-9][0-9].)|(?:[0-9].)){3}(?:(?:2[0-5][0-5])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$", message = "应用IP格式错误！")
	private String in_ip;
	//应用用户名
	@NotNull(message="应用用户名不能为空！")
	private String in_username;
	//应用密码
	@NotNull(message="应用密码不能为空！")
	@JsonIgnore
	private String in_password;
	//带外IP
	@Pattern(regexp = "^(?:(?:2[0-4][0-9].)|(?:25[0-5].)|(?:1[0-9][0-9].)|(?:[1-9][0-9].)|(?:[0-9].)){3}(?:(?:2[0-5][0-5])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$", message = "带外IP格式错误！")
	@NotNull(message="带外IP不能为空！")
	private String out_ip;
	//带外用户名
	@NotNull(message="带外用户名不能为空！")
	private String out_username;
	//带外密码
	@NotNull(message="带外密码不能为空！")
	@JsonIgnore
	private String out_password;
	//操作系统
	@Max(value = 9999, message = "操作系统值不能大于9999")
	@Min(value = 0, message = "操作系统值不能小于0")
	private Integer opersys;
	//资产编号
	@Size(min=0,max=30,message="资产编号长度不能超过30个字符！")
	private String assetno;
	//位置信息
	@Size(min=0,max=30,message="位置信息长度不能超过30个字符！")
	private String position;
	private String positionName;
	//1-在用0-已删除
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
	//group_id
	private Integer group_id;
	//sys_id
	private Integer sys_id;
	//dev_id
	private Integer dev_id;
	//主机名
	@NotNull(message="主机名不能为空！")
	@Size(min=0,max=30,message="主机名长度不能超过30个字符！")
	private String name;
	//父级id
	private String parent_id;
	//字典名称
	private String dictname;
	private String devicetypename;
	private String factoryname;
	private String opersysname;
	//搜索内容
	private String searchText;
	//周期
	private String period;
	//指标类ID
	private Integer indextype_id;
	//组合公用id,区别不同组合展示不同设备
	private Integer grpcomid;
	
	//新增设备字段：健康状态，温度，寿命，端口
	//健康状态
	private String healthstatus;
	//温度
	private Integer temperature;
	//寿命
	private String life;
	//端口
	private String port;
	//ids 用来存放多个id;用,隔开(1,2,3...)
	private String ids;
	private List<String> idList;
	//服务器状态
	private String serverstatus;
	//是否本机
	private String IsOrNot;
	@JsonIgnore
	private String DBTYPE;
	private String mData;
	private String jwt;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public String getIsOrNot() {
		return IsOrNot;
	}
	public void setIsOrNot(String isOrNot) {
		IsOrNot = isOrNot;
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
	public Integer getDev_id() {
		return dev_id;
	}
	public void setDev_id(Integer dev_id) {
		this.dev_id = dev_id;
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
	public Integer getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(Integer devicetype) {
		this.devicetype = devicetype;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Integer getModel_id() {
		return model_id;
	}
	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
	}
	public String getIn_ip() {
		return in_ip;
	}
	public void setIn_ip(String in_ip) {
		this.in_ip = in_ip;
	}
	public String getIn_username() {
		return in_username;
	}
	public void setIn_username(String in_username) {
		this.in_username = in_username;
	}
	public String getOut_ip() {
		return out_ip;
	}
	public void setOut_ip(String out_ip) {
		this.out_ip = out_ip;
	}
	public String getOut_username() {
		return out_username;
	}
	public void setOut_username(String out_username) {
		this.out_username = out_username;
	}
	public Integer getOpersys() {
		return opersys;
	}
	public void setOpersys(Integer opersys) {
		this.opersys = opersys;
	}
	public String getAssetno() {
		return assetno;
	}
	public void setAssetno(String assetno) {
		this.assetno = assetno;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
	
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public Integer getSys_id() {
		return sys_id;
	}
	public void setSys_id(Integer sys_id) {
		this.sys_id = sys_id;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getGrpcomid() {
		return grpcomid;
	}
	public void setGrpcomid(Integer grpcomid) {
		this.grpcomid = grpcomid;
	}
	public String getHealthstatus() {
		return healthstatus;
	}
	public void setHealthstatus(String healthstatus) {
		this.healthstatus = healthstatus;
	}
	public Integer getTemperature() {
		return temperature;
	}
	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}
	public String getLife() {
		return life;
	}
	public void setLife(String life) {
		this.life = life;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDictname() {
		return dictname;
	}
	public void setDictname(String dictname) {
		this.dictname = dictname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFactoryname() {
		return factoryname;
	}
	public void setFactoryname(String factoryname) {
		this.factoryname = factoryname;
	}
	public String getDevicetypename() {
		return devicetypename;
	}
	public void setDevicetypename(String devicetypename) {
		this.devicetypename = devicetypename;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getOpersysname() {
		return opersysname;
	}
	public void setOpersysname(String opersysname) {
		this.opersysname = opersysname;
	}
	public String getServerstatus() {
		return serverstatus;
	}
	public void setServerstatus(String serverstatus) {
		this.serverstatus = serverstatus;
	}
	public String getIn_password() {
		return in_password;
	}
	public void setIn_password(String in_password) {
		this.in_password = in_password;
	}
	public String getOut_password() {
		return out_password;
	}
	public void setOut_password(String out_password) {
		this.out_password = out_password;
	}
	@Override
	public String toString() {
		return "DevicesPo [id=" + id + ", uid=" + uid + ", devicetype="
				+ devicetype + ", sn=" + sn + ", model_id=" + model_id
				+ ", in_ip=" + in_ip + ", in_username=" + in_username
				+ ", in_password=" + in_password.toString() + ", out_ip=" + out_ip
				+ ", out_username=" + out_username + ", out_password="
				+ out_password.toString() + ", opersys=" + opersys + ", assetno="
				+ assetno + ", position=" + position + ", use_flag=" + use_flag
				+ ", create_by=" + create_by + ", create_date=" + create_date
				+ ", update_by=" + update_by + ", update_date=" + update_date
				+ ", remark=" + remark + ", group_id=" + group_id + ", sys_id="
				+ sys_id + ", dev_id=" + dev_id + ", name=" + name + ", model="
				+ model + ", parent_id=" + parent_id + ", factory=" + factory
				+ ", dictname=" + dictname + ", devicetypename="
				+ devicetypename + ", factoryname=" + factoryname
				+ ", opersysname=" + opersysname + ", searchText=" + searchText
				+ ", period=" + period + ", indextype_id=" + indextype_id
				+ ", grpcomid=" + grpcomid + ", healthstatus=" + healthstatus
				+ ", temperature=" + temperature + ", life=" + life + ", port="
				+ port + ", ids=" + ids + ", serverstatus=" + serverstatus
				+ ", IsOrNot=" + IsOrNot + ", DBTYPE=" + DBTYPE + "]";
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public List<String> getIdList() {
		return idList;
	}
	public void setIdList(List<String> idList) {
		this.idList = idList;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
}
