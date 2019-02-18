package com.nari.taskmanager.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Synchronization;

import com.nari.taskmanager.util.ParseUtil;
import com.nari.taskmanager.util.TaskFileUtil;

public class TaskConfig {
	/**
	 * 任务创建执行的状态
	 * 0-creating,1-init，2-running,3-success,4-fail,5-error creating,6-正在停止，7-已由用户停止
	 * 任务正在创建中，用户点击创建但未点保存。
	 */
	public static final int TASK_STATE_CREATING = 0;
	public static final int TASK_STATE_INIT = 1;
	public static final int TASK_STATE_RUNNING = 2;
	public static final int TASK_STATE_SUCCESS = 3;
	public static final int TASK_STATE_FAIL = 4;
	public static final int TASK_STATE_ERROR = 5;
	public static final int TASK_STATE_STOPPINF = 6;
	public static final int TASK_STATE_STOPPED = 7;

	// 一般方法的String返回值
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String INNER_ERROR = "内部错误";
	public static final String DB_ERROR = "数据库错误";

	/**
	 * 任务类型 1-普通任务 ， 2-定时任务,3-zk启动的任务。
	 */
	public static final int TASK_TYPE_NORMAL = 1;
	public static final int TASK_TYPE_SCHEDULE = 2;
	public static final int TASK_TYPE_ZK= 3;
	
	
	/**
	 * 任务执行的并行策略。
	 * 0--不受限制。
	 */
	public static final int TASK_STRATEGY_ALL_PARALLEL=0;
	
	/**
	 * 任务执行的并行策略。
	 * 1--同一用户下的不能并行（同一 userid）。
	 */
	
	public static final int TASK_STRATEGY_USER_PARALLEL=1;
	/**
	 * 任务执行的并行策略。
	 * 2--独占任务只能单独执行。
	 */
	public static final int TASK_STRATEGY_ONE_PARALLEL=2;
	
	
	/**
	 * 支持的上传文件的类型。
	 */
	public static final String UPLOAD_SUPPORT_TYPE_SH="sh";
	public static final String UPLOAD_SUPPORT_TYPE_INI="ini";
	public static final String UPLOAD_SUPPORT_TYPE_SQL="sql";

	/**
	 * task配置文件
	 */
	public static final String TASK_PARAM_FILE_NAME = "paramList.ini";
	
	
	/**
	 * 任务根路径
	 */
	public static final String TASK_ROOT_PATH = System.getProperty("webapp.root");
	/**
	 * 任务模板的保存路径
	 */
	public static final String TASK_TEMPLATE_PATH = "shell" + File.separator + "taskTemplate";
	/**
	 * shell/task</br>
	 * 用户新建任务的保存路径   （要持久化到数据库）相对路径</br>
	 */
	private static final String TASK_TASK_PATH =  "shell" + File.separator + "task";
	
	/**
	 *	本地任务保存的 路径。
	 *./RtmanageCon/shell/task
	 */
	public static final String TASK_SHELL_BASE_PATH = TASK_ROOT_PATH+TASK_TASK_PATH+File.separator;
	
	/**
	 * 本地模板的保存路径
	 */
	public static final String TASK_TPL_BASE_PATH = TASK_ROOT_PATH+TASK_TEMPLATE_PATH+File.separator;
	/**
	 * 远程目标主机的任务保存路径</br>
	 *\/root/return/task
	 */
	public static final String REMOTE_TASK_PATH = File.separator+"root"+File.separator+"return"+File.separator+"task";
	
	/**
	 * 系统启动时的配置文件。</br>
	 * 需要用户手动修改</br>
	 */
	public static final String systemConfig = TASK_ROOT_PATH+"WEB-INF"+File.separator+"taskConfig"+File.separator+"server.properties";
	
	public static final String returnConfig="config/return.properties";
	
	/**
	 * shell脚本回调时的配置参数，主机的ip与端口。
	 */
	public static final String SHELL_RESPONSE_SERVER_CONFIG_FILE=TASK_ROOT_PATH+"shell"+File.separator+"task"+File.separator+"commScript"+File.separator+"callback"+File.separator+"hostConfig.ini";
	
	/**
	 * 	基础的初始化脚本路径，双机信任，分发任务。
	 */
	public static final String SHELL_COMMON_INIT_PATH = TASK_TASK_PATH+File.separator+"commScript";
	/**
	 * 本地启动任务时调动的脚本，用于分发任务（复制脚本到目标主机)
	 */
	public static final String SHELL_COMMON_INIT_SCRIPT_NAME = "commonInitial.sh";
	/**
	 * 调用目标主机上的脚本
	 */
	public static final String SHELL_CALL_REMOTE_SCRIPT_NAME = "callRemoteTask.sh";
	
