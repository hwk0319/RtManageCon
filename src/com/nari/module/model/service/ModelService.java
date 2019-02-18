package com.nari.module.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.model.dao.ModelDao;
import com.nari.module.model.po.ModelPo;

@Service(value="modelService")
public class ModelService {
	@Resource(name = "modelDao")
	private  ModelDao dao;
	
	public List<ModelPo> search(@Param("po") ModelPo po){
		return dao.search(po);
	}
	
	public int insert(@Param("po")   ModelPo po) {
		return dao.insert(po);
	}	
	
	public int update(@Param("po")   ModelPo po) {
		return dao.update(po);
	}		

	public int delete(@Param("po")   ModelPo po) {
		return dao.delete(po);
	}
}
