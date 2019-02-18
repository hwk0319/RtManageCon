package com.nari.taskmanager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nari.module.device.po.DevicesPo;

@Repository
public interface  ZKTaskDao {
	
	public DevicesPo getDevcieByUid(@Param("uid") String uid);
	
	public List<Map<String,String>> getDeviceParamByUid(@Param("sql") String sql);
	
	public List<Map<String,String>> getSystemParamByUid(@Param("sql") String sql);
	
}
