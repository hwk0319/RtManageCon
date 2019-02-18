package com.nari.monitormgt.monihome.doublemon.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.AxisFault;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.nari.controller.BaseController;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.device.service.DeviceService;
import com.nari.module.doublemgt.po.DoublemgtPo;
import com.nari.module.doublemgt.service.DoublemgtService;
import com.nari.module.general.service.GeneranService;
import com.nari.module.paramdb.po.paramdbPo;
import com.nari.monitormgt.monihome.doublemon.po.doublemonPo;
import com.nari.monitormgt.monihome.doublemon.service.doublemonService;
import com.nari.monitormgt.monihome.util.loadBalancingClient;
import com.nari.monitormgt.monihome.util.usercount;
import com.nari.po.User;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.service.PreSetTaskExecService;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping(value="doublemonCon")
public class doublemonController extends BaseController {
	private static Logger logger = Logger.getLogger(doublemonController.class); 
	@Resource(name="doublemonService")
	private doublemonService service;
	@Autowired
	private PreSetTaskExecService preSetTaskExecService;
	@Resource(name="doublemgtService")
	private DoublemgtService doubleMgtService;
	@Resource(name="devicesService")
	private DeviceService devicesService;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
//	private loadBalancingClient loadClient;
	
	@ResponseBody
	@RequestMapping(value="/searchDoubleMgt")
	public List<DoublemgtPo> searchDoubleMgt(DoublemgtPo po, HttpServletRequest request){
		List<DoublemgtPo> list = null;
		try {
			//判断内外网
			String inOrOut = Util.getInOrOut(request);
			po.setPosition(inOrOut);
			list = doubleMgtService.search(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 获取主页所有数据项
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/getDoubinfo")
	public String getDoubinfo(HttpServletRequest req,doublemonPo po) throws Exception{	
		String double_id=toString(po.getDouble_id());
		String retStr="";
		/*****根据双活id查询中心数据******/
		List<Map> listC=service.searchDoubleById(double_id);
		if(listC!=null && listC.size()>0){
			StringBuffer sb1=new StringBuffer();
			StringBuffer sb2=new StringBuffer();
			String core_id="";
			boolean outLine1=false,inLine1=false,dbLine1=false;//记录 第一个中心每组中的链接线
			boolean outLine2=false,inLine2=false,dbLine2=false;//记录 第二个中心每组中的链接线

			//查询数据库是否有告警信息
			/**
			 * 第一个中心
			 */
			Map<String, Object> tempC=new LinkedCaseInsensitiveMap<Object>(); 
			tempC.putAll(listC.get(0)); 
			double_id=toString(tempC.get("double_id"));
			core_id=toString(tempC.get("core_id"));
			sb1.append("\"core_name\":\""+tempC.get("core")+"\",");//中心名
			sb1.append("\"core_id\":\""+core_id+"\",");//中心名id
			//查询中心各组的设备数据
			List<Map> listG=service.searchDoubleCoreById(core_id);
			String outStr="",inStr="",dbStr="", db_info="";
			if(listG!=null && listG.size()>0){
				String db_id="";//数据库服务器id
				Map<String, Object> tempG=new LinkedCaseInsensitiveMap<Object>(); 
				for (int i = 0,allsize=listG.size(); i <allsize; i++) {
					tempG.putAll(listG.get(i)); 
					String group_id=toString(tempG.get("group_id"));//组Id
					String id=toString(tempG.get("device_id"));//设备id
					String device_uid=toString(tempG.get("device_uid"));//设备uid
					int grouptype=toInteger(tempG.get("grouptype"));//1=数据库，2=数据库，3=内网，4=外网
					String warn_level_bool=toString(tempG.get("warn_level_bool"));
					String[] warn_level_bools=warn_level_bool.split("-");
					String bool=toString(warn_level_bools[0]);
					String servImg=warn_level_bools[1];
					String str="",out_val="",in_val="";
					try {
						out_val=toString(tempG.get("out_val"));
						in_val=toString(tempG.get("in_val"));
						if(out_val.equals("连接失败")&&in_val.equals("连接失败")){//带外和带内同时Ping不通表示服务器宕机
							servImg="app_gray";
						}
					} catch (Exception e) {
						logger.error(e);
					}
					/**数据库服务器**/
					if(grouptype==1 || grouptype==2){
						db_id+=id+",";
						if(servImg.equalsIgnoreCase("app_gray")){
							servImg="db_gray";
						}else if(servImg.equalsIgnoreCase("app_red")){
							servImg="db_red";
						}else if(servImg.equalsIgnoreCase("app_yellow")){
							servImg="db_yellow";
						}else{
							//查询设备告警信息
							List<Map> listS=service.searchWarnlogByDevUid(device_uid);
							if(servImg.equalsIgnoreCase("app_green") && listS!=null && listS.size()>0){
								servImg="db_green";
							}else{
								servImg="db_green_green";
							}
						}
						if(bool.equals("true")){
							dbLine1=true;
						}
						str="<img id='"+id+"' uid='"+device_uid+"' group_id='"+group_id+"' onclick='jumpServer("+device_uid+","+id+")' title='"+toString(tempG.get("name"))+"' src='/RtManageCon/jsp/pages/monitormgt/monihome/monimenu/double/img/"+servImg+".png'/>";
						dbStr+=str;
					}else{
						str="<img id='"+id+"' uid='"+device_uid+"' group_id='"+group_id+"' onclick='jumpServer("+device_uid+","+id+")' title='"+toString(tempG.get("name"))+"' src='/RtManageCon/jsp/pages/monitormgt/monihome/monimenu/double/img/"+servImg+".png'/>";
						/**内网服务器**/
						if(grouptype==3){
							if(bool.equals("true")){
								inLine1=true;
							}
							inStr+=str;
						} 
						/**外网服务器**/
						else if(grouptype==4){
							if(bool.equals("true")){
								outLine1=true;
							}
							outStr+=str;
						}
					}
				}
				//判定主备库
				if(!"".equals(db_id)){
					String describe="未做主备",value="NO",scn="";
					//获取主备库状态
					db_id = db_id.substring(0,db_id.length()-1);
					String []ids = db_id.split(",");
					
					List<Map> indList=service.searchZhuBeiKuStatus(ids);
					if(indList!=null && indList.size()>0){
						Map<String, Object> tempInd=new LinkedCaseInsensitiveMap<Object>(); 
						tempInd.putAll(indList.get(0)); 
						try {
							if(tempInd.get("describe").equals("PRIMARY")){
								describe="主库";
							}else if(tempInd.get("describe").equals("PHYSICAL STANDBY")){
								describe="备库";
							}else{
								describe="未做主备";
							}
							value=toString(tempInd.get("describe"));
						} catch (Exception e) {
							logger.error(e);
						}
					}
					
					//查询scn
					indList=service.searchScn(ids);
					if(indList!=null && indList.size()>0){
						Map<String, Object> tempInd=new LinkedCaseInsensitiveMap<Object>(); 
						tempInd.putAll(indList.get(0)); 
						try {
							scn=toString(tempInd.get("scn"));
						} catch (Exception e) {
							logger.error(e);
						}
					}
					
					db_info+="<span value='"+value+"'>"+describe+"</span>";
					db_info+="<span>SCN："+scn+"</span>";
				}
			}
			//连线判断
			if(outLine1==false || dbLine1==false){
				sb1.append("\"outLine\":\"out_db_err_l\",");
			}else{
				sb1.append("\"outLine\":\"out_db_succ_l\",");
			}
			if(inLine1==false || dbLine1==false){
				sb1.append("\"inLine\":\"in_db_err\",");
			}else{
				sb1.append("\"inLine\":\"in_db_succ\",");
			}
			sb1.append("\"outStr\":\""+outStr+"\",\"inStr\":\""+inStr+"\",\"dbStr\":\""+dbStr+"\",\"db_info\":\""+db_info+"\"");
			/**
			 * 第二个中心
			 */
			tempC.putAll(listC.get(1)); 
			core_id=toString(tempC.get("core_id"));
			sb2.append("\"core_name\":\""+tempC.get("core")+"\",");//中心名
			sb2.append("\"core_id\":\""+core_id+"\",");//中心名id
			listG=service.searchDoubleCoreById(core_id);			
			outStr="";inStr="";dbStr="";db_info="";
			if(listG!=null && listG.size()>0){
				String db_id="";//数据库服务器uid
				Map<String, Object> tempG=new LinkedCaseInsensitiveMap<Object>(); 
				for (int i = 0,allsize=listG.size(); i <allsize; i++) {
					tempG.putAll(listG.get(i)); 
					String group_id=toString(tempG.get("group_id"));//组Id
					String id=toString(tempG.get("device_id"));//设备id
					String uid=toString(tempG.get("device_uid"));//设备uid
					int grouptype=toInteger(tempG.get("grouptype"));//1=数据库，2=数据库，3=内网，4=外网
					String warn_level_bool=toString(tempG.get("warn_level_bool"));
					String[] warn_level_bools=warn_level_bool.split("-");
					String bool=toString(warn_level_bools[0]);
					String servImg=warn_level_bools[1];
					String str="",out_val="",in_val="";
					try {
						out_val=toString(tempG.get("out_val"));
						in_val=toString(tempG.get("in_val"));
						if(out_val.equals("连接失败")&&in_val.equals("连接失败")){//带外和带内同时Ping不通表示服务器宕机
							servImg="app_gray";
						}
					} catch (Exception e) {
						logger.error(e);
					}
					/**数据库服务器**/
					if(grouptype==1 || grouptype==2){
						db_id+=id+",";
						if(servImg.equalsIgnoreCase("app_gray")){
							servImg="db_gray";
						}else if(servImg.equalsIgnoreCase("app_red")){
							servImg="db_red";
						}else if(servImg.equalsIgnoreCase("app_yellow")){
							servImg="db_yellow";
						}else{
							List<Map> listS=service.searchWarnlogByDevUid(uid);
							if(servImg.equalsIgnoreCase("app_green") && listS!=null && listS.size()>0){
								servImg="db_green";
							}else{
								servImg="db_green_green";
							}
						}
						if(bool.equals("true")){
							dbLine2=true;
						}
						str="<img id='"+id+"' uid='"+uid+"' group_id='"+group_id+"' onclick='jumpServer("+uid+","+id+")' title='"+toString(tempG.get("name"))+"' src='/RtManageCon/jsp/pages/monitormgt/monihome/monimenu/double/img/"+servImg+".png'/>";
						dbStr+=str;
					}else{
						str="<img id='"+id+"' uid='"+uid+"' group_id='"+group_id+"' onclick='jumpServer("+uid+","+id+")' title='"+toString(tempG.get("name"))+"' src='/RtManageCon/jsp/pages/monitormgt/monihome/monimenu/double/img/"+servImg+".png'/>";
						/**内网服务器**/
						if(grouptype==3){
							if(bool.equals("true")){
								inLine2=true;
							}
							inStr+=str;
						} 
						/**外网服务器**/
						else if(grouptype==4){
							if(bool.equals("true")){
								outLine2=true;
							}
							outStr+=str;
						}
					}
				}
				//判定主备库
				if(!"".equals(db_id)){
					String describe="未做主备",value="NO",scn="";
					//获取主备库状态
					db_id = db_id.substring(0,db_id.length()-1);
					String []ids = db_id.split(",");
					
					List<Map> indList=service.searchZhuBeiKuStatus(ids);
					if(indList!=null && indList.size()>0){
						Map<String, Object> tempInd=new LinkedCaseInsensitiveMap<Object>(); 
						tempInd.putAll(indList.get(0)); 
						try {
							if(tempInd.get("describe").equals("PRIMARY")){
								describe="主库";
							}else if(tempInd.get("describe").equals("PHYSICAL STANDBY")){
								describe="备库";
							}else{
								describe="未做主备";
							}
							value=toString(tempInd.get("describe"));
						} catch (Exception e) {
							logger.error(e);
						}
					}
					//获取scn号
					indList=service.searchScn(ids);
					
					if(indList!=null && indList.size()>0){
						Map<String, Object> tempInd=new LinkedCaseInsensitiveMap<Object>(); 
						tempInd.putAll(indList.get(0)); 
						try {
							scn=toString(tempInd.get("scn"));
						} catch (Exception e) {
							logger.error(e);						}
					}
					db_info+="<span value='"+value+"'>"+describe+"</span>";
					db_info+="<span>SCN："+scn+"</span>";
				}
			}
			if(outLine2==false || dbLine2==false){
				sb2.append("\"outLine\":\"out_db_err_r\",");
			}else{
				sb2.append("\"outLine\":\"out_db_succ_r\",");
			}
			if(inLine2==false || dbLine2==false){
				sb2.append("\"inLine\":\"in_db_err\",");
			}else{
				sb2.append("\"inLine\":\"in_db_succ\",");
			}
			sb2.append("\"outStr\":\""+outStr+"\",\"inStr\":\""+inStr+"\",\"dbStr\":\""+dbStr+"\",\"db_info\":\""+db_info+"\"");
			
			String line_1="sw-green",line_2="sw-green";
			if(outLine1==false || outLine2==false){
				line_1="sw-red";
			}
			if(inLine1==false || inLine2==false){
				line_2="sw-red";
			}
			retStr="[{"+sb1.toString()+"}]"+"%TAB%"+"[{"+sb2.toString()+"}]"+"%TAB%[{\"line_1\":\""+line_1+"\",\"line_2\":\""+line_2+"\"}]";
		}
		
		return retStr+"%TAB%"+double_id;
	}
	/***
	 * 任务切换操作和获取任务流程进度信息
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/executeSwitch")
	public String executeSwitch(HttpServletRequest req,doublemonPo po) throws Exception{	
		String status="success",value="";
		if(po.getType()==1){
			//primary:主库   standby:备库
			String standby_a=po.getStandby_a();//A中心主备标识 
			//String standby_b=po.getStandby_b();//B中心主备标识
			int primaryId = standby_a.equalsIgnoreCase("PRIMARY")?po.getGroup_id_a():po.getGroup_id_b();
			int standbyId = standby_a.equalsIgnoreCase("PRIMARY")?po.getGroup_id_b():po.getGroup_id_a();
			JSONObject obj=new JSONObject();
			int swMode = Integer.parseInt(po.getSwMode());
			if(swMode == 1){
				 //switchover
				 obj=preSetTaskExecService.OracleDgSwitch(primaryId, standbyId, po.getDouble_id());
			}else{
				 //failover
				 obj=preSetTaskExecService.oracleFaileOver(primaryId, standbyId, po.getDouble_id());
			}
			status=toString(obj.get(TaskConfig.TASK_JSON_STATUS));
			if(status.equals(TaskConfig.ERROR)){
				value=toString(obj.get(TaskConfig.TASK_JSON_VALUE));
			}else{
				value=gettaskinfo(po.getDouble_id());
			}
			//添加日志
			String inOrOut = Util.getInOrOut(req);
			User userInfo = (User) req.getSession().getAttribute("user");
			String swName = "";
			if(swMode == 1){
				swName = "switchover";
			}else{
				swName = "failover";
			}
			generservice.insert_oprt_logs("数据库切换","切换",userInfo.getUsername(),"数据库进行（"+swName+"）切换",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}else{
				value=gettaskinfo(po.getDouble_id());
		}
		return "[{\"status\":\""+status+"\",\"value\":\""+value+"\"}]";
	}
	
	/**
	 * 拼接任务进度信息
	 * @param uid 双活id
	 * @return
	 */
	public String gettaskinfo(int double_id){
		StringBuffer sb=new StringBuffer();
		List<Map> list=service.searchTaskInfo(double_id);
		if(list!=null && list.size()>0){
			int allsize=list.size();
			int time=0;
			Map<String, Object> tempList=new LinkedCaseInsensitiveMap<Object>(); 
			for (int i = 0; i <allsize; i++) {
				tempList.putAll(list.get(i));
				int task_step_state=toInteger(tempList.get("task_step_state"));
	          	String  cir_class="",cir_span="";
	          	if(task_step_state==1){
	          		cir_class="cir_gray";
	          	}else if(task_step_state==2 || task_step_state==3){
	          		cir_class="cir_green";
	          		cir_span="cir_finsh";
	          	}else if(task_step_state==4 || task_step_state==5){
	          		cir_class="cir_red";
	          		cir_span="cir_finsh";
	          	}
	          sb.append("<li>");
	          sb.append("<i class='"+cir_class+"'></i>");
	          sb.append("<div> <span id='"+cir_span+"'> "+toString(tempList.get("task_step_desc"))+"，耗时 "+toString(tempList.get("task_step_costtime"))+" 秒 ("+toString(tempList.get("task_step_statename"))+")</span>");
	          sb.append("</div>");
	          sb.append("</li>");
	           try {
	        	   time=time+toInteger(tempList.get("task_step_costtime"));
				} catch (Exception e) {
				}
			}
			tempList.putAll(list.get(0));
			sb.append("%TAB%"+tempList.get("task_step_createtime")+"，耗时 "+time+" 秒:</div>");
			List<Map> list2=service.searchTaskInfos(double_id);
			int size2=0;
			try {
				size2=list2.size();
			} catch (Exception e) {
			}
			if(allsize==size2){//任务执行完成不重新取数
				 sb.append("%TAB%false");
			}else{
				//判断任务是否执行失败
//				List<TaskOperation> taskList = service.getTaskOperation(double_id);
//				if(taskList.size() > 0 && taskList != null){
//					int status = taskList.get(0).getTask_opera_state();
//					if(TaskConfig.TASK_STATE_FAIL == status || TaskConfig.TASK_STATE_ERROR == status){
//						sb.append("%TAB%false");
//						return sb.toString();
//					}
//				}
				sb.append("%TAB%true");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取双活信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getDoubleInfo")
	public Map<String, String> getDoubleInfo(DoublemgtPo po,HttpServletRequest request){
		Map<String, String> map = new HashMap<>();
		try {
			//判断内外网
			String inOrOut = Util.getInOrOut(request);
			po.setPosition(inOrOut);
			//根据双活id查询中心
			List<DoublemgtPo> coreList = service.getCoreInfo(po);
			if(coreList!=null && coreList.size()>0){
				map.put("name", coreList.get(0).getName());
				StringBuffer core = new StringBuffer();
				for (int i = 0; i < coreList.size() && i<1000; i++) {
					core.append(coreList.get(i).getCore()+",");
					po.setCore_id(coreList.get(i).getId());
					//根据中心id查询下面的组
					List<DoublemgtPo> list = service.getDoubleInfo(po);
					if(list!=null && list.size()>0){
						StringBuffer coreSb = new StringBuffer();
						for (int j = 0; j < list.size() && j<1000; j++) {
							coreSb.append(list.get(j).getName()+",");
						}
						map.put(coreList.get(i).getCore(), coreSb.substring(0, coreSb.length()-1));
					}
				}
				map.put("core", core.substring(0, core.length()-1));
			}else{
				map.put("core", "0");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
		
	}
	
	/**
	 * 调用在线人数接口
	 * @param po
	 * @return
	 * @throws AxisFault
	 */
	@ResponseBody
	@RequestMapping(value="/userconut")
	public String search(doublemonPo po) throws AxisFault{
		String value =null;
		String valueOut =null;
		usercount uc = new usercount();
		value = uc.sendCount("in");//内网
		valueOut = uc.sendCount("out");//外网
		//value 有可能要做处理
		int res=0;
		int count = service.select(po);
		if(count>0){
			po.setValue(value);
			res=service.update(po); 
		}else{
			res=service.insert(po);
		}
		return value;
	}
	
	/**
	 * @throws Exception 
	 * 获取数据库数据
	 * @param po
	 * @return
	 * @throws 
	 */
	@ResponseBody
	@RequestMapping(value="/getDatebase")
	public List<doublemonPo> getDatebase(doublemonPo po, HttpServletRequest request) throws Exception{
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		List<doublemonPo> list = service.getDatebase(po);
		return list;
	}
	/**
	 * 获取数据库表空间数据
	 * @param po
	 * @return
	 * @throws 
	 */
	@ResponseBody
	@RequestMapping(value="/getTablespace")
	public Map<String, String> getTablespace(doublemonPo po){
		Map<String, String> map = new HashMap<String, String>();
		try {
			List<paramdbPo> list = service.getTablespace(po);
			if(list != null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					map.put("sum_space", list.get(i).getSum_space());
					map.put("used_space", list.get(i).getUsed_space());
					map.put("used_rate", list.get(i).getUsed_rate());
					map.put("free_space", list.get(i).getFree_space());
					double free_rate = 100 - Double.parseDouble(list.get(i).getUsed_rate());
					map.put("free_rate", RoundX(free_rate, 2)+"");
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
	
	/**
	 * 负载均衡切换,调用负载均衡接口
	 * @return
	 * 
	 * ********http1只读   http2只写********
	 */
	@ResponseBody
	@RequestMapping(value="/doFuZaiJunHengSwitch")
	public String doFuZaiJunHengSwitch(String swMode){
		String res = "";
		String pStr1 = "";
		String pStr2 = "";
		String urlIp1 = "10.30.11.144";
		String urlIp2 = "10.30.11.145";
		try {
			//A中心读写，B中心只读
			if(Integer.parseInt(swMode) == 1){
				pStr1 = "4;2;0;1;10;1;1;14;1;2";//读写
				pStr2 = "4;1;0;10;1;1;14;1;2";//只读
			}else if(Integer.parseInt(swMode) == 2){
				// A中心读写，B中心故障
				pStr1 = "4;2;0;1;10;1;1;14;1;2";
				pStr2 = "10;1;1;14;1;2";
			}else if(Integer.parseInt(swMode) == 3){
				// A中心只读，B中心读写 
				pStr1 = "4;1;0;10;1;1;14;1;2";
				pStr2 = "4;2;0;1;10;1;1;14;1;2";
			}else if(Integer.parseInt(swMode) == 4){
				// A中心故障，B中心读写
				pStr1 = "10;1;1;14;1;2";
				pStr2 = "4;2;0;1;10;1;1;14;1;2";
			}
			
			res = loadBalancingClient.getFuZaiJunHengSwitch(urlIp1, pStr1);
			if(res.equals("success")){
				res = loadBalancingClient.getFuZaiJunHengSwitch(urlIp2, pStr2);
			}else{
				res = "faill";
			}
		} catch (Exception e) {
			logger.error(e);
			return "faill";
		}
		
		return res;
	}
	
	
	public void exeSSHCommand(HttpServletRequest req,String node){
		String path=req.getSession().getServletContext().getRealPath("/")+"WEB-INF/classes/config/F5LoadBalancing.properties";
		try(FileInputStream fis = new FileInputStream(path);
			InputStream in = new BufferedInputStream(fis);){
			Properties pps = new Properties();
			pps.load(in);
			String ssh_host=pps.getProperty("ssh_host");
			int ssh_port=Integer.parseInt(pps.getProperty("ssh_port"));
			String ssh_user=pps.getProperty("ssh_user");
			String ssh_password=pps.getProperty("ssh_password");
			String domain_name = pps.getProperty("domain_name");
																							//替换node
			String ssh_command = "tmsh modify gtm pool a "+domain_name+" members modify { GTM:/Common/vs_mgmt { member-order 0 } }";
			String result = exeCommand(ssh_host,ssh_port,ssh_user,ssh_password,ssh_command);
			System.out.println(result);
		} catch (JSchException | IOException e) {
			logger.error(e);
		}
	}
	
	public static String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");
        channelExec.disconnect();
        session.disconnect();
        return out;
    }
	
	/**
	 * f5负载均衡切换
	 * @param req
	 * @param vsName
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/doF5Switch")
	public String doF5Switch(HttpServletRequest req, String vsName) throws Exception{
		//添加日志
		String inOrOut = Util.getInOrOut(req);
		User userInfo = (User) req.getSession().getAttribute("user");
		generservice.insert_oprt_logs("负载均衡切换","切换",userInfo.getUsername(),"负载均衡进行切换",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
			
		String status="success";
		int ssh_port = 0;
		String ssh_host = "";
		String ssh_user = "";
		String ssh_password = "";
		String poolName = "";
		String vsName1 = "";
		String vsName2 = "";

		DevicesPo po = new DevicesPo();
		po.setDevicetype(10);
		List<DevicesPo> list = devicesService.search(po);
		if(list.size() > 0 && list != null){
			ssh_port = Integer.parseInt(list.get(0).getPort());
			ssh_host = list.get(0).getIn_ip();
			ssh_user = list.get(0).getIn_username();
			RsaDecryptTool rsaTool = new RsaDecryptTool();
			ssh_password = rsaTool.decrypt(list.get(0).getIn_password());
			String remake[] = list.get(0).getRemark().split(",");
			poolName = remake[0];
			vsName1 = remake[1];
			vsName2 = remake[2];
		}
		String ssh_command = "tmsh modify gtm pool a "+poolName+" members modify { severe:/Common/"+vsName2+" { member-order  0 } }";
		String result = exeCommand1(ssh_host,ssh_port,ssh_user,ssh_password,ssh_command);
		
		return result;
	}
	
	public static String exeCommand1(String host, int port, String user, String password, String command) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();
        
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        BufferedReader in = new BufferedReader(new InputStreamReader
                (channelExec.getInputStream()));
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        
        channelExec.disconnect();
        session.disconnect();
        
        //接收远程服务器执行命令的结果
        List<String> stdout = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {  
            stdout.add(line);  
        }  
        in.close();  

        // 得到returnCode
        int returnCode = 0;
        if (channelExec.isClosed()) {  
            returnCode = channelExec.getExitStatus();  
        }  
        
//        System.out.println("returnCode:"+returnCode);
	
       return stdout.toString();
	}
}
