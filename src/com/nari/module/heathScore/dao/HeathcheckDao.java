package com.nari.module.heathScore.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.healthModel.po.healthmodelPo;
import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.heathScore.po.HeathScoreDetailPo;
import com.nari.module.heathScore.po.HeathcheckDeductPo;
import com.nari.module.heathScore.po.HeathcheckPo;

@Repository(value="heathcheckDao")
public interface HeathcheckDao{
	public List<HeathcheckPo> search(@Param("po") HeathcheckPo po);	
	public int insert(@Param("po") HeathcheckPo po);
	public int update(@Param("po") HeathcheckPo po);
	public int delete(@Param("po") HeathcheckPo po);
	public List<HeathScoreDetailPo> searchDetalByCheckId(@Param("po") HeathScoreDetailPo po);
	public List<healthmodelPo> searchModel(@Param("po") healthmodelPo po);
	public List<HeathcheckPo> searchModelId();
	public int updateStatus(@Param("po") HeathcheckPo po);
	public List<modelitemPo> searchModelById(@Param("po") modelitemPo po);
	public List<HeathScoreDetailPo> searchHeathDetail(@Param("po") HeathScoreDetailPo po);
	public List<HeathcheckDeductPo> searchHeathScoreDedect(@Param("po") HeathcheckPo po);
	public List<Map> customSearch(@Param("sql")String sql);
	public List<Map> searchRecordTime(@Param("health_check_id")Integer health_check_id, @Param("model_id")String model_id,
			@Param("model_item_id")String model_item_id, @Param("record_time")String record_time);
	public List<Map> searchHealthModelItemMetricById( @Param("model_item_id")String model_item_id);
	public List<Map> searchHealthModelItemById( @Param("health_check_id")Integer health_check_id);
	public List<modelitemPo> searchModelByModelId( @Param("po")HeathcheckPo po);
	public List<HeathScoreDetailPo> searchNewHeathDetail( @Param("po")HeathScoreDetailPo hcheckPo);
}
