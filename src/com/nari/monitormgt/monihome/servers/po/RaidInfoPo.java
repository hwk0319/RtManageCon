package com.nari.monitormgt.monihome.servers.po;

public class RaidInfoPo {
	private String named;
	private String mapped_device;
	private String raid_level;
	private String os_name;
	private String solt_num;
	private String solttype;
	private String capacity;
	private String health_score;
	private String sn;
	private String state;
	public String getMapped_device() {
		return mapped_device;
	}
	public String getRaid_level() {
		return raid_level;
	}
	public String getOs_name() {
		return os_name;
	}
	public String getSolt_num() {
		return solt_num;
	}
	public String getSolttype() {
		return solttype;
	}
	public String getCapacity() {
		return capacity;
	}
	public String getHealth_score() {
		return health_score;
	}
	public String getSn() {
		return sn;
	}
	public String getState() {
		return state;
	}
	public String getNamed() {
		return named;
	}
	public void setNamed(String named) {
		this.named = named;
	}
	
}
