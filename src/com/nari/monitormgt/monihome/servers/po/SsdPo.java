package com.nari.monitormgt.monihome.servers.po;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class SsdPo {
	private Integer name;
	private String uid;
	private String index_id;
	private String value;
	
	private String index_id1;
	private String index_id2;
	private String index_id3;
	private String index_id4;
	private String index_id5;
	private String index_id6;
	
	private String osname;
	private String health;
	private String ssdnum;
	private String ssdsn;
	private String temps;
	private Integer num;
	@JsonIgnore
	private String DBTYPE;
	
	private String sqlstr;

	public String getIndex_id() {
		return index_id;
	}

	public void setIndex_id(String index_id) {
		this.index_id = index_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getName() {
		return name;
	}

	public void setName(Integer name) {
		this.name = name;
	}

	public String getIndex_id1() {
		return index_id1;
	}

	public void setIndex_id1(String index_id1) {
		this.index_id1 = index_id1;
	}

	public String getIndex_id2() {
		return index_id2;
	}

	public void setIndex_id2(String index_id2) {
		this.index_id2 = index_id2;
	}

	public String getIndex_id3() {
		return index_id3;
	}

	public void setIndex_id3(String index_id3) {
		this.index_id3 = index_id3;
	}

	public String getIndex_id4() {
		return index_id4;
	}

	public void setIndex_id4(String index_id4) {
		this.index_id4 = index_id4;
	}

	public String getIndex_id5() {
		return index_id5;
	}

	public void setIndex_id5(String index_id5) {
		this.index_id5 = index_id5;
	}

	public String getOsname() {
		return osname;
	}

	public void setOsname(String osname) {
		this.osname = osname;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getSsdnum() {
		return ssdnum;
	}

	public void setSsdnum(String ssdnum) {
		this.ssdnum = ssdnum;
	}

	public String getSsdsn() {
		return ssdsn;
	}

	public void setSsdsn(String ssdsn) {
		this.ssdsn = ssdsn;
	}

	public String getTemps() {
		return temps;
	}

	public void setTemps(String temps) {
		this.temps = temps;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getIndex_id6() {
		return index_id6;
	}

	public void setIndex_id6(String index_id6) {
		this.index_id6 = index_id6;
	}

	public String getSqlstr() {
		return sqlstr;
	}

	public void setSqlstr(String sqlstr) {
		this.sqlstr = sqlstr;
	}

	public String getDBTYPE() {
		return DBTYPE;
	}

	public void setDBTYPE(String dBTYPE) {
		DBTYPE = dBTYPE;
	}
}
