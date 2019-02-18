package com.nari.module.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.common.po.CommonDictPo;
import com.nari.module.common.po.ViewUidPo;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.indextype.po.IndexPo;
import com.nari.module.indextype.po.IndextypePo;
import com.nari.module.model.po.ModelPo;
import com.nari.module.systemmgt.po.SystemmgtPo;

@Repository(value="commonDao")
public interface CommonDao{
	public List<CommonDictPo> search(@Param("po") CommonDictPo po);	
	public List<ModelPo> searchModel(@Param("po") ModelPo po);	
	public List<ModelPo> searchModelByFactory(@Param("po") ModelPo po);	
	public List<DevicesPo> searchServer(@Param("po") DevicesPo po);
	public List<IndextypePo> searchIndexType(@Param("po") IndextypePo po);
	public List<IndexPo> searchIndex(@Param("po") IndexPo po);
	public List<ViewUidPo> searchViewByUid(@Param("po") ViewUidPo po);
	
	public List<CommonDictPo> searchSysType(@Param("po") CommonDictPo po);
	public List<CommonDictPo> customSearch(@Param("po") CommonDictPo po);
}
