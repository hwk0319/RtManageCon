package com.nari.module.group.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.general.service.GeneranService;
import com.nari.module.group.po.GroupDevicePo;
import com.nari.module.group.po.GroupPo;
import com.nari.module.group.service.GroupService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;


@Controller
@RequestMapping(value="groupCon")
public class GroupController extends BaseController {
	private Logger logger = Logger.getLogger(GroupController.class);
	@Resource(name="groupService")
	private GroupService service;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<GroupPo> search(GroupPo po, HttpServletRequest request) throws Exception{
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("组管理","查询");
		if(result){
			generservice.insert_oprt_logs("配置管理-组管理","查询",userInfo.getUsername(),"用户查询组管理数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		String ids = toString(po.getIds());
		if(!Util.sql_inj(ids)){
			if(ids!=null && !"".equals(ids)){
				List<String> idList = Arrays.asList(ids.split(","));
				po.setIdList(idList);
			}
		}
		return service.search(po);
	}
	
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated GroupPo po, BindingResult bindingResult, HttpServletRequest request, String oper, String adddata, String editdata) throws Exception{	
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
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		int res=0;
		
		if(oper != null && oper.equals("saveOrUpdate"))
		{
//			RsaDecryptTool rsaTool = new RsaDecryptTool();
//			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
//			//对传输过来的数据进行MD5加密
//			StringBuffer sb = new StringBuffer();
//			sb.append(po.getId() == null ? "" : po.getId()).append(po.getGrouptype()).append(po.getGrotype()).append(po.getName()).append(po.getDescription() == null ? "" : po.getDescription());
//			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
//			String newMdata = MD5.GetMD5Code(a);
//			if(!newMdata.equals(mdate)){
//				map.put("integrity", SysConstant.DATE_INTEGRITY);
//				return map;
//			}
			
			/*StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getDescription())){
				sb1.append("描述不能含有特殊字符").append("</br>");
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
			}*/
			
			po.setCreate_by(userInfo.getId());
			//添加组
			if(po.getId()==null){
				res = service.insert(po);
				//如果设备数据不为空则添加组和设备关联表数据
				if(adddata != null && !"".equals(adddata)){
					res = addGroupDevice(adddata,po);
				}
				//判断是否需要添加审计数据
				boolean result = isAudit("组管理","新增");
				if(result){
					generservice.insert_oprt_log("配置管理-组管理","新增",userInfo.getUsername(),"新增了一条组记录",userInfo.getIpaddr(),po.getId(),inOrOut);
				}
			}else{
				//修改组
				po.setUpdate_by(userInfo.getId());
				res = service.update(po);
				if(adddata != null && !"".equals(adddata)){
					res = addGroupDevice(adddata,po);
				}
				//删除设备
				if(po.getIds() != null && !"".equals(po.getIds())){
					List<String> idList = Arrays.asList(po.getIds().split(","));
					po.setIdList(idList);
					res = service.deleteDevice(po);
				}
			}
			if(po.getDouble_id()!=null){
				res = service.insertDoubleGro(po);
			}
			if(po.getIntegrate_id()!=null){
				res = service.insertIntegrateGro(po);
			}
			if(po.getCluster_id()!=null){
				res = service.insertClusterGro(po);
			}
			//保存/编辑成功移除jwt
			request.getSession().removeAttribute("jwt");
			generservice.insert_oprt_log("配置管理-组管理","编辑",userInfo.getUsername(),"修改了一条组记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}
		else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(userInfo.getId());
			res = service.delete(po);
			generservice.insert_oprt_log("配置管理-组管理","删除",userInfo.getUsername(),"删除了一条组记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else{
			res = 0;
		}
		if(res != 0){
			map.put("success", SysConstant.SAVE_SUCCESS);
		}else{
			map.put("success", SysConstant.SAVE_FILE);
		}
		return map;
	}
	
	/**
	 * 添加组和设备关联表数据
	 * @param adddata
	 * @return
	 */
	public int addGroupDevice(String adddata, GroupPo po){
		int res = 0;
		GroupPo groupPo = new GroupPo();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			groupPo.setCreate_date(date2);
			groupPo.setUpdate_date(date2);
		} catch (Exception e) {
		}
		JSONArray josnArray = JSONArray.fromObject(adddata);
		
		for (int i = 0; i < josnArray.size(); i++) {
			JSONObject jsonObj = (JSONObject) josnArray.get(i);
			groupPo.setDevice_id(Integer.parseInt(jsonObj.get("id").toString()));
			groupPo.setId(po.getId());
			groupPo.setCreate_by(po.getCreate_by());
			res = service.insertGroDev(groupPo);
		}
		
		return res;
	}
	
	/**
	 * 根据group_id 查询组和设备关联表数据
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchGroDev")
	public List<DevicesPo> searchGroDev(GroupDevicePo po){
		List<DevicesPo> devList = null;
		try {
			List<GroupDevicePo> list = null;
			list = service.searchGroDev(po);
			if(list != null && list.size() > 0){
				List<String> idList = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					idList.add(String.valueOf(list.get(i).getDev_id()));
				}
				DevicesPo devicesPo = new DevicesPo();
				devicesPo.setIdList(idList);
				//devicesPo.setIds(sb.toString());     
				devList = service.searchDevice(devicesPo);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return devList;
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
	
	/**
	 * 根据id查询组
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchById")
	public List<GroupPo> searchById(GroupPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<GroupPo> list = null;
		try {
			list = service.search(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

}
