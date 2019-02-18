package com.nari.module.group.po;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;


/**
 * 
 * @author Administrator
 * @date 2017年5月4日
 * @Description
 *
 */
public class GroupPo extends BasePo{
	//编号
	private Integer id;
	//统一ID
	private String uid;
	//名称
	@Size(min=1,max=50,message="名称请输入1到50个字符！")
	private String name;
	//描述
	@Size(min=0,max=200,message="描述请输入1到200个字符！")
	private String description;
	//1-在用0-已删除
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
	//双活ID
	private Integer double_id;
	//y一体机ID
	private Integer integrate_id;
	//集群ID
	private Integer cluster_id;
	//设备ID
	private Integer device_id;
	//具体类型
	private Integer grotype;
	//应用IP
	private String in_ip;
	//id 集合
	private String ids;
	//id List集合
	private List<String> idList;
	//中心ID
	private Integer core_id;
	//中心标示
	private String core_tagging;
	//类型名称
	private String grotypeName;
	//组类型
	private Integer grouptype;
	//组类型名称
	private String grouptypeName;
	//位置信息
	private String position;
	@JsonIgnore
	private String DBTYPE;
	private String mData;
	private String mid;
	private String mgrouptype;
	private String mgrotype;
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMgrouptype() {
		return mgrouptype;
	}
	public void setMgrouptype(String mgrouptype) {
		this.mgrouptype = mgrouptype;
	}
	public String getMgrotype() {
		return mgrotype;
	}
	public void setMgrotype(String mgrotype) {
		this.mgrotype = mgrotype;
	}
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
	public String getCore_tagging() {
		return core_tagging;
	}
	public void setCore_tagging(String core_tagging) {
		this.core_tagging = core_tagging;
	}
	public Integer getIntegrate_id() {
		return integrate_id;
	}
	public void setIntegrate_id(Integer integrate_id) {
		this.integrate_id = integrate_id;
	}
	public Integer getGrotype() {
		return grotype;
	}
	public void setGrotype(Integer grotype) {
		this.grotype = grotype;
	}
	public Integer getCluster_id() {
		return cluster_id;
	}
	public void setCluster_id(Integer cluster_id) {
		this.cluster_id = cluster_id;
	}
	public Integer getDouble_id() {
		return double_id;
	}
	public void setDouble_id(Integer double_id) {
		this.double_id = double_id;
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
		return "GroupPo [id=" + id + ", uid=" + uid + ", name=" + name
				+ ", description=" + description + ", use_flag=" + use_flag
				+ ", create_by=" + create_by + ", create_date=" + create_date
				+ ", update_by=" + update_by + ", update_date=" + update_date
				+ ", remark=" + remark + "]";
	}
	public Integer getDevice_id() {
		return device_id;
	}
	public void setDevice_id(Integer device_id) {
		this.device_id = device_id;
	}
	public String getIn_ip() {
		return in_ip;
	}
	public void setIn_ip(String in_ip) {
		this.in_ip = in_ip;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getGrotypeName() {
		return grotypeName;
	}
	public void setGrotypeName(String grotypeName) {
		this.grotypeName = grotypeName;
	}
	public Integer getGrouptype() {
		return grouptype;
	}
	public void setGrouptype(Integer grouptype) {
		this.grouptype = grouptype;
	}
	public String getGrouptypeName() {
		return grouptypeName;
	}
	public void setGrouptypeName(String grouptypeName) {
		this.grouptypeName = grouptypeName;
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
}
