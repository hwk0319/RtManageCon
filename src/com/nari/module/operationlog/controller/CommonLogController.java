package com.nari.module.operationlog.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.po.User;
import com.nari.util.JWTUtil;
import com.nari.util.Util;


@Controller
@RequestMapping(value="commonConLog")
public class CommonLogController extends BaseController {
	
	/**
	 * 创建JWT
	 * @param request
	 * @return
	 * 2018年5月28日11:27:35
	 */
	@ResponseBody
	@RequestMapping(value="/createJWT")
	public String createJWT(HttpServletRequest request){
		User userInfo = (User) request.getSession().getAttribute("user");
		String id = userInfo.getId()+"";
		String jwt = JWTUtil.createJWT(userInfo.getUsername(), id, userInfo.getRolename());
		jwt = Util.xssEncode(jwt);
		request.getSession().setAttribute("jwt", jwt);
		return jwt;
	}
}
