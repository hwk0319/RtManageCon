package com.nari.module.device.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.device.dao.DeviceDao;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.dict.po.DictPo;

@Service(value="devicesService")
public class DeviceService {
	@Resource(name = "devicesDao")
	private  DeviceDao dao;
	
	public List<DevicesPo> search(@Param("po") DevicesPo po){
		return dao.search(po);
	}
	public List<DevicesPo> search2(@Param("po") DevicesPo po){
		return dao.search2(po);
	}
	
	public int insert(@Param("po")   DevicesPo po) {
		return dao.insert(po);
	}	
	public int insertGroupDev(@Param("po")   DevicesPo po) {
		return dao.insertGroupDev(po);
	}	
	
	public int update(@Param("po")   DevicesPo po) {
		return dao.update(po);
	}		

	public int delete(@Param("po")   DevicesPo po) {
		return dao.delete(po);
	}
	public int deleteFuShuDev(@Param("po")   DevicesPo po) {
		return dao.deleteFuShuDev(po);
	}
	
	public int deleteByUid(@Param("po")   DevicesPo po) {
		return dao.deleteByUid(po);
	}
	
	public int insertSysDev(@Param("po")   DevicesPo po) {
		return dao.insertSysDev(po);
	}	

	public List<DevicesPo> searchIndex(@Param("po") DevicesPo po){
		return dao.searchIndex(po);
	}
	
	public List<DevicesPo> searchDeviceInfo(@Param("po") DevicesPo po){
		return dao.searchDeviceInfo(po);
	}
	
	public DevicesPo searchPoById(@Param("po") DevicesPo po){
		return dao.searchPoById(po);
	}
	
	public List<DevicesPo> searchById(@Param("po") DevicesPo po){
		return dao.searchById(po);
	}
	
	public int searchMaxNum(@Param("po") DevicesPo po){
		return dao.searchMaxNum(po);
	}
	public DictPo searchFactory(@Param("po") DevicesPo po) {
		return dao.searchFactory(po);
	}
	public List<DevicesPo> zdysearch(@Param("sql") String sql){
		return dao.zdysearch(sql);
	}
	public int deleteSystemDev(DevicesPo po) {
		return dao.deleteSystemDev(po);
	}
	public int deleteGroupDev(DevicesPo po) {
		return dao.deleteGroupDev(po);
	}
	public int isDevUsed(@Param("po") DevicesPo po) {
		return dao.isDevUsed(po);
	}
	public int findmonnum(@Param("po") DevicesPo po) {
		return dao.findmonnum(po);
	}
	public int findsysnum(@Param("po") DevicesPo po) {
		return dao.findsysnum(po);
	}
}
