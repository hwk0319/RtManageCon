package com.nari.module.warnlog.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.module.warnlog.po.WarnlogPo;
import com.nari.module.warnlog.service.WarnlogService;
import com.nari.po.User;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;

@Controller
@RequestMapping(value="WarnlogCon")
public class WarnlogController extends BaseController {
	 private static Logger logger = Logger.getLogger(WarnlogController.class); 
	@Resource(name="WarnlogService")
	private WarnlogService service;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<WarnlogPo> search(WarnlogPo po, HttpServletRequest request) throws Exception{
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		List<WarnlogPo> list = service.search(po);
		User userInfo = (User) request.getSession().getAttribute("user");
		//判断是否需要添加审计数据
		boolean result = isAudit("异常故障","查询");
		if(result){
			generservice.insert_oprt_logs("异常告警-异常故障","查询",userInfo.getUsername(),"用户查询异常故障数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/update")
	public int update(WarnlogPo po,HttpServletRequest request) throws Exception{
		String inOrOut = Util.getInOrOut(request);
		User userInfo = (User) request.getSession().getAttribute("user");
		int res= service.update(po);
		String device_name=service.search(po).get(0).getDevice_name();
		if(po.getProcess_status()==1){
			//判断是否需要添加审计数据
			boolean result = isAudit("异常故障","确认");
			if(result){
				generservice.insert_oprt_log("异常告警-异常故障","确认",userInfo.getUsername(),"设备名称:"+device_name+"异常告警-已确认",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(po.getProcess_status()==2){
			//判断是否需要添加审计数据
			boolean result = isAudit("异常故障","消除");
			if(result){
				generservice.insert_oprt_log("异常告警-异常故障","消除",userInfo.getUsername(),"设备名称:"+device_name+"异常告警-已消除",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(po.getProcess_status()==3){
			//判断是否需要添加审计数据
			boolean result = isAudit("异常故障","忽略");
			if(result){
				generservice.insert_oprt_log("异常告警-异常故障","忽略",userInfo.getUsername(),"设备名称:"+device_name+"异常告警-已忽略",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}
		return res;
	}
	/**
	 * 获取服务器、双活、集群、一体机故障数量
	 * @date：2017年6月30日
	 */
	@ResponseBody
	@RequestMapping(value="/geterrrorNum")
	public String geterrrorNum(WarnlogPo po){
		String str="";
		try {
			List<WarnlogPo> list=service.Searchnum(po);
			String servNum="",doubNum="",clusNum="",inteNum="";
			if(list!=null && list.size()>0){
				servNum=toString(list.get(0).getServNum());
				doubNum=toString(list.get(0).getDoubNum());
				clusNum=toString(list.get(0).getClusNum());
				inteNum=toString(list.get(0).getInteNum());
			}
			str = "[{'servNum':'"+servNum+"','doubNum':'"+doubNum+"','clusNum':'"+clusNum+"','inteNum':'"+inteNum+"'}]";
			str = Util.xssEncode(str);
		} catch (Exception e) {
			logger.error(e);
		}
		return str;
	}
	
	/**
	 * 获取系统告警页面滚动信息
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getalarm")
	public String getalarm(WarnlogPo po){
		String str="";
		try {
			List<WarnlogPo> list = null;
			po.setPosition(Util.getInOrOut());
			if("ORACLE".equalsIgnoreCase(MyPropertiesPersist.DBTYPE)){
				list = service.Searchdev(po);
			}
			if(list!=null && list.size()>0){
				if("7001".equals(list.get(0).getDevice_id())){
					//日志告警,取最新的一条数据
					str=toString(list.get(0).getWarn_level())+toString(list.get(0).getLogwarn_info())+" 发生日期："+toString(list.get(0).getNewest_warntime());
				}else{
					//设备告警,取最新的一条数据
					str="<span style=\"cursor:pointer;\" onclick=\"jumpWarnLog('"+list.get(0).getDevice_id()+"')\">"+toString(list.get(0).getWarn_level())+toString(list.get(0).getWarn_info())+" 发生日期："+toString(list.get(0).getNewest_warntime())+"</span>";
				}
			}
			str = Util.xssEncode(str);
		} catch (Exception e) {
			logger.error(e);
		}
		return str;
	}
	
	/**
	 * 判断是否需要审计
	 * @param po
	 * @return true 审计，false 不审计
	 */
	@ResponseBody
	@RequestMapping(value="/isAudit")
	public boolean isAudit(String erjiName, String methodName){
		boolean res = false;
		AuditPo po = new AuditPo();
		po.setErjiname(erjiName);
		List<AuditPo> list = operService.searchAuditByErjiname(po);
		if(list != null && list.size() > 0){
			String audit = list.get(0).getIsAudit();
			if(audit.equals("1")){
				String auditthose = list.get(0).getAuditThose();
				if(auditthose.contains(methodName)){
					res = true;
				}
			}
		}
		return res;
	}
}
