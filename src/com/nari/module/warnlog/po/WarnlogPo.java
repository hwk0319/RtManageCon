package com.nari.module.warnlog.po;

public class WarnlogPo {
		//编号
		private Integer id;
		private String device_id;
		//设备名称
		private String device_name;
		//故障部位 
		private String warn_part;
		//告警级别
		private String warn_level;
		//初次发生日期
		private String warn_time;
		//详细信息
		private String warn_info;
		//处理状态
		private Integer process_status;
		//是否通知
		private boolean isnoticed;
		//最新发生日期
		private String newest_warntime;
		//发生次数
		private  Integer occur_times;
		//日志告警信息
		private String logwarn_info;
		
		private String warn_timeS;
		private String warn_timeE;
		private String warn_type;
		
		private Integer servNum;//服务器数量
		private Integer doubNum;//双活数量
		private Integer clusNum;//集群数量
		private Integer inteNum;//一体机数量
		//位置信息
		private String position;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getDevice_name() {
			return device_name;
		}
		public void setDevice_name(String device_name) {
			this.device_name = device_name;
		}
		public String getWarn_part() {
			return warn_part;
		}
		public void setWarn_part(String warn_part) {
			this.warn_part = warn_part;
		}
		public String getWarn_level() {
			return warn_level;
		}
		public void setWarn_level(String warn_level) {
			this.warn_level = warn_level;
		}
		public String getWarn_time() {
			return warn_time;
		}
		public void setWarn_time(String warn_time) {
			this.warn_time = warn_time;
		}
		public String getWarn_info() {
			return warn_info;
		}
		public void setWarn_info(String warn_info) {
			this.warn_info = warn_info;
		}
		public Integer getProcess_status() {
			return process_status;
		}
		public void setProcess_status(Integer process_status) {
			this.process_status = process_status;
		}
		public String getWarn_timeS() {
			return warn_timeS;
		}
		public void setWarn_timeS(String warn_timeS) {
			this.warn_timeS = warn_timeS;
		}
		public String getWarn_timeE() {
			return warn_timeE;
		}
		public void setWarn_timeE(String warn_timeE) {
			this.warn_timeE = warn_timeE;
		}
		public boolean isIsnoticed() {
			return isnoticed;
		}
		public void setIsnoticed(boolean isnoticed) {
			this.isnoticed = isnoticed;
		}
		public String getNewest_warntime() {
			return newest_warntime;
		}
		public void setNewest_warntime(String newest_warntime) {
			this.newest_warntime = newest_warntime;
		}
		public Integer getOccur_times() {
			return occur_times;
		}
		public void setOccur_times(Integer occur_times) {
			this.occur_times = occur_times;
		}
		public Integer getServNum() {
			return servNum;
		}
		public void setServNum(Integer servNum) {
			this.servNum = servNum;
		}
		public Integer getDoubNum() {
			return doubNum;
		}
		public void setDoubNum(Integer doubNum) {
			this.doubNum = doubNum;
		}
		public Integer getClusNum() {
			return clusNum;
		}
		public void setClusNum(Integer clusNum) {
			this.clusNum = clusNum;
		}
		public Integer getInteNum() {
			return inteNum;
		}
		public void setInteNum(Integer inteNum) {
			this.inteNum = inteNum;
		}
		public String getDevice_id() {
			return device_id;
		}
		public void setDevice_id(String device_id) {
			this.device_id = device_id;
		}
		public String getWarn_type() {
			return warn_type;
		}
		public void setWarn_type(String warn_type) {
			this.warn_type = warn_type;
		}
		public String getLogwarn_info() {
			return logwarn_info;
		}
		public void setLogwarn_info(String logwarn_info) {
			this.logwarn_info = logwarn_info;
		}
		@Override
		public String toString() {
			return "WarnlogPo [id=" + id + ", device_id=" + device_id
					+ ", device_name=" + device_name + ", warn_part="
					+ warn_part + ", warn_level=" + warn_level + ", warn_time="
					+ warn_time + ", warn_info=" + warn_info
					+ ", process_status=" + process_status + ", isnoticed="
					+ isnoticed + ", newest_warntime=" + newest_warntime
					+ ", occur_times=" + occur_times + ", logwarn_info="
					+ logwarn_info + ", warn_timeS=" + warn_timeS
					+ ", warn_timeE=" + warn_timeE + ", warn_type=" + warn_type
					+ ", servNum=" + servNum + ", doubNum="
					+ doubNum + ", clusNum=" + clusNum + ", inteNum=" + inteNum
					+ "]";
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
}
