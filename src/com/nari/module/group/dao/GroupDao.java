package com.nari.module.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.group.po.GroupDevicePo;
import com.nari.module.group.po.GroupPo;

@Repository(value="groupDao")
public interface GroupDao{
	public List<GroupPo> search(@Param("po") GroupPo po);	
	public int insert(@Param("po") GroupPo po);
	public int insertGroDev(@Param("po") GroupPo po);
	public int update(@Param("po") GroupPo po);
	public int delete(@Param("po") GroupPo po);
	public int insertDoubleGro(@Param("po") GroupPo po);
	public int insertClusterGro(@Param("po") GroupPo po);
	public int insertIntegrateGro(@Param("po") GroupPo po);
	public List<GroupDevicePo> searchGroDev(@Param("po") GroupDevicePo po);
	public List<DevicesPo> searchDevice(@Param("po") DevicesPo po);
	public int deleteDevice(@Param("po") GroupPo po);
}
