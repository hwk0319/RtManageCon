package com.nari.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.util.DefaultPropertiesPersister;

import com.nari.utils.RsaDecryptTool;
public class MyPropertiesPersist extends DefaultPropertiesPersister {
	
	private Logger logger = Logger.getLogger(MyPropertiesPersist.class);
    
	public final String dbKeyStr = "NariRtManageCon";
	/**
	 * 使用什么数据库
	 */
	public static String DBTYPE="ORACLE";
	public static String MENUURL="jsp/pages/monitormgt/monihome/monihome.jsp";//定义进入首页
    public void load(Properties props, InputStream is) throws IOException{
    	RsaDecryptTool rsaTool = new RsaDecryptTool();
	    Properties properties = new Properties(); 
	    properties.load(is); 	    
	    DBTYPE=properties.get("DBTYPE").toString();
	    if ( properties.get("jdbc.mm") != null && properties.get("jdbc.mm").toString().length() > 100){
	        //这里通过解密算法，得到你的真实密码，然后写入到properties中	    	 
	    	 StringBuffer mm = new StringBuffer(properties.getProperty("jdbc.mm"));
	    	try {
	    		String mm1 = rsaTool.decrypt(mm.toString());
	    		mm = new StringBuffer(mm1.trim());
			} catch (Exception e) {
				logger.error(e);
			}
        	 properties.setProperty("jdbc.mm" , mm.toString());  
	    }
	    if ( properties.get("jdbc.username") != null && properties.get("jdbc.username").toString().length() > 100){
	    	 StringBuffer username = new StringBuffer(properties.getProperty("jdbc.username"));
	    	try {
	    		String name = rsaTool.decrypt(username.toString());
	    		username = new StringBuffer(name.trim());
			} catch (Exception e) {
				logger.error(e);
			}
        	 properties.setProperty("jdbc.username" , username.toString());  
	    }
	    OutputStream outputStream = null; 
	    try {
	        outputStream = new ByteArrayOutputStream(); 
	        properties.store(outputStream, ""); 
	        is = outStream2InputStream(outputStream); 
	        super.load(props, is);
	    }catch(IOException e) { 
	        throw e;
	    }finally {
	    	if(outputStream!=null){
	    		try{
	    			 outputStream.close();
	    		}catch(Exception e){
	    		}
	    	}
	    	if(is!=null){
	    		try{
	    			 is.close();
	    		}catch(Exception e){
	    		}
	    	}
	    }
    }
    
    private InputStream outStream2InputStream(OutputStream out){
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        bos = (ByteArrayOutputStream) out ;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(bos.toByteArray()); 
        return swapStream;
    }
    
}