package com.nari.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nari.common.service.CrudService;
import com.nari.dao.SysParamDao;
import com.nari.po.SysParamPo;

@Service
@Transactional(readOnly = true)
public class SysParamService extends CrudService<SysParamDao, SysParamPo>{
	
}
