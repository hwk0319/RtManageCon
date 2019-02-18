package com.nari.po;

import com.nari.util.MyPropertiesPersist;

public class SystemMenu {
	// 菜单ID
	private double menucode;
	// 菜单名称
	private String menuname;
	// 父菜单名称
	private String parentcode;
	// 菜单级别
	private double menulevel;
	// 菜单路径
	private String menuurl;
	// 菜单图片
	private String menuimage;
	// 菜单图标
	private String menuicon;
	// 菜单是否有效
	private double menuuse;
	// 菜单排序
	private double menuorder;
	// 菜单打开时指向区域
	private String menutarget;
	//菜单类型
	private double menutype;
	private String level;
	private String leaf;
	private String expanded;
	private String DBTYPE;
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = MyPropertiesPersist.DBTYPE;
	}
	public double getMenucode() {
		return menucode;
	}

	public void setMenucode(double menucode) {
		this.menucode = menucode;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getParentcode() {
		return parentcode;
	}

	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	public double getMenulevel() {
		return menulevel;
	}

	public void setMenulevel(double menulevel) {
		this.menulevel = menulevel;
	}

	public String getMenuurl() {
		return menuurl;
	}

	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}

	public String getMenuimage() {
		return menuimage;
	}

	public void setMenuimage(String menuimage) {
		this.menuimage = menuimage;
	}

	public String getMenuicon() {
		return menuicon;
	}

	public void setMenuicon(String menuicon) {
		this.menuicon = menuicon;
	}

	public double getMenuuse() {
		return menuuse;
	}

	public void setMenuuse(double menuuse) {
		this.menuuse = menuuse;
	}

	public double getMenuorder() {
		return menuorder;
	}

	public void setMenuorder(double menuorder) {
		this.menuorder = menuorder;
	}

	public String getMenutarget() {
		return menutarget;
	}

	public void setMenutarget(String menutarget) {
		this.menutarget = menutarget;
	}

	public double getMenutype() {
		return menutype;
	}

	public void setMenutype(double menutype) {
		this.menutype = menutype;
	}

	public String getLeaf() {
		return leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

	public String getExpanded() {
		return expanded;
	}

	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String Level) {
		level = Level;
	}
	@Override
	public String toString() {
		return "SystemMenu [menucode=" + menucode + ", menuname=" + menuname
				+ ", parentcode=" + parentcode + ", menulevel=" + menulevel
				+ ", menuurl=" + menuurl + ", menuimage=" + menuimage
				+ ", menuicon=" + menuicon + ", menuuse=" + menuuse
				+ ", menuorder=" + menuorder + ", menutarget=" + menutarget
				+ ", menutype=" + menutype + ", level=" + level + ", leaf="
				+ leaf + ", expanded=" + expanded + ", DBTYPE=" + DBTYPE + "]";
	}

}
