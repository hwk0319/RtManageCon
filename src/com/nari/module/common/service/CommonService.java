package com.nari.module.common.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.common.dao.CommonDao;
import com.nari.module.common.po.CommonDictPo;
import com.nari.module.common.po.ViewUidPo;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.indextype.po.IndexPo;
import com.nari.module.indextype.po.IndextypePo;
import com.nari.module.model.po.ModelPo;

@Service(value="commonService")
public class CommonService {
	@Resource(name = "commonDao")
	private  CommonDao dao;
	
	public List<CommonDictPo> search(@Param("po") CommonDictPo po){
		return dao.search(po);
	}
	
	public List<ModelPo> searchModel(@Param("po") ModelPo po){
		return dao.searchModel(po);
	}
	public List<ModelPo> searchModelByFactory(@Param("po") ModelPo po){
		return dao.searchModelByFactory(po);
	}
	
	public List<DevicesPo> searchServer(@Param("po") DevicesPo po){
		return dao.searchServer(po);
	}
	
	public List<IndextypePo> searchIndexType(@Param("po") IndextypePo po){
		return dao.searchIndexType(po);
	}

	public List<CommonDictPo> searchSysType(@Param("po") CommonDictPo po){
		return dao.searchSysType(po);
	}
	public List<IndexPo> searchIndex(IndexPo po){
		return dao.searchIndex(po);
	}
	
	public List<ViewUidPo> searchViewByUid(ViewUidPo po){
		return dao.searchViewByUid(po);
	}
	public List<CommonDictPo> customSearch(@Param("po") CommonDictPo po){
		return dao.customSearch(po);
	}
}
