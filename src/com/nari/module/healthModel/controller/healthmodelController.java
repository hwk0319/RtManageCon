package com.nari.module.healthModel.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.general.service.GeneranService;
import com.nari.module.healthModel.po.healthmodelPo;
import com.nari.module.healthModel.po.itemmetricPo;
import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.healthModel.service.healthmodelService;
import com.nari.module.thresholdmgt.po.ThresholdPo;
import com.nari.module.thresholdmgt.service.ThresholdService;
import com.nari.po.User;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.Util;

@Controller
@RequestMapping(value="healthmodelCon")
public class healthmodelController extends BaseController {
	private static Logger logger = Logger.getLogger(healthmodelController.class);
	@Resource(name="healthmodelService")
	private healthmodelService service;
	
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	
	@Resource(name=" thresholdService")
	private  ThresholdService thresService;
	
	
	@ResponseBody
	@RequestMapping(value="/searchModel")
	public List<healthmodelPo> searchModel(healthmodelPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchModel(po);
	}
	@ResponseBody
	@RequestMapping(value="/getindexinfo")
	public List<healthmodelPo> getindexinfo(HttpServletRequest req,HttpServletResponse res, healthmodelPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String index_ids = po.getIndex_ids();
		String index_type = po.getIndex_type();
		if(index_ids.indexOf(",")>-1){
			index_ids=" and index_id not in ("+index_ids.substring(0,index_ids.length()-1)+")";
		}
		String sql="select index_id as model_id ,description as model_desc from mon_index where use_flag = '1' and warn_rule<>0 and index_type="+index_type+index_ids;
		return service.zdysehmode(sql);
	}
	@ResponseBody
	@RequestMapping(value="/searchItem")
	public void searchItem(HttpServletRequest req,HttpServletResponse res, healthmodelPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String model_id = String.valueOf(po.getModel_id());
		String sql="select * from h_model_item where use_flag='1'";
		if(model_id !=null && !"".equals(model_id)){
			sql+=" and model_id="+model_id;
		}else{
			sql+=" and model_id=''";
		}
		List<modelitemPo> list=service.zdyseitem(sql);
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			for (int i = 0,allsize=list.size(); i <allsize; i++) {
				sb.append("<tr ondblclick=\"doclickinfo(this,'edit_item')\" class=\"jqgrow ui-row-ltr ui-widget-content\">");
				sb.append("<td style=\"text-align:center;width: 35px;\" class=\"jqgrid-rownum ui-state-default\">"+(i+1)+"</td>");
				sb.append("<td class=\"isedit\" id=\"model_item_id\" style=\"display:none;\">"+list.get(i).getModel_item_id()+"</td>");
				sb.append("<td class=\"isedit\" id=\"model_item_name\" style=\"text-align:center;width: 100px;\">"+list.get(i).getModel_item_name()+"</td>");
				sb.append("<td class=\"isedit\" id=\"total_score\" readonly=\"readonly\" style=\"text-align:center;width: 100px;\">"+toString(list.get(i).getTotal_score())+"</td>");
				sb.append("<td class=\"isedit\" id=\"model_item_desc\" style=\"text-align:center;width: 200px;\">"+toString(list.get(i).getModel_item_desc())+"</td>");
				sb.append("<td  style=\"text-align:center;\"><a id=\"edit_item\" onclick=\"itemEdit('E',this)\" style=\"display:none;\">编辑</a><a onclick=\"itemEdit('D',this)\" style=\"cursor:pointer;\">删除</a></td>");
				sb.append("</tr>");
			}
		}
		res.getWriter().write(sb.toString());
	}
	
	@ResponseBody
	@RequestMapping(value="/searchItem1")
	public List<modelitemPo> searchItem1(HttpServletRequest req,HttpServletResponse res, healthmodelPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String model_id = String.valueOf(po.getModel_id());
		String sql="select * from h_model_item where use_flag='1'";
		if(model_id !=null && !"".equals(model_id)){
			sql+=" and model_id="+model_id;
		}else{
			sql+=" and model_id=''";
		}
		List<modelitemPo> list=service.zdyseitem(sql);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchMetric")
	public String searchMetric(HttpServletRequest req,HttpServletResponse res, modelitemPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String model_item_id = String.valueOf(po.getModel_item_id());
		String metric_ids = po.getMetric_ids();
		String wid = po.getWid();
		if(metric_ids.indexOf(",")>-1){
			metric_ids=metric_ids.substring(0,metric_ids.length()-1);
			metric_ids=" and metric_id not in ("+metric_ids+")";
		}
		String str="";
		if(MyPropertiesPersist.DBTYPE.equalsIgnoreCase("ORACLE")){
			 str=" and rownum=1 ";
		}else{
			 str=" limit 1";
		}
		String sql="select A.*,(select description from mon_index where use_flag='1' and index_id=A.metric_id "+str+") as metric_id_name from h_model_item_metric A  where use_flag='1'"
				+ " and model_item_id="+model_item_id+metric_ids;
		List<itemmetricPo> list=service.zdysemetric(sql);
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			for (int i = 0,allsize=list.size(); i <allsize; i++) {
				sb.append("<tr ondblclick=\"doclickinfo(this,'edit_metric')\" id=\""+model_item_id+"\"  class=\"jqgrow ui-row-ltr ui-widget-content\">");
				sb.append("<td style=\"text-align:center;width: 35px;\" class=\"jqgrid-rownum ui-state-default\">"+(i+1)+"</td>");
				sb.append("<td class=\"isedit\" id=\"model_item_id\" style=\"display:none;\">"+model_item_id+"</td>");
				sb.append("<td class=\"isedit\" id=\"metric_id\" style=\"display:none;\">"+list.get(i).getMetric_id()+"</td>");
				sb.append("<td class=\"isedit\" id=\"metric_id_name\" style=\"text-align:center;width: "+wid+"px;\" readonly=\"readonly\" onclick=\"getindex(this)\">"+list.get(i).getMetric_id_name()+"</td>");
				sb.append("<td class=\"isedit\" id=\"total_score\" style=\"text-align:center;width: "+wid+"px;\">"+toString(list.get(i).getTotal_score())+"</td>");
				sb.append("<td  style=\"text-align:center;width:"+wid+"px\"><a id=\"edit_metric\" onclick=\"metricEdit('E',this)\" style=\"display:none\">编辑</a><a onclick=\"metricEdit('D',this)\" style=\"cursor:pointer;\">删除</a></td>");
				sb.append("</tr>");
			}
		}
		return sb.toString();
	}
	/**
	 * 获取序列id下一值
	 * @date：2017年7月10日
	 */
	@ResponseBody
	@RequestMapping(value="/getid")
	public String getid(HttpServletRequest req,HttpServletResponse res, healthmodelPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String tabname = po.getTabname();
		int id=0;
		if(tabname.equalsIgnoreCase("h_model_item")){
			String sql="select max(model_item_id) as model_item_id from h_model_item";
			List<modelitemPo> list=service.zdyseitem(sql);
			try {
				id=list.get(0).getModel_item_id();
			} catch (Exception e) {
				logger.error(" 获取序列id值错误:"+e.toString());
			}
		}
		return ""+(id+1);
	}
	/**
	 * 保存
	 * @date：2017年7月10日
	 */
	@ResponseBody
	@RequestMapping(value="/saveinfo")
	public void saveinfo(HttpServletRequest req,HttpServletResponse res, healthmodelPo pom) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String inOrOut = Util.getInOrOut(req);
		String modeStr = pom.getModeStr();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Date date2=null;
		try {
			date2=df.parse(df.format(date));
		} catch (Exception e) {
		}
		String tt="";
		if(MyPropertiesPersist.DBTYPE.equalsIgnoreCase("ORACLE")){
			 tt=" sysdate";
		}else{
			 tt=" now()";
		}
		List<Map<String,Object>> modelList=jsonToList(modeStr);
		int result=0;
		int total=0;
		/**
		 * 删除
		 */
		deleteinfo(req, res, pom);
		/**
		 * 新增or修改
		 */
		if(modelList!=null && modelList.size()>0){
			User userInfo = (User) req.getSession().getAttribute("user");
			String userid=toString(userInfo.getId());
			/**
			 * 1、处理健康模型表
			 */
			String model_id=toString(modelList.get(0).get("model_id"));
			String model_name=toString(modelList.get(0).get("model_name"));
			String model_desc=toString(modelList.get(0).get("model_desc"));
			try {
				if(model_id.equals("") || model_id==null){
					healthmodelPo po=new healthmodelPo();
					po.setDBTYPE(MyPropertiesPersist.DBTYPE);
					po.setModel_name(model_name);
					po.setModel_desc(model_desc);
					po.setCreate_by(userid);
					po.setCreate_date(date2);
					result=service.insertModel(po);//新增模型
					model_id=toString(po.getModel_id());
					generservice.insert_oprt_log("基线管理-健康模型","新增",userInfo.getUsername(),"新增模型"+model_name,userInfo.getIpaddr(),po.getModel_id(),inOrOut);
				}else{
					String sql="update h_health_model set model_name='"+model_name+"',model_desc='"+model_desc+"'"
							+ ",update_by='"+userid+"',update_date="+tt+",use_flag='1'  where model_id="+model_id;
					result=service.update(sql);//修改模型
					generservice.insert_oprt_log("基线管理-健康模型","修改",userInfo.getUsername(),"修改模型"+model_name,userInfo.getIpaddr(),toInteger(model_id),inOrOut);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/**
			 * 2、处理模型分项表
			 */
			String item_metric = pom.getItem_metric();
			List<Map<String,Object>> itemList=jsonToList(item_metric);
			if(itemList!=null && itemList.size()>0 && result>0){
				for (int i = 0,itemSize=itemList.size(); i <itemSize; i++) {
					int A=1;
					modelitemPo itempo=new modelitemPo();
					int model_item_id=toInteger(itemList.get(i).get("model_item_id"));
					String model_item_name=toString(itemList.get(i).get("model_item_name"));
					String model_item_desc=toString(itemList.get(i).get("model_item_desc"));
					try {
						String sql="select * from h_model_item where model_item_id="+model_item_id;
						List<modelitemPo> list=service.zdyseitem(sql);
						if(list!=null&&list.size()>0){
							sql="update h_model_item set model_item_name='"+model_item_name+"',model_item_desc='"+model_item_desc+"'"
									+ ",update_by='"+userid+"',update_date="+tt+",use_flag='1'  where model_item_id="+model_item_id;
							result=service.update(sql);
							A=2;
						}else{
							itempo.setDBTYPE(MyPropertiesPersist.DBTYPE);
							itempo.setModel_item_id(model_item_id);
							itempo.setModel_item_name(model_item_name);
							itempo.setModel_item_desc(model_item_desc);
							itempo.setModel_id(Integer.parseInt(model_id));
							itempo.setCreate_by(userid);
							itempo.setCreate_date(date2);
							result=service.insertItem(itempo);
						}
					} catch (Exception e) {
					}
					/**
					 * 3、处理模型分项指标表
					 */
					try {
						String metricStr=toString(itemList.get(i).get("model_item_metric"));
						List<Map<String,Object>> metricList=jsonToList(metricStr);
						if(metricList!=null && metricList.size()>0){
							if(A==1){
								generservice.insert_oprt_log("基线管理-健康模型","新增",userInfo.getUsername(),"健康模型:"+model_name+"下新增模型分项"+model_item_name,userInfo.getIpaddr(),toInteger(model_id),inOrOut);
							}else{
								generservice.insert_oprt_log("基线管理-健康模型","编辑",userInfo.getUsername(),"修改了健康模型:"+model_name+"下的模型分项"+model_item_name,userInfo.getIpaddr(),toInteger(model_id),inOrOut);
							}
							for (int j = 0,metricize=metricList.size(); j <metricize; j++) {
								itemmetricPo metricpo=new itemmetricPo();
								int metric_id=toInteger(metricList.get(j).get("metric_id"));
								int total_score=toInteger(metricList.get(j).get("total_score"));
								try {
									String sql="select * from h_model_item_metric where model_item_id="+model_item_id+" and metric_id="+metric_id;
									List<itemmetricPo> list=service.zdysemetric(sql);
									if(list!=null && list.size()>0){
										sql="update h_model_item_metric set total_score="+total_score+""
												+ ",update_by='"+userid+"',update_date="+tt+",use_flag='1'  where model_item_id="+model_item_id+" and metric_id="+metric_id;
										result=service.update(sql);
										generservice.insert_oprt_log("基线管理-健康模型","编辑",userInfo.getUsername(),"修改健康模型:"+model_name+"下指标分项"+metric_id,userInfo.getIpaddr(),toInteger(model_id),inOrOut);
									}else{
										metricpo.setDBTYPE(MyPropertiesPersist.DBTYPE);
										metricpo.setModel_item_id(model_item_id);
										metricpo.setMetric_id(metric_id);
										metricpo.setTotal_score(total_score);
										metricpo.setCreate_by(userid);
										metricpo.setCreate_date(date2);
										result=service.insertMetric(metricpo);
										generservice.insert_oprt_log("基线管理-健康模型","新增",userInfo.getUsername(),"健康模型:"+model_name+"下新增指标分项"+metric_id,userInfo.getIpaddr(),toInteger(model_id),inOrOut);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							String sql="";
							//清理模型指标
							String metric_ids=toString(itemList.get(i).get("metric_ids"));
							if(metric_ids.indexOf(",")>0){
								sql="delete from h_model_item_metric where use_flag='1' and model_item_id="+model_item_id+" and metric_id not in ("+metric_ids.substring(0,metric_ids.length()-1)+")";
							}
							result=service.update(sql);
							//修改模型分项的总分
							sql="update h_model_item A set total_score=(select sum(total_score) from h_model_item_metric where use_flag='1' and  A.model_item_id=model_item_id) where model_item_id="+model_item_id;
							result=service.update(sql);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//修改健康模型的总分
				String sql="select sum(total_score) as total_score from h_model_item where use_flag='1' and  model_id="+model_id;
				List<healthmodelPo> list=service.zdysehmode(sql);
				if(list!=null && list.size()>0){
					try {
						total=list.get(0).getTotal_score();
					} catch (Exception e) {
					}
				}
				sql="update h_health_model A set total_score="+total+" where model_id="+model_id;
				service.update(sql);
			}
		}
		res.getWriter().write(total+"");
	}
	/**
	 * 删除操作
	 * @date：2017年7月10日
	 */
	public void deleteinfo(HttpServletRequest req,HttpServletResponse res, healthmodelPo po) throws Exception{	
		User userInfo = (User) req.getSession().getAttribute("user");
		res.setContentType("text/html;charset=UTF-8");
		String inOrOut = Util.getInOrOut(req);
		String del_item_arr = po.getDel_item_arr();
		try {
			if(!"".equals(del_item_arr)){
				if(del_item_arr.indexOf(",")<0){
					del_item_arr=del_item_arr+",";
				}
				String[] item_ids=del_item_arr.split(",");
				for (int i = 0,allsize=item_ids.length; i <allsize; i++) {
					String model_item_id=item_ids[i];
					String sql="update h_model_item set use_flag='0' where model_item_id="+model_item_id;
					service.update(sql);
					sql="update h_model_item_metric set use_flag='0' where model_item_id="+model_item_id;
					service.update(sql);
					generservice.insert_oprt_log("基线管理-健康模型","删除",userInfo.getUsername(),"删除了一条模型分项",userInfo.getIpaddr(),toInteger(model_item_id),inOrOut);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		String del_metric_arr = po.getDel_metric_arr();
		try {
			if(!"".equals(del_metric_arr)){
					if(del_metric_arr.indexOf("%TAB%")<0){
						del_metric_arr=del_metric_arr+"%TAB%";
					}
				String[] sqls=del_metric_arr.split("%TAB%");
				for (int i = 0,allsize=sqls.length; i <allsize; i++) {
					String sql=sqls[i];
					service.update(sql);
					try {
						String[] arr=sql.split("and");
						String model_item_id_1=arr[0].split("model_item_id=")[1];
						int model_item_id=Integer.parseInt(model_item_id_1.trim());
						generservice.insert_oprt_log("基线管理-健康模型","删除",userInfo.getUsername(),"删除了一条分项指标",userInfo.getIpaddr(),model_item_id,inOrOut);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	@ResponseBody
	@RequestMapping(value="/delModel")
	public void delModel(HttpServletRequest req,HttpServletResponse res, healthmodelPo po) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String model_id = String.valueOf(po.getModel_id());
		String sql="update h_health_model set use_flag='0' where model_id="+model_id;
		int num=service.update(sql);
		if(num>0){
			sql="update h_model_item set use_flag='0' where model_id="+model_id;
			num=service.update(sql);
			if(num>0){
				sql="update h_model_item_metric set use_flag='0' where model_item_id in (select model_item_id from h_model_item where model_id="+model_id+")";
				num=service.update(sql);
			}
		}
		res.getWriter().write(num+"");
	}
	/**
	 * 复制
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/copyModel")
	public void copyModel(HttpServletRequest req,HttpServletResponse res, healthmodelPo pom) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		String old_model_id = String.valueOf(pom.getModel_id());
		String model_name = pom.getModel_name();
		String model_desc = pom.getModel_desc();
		String sql="select * from h_health_model where model_id="+old_model_id;
		List<healthmodelPo> list=service.zdysehmode(sql);
		if(list!=null && list.size()>0){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			Date date2=null;
			try {
				date2=df.parse(df.format(date));
			} catch (Exception e) {
			}
			User userInfo = (User) req.getSession().getAttribute("user");
			String userid=toString(userInfo.getId());
			/**
			 * 1、添加健康模型
			 */
			healthmodelPo po=new healthmodelPo();
			po.setDBTYPE(MyPropertiesPersist.DBTYPE);
			po.setModel_name(model_name);
			po.setTotal_score(list.get(0).getTotal_score());
			po.setModel_desc(model_desc);
			po.setCreate_by(userid);
			po.setCreate_date(date2);
			int result=service.insertModel(po);
			int new_model_id=toInteger(po.getModel_id());
			if(result>0){
				sql="select * from h_model_item where use_flag='1' and model_id="+old_model_id;
				List<modelitemPo> listItem=service.zdyseitem(sql);
				if(listItem!=null && listItem.size()>0){
					for (int i = 0,itemSize=listItem.size(); i <itemSize; i++) {
						int old_model_item_id=toInteger(listItem.get(i).getModel_item_id());
						/**
						 * 2、复制模型分项
						 */
						modelitemPo itempo=new modelitemPo();
						List<modelitemPo> list1=service.zdyseitem("select max(model_item_id) as model_item_id from h_model_item");
						int last_model_item_id=0;
						int new_model_item_id=1;
						try {
							last_model_item_id=list1.get(0).getModel_item_id();
							new_model_item_id=last_model_item_id+1;
						} catch (Exception e) {
						}
						itempo.setDBTYPE(MyPropertiesPersist.DBTYPE);
						itempo.setModel_item_id(new_model_item_id);
						itempo.setModel_item_name(listItem.get(i).getModel_item_name());
						itempo.setModel_item_desc(listItem.get(i).getModel_item_desc());
						itempo.setModel_id(new_model_id);
						itempo.setTotal_score(listItem.get(i).getTotal_score());
						itempo.setCreate_by(userid);
						itempo.setCreate_date(date2);
						result=service.insertItem(itempo);
						if(result>0){
							/**
							 * 3、复制模型分项指标
							 */
							sql="select * from h_model_item_metric where use_flag='1' and model_item_id="+old_model_item_id;
						    List<itemmetricPo> listMetric=service.zdysemetric(sql);
						    if(listMetric!=null && listMetric.size()>0){
						    	String metric_ids="";
						    	for (int j = 0,metricSize=listMetric.size(); j <metricSize; j++) {
						    		int metric_id=toInteger(listMetric.get(j).getMetric_id());
						    		itemmetricPo metricpo=new itemmetricPo();
						    		metricpo.setDBTYPE(MyPropertiesPersist.DBTYPE);
						    		metricpo.setModel_item_id(new_model_item_id);
									metricpo.setMetric_id(metric_id);
									metricpo.setTotal_score(listMetric.get(j).getTotal_score());
									metricpo.setCreate_by(userid);
									metricpo.setCreate_date(date2);
									result=service.insertMetric(metricpo);
									metric_ids+=metric_id+",";
								}
						    	/**
								 * 4、复制指标阈值
								 */
						    	sql="select * from h_metric_rule where use_flag='1' and model_id="+old_model_id+" "
										+ " and model_item_id="+old_model_item_id+" and metric_id in ("+metric_ids.substring(0,metric_ids.length()-1)+")";
						    	List<Map<String,Object>> listRule=service.customSearch(sql);
								if(listRule!=null && listRule.size()>0){
									for (int j = 0,ruleSize=listRule.size(); j <ruleSize; j++) {
										ThresholdPo thresPo=new ThresholdPo();
										thresPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
										thresPo.setRule_name(toString(listRule.get(j).get("rule_name")));
										thresPo.setMetric_id(toInteger(listRule.get(j).get("metric_id")));
										thresPo.setModel_item_id(new_model_item_id);
										thresPo.setModel_id(new_model_id);
										thresPo.setMethod1(toString(listRule.get(j).get("method1")));
										thresPo.setMethod2(toString(listRule.get(j).get("method2")));
										thresPo.setMethod3(toString(listRule.get(j).get("method3")));
										thresPo.setMetric_value1(toString(listRule.get(j).get("metric_value1")));
										thresPo.setMetric_value2(toString(listRule.get(j).get("metric_value2")));
										thresPo.setMetric_value3(toString(listRule.get(j).get("metric_value3")));
										thresPo.setDeduct1(toInteger(listRule.get(j).get("deduct1")));
										thresPo.setDeduct2(toInteger(listRule.get(j).get("deduct2")));
										thresPo.setDeduct3(toInteger(listRule.get(j).get("deduct3")));
										thresPo.setCreate_by(userid);
										thresPo.setCreate_date(date2);
										thresService.insert(thresPo);
									}
								}
						    }
						}
					}
				}
			}
		}
	}
	/**
	 * json转List<Map<String,Object>>
	 * @param json:json格式的值
	 * @return List<Map<String,Object>>：返回类型 
	 * @date：2017年7月10日
	 */
	public static List<Map<String,Object>> jsonToList(String json) {
		List<Map<String,Object>> list=null;
		try {
			JSONArray ja = JSONArray.fromObject(json);  
			list = ja;  
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return list;  
	}
}
