package com.nari.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.nari.dao.LoginMonitorDao;
import com.nari.po.LoginMonitor;
import com.nari.po.User;
import com.nari.util.SysConstant;

/**
 * 用户权限控制过滤器
 * 2018年7月5日14:02:27
 * @author Administrator
 *
 */
public class PermissionsFilter implements Filter {  
	
	public FilterConfig config;
	private String sjgly;  
    private String xtgly;  
    private String ywgly;
    private String sjglyjsp;  
    private String xtglyjsp;  
    private String ywglyjsp;
    
	private LoginMonitorDao loginMonitorDao;
	
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {  
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;  
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;  
        HttpSession session = httpRequest.getSession(true);  
        String url=httpRequest.getRequestURI();  
        Object object = session.getAttribute("user");  
        User user = object == null ? null : (User) object;  
        Cookie[] cookie = httpRequest.getCookies();
        if(cookie == null && user == null){
        	filterChain.doFilter(servletRequest, servletResponse);
        	return;
        }else if(cookie == null){
        	String str = "<script>top.layer.alert('登录超时,请重新登录!', function(){parent.timeOut();});setTimeout(function(){window.location.href = parent.getContextPath()+'/signout';}, 2000);</script>";
        	httpResponse.setContentType("text/html;charset=UTF-8");// 解决中文乱码
    		PrintWriter writer = httpResponse.getWriter();
    		writer.write(str);
    		writer.flush();
    		writer.close();
        	filterChain.doFilter(servletRequest, servletResponse);
        	return;
        }
		String cok = "";
		for (int i = 0; i < cookie.length; i++) {
			Cookie cook = cookie[i];
			if("JSESSIONID".equalsIgnoreCase(cook.getName())){ //获取键 
				cok = cook.getValue().toString();    //获取值 
			}
		}
		if(user!=null){
			if(!"".equals(cok)){
		        LoginMonitor po = new LoginMonitor();
				po.setUsername(user.getUsername());
				po.setIp(user.getIpaddr());
				List<LoginMonitor> list = loginMonitorDao.findCookieByUser(po);
				if(list!=null && list.size()>0){
					String saveCookie = list.get(0).getCookie();
					if(!cok.equals(saveCookie)){
						PrintWriter p = httpResponse.getWriter();
						p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
						p.flush();
						p.close();
						return;
					}
				}
	        }else{
	        	PrintWriter p = httpResponse.getWriter();
				p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
				p.flush();
				p.close();
				return;
	        }
	        String context = httpRequest.getContextPath()+"/";
	        if(!url.equals(context)){
	        	if(user != null && !url.contains(".")){
	        		int roleId = user.getRoleid();
	        		boolean result = false;
	        		//系统审计员
	        		if(roleId == SysConstant.SJGLY){
	        			String []permission = sjgly.split(",");
	        			for (int i = 0; i < permission.length; i++) {
	        				if(url.contains(permission[i])){
	        					result = true;
	        					break;
	        				}
	        			}
	        			if(!result){
	        				PrintWriter p = httpResponse.getWriter();
	        				p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
	        				p.flush();
	        				p.close();
	        				return;
	        			}
	        		}else if(roleId == SysConstant.XTGLY){
	        			//系统管理员
	        			String []permission = xtgly.split(",");
	        			for (int i = 0; i < permission.length; i++) {
	        				if(url.contains(permission[i])){
	        					result = true;
	        					break;
	        				}
	        			}
	        			if(!result){
	        				PrintWriter p = httpResponse.getWriter();
	        				p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
	        				p.flush();
	        				p.close();
	        				return;
	        			}
	        		}else{
	        			//业务操作员
	        			String []permission = ywgly.split(",");
	        			for (int i = 0; i < permission.length; i++) {
	        				if(url.contains(permission[i])){
	        					result = true;
	        					break;
	        				}
	        			}
	        			if(!result){
	        				PrintWriter p = httpResponse.getWriter();
	        				p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
	        				p.flush();
	        				p.close();
	        				return;
	        			}
	        		}
	        	}else if(url.endsWith(".jsp") && !url.endsWith("index.jsp") && !url.endsWith("error.jsp")){
	        		int roleId = user.getRoleid();
	        		boolean result = false;
	        		//系统审计员
	        		if(roleId == SysConstant.SJGLY){
	        			String []permission = sjglyjsp.split(",");
	        			for (int i = 0; i < permission.length; i++) {
	        				if(url.contains(permission[i])){
	        					result = true;
	        					break;
	        				}
	        			}
	        			if(!result){
	        				PrintWriter p = httpResponse.getWriter();
	        				p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
	        				p.flush();
	        				p.close();
	        				return;
	        			}
	        		}else if(roleId == SysConstant.XTGLY){
	        			//系统管理员
	        			String []permission = xtglyjsp.split(",");
	        			for (int i = 0; i < permission.length; i++) {
	        				if(url.contains(permission[i])){
	        					result = true;
	        					break;
	        				}
	        			}
	        			if(!result){
	        				PrintWriter p = httpResponse.getWriter();
	        				p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
	        				p.flush();
	        				p.close();
	        				return;
	        			}
	        		}else{
	        			//业务操作员
	        			String []permission = ywglyjsp.split(",");
	        			for (int i = 0; i < permission.length; i++) {
	        				if(url.contains(permission[i])){
	        					if(url.contains("operation_log") || url.contains("system_log")){
	        						continue;
	        					}
	        					result = true;
	        					break;
	        				}
	        			}
	        			if(!result){
	        				PrintWriter p = httpResponse.getWriter();
	        				p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
	        				p.flush();
	        				p.close();
	        				return;
	        			}
	        		}
	        	}
	        }
		}
        filterChain.doFilter(servletRequest, servletResponse);  
        return;   
    }  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    	this.config = filterConfig;
    	this.sjgly=filterConfig.getInitParameter("sjgly");  
        this.xtgly=filterConfig.getInitParameter("xtgly");  
        this.ywgly=filterConfig.getInitParameter("ywgly");
        this.sjglyjsp=filterConfig.getInitParameter("sjglyjsp");  
        this.xtglyjsp=filterConfig.getInitParameter("xtglyjsp");  
        this.ywglyjsp=filterConfig.getInitParameter("ywglyjsp");
        
        ServletContext sc = filterConfig.getServletContext(); 
        XmlWebApplicationContext cxt = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(sc);
        
        if(cxt != null && cxt.getBean("loginMonitorDao") != null && loginMonitorDao == null)
        	loginMonitorDao = (LoginMonitorDao) cxt.getBean("loginMonitorDao");
    }  
  
    @Override  
    public void destroy() {  
        //To change body of implemented methods use File | Settings | File Templates.  
    }  
}
