package com.nari.module.device.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.dict.po.DictPo;

@Repository(value="devicesDao")
public interface DeviceDao{
	public List<DevicesPo> search(@Param("po") DevicesPo po);	
	public List<DevicesPo> search2(@Param("po") DevicesPo po);	
	public int insert(@Param("po") DevicesPo po);
	public int update(@Param("po") DevicesPo po);
	public int delete(@Param("po") DevicesPo po);
	public int insertGroupDev(@Param("po") DevicesPo po);
	public int insertSysDev(@Param("po") DevicesPo po);
	public List<DevicesPo> searchIndex(@Param("po") DevicesPo po);	
	public List<DevicesPo> searchDeviceInfo(@Param("po") DevicesPo po);
	public DevicesPo searchPoById(@Param("po") DevicesPo po);
	public int deleteByUid(@Param("po") DevicesPo po);
	public List<DevicesPo> searchById(@Param("po") DevicesPo po);	
	public int deleteFuShuDev(@Param("po") DevicesPo po);
	public int searchMaxNum(@Param("po") DevicesPo po);
	public DictPo searchFactory(@Param("po") DevicesPo po);	
	public List<DevicesPo> zdysearch(@Param("sql") String sql);
	public int deleteSystemDev(@Param("po") DevicesPo po);
	public int deleteGroupDev(@Param("po") DevicesPo po);
	public int isDevUsed(@Param("po") DevicesPo po);
	public int findmonnum(@Param("po") DevicesPo po);
	public int findsysnum(@Param("po") DevicesPo po);
}
