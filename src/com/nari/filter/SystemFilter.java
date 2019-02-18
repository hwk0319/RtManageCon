/*package com.nari.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.nari.po.User;
  
*//** 
  springMVC拦截器 判断session中用户是否过期 
 * Date: 2016-4-25 
 * To change this template use File | Settings | File Templates. 
 * 
 * @author ydd 
 *//* 
  
public class SystemFilter implements Filter {  
	private static Logger logger = Logger.getLogger(SystemFilter.class); 
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {  
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;  
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;  
        HttpSession session = httpRequest.getSession(true);  
        String url=httpRequest.getRequestURI();  
        Object object = session.getAttribute("user");  
        User user = object == null ? null : (User) object;  
        String conString = httpRequest.getHeader("REFERER");
      
        if(!httpRequest.getServletPath().contains("index.jsp"))
        {
	        if (user == null) {  
	        	 //判断获取的路径不为空且不是访问登录页面或执行登录操作时跳转  
	            if(url!=null && !url.equals("") && !httpRequest.getServletPath().contains("erro.jsp") )
	            {  
	//            	httpResponse.sendRedirect(httpRequest.getContextPath()+"/jsps/login/login.jsp"); 
	            	PrintWriter p = httpResponse.getWriter();
					p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/error/erro.jsp"+ "';</script>");
					p.flush();
					p.close();
	
	                return ;  
	            }
	        }  
	        else
	        {
		        if(conString == "" || conString == null)
		        {
		        	PrintWriter p = httpResponse.getWriter();
					p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/error/erro.jsp"+ "';</script>");
					p.flush();
					p.close();
		
		            return ;          	
		        }else
		        {
		            String perUrl; 
		            String[] shUrl = url.split("/", 0);
	//	            if(shUrl.length > 6)
	//	            {
	//	            	perUrl = shUrl[2]+"/"+shUrl[3]+"/"+shUrl[4]+"/";
	//	            }else 
		            if(shUrl.length > 4)
		            {
		            	perUrl = shUrl[2]+"/"+shUrl[3]+"/";
		            }else
		            {
		            	perUrl = url.substring(url.indexOf('/',url.indexOf('/')+1)+1, url.lastIndexOf('/')+1);
		            }
		            if(!user.getPermissions().contains(perUrl) && !perUrl.equals("jsps/")  && !perUrl.equals("jsp/comm/jsp/") && !perUrl.equals("jsp/plugins/") && !perUrl.equals("jsp/comm/") && !perUrl.equals("jsp/pages/") && !perUrl.equals("jsp/") && !perUrl.equals("jsps/error/") )
		            {
			        	PrintWriter p = httpResponse.getWriter();
						p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/error/erro.jsp"+ "';</script>");
						p.flush();
						p.close();
			
			            return ; 	            	
		            }
		        }
        }
        }
        
        filterChain.doFilter(servletRequest, servletResponse);  
        return;  
    }  
  
    *//** 
     * 判断是否为Ajax请求 
     * 
     * @param request HttpServletRequest 
     * @return 是true, 否false 
     *//*
    public static boolean isAjaxRequest(HttpServletRequest request) {  
        return request.getRequestURI().startsWith("/api");  
//        String requestType = request.getHeader("X-Requested-With");  
//        return requestType != null && requestType.equals("XMLHttpRequest");  
    }  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    }  
  
    @Override  
    public void destroy() {  
        //To change body of implemented methods use File | Settings | File Templates.  
    }  
} */ 





package com.nari.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.nari.module.general.service.GeneranService;
import com.nari.po.User;
import com.nari.util.SysConstant;
  
/** 
 * springMVC拦截器 判断session中用户是否过期 
 * Date: 2016-4-25 
 * To change this template use File | Settings | File Templates. 
 * 
 * @author ydd */
   
  
public class SystemFilter implements Filter {  
	private static Logger logger = Logger.getLogger(SystemFilter.class); 
	
	private GeneranService generservice;
	
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {  
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;  
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;  
        HttpSession session = httpRequest.getSession(true);  
        String url=httpRequest.getRequestURI();  
        Object object = session.getAttribute("user");  
        User user = object == null ? null : (User) object;  
//        logger.info("fangwen:----------------"+url);
//        System.out.println(httpRequest.getSession().getServletContext().getRealPath("/")+"-------" );
        String conString = httpRequest.getHeader("REFERER");
        if (user == null) {  
        	 //判断获取的路径不为空且不是访问登录页面或执行登录操作时跳转  
            if(url!=null && !url.equals("") && url.indexOf("/jsps/login/login.jsp")<0)
            {  
//            	generservice.insert_oprt_logs("越权访问","越权",null,"越权访问",user.getIpaddr(),user.getId(),SysConstant.OPRTLOG_FAIL);
            	PrintWriter p = httpResponse.getWriter();
            	p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/login/login.jsp"+ "';</script>");
				p.flush();
				p.close();
        
                return ;  
            }
        }else{
	        if(conString == "" || conString == null)
	        {
//	        	generservice.insert_oprt_logs("越权访问","越权",user.getUsername(),"越权访问",user.getIpaddr(),user.getId(),SysConstant.OPRTLOG_FAIL);
	        	String context = httpRequest.getContextPath()+"/";
	        	PrintWriter p = httpResponse.getWriter();
	            if(url.equals(context)){
	            	p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/index.jsp"+ "';</script>");
	            }else{
	            	p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
	            }
	            p.flush();
				p.close();
	            return ;           	
	        }else
	        {
	        	String perUrl; 
	            String[] shUrl = url.split("/", 0);
	            if(shUrl.length > 4)
	            {
	            	perUrl = shUrl[2]+"/"+shUrl[3]+"/";
	            }else
	            {
	            	perUrl = url.substring(url.indexOf('/',url.indexOf('/')+1)+1, url.lastIndexOf('/')+1);
	            }
//	            if(!user.getPermissions().contains(perUrl) && !perUrl.equals("jsps/")  && !perUrl.equals("jsp/comm/jsp/") && !perUrl.equals("jsp/plugins/") && !perUrl.equals("jsp/comm/") && !perUrl.equals("jsp/pages/") && !perUrl.equals("jsp/") && !perUrl.equals("jsps/errorPages/") )
//	            {
//		        	PrintWriter p = httpResponse.getWriter();
//					p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
//					p.flush();
//					p.close();
//		            return ; 	            	
//	            }
	        }
        }

        
        filterChain.doFilter(servletRequest, servletResponse);  
        return;  
    }  
  
    /** 
     * 判断是否为Ajax请求 
     * 
     * @param request HttpServletRequest 
     * @return 是true, 否false 
     */ 
    public static boolean isAjaxRequest(HttpServletRequest request) {  
        return request.getRequestURI().startsWith("/api");  
//        String requestType = request.getHeader("X-Requested-With");  
//        return requestType != null && requestType.equals("XMLHttpRequest");  
    }  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    	ServletContext sc = filterConfig.getServletContext(); 
        XmlWebApplicationContext cxt = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(sc);
        
        if(cxt != null && cxt.getBean("GeneranService") != null && generservice == null)
        	generservice = (GeneranService) cxt.getBean("GeneranService");
    }  
  
    @Override  
    public void destroy() {  
        //To change body of implemented methods use File | Settings | File Templates.  
    }  
}  
