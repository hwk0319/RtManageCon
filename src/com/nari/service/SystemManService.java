package com.nari.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nari.dao.LoginMonitorDao;
import com.nari.dao.OprtLogDao;
import com.nari.dao.SystemManDao;
import com.nari.po.OprtLog;
import com.nari.po.SysCodeValue;
import com.nari.po.SysRole;
import com.nari.po.SystemMenu;
import com.nari.po.User;

@Service(value = "systemManService")
public class SystemManService {
	@Resource(name = "sysManDao")
	private SystemManDao sysManDao;

	@Resource(name = "oprtLogDao")
	private OprtLogDao logDao;

	OprtLog logPo = new OprtLog();

	@Resource
	private LoginMonitorDao loginMonitorDao;
	@Autowired  
	private  HttpServletRequest request;  
	public List<SystemMenu> searchGrid(SystemMenu po) {
		List<SystemMenu> list = sysManDao.searchGrid(po);
		return list;
	}

	// 编辑菜单
	public int updateSysMenu(@Param("po") SystemMenu po, HttpServletRequest request) {
		int res = sysManDao.updateSysMenu(po);
		return res;
	}

	public List<SysRole> sysRoleSearch(SysRole po) {
		List<SysRole> list = sysManDao.sysRoleSearch(po);
		return list;
	}

	// 增加角色
	public int insertSysRole(@Param("po") SysRole po, HttpServletRequest request) {
		int res = sysManDao.insertSysRole(po);
		return res;
	}

	// 编辑角色
	public int updateSysRole(@Param("po") SysRole po, HttpServletRequest request) {
		int res = sysManDao.updateSysRole(po);
		return res;
	}

	// 删除角色
	public int deleSysRole(@Param("po") SysRole po, HttpServletRequest request) {
		int res = sysManDao.deleSysRole(po);
		return res;
	}

	public List<SystemMenu> roleMenuGrid() {
		List<SystemMenu> list = sysManDao.roleMenuGrid();
		return list;
	}

	// 角色关联菜单
	public int updateRoleMenu(@Param("po") SysRole po, HttpServletRequest request) {
		int res = sysManDao.updateRoleMenu(po);
		return res;
	}

	public List<SysCodeValue> findSysCodeValue(@Param("code_type") String code_type) {
		List<SysCodeValue> list = sysManDao.findSysCodeValue(code_type);
		return list;
	}
	
	//从session中获取用户信息
	public User getUserInfo(){
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}
}
