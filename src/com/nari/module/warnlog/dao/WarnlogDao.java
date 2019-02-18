package com.nari.module.warnlog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.warnlog.po.WarnlogPo;

@Repository(value="WarnlogDao")
public interface WarnlogDao {
	public List<WarnlogPo> search(@Param("po") WarnlogPo po);
	public int update(@Param("po") WarnlogPo po);
	public int insert(@Param("po") WarnlogPo po);
	public List<WarnlogPo> customSearch(@Param("po") WarnlogPo po);
	
	public List<WarnlogPo> Searchnum(@Param("po") WarnlogPo po);
	public List<WarnlogPo> Searchdev(@Param("po") WarnlogPo po);
}
