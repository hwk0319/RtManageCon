package com.nari.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nari.dao.LoginMonitorDao;
import com.nari.dao.OprtLogDao;
import com.nari.dao.SysParamDao;
import com.nari.dao.UsersDao;
import com.nari.po.LoginMonitor;
import com.nari.po.OprtLog;
import com.nari.po.User;
import com.nari.util.MyPropertiesPersist;

@Service(value = "userService")
public class UserService {
	@Resource
	private UsersDao dao;
	@Resource
	private LoginMonitorDao loginMonitorDao;
	@Resource(name = "sysParamDao")
	private SysParamDao paramDao;

	@Resource(name = "oprtLogDao")
	private OprtLogDao logDao;
	@Autowired  
	private  HttpServletRequest request;  

	OprtLog logPo = new OprtLog();

	public List<User> search(@Param("po") User po) {
		return dao.search(po);
	}

	// 注销用户
	public int delete(@Param("po") User po, HttpServletRequest request) {
		int res = dao.delete(po);
		return res;
	}

	// 新增用户
	public int insert(@Param("po") User user, HttpServletRequest request) {
		int res = dao.insert(user);
		return res;
	}
		// 新增isc用户
		public int insert_isc(@Param("po") User po, HttpServletRequest request) {
			return dao.insert_isc(po);
		}

	public int update(@Param("po") User user, HttpServletRequest request) {
		int res = dao.update(user);
		return res;
	}

	// 角色授权
	public int accredit(@Param("po") User user, HttpServletRequest request) {
		int res = dao.accredit(user);
		return res;
	}

	// 锁定
	public int lock(@Param("po") User user, HttpServletRequest request) {
		int res = dao.lock(user);
		return res;
	}

	// 解锁
	public int unlock(@Param("po") User user, HttpServletRequest request) {
		int res = dao.unlock(user);
		return res;
	}

	public String checkUsername(String name) {
		User userInfo = dao.findUserByName(name);
		if (userInfo == null) {
			return "success";
		} else {
			return "error";
		}
	}
	
	public String checkLogin(String name) {
		LoginMonitor loginMonitor = loginMonitorDao.findUserByName(name,MyPropertiesPersist.DBTYPE);
		if (loginMonitor == null) {
			return "success";
		} else {
			return "error";
		}
	}
	//从session中获取用户信息
	public User getUserInfo(){
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}
}
