package com.nari.module.systemmgt.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.group.po.GroupPo;
import com.nari.module.systemmgt.dao.SystemmgtDao;
import com.nari.module.systemmgt.po.SystemDevicePo;
import com.nari.module.systemmgt.po.SystemmgtPo;
@Transactional
@Service(value="systemmgtService")
public class SystemmgtService {
    @Resource(name="systemmgtDao")
    private SystemmgtDao dao;
    
    public List<SystemmgtPo> search(@Param("po") SystemmgtPo po){
		return dao.search(po);
	}
    public List<SystemmgtPo> search2(@Param("po") SystemmgtPo po){
		return dao.search2(po);
	}
	public int insert(@Param("po")   SystemmgtPo po) {
		return dao.insert(po);
	}
	public int insertGroupSys(@Param("po")   SystemmgtPo po) {
		return dao.insertGroupSys(po);
	}
	public int update(@Param("po")   SystemmgtPo po) {
		return dao.update(po);
	}		
	public int delete(@Param("po")   SystemmgtPo po) throws Exception{
		int res = dao.deletesys_dev(po);
		int res1 = dao.delete(po);
		return res1;
	}
	public List<SystemmgtPo> searchIndex(@Param("po") SystemmgtPo po){
			return dao.searchIndex(po);
	}
	public int insertSysDev(@Param("po")   SystemmgtPo po) {
		return dao.insertSysDev(po);
	}
	public List<SystemDevicePo> searchSysDev(@Param("po") SystemDevicePo po){
		return dao.searchSysDev(po);
	}
	
	public List<DevicesPo> searchDevice(@Param("po") DevicesPo po){
		return dao.searchDevice(po);
	}
	
	public int deleteDevice(@Param("po")   SystemmgtPo po) {
		return dao.deleteDevice(po);
	}
//	public List<DevicesPo> searchDevices(GroupPo po) {
//		return dao.searchDevices(po);
//	}
	public int findmonnum(@Param("po") SystemmgtPo po) {
		return dao.findmonnum(po);
	}
}
