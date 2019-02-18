package com.nari.monitormgt.monihome.group.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.monitormgt.monihome.group.po.GroupsPo;
import com.nari.monitormgt.monihome.group.service.GroupsService;
import com.nari.monitormgt.monihome.servers.po.Pager;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.Util;


@Controller
@RequestMapping(value="groupsCon")
public class GruopsController extends BaseController {
	
	private Logger logger = Logger.getLogger(GruopsController.class);

	@Resource(name="groupsService")
	private GroupsService service;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public Pager search(GroupsPo po,Pager pager,HttpServletRequest request) throws Exception{
		if(pager.getPageNo() != null){
			po.setPageNum(pager.getPageNo());
		}
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		pager = service.search(po);
		return pager;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchList")
	public List<GroupsPo> searchList(GroupsPo po){
		List<GroupsPo> list = null;
		try {
			list = service.searchList(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
}
