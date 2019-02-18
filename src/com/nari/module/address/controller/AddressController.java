package com.nari.module.address.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.nari.module.address.po.AddressPo;
import com.nari.module.address.service.AddressService;
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
@RequestMapping(value="addressCon")
public class AddressController extends BaseController {
	
	private Logger logger = Logger.getLogger(AddressController.class);

	@Resource(name="addressService")
	private AddressService service;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<AddressPo> search(AddressPo po, HttpServletRequest request){	
		List<AddressPo> list = new ArrayList<>();
		try {
			//判断是否需要添加审计数据
			User userInfo = (User) request.getSession().getAttribute("user");
			boolean result = isAudit("联系人管理","查询");
			if(result){
				String inOrOut = Util.getInOrOut(request);
				generservice.insert_oprt_logs("配置管理-联系人管理","查询",userInfo.getUsername(),"用户查询联系人管理数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
			}
			list = service.search(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	
	/**
	 * @param po
	 * @param bindingResult 接受错误信息
	 * @Validated: 注解效验器
	 * @param request
	 * @param oper
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated AddressPo po, BindingResult bindingResult, HttpServletRequest request,String oper) throws Exception{	
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
		String inOrOut = Util.getInOrOut(request);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		int res;
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		if(oper != null && oper.equals("add"))
		{
//			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
//			//对传输过来的数据进行MD5加密
//			StringBuffer sb = new StringBuffer();
//			sb.append(po.getId() == null ? "" : po.getId()).append(po.getName()).append(po.getPhone()).append(po.getEmail()).append(po.getAddress() == null ? "" : po.getAddress());
//			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
//			String newMdata = MD5.GetMD5Code(a);
//			if(!mdate.equals(newMdata)){
//				map.put("integrity", SysConstant.DATE_INTEGRITY);
//				return map;
//			}
			//数据校验
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getPhone())){
				sb1.append("手机号名不能含有特殊字符").append("</br>");
			}
			if(Util.validationStr(po.getAddress())){
				sb1.append("地址不能含有特殊字符！").append("</br>");
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
			boolean result = isAudit("联系人管理","新增");
			if(result){
				generservice.insert_oprt_log("系统管理-联系人管理","新增",userInfo.getUsername(),"新增"+po.getName()+"联系人",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(oper != null && oper.equals("edit"))
		{
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId() == null ? "" : po.getId()).append(po.getName()).append(po.getPhone()).append(po.getEmail()).append(po.getAddress() == null ? "" : po.getAddress());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			//数据校验
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getPhone())){
				sb1.append("手机号名不能含有特殊字符").append("</br>");
			}
			if(Util.validationStr(po.getAddress())){
				sb1.append("地址不能含有特殊字符！").append("</br>");
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
			
			po.setUpdate_by(userInfo.getId());
			res = service.update(po);
			generservice.insert_oprt_log("系统管理-联系人管理","编辑",userInfo.getUsername(),"修改"+po.getName()+"联系人",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(userInfo.getId());
			String name = service.search(po).get(0).getName();
			res = service.delete(po);
			generservice.insert_oprt_log("系统管理-联系人管理","删除",userInfo.getUsername(),"删除"+name+"联系人",userInfo.getIpaddr(),po.getId(),inOrOut);
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