	/**
	 * local任务回调的脚本路径。
	 */
	public static final String SHELL_RESPONSE_SCRIPT_PATH= TASK_TASK_PATH+File.separator+"commScript"+File.separator+"callback";
	

	/**
	 *  前台上传文件，标识传送的为脚本文件。
	 */
	public static final String TASK_FILEUPLOAD_TYPE_SHELL = "shell";

	/**
	 * 前台上传文件，标识传送的为脚本模板文件。
	 */
	public static final String TASK_FILEUPLOAD_TYPE_SHELL_TPL = "shellTemplate";

	/**
	 *  前台上传文件，标识传送的为参数配置文件。
	 */
	public static final String TASK_FILEUPLOAD_TYPE_PARAM = "param";

	/**
	 * 前台上传文件，标识传送的为参数配置模板文件。
	 */
	public static final String TASK_FILEUPLOAD_TYPE_PARAM_TPL = "paramTemplate";

	public static final String LINESEPARATOR = System.getProperty("line.separator", "\n");
	
	/**
	 * 脚本执行返回的状态
	 * 1--正在执行（上报执行消息或进度）。
	 * 2--执行成功。
	 * 3--执行失败。
	 * 4--脚本返回值。
	 * 5--通知后台进行判断。
	 */
	public static final int TASK_SHELL_RESULT_RUNNING = 1;
	public static final int TASK_SHELL_RESULT_SUCCESS = 2;
	public static final int TASK_SHELL_RESULT_EXCEPTION = 3;
	public static final int TASK_SHELL_RESULT_VALUE = 4;
	public static final int TASK_SHELL_RESULT_OPERA = 5;
	
	/**
	 * 校验的类型
	 * 模板
	 * 任务
	 */
	public static final String TASK_REQUEST_TYPE="task";
	public static final String TPL_REQUEST_TYPE="tpl";
	
	/**
	 * shell返回日志级别
	 */
	public static final String TASK_LOG_LEVEL_INFO="info";
	public static final String TASK_LOG_LEVEL_DEBUG="debug";
	public static final String TASK_LOG_LEVEL_WARN="warn";
	public static final String TASK_LOG_LEVEL_ERROR="error";
	public static final String TASK_LOG_LEVEL_FATAL="fatal";
	public static final String TASK_LOG_LEVEL_TRACE="trace";
	
	/**
	 * 返回的json字段名称
	 */
	public static final String TASK_JSON_STATUS="status";
	public static final String TASK_JSON_VALUE="value";
	
	
	//===================================ZK 任务配置============================
	public static final String TASK_ZK_UID_FIAG="#{uid}";
	public static final String TASK_ZK_WATCH_PATH="/RT/Tasks";
	public static final String TASK_ZK_WEB_FLAG="/RT/WEBSEVICE";
	public static final String TASK_ZK_CONFIG_PATH="config/monitor.xml";
	public static final int TASK_ZK_WATCH_TIMEOUT=5000;
	//===================================ZK 任务配置 end============================
	

	public static Map<String,String> getPropertiesContext(){
		
		if(null == PropertiesContext.localContext){
			synchronized (PropertiesContext.class) {
				if(null == PropertiesContext.localContext){
					try {
						Properties prop = TaskFileUtil.LoadPropFile(TaskConfig.returnConfig);
						ParseUtil.parseInitConfig(prop);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return PropertiesContext.localContext;
		
	}
	
	/**
	 * zookeeper server ip KEY
	 */
	public static final String KEY_ZKCONNECT_STR = "zkconnect_str_key"; 
	/**
	 * shell response Ip KEY。
	 */
	public static final String KEY_SHELL_RESPONSE_IP = "shell_server_conn_ip";
	
	public static final String KEY_RETURN_HOST_IP = "return_host_ip";
	
	/**
	 * 远程目标主机的路径 KEY。
	 */
	public static final String SHELL_REMOTE_TASK_PATH="shell_remote_task_path";
	
	/**
	 * 用户配置脚本，脚本IP参数配置项前缀。
	 */
	public static final String USER_TASK_REMOTEIP_SUFFIX="shellip_";
	
	/**
	 * 用户配置脚本，脚本passwd参数配置项前缀。
	 */
	public static final String USER_TASK_REMOTEPASSWD_SUFFIX="shellpasswd_";
	/**
	 * 用户配置脚本，脚本passwd参数配置项前缀。
	 */
	public static final String USER_TASK_REMOTEPUSER_SUFFIX="shelluser_";
	
	public static class PropertiesContext{
		public static  Map<String,String> localContext = null;
		public String get(String key){
			return localContext.get(key);
		}
	}

}
