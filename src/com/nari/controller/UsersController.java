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
import com.nari.po.User;
import com.nari.service.UserService;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.RSAUtils;
import com.nari.util.Util;

@Controller
@RequestMapping(value="usersCon")
public class UsersController {
	
	private Logger logger = Logger.getLogger(UsersController.class);
	
	@Resource(name="userService")
	private UserService service;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	/**
	 * 保存用户
	 * @author ydd
	 * @param name
	 * @param userId
	 * @param age
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/insert")
	public int insert(User po,HttpServletRequest request) throws Exception{		
		User userInfo = (User) request.getSession().getAttribute("user");
		String username = RSAUtils.decryptStringByJs(po.getUsername());
		po.setUsername(username);
		String pass = RSAUtils.decryptStringByJs(po.getMm());
		//md5加密
		String mm_md5 = MD5.GetMD5Code(pass);
		po.setMmM(mm_md5);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		int res = service.insert(po, request);
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_log("系统管理-用户管理","新增",userInfo.getUsername(),"新增"+username+"用户",userInfo.getIpaddr(),po.getId(),inOrOut);
		return res;
	}
	@ResponseBody
	@RequestMapping(value="/update")
	public int update(User po,HttpServletRequest request) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		int res = service.update(po, request);
		String updateuser=service.search(po).get(0).getUsername();
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_log("系统管理-用户管理","编辑",userInfo.getUsername(),"编辑"+updateuser+"用户",userInfo.getIpaddr(),po.getId(),inOrOut);
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<User> search(User po){
		List<User> user = new ArrayList<>();
		try {
			user = service.search(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return user;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/delete")
	public int delete(User po,HttpServletRequest request) throws Exception{		
		User userInfo = (User) request.getSession().getAttribute("user");
		int res = service.delete(po, request);
		String updateuser=service.search(po).get(0).getUsername();
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_log("系统管理-用户管理","注销",userInfo.getUsername(),"注销"+updateuser+"用户",userInfo.getIpaddr(),po.getId(),inOrOut);
		return res;
	}
	
	//accredit
	@ResponseBody
	@RequestMapping(value="/accredit")
	public int accredit(User po,HttpServletRequest request) throws Exception{	
		User userInfo = (User) request.getSession().getAttribute("user");
		int res = service.accredit(po, request);
		String updateuser=service.search(po).get(0).getUsername();
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_log("系统管理-用户管理","角色授权",userInfo.getUsername(),"授权"+updateuser+"用户为"+po.getRolename(),userInfo.getIpaddr(),po.getId(),inOrOut);
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/lock")
	public int lock(User po,HttpServletRequest request) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		int res = service.lock(po, request);
		String updateuser=service.search(po).get(0).getUsername();
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_log("系统管理-用户管理","锁定用户",userInfo.getUsername(),"锁定"+updateuser+"用户",userInfo.getIpaddr(),po.getId(),inOrOut);
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/unlock")
	public int unlock(User po,HttpServletRequest request) throws Exception{		
		User userInfo = (User) request.getSession().getAttribute("user");
		int res = service.unlock(po, request);
		String updateuser=service.search(po).get(0).getUsername();
		String inOrOut = Util.getInOrOut(request);
		generservice.insert_oprt_log("系统管理-用户管理","解锁用户",userInfo.getUsername(),"解锁"+updateuser+"用户",userInfo.getIpaddr(),po.getId(),inOrOut);
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/checkUN")
	public List<String> checkUsername(String name){		
		String result =null; 
		result=	service.checkUsername(name);
		List<String> res = new ArrayList<String>();
		res.add(result);
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/checkLogin")
	public List<String> checkLogin(String name){		
		String result =null;
		result=	service.checkLogin(name);
		List<String> res = new ArrayList<String>();
		res.add(result);
		return res;
	}
	
	/**
	 * 获取session
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getSession")
	public String getSession(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "ERROR";
		}
		return "SECCUSS";
	}
}
