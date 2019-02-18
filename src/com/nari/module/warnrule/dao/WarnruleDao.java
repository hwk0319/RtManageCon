package com.nari.module.warnrule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.warnrule.po.WarnrulePo;
import com.nari.taskmanager.po.TaskOperation;

@Repository(value="warnruleDao")
public interface WarnruleDao{
	public List<WarnrulePo> search(@Param("po") WarnrulePo po);	
	public int insert(@Param("po") WarnrulePo po);
	public int update(@Param("po") WarnrulePo po);
	public int delete(@Param("po") WarnrulePo po);
	public List<WarnrulePo> searchTask(@Param("po") WarnrulePo po);	
}
