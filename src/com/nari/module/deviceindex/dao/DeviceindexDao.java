package com.nari.module.deviceindex.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.deviceindex.po.DeviceindexPo;
import com.nari.module.systemmgt.po.SystemmgtPo;

@Repository(value="deviceindexDao")
public interface DeviceindexDao{
	public List<DeviceindexPo> search(@Param("po") DeviceindexPo po);	
	public List<DeviceindexPo> search2(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> zdysearch(@Param("po") DeviceindexPo po);
	public int insert(@Param("po") DeviceindexPo po);
	public int update(@Param("po") DeviceindexPo po);
	public int delete(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> searchUid(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> searchUidBytype(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> ZKsearchUid(@Param("po") DeviceindexPo po);
	public int zdyinsert(@Param("sql") String sql);
	public int zdydelete(@Param("sql") String sql);
	public int zdyupdate(@Param("sql") String sql);
	
	public List<DeviceindexPo> searchindex(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> searchindex2(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> searchbyid(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> searchtype(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> searchtype2(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> upload(@Param("po") DeviceindexPo po);
	
	public int updateindex(@Param("po") DeviceindexPo po);
	public int insertindex(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> checkIndex(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> zdysearchDev(@Param("po") DeviceindexPo po);
	public List<DeviceindexPo> zdysearchMonDev(@Param("po") DeviceindexPo po);
	public int updateDevices(@Param("po") DevicesPo devPo);
	public int insertDevices(@Param("po") DevicesPo devPo);
	public int updateSystem(@Param("po") SystemmgtPo sysPo);
	public int insertSystem(@Param("po") SystemmgtPo sysPo);
}
