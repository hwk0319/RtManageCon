package com.nari.module.healthModel.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.healthModel.po.itemmetricPo;
import com.nari.module.healthModel.po.healthmodelPo;

@Repository(value="healthmodelDao")
public interface healthmodelDao {
	public List<healthmodelPo> searchModel(@Param("po") healthmodelPo po);
	public int insertModel(@Param("po") healthmodelPo po);
	public int insertItem(@Param("po") modelitemPo po);
	public int insertMetric(@Param("po") itemmetricPo po);
	public List<modelitemPo> zdyseitem(@Param("sql") String sql);
	public List<itemmetricPo> zdysemetric(@Param("sql") String sql);
	public List<healthmodelPo> zdysehmode(@Param("sql") String sql);
	public int update(@Param("sql") String sql);	
	public List<Map<String,Object>> customSearch(@Param("sql") String sql);
	
	
	public List<healthmodelPo> zdysehmode(@Param("po") healthmodelPo po);
	public int updateModel(@Param("po") healthmodelPo po);
	public int updateItemMetric(@Param("po") itemmetricPo po);
	public int deleteItemMetric(@Param("po") itemmetricPo po);
	public int updateModelItemTotal(@Param("po") itemmetricPo po);
	public List<healthmodelPo> zdysehmodeItemTotal(@Param("po") healthmodelPo po);
	public int updateHealthModelTotal(@Param("po") healthmodelPo po);
	public int updateHealthModelItem(@Param("model_item_id") String model_item_id);
	public int updateHealthModelItemMetric(@Param("model_item_id") String model_item_id);
	public int updateHeathmodelByModelId(@Param("model_id") String model_id);
	public int updateHeathmodelItemByModelId(@Param("model_id") String model_id);
	public int updateHealthmodelItemMetricByModelId(@Param("model_id") String model_id);
	public List<healthmodelPo> queryHealthModelByModelId(@Param("model_id") String model_id);
	public List<modelitemPo> queryHealthmodelItemByModelId(@Param("model_id") String model_id);
	public int queryMaxModelItemId();
	public List<itemmetricPo> queryHealthmodelItemMetricByModelItemId(@Param("po") int model_id);
	public List<Map<String,Object>> queryHealthmodelMetricRule(Map<String,Object> map);
	public List<modelitemPo> zdyseitem(@Param("po") healthmodelPo po);
	public List<modelitemPo> zdyseitemId(@Param("po") healthmodelPo po);
	public int updateItem(@Param("po") modelitemPo po);
	public List<itemmetricPo> zdysemetric(@Param("po") modelitemPo po);
	public int update(@Param("po") healthmodelPo po);
	public List<Map<String,Object>> customSearch(@Param("po") healthmodelPo po);
	public List<modelitemPo> zdyseModelItem(@Param("model_item_id") int model_item_id);
	public List<itemmetricPo> zdyseItemMetric(@Param("po")itemmetricPo po);
	public int delMetric(Map<String,Object> map);
}
