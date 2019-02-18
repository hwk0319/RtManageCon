package com.nari.monitormgt.monihome.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.monitormgt.monihome.group.po.GroupsPo;

@Repository(value="groupsDao")
public interface GroupsDao{
	public List<GroupsPo> search(@Param("po") GroupsPo po);	
	public List<GroupsPo> searchList(@Param("po") GroupsPo po);	
	public int searchTotal(@Param("po") GroupsPo po);
}
 