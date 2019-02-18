package com.nari.po;

import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;

public class DeviceBasePo {
	/**
	 *	带外IP
	 */
	private String ip;
	private String name;
	/**
	 *  带外用户名
	 */
	private String user_name;
	/**
	 *  带外密码
	 */
	private String mm;
	/**
	 *  带外TOKEN
	 */
	private String access_token;
	/**
	 *  SSH协议端口
	 */
	private int ssh_port;
	/**
	 *  HTTPS协议端口
	 */
	private int http_port;
	/**
	 *  IPMI
	 */
	private String ipmikey;
	/**
	 *  设备资产编号
	 */
	private String code;
	/**
	 *  带内IP
	 */
	private String inband_ip;
	/**
	 *  带内用户名
	 */
	private String inband_user_name;
	/**
	 *  带内密码
	 */
	private String inband_mm;
	/**
	 *  带内TOKEN
	 */
	private String inband_access_token;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getSsh_port() {
		return ssh_port;
	}

	public void setSsh_port(int ssh_port) {
		this.ssh_port = ssh_port;
	}

	public int getHttp_port() {
		return http_port;
	}

	public void setHttp_port(int http_port) {
		this.http_port = http_port;
	}

	public String getIpmikey() {
		return ipmikey;
	}

	public void setIpmikey(String ipmikey) {
		this.ipmikey = ipmikey;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInband_ip() {
		return inband_ip;
	}

	public void setInband_ip(String inband_ip) {
		this.inband_ip = inband_ip;
	}

	public String getInband_user_name() {
		return inband_user_name;
	}

	public void setInband_user_name(String inband_user_name) {
		this.inband_user_name = inband_user_name;
	}

	public String getInband_access_token() {
		return inband_access_token;
	}

	public void setInband_access_token(String inband_access_token) {
		this.inband_access_token = inband_access_token;
	}
	
	/**
	 * 带外参数
	 * @return Map<String,String>
	 */
	public Map<String,String> toOutBandMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("MGMT_ADDR", this.getIp());
		map.put("USER", base64Encode(this.getUser_name().getBytes()));
		map.put("PASSWORD", base64Encode(this.getMm().getBytes()));
		map.put("IPMI_KEY", this.getIpmikey());
		map.put("SSH_PORT", Integer.toString(this.getSsh_port()));
		map.put("HTTPS_PORT", Integer.toString(this.getHttp_port()));
		return map;
	}
	
	/**
	 * 带内参数
	 * @return Map<String,String>
	 */
	public Map<String,String> toInBandMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("MGMT_ADDR", this.getInband_ip());
		map.put("USER", base64Encode(this.getInband_user_name().getBytes()));
		map.put("PASSWORD", base64Encode(this.getInband_mm().getBytes()));
		map.put("IPMI_KEY", this.getIpmikey());
		map.put("SSH_PORT", Integer.toString(this.getSsh_port()));
		map.put("HTTPS_PORT", Integer.toString(this.getHttp_port()));
		return map;
	}
	
	/**
	 * 将字符数组转换为base64编码格式
	 * @param bytes
	 * @return
	 */
	public String base64Encode(byte[] bytes){  
        return new BASE64Encoder().encode(bytes);  
    }

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getInband_mm() {
		return inband_mm;
	}

	public void setInband_mm(String inband_mm) {
		this.inband_mm = inband_mm;
	}
	
}
