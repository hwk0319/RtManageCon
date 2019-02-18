package com.nari.module.device.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.device.service.DeviceService;
import com.nari.module.deviceindex.controller.DeviceindexController;
import com.nari.module.deviceindex.po.DeviceindexPo;
import com.nari.module.deviceindex.service.DeviceindexService;
import com.nari.module.dict.po.DictPo;
import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.IpUtils;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.URLEncoder;
import com.nari.util.Util;
import com.nari.util.ZkClientUtils;
import com.nari.utils.RsaDecryptTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="devicesCon")
public class DeviceController extends BaseController {
	@Resource(name="devicesService")
	private DeviceService service;
	@Resource(name="deviceindexService")
	private DeviceindexService service2;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<DevicesPo> search(DevicesPo po, HttpServletRequest request) throws Exception{
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		if(po.getIds() != null && !"".equals(po.getIds())){
			List<String> list = Arrays.asList(po.getIds().split(","));
			po.setIdList(list);
		}
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("设备管理","查询");
		if(result){
			generservice.insert_oprt_logs("配置管理-设备管理","查询",userInfo.getUsername(),"用户查询设备管理数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return service.search(po);
	}
	
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated DevicesPo po, BindingResult bindingResult, HttpServletRequest request, String oper, String adddata, String editdata) throws Exception{
		Map<String,String> map=new HashMap<String,String>();
		//判断是否重复提交
		if(!Util.getSessionJWT(request, po.getJwt())){
			map.put("errors", SysConstant.CHONTFUTIJIOA);
			return map;
		}
		User userInfo = (User) request.getSession().getAttribute("user");
		po.setCreate_by(userInfo.getId());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			po.setCreate_date(date2);
			po.setUpdate_date(date2);
		} catch (Exception e) {
		}
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		int res=0;
		if(oper != null && oper.equals("saveOrUpdate")){
			//数据完整性校验
			RsaDecryptTool rsaTool = new RsaDecryptTool();
			po.setIn_username(rsaTool.decrypt(po.getIn_username()));
			po.setOut_username(rsaTool.decrypt(po.getOut_username()));
//			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
//			StringBuffer sb = new StringBuffer();
//			sb.append(po.getId() == null ? "" : po.getId()).append(po.getUid()).append(po.getDevicetype()).append(po.getFactory()).append(po.getModel()).append(po.getPosition()).append(po.getAssetno()).append(po.getIn_ip()).append(po.getIn_username()).append(po.getOpersys());
//			sb.append(po.getOut_ip()).append(po.getOut_username()).append(po.getName()).append(po.getSn()).append(po.getIn_password()).append(po.getOut_password());
//			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
//			String newMdata = MD5.GetMD5Code(a);
//			if(!mdate.equals(newMdata)){
//				map.put("integrity", SysConstant.DATE_INTEGRITY);
//				return map;
//			}
//			if(!oper.equals("del")){
				/*StringBuffer sb1 = new StringBuffer();
				//特殊字符校验
				if(Util.validationStr(po.getPosition())){
					sb1.append("位置信息不能含有特殊字符！").append("</br>");
				}
				if(Util.validationStr(po.getAssetno())){
					sb1.append("资产编号不能含有特殊字符").append("</br>");
				}
				if(Util.validationStr(po.getIn_username())){
					sb1.append("应用用户名不能含有特殊字符！").append("</br>");
				}
				if(Util.validationStr(po.getOut_username())){
					sb1.append("带外用户名不能含有特殊字符！").append("</br>");
				}
				if(Util.validationStr(po.getName())){
					sb1.append("主机名不能含有特殊字符！").append("</br>");
				}
				if(Util.validationStr(po.getSn())){
					sb1.append("SN码不能含有特殊字符！").append("</br>");
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
//			}
			//通过id判断操作为添加还是修改
			if(po.getId() == null){
				//添加服务器
				res = service.insert(po);
				//处理json字符串 并保存到po 完成添加附属设备
				if(adddata != null && !"".equals(adddata)){
					res = doDataStr(adddata,po);
				}
				//添加组合
				if(po.getGroup_id()!=null){
					res = service.insertGroupDev(po);
				}
				if(po.getSys_id()!=null){
					res = service.insertSysDev(po);
				}
				//判断是否需要添加审计数据
				boolean result = isAudit("设备管理","新增");
				if(result){
					generservice.insert_oprt_log("配置管理-设备管理","新增",userInfo.getUsername(),"新增了一条设备记录",userInfo.getIpaddr(),po.getId(),inOrOut);
				}
			}else{
				po.setUpdate_by(userInfo.getId());
				//修改服务器
				res = service.update(po);
				
				//如果有新增附属设备则添加
				if(adddata != null && !"".equals(adddata)){
					//处理json字符串 并保存到po 完成添加附属设备
					res = doDataStr(adddata,po);
				}
				//修改附属设备
				if(editdata!=null && !"".equals(editdata)){
					res = dataEdit(editdata,po);
				}
				//如果有删除数据则删除
				if(po.getIds()!=null && !"".equals(po.getIds())){
					res = service.deleteFuShuDev(po);
				}
				dozkinfo(oper, po);
				generservice.insert_oprt_log("配置管理-设备管理","编辑",userInfo.getUsername(),"修改了一条设备记录",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(oper != null && oper.equals("del"))
		{
			//判断zk是否有数据，如果有删除zk数据
			dozkinfo(oper, po);
			
			List<DevicesPo> list=service.search2(po);
			po.setUid(list.get(0).getUid());
			po.setUpdate_by(userInfo.getId());
			
//			int monnum = service.findmonnum(po);
//			int sysnum = service.findsysnum(po);
//			int sum = monnum+sysnum;
//			if(sum>0){
//				return 10;
//			}else{
				//先删除附属设备
				res = service.deleteByUid(po);
				//删除设备-软件系统关联数据
				res = service.deleteSystemDev(po);
				//删除设备-组关联数据
				res = service.deleteGroupDev(po);
				//删除服务器
				res = service.delete(po);
				
				generservice.insert_oprt_log("配置管理-设备管理","删除",userInfo.getUsername(),"删除了一条设备记录",userInfo.getIpaddr(),po.getId(),inOrOut);
//			}
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
	 * 处理json字符串 并保存到po 完成添加附属设备操作
	 * @param datastr
	 */
	public int doDataStr(String datastr,DevicesPo po){
	
		int res=0;
		//根据服务器ID查询到uid,保存到附属设备的parent_id
		DevicesPo devPo = new DevicesPo();
		devPo.setId(po.getId());
		List<DevicesPo> list = service.search(devPo);
		
		//把页面传的json字符串转成json数组，并保存到po
		JSONArray jsonArray = JSONArray.fromObject(datastr);  
		for (int i = 0; i < jsonArray.size(); i++) {
			
			JSONObject obj = (JSONObject) jsonArray.get(i);  
			
		    DevicesPo devicesPo = new DevicesPo();
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			try {
				Date date2=df.parse(df.format(date));
				devicesPo.setCreate_date(date2);
				devicesPo.setUpdate_date(date2);
			} catch (Exception e) {
			}
			devicesPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
		    devicesPo.setParent_id(list.get(0).getUid());
		    devicesPo.setCreate_by(po.getCreate_by());
		    //从json中取出数据保存到po里面
			devicesPo.setDevicetype(Integer.parseInt(obj.get("devicetype").toString()));
			devicesPo.setSn(obj.get("sn").toString());
			devicesPo.setModel(obj.get("model").toString());
//			devicesPo.setName(obj.get("name").toString());
			devicesPo.setFactory(obj.get("factory").toString());
			devicesPo.setAssetno(obj.get("assetno").toString());
//			String position = obj.get("position").toString();
//			if(position != null && !"".equals(position)){
//				devicesPo.setPosition(Integer.parseInt(position));
//			}
			devicesPo.setPort(obj.get("port").toString());
			
			//添加附属设备
			res = service.insert(devicesPo);
		}
		return res;
	}
	
	/**
	 * 修改附属设备数据
	 * @param datastr
	 * @param po
	 * @return
	 */
	public int dataEdit(String datastr,DevicesPo po){
	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			Date date2 = new Date();
			try {
				 date2=df.parse(df.format(date));
			} catch (Exception e) {
			}
		int res=0;
		
		//把页面传的json字符串转成json数组，并update
		JSONArray jsonArray = JSONArray.fromObject(datastr);  
		for (int i = 0; i < jsonArray.size(); i++) {
			
			DevicesPo devPo = new DevicesPo();
			
			JSONObject obj = (JSONObject) jsonArray.get(i);  
		    
			devPo.setId(Integer.parseInt(obj.get("id").toString()));
			//根据id查询设备信息
			DevicesPo devicesPo = service.searchPoById(devPo);
			devicesPo.setUpdate_date(date2);
		    //保存编辑后的数据
			devicesPo.setDevicetype(Integer.parseInt(obj.get("devicetype").toString()));
			devicesPo.setSn(obj.get("sn").toString());
			devicesPo.setModel(obj.get("model").toString());
			devicesPo.setFactory(obj.get("factory").toString());
			devicesPo.setAssetno(obj.get("assetno").toString());
			devicesPo.setPort(obj.get("port").toString());
			
			//修改附属设备
			res = service.update(devicesPo);
		}
		return res;
	}
	
	@Resource(name="deviceindexService")
	private DeviceindexService devindservice;
	public void dozkinfo(String oper,DevicesPo podev){
		DevicesPo po = new DevicesPo();
		po.setId(podev.getId());
		List<DevicesPo> listdev = service.search(po);
		if(listdev.size() > 0 && listdev != null){
			po = listdev.get(0); 
		}
		//解密
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		String inpwd = rsaTool.decrypt(po.getIn_password());
		po.setIn_password(inpwd);
		String outpwd = rsaTool.decrypt(po.getOut_password());
		po.setOut_password(outpwd);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		
		String conn = "";
		String uid = po.getUid();
		DeviceindexPo po2=new DeviceindexPo();
		List<DeviceindexPo> listA = new ArrayList<>();
		try {
			if(MyPropertiesPersist.DBTYPE.equalsIgnoreCase("ORACLE")){
				po2.setTabname("'MGT_DEVICE'");
				listA =service2.searchtype(po2);
			}else{
				po2.setTabname("'mgt_device'");
				listA =service2.searchtype(po2);
			}
				conn=DeviceindexController.getObjectVals(po,listA);
		} catch (Exception e) {
		}
		if("saveOrUpdate".equals(oper) && po.getId() != null){//修改
			try {
				conn=DeviceindexController.toString(conn);
			} catch (Exception e) {
			}
			ZkClientUtils.updateConn(uid,conn);
		}else  if(oper != null && oper.equals("del")){//删除
			DeviceindexPo devindPo = new DeviceindexPo();
			devindPo.setUid(uid);
			devindPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
			List<DeviceindexPo> list=devindservice.search2(devindPo);
			if(list!=null && list.size()>0){
				for (int i = 0,allsize=list.size(); i <allsize; i++) {
					String pros="Type="+DeviceindexController.toString(list.get(i).getIndextype_id())+",Cron="+DeviceindexController.toString(list.get(i).getPeriod());//指标分类+周期
					ZkClientUtils.dropNode(uid,pros);
				}
			}
		}
	}
	
	/**
	 * 查询服务器下面的设备
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchDeviceInfo")
	public List<DevicesPo> searchDeviceInfo(@Param("po") DevicesPo po){
		return service.searchDeviceInfo(po);
	}
	
	/**
	 * 根据id查询设备
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchById")
	public List<DevicesPo> searchById(DevicesPo po){
		return service.searchById(po);
	}
	
	/**
	 * 根据设备类型查询已添加附属设备数量
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchMaxNum")
	public int searchMaxNum(DevicesPo po){
		return service.searchMaxNum(po);
	}
	
	@ResponseBody
	@RequestMapping(value="/searchFactory")
	public DictPo searchFactory(DevicesPo po){
		return service.searchFactory(po);
	}
	
	@ResponseBody
	@RequestMapping(value="/getHostnameAndSn")
	public List<String> getHostnameAndSn(DevicesPo po){
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		po.setIn_password(rsaTool.decrypt(po.getIn_password()));
		List<String> list = new ArrayList<String>();
		list=IpUtils.IsOrNotMyself(po.getIn_ip(),po.getIn_username(),po.getIn_password(),po.getIsOrNot());
		return list;
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
			if("1".equals(audit)){
				String auditthose = list.get(0).getAuditThose();
				if(auditthose.contains(methodName)){
					res = true;
				}
			}
		}
		return res;
	}
}
