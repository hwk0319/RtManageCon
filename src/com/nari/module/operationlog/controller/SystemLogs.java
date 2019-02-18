package com.nari.module.operationlog.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.po.SystemLogPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@Controller
@RequestMapping(value="systemlogs")
public class SystemLogs {
	
	private Logger logger = Logger.getLogger(SystemLogs.class);
	
	@Resource(name="operationlogService")
	private OperationlogService service;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	
	private static Connection conn = null;

	/**
     *
     * @param hostname
     * @return
     */
    private Connection getConnection(String hostname, int port) {
        if (conn == null) {
            conn = new Connection(hostname, port);
        }
        return conn;
    }

    public List<String> exec(String hostname,String username,String mm,int port,String shell){
    	List<String> logs = new ArrayList<>();
    	Connection conn = null;
    	Session sess = null;
    	InputStream is = null;
    	InputStreamReader isr = null;
    	BufferedReader br = null;
    	try {
	    	conn = getConnection(hostname, port);
	    	conn.connect();
	    	boolean isAuthenticated = conn.authenticateWithPassword(username, mm);
            if (isAuthenticated == false) throw new IOException("Authentication failed.");
            sess = conn.openSession();
            sess.execCommand(shell);
            is = new StreamGobbler(sess.getStdout());
            isr = new InputStreamReader(is,"UTF-8");
            br = new BufferedReader(isr);
            String line = null;
            while (true) {
               line = br.readLine();
               line = Util.xssEncode(line);//过滤script脚本，预防xss攻击
               logs.add(line);
               if (line == null){
                	break;
               }
            }
        } catch (Exception e) {
        	logger.error(e);
        }finally{
        	try {
        		if(conn!=null)conn.close();
        		if(is!=null)is.close();
        		if(isr!=null)isr.close();
        		if(br!=null)br.close();
        		if(sess!=null)sess.close();
			} catch (IOException e2) {
				logger.error(e2);
			}
        }
    	return logs;
    }
    
