package com.nari.taskmanager.watcher;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.nari.taskmanager.config.TaskConfig;

public class ZKClient {
	static ZooKeeper zk;
	static Logger logger = Logger.getLogger(ZKClient.class);
	public static ZooKeeper getZKClient() throws IOException{
		if(null == zk){
			try {
				zk = new ZooKeeper(TaskConfig.getPropertiesContext().get(TaskConfig.KEY_ZKCONNECT_STR), TaskConfig.TASK_ZK_WATCH_TIMEOUT, new Watcher(){
					@Override
					public void process(WatchedEvent event) {
					}
				});
			} catch (IOException e) {
				logger.error("creat zk client error. zkconnect:"+TaskConfig.getPropertiesContext().get(TaskConfig.KEY_ZKCONNECT_STR));
				throw e;
			}
		}
		return zk;
	}
}
