package com.nari.util;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nari.dao.LoginMonitorDao;
import com.nari.dao.SysParamDao;
import com.nari.po.LoginMonitor;
import com.nari.po.SysParamPo;
import com.nari.po.User;

public class SessionListener implements HttpSessionListener{	
	private SysParamPo sysParamPo = new SysParamPo();

	public void sessionCreated(HttpSessionEvent arg0) {
		SysParamDao sysParams=(SysParamDao)this.getObjectFromApplication(arg0.getSession().getServletContext(), "sysParamDao"); 
		//超时时间
		int sessionTimeout = 1800;//秒
//		int sessionTimeout = 30;//秒
		//获取配置信息中设置的会话超时时间
		sysParamPo.setCode(SysConstant.SESSION_CODE);
		List<SysParamPo> sysList = sysParams.search(sysParamPo);
		if(sysList!=null && sysList.size()>0){
			SysParamPo sessionParam  = sysList.get(0);
			sessionTimeout = sessionParam.getValue()==null?1800:Integer.parseInt(sessionParam.getValue());
			if(sessionTimeout > 1800){
				sessionTimeout = 1800;
			}
		}
		//设置session的超时时间
		arg0.getSession().setMaxInactiveInterval(sessionTimeout);
	}
	
    /** 
     * 通过WebApplicationContextUtils 得到Spring容器的实例。根据bean的名称返回bean的实例。 
     * @param servletContext  ：ServletContext上下文。 
     * @param beanName  :要取得的Spring容器中Bean的名称。 
     * @return 返回Bean的实例。 
     */  
    private Object getObjectFromApplication(ServletContext servletContext,String beanName){  
        //通过WebApplicationContextUtils 得到Spring容器的实例。  
        ApplicationContext application=WebApplicationContextUtils.getWebApplicationContext(servletContext);  
        //返回Bean的实例。  
        return application.getBean(beanName);  
    } 

	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		session.invalidate();
		User user = (User) session.getAttribute("user");
		if(user!=null){
			String ip = user.getIpaddr()==null?"":user.getIpaddr();
			LoginMonitor loginMonitor = new LoginMonitor();
			loginMonitor.setIp(ip);
			LoginMonitorDao loginMonitorDao=(LoginMonitorDao)this.getObjectFromApplication(arg0.getSession().getServletContext(), "loginMonitorDao");
			loginMonitorDao.delete(loginMonitor);
		}
	}

}
