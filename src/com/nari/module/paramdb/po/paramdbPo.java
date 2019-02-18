package com.nari.module.paramdb.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nari.monitormgt.monihome.servers.po.PagePo;
import com.nari.util.MyPropertiesPersist;

public class paramdbPo extends PagePo{
	/**
	 * mgt_system表字段
	 */
	private Integer id;
	private Integer systype;
	private String uid;
	private String type;
	private String name;
	private String ip;
	private String username;
	private StringBuffer mm;
	@JsonIgnore
	private Integer create_by;
	@JsonIgnore
	private Date create_date;
	@JsonIgnore
	private Integer update_by;
	@JsonIgnore
	private Date update_date;
	private String remark;
	@JsonIgnore
	private boolean use_flag;
	private String reserver1;
	private String reserver2;
	private String reserver3;
	private String position;
	
	private int pagesize;
	private String kw;
	/**
	 * mon_indexdata表
	 */
	private Integer index_id;
	private String value;
	private String record_time;
	private String Connect_failed;
	private String cpu_usage;
	private String instance_state;
	private String session_num;
	private String SGA_hit;
	private String user_time;
	private String system_time;
	private String iops;
	private String io_speed;
	private String Memery_usage_percent;
	private String Response_Time;
	private String Idle_Time;
	private String PGA_hit;
	private String DB_cache_match;
	private String share_pool_match;
	private String table_name;
	private String table_type;
	private String sum_space;
	private String sum_blocks;
	private String used_space;
	private String used_rate;
	private String free_space;
	
	private String capacity_used;
	private String TPS;
	private String QPS;
	
	private Integer num;
	
	private String DBTYPE;
	private String searchText;
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
	public Integer getSystype() {
		return systype;
	}
	public void setSystype(Integer systype) {
		this.systype = systype;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getIndex_id() {
		return index_id;
	}
	public void setIndex_id(Integer index_id) {
		this.index_id = index_id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
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
	public String getReserver1() {
		return reserver1;
	}
	public void setReserver1(String reserver1) {
		this.reserver1 = reserver1;
	}
	public String getReserver2() {
		return reserver2;
	}
	public void setReserver2(String reserver2) {
		this.reserver2 = reserver2;
	}
	public String getReserver3() {
		return reserver3;
	}
	public void setReserver3(String reserver3) {
		this.reserver3 = reserver3;
	}
	public boolean isUse_flag() {
		return use_flag;
	}
	public void setUse_flag(boolean use_flag) {
		this.use_flag = use_flag;
	}
	public String getConnect_failed() {
		return Connect_failed;
	}
	public void setConnect_failed(String connect_failed) {
		Connect_failed = connect_failed;
	}
	public String getCpu_usage() {
		return cpu_usage;
	}
	public void setCpu_usage(String cpu_usage) {
		this.cpu_usage = cpu_usage;
	}
	public String getInstance_state() {
		return instance_state;
	}
	public void setInstance_state(String instance_state) {
		this.instance_state = instance_state;
	}
	public String getSession_num() {
		return session_num;
	}
	public void setSession_num(String session_num) {
		this.session_num = session_num;
	}
	public String getSGA_hit() {
		return SGA_hit;
	}
	public void setSGA_hit(String sGA_hit) {
		SGA_hit = sGA_hit;
	}
	public String getUser_time() {
		return user_time;
	}
	public void setUser_time(String user_time) {
		this.user_time = user_time;
	}
	public String getSystem_time() {
		return system_time;
	}
	public void setSystem_time(String system_time) {
		this.system_time = system_time;
	}
	public String getIops() {
		return iops;
	}
	public void setIops(String iops) {
		this.iops = iops;
	}
	public String getIo_speed() {
		return io_speed;
	}
	public void setIo_speed(String io_speed) {
		this.io_speed = io_speed;
	}
	public String getMemery_usage_percent() {
		return Memery_usage_percent;
	}
	public void setMemery_usage_percent(String memery_usage_percent) {
		Memery_usage_percent = memery_usage_percent;
	}
	public String getResponse_Time() {
		return Response_Time;
	}
	public void setResponse_Time(String response_Time) {
		Response_Time = response_Time;
	}
	public String getIdle_Time() {
		return Idle_Time;
	}
	public void setIdle_Time(String idle_Time) {
		Idle_Time = idle_Time;
	}
	public String getPGA_hit() {
		return PGA_hit;
	}
	public void setPGA_hit(String pGA_hit) {
		PGA_hit = pGA_hit;
	}
	public String getDB_cache_match() {
		return DB_cache_match;
	}
	public void setDB_cache_match(String dB_cache_match) {
		DB_cache_match = dB_cache_match;
	}
	public String getShare_pool_match() {
		return share_pool_match;
	}
	public void setShare_pool_match(String share_pool_match) {
		this.share_pool_match = share_pool_match;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getTable_type() {
		return table_type;
	}
	public void setTable_type(String table_type) {
		this.table_type = table_type;
	}
	public String getSum_space() {
		return sum_space;
	}
	public void setSum_space(String sum_space) {
		this.sum_space = sum_space;
	}
	public String getSum_blocks() {
		return sum_blocks;
	}
	public void setSum_blocks(String sum_blocks) {
		this.sum_blocks = sum_blocks;
	}
	public String getUsed_space() {
		return used_space;
	}
	public void setUsed_space(String used_space) {
		this.used_space = used_space;
	}
	public String getUsed_rate() {
		return used_rate;
	}
	public void setUsed_rate(String used_rate) {
		this.used_rate = used_rate;
	}
	public String getFree_space() {
		return free_space;
	}
	public void setFree_space(String free_space) {
		this.free_space = free_space;
	}
	public String getCapacity_used() {
		return capacity_used;
	}
	public void setCapacity_used(String capacity_used) {
		this.capacity_used = capacity_used;
	}
	public String getTPS() {
		return TPS;
	}
	public void setTPS(String tPS) {
		TPS = tPS;
	}
	public String getQPS() {
		return QPS;
	}
	public void setQPS(String qPS) {
		QPS = qPS;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "paramdbPo [uid=" + uid + ", index_id=" + index_id + ", value="
				+ value + ", record_time=" + record_time + ", Connect_failed="
				+ Connect_failed + ", cpu_usage=" + cpu_usage
				+ ", instance_state=" + instance_state + ", session_num="
				+ session_num + ", SGA_hit=" + SGA_hit + ", user_time="
				+ user_time + ", system_time=" + system_time + ", iops=" + iops
				+ ", io_speed=" + io_speed + ", Memery_usage_percent="
				+ Memery_usage_percent + ", Response_Time=" + Response_Time
				+ ", Idle_Time=" + Idle_Time + ", PGA_hit=" + PGA_hit
				+ ", DB_cache_match=" + DB_cache_match + ", share_pool_match="
				+ share_pool_match + ", table_name=" + table_name
				+ ", table_type=" + table_type + ", sum_space=" + sum_space
				+ ", sum_blocks=" + sum_blocks + ", used_space=" + used_space
				+ ", used_rate=" + used_rate + ", free_space=" + free_space
				+ ", capacity_used=" + capacity_used + ", TPS=" + TPS
				+ ", QPS=" + QPS + ", num=" + num +  ", DBTYPE=" + DBTYPE + "]";
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public StringBuffer getMm() {
		return mm;
	}
	public void setMm(StringBuffer mm) {
		this.mm = mm;
	}
	public String getKw() {
		return kw;
	}
	public void setKw(String kw) {
		this.kw = kw;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
}
