package com.nari.module.indextype.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.general.service.GeneranService;
import com.nari.module.indextype.po.IndextypePo;
import com.nari.module.indextype.service.IndextypeService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping(value="indextypeCon")
public class IndextypeController extends BaseController{
	
	private Logger logger = Logger.getLogger(IndextypeController.class);
	
	@Resource(name="indextypeService")
	private IndextypeService service;
	
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<IndextypePo> search(IndextypePo po, HttpServletRequest request){
		List<IndextypePo> list = null;
		try {
			String inOrOut = Util.getInOrOut(request);
			//判断是否需要添加审计数据
			User userInfo = (User) request.getSession().getAttribute("user");
			boolean result = isAudit("指标分类","查询");
			if(result){
				generservice.insert_oprt_logs("配置管理-指标分类","查询",userInfo.getUsername(),"用户查询指标分类数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
			}
			list = service.search(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	/**
	 * 校验
	 * @param request
	 * @param response
	 * @return String 不为空时返回具体错误描述
	 * @date：2017年5月27日
	 */
	@ResponseBody
	@RequestMapping(value="/checkindexType")
	public String checkindexType(HttpServletRequest req,IndextypePo po) throws Exception{	
		List<IndextypePo> list=service.checkIndexTypeID(po);
		String mess="";
		if(list!=null && list.size()>0){
			mess="指标分类ID重复!";
		}
		return mess;
	}
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated IndextypePo po, BindingResult bindingResult, HttpServletRequest request, String oper) throws Exception{	
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
		int res;
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		if(po.getId() == null && "saveOrUpdate".equals(oper))
		{
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId() == null ? "" : po.getId()).append(po.getIndextype_id()).append(po.getName());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
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
			
			po.setCreate_by(userInfo.getId());
			res = service.insert(po);
			//判断是否需要添加审计数据
			boolean result = isAudit("指标分类","新增");
			if(result){
				generservice.insert_oprt_log("配置管理-指标分类","新增",userInfo.getUsername(),"新增了一条指标分类",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(po.getId() != null && oper.equals("saveOrUpdate"))
		{
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId() == null ? "" : po.getId()).append(po.getIndextype_id()).append(po.getName());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
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
			//修改指标分类ID，同时更新对应的指标项数据
			List<IndextypePo> list=service.searchIndexTypeById(po);
			po.setUpdate_by(userInfo.getId());
			res = service.update(po);
			int oldIndextypeid = list.get(0).getIndextype_id();
			service.updateMonIndex(po, oldIndextypeid);
			generservice.insert_oprt_log("配置管理-指标分类","编辑",userInfo.getUsername(),"修改了一条指标分类",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(userInfo.getId());
			
			//删除指标分类同时删除对应的指标项
			List<IndextypePo> list = service.searchIndexTypeById(po);
			res = service.delete(po);
			int indextypeid = list.get(0).getIndextype_id();
			po.setIndextype_id(indextypeid);
			service.updateMonIndex1(po);
			generservice.insert_oprt_log("配置管理-指标分类","删除",userInfo.getUsername(),"删除了一条指标分类",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else{
			res = 0;
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
