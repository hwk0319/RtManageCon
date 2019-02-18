package com.nari.module.operationlog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.po.OperationlogPo;

@Repository(value="operationlogDao")
public interface OperationlogDao{
	public List<OperationlogPo> search(@Param("po") OperationlogPo po);	
	public List<OperationlogPo> searchCounts(@Param("po") OperationlogPo po);	
	public List<OperationlogPo> searchTypeCounts(@Param("po") OperationlogPo po);	
	public List<AuditPo> searchAudit(@Param("po") AuditPo po);	
	public int insertAudit(@Param("po") AuditPo po);
	public int updateAudit(@Param("po") AuditPo po);
	public int deleteAudit(@Param("po") AuditPo po);	
	public List<AuditPo> searchAuditByErjiname(@Param("po") AuditPo po);	
}
