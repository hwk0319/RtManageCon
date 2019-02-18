package com.nari.monitormgt.monihome.active.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.monitormgt.monihome.active.po.ActivePo;
import com.nari.monitormgt.monihome.active.service.ActiveService;
import com.nari.monitormgt.monihome.servers.po.Pager;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.Util;


@Controller
@RequestMapping(value="activeCon")
public class ActiveController extends BaseController {
	
	private Logger logger = Logger.getLogger(ActiveController.class);

	@Resource(name="activeService")
	private ActiveService service;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public Pager search(ActivePo po,Pager pager,HttpServletRequest request) throws Exception{
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		pager = service.search(po);
		return pager;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchList")
	public List<ActivePo> searchList(ActivePo po){
		List<ActivePo> list = null;
		try {
			list = service.searchList(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
}
