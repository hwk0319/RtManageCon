package com.nari.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.nari.po.User;

/**
 * 拦截不安全的HTTP请求
 * 只允许GET和POST请求，其他请求一律返回405
 * @author 张磊
 * @ClassName HeadFilter
 * @ModifiedBy Sotter
 * @date 2018年7月5日 下午3:24:12
 */
public class HeadFilter implements Filter {  
	private static Logger logger = Logger.getLogger(HeadFilter.class);
	
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {  
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;  
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String method = httpRequest.getMethod();
        if(!"GET".equalsIgnoreCase(method) && !"POST".equalsIgnoreCase(method)){
        	httpResponse.setHeader("Allow", "GET,POST");
        	httpResponse.setStatus(405);
        	PrintWriter p = httpResponse.getWriter();
			p.write("<script>window.location.href='"+ httpRequest.getContextPath() + "/jsps/errorPages/error.jsp"+ "';</script>");
			p.flush();
			p.close();
        	return;
        }
        filterChain.doFilter(servletRequest, servletResponse);  
        return;  
    }  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    
    }  
  
    @Override  
    public void destroy() {
    	
    }  
    
    /** 
     * 判断是否为Ajax请求 
     * 
     * @param request HttpServletRequest 
     * @return 是true, 否false 
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {  
        return request.getRequestURI().startsWith("/api");  
    }  
}  
