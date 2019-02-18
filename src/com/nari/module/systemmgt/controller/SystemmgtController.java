package com.nari.module.systemmgt.controller;

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

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.deviceindex.controller.DeviceindexController;
import com.nari.module.deviceindex.po.DeviceindexPo;
import com.nari.module.deviceindex.service.DeviceindexService;
import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.module.systemmgt.po.SystemDevicePo;
import com.nari.module.systemmgt.po.SystemmgtPo;
import com.nari.module.systemmgt.service.SystemmgtService;
import com.nari.po.User;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.util.ZkClientUtils;

@Controller
@RequestMapping(value="systemmgtCon")
public class SystemmgtController extends BaseController{
	@Resource(name="systemmgtService")
	private SystemmgtService service;
	@Resource(name="deviceindexService")
	private DeviceindexService service2;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<SystemmgtPo> search(SystemmgtPo po, HttpServletRequest request) throws Exception{
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("软件系统管理","查询");
		if(result){
			generservice.insert_oprt_logs("配置管理-软件系统管理","查询",userInfo.getUsername(),"用户查询软件系统管理数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		List<SystemmgtPo> list = service.search(po);
		return list;
	}
	@Resource(name = "GeneranService")
	private   GeneranService generservice;

	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated SystemmgtPo po, BindingResult bindingResult, HttpServletRequest request, String oper, String adddata, String editdata) throws Exception{	
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
			//数据完整性校验
//			RsaDecryptTool rsaTool = new RsaDecryptTool();
//			po.setUsername(rsaTool.decrypt(po.getUsername()));
//			if(po.getReserver2() != null && !"".equals(po.getReserver2())){
//				po.setReserver2(rsaTool.decrypt(po.getReserver2()));
//			}
//			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
//			//对传输过来的数据进行MD5加密
//			StringBuffer sb = new StringBuffer();
//			sb.append(po.getId() == null ? "" : po.getId()).append(po.getUid()).append(po.getSystype()).append(po.getType()).append(po.getName()).append(po.getIp()).append(po.getUsername()).append(po.getMm()).append(po.getReserver1()).append(po.getReserver2()).append(po.getReserver3());
//			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
//			String newMdata = MD5.GetMD5Code(a);
//			if(!mdate.equals(newMdata)){
//				map.put("integrity", SysConstant.DATE_INTEGRITY);
//				return map;
//			}
			
			/*StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getUsername())){
				sb1.append("用户名不能含有特殊字符").append("</br>");
			}
			if(Util.validationStr(po.getReserver1())){
				sb1.append("数据库实例名不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getReserver2())){
				sb1.append("设备账号不能含有特殊字符！").append("</br>");
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
			
			//添加软件系统
			if(po.getId()==null){
				po.setCreate_by(userInfo.getId());
				res = service.insert(po);
				//如果设备数据不为空则添加组和设备关联表数据
				if(adddata != null && !"".equals(adddata)){
					res = addSystemDevice(adddata,po);
				}
				//判断是否需要添加审计数据
				boolean result = isAudit("软件系统管理","新增");
				if(result){
					generservice.insert_oprt_log("配置管理-软件系统管理","新增",userInfo.getUsername(),"新增了一条软件系统记录",userInfo.getIpaddr(),po.getId(),inOrOut);
				}
			}else{
				//修改
				po.setUpdate_by(userInfo.getId());
				res = service.update(po);
				if(res>0){
					dozkinfo("edit", po);
				}
				//如果设备数据不为空则添加组和设备关联表数据
				if(adddata != null && !"".equals(adddata)){
					po.setCreate_by(userInfo.getId());
					res = addSystemDevice(adddata,po);
				}
				//删除设备
				if(po.getIds() != null && !"".equals(po.getIds())){
					List<String> idList = Arrays.asList(po.getIds().split(","));
					po.setIdList(idList);
					res = service.deleteDevice(po);
				}
				generservice.insert_oprt_log("配置管理-软件系统管理","编辑",userInfo.getUsername(),"修改了一条软件系统记录",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
			if(po.getGroup_id()!=null){
				res = service.insertGroupSys(po);
			}
			//保存/编辑成功移除jwt
			request.getSession().removeAttribute("jwt");
		}
		else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(userInfo.getId());
			List<SystemmgtPo> list=service.search2(po);
//			int sysnum = service.findmonnum(po);
//			if(sysnum>0){
//				return 10;
//			}else{
				res = service.delete(po);
				po=list.get(0);
				if(res>0){
					dozkinfo(oper, po);
				}
				generservice.insert_oprt_log("配置管理-软件系统管理","删除",userInfo.getUsername(),"删除了一条软件系统记录",userInfo.getIpaddr(),po.getId(),inOrOut);
//			}
		}
		if(res != 0){
			map.put("success", SysConstant.SAVE_SUCCESS);
		}else{
			map.put("success", SysConstant.SAVE_FILE);
		}
		return map;
	}
	
	@Resource(name="deviceindexService")
	private DeviceindexService devindservice;
	public void dozkinfo(String oper,SystemmgtPo po){
		String conn="";
		String uid=DeviceindexController.toString(po.getUid());
		DeviceindexPo po2=new DeviceindexPo();
		List<DeviceindexPo> listA=new ArrayList<>();
		try {
			if(MyPropertiesPersist.DBTYPE.equalsIgnoreCase("ORACLE")){
				po2.setTabname("'MGT_SYSTEM'");
				listA=service2.searchtype(po2);
			}else{
				conn=DeviceindexController.getObjectVals(po,listA);
			}
		} catch (Exception e) {
		}
		if("edit".equals(oper)){//修改
			try {
				conn=DeviceindexController.toString(conn);
			} catch (Exception e) {
			}
			ZkClientUtils.updateConn(uid,conn);
		}else  if(oper != null && oper.equals("del")){//删除
			DeviceindexPo devindPo=new DeviceindexPo();
			devindPo.setUid(uid);
			devindPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
			List<DeviceindexPo> list=devindservice.search2(devindPo);
			if(list!=null && list.size()>0){
				for (int i = 0,allsize=list.size(); i <allsize; i++) {
					String pros="Type="+DeviceindexController.toString(list.get(i).getIndextype_id())+",Cron="+DeviceindexController.toString(list.get(i).getPeriod()).trim();//指标分类+周期
					ZkClientUtils.dropNode(uid,pros);
				}
			}
		}
	}
	
	/**
	 * 添加组和设备关联表数据
	 * @param adddata
	 * @return
	 */
	public int addSystemDevice(String adddata, SystemmgtPo po){
		SystemmgtPo systemPo = new SystemmgtPo();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			systemPo.setCreate_date(date2);
			systemPo.setUpdate_date(date2);
		} catch (Exception e) {
		}
		systemPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
		int res = 0;
		
		JSONArray josnArray = JSONArray.fromObject(adddata);
		
		for (int i = 0; i < josnArray.size(); i++) {
			JSONObject jsonObj = (JSONObject) josnArray.get(i);
			systemPo.setId(po.getId());
			systemPo.setDev_id(Integer.parseInt(jsonObj.get("id").toString()));
			systemPo.setCreate_by(po.getCreate_by());
			
			res = service.insertSysDev(systemPo);
		}
		return res;
	}
	
	/**
	 * 根据group_id 查询软件系统和设备关联表数据
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchSysDev")
	public List<DevicesPo> searchSysDev(SystemDevicePo po){
		List<SystemDevicePo> list = service.searchSysDev(po);
		List<DevicesPo> devList = new ArrayList<>();
		if(list.size() > 0){
			DevicesPo devicesPo = new DevicesPo();
			List<String> idList = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				idList.add(String.valueOf(list.get(i).getDev_id()));
			}
			devicesPo.setIdList(idList);
			//devicesPo.setIds(sb.toString());     
			devList = service.searchDevice(devicesPo);
		}
		return devList;
	}
	
	/**
	 * 根据组id查询关联设备数据
	 * @param po
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value="/searchDevices")
//	public List<DevicesPo> searchDevices(GroupPo po){
//		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
//		return service.searchDevices(po);
//	}
	
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
