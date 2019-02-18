package com.nari.module.noticerule.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.noticerule.dao.noticeruleDao;
import com.nari.module.noticerule.po.noticerulePo;
/**
 * 通知规则
 * @author：ldj
 * @date：2017年5月16日
 */
@Service(value="noticeruleService")
public class noticeruleService {
	@Resource(name = "noticeruleDao")
	private  noticeruleDao dao;
	public List<noticerulePo> search(@Param("po") noticerulePo po){
		return dao.search(po);
	}
	public List<noticerulePo> zdysearch(@Param("po") noticerulePo po){
		return dao.zdysearch(po);
	}
	public int insert(@Param("po")   noticerulePo po) {
		return dao.insert(po);
	}	
	public int update(@Param("po")   noticerulePo po) {
		return dao.update(po);
	}		
	public int delete(@Param("po")   noticerulePo po) {
		return dao.delete(po);
	}
	
	public List<noticerulePo> searchnotice(@Param("po") noticerulePo po){
		return dao.searchnotice(po);
	}
	
	public List<noticerulePo> searchnotice2(@Param("po") noticerulePo po){
		return dao.searchnotice2(po);
	}
}
