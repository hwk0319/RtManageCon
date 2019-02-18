package com.nari.module.heathScore.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.healthModel.po.healthmodelPo;
import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.heathScore.dao.HeathcheckDao;
import com.nari.module.heathScore.po.HeathScoreDetailPo;
import com.nari.module.heathScore.po.HeathcheckDeductPo;
import com.nari.module.heathScore.po.HeathcheckPo;

@Service(value="heathcheckService")
public class HeathcheckService {
	@Resource(name = "heathcheckDao")
	private  HeathcheckDao dao;
	
	public List<HeathcheckPo> search(@Param("po") HeathcheckPo po){
		return dao.search(po);
	}
	
	public int insert(@Param("po") HeathcheckPo po){
		return dao.insert(po);
	}

	public int update(@Param("po") HeathcheckPo po) {
		return dao.update(po);
	}

	public int delete(@Param("po") HeathcheckPo po) {
		return dao.delete(po);
	}
	
	public List<HeathScoreDetailPo> searchDetalByCheckId(@Param("po") HeathScoreDetailPo po){
		return dao.searchDetalByCheckId(po);
	}
	
	public List<healthmodelPo> searchModel(@Param("po") healthmodelPo po){
		return dao.searchModel(po);
	}

	public List<HeathcheckPo> searchModelId() {
		return dao.searchModelId();
	}

	public int updateStatus(@Param("po") HeathcheckPo po) {
		return dao.updateStatus(po);
	}

	public List<modelitemPo> searchModelById(modelitemPo po) {
		return dao.searchModelById(po);
	}

	public List<HeathScoreDetailPo> searchHeathDetail(HeathScoreDetailPo po) {
		return dao.searchHeathDetail(po);
	}

	public List<HeathcheckDeductPo> searchHeathScoreDedect(HeathcheckPo po) {
		return dao.searchHeathScoreDedect(po);
	}

	public List<Map> customSearch(String sql) {
		return dao.customSearch(sql);
	}

	public List<Map> searchRecordTime(Integer health_check_id, String model_id,
			String model_item_id, String record_timef) {
		return dao.searchRecordTime(health_check_id,model_id,model_item_id,record_timef);
	}

	public List<Map> searchHealthModelItemMetricById(String model_item_id) {
		return dao.searchHealthModelItemMetricById(model_item_id);
	}

	public List<Map> searchHealthModelItemById(Integer health_check_id) {
		return dao.searchHealthModelItemById(health_check_id);
	}

	public List<modelitemPo> searchModelByModelId(HeathcheckPo po) {
		return dao.searchModelByModelId(po);
	}

	public List<HeathScoreDetailPo> searchNewHeathDetail(HeathScoreDetailPo hcheckPo) {
		return dao.searchNewHeathDetail(hcheckPo);
	}

}
