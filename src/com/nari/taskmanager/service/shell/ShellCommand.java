package com.nari.taskmanager.service.shell;

import java.util.Map;

import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskStep;

public abstract  class ShellCommand {
	protected TaskOperation operaiton;
	protected TaskStep step;
	public ShellCommand(){
		
	}
	public ShellCommand(TaskOperation operation,TaskStep step) throws Exception
	{
		this.operaiton = operation;
		this.step =step;
	}
	
	/**
	 * 设置脚本执行的远程主机的IP。</br>
	 * 根据定义好的命名规则，获取参数中配置的脚本所要执行的远程主机IP。</br>
	 * @throws Exception
	 */
	public void initParam() throws Exception{
	}
	
	/**
	 * 获取脚本所执行的主机的信息</br>
	 * Map<String,Map<String,String>>   脚本名：《ip:passwd》
	 * @return
	 */
	public abstract Map<String,String> getShellDstHostInfo();

}
