package com.nari.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.nari.taskmanager.config.TaskConfig;

/**
 * zk客户端工具类
 * @author 
 */
public class ZkClientUtils {
	
	private static Logger logger = Logger.getLogger(ZkClientUtils.class);
	
	public static final int SESSION_TIMEOUT = 5000;
	public static final String ID_PATH = "/RT/Ids";
	public static final String PATHS = "/RT/Id";
	public static final String Col_PATH = "/RT/Collector";
	public static final String HEALTH_PATH = "/RT/Health";
	public static final String PERF_PATH = "/RT/Perf";

	/**
	 * 创建zkClient
	 * @return
	 */
	public static ZooKeeper zkClinet() {
		ZooKeeper zkClinet;
		try {
			String CONN_STRING=TaskConfig.PropertiesContext.localContext.get(TaskConfig.KEY_ZKCONNECT_STR);
			zkClinet = new ZooKeeper(CONN_STRING, SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
				}
			});
		} catch (IOException e) {
			logger.error("连接创建失败，发生 IOException",e);
			return null;
		}
		return zkClinet;
	}
	/**
	 * 关闭zkClient
	 * @param zk
	 */
	public static void closeZk(ZooKeeper zk) {
		try {
			zk.close();
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}
	/**
	 * 获取节点数据
	 * @param path
	 * @param zk
	 * @return
	 */
	public static String getData(String path, ZooKeeper zk) {
		String data;
		try {
			data = new String(zk.getData(path, false, null));
		} catch (KeeperException | InterruptedException e) {
			logger.error(e);
			return null;
		}
		return data;
	}
	/**
	 * 获取子节点
	 * @param path
	 * @param zk
	 * @return
	 */
	public static List<String> getChildren(String path, ZooKeeper zk) {
		List<String> children = new ArrayList<String>();
		try {
			children = zk.getChildren(path, false);
		} catch (KeeperException | InterruptedException e) {
			logger.error(e);
			return null;
		}
		return children;
	}
	/**
	 * 如果Ids和Collector为空，则新建Ids和Collector节点；初始化设备uid节点，包含节点创建和初始化数据
	 * @param uid
	 * @return
	 */
	public static void initNode(String uid, String conn, String pros, String colType,String oldType){
		ZooKeeper zk =zkClinet();
		List<Op> ops = new ArrayList<Op>();
		try{
			if(null == zk.exists(ID_PATH, false))
			{
				ops.add(Op.create(ID_PATH,"".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			if(null == zk.exists(Col_PATH, false))
			{
				ops.add(Op.create(Col_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			if(null == zk.exists(PATHS, false))
			{
				ops.add(Op.create(PATHS, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			String newType="";
			try {
				newType=pros.split("&")[0].split("=")[1];
			} catch (Exception e) {
				logger.error(e);
			}
			String A=PATHS+"/"+uid;
			if(null == zk.exists(A, false))
			{
				ops.add(Op.create(PATHS+"/"+uid,"".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				
				ops.add(Op.create(PATHS+"/"+uid+"/connection", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				ops.add(Op.setData(PATHS+"/"+uid+"/connection", conn.getBytes(), -1));
				
				ops.add(Op.create(PATHS+"/"+uid+"/pros", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				ops.add(Op.setData(PATHS+"/"+uid+"/pros", (pros+";").getBytes(), -1));
				
				ops.add(Op.create(PATHS+"/"+uid+"/results", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				ops.add(Op.create(PATHS+"/"+uid+"/results/"+newType, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				
				String ids = getData(ID_PATH, zk);
				ops.add(Op.setData(ID_PATH, (ids+uid+";").getBytes(), -1));
			}else{
				String pro =getData(PATHS+"/"+uid+"/pros", zk);
				ops.add(Op.setData(PATHS+"/"+uid+"/pros", (pro+pros+";").getBytes(), -1));
				
				if(null == zk.exists(PATHS+"/"+uid+"/results/"+newType, false)){
					ops.add(Op.create(PATHS+"/"+uid+"/results/"+newType, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				}
			}
			zk.multi(ops);
		} catch (Exception e) {
			logger.error(e);
		} 
		finally{
			closeZk(zk);
		}
	}
	/**
	 * 根据uid删除节点,如果该uid只有一个指标分类，则此次删除完全删除该uid节点；如果uid有多个指标分类，本次删除只是删除该指标分类
	 * @param uid
	 * @return
	 */
	public static void dropNode(String uid,String cron)
	{
		logger.info(">>>start dropNode!!!");
		ZooKeeper zk = zkClinet();
		List<Op> ops = new ArrayList<Op>();
		String pros = "";
		try {
			if(null != zk.exists(PATHS+"/"+uid+"/pros", false))
			{
				pros = getData(PATHS+"/"+uid+"/pros", zk);
			}
			List<String> prosl=new ArrayList<String>(Arrays.asList(pros.split(";")));
			logger.info("prosl="+prosl);
			if(prosl.size() <= 1)
			{
				String ids = getData(ID_PATH, zk);
				List<String> idl=new ArrayList<String>(Arrays.asList(ids.split(";")));
				idl.remove(uid);
				String new_ids = "";
				for(String id : idl)
				{
					new_ids = new_ids+id+";";
				}	
				ops.add(Op.setData(ID_PATH, new_ids.getBytes(), -1));
				logger.info("path="+PATHS+"/"+uid);
				List<String> children1 = getChildren(PATHS+"/"+uid, zk);
				logger.info(children1);
				if(children1 != null && children1.size()>0){
					for(String c1 : children1)
					{
						List<String> children2 = getChildren(PATHS+"/"+uid+"/"+c1, zk);
						if(children2.size() > 0)
						{
							for(String c2 : children2)
							{
								ops.add(Op.delete(PATHS+"/"+uid+"/"+c1+"/"+c2, -1));
							}
						}
						ops.add(Op.delete(PATHS+"/"+uid+"/"+c1, -1));
					}
				}
				ops.add(Op.delete(PATHS+"/"+uid, -1));
			}else{
				prosl.remove(cron);
				String new_pros = "";
				for(String pro : prosl)
				{
					new_pros = new_pros+pro+";";
				}
				logger.info("new_pros="+new_pros);
				ops.add(Op.setData(PATHS+"/"+uid+"/pros", new_pros.getBytes(), -1));
				try {
					String Type=cron.split("&")[0].split("=")[1];
					ops.add(Op.delete(PATHS+"/"+uid+"/results/"+Type, -1));
				} catch (Exception e) {
					logger.error(e);
				}
			}
			zk.multi(ops);
			closeZk(zk);
		} catch (Exception e) {
			logger.error(e);
		} 
	}
	
	/**
	 * device或者system数据更新，同步更新zk中的connection参数
	 * @param uid
	 * @return
	 */
	public static void updateConn(String uid,String conn)
	{
		ZooKeeper zk = zkClinet();
		try{
			if(null != zk.exists(PATHS+"/"+uid+"/connection", false))
			{
				zk.setData(PATHS+"/"+uid+"/connection", conn.getBytes(), -1);
			}
		}catch(Exception e){
			logger.error(e);
		}
		closeZk(zk);
	}
	
	/**
	 * 创建节点
	 * @param id
	 */
	public static String createNode(int id){
		ZooKeeper zk = zkClinet();
		String nodePath ="";
		List<Op> ops = new ArrayList<Op>();
		try {
			if(null == zk.exists(HEALTH_PATH, false))
			{
				ops.add(Op.create(HEALTH_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				zk.multi(ops);
			}
			nodePath = zk.create("/RT/Health/"+id, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (Exception e) {
			logger.error("***** 节点创建失败！*****",e);
		}
		closeZk(zk);
		return nodePath;
	}
	
	/**
	 * 删除节点
	 * @param id
	 */
	public static void dropNode(int id){
		ZooKeeper zk = zkClinet();
		try {
			String path = "/RT/Health/"+id;
			if (null != zk.exists(path, false)){
				zk.delete(path, -1);
			}
		}catch (Exception e) {
			logger.error("***** 节点删除失败！*****",e);
		}
	}
	public static void clean() {
		ZooKeeper zk = zkClinet();
		try {
			zk.setData("/RT/Ids", "".getBytes(), -1);
			
			String path = "/RT/Id";
			List<String> c = zk.getChildren(path, false);

			for (String p : c) {
				String p1 = path + "/" + p;
				List<String> gcs = zk.getChildren(p1, false);
				for (String gc : gcs) {
					String p2 = p1 + "/" + gc;
					List<String> dcs = zk.getChildren(p2, false);
					for (String dc : dcs) {
						String p3 = p2 + "/" + dc;
						zk.setData(p3, "".getBytes(), -1);
						zk.delete(p3, -1);
					}
					zk.setData(p2, "".getBytes(), -1);
					zk.delete(p2, -1);
				}
				zk.setData(p1, "".getBytes(), -1);
				zk.delete(p1, -1);
			}

		} catch (KeeperException | InterruptedException e) {
			logger.error(e);
		} finally {
			closeZk(zk);
		}
	}
	
	/**
	 * 创建性能评分节点
	 * @param id
	 */
	public static String createPerfNode(int id){
		ZooKeeper zk = zkClinet();
		String nodePath ="";
		List<Op> ops = new ArrayList<Op>();
		try {
			if(null == zk.exists(PERF_PATH, false))
			{
				ops.add(Op.create(PERF_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
				zk.multi(ops);
			}
			nodePath = zk.create(PERF_PATH+"/"+ id, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (Exception e) {
			logger.error("***** 节点创建失败！*****",e);
		}
		closeZk(zk);
		return nodePath;
	}
	
	/**
	 * 删除性能评分节点
	 * @param id
	 */
	public static void dropPerfNode(int id){
		ZooKeeper zk = zkClinet();
		try {
			String path = PERF_PATH+"/"+id;
			if (null != zk.exists(path, false)){
				zk.delete(path, -1);
			}
		}catch (Exception e) {
			logger.error("***** 节点创建失败！*****",e);
		}
	}
	
}
