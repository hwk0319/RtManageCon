package com.nari.module.dict.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

import com.nari.controller.BaseController;
import com.nari.module.dict.po.DictPo;
import com.nari.module.dict.service.DictService;
import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;


@Controller
@RequestMapping(value="dictCon")
public class DictController extends BaseController {

	@Resource(name="dictService")
	private DictService service;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<DictPo> search(DictPo po, HttpServletRequest request) throws Exception{	
		try {
			String type=po.getType().trim();
			if(type==null || "".equals(type)){
				po.setType(null);
			}
			String name=po.getName().trim();
			if(name==null || "".equals(name)){
				po.setName(null);
			}
		} catch (Exception e) {
		}
		String inOrOut = Util.getInOrOut(request);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("数据字典","查询");
		if(result){
			generservice.insert_oprt_logs("系统管理-数据字典","查询",userInfo.getUsername(),"用户查询数据字典数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return service.search(po);
	}
	
	@ResponseBody
	@RequestMapping(value="/customSql")
	public List<DictPo> customSql(DictPo po){
		String type=toString(po.getType());
		if(!Util.sql_inj(type)){
			if(type!=null && !"".equals(type)){
			List<String> types = Arrays.asList(type.split(","));
			po.setTypes(types);
			}
		}
		return service.customSql(po);
	}
	
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated DictPo po, BindingResult bindingResult, HttpServletRequest request, String oper) throws Exception{
		Map<String,String> map=new HashMap<String,String>();
		
		//判断是否重复提交
//		if(!Util.getSessionJWT(request, po.getJwt())){
//			map.put("errors", SysConstant.CHONTFUTIJIOA);
//			return map;
//		}
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
		if(oper != null && oper.equals("add"))
		{
			//数据完整性校验
			/*RsaDecryptTool rsaTool = new RsaDecryptTool();
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId() == null ? "" : po.getId()).append(po.getType()).append(po.getLabel()).append(po.getValue()).append(po.getName()).append(po.getDescription() == null ? "" : po.getDescription());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}*/
			
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getType())){
				sb1.append("类型不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getLabel())){
				sb1.append("标签不能含有特殊字符").append("</br>");
			}
			if(Util.validationStr(po.getValue())){
				sb1.append("值不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getDescription())){
				sb1.append("描述不能含有特殊字符！").append("</br>");
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
			
			po.setCreate_by(toString(userInfo.getId()));
			res = service.insert(po);
			//判断是否需要添加审计数据
			boolean result = isAudit("数字字典","新增");
			if(result){
				generservice.insert_oprt_log("系统管理-数字字典","新增",userInfo.getUsername(),"新增了"+po.getLabel()+"名称为"+po.getName()+"的数据",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(oper != null && oper.equals("edit"))
		{
			//数据完整性校验
			RsaDecryptTool rsaTool = new RsaDecryptTool();
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId() == null ? "" : po.getId()).append(po.getType()).append(po.getLabel()).append(po.getValue()).append(po.getName()).append(po.getDescription() == null ? "" : po.getDescription());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getType())){
				sb1.append("类型不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getLabel())){
				sb1.append("标签不能含有特殊字符").append("</br>");
			}
			if(Util.validationStr(po.getValue())){
				sb1.append("值不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getDescription())){
				sb1.append("描述不能含有特殊字符！").append("</br>");
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
			
			if("".equals(po.getDescription())){
				po.setDescription(null);
			}
			po.setUpdate_by(toString(userInfo.getId()));
			res = service.update(po);
			generservice.insert_oprt_log("系统管理-数字字典","编辑",userInfo.getUsername(),"修改了"+po.getLabel()+"名称为"+po.getName()+"的数据",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(toString(userInfo.getId()));
//			String type=service.search(po).get(0).getType();
//			String name=service.search(po).get(0).getName();
			List<DictPo> list = service.search(po);
			res = service.delete(po);
			generservice.insert_oprt_log("系统管理-数字字典","删除",userInfo.getUsername(),"删除了"+list.get(0).getLabel()+"名称为"+list.get(0).getName()+"的数据",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else
		{
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
