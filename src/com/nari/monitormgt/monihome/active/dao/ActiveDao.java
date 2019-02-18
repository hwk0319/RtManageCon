package com.nari.monitormgt.monihome.active.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.monitormgt.monihome.active.po.ActivePo;

@Repository(value="ActiveDao")
public interface ActiveDao{
	public List<ActivePo> search(@Param("po") ActivePo po);	
	public List<ActivePo> searchList(@Param("po") ActivePo po);	
	public int searchTotal(@Param("po") ActivePo po);
}
 