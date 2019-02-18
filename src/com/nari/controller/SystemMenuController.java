package com.nari.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.po.SystemMenu;
import com.nari.po.User;
import com.nari.service.SystemMenuService;

@Controller
@Service
@RequestMapping("/sysMenu")
public class SystemMenuController {
	@Resource
	private SystemMenuService menuservice;

	@RequestMapping("/menu")
	@ResponseBody
	public List<SystemMenu> findSystemMenu(HttpServletRequest request) {
		List<SystemMenu> list = menuservice.findMenuCode(request);
		return list;
	}

	@RequestMapping("/findmenubyp")
	@ResponseBody
	public List<SystemMenu> findMenuByPCode(String parentcode, HttpServletRequest request) {
		//判断用户是否拥有请求菜单权限
		User userInfo = (User) request.getSession().getAttribute("user");
		List<SystemMenu> list = new ArrayList<SystemMenu>();
		String[] code = userInfo.getFuncs().split(",");
		if(code != null && code.length > 0){
			for (int i = 0; i < code.length; i++) {
				if(code[i].equals(parentcode)){
					list = menuservice.findMenuByPCode(parentcode);
					break;
				}
			}
		} 
		return list;
	}
}