    /**
     * 系统日志获取方法
     * @param request
     * @return
     * @throws Exception 
     */
    @ResponseBody
	@RequestMapping(value="/logs")
	public  List<String> logs(SystemLogPo po, HttpServletRequest request) throws Exception{
    	List<String> logs = new ArrayList<String>();
    	
    	//读取log4j.xml文件 判断是否要输出日志
    	String res = this.dom4jXml();
    	if("".equals(res)){
    		return logs;
    	}
    	
    	String shell;
    	String date = po.getDate() == null ? "" : po.getDate().trim();
    	String systemLogType = po.getSystemLogType() == null ? "" : po.getSystemLogType();
    	
    	RsaDecryptTool rsaTool = new RsaDecryptTool();
    	Properties prop = this.getProperties(request);
		String hostip = prop.getProperty("hostip").trim();
		String hostname = rsaTool.decrypt(prop.getProperty("hostname").trim());
		String mm = rsaTool.decrypt(prop.getProperty("mm").trim());
		int port = Integer.parseInt(prop.getProperty("port").trim());
		String inOrOut = Util.getInOrOut(request);
        if(!"".equals(date)){
        	 //判断输入日志是否为当前日期
        	 Date d = new Date();
        	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	 String newDate = df.format(d);
        	if(date.equals(newDate)){
        		 shell = "tail -n 1000 /usr/software/apache-tomcat-8.0.53/webapps/RtManageCon/logs/dailyRollingFile.log";
        	 }else{
        		 shell = "tail -n 1000 /usr/software/apache-tomcat-8.0.53/webapps/RtManageCon/logs/dailyRollingFile.log."+date+".log";
        	 }
         }else{
        	 shell = "tail -n 1000 /usr/software/apache-tomcat-8.0.53/webapps/RtManageCon/logs/dailyRollingFile.log";
         }
         logs = this.exec(hostip, hostname, mm, port, shell);

		 List<String> logsType = new ArrayList<String>();
		 //判断是否根据类型查询
		 if(systemLogType != null && !"".equals(systemLogType)){
			 
			 for (int i = 0; i < logs.size(); i++) {
				 String str = logs.get(i);
				 if(str != null && !"".equals(str)){
					 if(str.trim().contains(systemLogType+"]") || str.trim().contains(systemLogType+" ]")){
						 logsType.add(str);
					 }
				 }
			}
		 }else{
			 //提取关键日志信息
			 for (int i = logs.size()-1; i >= 0; i--) {
				 String str = logs.get(i);
				 if(str != null && !"".equals(str)){
					 if(str.contains("ERROR")){
						 logsType.add(str);
					 }else if(str.contains("DEBUG")){
						 logsType.add(str);
					 }else if(str.contains("WARN")){
						 logsType.add(str);
					 }else if(str.contains("INFO")){
						 logsType.add(str);
					 }
				 }
			}
			//判断是否需要添加审计数据
			User userInfo = (User) request.getSession().getAttribute("user");
			boolean result = isAudit("系统日志","查询");
			if(result){
				generservice.insert_oprt_logs("日志分析-系统日志","查询",userInfo.getUsername(),"用户查询系统日志数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
			}
		 }
		 return logsType;
	}
    
    /**
     * 解析log4j.xml文件
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
	public String dom4jXml(){
         // 创建SAXReader的对象reader
         SAXReader reader = new SAXReader();
         try {
        	 String path = System.getProperty("webapp.root") + "WEB-INF"+File.separator+"log4j.xml";
        	 if(path.contains("..")){
        		 throw new IllegalArgumentException();
        	 }
             // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
             Document document = reader.read(new File(path));
             // 通过document对象获取根节点bookstore
             Element bookStore = document.getRootElement();
             // 通过element对象的elementIterator方法获取迭代器
			Iterator it = bookStore.elementIterator();
             // 遍历迭代器，获取根节点中的信息
             while (it.hasNext()) {
				Element book = (Element) it.next();
				//获取root下面的内容
				String name = book.getName();
				if(!"".equals(name) && name.equals("root")){
					// 解析子节点的信息
					Iterator itt = book.elementIterator();
					while (itt.hasNext()) {
						Element bookChild = (Element) itt.next();
						String refValue = bookChild.attributeValue("ref");
						//判断是否配置了在控制台输出日志
						if(!"".equals(refValue) && "console".equals(refValue)){
							return "console";
						}
					}
				}
             }
         } catch (DocumentException e) {
        	logger.error(e);
        }
    	return "";
    }
    
    /**
	 * 判断是否需要审计
	 * @param po
	 * @return true 审计，false 不审计
	 */
	@ResponseBody
	@RequestMapping(value="/isAudit")
	public boolean isAudit(String erjiName, String methodName){
		boolean res = false;
		AuditPo po = new AuditPo();
		po.setErjiname(erjiName);
		List<AuditPo> list = service.searchAuditByErjiname(po);
		if(list != null && list.size() > 0){
			String audit = list.get(0).getIsAudit();
			if("1".equals(audit)){
				String auditthose = list.get(0).getAuditThose();
				if(auditthose.contains(methodName)){
					res = true;
				}
			}
		}
		return res;
	}
    
	/**
	 * 读取systemLog.properties配置文件信息
	 * @param req
	 * @return
	 * @throws IOException
	 */
	private Properties getProperties(HttpServletRequest req) throws IOException {  
	   	 Properties prop = new Properties();  
	  	 String path = req.getSession().getServletContext().getRealPath("/")+"WEB-INF/classes/config/systemLog.properties";
	   	 try(InputStream in = new BufferedInputStream (new FileInputStream(path));  ) {
	   		 prop.load(in);  
	   	 } catch (IOException e) {  
	   		 throw e;
	   	 }  
	   	 return prop;
	}

}
