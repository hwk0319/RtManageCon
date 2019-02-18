package com.nari.monitormgt.monihome.servers.po;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class IbCardPo {
	private Integer name;
	private String uid;
	private String index_id;
	private String value;
	private String ibname;
	
	private String index_id1;
	private String index_id2;
	private String index_id3;
	private String index_id4;
	private String index_id5;
	private String index_id6;
	private String index_id7;
	private String index_id8;

	private String gid;
	private String ibport;
	private String state;
	private String physstate;
	private String rate;
	private Integer num;
	@JsonIgnore
	private String DBTYPE;
	

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getIbport() {
		return ibport;
	}

	public void setIbport(String ibport) {
		this.ibport = ibport;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhysstate() {
		return physstate;
	}

	public void setPhysstate(String physstate) {
		this.physstate = physstate;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

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

	public String getIndex_id6() {
		return index_id6;
	}

	public void setIndex_id6(String index_id6) {
		this.index_id6 = index_id6;
	}
	
	public String getIndex_id7() {
		return index_id7;
	}

	public void setIndex_id7(String index_id7) {
		this.index_id7 = index_id7;
	}

	public String getIndex_id8() {
		return index_id8;
	}

	public void setIndex_id8(String index_id8) {
		this.index_id8 = index_id8;
	}

	public String getIbname() {
		return ibname;
	}

	public void setIbname(String ibname) {
		this.ibname = ibname;
	}

	public String getDBTYPE() {
		return DBTYPE;
	}

	public void setDBTYPE(String dBTYPE) {
		DBTYPE = dBTYPE;
	}

}
