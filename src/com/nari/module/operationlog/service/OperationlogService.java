package com.nari.module.operationlog.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.operationlog.dao.OperationlogDao;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.po.OperationlogPo;

@Service(value="operationlogService")
public class OperationlogService {
	@Resource(name = "operationlogDao")
	private  OperationlogDao dao;
	
	public List<OperationlogPo> search(@Param("po") OperationlogPo po){
		List<OperationlogPo> list = dao.search(po);
		return list;
	}
	public List<OperationlogPo> searchCounts(@Param("po") OperationlogPo po){
		List<OperationlogPo> list = dao.searchCounts(po);
		return list;
	}
	public List<OperationlogPo> searchTypeCounts(@Param("po") OperationlogPo po){
		List<OperationlogPo> list = dao.searchTypeCounts(po);
		return list;
	}
	public List<AuditPo> searchAudit(@Param("po") AuditPo po){
		List<AuditPo> list = dao.searchAudit(po);
		return list;
	}
	public List<AuditPo> searchAuditByErjiname(@Param("po") AuditPo po){
		List<AuditPo> list = dao.searchAuditByErjiname(po);
		return list;
	}
	public int insertAudit(@Param("po") AuditPo po){
		return dao.insertAudit(po);
	}
	public int updateAudit(AuditPo po) {
		return dao.updateAudit(po);
	}
	public int deleteAudit(AuditPo po) {
		return dao.deleteAudit(po);
	}
}
