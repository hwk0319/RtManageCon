package com.nari.taskmanager.util;



import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.config.TaskConfig.PropertiesContext;

public class ParseUtil {
	
	
	private static Logger logger = Logger.getLogger(ParseUtil.class); 
	
	
	public static void parseInitConfig(Properties prop) throws Exception
	{
		Map<String,String> map = propToMap(prop);
		PropertiesContext.localContext = map;
		logger.info("zk connect str   :"+map.get(TaskConfig.KEY_ZKCONNECT_STR));
		logger.info("shell response IP:"+map.get(TaskConfig.KEY_SHELL_RESPONSE_IP));
		logger.info("local IP         :"+map.get(TaskConfig.KEY_RETURN_HOST_IP));
	}
	
	private static Map<String,String> propToMap(Properties prop) throws Exception
	{
		Map<String,String> map = new HashMap<String,String>();
		for(Entry<Object, Object> enrty : prop.entrySet())
		{
			Object KeyObject = enrty.getKey();
			if(null == KeyObject)
			{
				logger.error("parse poperties file error,null key,file path:"+TaskConfig.TASK_ROOT_PATH+"WEB-INF/taskConfig/server.properties");
				throw new Exception("null key!");
			}
			String key = (String)KeyObject;
			Object valueObj = enrty.getValue();
			if(null ==valueObj)
			{
				logger.error("parse poperties file error,null value,file path:"+TaskConfig.TASK_ROOT_PATH+"WEB-INF/taskConfig/server.properties");
				throw new Exception("null value!");
			}
			String value = (String)valueObj;
			map.put(key, value);
		}
		return map;
	}

}
