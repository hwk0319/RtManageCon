package com.nari.module.thresholdmgt.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.thresholdmgt.dao.ThresholdDao;
import com.nari.module.thresholdmgt.po.ThresholdPo;

@Service(value=" thresholdService")
public class ThresholdService {
    @Resource(name=" thresholdDao")
    private  ThresholdDao dao;
    public List<ThresholdPo> search(@Param("po") ThresholdPo po){
		return dao.search(po);
	}
	public int insert(@Param("po")   ThresholdPo po) {
		return dao.insert(po);
	}
	public int update(@Param("po")   ThresholdPo po) {
		return dao.update(po);
	}	
	public int delete(@Param("po")   ThresholdPo po) {
		return dao.delete(po);
	}
  public List<ThresholdPo> searchmodel(@Param("po") ThresholdPo po){
		return dao.searchmodel(po);
	}
  public List<ThresholdPo> searchmodelitem(@Param("po") ThresholdPo po){
		return dao.searchmodelitem(po);
	}
  public List<ThresholdPo> searchindex(@Param("po") ThresholdPo po){
		return dao.searchindex(po);
	}
  public List<ThresholdPo> searchstatus(@Param("po") ThresholdPo po){
		return dao.searchstatus(po);
	}
	
	
	
	
	
}
