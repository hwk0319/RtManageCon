package com.nari.po;

import com.nari.common.persistence.BaseEntity;

public class DbConnectPo extends BaseEntity<DbConnectPo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 连接url
	private String conn_url;

	// 计算节点编号
	private String device_code;

	// 方言
	private String dialect;
	// 驱动程序
	private String driver_class;
	private String id;
	private String is_main;
	// 连接名称
	private String name;
	// 用户密码
	private String mm;
	// 用户名
	private String user_name;
	public String getConn_url() {
		return conn_url;
	}
	public String getDevice_code() {
		return device_code;
	}

	public String getDialect() {
		return dialect;
	}

	public String getDriver_class() {
		return driver_class;
	}

	public String getId() {
		return id;
	}

	public String getIs_main() {
		return is_main;
	}

	public String getName() {
		return name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setConn_url(String conn_url) {
		this.conn_url = conn_url;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public void setDriver_class(String driver_class) {
		this.driver_class = driver_class;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIs_main(String is_main) {
		this.is_main = is_main;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	
}
