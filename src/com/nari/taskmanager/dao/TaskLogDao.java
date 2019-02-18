package com.nari.taskmanager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.nari.taskmanager.po.TaskLog;

@Repository
public interface TaskLogDao {
	
	/**
	 * 根据taskId获得任务的所有日志
	 * @param taskId
	 * @return
	 */
	public List<TaskLog> getTaskLogByTaskId(@Param("taskId") int taskId);
	
	
	public void insertTaskLog(@Param("taskLog") TaskLog taskLog);
	
	public void clearTaskLog(@Param("taskId") int  taskId);
	
}
