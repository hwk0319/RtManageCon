package com.nari.taskmanager.service.shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskParam;
import com.nari.taskmanager.po.TaskStep;

/**
 * 普通的shell命令</br>
 * 由用户创建的任务的普通的任务的shell命令。</br>
 * 用户自己配置的参数，为了指定脚本运行的目的主机，参数命名格式格式如下。</br>
 * ip参数名为：“shellip_”加脚本名。如果该脚本对应多个主机，多个IP用“,”隔开。</br>
 * 密码参数名：“shellpasswd_”加脚本名。如果该脚本对应多个主机，多个passwd用“,”隔开，并与ip的顺序相对应。</br>
 * 如   shellip_install.sh		:1.1.1.1,2.2.2.2</br>
 * 	   shellpasswd_install.sh	:admin,root</br>
 * @author admin
 *
 */
public class NormalShellCommand extends ShellCommand {
	private Logger logger = Logger.getLogger(NormalShellCommand.class);
	Map<String,String> ipInfo = new HashMap<String,String>();
	public NormalShellCommand(TaskOperation operation,TaskStep step) throws Exception
	{
		super(operation,step);
		initParam();
	}
	
	/**
	 * 普通任务的远程主机IP的配置</br>
	 * 参数命名格式为 ”shellip_“ + shellName="ip1,ip2",</br>
	 * 解释为 脚本名称为shellName 的脚本需要在ip1,ip2的主机上执行</br>
	 * 当获取初始化的脚本时step为null，会返回所有IP与passwd。<br>
	 * 当获取步骤的脚本时，只返回当前步骤的对应的ip与passwd。<br>
	 */
	@Override
	public void initParam() throws Exception {
		List<TaskParam> params = operaiton.getParams();
		for (TaskParam paramIp : params) {
			String paramIpName = paramIp.getName();
			if (paramIpName.startsWith(TaskConfig.USER_TASK_REMOTEIP_SUFFIX)) {
				String shellName = paramIpName.substring(TaskConfig.USER_TASK_REMOTEIP_SUFFIX.length(),paramIpName.length());
				if(null!=step && !shellName.equals(step.getShellName()))
				{
					continue;
				}
				for(TaskParam paramPasswd : params)
				{
					String paramPasswdName = paramPasswd.getName();
					if(paramPasswdName.startsWith(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX) && paramPasswdName.substring(TaskConfig.USER_TASK_REMOTEPASSWD_SUFFIX.length(),paramPasswdName.length()).equals(shellName))
					{
						String ipStr = paramIp.getValue();
						String passwdStr = paramPasswd.getValue();
						if(null == ipStr || null == passwdStr){
							throw new Exception("invalide ip or passwd ip:"+ipStr+" passwd:"+(null == passwdStr? "null" : "******" )+" shellName:"+shellName);
						}
						String[] ips = ipStr.split(",");
						String[] passwds = passwdStr.split(",");
						if(ips.length != passwds.length || ips.length==0)
						{
							logger.error("get ip and passwd param error,the number of ip and passwd is not equal.");
							logger.error("Ip    :"+ipStr);
							throw new Exception("get ip and passwd param error.");
						}
						for(int i=0;i<ips.length;i++)
						{
							ipInfo.put(ips[i], passwds[i]);
						}
					}
				}
			}
		}
		logger.info("get remote host info,size:"+ipInfo.size());
	}

	@Override
	public Map<String, String> getShellDstHostInfo() {
		return ipInfo;
	}
}
