package com.nari.module.healthModel.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.healthModel.dao.healthmodelDao;
import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.healthModel.po.itemmetricPo;
import com.nari.module.healthModel.po.healthmodelPo;

@Service(value="healthmodelService")
public class healthmodelService {
	@Resource(name = "healthmodelDao")
	private  healthmodelDao dao;
	/**** 健康模型相关处理方法-start ****/
	public List<healthmodelPo> searchModel(@Param("po") healthmodelPo po){
		return dao.searchModel(po);
	}
	public List<healthmodelPo> zdysehmode(@Param("sql") String sql){
		return dao.zdysehmode(sql);
	}
	public int insertModel(@Param("po") healthmodelPo po){
		return dao.insertModel(po);
	}
	/**** 健康模型相关处理方法-end ****/
	
	/**** 模型分项相关处理方法-start ****/
	public List<modelitemPo> zdyseitem(@Param("sql") String sql){
		return dao.zdyseitem(sql);
	}
	public int insertItem(@Param("po") modelitemPo po){
		return dao.insertItem(po);
	}
	/**** 模型分项相关处理方法-end ****/
	
	/**** 模型分项指标相关处理方法-start ****/
	public List<itemmetricPo> zdysemetric(@Param("sql") String sql){
		return dao.zdysemetric(sql);
	}
	public int insertMetric(@Param("po") itemmetricPo po){
		return dao.insertMetric(po);
	}
	/**** 模型分项指标相关处理方法-end ****/
	
	public int update(@Param("sql") String sql){
		return dao.update(sql);
	}	
	
	public List<Map<String,Object>> customSearch(@Param("sql") String sql){
		return dao.customSearch(sql);
	}
	
	/*************************************************************
	 * 
	 * @param po
	 * @return
	 */
	
	public List<healthmodelPo> zdysehmode(healthmodelPo po){
		return dao.zdysehmode(po);
	}
	public int updateModel(healthmodelPo po){
		return dao.updateModel(po);
	}
	public int updateItemMetric(itemmetricPo po){
		return dao.updateItemMetric(po);
	}
	public int deleteItemMetric(itemmetricPo po){
		return dao.deleteItemMetric(po);
	}
	public int updateModelItemTotal(itemmetricPo po){
		return dao.updateModelItemTotal(po);
	}
	public List<healthmodelPo> zdysehmodeItemTotal(healthmodelPo po){
		return dao.zdysehmodeItemTotal(po);
	}
	public int updateHealthModelTotal(healthmodelPo po){
		return dao.updateHealthModelTotal(po);
	}
	public int updateHealthModelItem(String model_item_id){
		return dao.updateHealthModelItem(model_item_id);
	}
	public int updateHealthModelItemMetric(String model_item_id){
		return dao.updateHealthModelItemMetric(model_item_id);
	}
	public int updateHeathmodelByModelId(String model_id){
		return dao.updateHeathmodelByModelId(model_id);
	}
	public int updateHeathmodelItemByModelId(String model_id){
		return dao.updateHeathmodelItemByModelId(model_id);
	}
	public int updateHealthmodelItemMetricByModelId(String model_id){
		return dao.updateHealthmodelItemMetricByModelId(model_id);
	}
	public List<healthmodelPo> queryHealthModelByModelId(String model_id){
		return dao.queryHealthModelByModelId(model_id);
	}
	public List<modelitemPo> queryHealthmodelItemByModelId(String model_id){
		return dao.queryHealthmodelItemByModelId(model_id);
	}
	public int queryMaxModelItemId(){
		return dao.queryMaxModelItemId();
	}
	public List<itemmetricPo> queryHealthmodelItemMetricByModelItemId(int model_id){
		return dao.queryHealthmodelItemMetricByModelItemId(model_id);
	}
	public List<Map<String,Object>> queryHealthmodelMetricRule(Map<String,Object> map){
		return dao.queryHealthmodelMetricRule(map);
	}
	public int delMetric(Map<String,Object> map){
		return dao.delMetric(map);
	}
	
	/**** 健康模型相关处理方法-end ****/
	
	/**** 模型分项相关处理方法-start ****/
	public List<modelitemPo> zdyseitem(healthmodelPo po){
		return dao.zdyseitem(po);
	}
	public List<modelitemPo> zdyseitemId(healthmodelPo po){
		return dao.zdyseitemId(po);
	}
	public int updateItem(modelitemPo po){
		return dao.updateItem(po);
	}
	/**** 模型分项相关处理方法-end ****/
	
	/**** 模型分项指标相关处理方法-start ****/
	public List<itemmetricPo> zdysemetric(modelitemPo po){
		return dao.zdysemetric(po);
	}
	/**** 模型分项指标相关处理方法-end ****/
	
	public List<Map<String,Object>> customSearch(healthmodelPo po){
		return dao.customSearch(po);
	}
	public List<modelitemPo> zdyseModelItem(int model_item_id){
		return dao.zdyseModelItem(model_item_id);
	}
	public List<itemmetricPo> zdyseItemMetric(itemmetricPo po){
		return dao.zdyseItemMetric(po);
	}
}
