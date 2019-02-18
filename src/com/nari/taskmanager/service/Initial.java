package com.nari.taskmanager.service;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.nari.module.common.service.CommonService;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.module.warnlog.service.WarnlogService;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.quartz.LogWarnJob;
import com.nari.taskmanager.quartz.TaskManagerSchedule;
import com.nari.taskmanager.util.ParseUtil;
import com.nari.taskmanager.util.TaskFileUtil;

@Service
public class Initial implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired  
	private TaskManageService taskManageService;
	@Resource(name="commonService")
	private CommonService service;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	@Resource(name="WarnlogService")
	private WarnlogService warnService;
	
	private static Logger logger = Logger.getLogger(Initial.class);

	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (event.getApplicationContext().getParent() == null) {
			SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) event.getApplicationContext().getBean("sqlSessionFactory");
			sqlSessionFactory.getConfiguration().setJdbcTypeForNull(org.apache.ibatis.type.JdbcType.NULL);
			sqlSessionFactory.getConfiguration().setLogImpl(org.apache.ibatis.logging.log4j.Log4jImpl.class);
			logger.info(">>>>>>>>init system context.");
			try {
				// 加载初始化配置文件
				//loadConfig();//改为懒加载
				// 配置启动配置文件
				configService();
				// 启动后手动注入
				injectMyDao();
				// 添加任务。
				loadSystemTask();
				loadExistTask();
				
			} catch (Exception e) {
				logger.error(">>>>>>>>init system ERROR.", e);
				System.exit(1);
			}
			// logger.info(">>>>>>>>start zookeeper watcehr.");
			// zKTaskManagerService.startZKWatcher();
			logger.info(">>>>>>>>init system context success,system start OK.");
		}
	}

private void loadSystemTask() throws SchedulerException {
		TaskManagerSchedule.loadSystemTask();
	}

	/*	private void injectMyDao() {
		logger.info("inject dao service.");
		OperationExector.setTaskOperationDao(taskOperationDao);
		OperationExector.setTaskStepDao(taskStepDao);
		OperationExector.setTaskRunningConfigService(taskRunningConfigService);
		OperationExector.setTaskManageService(taskManageService);
		StateManageService.setTaskOperationDao(taskManageService.taskOperationDao);
		StateManageService.setTaskStepDao(taskManageService.taskStepDao);
		TaskOperationJob.setTaskManageService(taskManageService);
		logger.info("inject dao service success.");
	}*/
	private void injectMyDao() {
		logger.info("inject dao service.");
		LogWarnJob.operService = operService;
		LogWarnJob.service = service;
		LogWarnJob.warnService = warnService;
		logger.info("inject dao service success.");
	}

	/**
	 * 启动时加载配置文件。
	 * 
	 * @throws Exception
	 */
	private void loadConfig() throws Exception {
		/*
		 * String configFilePath =
		 * TaskConfig.TASK_ROOT_PATH+File.separator+"WEB-INF"+File.separator+
		 * "monitor.xml"; logger.info("load config file. "+configFilePath);
		 * Document doc = TaskFileUtil.loadXmlFile(configFilePath); try {
		 * ParseUtil.parseInitConfig(doc); } catch (Exception e) { logger.error(
		 * "parse init config file error filePath:"+configFilePath);
		 * logger.error(e.getMessage(),e); throw e; } logger.info(
		 * "load config file success.");
		 */

		//String configFilePath = TaskConfig.systemConfig;
		logger.info("load config file. " + TaskConfig.returnConfig);
		Properties prop = TaskFileUtil.LoadPropFile(TaskConfig.returnConfig);
		ParseUtil.parseInitConfig(prop);
		logger.info("load config file success.");
	}

	/**
	 * 配置shell response ip </br>
	 * 根据加载的配置参数，配置shell脚本的response IP的配置文件。</br>
	 * 
	 * @throws Exception
	 */
	private static void configService() throws Exception {
		logger.info("config service on start.");
		String hostServerAddr = "hostServerAddr=" + TaskConfig.getPropertiesContext().get(TaskConfig.KEY_SHELL_RESPONSE_IP);
		TaskFileUtil.savefile(TaskConfig.SHELL_RESPONSE_SERVER_CONFIG_FILE, hostServerAddr);
		logger.info("config service on start success.");
	}

	/**
	 * 载入数据库中保存的任务。</br>
	 * 1，如果任务状态为running ， 那么继续执行</br>
	 * 2,如果任务正状态为stopping，设置任务状态为stopped。</br>
	 * 3,如果任务状态为stopped，success，不做处理。</br>
	 * 4，定时任务，
	 * 
	 * 
	 */
	private void loadExistTask() {
		//如果主节点已经启动那么备节点不在启动。
		boolean isMasterStaerup = false;
		try {
			isMasterStaerup = ZKTaskManageService.isMasterNodeStartup();
		} catch (Exception e1) {
			logger.error("can not get is the master node startup, so load exit task form db.");
			logger.error(e1);
		}
		if(isMasterStaerup){
			logger.info("the master node is already startup, the backup dont need load task from db.");
			return;
		}
		//节点启动在zk上设置一个启动标志
		try {
			ZKTaskManageService.createMaterStartupFlagNode();
			logger.info("create startup flag succcess.");
		} catch (Exception e1) {
			logger.error(e1);
		}
		//从数据库加载任务。
		loadTaskFromDB();
		logger.info("load exist task from db finesh.");
	}
	
	
	
	void loadTaskFromDB() {
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
				try {
					taskManageService.updateTaskOperation(operation);
				} catch (Exception e1) {
					logger.error(e1);
					logger.error("update taskOperation error.");
				}
			} else {

			}
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
	}
}
