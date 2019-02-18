package com.nari.module.warnlog.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.warnlog.dao.WarnlogDao;
import com.nari.module.warnlog.po.WarnlogPo;

@Service(value="WarnlogService")
public class WarnlogService {
	@Resource(name = "WarnlogDao")
	private  WarnlogDao dao;
	
	public List<WarnlogPo> search(@Param("po") WarnlogPo po){
		return dao.search(po);
	}
	public int update(@Param("po") WarnlogPo po){
		return dao.update(po);
	}
	public List<WarnlogPo> customSearch(@Param("po") WarnlogPo po){
		return dao.customSearch(po);
	}
	
	public List<WarnlogPo> Searchnum(@Param("po") WarnlogPo po){
		return dao.Searchnum(po);
	}
	
	
	public List<WarnlogPo> Searchdev(@Param("po") WarnlogPo po){
		return dao.Searchdev(po);
	}
	
	public int insert(@Param("po") WarnlogPo po){
		return dao.insert(po);
	}
}
