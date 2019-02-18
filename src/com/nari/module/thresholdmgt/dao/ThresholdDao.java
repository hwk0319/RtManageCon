package com.nari.module.thresholdmgt.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.thresholdmgt.po.ThresholdPo;


@Repository(value=" thresholdDao")
public interface  ThresholdDao {
	public List<ThresholdPo> search(@Param("po") ThresholdPo po);
	public int insert(@Param("po") ThresholdPo po);
	public int update(@Param("po") ThresholdPo po);
	public int delete(@Param("po") ThresholdPo po);
	public List<ThresholdPo> searchmodel(@Param("po") ThresholdPo po);
	public List<ThresholdPo> searchmodelitem(@Param("po") ThresholdPo po);
	public List<ThresholdPo> searchindex(@Param("po") ThresholdPo po);
	public List<ThresholdPo> searchstatus(@Param("po") ThresholdPo po);
}
