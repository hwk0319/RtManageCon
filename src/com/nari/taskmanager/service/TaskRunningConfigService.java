package com.nari.taskmanager.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nari.taskmanager.dao.TaskLogDao;
import com.nari.taskmanager.po.TaskBoundStep;
import com.nari.taskmanager.po.TaskLog;
import com.nari.taskmanager.po.TaskStep;

@Service
public class TaskRunningConfigService {

	@Autowired
	TaskLogDao taskLogDao;

	/**
	 * 把相同steporder的步骤绑定为bound。</br>
	 * 返回一个bound的list。</br>
	 * 
	 * @param steps
	 * @return
	 */
	protected List<TaskBoundStep> buildBoundStep(List<TaskStep> steps) {
		List<TaskBoundStep> bounds = new ArrayList<TaskBoundStep>();
		TaskBoundStep bound = null;
		int stepOrder = Integer.MAX_VALUE;
		for (TaskStep step : steps) {
			if (stepOrder != step.getStepOrder()) {
				stepOrder = step.getStepOrder();
				bound = new TaskBoundStep();
				bound.getSteps().add(step);
				bounds.add(bound);
			} else {
				if (null == bound) {
					bound = new TaskBoundStep();
					bounds.add(bound);
				}
				bound.getSteps().add(step);
			}
		}
		return bounds;
	}

	@Transactional
	public void insertTaskLog(int taskId, int stepId, String hostIp, String level, String detial) {
		TaskLog log = new TaskLog();
		log.setTaskId(taskId);
		log.setStepId(stepId);
		log.setHostIp(hostIp);
		log.setLogLevel(level);
		log.setLogDetial(detial);
		log.setLogTime(new Timestamp(System.currentTimeMillis()).toString());
		insertTaskLog(log);
	}

	public void insertTaskLog(TaskLog log) {
		taskLogDao.insertTaskLog(log);
	}
	
	public void clearTaskLog(int taskId)
	{
		taskLogDao.clearTaskLog(taskId);
	}

}
