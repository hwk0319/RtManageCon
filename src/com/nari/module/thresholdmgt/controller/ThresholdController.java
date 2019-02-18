package com.nari.module.thresholdmgt.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.general.service.GeneranService;
import com.nari.module.noticerule.po.noticerulePo;
import com.nari.module.thresholdmgt.po.ThresholdPo;
import com.nari.module.thresholdmgt.service.ThresholdService;
import com.nari.po.User;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.Util;

@Controller
@RequestMapping(value="thresholdCon")
public class  ThresholdController extends BaseController{
	
	private Logger logger = Logger.getLogger(ThresholdController.class);
	
	@Resource(name=" thresholdService")
	private  ThresholdService service;
	/*
	 * 查询阈值列表
	 */
	@ResponseBody
	@RequestMapping(value="/search")
	public List<ThresholdPo> search(ThresholdPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<ThresholdPo> list = service.search(po);
		return list;
	}
	
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	/*
	 * 查询模型列表
	 */
	@ResponseBody
	@RequestMapping(value="/searchmodel")
	public List<ThresholdPo> searchmodel(ThresholdPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchmodel(po);
	}
	/*
	 * 查询模型分项列表
	 */
	@ResponseBody
	@RequestMapping(value="/searchmodelitem")
	public List<ThresholdPo> searchmodelitem(ThresholdPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchmodelitem(po);
	}
	/*
	 * 查询指标项列表
	 */
	@ResponseBody
	@RequestMapping(value="/searchindex")
	public List<ThresholdPo> searchindex(ThresholdPo po, String oper){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		String sql="";
		if(oper.equals("add")){
			sql=" and metric_id not in (select metric_id from h_metric_rule where model_item_id='"+po.getModel_item_id()+"' and use_flag='1')";
		}else{
			sql=" AND A.metric_id ='"+po.getIndex_id()+"' ";
		}
			
		po.setSql(sql);
		return service.searchindex(po);
	}
	/*
	 * 查询模型状态
	 */
	@ResponseBody
	@RequestMapping(value="/searchstatus")
	public List<ThresholdPo> searchstatus(ThresholdPo po){
		List<ThresholdPo> list = null;
		try {
			list = service.searchstatus(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/update")
	public int update(ThresholdPo po, HttpServletRequest request, String oper) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		String inOrOut = Util.getInOrOut(request);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			po.setCreate_date(date2);
			po.setUpdate_date(date2);
		} catch (Exception e) {
		}
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		int res=0;
		if("".equals(po.getMethod1())){
			po.setMethod1(null);
		}
		if("".equals(po.getMethod2())){
			po.setMethod2(null);
		}
		if("".equals(po.getMethod3())){
			po.setMethod3(null);
		}
		
		if(oper != null && oper.equals("saveOrUpdate")){
			if(po.getRule_id()==null){
				po.setCreate_by(String.valueOf(userInfo.getId()));
				res = service.insert(po);
				generservice.insert_oprt_log("基线管理-指标阈值","新增",userInfo.getUsername(),"新增了一条指标阈值记录",userInfo.getIpaddr(),po.getRule_id(),inOrOut);
			}else if(po.getRule_id()!=null){
				po.setUpdate_by(String.valueOf(userInfo.getId()));
				res = service.update(po);
				generservice.insert_oprt_log("基线管理-指标阈值","编辑",userInfo.getUsername(),"修改了一条指标阈值记录",userInfo.getIpaddr(),po.getRule_id(),inOrOut);
			}
		}else if(oper != null && oper.equals("del")){
			po.setUpdate_by(String.valueOf(userInfo.getId()));
			int rule_id=po.getId();
			res = service.delete(po);
			generservice.insert_oprt_log("基线管理-指标阈值","删除",userInfo.getUsername(),"删除了一条指标阈值记录",userInfo.getIpaddr(),rule_id,inOrOut);
		}else{
			res=0;
		}
		return res;
	}
}
