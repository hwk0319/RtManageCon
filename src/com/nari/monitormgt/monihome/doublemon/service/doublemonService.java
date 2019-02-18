package com.nari.monitormgt.monihome.doublemon.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.doublemgt.po.DoublemgtPo;
import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.heathScore.po.HeathScoreDetailPo;
import com.nari.module.heathScore.po.HeathcheckPo;
import com.nari.module.paramdb.po.paramdbPo;
import com.nari.monitormgt.monihome.doublemon.dao.doublemonDao;
import com.nari.monitormgt.monihome.doublemon.po.doublemonPo;
import com.nari.taskmanager.po.TaskOperation;

@Service(value="doublemonService")
public class doublemonService {
	@Resource(name = "doublemonDao")
	private  doublemonDao dao;
	public List<Map> search(@Param("sql") String sql){
		return dao.search(sql);
	}
	
	public List<DoublemgtPo> getDoubleInfo(DoublemgtPo po) {
		return dao.getDoubleInfo(po);
	}

	public List<DoublemgtPo> getCoreInfo(DoublemgtPo po) {
		return dao.getCoreInfo(po);
	}
	
	public int insert(@Param("po") doublemonPo po) {
		return dao.insert(po);
	}	
	
	public int select(@Param("po") doublemonPo po) {
		return dao.select(po);
	}		

	public int update(@Param("po") doublemonPo po) {
		return dao.update(po);
	}

	public List<paramdbPo> getTablespace(doublemonPo po) {
		return dao.getTablespace(po);
	}

	public List<Map> searchDoubleById(String double_id) {
		return dao.searchDoubleById(double_id);
	}

	public List<Map> searchDoubleCoreById(String core_id) {
		return dao.searchDoubleCoreById(core_id);
	}

	public List<Map> searchWarnlogByDevUid(String device_uid) {
		return dao.searchWarnlogByDevUid(device_uid);
	}

	public List<Map> searchZhuBeiKuStatus(String[] ids) {
		return dao.searchZhuBeiKuStatus(ids);
	}

	public List<Map> searchScn(String[] ids) {
		return dao.searchScn(ids);
	}

	public List<Map> searchTaskInfo(int double_id) {
		return dao.searchTaskInfo(double_id);
	}

	public List<Map> searchTaskInfos(int double_id) {
		return dao.searchTaskInfos(double_id);
	}

	public List<doublemonPo> getDatebase(doublemonPo po) {
		return dao.getDatebase(po);
	}

	public List<TaskOperation> getTaskOperation(int double_id) {
		return dao.getTaskOperation(double_id);
	}
}
