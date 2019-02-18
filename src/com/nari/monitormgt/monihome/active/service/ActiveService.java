package com.nari.monitormgt.monihome.active.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.monitormgt.monihome.active.dao.ActiveDao;
import com.nari.monitormgt.monihome.active.po.ActivePo;
import com.nari.monitormgt.monihome.servers.po.Pager;

@Service(value="activeService")
public class ActiveService {
	@Resource(name = "ActiveDao")
	private  ActiveDao dao;
	
	public Pager search(@Param("po") ActivePo po){
	Pager pager = new Pager();
	//查询总记录数
	int total = dao.searchTotal(po);
	int pageCount = 0;
	//计算总页数
	if(total > 0){
	    pageCount = total % po.getPageSize() == 0 ? total / po.getPageSize() : total / po.getPageSize() + 1;
	}
	//计算从第几条记录开始查询
	int startTotal = (po.getPageNum() - 1) * po.getPageSize();
	po.setStartTotal(startTotal);
	pager.setRows(dao.search(po));
	pager.setTotal(total);
	pager.setPageSize(po.getPageSize());
	pager.setPageNo(po.getPageNum());
	pager.setPageCount(pageCount);
	return pager;
}
	
	public int searchTotal(@Param("po") ActivePo po){
		return dao.searchTotal(po);
	}
	
	public List<ActivePo> searchList(@Param("po") ActivePo po){
		return dao.searchList(po);
	};

}
