package com.nari.module.systemmgt.po;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;
/**
 * 
 * @author sz
 * @date 2017年5月9日
 * @Description
 *
 */
public class SystemmgtPo extends BasePo{

	//编号
	private Integer id;
	//系统类别1：数据库系统 2：文件系统 3：备份系统 4：数据库资源池
	//操作系统
	@Max(value = 9999, message = "操作系统值不能大于9999")
	@Min(value = 0, message = "操作系统值不能小于0")
	private Integer systype;
	//统一ID
	private String uid;
	//具体类型
	@Size(min=1,max=10,message="具体类型长度不能超过10个字符！")
	@Pattern(regexp = "^[0-9]*$", message = "具体类型错误！")
	private String type;
	//名称
	@Size(min=1,max=30,message="名称请输入1到30个字符！")
	private String name;
	//管理IP
	@Pattern(regexp = "^(?:(?:2[0-4][0-9].)|(?:25[0-5].)|(?:1[0-9][0-9].)|(?:[1-9][0-9].)|(?:[0-9].)){3}(?:(?:2[0-5][0-5])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$", message = "管理IP格式错误！")
	private String ip;
	//用户名
	private String username;
	//密码
	private String mm;
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
	//周期
	private String period;
	//指标类ID
	private Integer indextype_id;
	private Integer group_id;
	private Integer dev_id;
	//id集合
	private String ids;
	//id List集合 防止SQL注入
	private List<String> idList;
	//应用IP
	private String in_ip;
	//数据库实例名
	@Size(min=1,max=64,message="数据库实例名请输入1到64个字符！")
	private String reserver1;
	//设备账号
	private String reserver2;
	//设备密码
	@JsonIgnore
	private String reserver3;
	@JsonIgnore
	private String DBTYPE;
	@JsonIgnore
	private String password;
	private String port;
	//位置信息
	private String position;
	
	//软件名称
	private String systemName;
	//软件ip
	private String systemIp;
	
	private String mData;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSystype() {
		return systype;
	}
	public void setSystype(Integer systype) {
		this.systype = systype;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public Integer getDev_id() {
		return dev_id;
	}
	public void setDev_id(Integer dev_id) {
		this.dev_id = dev_id;
	}
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getIn_ip() {
		return in_ip;
	}
	public void setIn_ip(String in_ip) {
		this.in_ip = in_ip;
	}
	public String getReserver1() {
		return reserver1;
	}
	public void setReserver1(String reserver1) {
		this.reserver1 = reserver1;
	}
	public String getReserver2() {
		return reserver2;
	}
	public void setReserver2(String reserver2) {
		this.reserver2 = reserver2;
	}
	public String getReserver3() {
		return reserver3;
	}
	public void setReserver3(String reserver3) {
		this.reserver3 = reserver3;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSystemIp() {
		return systemIp;
	}
	public void setSystemIp(String systemIp) {
		this.systemIp = systemIp;
	}
	@Override
	public String toString() {
		return "SystemmgtPo [id=" + id + ", systype=" + systype + ", uid="
				+ uid + ", type=" + type + ", name=" + name + ", ip=" + ip
				+ ", username=" + username + ", mm=" + mm.toString() + ", use_flag="
				+ use_flag + ", create_by=" + create_by + ", create_date="
				+ create_date + ", update_by=" + update_by + ", update_date="
				+ update_date + ", remark=" + remark + ", period=" + period
				+ ", indextype_id=" + indextype_id + ", group_id=" + group_id
				+ ", dev_id=" + dev_id + ", ids=" + ids + ", in_ip=" + in_ip
				+ ", reserver1=" + reserver1 + ", reserver2=" + reserver2
				+ ", reserver3=" + reserver3 + ", DBTYPE="
				+ DBTYPE + "]";
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public List<String> getIdList() {
		return idList;
	}
	public void setIdList(List<String> idList) {
		this.idList = idList;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
