package com.nari.module.operationlog.po;

public class OperationlogPo {
	
	//编号
	private Integer id; 
	//菜单类型
	private String module ;
	//操作类型
	private String oprt_type;
	//操作时间
	private String time;
	//操作人
	private String oprt_user;
	//操作内容
	private String oprt_content;
	//操作人主机IP
	private String ip ;
	//操作目标ID
	private Integer oprt_id;
	//是否成功
	private String flag;
	//事件类型
	private String type;
	//系统事件
	private String systems;
	//业务事件
	private String business;
	//事件等级
	private String levels;
	
	//统计
	private String counts;
	private String logins;
	private String logout;
	private String adds;
	private String edit;
	private String deletes;
	private String score;
	private String yuequan;
	private String ipChange;
	private String loginFail;
	private String searchs;
	private String doCounts;
	private String exports;
	//周期
	private String zhouqi;
	private String sql;
	private String position;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getOprt_type() {
		return oprt_type;
	}
	public void setOprt_type(String oprt_type) {
		this.oprt_type = oprt_type;
	}
	public String getOprt_user() {
		return oprt_user;
	}
	public void setOprt_user(String oprt_user) {
		this.oprt_user = oprt_user;
	}
	public String getOprt_content() {
		return oprt_content;
	}
	public void setOprt_content(String oprt_content) {
		this.oprt_content = oprt_content;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getOprt_id() {
		return oprt_id;
	}
	public void setOprt_id(Integer oprt_id) {
		this.oprt_id = oprt_id;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSystems() {
		return systems;
	}
	public void setSystems(String systems) {
		this.systems = systems;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getCounts() {
		return counts;
	}
	public void setCounts(String counts) {
		this.counts = counts;
	}
	public String getLogins() {
		return logins;
	}
	public void setLogins(String logins) {
		this.logins = logins;
	}
	public String getLogout() {
		return logout;
	}
	public void setLogout(String logout) {
		this.logout = logout;
	}
	public String getAdds() {
		return adds;
	}
	public void setAdds(String adds) {
		this.adds = adds;
	}
	public String getEdit() {
		return edit;
	}
	public void setEdit(String edit) {
		this.edit = edit;
	}
	public String getDeletes() {
		return deletes;
	}
	public void setDeletes(String deletes) {
		this.deletes = deletes;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getYuequan() {
		return yuequan;
	}
	public void setYuequan(String yuequan) {
		this.yuequan = yuequan;
	}
	public String getIpChange() {
		return ipChange;
	}
	public void setIpChange(String ipChange) {
		this.ipChange = ipChange;
	}
	public String getLoginFail() {
		return loginFail;
	}
	public void setLoginFail(String loginFail) {
		this.loginFail = loginFail;
	}
	@Override
	public String toString() {
		return "OperationlogPo [id=" + id + ", module=" + module
				+ ", oprt_type=" + oprt_type + ", time=" + time
				+ ", oprt_user=" + oprt_user + ", oprt_content=" + oprt_content
				+ ", ip=" + ip + ", oprt_id=" + oprt_id + ", flag=" + flag
				+ ", type=" + type + ", systems=" + systems + ", business="
				+ business + ", counts=" + counts + ", logins=" + logins
				+ ", logout=" + logout + ", adds=" + adds + ", edit=" + edit
				+ ", deletes=" + deletes + ", score=" + score + ", yuequan="
				+ yuequan + ", ipChange=" + ipChange + ", loginFail="
				+ loginFail + "]";
	}
	public String getZhouqi() {
		return zhouqi;
	}
	public void setZhouqi(String zhouqi) {
		this.zhouqi = zhouqi;
	}
	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	public String getSearchs() {
		return searchs;
	}
	public void setSearchs(String searchs) {
		this.searchs = searchs;
	}
	public String getDoCounts() {
		return doCounts;
	}
	public void setDoCounts(String doCounts) {
		this.doCounts = doCounts;
	}
	public String getExports() {
		return exports;
	}
	public void setExports(String exports) {
		this.exports = exports;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
