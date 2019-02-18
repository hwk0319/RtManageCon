package com.nari.module.general.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.general.dao.GeneralDao;
import com.nari.module.general.po.GeneralPo;
import com.nari.util.MyPropertiesPersist;
@Service(value="GeneranService")
public class GeneranService{
	@Resource(name = "GeneralDao")
	private  GeneralDao dao;
	public List<GeneralPo> search(String userid,List<String> roleids){
		return dao.search(userid,roleids);
	}
	public int insertDatas(@Param("sql") String sql){
		return dao.insertDatas(sql);
	}
	public int insert_oprt_log(String module, String oprt_type, String oprt_user, String oprt_content, String ip, int oprt_id,String position){
		return dao.insert_oprt_log(module,oprt_type,oprt_user,oprt_content,ip,oprt_id,MyPropertiesPersist.DBTYPE,position);
	}	
	public int insert_oprt_logs(String module, String oprt_type, String oprt_user, String oprt_content, String ip, int oprt_id,String flag,String type,String levels,String position){
		return dao.insert_oprt_logs(module,oprt_type,oprt_user,oprt_content,ip,oprt_id,MyPropertiesPersist.DBTYPE,flag,type,levels,position);
	}	
	
}
