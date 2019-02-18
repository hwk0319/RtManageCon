package com.nari.module.address.po;


import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.po.BasePo;
import com.nari.util.MyPropertiesPersist;

public class AddressPo extends BasePo{
	//编号
	private Integer id; 
	//姓名
	@NotNull(message="姓名不能为空！")
	@Size(min=1,max=20,message="姓名请输入1到20个字符！")
	private String name ;
	//手机号
	@NotNull(message="手机号不能为空！")
	@Pattern(regexp = "^1[3|4|5|7|8][0-9]{9}$", message = "手机号不合法！")
	private String phone;
	//邮箱
	@NotNull(message="邮箱不能为空！")
	@Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$", message = "邮箱不合法！")
	private String email;
	//地址
	@Size(min=0,max=100,message="地址请输入1到100个字符！")
	private String address;
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
	@JsonIgnore
	private String DBTYPE;
	private String mData;
	
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	@Override
	public String toString() {
		return "AddressPo [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", email=" + email + ", address=" + address + ", create_by="
				+ create_by + ", create_date=" + create_date + ", update_by="
				+ update_by + ", update_date=" + update_date + ", remark="
				+ remark + ", use_flag=" + use_flag + ", DBTYPE=" + DBTYPE
				+ ", mData=" + mData + "]";
	}
}
