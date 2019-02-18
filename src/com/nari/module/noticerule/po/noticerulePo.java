package com.nari.module.noticerule.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;
/**
 * 通知规则
 * @author：ldj
 * @date：2017年5月16日
 */
public class noticerulePo extends BasePo{
	/** 编号. */
	private Integer id;
	/** 告警类型. */
	private Integer type;
	/** 告警等级. */
	private Integer level;
	/** 联系人编号 */
	private Integer address_id;
	/** 通知方式. */
	private Integer way;
	/** 使用标识. */
	@JsonIgnore
	private Boolean use_flag;
	/** 创建者. */
	@JsonIgnore
	private String create_by;
	/** 创建日期. */
	@JsonIgnore
	private Date create_date;
	/** 修改者. */
	@JsonIgnore
	private String update_by;
	/** 修改日期. */
	@JsonIgnore
	private Date update_date;
	/** 备注. */
	private String remark;
	private String notice_bh;
	@JsonIgnore
	private String DBTYPE;
	private String mid;
	private String mtype;
	private String mlevel;
	private String mway;
	private String maddress_id;
	private String mData;
	private String warntypeName;
	private String warnlevelName;
	private String wayName;
	private String address_name;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}

	public String getNotice_bh() {
		return notice_bh;
	}

	public void setNotice_bh(String notice_bh) {
		this.notice_bh = notice_bh;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getAddress_id() {
		return address_id;
	}

	public void setAddress_id(Integer address_id) {
		this.address_id = address_id;
	}

	public Integer getWay() {
		return way;
	}

	public void setWay(Integer way) {
		this.way = way;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getMlevel() {
		return mlevel;
	}
	public void setMlevel(String mlevel) {
		this.mlevel = mlevel;
	}
	public String getMway() {
		return mway;
	}
	public void setMway(String mway) {
		this.mway = mway;
	}
	public String getMaddress_id() {
		return maddress_id;
	}
	public void setMaddress_id(String maddress_id) {
		this.maddress_id = maddress_id;
	}
	@Override
	public String toString() {
		return "noticerulePo [id=" + id + ", type=" + type + ", level="
				+ level + ", address_id=" + address_id + ", way=" + way
				+ ", use_flag=" + use_flag + ", use_flag=" + use_flag + ", create_by="
				+ create_by + ", create_date=" + create_date + ", update_by="
				+ update_by + ", update_date=" + update_date + ", remark="
				+ remark + "]";
	}
	public String getmData() {
		return mData;
	}
	public void setmData(String mData) {
		this.mData = mData;
	}
	public String getWarntypeName() {
		return warntypeName;
	}
	public void setWarntypeName(String warntypeName) {
		this.warntypeName = warntypeName;
	}
	public String getWarnlevelName() {
		return warnlevelName;
	}
	public void setWarnlevelName(String warnlevelName) {
		this.warnlevelName = warnlevelName;
	}
	public String getWayName() {
		return wayName;
	}
	public void setWayName(String wayName) {
		this.wayName = wayName;
	}
	public String getAddress_name() {
		return address_name;
	}
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}
}
