package com.nari.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.nari.common.persistence.CrudDao;
import com.nari.common.persistence.MyBatisDao;
import com.nari.po.AlarmDevice;
import com.nari.po.DbConnectPo;
/**
 * 获取数据库配置信息
 * 
 * @author ydd
 *
 */
@MyBatisDao
public interface DbConnectMapper extends CrudDao<DbConnectPo>{
	/*
	 * 获取数据库连接信息List
	 */
	public List<DbConnectPo> getDbconnect();

	public List<DbConnectPo> getAllDbconnect();
	
	public List<DbConnectPo> selectDatabase(@Param("list") List<AlarmDevice> list);
	
	public List<DbConnectPo> selectDatabase_not(@Param("list") List<AlarmDevice> list);

	public int delete(@Param("po") DbConnectPo po);
	public List<DbConnectPo> searchById(@Param("po") DbConnectPo po);
	public List<DbConnectPo> searchByDeviceCodeAndName(@Param("po") DbConnectPo po);
}
