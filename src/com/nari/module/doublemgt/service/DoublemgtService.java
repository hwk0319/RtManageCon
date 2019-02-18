package com.nari.module.doublemgt.service;


import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.doublemgt.dao.DoublemgtDao;
import com.nari.module.doublemgt.po.CorePo;
import com.nari.module.doublemgt.po.DoublemgtPo;

@Service(value="doublemgtService")
public class DoublemgtService {
	 @Resource(name="doublemgtDao")
	    private DoublemgtDao dao;
	  public List<DoublemgtPo> search(@Param("po") DoublemgtPo po){
			return dao.search(po);
		}
		public int insert(@Param("po")   DoublemgtPo po) {
			return dao.insert(po);
		}
		public int deletecore(@Param("po")   DoublemgtPo po) {
			return dao.deletecore(po);
		}		
		public int deletecoregroup(@Param("po")   DoublemgtPo po) {
			return dao.deletecoregroup(po);
		}		
		public int update(@Param("po")   DoublemgtPo po) {
			return dao.update(po);
		}		
		public int delete(@Param("po")   DoublemgtPo po) {
			return dao.delete(po);
		}
		public List<DoublemgtPo> search_group_core(@Param("po") DoublemgtPo po){
			return dao.search_group_core(po);
		}
		public int insertcore(@Param("po")   CorePo po) {
			return dao.insertcore(po);
		}
		public int deletegroup(@Param("po")   CorePo po) {
			return dao.deletegroup(po);
		}
		public int insertcoreGroup(@Param("po")   CorePo po) {
			return dao.insertcoreGroup(po);
		}
		public int updatecore(@Param("po")   CorePo po) {
			return dao.updatecore(po);
		}	
}
