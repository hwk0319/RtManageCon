package com.nari.monitormgt.monihome.group.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.monitormgt.monihome.group.dao.GroupsDao;
import com.nari.monitormgt.monihome.group.po.GroupsPo;
import com.nari.monitormgt.monihome.servers.po.Pager;

@Service(value="groupsService")
public class GroupsService {
	@Resource(name = "groupsDao")
	private  GroupsDao dao;
	
	public Pager search(@Param("po") GroupsPo po){
	Pager pager = new Pager();
	//查询总记录数
	int total = dao.searchTotal(po);
	int pageCount = 0;
	//计算总页数
	if(total > 0){
	    pageCount = total % po.getPageSize() == 0 ? total / po.getPageSize() : total / po.getPageSize() + 1;
	}
	//计算从第几条记录开始查询
	if(po.getDBTYPE().equals("ORACLE")){
		int startTotal = (po.getPageNum() - 1) * po.getPageSize() + 1;
		int endTotal = startTotal + po.getPageSize() - 1;
		po.setStartTotal(startTotal);
		po.setEndTotal(endTotal);
	}else{
		int startTotal = (po.getPageNum() - 1) * po.getPageSize();
		po.setStartTotal(startTotal);
	}
	pager.setRows(dao.search(po));
	pager.setTotal(total);
	pager.setPageSize(po.getPageSize());
	pager.setPageNo(po.getPageNum());
	pager.setPageCount(pageCount);
	return pager;
}
	
	public int searchTotal(@Param("po") GroupsPo po){
		return dao.searchTotal(po);
	}
	
	public List<GroupsPo> searchList(@Param("po") GroupsPo po){
		return dao.searchList(po);
	};

}
