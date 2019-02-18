package com.nari.monitormgt.monihome.doublemon.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.doublemgt.po.DoublemgtPo;
import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.heathScore.po.HeathScoreDetailPo;
import com.nari.module.heathScore.po.HeathcheckPo;
import com.nari.module.paramdb.po.paramdbPo;
import com.nari.monitormgt.monihome.doublemon.po.doublemonPo;
import com.nari.taskmanager.po.TaskOperation;
@Repository(value="doublemonDao")
public interface doublemonDao {
	public List<Map> search(@Param("sql") String sql);

	public List<DoublemgtPo> getDoubleInfo(@Param("po") DoublemgtPo po);

	public List<DoublemgtPo> getCoreInfo(@Param("po") DoublemgtPo po);
	
	public int insert(@Param("po") doublemonPo po);
	
	public int select(@Param("po") doublemonPo po);
	
	public int update(@Param("po") doublemonPo po);

	public List<paramdbPo> getTablespace(@Param("po") doublemonPo po);

	public List<Map> searchDoubleById(@Param("double_id") String double_id);

	public List<Map> searchDoubleCoreById(@Param("core_id") String core_id);

	public List<Map> searchWarnlogByDevUid(@Param("device_uid") String device_uid);

	public List<Map> searchZhuBeiKuStatus(@Param("ids") String[] ids);

	public List<Map> searchScn(@Param("ids") String[] ids);

	public List<Map> searchTaskInfo(@Param("double_id") int double_id);

	public List<Map> searchTaskInfos(@Param("double_id") int double_id);

	public List<doublemonPo> getDatebase(@Param("po") doublemonPo po);

	public List<TaskOperation> getTaskOperation(@Param("double_id") int double_id);
}
