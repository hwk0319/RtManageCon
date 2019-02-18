package com.nari.module.dict.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.dict.po.DictPo;

@Repository(value="dictDao")
public interface DictDao{
	public List<DictPo> search(@Param("po") DictPo po);	
	public int insert(@Param("po") DictPo po);
	public int update(@Param("po") DictPo po);
	public int delete(@Param("po") DictPo po);
	public List<DictPo> customSql(@Param("po") DictPo po);	
	
}
