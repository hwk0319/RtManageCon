package com.nari.module.paramdb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.paramdb.po.paramdbPo;

@Repository(value="paramdbDao")
public interface paramdbDao{
	public List<paramdbPo> searchOrcl(@Param("po") paramdbPo po);
	public List<paramdbPo> searchOrcl1(@Param("po") paramdbPo po);
	public List<paramdbPo> searchMysql(@Param("po") paramdbPo po);
	public List<paramdbPo> searchDataList(@Param("po") paramdbPo po);
	public List<paramdbPo> searchDataNum(@Param("po") paramdbPo po);
	public List<paramdbPo> searchDataError(@Param("po") paramdbPo po);
	public List<paramdbPo> searchMgtSystem(@Param("po") paramdbPo po);
	public List<paramdbPo> searchMgtSystems(@Param("po") paramdbPo po);
	public List<paramdbPo> searchMonindexdata(@Param("po") paramdbPo po);
	public List<paramdbPo> searchMgtdevice(@Param("po") paramdbPo po);
	public List<paramdbPo> searchTrend(@Param("po") paramdbPo po);
}
 