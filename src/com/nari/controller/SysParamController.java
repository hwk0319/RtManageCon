package com.nari.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.po.SysParamPo;
import com.nari.service.SysParamService;

@Controller
@RequestMapping(value="sysParamCon")
public class SysParamController{
	
	@Autowired
	private SysParamService service;

	@ResponseBody
	@RequestMapping(value="/search")	
	public List<SysParamPo> search(SysParamPo po){
		List<SysParamPo> list = service.search(po);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchGrid", method = RequestMethod.POST)	
	public List<SysParamPo> searchGrid(SysParamPo po){
		return search(po);

	}	
	
	@ResponseBody
	@RequestMapping(value="/save")	
	public int save(SysParamPo po){
		return	service.save(po);
		//return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/insert")	
	public int insert(SysParamPo po){
		return	service.save(po);
		//return list;
	}
}
