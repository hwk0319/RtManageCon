package com.nari.po;


public class RsDgDelay {
	//设备名称
	private String main_time;
	//设备Ip
	private String sub_time;
	//异常时间
	private String main_scan;
	//异常信息
	private String sub_scan;
	//设备编号
	private String delay_time;
	//状态值
	private String get_time;
	
	private int id;

	public String getMain_time() {
		return main_time;
	}

	public void setMain_time(String main_time) {
		this.main_time = main_time;
	}

	public String getSub_time() {
		return sub_time;
	}

	public void setSub_time(String sub_time) {
		this.sub_time = sub_time;
	}

	public String getMain_scan() {
		return main_scan;
	}

	public void setMain_scan(String main_scan) {
		this.main_scan = main_scan;
	}

	public String getSub_scan() {
		return sub_scan;
	}

	public void setSub_scan(String sub_scan) {
		this.sub_scan = sub_scan;
	}

	public String getDelay_time() {
		return delay_time;
	}

	public void setDelay_time(String delay_time) {
		this.delay_time = delay_time;
	}

	public String getGet_time() {
		return get_time;
	}

	public void setGet_time(String get_time) {
		this.get_time = get_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	} 
	 
}
