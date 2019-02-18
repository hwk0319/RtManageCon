package com.nari.module.doublemgt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.doublemgt.po.CorePo;
import com.nari.module.doublemgt.po.DoublemgtPo;

@Repository(value="doublemgtDao")
public interface DoublemgtDao {
	public List<DoublemgtPo> search(@Param("po") DoublemgtPo po);
	public int insert(@Param("po") DoublemgtPo po);
	public int update(@Param("po") DoublemgtPo po);
	public int delete(@Param("po") DoublemgtPo po);
	public int insertDoubleGroup(@Param("po") DoublemgtPo po);
	public List<DoublemgtPo> search_group_core(@Param("po") DoublemgtPo po);
	public int deletecore(@Param("po") DoublemgtPo po);
	public int deletecoregroup(@Param("po") DoublemgtPo po);
	
	public int insertcore(@Param("po") CorePo po);
	public int updatecore(@Param("po") CorePo po);
	public int deletegroup(@Param("po") CorePo po);
	public int insertcoreGroup(@Param("po") CorePo po);
}
