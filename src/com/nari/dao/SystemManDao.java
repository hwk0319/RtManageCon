package com.nari.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.po.SysCodeValue;
import com.nari.po.SysRole;
import com.nari.po.SystemMenu;

@Repository(value="sysManDao")
public interface SystemManDao {
	//查询菜单
	public List<SystemMenu> searchGrid(@Param("po")SystemMenu po);
	//修改菜单
	public int updateSysMenu(@Param("po")SystemMenu po);
	//查询角色列表
	public List<SysRole> sysRoleSearch(@Param("po")SysRole po);
	//增加角色
	public int insertSysRole(@Param("po")SysRole po);
	//修改角色
	public int updateSysRole(@Param("po")SysRole po);
	//删除角色
	public int deleSysRole(@Param("po")SysRole po);
	//插叙角色可以关联的菜单
	public List<SystemMenu> roleMenuGrid();
	//关联角色和菜单
	public int updateRoleMenu(@Param("po")SysRole po);
	
	
	public List<SysCodeValue> findSysCodeValue(@Param("code_type")String code_type);
}
