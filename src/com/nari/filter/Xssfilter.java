package com.nari.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class Xssfilter implements Filter { 
	public FilterConfig config;
	private String filterChar;  
    private String replaceChar;  
    private String splitChar;
	  
    @Override  
    public void destroy() {  
    }  
    /**  
     * 过滤器用来过滤的方法  
     */  
	@Override  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
    	//包装request  
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request,filterChar,replaceChar,splitChar);  
        chain.doFilter(xssRequest, response);  
    }  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    	config = filterConfig;
    	this.filterChar=filterConfig.getInitParameter("FilterChar");  
        this.replaceChar=filterConfig.getInitParameter("ReplaceChar");  
        this.splitChar=filterConfig.getInitParameter("SplitChar");
    }  
    
}  
