package com.nari.module.paramdb.service;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.paramdb.dao.paramdbDao;
import com.nari.module.paramdb.po.paramdbPo;

@Service(value="paramdbService")
public class paramdbService {
	@Resource(name = "paramdbDao")
	private  paramdbDao dao;
	
	public List<paramdbPo> searchOrcl(@Param("po") paramdbPo po){
		return dao.searchOrcl(po);
	}
	public List<paramdbPo> searchOrcl1(@Param("po") paramdbPo po){
		return dao.searchOrcl1(po);
	}
	public List<paramdbPo> searchMysql(@Param("po") paramdbPo po){
		return dao.searchMysql(po);
	}
	public List<paramdbPo> searchDataList(paramdbPo po) {
		return dao.searchDataList(po);
	}
	public List<paramdbPo> searchDataNum(paramdbPo po) {
		return dao.searchDataNum(po);
	}
	public List<paramdbPo> searchDataError(paramdbPo po) {
		return dao.searchDataError(po);
	}
	public List<paramdbPo> searchMgtSystem(paramdbPo po) {
		return dao.searchMgtSystem(po);
	}
	public List<paramdbPo> searchMgtSystems(paramdbPo po) {
		return dao.searchMgtSystems(po);
	}
	public List<paramdbPo> searchMonindexdata(paramdbPo po) {
		return dao.searchMonindexdata(po);
	}
	public List<paramdbPo> searchMgtdevice(paramdbPo po) {
		return dao.searchMgtdevice(po);
	}
	public List<paramdbPo> searchTrend(paramdbPo po) {
		return dao.searchTrend(po);
	}
}
