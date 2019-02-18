/*package com.nari.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.nari.dao.LoginMonitorDao;
import com.nari.dao.OprtLogDao;
import com.nari.dao.SysParamDao;
import com.nari.dao.UsersDao;
import com.nari.po.OprtLog;
import com.nari.po.User;
import com.nari.util.LogConstant;
import com.nari.util.MyPropertiesPersist;
import com.sgcc.isc.core.orm.role.OrganizationalRole;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.factory.agent.FrontLoadedAgantManage;
import com.sgcc.isc.service.adapter.factory.agent.IFrontLoadedAgent;
import com.sgcc.isc.service.adapter.factory.agent.config.CacheConfig;
import com.sgcc.isc.service.adapter.factory.agent.config.ServiceAgentConfiguration;
import com.sgcc.isc.service.adapter.helper.IRoleService;
import com.sgcc.isc.ualogin.client.util.IscSSOResourceUtil;
import com.sgcc.isc.ualogin.client.vo.IscSSOUserBean;

@Service(value="loginService")
public class LoginService {
	@Resource
	private UsersDao dao;
	@Resource(name="oprtLogDao")
	private OprtLogDao logDao;
	@Resource
	private LoginMonitorDao loginMonitorDao;
	@Resource(name="sysParamDao")
	private SysParamDao paramDao;
	
	
	public String login(String name, String pwd, String identifyCode,HttpServletRequest request) {
		
		String name_isc="";
		String RoleId_isc;
		//**************isc用户校验,如果isc校验通过，而本地无此用户，则新增该用户，然后进行本地用户校验；如果isc校验通过，本地有此用户，则以此用 a户名密码继续进行本地用户校验*********************************************************************************//*
		com.sgcc.isc.core.orm.identity.User IscUser;
		try {
			IscSSOUserBean userbean =IscSSOResourceUtil.getIscUserBean(request);
			
			//在启动业务应用时，配置服务代理的配置，初始化服务代理 
			IFrontLoadedAgent agent = FrontLoadedAgantManage.getInstance();
			//配置
			CacheConfig cacheConfig = new 
					CacheConfig("failover:(tcp://192.168.30.90:61616)","AGENT_MQ_402881483c18585e013c185c01c10001");
			ServiceAgentConfiguration agentConfig = new ServiceAgentConfiguration();
			agentConfig.setCacheConfig(cacheConfig);
			agentConfig.setServiceURI("http://192.168.30.90:22002/isc_frontmv_serv");
			agentConfig.setAppId("8a23694f424b956e01424b99dc5c0004");
			agentConfig.setCacheJmsURI("");
			agentConfig.setLogJmsURI("");
			//设置前置节点配置信息
			agent.setConfigProps(agentConfig);
			//启动缓存jms，缓存服务，日志服务，初始化服务容器
			agent.startupAll();
			
			IRoleService rs2 = (IRoleService) AdapterFactory.getInstance(Constants.CLASS_ROLE);
			
			String userid_isc=userbean.getIscUserId();
			name_isc=userbean.getIscUserSourceId();
			Map parm=new HashMap();
			parm.put(null,null);
			List<OrganizationalRole> list=rs2.getOrgRolesByUserId(userid_isc, "8a23694f424b956e01424b99dc5c0004", parm);
			RoleId_isc=list.get(0).getId();
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "isc校验失败";
		}
		//***********************************************************************************************//*
		
		//查询已登录系统的人数，超过一定的值，不允许在登录系统
//		int loginNumber = loginMonitorDao.findCount();
//		SysParamPo sysParamPo = paramDao.findByCode("2000");
//		if(loginNumber >= Integer.parseInt(sysParamPo.getValue())){
//			return "登录人数已达上限";
//		}
		//rsa解密用户名
		//String userName = RSAUtils.decryptStringByJs(name);
		//根据用户名查找是否存在此用户
		User userInfo = dao.findUserByName(name_isc);
		if(userInfo ==null){
			User po = new User();
			po.setUsername(name_isc);
			if(RoleId_isc.equals("4028a8005fd91d3a015fdde3f572003e")){
				po.setRoleid(1);
				po.setRolename("身份管理员");
			}else if(RoleId_isc.equals("4028a8005fd91d3a015fdde461ac003f")){
				po.setRoleid(2);
				po.setRolename("运维人员");
			}else if(RoleId_isc.equals("4028a8005fd91d3a015fdde4f0630040")){
				po.setRoleid(3);
				po.setRolename("审计业务员");
			}
			dao.insert_isc(po);
			
		}
		userInfo=  dao.findUserByName(name_isc);
		if(userInfo !=null){
			//增加权限列表
			List<String> permissions = new ArrayList<String>();
			String[] funcs = userInfo.getFuncs().split(",");
			
			for(String func : funcs)
			{
				List<String> pers = dao.getPermissions(func,MyPropertiesPersist.DBTYPE);
				
				for(String per :pers)
				{
					String[] temp = per.split("/");
					if(temp.length > 3)
					{
						String perm = temp[0]+"/"+temp[1]+"/"+temp[2]+"/";
						permissions.add(perm);								
					}else if(temp.length == 2)
					{
						permissions.add(per);
					}

				}
			}
			
			userInfo.setPermissions(permissions);
			request.getSession().setAttribute("user", userInfo);
		}
		return userInfo.getFirstlogin()+","+userInfo.getId()+","+userInfo.getUsername();
			if (userInfo != null) {
			//判断用户是否被注销
			User userInfo1 = dao.findUserByStatus(name_isc);
			if(userInfo1 != null){
				return "用户被注销";
			}
			//判断用户是否被锁定
			User userInfo2 = dao.findUserByStatusLock(name_isc);
			if(userInfo2 != null){
				return "用户被锁定";
			}
			//判断用户口令是否过期
			if(!userInfo.getFirstlogin().trim().equals("1")){
				try{
					Date pwdTime = userInfo.getPwdtime();
					Date date = new Date();
					if(date.getTime()>pwdTime.getTime()){
						return "密码已到期，请联系管理员";
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			//判断用户是否在规定IP内登录
			if(StringUtils.isNotBlank(userInfo.getStart_ip()) && StringUtils.isNotBlank(userInfo.getEnd_ip())){
				//把开始IP跟结束IP转换为long型数字
				long startIp = IpChange.ipToLong(userInfo.getStart_ip().trim());
				long endIp = IpChange.ipToLong( userInfo.getEnd_ip().trim());
				//获取用户的IP地址并转换为long型数字
				long userIp = IpChange.ipToLong(IpUtils.getIpAddr(request));
				if(!((startIp<=userIp)&&(userIp<=endIp))){
					return "请在规定IP段内登录";
				}
			}
			//判断用户是否在规定的时间间隔内登录
			if(StringUtils.isNotBlank(userInfo.getStart_time()) && StringUtils.isNotBlank(userInfo.getEnd_time())){
				int startTime = Integer.parseInt(userInfo.getStart_time().trim());
				int endTime = Integer.parseInt(userInfo.getEnd_time().trim());
				Date date = new Date();
				int hour = date.getHours();
				if(startTime < endTime){
					if(!((startTime<=hour)&&(hour<endTime))){
						return "请在规定时间间隔内登录";
					}
				}else if(startTime == endTime){
					return "请在规定时间间隔内登录";
				}else{
					if(!(((startTime<=hour)&&(hour<=23))||((0<=hour)&&(hour<endTime)))){
						return "请在规定时间间隔内登录";
					}
				}
			}
//			//判断用户是否已经登录
			LoginMonitor loginMonitor1 = loginMonitorDao.findUserByName(userName);
			if(loginMonitor1 != null){
				return "用户已登录";
			}
			//获取用户的IP地址
			String userIp  = IpUtils.getIpAddr(request);
//			//判断一台电脑上是否只登录了一个用户
			LoginMonitor loginMonitor2 = loginMonitorDao.findUserByIp(userIp);
			if(loginMonitor2 != null){
				return "一台设备只能登录一个用户";
			}
			//解密传输密码
			String passW = RSAUtils.decryptStringByJs(pwd);
			//MD5加密密码
			String pass_md5 = passW.substring(0,32);
			if (pass_md5 != null && !pass_md5.equals("")) {
				//原文密码
				String pass = passW.substring(32, passW.length());
				pass =  MD5.GetMD5Code(pass);
				//验证js密码传输是否正确
				if(!pass_md5.equals(pass)){
					return "密码传输验证不正确。";
				}
				//验证数据库rsa加密密码和md5加密密码是否一致
				String pass_rsa_pg = userInfo.getPassword();
				//获取数据库md5密码
				String pass_md5_pg = userInfo.getPasswordm();
				String pass_pg = RSAUtils.decryptStringByJs(pass_rsa_pg);
				pass_pg = MD5.GetMD5Code(pass_pg);
				if(!pass_pg.equals(pass_md5_pg)){
					return "数据库密码一致性验证错误";
				}
				if (pass_md5.equals(pass_md5_pg)) {
					userInfo.setIpaddr(userIp);
					
					//增加权限列表
					List<String> permissions = new ArrayList<String>();
					String[] funcs = userInfo.getFuncs().split(",");
					
					for(String func : funcs)
					{
						List<String> pers = dao.getPermissions(func,MyPropertiesPersist.DBTYPE);
						
						for(String per :pers)
						{
							String[] temp = per.split("/");
							if(temp.length > 3)
							{
								String perm = temp[0]+"/"+temp[1]+"/"+temp[2]+"/";
								permissions.add(perm);								
							}else if(temp.length == 2)
							{
								permissions.add(per);
							}

						}
					}
					
					userInfo.setPermissions(permissions);
					
					request.getSession().setAttribute("user", userInfo);
					//往登录信息记录表里面插入一条记录  (用户名跟主机IP)  第一次登陆的时候，先不插入登陆记录表，等用户修改密码完毕再插入记录表
					if(!userInfo.getFirstlogin().trim().equals("1")){
						//记录登录信息
						LoginMonitor loginMonitor = new LoginMonitor();
						loginMonitor.setIp(userIp);
						loginMonitor.setUsername(name_isc);
						loginMonitorDao.insert(loginMonitor);
					}
					//保存操作日志
					OprtLog logPo = new OprtLog();
					logPo.setModule(LogConstant.MODULE_LOGIN);
					logPo.setOprt_type(LogConstant.OPRT_TYPE_LOGON);
					logPo.setOprt_user(name_isc);
					logPo.setOprt_content("用户登录。");
					logPo.setIp(userInfo.getIpaddr());
					saveLog(logPo);
					//返回是否第一次登陆的状态、用户id、用户名
					return userInfo.getFirstlogin()+","+userInfo.getId()+","+userInfo.getUsername();
				} else {
					User user = new User();
					user.setUsername(name_isc);
					dao.updateErrNum(user);
					SysParamPo sysParam = paramDao.findByCode("1000");
					User user_errNum = dao.findUserByName(name_isc);
					if(user_errNum.getErr_num() > Integer.parseInt(sysParam.getValue())){
						//将用户状态锁定
						dao.updateStatus(name_isc);
						//密码错误次数已达上限，账户被锁定，给管理员发送邮件
						SysParamPo sysParamSend = paramDao.findByCode("5000");
						SysParamPo sysParamReceive = paramDao.findByCode("6000");
						String subject = "用户账号被锁定";
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
						String time = sdf.format(date);
						String text = "用户输入密码错误次数已达上限，账号已于"+ time +"锁定";
						SendEmailUtil.sendTextMail(sysParamSend.getName(), sysParamSend.getValue(), sysParamReceive.getName(), subject, text);
						return "账户已被锁定，请咨询管理员";
					}
					return "密码错误";
				}
			} else {
				return "用户不存在";
			}
		} else {
			return "用户不存在";
		}

		
		
	}
	
	public User iscLogin(HttpServletRequest request) throws Exception{

		String name_isc="";
		String RoleId_isc;
		//**************isc用户校验,如果isc校验通过，而本地无此用户，则新增该用户，然后进行本地用户校验；如果isc校验通过，本地有此用户，则以此用 a户名密码继续进行本地用户校验*********************************************************************************//*
		com.sgcc.isc.core.orm.identity.User IscUser;

			IscSSOUserBean userbean =IscSSOResourceUtil.getIscUserBean(request);
			
			//在启动业务应用时，配置服务代理的配置，初始化服务代理 
			IFrontLoadedAgent agent = FrontLoadedAgantManage.getInstance();
			//配置
			CacheConfig cacheConfig = new 
					CacheConfig("failover:(tcp://192.168.30.90:61616)","AGENT_MQ_402881483c18585e013c185c01c10001");
			cacheConfig.setIscache(false);
			ServiceAgentConfiguration agentConfig = new ServiceAgentConfiguration();
			agentConfig.setCacheConfig(cacheConfig);
			agentConfig.setServiceURI("http://192.168.30.90:22002/isc_frontmv_serv");
			agentConfig.setAppId("8a23694f424b956e01424b99dc5c0004");
			//agentConfig.setCacheJmsURI("");
			//agentConfig.setLogJmsURI("");
			//设置前置节点配置信息
			agent.setConfigProps(agentConfig);
			//启动缓存jms，缓存服务，日志服务，初始化服务容器
			agent.startupAll();
			IRoleService rs2 = (IRoleService) AdapterFactory.getInstance(Constants.CLASS_ROLE);
			
			String userid_isc=userbean.getIscUserId();
			name_isc=userbean.getIscUserSourceId();
			Map parm=new HashMap();
			parm.put(null,null);
			List<OrganizationalRole> list=rs2.getOrgRolesByUserId(userid_isc, "8a23694f424b956e01424b99dc5c0004", parm);
			RoleId_isc=list.get(0).getId();
			
		//***********************************************************************************************//*
		
//查询已登录系统的人数，超过一定的值，不允许在登录系统
int loginNumber = loginMonitorDao.findCount();
SysParamPo sysParamPo = paramDao.findByCode("2000");
if(loginNumber >= Integer.parseInt(sysParamPo.getValue())){
	return "登录人数已达上限";
}
		//rsa解密用户名
		//String userName = RSAUtils.decryptStringByJs(name);
		//根据用户名查找是否存在此用户
		User userInfo = dao.findUserByName(name_isc);
		if(userInfo ==null){
			User po = new User();
			po.setUsername(name_isc);
			if(RoleId_isc.equals("4028a8005fd91d3a015fdde3f572003e")){
				po.setRoleid(1);
				po.setRolename("身份管理员");
			}else if(RoleId_isc.equals("4028a8005fd91d3a015fdde461ac003f")){
				po.setRoleid(2);
				po.setRolename("运维人员");
			}else if(RoleId_isc.equals("4028a8005fd91d3a015fdde4f0630040")){
				po.setRoleid(3);
				po.setRolename("审计业务员");
			}
			po.setRoleid(2);
			po.setRolename("运维人员");
			dao.insert_isc(po);
			
		}
		userInfo=  dao.findUserByName(name_isc);
		if(userInfo !=null){
			//增加权限列表
			List<String> permissions = new ArrayList<String>();
			String[] funcs = userInfo.getFuncs().split(",");
			
			for(String func : funcs)
			{
				List<String> pers = dao.getPermissions(func,MyPropertiesPersist.DBTYPE);
				
				for(String per :pers)
				{
					String[] temp = per.split("/");
					if(temp.length > 3)
					{
						String perm = temp[0]+"/"+temp[1]+"/"+temp[2]+"/";
						permissions.add(perm);								
					}else if(temp.length == 2)
					{
						permissions.add(per);
					}

				}
			}
			
			userInfo.setPermissions(permissions);
			request.getSession().setAttribute("user", userInfo);
		
		}
		return userInfo;
	}
	
	public void saveLog(OprtLog logPo){
		String date="";
		if(MyPropertiesPersist.DBTYPE.equalsIgnoreCase("ORACLE")){
			date="sysdate";
		}else{
			date="now()";
		}
		String sql="insert into sys_oprt_log(module,oprt_type,oprt_user,oprt_content,oprt_info,oprt_time,ip) values "
				+ "('"+logPo.getModule()+"','"+logPo.getOprt_type()+"','"+logPo.getOprt_user()+"','"+logPo.getOprt_content()+"','',"+date+",'"+logPo.getIp()+"')";
		logPo.setSql(sql);
		logDao.insert(logPo);
	}
	
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user =  (User) session.getAttribute("user");
	
		
		//保存操作日志
		OprtLog logPo = new OprtLog();
		logPo.setModule(LogConstant.MODULE_LOGIN);
		logPo.setOprt_type(LogConstant.OPRT_TYPE_LOGOFF);
		logPo.setOprt_user(user.getUsername());
		logPo.setOprt_content("用户注销。"); 
		logPo.setIp(user.getIpaddr());
		saveLog(logPo);
		
		session.invalidate();
//		LoginMonitor loginMonitor = new LoginMonitor();
//		//获取用户的IP地址
//		String userIp  = IpUtils.getIpAddr(request);
//		loginMonitor.setIp(userIp);
//		loginMonitorDao.delete(loginMonitor);
		return "success";
	}
}*/



