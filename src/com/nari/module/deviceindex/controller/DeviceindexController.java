package com.nari.module.deviceindex.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.device.service.DeviceService;
import com.nari.module.deviceindex.po.DeviceindexPo;
import com.nari.module.deviceindex.service.DeviceindexService;
import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.module.systemmgt.po.SystemmgtPo;
import com.nari.module.systemmgt.service.SystemmgtService;
import com.nari.po.User;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.util.ZkClientUtils;
import com.nari.utils.RsaDecryptTool;


@Controller
@RequestMapping(value="devmonimgtCon")
public class DeviceindexController extends BaseController {
	private Logger logger = Logger.getLogger(DeviceindexController.class);
	@Resource(name="deviceindexService")
	private DeviceindexService service;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@Resource(name="devicesService")
	private DeviceService deviceService;
	@Resource(name="systemmgtService")
	private SystemmgtService systemService;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	static RsaDecryptTool rsaTool = new RsaDecryptTool();
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<DeviceindexPo> search(DeviceindexPo po, HttpServletRequest request) throws Exception{	
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<DeviceindexPo> list = service.search(po);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("设备监控管理","查询");
		if(result){
			generservice.insert_oprt_logs("配置管理-设备监控管理","查询",userInfo.getUsername(),"用户查询设备监控管理数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/searchUid")
	public List<DeviceindexPo> searchUid(DeviceindexPo po){	
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchUid(po);
	}
	@ResponseBody
	@RequestMapping(value="/searchUidBytype")
	public List<DeviceindexPo> searchUidBytype(DeviceindexPo po){	
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchUidBytype(po);
	}
	/**
	 * 校验
	 * @param request
	 * @param response
	 * @return String 不为空时返回具体错误描述
	 * @date：2017年5月27日
	 */
	@ResponseBody
	@RequestMapping(value="/checkDevIndex")
	public String checkDevIndex(DeviceindexPo podev, HttpServletRequest req){	
		String uids = podev.getUid();
		String[] uid =uids.split(",");
		String mess="";
		for(int i=0;i<uid.length;i++){
			DeviceindexPo po=new DeviceindexPo();
			po.setId(podev.getId());
			po.setUid(uid[i]);
			po.setIndextype_id(podev.getIndextype_id());
			List<DeviceindexPo> list=service.checkIndex(po);
			if(list!=null && list.size()>0){
				mess+=uid[i]+"已添加当前指标分类监控,请重新选择！";
			}
		}
		return mess;
	}
	/**
	 * 增、删、改入口
	 */
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated DeviceindexPo po, BindingResult bindingResult, HttpServletRequest request, String oper) throws Exception{	
		Map<String,String> map=new HashMap<String,String>();
		//判断是否重复提交
		if(!Util.getSessionJWT(request, po.getJwt())){
			map.put("errors", SysConstant.CHONTFUTIJIOA);
			return map;
		}
		User userInfo = (User) request.getSession().getAttribute("user");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		try {
			Date date2=df.parse(df.format(date));
			po.setCreate_date(date2);
			po.setUpdate_date(date2);
		} catch (Exception e) {
		}
		int res=0;
		if(oper != null && oper.equals("add")){
			po.setCreate_by(userInfo.getId());
			String uids = po.getUid();
			/*//数据完整性校验
			String mdate = rsaTool.decrypt(po.getMdata());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getUid()).append(po.getIndextype_id()).append(po.getPeriod()).append(po.getCollect()).append(po.getType());
			String newMdata = MD5.GetMD5Code(sb.toString());
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}*/
//			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
//			if(Util.validationStrZhouqi(po.getPeriod())){
//				sb1.append("周期不能含有特殊字符").append("</br>");
//			}
			//数据校验
			/*if (bindingResult.hasErrors()) {
				List<ObjectError> ErrorList = bindingResult.getAllErrors();
				for(ObjectError objectError:ErrorList){
					sb1.append(objectError.getDefaultMessage()).append("</br>");
				}    
			}
			if(sb1.toString() != null && !"".equals(sb1.toString())){
				map.put("errors", sb1.toString());
				return map;
			}*/
			
			String[] uid =uids.split(",");
			for(int i=0;i<uid.length;i++){
				po.setUid(uid[i]);
				if(po.getId()==null){
					res = service.insert(po);
					dozkinfo(oper, po,po);
					//判断是否需要添加审计数据
					boolean result = isAudit("设备监控管理","新增");
					if(result){
						generservice.insert_oprt_log("配置管理-设备监控管理","新增",userInfo.getUsername(),"新增了一条设备监控记录",userInfo.getIpaddr(),po.getId(),inOrOut);
					}
					po.setId(null);
				}
			}
		}else if(oper != null && oper.equals("edit"))
		{
			//数据完整性校验
			/*String mdate = rsaTool.decrypt(po.getMdata());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId()).append(po.getUid()).append(po.getIndextype_id()).append(po.getPeriod()).append(po.getCollect()).append(po.getType());
			String newMdata = MD5.GetMD5Code(sb.toString());
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			
			StringBuffer sb1 = new StringBuffer();*/
			//特殊字符校验
//			if(Util.validationStrZhouqi(po.getPeriod())){
//				sb1.append("周期不能含有特殊字符").append("</br>");
//			}
			
			//数据校验
			/*if (bindingResult.hasErrors()) {
				List<ObjectError> ErrorList = bindingResult.getAllErrors();
				for(ObjectError objectError:ErrorList){
					sb1.append(objectError.getDefaultMessage()).append("</br>");
				}    
			}
			if(sb1.toString() != null && !"".equals(sb1.toString())){
				map.put("errors", sb1.toString());
				return map;
			}*/
			
			po.setUpdate_by(userInfo.getId());
			DeviceindexPo oldPo=new DeviceindexPo();
			oldPo.setId(po.getId());
			List<DeviceindexPo> list=service.searchindex(oldPo);
			res = service.update(po);
			oldPo=list.get(0);
			dozkinfo(oper,oldPo,po);
			generservice.insert_oprt_log("配置管理-设备监控管理","编辑",userInfo.getUsername(),"修改了一条设备监控记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(userInfo.getId());
			DeviceindexPo oldPo=new DeviceindexPo();
			oldPo.setId(po.getId());
			List<DeviceindexPo> list=service.searchindex(oldPo);
			res = service.delete(po);
			generservice.insert_oprt_log("配置管理-设备监控管理","删除",userInfo.getUsername(),"删除了一条设备监控记录",userInfo.getIpaddr(),po.getId(),inOrOut);
			oldPo=list.get(0);
			dozkinfo(oper,oldPo,oldPo);
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
	 * 任务写入zk
	 * @param oper:增、删、改标识
	 * @param oldPo:原po
	 * @param po：新po
	 */
	public void dozkinfo(String oper,DeviceindexPo oldPo,DeviceindexPo po) throws Exception{
		String conn = "";
		String uid = toString(po.getUid());
		List<DeviceindexPo> listA = service.zdysearchDev(po);
		Hashtable<String, String> numbers = new Hashtable<String, String>(); 
		String oldType = toString(oldPo.getIndextype_id());
		String newType = toString(po.getIndextype_id());
		String pros = "Type="+newType+"&Cron="+toString(po.getPeriod()).trim()+"&collectType="+toString(po.getCollect());//指标分类+周期
		numbers.put("oper", oper);
		numbers.put("oldType", oldType);
		numbers.put("newType", newType);
		numbers.put("pros", pros);
		try {
			if(po.getType() !=null && po.getType().equals("D")){//设备
				DevicesPo devicePo=new DevicesPo();
				devicePo.setUid(uid);
				devicePo.setDBTYPE(MyPropertiesPersist.DBTYPE);
				List<DevicesPo> list=deviceService.search2(devicePo);
				//密码解密
				devicePo = list.get(0);
				devicePo.setIn_password(rsaTool.decrypt(devicePo.getIn_password()));
				devicePo.setOut_password(rsaTool.decrypt(devicePo.getOut_password()));
				conn = getObjectVals(devicePo,listA);
				numbers.put("uid", uid);
				numbers.put("conn", conn);
				dozkinfo2(numbers, oldPo, po);
			}else if(po.getType() !=null && po.getType().equals("S")){//软件
				SystemmgtPo systemPo = new SystemmgtPo();
				systemPo.setUid(uid);
				systemPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
				List<SystemmgtPo> list=systemService.search2(systemPo);
				systemPo = list.get(0);
				//密码解密
				systemPo.setPassword(rsaTool.decrypt(systemPo.getPassword()));
				if(systemPo.getReserver3() != null && !"".equals(systemPo.getReserver3())){
					systemPo.setReserver3(rsaTool.decrypt(systemPo.getReserver3()));
				}
				conn = getObjectVals(systemPo,listA);
				numbers.put("uid", uid);
				numbers.put("conn", conn);
				dozkinfo2(numbers, oldPo, po);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void dozkinfo2(Hashtable<String, String> map,DeviceindexPo oldPo,DeviceindexPo po){
		String oper=toString(map.get("oper"));
		String conn=toString(map.get("conn"));
		String uid=toString(map.get("uid"));
		String pros=toString(map.get("pros"));
		String newType=toString(map.get("newType"));
		String oldType=toString(map.get("oldType"));
		if(oper != null && oper.equals("add")){
			try {
				conn=toString(conn);
			} catch (Exception e) {
			}
			ZkClientUtils.initNode(uid,conn,pros,toString(po.getCollect()),newType);
		}else if(oper != null && oper.equals("edit")){
			try {
				String delpros="Type="+toString(oldPo.getIndextype_id())+"&Cron="+toString(oldPo.getPeriod()).trim()+"&collectType="+toString(oldPo.getCollect()).trim();//指标分类+周期
				ZkClientUtils.dropNode(uid,delpros);
				conn=toString(conn);
			} catch (Exception e) {
			}
			ZkClientUtils.initNode(uid,conn,pros,toString(po.getCollect()),oldType);
		}else  if(oper != null && oper.equals("del")){
			ZkClientUtils.dropNode(uid,pros);
		}
	}
	
	public static String getObjectVals(Object model,List<DeviceindexPo> list) throws NoSuchMethodException,IllegalAccessException, IllegalArgumentException,InvocationTargetException{
		StringBuffer returnValue = new StringBuffer();
		Field[] field = model.getClass().getDeclaredFields();//获取实体类的所有属性，返回Field数组
		for (int i = 0,allsize=field.length; i <allsize; i++) {
			String name = field[i].getName(); //获取属性的名字
			if(!name.equals("use_flag") && !name.equals("create_by") && !name.equals("create_date") && !name.equals("update_by") && !name.equals("update_date") && !name.equals("remark")){
				for (int j = 0,colSize=list.size(); j <colSize; j++) {
					if(name.equalsIgnoreCase(list.get(j).getDiname())){
						returnValue.append(name+"=");
						name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
						String type = field[i].getGenericType().toString(); // 获取属性的类型
						if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
							Method m = model.getClass().getMethod("get" + name);
							String value = (String) m.invoke(model); // 调用getter方法获取属性值
							returnValue.append(toString(value)+";");
						}
						if (type.equals("class java.lang.Integer")) {
							Method m = model.getClass().getMethod("get" + name);
							Integer value = (Integer) m.invoke(model);
							returnValue.append(toString(value)+";");
						}
						if (type.equals("class java.lang.Short")) {
							Method m = model.getClass().getMethod("get" + name);
							Short value = (Short) m.invoke(model);
							returnValue.append(toString(value)+";");
						}
						if (type.equals("class java.lang.Double")) {
							Method m = model.getClass().getMethod("get" + name);
							Double value = (Double) m.invoke(model);
							returnValue.append(toString(value)+";");
						}
						if (type.equals("class java.lang.Boolean")) {
							Method m = model.getClass().getMethod("get" + name);
							Boolean value = (Boolean) m.invoke(model);
							returnValue.append(toString(value)+";");
						}
						if (type.equals("class java.util.Date")) {
							Method m = model.getClass().getMethod("get" + name);
							Date value = (Date) m.invoke(model);
							returnValue.append(toString(value)+";");
						}
						break;
					}
				}
			}
		}
		return returnValue.toString();
	}
	
	/**
	 * 设置批量上传zk
	 * @date：2017年7月5日
	 */
	@ResponseBody
	@RequestMapping(value="/zkupload")
	public String zkupload(HttpServletRequest req, HttpServletResponse res, DeviceindexPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		//判断内外网
		String inOrOut = Util.getInOrOut(req);
		po.setPosition(inOrOut);
		String uids = po.getUid();
		po.setUids(Arrays.asList(uids.split(",")));
		List<DeviceindexPo> list = service.zdysearchMonDev(po);
		if(list != null && list.size()>0){
			for (int i = 0,allsize = list.size(); i <allsize; i++) {
				DeviceindexPo po2 = list.get(i);
				dozkinfo("edit",po2,po2);
			}
		}else{//为空则清空zk节点
			ZkClientUtils.clean();
		}
		return "上传成功!";
    }
	
	/**
	 * 从zk下载到本地
	 * @date：2017年7月5日
	 */
	@ResponseBody
	@RequestMapping(value="/zkdownload")
	public String zkdownload(HttpServletRequest req, HttpServletResponse res, DeviceindexPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		User userInfo = (User) req.getSession().getAttribute("user");
		int userid=userInfo.getId();
		String isBol = po.getIsBol();//Y:提醒将要写入的数据  N:直接zk写入到库中
		String mess = "";
		if(isBol.equals("N")){
			mess=zk_download(userid);
		}else {
			int num =checkzknum();
			if(num>0){
				mess="确定将 "+num+" 条数据从 zookeeper 导入到数据库中，该操作会覆盖本地已有数据，是否确定继续？";
			}else{
				mess="没有要导入的数据！&TAB&";
			}
		}
		return mess;
	}
	
	public String zk_download(int userid){
		int count=0;
		ZooKeeper zk = ZkClientUtils.zkClinet();
		try {
			String ids = ZkClientUtils.getData("/RT/Ids", zk);
			if(ids.indexOf(";")>-1){
				String[] idAr = ids.split(";");
				for (int i = 0,allsize = idAr.length; i <allsize; i++) {
					String uid = idAr[i];//1开头设备2开头软件
					String conn = ZkClientUtils.getData("/RT/Id/"+uid+"/connection",zk);
					String pros = ZkClientUtils.getData("/RT/Id/"+uid+"/pros",zk);
					try {
						String tabname="",type="";
						DevicesPo devPo = null;
						SystemmgtPo sysPo = null;
						//判断是硬件设备还是软件系统
						if(uid.substring(0, 1).equals("1")){
							type="D";
							tabname = "mgt_device";
							devPo = new DevicesPo();
						}else{
							type="S";
							tabname = "mgt_system";
							sysPo = new SystemmgtPo();
						}
						int num = 0;
						int id = 0;
						if(conn.indexOf(";")>-1){
							String[] conn_ar = conn.split(";");
							for (int j = 0,colnum = conn_ar.length; j <colnum; j++) {
								String[] str = conn_ar[j].split("=");
								String col = str[0];
								String val = str.length == 2 ? str[1] : "";
								if("mgt_device".equals(tabname)){
									if("id".equals(col)){
										devPo.setId(Integer.parseInt(val));
										id = Integer.parseInt(val);
									}else if("uid".equals(col)){
										devPo.setUid(val);
									}else if("devicetype".equals(col)){
										devPo.setDevicetype(Integer.parseInt(val));
									}else if("factory".equals(col)){
										devPo.setFactory(val);
									}else if("sn".equals(col)){
										devPo.setSn(val);
									}else if("model_id".equals(col)){
										devPo.setModel_id(Integer.parseInt(val));
									}else if("model".equals(col)){
										devPo.setModel(val);
									}else if("in_ip".equals(col)){
										devPo.setIn_ip(val);
									}else if("in_username".equals(col)){
										devPo.setIn_username(val);
									}else if("in_password".equals(col)){
										devPo.setIn_password(val);
									}else if("out_ip".equals(col)){
										devPo.setOut_ip(val);
									}else if("out_username".equals(col)){
										devPo.setOut_username(val);
									}else if("out_password".equals(col)){
										devPo.setOut_password(val);
									}else if("opersys".equals(col)){
										devPo.setOpersys(Integer.parseInt(val));
									}else if("assetno".equals(col)){
										devPo.setAssetno(val);
									}else if("position".equals(col)){
										devPo.setPosition(val);
									}else if("name".equals(col)){
										devPo.setName(val);
									}else if("parent_id".equals(col)){
										devPo.setParent_id(val);
									}else if("healthstatus".equals(col)){
										devPo.setHealthstatus(val);
									}else if("temperature".equals(col)){
										devPo.setTemperature(Integer.parseInt(val));
									}else if("life".equals(col)){
										devPo.setLife(val);
									}else if("port".equals(col)){
										devPo.setPort(val);
									}
								}else{
									if("id".equals(col)){
										sysPo.setId(Integer.parseInt(val));
										id = Integer.parseInt(val);
									}else if("systype".equals(col)){
										sysPo.setSystype(Integer.parseInt(val));
									}else if("uid".equals(col)){
										sysPo.setUid(val);
									}else if("type".equals(col)){
										sysPo.setType(val);
									}else if("name".equals(col)){
										sysPo.setName(val);
									}else if("ip".equals(col)){
										sysPo.setIp(val);
									}else if("username".equals(col)){
										sysPo.setUsername(val);
									}else if("mm".equals(col)){
										sysPo.setMm(val);
									}else if("reserver1".equals(col)){
										sysPo.setReserver1(val);
									}else if("reserver2".equals(col)){
										sysPo.setReserver2(val);
									}else if("reserver3".equals(col)){
										sysPo.setReserver3(val);
									}else if("position".equals(col)){
										sysPo.setPosition(val);
									}
								}
							}
							if("mgt_device".equals(tabname)){
								//先修改设备表数据,若没有则新增
								num = service.updateDevices(devPo);
								if(num == 0){
									num = service.insertDevices(devPo);
								}
							}else{
								//先修改软件系统表数据,若没有则新增
								num = service.updateSystem(sysPo);
								if(num == 0){
									num = service.insertSystem(sysPo);
								}
							}
						}
						//先修改设备监控表数据，若返回小于0则新增
						num=0;
						String indextype_id="",period="",colType="";
						if(pros.indexOf(";")>-1){
							String[] pros_arr=pros.split(";");
							
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = new Date();
							
							for (int j = 0,arsize=pros_arr.length; j <arsize; j++) {
								String[] pros_ar=pros.split("&");
								try {
									indextype_id=pros_ar[0].split("=")[1];
									period=pros_ar[1].split("=")[1];
									colType=pros_ar[2].split("=")[1].replace(";","");
									DeviceindexPo po = new DeviceindexPo();
									po.setId(id);
									po.setIndextype_id(Integer.parseInt(indextype_id));
									po.setPeriod(period);
									po.setCollect(Integer.parseInt(colType));
									po.setType(type);
									po.setCreate_by(userid);
									po.setUpdate_by(userid);
									po.setUid(uid);
									po.setDBTYPE(MyPropertiesPersist.DBTYPE);
									try {
										Date date2=df.parse(df.format(date));
										po.setCreate_date(date2);
										po.setUpdate_date(date2);
									} catch (Exception e) {
										logger.error(e);
									}
									num = service.update(po);
									if(num == 0){
										po.setId(null);
										num = service.insert(po);
									}
									count = count + 1;
								} catch (Exception e) {
									logger.error(e);
								}
							}
						}
					} catch (Exception e) {
						logger.error(e);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		String mess="成功导入 "+count+" 条数据!";
		if(count == 0){
			mess="数据导入失败！";
		}
		ZkClientUtils.closeZk(zk);
		return mess;
	}
	
	public int checkzknum(){
		ZooKeeper zk = ZkClientUtils.zkClinet();
		int num = 0;
		String ids = ZkClientUtils.getData("/RT/Ids", zk);
		if(ids != null && ids.indexOf(";")>-1){
			String[] uids = ids.split(";");
			for (int i = 0, allsize = uids.length; i < allsize; i++) {
				String uid = uids[i];
				String pros = ZkClientUtils.getData("/RT/Id/"+uid+"/pros",zk);
				int len = pros.split(";").length;
				num = num + len;
			}
		}
		ZkClientUtils.closeZk(zk);
		return num;
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
