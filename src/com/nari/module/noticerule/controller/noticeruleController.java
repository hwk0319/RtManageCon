package com.nari.module.noticerule.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.module.deviceindex.controller.DeviceindexController;
import com.nari.module.general.service.GeneranService;
import com.nari.module.noticerule.po.noticerulePo;
import com.nari.module.noticerule.service.noticeruleService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;
/**
 * 通知规则
 * @date：2017年5月16日
 */
@Controller
@RequestMapping(value="noticeruleCon")
public class noticeruleController {
	@Resource(name="noticeruleService")
	private noticeruleService service;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<noticerulePo> search(noticerulePo po, HttpServletRequest request) throws Exception{
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		String inOrOut = Util.getInOrOut(request);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("通知规则","查询");
		if(result){
			generservice.insert_oprt_logs("配置管理-通知规则","查询",userInfo.getUsername(),"用户查询通知规则数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return service.search(po);
	}
	/**
	 * 校验
	 * @param request
	 * @param response
	 * @return String 不为空时返回具体错误描述
	 * @date：2017年5月27日
	 */
	@ResponseBody
	@RequestMapping(value="/checknoticerule")
	public String checkDevIndex(HttpServletRequest req,noticerulePo po) throws Exception{	
		List<noticerulePo> list = new ArrayList<>();
		if(po.getId()==null){//新增
			list=service.searchnotice(po);
		}else{//修改
			list=service.searchnotice2(po);
		}
		String mess="";
		if(list!=null && list.size()>0){
			mess="规则编码重复!";
		}
		return mess;
	}
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated noticerulePo po, BindingResult bindingResult, HttpServletRequest request, String oper) throws Exception{
		Map<String,String> map=new HashMap<String,String>();
		//判断是否重复提交
		if(!Util.getSessionJWT(request, po.getJwt())){
			map.put("errors", SysConstant.CHONTFUTIJIOA);
			return map;
		}
		User userInfo = (User) request.getSession().getAttribute("user");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			po.setCreate_date(date2);
			po.setUpdate_date(date2);
		} catch (Exception e) {
		}
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		String inOrOut = Util.getInOrOut(request);
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		int res=0;
		if(oper != null && oper.equals("add"))
		{
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getType()).append(po.getLevel()).append(po.getWay()).append(po.getAddress_id());
			String newMdata = MD5.GetMD5Code(sb.toString());
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			StringBuffer sb1 = new StringBuffer();
			//数据校验
			if (bindingResult.hasErrors()) {
				List<ObjectError> ErrorList = bindingResult.getAllErrors();
				for(ObjectError objectError:ErrorList){
					sb1.append(objectError.getDefaultMessage()).append("</br>");
				}    
			}
			if(sb1.toString() != null && !"".equals(sb1.toString())){
				map.put("errors", sb1.toString());
				return map;
			}
			
			po.setCreate_by(String.valueOf(userInfo.getId()));
			if(po.getId()==null){
				res = service.insert(po);
			}
			//判断是否需要添加审计数据
			boolean result = isAudit("通知规则","新增");
			if(result){
				generservice.insert_oprt_log("配置管理-通知规则","新增",userInfo.getUsername(),"新增了一条通知规则记录",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(oper != null && oper.equals("edit"))
		{
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId()).append(po.getType()).append(po.getLevel()).append(po.getWay()).append(po.getAddress_id());
			String newMdata = MD5.GetMD5Code(sb.toString());
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			StringBuffer sb1 = new StringBuffer();
			//数据校验
			if (bindingResult.hasErrors()) {
				List<ObjectError> ErrorList = bindingResult.getAllErrors();
				for(ObjectError objectError:ErrorList){
					sb1.append(objectError.getDefaultMessage()).append("</br>");
				}    
			}
			if(sb1.toString() != null && !"".equals(sb1.toString())){
				map.put("errors", sb1.toString());
				return map;
			}
			
			po.setUpdate_by(String.valueOf(userInfo.getId()));
			res = service.update(po);
			generservice.insert_oprt_log("配置管理-通知规则","编辑",userInfo.getUsername(),"修改了一条通知规则记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(String.valueOf(userInfo.getId()));
			res = service.delete(po);
			generservice.insert_oprt_log("配置管理-通知规则","删除",userInfo.getUsername(),"删除了一条通知规则记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}
		//保存/编辑成功移除jwt
		request.getSession().removeAttribute("jwt");
		if(res != 0){
			map.put("success", SysConstant.SAVE_SUCCESS);
		}else{
			map.put("success", SysConstant.SAVE_FILE);
		}
		return map;
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
