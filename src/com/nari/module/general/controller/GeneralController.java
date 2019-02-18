package com.nari.module.general.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nari.controller.BaseController;
import com.nari.module.general.service.GeneranService;

@Controller
@RequestMapping(value="GeneralCon")
public class GeneralController extends BaseController {
	
	@Resource(name = "GeneranService")
	private   GeneranService service;

}
