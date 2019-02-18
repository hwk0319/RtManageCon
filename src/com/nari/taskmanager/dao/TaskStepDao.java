package com.nari.taskmanager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskParam;
import com.nari.taskmanager.po.TaskStep;

@Repository
public interface TaskStepDao {

	public List<TaskStep> getTaskStepTemplate(@Param("taskStep")TaskStep taskStep);


	public List<TaskParam> getTaskParamTemplate(@Param("taskParam") TaskParam taskParam);


	public void saveTaskStepTemplate(@Param("taskStep") TaskStep stepTemplate);

	public void removeTaskStepTemplate(@Param("taskStep") TaskStep stepTemplate);

	public void updateTaskStepTemplate(@Param("taskStep") TaskStep stepTemplateTpl);

	public void saveTaskParamTemplate(@Param("taskParam") TaskParam taskParamTemplate);

	public void updateTaskParamTemplate(@Param("taskParam") TaskParam paramTemplate);

	public void removeTaskParamTemplate(@Param("taskParam") TaskParam paramTemplate);

	/**
	 * 在创建任务时新增一个步骤
	 * 
	 * @param step
	 */
	public int addTaskStep(@Param("taskStep") TaskStep taskStep);

	/**
	 * 在创建任务时新增一个参数
	 * 
	 * @param param
	 */
	public void addTaskParam(@Param("taskParam") TaskParam param);

	/**
	 * 在创建任务时修改一个step。 根据step的id进行修改。
	 * 
	 * @param step
	 */
	public void updateTaskStep(@Param("taskStep") TaskStep step);
	

	/**
	 * 在创建任务时修改一个param 根据param的id进行修改
	 * 
	 * @param param
	 */
	public void updateTaskParam(@Param("taskParam") TaskParam param);

	/**
	 * 删除一个任务步骤, 根据step的id删除，而不是根据taskId。只会删除一条。
	 * 
	 * @param step
	 */
	public void removeTaskStep(@Param("taskStep") TaskStep step);

	/**
	 * 删除一个步骤 根据param的id删除，而不是根据taskId，只会删除一条。
	 * 
	 * @param param
	 */
	public void removeTaskParam(@Param("taskParam") TaskParam param);
	
	/**
	 * 删除一个任务下的所有参数
	 * @param taskId
	 */
	public void removeTaskParams(@Param("taskId") int taskId);

	/**
	 * 根据taskStep的属性修改。
	 * @return
	 */
	public List<TaskStep> getTaskStep(@Param("taskStep") TaskStep taskStep);


	/**
	 * 根据taskid获取相应任务的所有param。
	 * 
	 * @param taskId
	 * @return
	 */
	public List<TaskParam> getTaskParam(@Param("taskParam") TaskParam taskParam);


	/**
	 * 删除一个任务下的所有步骤。
	 * @param operation
	 */
	public void removeTaskSteps(@Param("taskId")int taskId);


	public void removeTaskParams(@Param("operation")TaskOperation operation);


	public void removeTaskParamTpls(@Param("taskId")int taskId);

	public void removeTaskStepTpls(@Param("taskId") int id);

}
