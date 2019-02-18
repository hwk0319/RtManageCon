package com.nari.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.po.ActionMenuUrl;
import com.nari.po.SystemMenu;

@Repository(value = "systemMenuDao")
public interface SystemMenuDao {
	// 根据code 查询菜单列表
	public List<SystemMenu> findMenuByCode(@Param("menucode") int[] menucode);

	// 根据menuurl查询菜单信息
	public List<SystemMenu> findMenuByURL(@Param("menuurl") String menuurl);

	// 根据id查询 code
	public String findMenuCode(@Param("id") int id);

	// 根据parentcode 查询 子菜单
	public List<SystemMenu> findMenuByPCode(
			@Param("parentcode") String parentcode);

	// 根据url查询模块名
	public String findModuleByUrl(@Param("menuurl") String menuurl);

	// 根据ajax请求查询所属菜单
	public List<ActionMenuUrl> findMenuByAjaxUrl(@Param("ajaxurl") String ajaxurl);
	
	public List<String> findMenuNameByUrl(@Param("ajaxurl")String ajaxurl);
}
