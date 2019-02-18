package com.nari.taskmanager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nari.module.doublemgt.po.DoublemgtPo;
import com.nari.module.group.po.GroupPo;
import com.nari.taskmanager.po.TaskExecParam;

@Repository
public interface TaskExecDao {

	/**
	 * 根据组的Id获取host的信息。
	 * @param id
	 * @return
	 */
	public List<TaskExecParam> getDeviceInfoByGroupId(@Param("taskExecParam") TaskExecParam taskExecParam);

	/**
	 * 获取双活Po
	 * @param id
	 * @return
	 */
	public DoublemgtPo getDoubleLivePo(@Param("taskExecParam") TaskExecParam taskExecParam);
	
	/**
	 * 根据组Id获取组的信息。
	 * @param id
	 * @return
	 */
	public GroupPo getGroupInfoById(@Param("id") int id);
}
