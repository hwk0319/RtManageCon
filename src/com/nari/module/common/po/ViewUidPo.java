package com.nari.module.common.po;
/**
 * 
 * @author Administrator
 * @date 2017年5月18日
 * @Description 查询视图PO
 *
 */
public class ViewUidPo {
	//id
	private Integer id;
	//uid
	private Integer uid;
	//名称
	private String name;
	//类型
	private String type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
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
	@Override
	public String toString() {
		return "ViewUidPo [id=" + id + ", uid=" + uid + ", name=" + name
				+ ", type=" + type + "]";
	}
	
}
