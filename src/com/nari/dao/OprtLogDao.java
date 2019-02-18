package com.nari.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.po.OprtLog;
import com.nari.po.SysCodeValue;
import com.nari.po.SysUltravires;

@Repository(value = "oprtLogDao")
public interface OprtLogDao {
	public List<OprtLog> search(@Param("po") OprtLog po);


	public int insert(@Param("po") OprtLog po);
	//越权访问统计
	public List<SysUltravires> findViresUser(@Param("nowtime") String nowtime);

	public int insertLogs(@Param("po") OprtLog  po);
	
	public List<OprtLog> searchbytype(@Param("po")OprtLog po);
	
	//hejueshan
	public List<OprtLog> searchstatus(@Param("po") OprtLog po);
	//end
	
	//hejueshan
	public int updatestatus(@Param("po") String data);
	//end
	
	public List<SysCodeValue> findCodeValue(@Param("code_type")String code_type);
}
