package com.nari.taskmanager.service.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskStep;

public class InitShellBuilder extends ShellBuilder {
	private static Logger logger = Logger.getLogger(InitShellBuilder.class);
	
	/**
	 * 
	 * 启动任务时，拷贝初始化脚本到目标主机。 </br>
	 * 对于预设的任务，直接根据 
	 * localip=${1} </br>
	 * remoteIp=${2} </br>
	 * passwd=${3}	</br>
	 * taskPath=${4} </br>
	 * remoteTaskPath=${5} </br>
	 * taskId=${6} </br>
	 * @return
	 * @throws Exception
	 */
	public  List<String> getNormalShellCommand(TaskOperation operation,TaskStep step) throws Exception
	{
		NormalShellCommand norCmd = new NormalShellCommand(operation,null);
		Map<String,String> hostInfo = norCmd.getShellDstHostInfo();
		return builderCommand(operation,step,hostInfo);
	}
	
	private List<String> builderCommand(TaskOperation operation,TaskStep step,Map<String,String> ipInfos)
	{
		List<String> commands = new ArrayList<String>();
		String localIp = TaskConfig.getPropertiesContext().get(TaskConfig.KEY_RETURN_HOST_IP);
		String commShellPath = TaskConfig.TASK_ROOT_PATH+TaskConfig.SHELL_COMMON_INIT_PATH;
		String localTaskPath = TaskConfig.TASK_SHELL_BASE_PATH+operation.getId();//本地任务脚本的目录
		String remoteTaskPath = TaskConfig.REMOTE_TASK_PATH;
		int taskId = operation.getId();
		for(Entry<String,String> entry: ipInfos.entrySet())
		{
			String remoteIp = entry.getKey();
			String remotePasswd = entry.getValue();
			StringBuilder comm = new StringBuilder();
			/*comm.append("cd ").append(commShellPath).append("; nohup  ./")
					.append(TaskConfig.SHELL_COMMON_INIT_SCRIPT_NAME).append(" ").append(localIp).append(" ")
					.append(remoteIp).append(" ").append(remotePasswd).append(" ").append(localTaskPath).append(" ").append(remoteTaskPath).append(" ").append(taskId).append("  \n");*/
			comm.append(" sh ").append(commShellPath).append("/")
					.append(TaskConfig.SHELL_COMMON_INIT_SCRIPT_NAME).append(" ").append(localIp).append(" ")
					.append(remoteIp).append(" ").append(remotePasswd).append(" ").append(localTaskPath).append(" ").append(remoteTaskPath).append(" ").append(taskId).append("  \n");
			logger.info("build commom init shellcommand, taskId"+operation.getId()+" command:"+comm.toString());
			commands.add(comm.toString());
		}
		operation.setResultDesc("任务分发执行主机数:"+commands.size());
		 return commands;
	}

}
