package com.nari.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.module.general.service.GeneranService;
import com.nari.po.SysCodeValue;
import com.nari.po.SysRole;
import com.nari.po.SystemMenu;
import com.nari.po.User;
import com.nari.service.SystemManService;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;

@Controller
@RequestMapping(value="systemMan")
public class SystemManController extends BaseController {
	private Logger logger = Logger.getLogger(SystemManController.class);
	@Resource(name="systemManService")
	private SystemManService sysManservice;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	/**
	 * 菜单查询
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchGrid")
	public List<SystemMenu> searchGrid(SystemMenu po){
//		String menucode = String.valueOf(po.getMenucode());
//		if(menucode!=null && !"".equals(menucode)){
//			po.setMenucode(Double.parseDouble(menucode));
//		}
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<SystemMenu> list = sysManservice.searchGrid(po);
		return list;
	}
	/**
	 * 菜单修改
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/update")
	public int update(SystemMenu po,HttpServletRequest request) throws Exception{	
		User userInfo = (User) request.getSession().getAttribute("user");
		int res = sysManservice.updateSysMenu(po, request);
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_logs("系统管理-菜单管理","编辑菜单",userInfo.getUsername(),"编辑"+po.getMenuname()+"菜单",userInfo.getIpaddr(),toInteger(po.getMenucode()),SysConstant.OPRTLOG_SECCUSS,SysConstant.SYSTEM_EVENT,"0",inOrOut);
		return res;
	}
	/**
	 * 角色列表查询
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sysRoleSearch")
	public List<SysRole> sysRoleSearch(HttpServletRequest request, SysRole po){
		String roleid = String.valueOf(po.getRoleid());
		if(roleid!=null && !"".equals(roleid)){
			po.setRoleid(Integer.parseInt(roleid));
		}
		List<SysRole> list = sysManservice.sysRoleSearch(po);
		return list;
	}
	/**
	 * 增加角色
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/insertSysRole")
	public int insertSysRole(SysRole po,HttpServletRequest request) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		int res = sysManservice.insertSysRole(po, request);
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_logs("系统管理-角色管理","新增角色",userInfo.getUsername(),"新增"+po.getRolename()+"角色",userInfo.getIpaddr(),po.getRoleid(),SysConstant.OPRTLOG_FAIL,SysConstant.SYSTEM_EVENT,"0",inOrOut);
		return res;
	}
	/**
	 * 修改角色信息
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/updateSysRole")
	public int updateSysRole(SysRole po,HttpServletRequest request) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		int res = sysManservice.updateSysRole(po, request);
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_logs("系统管理-角色管理","编辑角色",userInfo.getUsername(),"编辑"+po.getRolename()+"角色",userInfo.getIpaddr(),po.getRoleid(),SysConstant.OPRTLOG_FAIL,SysConstant.SYSTEM_EVENT,"0",inOrOut);
		return res;
	}
	
	/**
	 * 删除角色信息
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/deleSysRole")
	public int deleSysRole(SysRole po,HttpServletRequest request) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		String Rolename =sysManservice.sysRoleSearch(po).get(0).getRolename();
		int res = sysManservice.deleSysRole(po, request);
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_logs("系统管理-角色管理","删除角色",userInfo.getUsername(),"删除"+Rolename+"角色",userInfo.getIpaddr(),po.getRoleid(),SysConstant.OPRTLOG_FAIL,SysConstant.SYSTEM_EVENT,"0",inOrOut);
		return res;
	}
	/**
	 * 查询角色可以关联的菜单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/roleMenuGrid")
	public List<SystemMenu> roleMenuGrid(){
		List<SystemMenu> list = new ArrayList<>();
		try {
			list = sysManservice.roleMenuGrid();
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 关联角色和菜单
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/updateRoleMenu")
	public int updateRoleMenu(HttpServletRequest request, SysRole po) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		String selrow = po.getRolemenu();
		String roleid = String.valueOf(po.getRoleid());
		po.setRolemenu(selrow);
		if(roleid!=null && !"".equals(roleid)){
			po.setRoleid(Integer.parseInt(roleid));
		}
		String Rolename =sysManservice.sysRoleSearch(po).get(0).getRolename();
		int res = sysManservice.updateRoleMenu(po, request);
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_logs("系统管理-角色管理","角色关联菜单",userInfo.getUsername(),"角色"+Rolename+"关联菜单",userInfo.getIpaddr(),po.getRoleid(),SysConstant.OPRTLOG_FAIL,SysConstant.SYSTEM_EVENT,"0",inOrOut);
		return res;
	}
	
	@RequestMapping("/findsyscodevalue")
	@ResponseBody
	public List<SysCodeValue> findSysCodeValue(String code_type){
		List<SysCodeValue> list= new ArrayList<>();
		try {
			list = sysManservice.findSysCodeValue(Util.xssEncode(code_type));
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
		
	}
}
