package com.nari.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
  
public class SystemFilterTest implements Filter {  
	private static Logger logger = Logger.getLogger(SystemFilterTest.class); 
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {  
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;  
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;  
        String servletPath = httpRequest.getServletPath(); 
        String url=httpRequest.getRequestURI();
        logger.info("url:"+url);
        logger.info("filter url:"+servletPath);
        logger.info("filter servletPath:"+servletPath);
      if(httpRequest.getServletPath().equals("/services")){
          logger.info("break filter on url:"+servletPath);
          if(url.contains("/services/I6000")){
        	  httpRequest.getRequestDispatcher("/services/I6000?wsdl").forward(httpRequest, httpResponse);
          }else{
        	  httpRequest.getRequestDispatcher("/services/TaskService?wsdl").forward(httpRequest, httpResponse);
          }
    	  return;
      }
        return;  
    }  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    }  
  
    @Override  
    public void destroy() {  
        //To change body of implemented methods use File | Settings | File Templates.  
    }  
}
