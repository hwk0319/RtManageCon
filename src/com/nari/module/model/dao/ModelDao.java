package com.nari.module.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.model.po.ModelPo;

@Repository(value="modelDao")
public interface ModelDao{
	public List<ModelPo> search(@Param("po") ModelPo po);	
	public int insert(@Param("po") ModelPo po);
	public int update(@Param("po") ModelPo po);
	public int delete(@Param("po") ModelPo po);
}
