package com.nari.taskmanager.watcher;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.quartz.TaskManagerSchedule;
import com.nari.taskmanager.service.TaskManageService;
import com.nari.taskmanager.service.ZKTaskManageService;

public class ZKTaskWatcher {
	
	@Autowired
	private ZKTaskManageService zkTaskManageService;
	
	@Autowired
	private TaskManageService taskManageService;
	
	protected ZooKeeper zk1 = null;
	protected ZooKeeper zk2 = null;
	
	private static Logger logger = Logger.getLogger(ZKTaskWatcher.class); 
	
	public void startWatcher()
	{
		logger.info(">>>>>>>>start zookeeper watcehr.");
		createNode();
		 watchNode();
		 watchStartFlagNode();
	}
	
	private void createNode() {
		try {
			ZKTaskManageService.createPersistentNode(TaskConfig.TASK_ZK_WATCH_PATH);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 持久化watcher
	 */
	private void watchNode() {
	
		try {
			if (null == zk1) {
				logger.info("create zk task watcher");
				zk1 = new ZooKeeper(TaskConfig.getPropertiesContext().get(TaskConfig.KEY_ZKCONNECT_STR), TaskConfig.TASK_ZK_WATCH_TIMEOUT, new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						 if (event.getState() == KeeperState.SyncConnected){
							 switch (event.getType()) {
							 case NodeDataChanged:
								 watchNode();
								 break;
							 case None:
								 break;
							 case NodeDeleted:
								 watchNode();
								 break;
							 case NodeChildrenChanged:
								 dataChangeProc();
								 watchNode();
								 break;
							 default:
								 watchNode();
								 break;
							 }
						 }
						 else
						 {
							 logger.error("zk disconnect from server.");
						 }
					}

				});
			}
			zk1.getChildren(TaskConfig.TASK_ZK_WATCH_PATH, true);
		} catch (IOException| KeeperException | InterruptedException e) {
			logger.error(e);
		}
	}
	
	
	private void watchStartFlagNode() {
		try {
			if (null == zk2) {
				logger.info("create zk task watcher");
				zk2 = new ZooKeeper(TaskConfig.getPropertiesContext().get(TaskConfig.KEY_ZKCONNECT_STR), TaskConfig.TASK_ZK_WATCH_TIMEOUT, new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						 if (event.getState() == KeeperState.SyncConnected){
							 switch (event.getType()) {
							 case None:
								 break;
							 case NodeDeleted:
								 loadTaskFromDB();
								 watchStartFlagNode();
								 return;
							 default:
								 watchStartFlagNode();
								 break;
							 }
						 }
						 else
						 {
							 logger.error("zk disconnect from server.");
							 try {
								Thread.sleep(60000);
							} catch (InterruptedException e) {
							}
						 }
					}

				});
			}
			zk2.exists(TaskConfig.TASK_ZK_WEB_FLAG, true);
		} catch (IOException| KeeperException | InterruptedException e) {
			logger.error(e);
		}
	}
	
	private void deletePath(String path) throws InterruptedException, KeeperException{
		logger.info("delete zk path: "+path);
		zk1.delete(path, -1);
	}
	
	private  void dataChangeProc(){
		try {
			List<String> zPaths = zk1.getChildren(TaskConfig.TASK_ZK_WATCH_PATH,false);
			String logStr = "";
			for(String str :zPaths)
			{
				logStr += str+" ";
			}
			logger.info("zk monitor data changed:"+logStr);
			for(String taskStrs : zPaths)
			{
				deletePath(TaskConfig.TASK_ZK_WATCH_PATH+"/"+taskStrs);
				String[] taskStr = taskStrs.split(",");
				int taskId = Integer.valueOf(taskStr[0]);
				String uid = taskStr[1];
				zkTaskManageService.startTaskByTaskIdAndUUID(taskId, uid);
			}
		} catch (KeeperException | InterruptedException  e) {
			logger.error("get data from zk error!");
			logger.error(e.getMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	//主备切换后备库从数据库加载任务。
	private void loadTaskFromDB() {
		//节点启动在zk上设置一个启动标志
		try {
			ZKTaskManageService.createMaterStartupFlagNode();
			logger.info("create startup flag succcess.");
		} catch (Exception e1) {
			logger.error(e1);
		}
		// 从数据库加载任务。
		logger.info("load exist task from db.");
		List<TaskOperation> tasks = taskManageService.getTaskwithStepAndParam(new TaskOperation());
		for (TaskOperation operation : tasks) {
			// 如果启动时从数据库取得的状态为running，说明之前正在执行，那么继续执行。
			if (TaskConfig.TASK_STATE_RUNNING == operation.getState()) {
				logger.info("continue running task id:" + operation.getId() + " name:" + operation.getName());
				taskManageService.continueTask(operation);
			}
			// 如果启动时从数据库取得的状态为STOPPING，说明之前已经手动触发停止，但是没有正常关闭，所以设置任务的状态为STOPPED,防止任务自动启动。
			else if (TaskConfig.TASK_STATE_STOPPINF == operation.getState()) {
				operation.setState(TaskConfig.TASK_STATE_STOPPED);
			} else {

			}
			taskManageService.taskOperationDao.updateTaskOperation(operation);
			// 如果任务为定时任务，在系统启动时，启动任务。
			if (TaskConfig.TASK_TYPE_SCHEDULE == operation.getType()
					&& TaskConfig.TASK_STATE_CREATING != operation.getState()
					&& TaskConfig.TASK_STATE_STOPPED != operation.getState()) {
				// OperationExector operationExector = new
				// OperationExector(operation);
				try {
					TaskManagerSchedule.addTaskJob(operation);
				} catch (SchedulerException e) {
					logger.error("add schedul task error onstart!");
				}
			}
		}
		logger.info("load exist task from db finesh.");
	}
}
