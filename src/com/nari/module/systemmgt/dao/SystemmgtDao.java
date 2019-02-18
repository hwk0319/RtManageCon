package com.nari.module.systemmgt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.systemmgt.po.SystemDevicePo;
import com.nari.module.systemmgt.po.SystemmgtPo;

@Repository(value="systemmgtDao")
public interface SystemmgtDao {
	public List<SystemmgtPo> search(@Param("po") SystemmgtPo po);
	public List<SystemmgtPo> search2(@Param("po") SystemmgtPo po);
	public int insert(@Param("po") SystemmgtPo po);
	public int update(@Param("po") SystemmgtPo po);
	public int delete(@Param("po") SystemmgtPo po);
	public int insertGroupSys(@Param("po") SystemmgtPo po);
	public List<SystemmgtPo> searchIndex(@Param("po") SystemmgtPo po);
	public int insertSysDev(@Param("po") SystemmgtPo po);
	public List<SystemDevicePo> searchSysDev(@Param("po") SystemDevicePo po);
	public List<DevicesPo> searchDevice(@Param("po") DevicesPo po);
	public int deleteDevice(@Param("po")   SystemmgtPo po);
//	public List<DevicesPo> searchDevices(@Param("po") GroupPo po);
	public int deletesys_dev(@Param("po") SystemmgtPo po);
	public int findmonnum(@Param("po") SystemmgtPo po);
}
