package com.nari.module.general.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.general.po.GeneralPo;
@Repository(value="GeneralDao")
public interface GeneralDao {
	public List<GeneralPo> search(@Param("userid") String userid,@Param("roleids") List<String> roleids);
	public int insertDatas(@Param("sql") String sql);
	public int insert_oprt_log(@Param("module")  String module,@Param("oprt_type")String oprt_type,@Param("oprt_user")String oprt_user,@Param("oprt_content")String oprt_content,@Param("ip")String ip,@Param("oprt_id")int oprt_id,@Param("DBTYPE")String DBTYPE,@Param("position")String position);
	public int insert_oprt_logs(@Param("module")  String module,@Param("oprt_type")String oprt_type,@Param("oprt_user")String oprt_user,@Param("oprt_content")String oprt_content,@Param("ip")String ip,@Param("oprt_id")int oprt_id,@Param("DBTYPE")String DBTYPE,@Param("flag")String flag,@Param("type")String type,@Param("levels")String levels,@Param("position")String position);
}
