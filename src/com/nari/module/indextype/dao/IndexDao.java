package com.nari.module.indextype.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.indextype.po.IndexPo;

@Repository(value="indexDao")
public interface IndexDao {

	public List<IndexPo> search(@Param("po") IndexPo po);
	public List<IndexPo> searchindex(@Param("po") IndexPo po);
	public int insert(@Param("po") IndexPo po);
	public int update(@Param("po") IndexPo po);
	public int delete(@Param("po") IndexPo po);
	public int insertindex(@Param("po") IndexPo po);
}
