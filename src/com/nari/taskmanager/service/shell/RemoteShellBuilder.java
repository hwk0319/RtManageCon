package com.nari.taskmanager.service.shell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskStep;

public class RemoteShellBuilder extends ShellBuilder {
	private static Logger logger = Logger.getLogger(RemoteShellBuilder.class);

	/**
	 * 执行调用远程目标主机的任务脚本。 </br>
	 * # $1 localIp</br>
	 * # $2 remoteIp 远程主机的IP </br>
	 * # $3 passwd 远程主机的密码 </br>
	 * # $4 shellPath 远程主机的脚本路径 </br>
	 * # $5 shellParam 远程主机的参数路径 </br>
	 * # $6 taskId=${6} </br>
	 * # $7 stepId=${7}</br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getNormalShellCommand(TaskOperation operation, TaskStep step) throws Exception {

		NormalShellCommand norcmd = new NormalShellCommand(operation, step);
		Map<String, String> hostInfo = norcmd.getShellDstHostInfo();
		List<String> cmms = new ArrayList<String>();
		String commShellPath = TaskConfig.TASK_ROOT_PATH + TaskConfig.SHELL_COMMON_INIT_PATH;
		String localIp = TaskConfig.getPropertiesContext().get(TaskConfig.KEY_RETURN_HOST_IP);
		String remoteTaskBasePath = TaskConfig.REMOTE_TASK_PATH + File.separator + operation.getId();

		String remoteShellPath = remoteTaskBasePath + File.separator + step.getShellName();
		String shellParamPath = remoteTaskBasePath + File.separator + TaskConfig.TASK_PARAM_FILE_NAME;
		for (Entry<String, String> entry : hostInfo.entrySet()) {
			String remoteIp = entry.getKey();
			String passwd = entry.getValue();
			StringBuffer cmdBuf = new StringBuffer();
			cmdBuf.append("sh ").append(commShellPath).append("/")
					.append(TaskConfig.SHELL_CALL_REMOTE_SCRIPT_NAME).append(" ").append(localIp).append(" ")
					.append(remoteIp).append(" ").append(passwd).append(" ").append(remoteShellPath).append(" ")
					.append(shellParamPath).append(" ").append(step.getTaskId()).append(" ").append(step.getId())
					.append(" \n");
			
/*			cmdBuf.append("cd ").append(commShellPath).append(" ; ").append("nohup ./")
			.append(TaskConfig.SHELL_CALL_REMOTE_SCRIPT_NAME).append(" ").append(localIp).append(" ")
			.append(remoteIp).append(" ").append(passwd).append(" ").append(remoteShellPath).append(" ")
			.append(shellParamPath).append(" ").append(step.getTaskId()).append(" ").append(step.getId())
			.append(" \n");*/
			logger.info("build init remote call shell, taskId:" + operation.getId() + " commands:" + cmdBuf.toString());
			cmms.add(cmdBuf.toString());
		}

		return cmms;
	}
}
