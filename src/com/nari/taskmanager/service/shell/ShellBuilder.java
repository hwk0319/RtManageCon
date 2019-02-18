package com.nari.taskmanager.service.shell;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskStep;

public abstract class ShellBuilder {
	static Logger logger = Logger.getLogger(ShellBuilder.class);
	/**
	 * 获得由用户自定义用户的任务的脚本。</br>
	 * @param operation
	 * @param step
	 * @return
	 */
	abstract List<String> getNormalShellCommand(TaskOperation operation, TaskStep step) throws Exception ;
	
	/**
	 * 根据operation中是presetId生成相应的sql<br>
	 * @param operation
	 * @param step
	 * @return
	 * @throws Exception
	 */
	public   List<String> getShellCommand(TaskOperation operation, TaskStep step) throws Exception{
		List<String> cmds = new ArrayList<String>();
		cmds = getNormalShellCommand(operation,step);
		if(0==cmds.size())
		{
			if(null != step)
			{
				int maxStepOrder = operation.getMaxStep();
				int currentStepOrder = step.getStepOrder();
				operation.setResultDesc("("+currentStepOrder+"/"+maxStepOrder+")  调用脚本失败!");
				step.setResultDesc("调用脚本失败");
				logger.error("cant not builder the shell command ! taskId:"+operation.getId()+" taskName:"+operation.getName()+" stepId:"+step.getId()+" stepName:"+step.getName());
			}
			else
			{
				operation.setResultDesc("调用脚本失败");
				logger.error("cant not builder the shell command ! taskId:"+operation.getId()+" taskName:"+operation.getName());
			}
			throw new Exception("调用脚本失败");
		}
		logger.info("the taskStep taskId:"+operation.getId()+" wating node response num "+cmds.size());
		operation.setDstHostNum(cmds.size());
		return cmds;
	}
}
