package com.nari.module.warnrule.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.warnrule.dao.WarnruleDao;
import com.nari.module.warnrule.po.WarnrulePo;
import com.nari.taskmanager.po.TaskOperation;

@Service(value="warnruleService")
public class WarnruleService {
	@Resource(name = "warnruleDao")
	private  WarnruleDao dao;
	
	public List<WarnrulePo> search(@Param("po") WarnrulePo po){
		return dao.search(po);
	}
	public List<WarnrulePo> searchTask(@Param("po") WarnrulePo po){
		return dao.searchTask(po);
	}
	public int insert(@Param("po")   WarnrulePo po) {
		return dao.insert(po);
	}	
	
	public int update(@Param("po")   WarnrulePo po) {
		return dao.update(po);
	}		

	public int delete(@Param("po")   WarnrulePo po) {
		return dao.delete(po);
	}

}
