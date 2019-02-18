package com.nari.module.deviceindex.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.deviceindex.dao.DeviceindexDao;
import com.nari.module.deviceindex.po.DeviceindexPo;
import com.nari.module.systemmgt.po.SystemmgtPo;

@Service(value="deviceindexService")
public class DeviceindexService {
	@Resource(name = "deviceindexDao")
	private  DeviceindexDao dao;
	
	public List<DeviceindexPo> search(@Param("po") DeviceindexPo po){
		return dao.search(po);
	}
	public List<DeviceindexPo> search2(@Param("po") DeviceindexPo po){
		return dao.search2(po);
	}
	public List<DeviceindexPo> zdysearch(@Param("po") DeviceindexPo po) {
		return dao.zdysearch(po);
	}
	public List<DeviceindexPo> searchUid(@Param("po") DeviceindexPo po){
		return dao.searchUid(po);
	}
	public List<DeviceindexPo> searchUidBytype(@Param("po") DeviceindexPo po){
		return dao.searchUidBytype(po);
	}
	public List<DeviceindexPo> ZKsearchUid(@Param("po") DeviceindexPo po){
		return dao.ZKsearchUid(po);
	}
	
	public int insert(@Param("po")   DeviceindexPo po) {
		return dao.insert(po);
	}	
	
	public int update(@Param("po")   DeviceindexPo po) {
		return dao.update(po);
	}		

	public int delete(@Param("po")   DeviceindexPo po) {
		return dao.delete(po);
	}
	public int zdyinsert(@Param("sql") String sql) {
		return dao.zdyinsert(sql);
	}
	public int zdydelete(@Param("sql") String sql) {
		return dao.zdydelete(sql);
	}
	public int zdyupdate(@Param("sql") String sql){
		return dao.zdyupdate(sql);
	}
	
	public List<DeviceindexPo> searchindex(@Param("po") DeviceindexPo po){
		return dao.searchindex(po);
	}
	public List<DeviceindexPo> searchindex2(@Param("po") DeviceindexPo po){
		return dao.searchindex2(po);
	}
	
	public List<DeviceindexPo> searchbyid(@Param("po") DeviceindexPo po){
		return dao.searchbyid(po);
	}
	
	public List<DeviceindexPo> searchtype(@Param("po") DeviceindexPo po){
		return dao.searchtype(po);
	}
	public List<DeviceindexPo> upload(@Param("po") DeviceindexPo po){
		return dao.upload(po);
	}
	
	public List<DeviceindexPo> searchtype2(@Param("po") DeviceindexPo po){
		return dao.searchtype2(po);
	}
	
	
	public int updateindex(@Param("po") DeviceindexPo po){
		return dao.updateindex(po);
	}
	
	public int insertindex(@Param("po") DeviceindexPo po){
		return dao.insertindex(po);
	}
	public List<DeviceindexPo> checkIndex(DeviceindexPo po) {
		return dao.checkIndex(po);
	}
	public List<DeviceindexPo> zdysearchDev(DeviceindexPo po) {
		return dao.zdysearchDev(po);
	}
	public List<DeviceindexPo> zdysearchMonDev(DeviceindexPo po) {
		return dao.zdysearchMonDev(po);
	}
	public int updateDevices(DevicesPo devPo) {
		return dao.updateDevices(devPo);
	}
	public int insertDevices(DevicesPo devPo) {
		return dao.insertDevices(devPo);
	}
	public int updateSystem(SystemmgtPo sysPo) {
		return dao.updateSystem(sysPo);
	}
	public int insertSystem(SystemmgtPo sysPo) {
		return dao.insertSystem(sysPo);
	}
}
