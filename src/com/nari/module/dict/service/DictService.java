package com.nari.module.dict.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.dict.dao.DictDao;
import com.nari.module.dict.po.DictPo;

@Service(value="dictService")
public class DictService {
	@Resource(name = "dictDao")
	private  DictDao dao;
	
	public List<DictPo> search(@Param("po") DictPo po){
		return dao.search(po);
	}
	
	public List<DictPo> customSql(@Param("po") DictPo po){
		return dao.customSql(po);
	}
	
	public int insert(@Param("po")   DictPo po) {
		return dao.insert(po);
	}	
	
	public int update(@Param("po")   DictPo po) {
		return dao.update(po);
	}		

	public int delete(@Param("po")   DictPo po) {
		return dao.delete(po);
	}

}
