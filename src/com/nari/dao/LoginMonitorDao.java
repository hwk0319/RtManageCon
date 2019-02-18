package com.nari.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.po.LoginMonitor;

@Repository(value="loginMonitorDao")
public interface LoginMonitorDao {
	public LoginMonitor findUserByName(@Param("username") String username, @Param("DBTYPE") String DBTYPE);
	public LoginMonitor findByUsernameNew(@Param("username") String username, @Param("DBTYPE") String DBTYPE);
	public LoginMonitor findUserByIp(String ip);
	public int insert(@Param("po") LoginMonitor po);
	public int delete(@Param("po") LoginMonitor po);
	public int findCount();
	public List<LoginMonitor> findCookieByUser(@Param("po") LoginMonitor po);
}