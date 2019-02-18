package com.nari.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.nari.taskmanager.config.TaskConfig;

public class startupCall implements ApplicationListener<ContextRefreshedEvent> {
	private static Logger logger = Logger.getLogger(startupCall.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		initZkNode();
	}

	/**
	 * 启动创建zk相关节点
	 */
	public void initZkNode(){
		ZooKeeper zk = zkClinet();
		List<Op> ops = new ArrayList<Op>();
		try {
			if(null == zk.exists("/RT", false)){
				zk.create("/RT","".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			if(null == zk.exists(ZkClientUtils.ID_PATH, false))
			{
				ops.add(Op.create(ZkClientUtils.ID_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			if(null == zk.exists("/RT/Id", false))
			{
				ops.add(Op.create("/RT/Id", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			if(null == zk.exists(ZkClientUtils.Col_PATH, false))
			{
				ops.add(Op.create(ZkClientUtils.Col_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			if(null == zk.exists(ZkClientUtils.HEALTH_PATH, false))
			{
				ops.add(Op.create(ZkClientUtils.HEALTH_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			if(null == zk.exists(ZkClientUtils.PERF_PATH, false))
			{
				ops.add(Op.create(ZkClientUtils.PERF_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
			}
			zk.multi(ops);
			logger.info("--------system init zk node seccuss!!!!");
		} catch (Exception e) {
			logger.error("未能连接到zk----------------------");
		}finally{
			ZkClientUtils.closeZk(zk);
		}
	}
	/**
	 * 创建zkClient
	 * @return
	 */
	public ZooKeeper zkClinet() {
		ZooKeeper zkClinet;
		try {
			String CONN_STRING=TaskConfig.PropertiesContext.localContext.get(TaskConfig.KEY_ZKCONNECT_STR);
			zkClinet = new ZooKeeper(CONN_STRING,ZkClientUtils.SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {}
			});
		} catch (IOException e) {
			logger.error("连接创建失败，发生 IOException---------------");
			logger.error(e);
			return null;
		}
		return zkClinet;
	}
}
