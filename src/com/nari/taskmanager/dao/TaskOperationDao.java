package com.nari.taskmanager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nari.taskmanager.po.TaskOperation;

@Repository
@Scope("singleton")
public interface TaskOperationDao {

	public void insertTaskTemplate(@Param("taskOperation") TaskOperation taskTemplate);

	public void removeTaskTemplate(@Param("taskOperation") TaskOperation taskOperation);

	public void insertTask(@Param("taskOperation") TaskOperation taskOperation);

	/**
	 * 删除任务以及与其关联的step与参数以及日志。
	 * @param taskOperation
	 */
	public void removeTask(@Param("taskOperation") TaskOperation taskOperation);

	/**
	 * 更新一个TaskOperation, 不关联step与 param
	 * 
	 * @param taskOperation
	 */
	public void updateTaskOperation(@Param("taskOperation") TaskOperation taskOperation);

	public void updateTaskOperationTemplate(@Param("taskOperation") TaskOperation operation);

	/**
	 * 更新一个任务参数。同时更新与其关联的step与 param
	 * 
	 * @param taskOperation
	 */
	//public void updateTask(@Param("taskOperation") TaskOperation taskOperation);

	public int getOperationSeq(@Param("taskOperation") TaskOperation taskOperation);

	public int getTemplateSeq(@Param("taskOperation") TaskOperation taskOperation);
	
	public int getTaskTotalCount(@Param("taskOperation") TaskOperation operation);
	public int getTaskTplTotalCount(@Param("taskOperation") TaskOperation operation);

	public List<TaskOperation> getTask(@Param("taskOperation") TaskOperation operation);

	//public List<TaskOperation> getTaskById(@Param("id") int id);

	public List<TaskOperation> getTemplateTask(@Param("taskOperation") TaskOperation operation);

	public List<TaskOperation> searchTotal(@Param("taskOperation")TaskOperation taskOperation);

	public List<TaskOperation> search1(@Param("taskOperation")TaskOperation taskOperation);
}
