package com.nari.taskmanager.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.dao.TaskOperationDao;
import com.nari.taskmanager.dao.ZKTaskDao;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.util.TaskFileUtil;
import com.nari.taskmanager.watcher.ZKClient;
import com.nari.taskmanager.watcher.ZKTaskWatcher;

@Service
public class ZKTaskManageService {
	
	@Autowired
	private ZKTaskDao zkTaskDao;
	
	@Autowired
	private TaskOperationDao taskOperationDao;
	
	@Resource(name="zkTaskWatcher")
	private ZKTaskWatcher zKTaskWatcher;
	
	@Autowired
	private TaskManageService taskManageService;
	
	private static Logger logger = Logger.getLogger(ZKTaskManageService.class);
	
	public void startZKWatcher(){
		zKTaskWatcher.startWatcher();
	}
	
	
	public JSONObject startTaskByTaskIdAndUUID(int taskId,String UUID) throws  IOException
	{
		logger.info("start zk task,taskId:"+taskId+" uuid:"+UUID);
		TaskOperation taskOperation = null;
		try {
			taskOperation = taskManageService.cloneTask(taskId,TaskConfig.TASK_STRATEGY_ALL_PARALLEL);
		} catch (Exception e1) {
			logger.error(" start ZK Task ERROR, taskID"+taskId+" uuid:"+UUID+ " msg:" + e1.getMessage());
			JSONObject  obj = new JSONObject();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, e1.getMessage());
			return obj;
		}
		logger.info("got clonetask for ZK newtaskId:"+taskOperation.getId()+" name:"+taskOperation.getName());
		taskOperation.setType(TaskConfig.TASK_TYPE_ZK);
		taskOperationDao.updateTaskOperation(taskOperation);
		Map<String,String> params = createParamFromTaskIdAndUUID(taskOperation,UUID);
		try {
			TaskFileUtil.appendParamToFile(params, taskOperation);
		} catch ( IOException e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		JSONObject  obj = taskManageService.startTask(taskOperation,false);
		return obj;
	}
	
	
	private Map<String,String> createParamFromTaskIdAndUUID(TaskOperation taskOperation,String uid)
	{
		String longSql = taskOperation.getSql();
		String sqls[] = longSql.split(";");
		Map<String,String> params = new HashMap<String,String>();
		for(String sql : sqls)
		{
			sql = sql.replace(TaskConfig.TASK_ZK_UID_FIAG, "'"+uid+"'").trim();
			List<Map<String,String>> paraMaps = new ArrayList<Map<String,String>>();
			if(uid.startsWith("1")){
				paraMaps = zkTaskDao.getDeviceParamByUid(sql);
				
			}
			else if(uid.startsWith("2"))
			{
				paraMaps=zkTaskDao.getSystemParamByUid(sql);
			}
			else
			{
				
			}
			
			for(Map<String,String> map : paraMaps) 
			{
				for(Entry<String,String> entry: map.entrySet()) 
				{
					String key = entry.getKey();
					String value= entry.getValue();
					if(null == params.get(key))
					{
						params.put(key, value);
					}
					else
					{
						params.put(key,params.get(key)+"|"+value);
					}
				}
			}
		}
		return params;
	}
	
	//判断主节点是否启动
	public static boolean isMasterNodeStartup() throws Exception{
		ZooKeeper zk;
		Stat stat;
		try {
			zk = ZKClient.getZKClient();
			stat = zk.exists(TaskConfig.TASK_ZK_WEB_FLAG, true);
		} catch (IOException | KeeperException | InterruptedException e) {
			logger.error("can not get is the master node startup!");
			throw new Exception("zookeeper connect exception!");
		}
		if(null == stat){
			return false;
		}
		return true;
	}
	
	//主节点启动时设置一个标志。
	public static void createMaterStartupFlagNode() throws Exception {
		ZooKeeper zk;
		try {
			zk = ZKClient.getZKClient();
			String path = zk.create(TaskConfig.TASK_ZK_WEB_FLAG, "	".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			logger.info("zookeeper create path success,path:"+path);
		} catch (IOException | KeeperException | InterruptedException e) {
//			logger.error(e.getMessage());
			logger.error("KeeperErrorCode = ConnectionLoss for "+TaskConfig.TASK_ZK_WEB_FLAG);
			logger.error("create master startup flag error,cannot create the node:"+TaskConfig.TASK_ZK_WEB_FLAG);
			throw new Exception("zookeeper node create error!");
		}
	}
	
	public static void createPersistentNode(String path) throws Exception {
		ZooKeeper zk;
		try {
			zk = ZKClient.getZKClient();
			if(null == zk.exists(path, true))
			zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (IOException | KeeperException | InterruptedException e) {
			logger.error("create master startup flag error,cannot create the node:"+TaskConfig.TASK_ZK_WEB_FLAG);
			throw new Exception("zookeeper node create error!");
		}
	}

}