package com.nari.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.nari.dao.LoginMonitorDao;
import com.nari.dao.OprtLogDao;
import com.nari.dao.SysParamDao;
import com.nari.dao.UsersDao;
import com.nari.po.LoginMonitor;
import com.nari.po.OprtLog;
import com.nari.po.SysParamPo;
import com.nari.po.User;
import com.nari.util.LogConstant;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.RSAUtils;
import com.nari.util.IpUtils;
import com.nari.util.Util;

@Service(value="loginService")
public class LoginService {
	@Resource
	private UsersDao dao;
	@Resource(name="oprtLogDao")
	private OprtLogDao logDao;
	@Resource
	private LoginMonitorDao loginMonitorDao;
	@Resource(name="sysParamDao")
	private SysParamDao paramDao;
		
	public String login(String name, String pwd, String identifyCode,HttpServletRequest request) throws Exception {

		//rsa解密用户名
		String userName = RSAUtils.decryptStringByJs(name);
		//根据用户名查找是否存在此用户
		User userInfo = dao.findUserByName(userName);
		if (userInfo != null) {
			//判断用户是否被注销
			User userInfo1 = dao.findUserByStatus(userName);
			if(userInfo1 != null){
				return "用户被注销";
			}
			//判断用户是否被锁定
			User userInfo2 = dao.findUserByStatusLock(userName);
			if(userInfo2 != null){
				return "用户被锁定";
			}

			//获取用户的IP地址
			String userIp  = IpUtils.getIpAddr(request);

			//解密传输密码
			String passW = RSAUtils.decryptStringByJs(pwd);
			//MD5加密密码
			String pass_md5 = passW.substring(0,32);
			if (pass_md5 != null && !pass_md5.equals("")) {
				//原文密码
				String pass = passW.substring(32, passW.length());
				pass =  MD5.GetMD5Code(pass);
				//验证js密码传输是否正确
				if(!pass_md5.equals(pass)){
					return "密码传输验证不正确。";
				}
				//验证数据库rsa加密密码和md5加密密码是否一致
				String pass_rsa_pg = userInfo.getMm();
				//获取数据库md5密码
				String pass_md5_pg = userInfo.getMmM();
				String pass_pg = RSAUtils.decryptStringByJs(pass_rsa_pg);
				pass_pg = MD5.GetMD5Code(pass_pg);
//				if(!pass_pg.equals(pass_md5_pg)){
//					return "数据库密码一致性验证错误";
//				}
				if (pass_md5.equals(pass_md5_pg)) {
					userInfo.setIpaddr(userIp);
					
					//增加权限列表
					List<String> permissions = new ArrayList<String>();
					String[] funcs = userInfo.getFuncs().split(",");
					
					for(String func : funcs)
					{
						List<String> pers = dao.getPermissions(func,MyPropertiesPersist.DBTYPE);
						
						for(String per :pers)
						{
							String[] temp = per.split("/");
							if(temp.length > 3)
							{
								String perm = temp[0]+"/"+temp[1]+"/"+temp[2]+"/";
								permissions.add(perm);								
							}else if(temp.length == 2)
							{
								permissions.add(per);
							}else if(temp.length == 3)
							{
								permissions.add(per);
							}

						}
					}
					
					userInfo.setPermissions(permissions);
					
					request.getSession().setAttribute("user", userInfo);
					//往登录信息记录表里面插入一条记录  (用户名跟主机IP)  第一次登陆的时候，先不插入登陆记录表，等用户修改密码完毕再插入记录表
					if(!userInfo.getFirstlogin().trim().equals("1")){
						//记录登录信息
						Cookie[] cookie = request.getCookies();
						String cok = "";
						for (int i = 0; i < cookie.length; i++) {
							Cookie cook = cookie[i];
							if("JSESSIONID".equalsIgnoreCase(cook.getName())){ //获取键 
								cok = cook.getValue().toString();    //获取值 
							}
						}
						LoginMonitor loginMonitor = new LoginMonitor();
						loginMonitor.setIp(userIp);
						loginMonitor.setUsername(userName);
						loginMonitor.setCookie(cok);
						loginMonitorDao.insert(loginMonitor);
					}
					//保存操作日志
					OprtLog logPo = new OprtLog();
					logPo.setModule(LogConstant.MODULE_LOGIN);
					logPo.setOprt_type(LogConstant.OPRT_TYPE_LOGON);
					logPo.setOprt_user(userName);
					logPo.setOprt_content("用户登录。");
					logPo.setIp(userInfo.getIpaddr());
					logPo.setPosition(Util.getInOrOut(request));
					saveLog(logPo);
					//返回是否第一次登陆的状态、用户id、用户名
					return userInfo.getFirstlogin()+","+userInfo.getId()+","+userInfo.getUsername();
				} else {
					User user = new User();
					user.setUsername(userName);
					dao.updateErrNum(user);
					SysParamPo sysParam = paramDao.findByCode("1000");
					User user_errNum = dao.findUserByName(userName);
					if(user_errNum.getErr_num() > Integer.parseInt(sysParam.getValue())){
						//将用户状态锁定
						dao.updateStatus(userName);
						//密码错误次数已达上限，账户被锁定，给管理员发送邮件
						SysParamPo sysParamSend = paramDao.findByCode("5000");
						SysParamPo sysParamReceive = paramDao.findByCode("6000");
						String subject = "用户账号被锁定";
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
						String time = sdf.format(date);
						String text = "用户输入密码错误次数已达上限，账号已于"+ time +"锁定";
						return "账户已被锁定，请咨询管理员";
					}
					return "密码错误";
				}
			} else {
				return "用户不存在";
			}
		} else {
			return "用户不存在";
		}
	}
	public void saveLog(OprtLog logPo){
		String date="";
		if(MyPropertiesPersist.DBTYPE.equalsIgnoreCase("ORACLE")){
			date="sysdate";
		}else{
			date="now()";
		}
		String sql="insert into sys_oprt_log(module,oprt_type,oprt_user,oprt_content,oprt_info,oprt_time,ip,position) values "
				+ "('"+logPo.getModule()+"','"+logPo.getOprt_type()+"','"+logPo.getOprt_user()+"','"+logPo.getOprt_content()+"','',"+date+",'"+logPo.getIp()+"','"+logPo.getPosition()+"')";
		logPo.setSql(sql);
		logDao.insert(logPo);
	}
	
	public String logout(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		User user =  (User) session.getAttribute("user");
		//保存操作日志
		OprtLog logPo = new OprtLog();
		logPo.setModule(LogConstant.MODULE_LOGIN);
		logPo.setOprt_type(LogConstant.OPRT_TYPE_LOGOFF);
		logPo.setOprt_user(user.getUsername());
		logPo.setOprt_content("用户注销。");
		logPo.setIp(user.getIpaddr());
		logPo.setPosition(Util.getInOrOut(request));
		this.saveLog(logPo);
		session.invalidate();
		LoginMonitor loginMonitor = new LoginMonitor();
		//获取用户的IP地址
		String userIp  = IpUtils.getIpAddr(request);
		loginMonitor.setIp(userIp);
		loginMonitorDao.delete(loginMonitor);
		return "success";
	}
}
