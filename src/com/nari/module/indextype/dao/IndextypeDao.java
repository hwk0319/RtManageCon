package com.nari.module.indextype.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.indextype.po.IndextypePo;

@Repository(value="indextypeDao")
public interface IndextypeDao {
	public List<IndextypePo> search(@Param("po") IndextypePo po);
	public int insert(@Param("po") IndextypePo po);
	public int update(@Param("po") IndextypePo po);
	public int delete(@Param("po") IndextypePo po);
	public List<IndextypePo> zdysearch(@Param("po") IndextypePo po);
	public List<IndextypePo> checkIndexTypeID(@Param("po") IndextypePo po);
	public List<IndextypePo> searchIndexTypeById(@Param("po") IndextypePo po);
	public int updateMonIndex(@Param("po") IndextypePo po, @Param("indexTypeId") int oldIndextypeid);
	public int updateMonIndex1(@Param("po") IndextypePo po);
}
