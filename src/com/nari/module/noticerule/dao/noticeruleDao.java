package com.nari.module.noticerule.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.noticerule.po.noticerulePo;
/**
 * 通知规则
 * @date：2017年5月16日
 */
@Repository(value="noticeruleDao")
public interface noticeruleDao {
	public List<noticerulePo> search(@Param("po") noticerulePo po);	
	public List<noticerulePo> zdysearch(@Param("po") noticerulePo po);	
	public int insert(@Param("po") noticerulePo po);
	public int update(@Param("po") noticerulePo po);
	public int delete(@Param("po") noticerulePo po);
	public List<noticerulePo> searchnotice(@Param("po") noticerulePo po);	
	public List<noticerulePo> searchnotice2(@Param("po") noticerulePo po);	
}
