/*package com.nari.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.po.User;
import com.nari.service.LoginService;
import com.nari.util.RSAUtils;
import com.nari.util.SysConstant;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Resource(name="loginService")
	private LoginService service;
	private static String publicKeyString = null;
	private static Logger logger = Logger.getLogger(LoginController.class);
	*//**
	 * 初始化rsa公钥
	 * @return
	 *//*
	@RequestMapping(value="initModel")
	@ResponseBody
	public String initModels(HttpServletRequest request){
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/");
		RSAUtils.setUrlPath(path);
		RSAPublicKey publicKey = RSAUtils.getDefaultPublicKey();
		publicKeyString = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
		request.getSession().setAttribute("model", publicKeyString);
		return publicKeyString;
	}
	*//**
	 * 验证码比对
	 * @param code
	 * @return
	 *//*
	@RequestMapping(value="verfify")
	@ResponseBody
	public String verifyCode(String code,HttpServletRequest request ){
		String code_sess = request.getSession().getAttribute(SysConstant.IDENTIFY_CODE).toString();
		if(code_sess.equalsIgnoreCase(code)){
			return "success";
		}else{
			return "false";
		}
	}
	*//**
	 * 根据用户名查询用户信息，用户不存在时返回null
	 * @param name
	 * @return
	 *//*
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public List<String> login(String name, String password,String identifyCode,
			HttpServletRequest req) {
		logger.info("user login username:"+"*****");
		String result = service.login(name, password, identifyCode,req);
		List<String> res = new ArrayList<String>();
		res.add(result);
		return res;
	}


	@RequestMapping(value = "/logout")
	@ResponseBody
	public List<String> logout(HttpServletRequest req) {
		String result = service.logout(req);
		List<String> res = new ArrayList<String>();
		res.add(result);
		return res;
	}
	
	@RequestMapping(value = "/iscLogin")
	@ResponseBody
	public User iscLogin(HttpServletRequest req) {
		User re = null;
		try {
			re = service.iscLogin(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return re;
	}	
	
}*/

package com.nari.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.po.User;
import com.nari.service.LoginService;
import com.nari.util.RSAUtils;
import com.nari.util.SysConstant;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Resource(name="loginService")
	private LoginService service;
	private static Logger logger = Logger.getLogger(LoginController.class);
	private static String publicKeyString = null;
	/**
	 * 初始化rsa公钥
	 * @return
	 */
	@RequestMapping(value="initModel")
	@ResponseBody
	public String initModels(HttpServletRequest request){
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		String publicKeyString = rsaTool.getPublicKey();
		request.getSession().setAttribute("models", publicKeyString);
		return publicKeyString;
	}
	/**
	 * 验证码比对
	 * @param code
	 * @return
	 */
	@RequestMapping(value="verfify")
	@ResponseBody
	public String verifyCode(String code,HttpServletRequest request ){
		String code_sess = request.getSession().getAttribute(SysConstant.IDENTIFY_CODE).toString();
		if(code_sess.equalsIgnoreCase(code)){
			return "success";
		}else{
			return "false";
		}
	}
	/**
	 * 根据用户名查询用户信息，用户不存在时返回null
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public List<String> login(String name, String pwd,String identifyCode,
			HttpServletRequest req) throws Exception {
		logger.info("user login username:"+"*****");
		String result = service.login(name, pwd, identifyCode,req);
		List<String> res = new ArrayList<String>();
		res.add(result);
		return res;
	}


	@RequestMapping(value = "/logout")
	@ResponseBody
	public List<String> logout(HttpServletRequest req) throws Exception {
		String result = service.logout(req);
		List<String> res = new ArrayList<String>();
		res.add(result);
		return res;
	}

	/**
	 * 初始化rsa公钥
	 * @return
	 */
	@RequestMapping(value="initModels")
	@ResponseBody
	public String initModels1(HttpServletRequest request){
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/");
		RSAUtils.setUrlPath(path);
		RSAPublicKey publicKey = RSAUtils.getDefaultPublicKey();
		publicKeyString = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
		request.getSession().setAttribute("model", publicKeyString);
		return publicKeyString;
	}
	
	/**
	 * 获取用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getUsername")
	@ResponseBody
	public User getUsername(HttpServletRequest request) {
		User userInfo = (User) request.getSession().getAttribute("user");
		return userInfo;
	}	
}
