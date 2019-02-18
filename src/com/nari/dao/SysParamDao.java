package com.nari.dao;

import com.nari.common.persistence.CrudDao;
import com.nari.common.persistence.MyBatisDao;
import com.nari.po.SysParamPo;

@MyBatisDao
public interface SysParamDao extends CrudDao<SysParamPo> {

	public SysParamPo findByCode(String code);
}
