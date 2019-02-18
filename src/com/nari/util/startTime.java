package com.nari.util;

import java.util.Date;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;


public class startTime implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		recordStartTime(arg0);
	}

	/**
	 * 记录启动系统时间
	 */
	public void recordStartTime(ContextRefreshedEvent event){
		
		ApplicationContext applicationContext = event.getApplicationContext();
		WebApplicationContext webApplicationContext = (WebApplicationContext)applicationContext;
		ServletContext servletContext = webApplicationContext.getServletContext();
		
		servletContext.setAttribute("startTime", new Date());  
	}
}
