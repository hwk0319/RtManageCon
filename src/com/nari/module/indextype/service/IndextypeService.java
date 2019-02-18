package com.nari.module.indextype.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.indextype.dao.IndextypeDao;
import com.nari.module.indextype.po.IndextypePo;


@Service(value="indextypeService")
public class IndextypeService {
	 @Resource(name="indextypeDao")
	    private IndextypeDao dao;
	 
	  public List<IndextypePo> search(@Param("po") IndextypePo po){
			return dao.search(po);
		}
		public int insert(@Param("po")   IndextypePo po) {
			return dao.insert(po);
		}
		public List<IndextypePo> zdysearch(@Param("po") IndextypePo po){
			return dao.zdysearch(po);
		}
		public int update(@Param("po")   IndextypePo po) {
			return dao.update(po);
		}		
		public int delete(@Param("po")   IndextypePo po) {
			return dao.delete(po);
		}
		public List<IndextypePo> checkIndexTypeID(IndextypePo po) {
			return dao.checkIndexTypeID(po);
		}
		public List<IndextypePo> searchIndexTypeById(IndextypePo po) {
			return dao.searchIndexTypeById(po);
		}
		public int updateMonIndex(IndextypePo po, int oldIndextypeid) {
			return dao.updateMonIndex(po, oldIndextypeid);
		}
		public int updateMonIndex1(IndextypePo po) {
			return dao.updateMonIndex1(po);
		}
}
