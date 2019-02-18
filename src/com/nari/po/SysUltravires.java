package com.nari.po;

//越权实体类
public class SysUltravires {
	// 越权人姓名
	private String oprt_user;
	// 越权时间
	private String oprt_time;
	// 越权次数
	private int oprt_count;

	public String getOprt_user() {
		return oprt_user;
	}

	public void setOprt_user(String oprt_user) {
		this.oprt_user = oprt_user;
	}

	public String getOprt_time() {
		return oprt_time;
	}

	public void setOprt_time(String oprt_time) {
		this.oprt_time = oprt_time;
	}

	public int getOprt_count() {
		return oprt_count;
	}

	public void setOprt_count(int oprt_count) {
		this.oprt_count = oprt_count;
	};

}
