package com.nari.module.group.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.group.dao.GroupDao;
import com.nari.module.group.po.GroupDevicePo;
import com.nari.module.group.po.GroupPo;

@Service(value="groupService")
public class GroupService {
	@Resource(name = "groupDao")
	private  GroupDao dao;
	
	public List<GroupPo> search(@Param("po") GroupPo po){
		return dao.search(po);
	}
	
	public List<GroupDevicePo> searchGroDev(@Param("po") GroupDevicePo po){
		return dao.searchGroDev(po);
	}
	
	public int insert(@Param("po")   GroupPo po) {
		return dao.insert(po);
	}	
	public int insertGroDev(@Param("po")   GroupPo po) {
		return dao.insertGroDev(po);
	}	
	public int insertDoubleGro(@Param("po")   GroupPo po) {
		return dao.insertDoubleGro(po);
	}
	public int insertClusterGro(@Param("po")   GroupPo po) {
		return dao.insertClusterGro(po);
	}
	public int insertIntegrateGro(@Param("po")   GroupPo po) {
		return dao.insertIntegrateGro(po);
	}
	public int update(@Param("po")   GroupPo po) {
		return dao.update(po);
	}		

	public int delete(@Param("po")   GroupPo po) {
		return dao.delete(po);
	}
	
	public List<DevicesPo> searchDevice(@Param("po") DevicesPo po){
		return dao.searchDevice(po);
	}
	
	public int deleteDevice(@Param("po")   GroupPo po) {
		return dao.deleteDevice(po);
	}
}
