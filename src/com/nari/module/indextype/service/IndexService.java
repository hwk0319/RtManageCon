package com.nari.module.indextype.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.indextype.dao.IndexDao;
import com.nari.module.indextype.po.IndexPo;

@Service(value="indexService")
public class IndexService {

	 @Resource(name="indexDao")
	    private IndexDao dao;
	 
	    public List<IndexPo> search(@Param("po") IndexPo po){
			return dao.search(po);
		}
	 public List<IndexPo> searchindex(@Param("po") IndexPo po){
			return dao.searchindex(po);
		}
		public int insert(@Param("po")   IndexPo po) {
			return dao.insert(po);
		}
		public int insertindex(@Param("po")   IndexPo po) {
			return dao.insertindex(po);
		}
		public int update(@Param("po")   IndexPo po) {
			return dao.update(po);
		}		
		public int delete(@Param("po")   IndexPo po) {
			return dao.delete(po);
		}
}
