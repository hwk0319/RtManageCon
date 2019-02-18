package com.nari.module.heathScore.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.general.service.GeneranService;
import com.nari.module.healthModel.po.healthmodelPo;
import com.nari.module.healthModel.po.modelitemPo;
import com.nari.module.heathScore.po.HeathScoreDetailPo;
import com.nari.module.heathScore.po.HeathcheckDeductPo;
import com.nari.module.heathScore.po.HeathcheckPo;
import com.nari.module.heathScore.service.HeathcheckService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.util.ZkClientUtils;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping(value="heathcheckCon")
public class HeathcheckController extends BaseController {
	
	private Logger logger = Logger.getLogger(HeathcheckController.class);

	@Resource(name="heathcheckService")
	private HeathcheckService service;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<HeathcheckPo> search(HeathcheckPo po, HttpServletRequest request) throws Exception{
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<HeathcheckPo> list = service.search(po);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("健康评分","查询");
		if(result){
			generservice.insert_oprt_logs("系统优化-健康评分","查询",userInfo.getUsername(),"用户查询健康评分数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return list;
	}

	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@ResponseBody
	@RequestMapping(value="/update")
	public int update(HeathcheckPo po, HttpServletRequest request, String oper) throws Exception{
		String inOrOut = Util.getInOrOut(request);
		//判断是否重复提交
		if(!Util.getSessionJWT(request, po.getJwt())){
			return -2;
		}
		User userInfo = (User) request.getSession().getAttribute("user");
		int res=0;
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		if(oper != null && oper.equals("saveOrUpdate")){
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getHealth_check_id() == null ? "" : po.getHealth_check_id()).append(po.getTarget_id()).append(po.getModel_id()).append(po.getCron());
			String newMdata = MD5.GetMD5Code(sb.toString());
			if(!mdate.equals(newMdata)){
				return -1;
			}
			//时间格式转换
			String begintime = po.getBegintime();
			String endtime = po.getEndtime();
			if(begintime !=null && !"".equals(begintime)){
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					po.setBegin_time(date.parse(begintime));
					po.setEnd_time(date.parse(endtime));
				} catch (ParseException e) {
					
				}
			}
			po.setStatus(0);//状态，0等待开始
			if("".equals(po.getCron()))po.setCron(null);
			//判断内外网
//			String inOrOut = Util.getInOrOut(request);
			po.setPosition(inOrOut);
			po.setDBTYPE(MyPropertiesPersist.DBTYPE);
			//通过id判断操作为添加还是修改
			if(po.getHealth_check_id() == null){
				res = service.insert(po);
				//判断是否需要添加审计数据
				boolean result = isAudit("健康评分","新增");
				if(result){
					generservice.insert_oprt_log("基线管理-健康评分","新增",userInfo.getUsername(),"新增了一条评分记录",userInfo.getIpaddr(),po.getHealth_check_id(),inOrOut);
				}
			}else{
				res = service.update(po);
				generservice.insert_oprt_log("基线管理-健康评分","编辑",userInfo.getUsername(),"修改了一条评分记录",userInfo.getIpaddr(),po.getHealth_check_id(),inOrOut);
			}
		}else if(oper != null && oper.equals("del"))
		{
			int Health_check_id=po.getId();
			po.setDBTYPE(MyPropertiesPersist.DBTYPE);
			res = service.delete(po);
			generservice.insert_oprt_log("基线管理-健康评分","删除",userInfo.getUsername(),"删除了一条评分记录",userInfo.getIpaddr(),Health_check_id,inOrOut);
			ZkClientUtils.dropNode(po.getId());
		}
		//保存/编辑成功移除jwt
		request.getSession().removeAttribute("jwt");
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchDetalByCheckId")
	public List<HeathScoreDetailPo> searchDetalByCheckId(HeathScoreDetailPo po){
		List<HeathScoreDetailPo> list = null;
		try {
			list = service.searchDetalByCheckId(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchModel")
	public List<healthmodelPo> searchModel(healthmodelPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		
//		List<HeathcheckPo> list = service.searchModelId();
//		if(list.size()>0){
//			StringBuffer sb = new StringBuffer();
//			for (int i = 0; i < list.size(); i++) {
//				if(i==0){
//					sb.append(list.get(0).getModel_id());
//				}else{
//					sb.append(","+list.get(0).getModel_id());
//				}
//			}
//			po.setModel_id(Integer.parseInt(sb.toString()));
//		}
		return service.searchModel(po);
	}
	
	/**
	 * 开始评分 创建节点
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/doScore")
	public String doScore(HeathcheckPo po,HttpServletRequest request) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		//修改状态为正在评估中
		po.setStatus(1);//状态，0等待开始，1正在进行评估，2评估完成
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		String inOrOut = Util.getInOrOut(request);
		int res = service.updateStatus(po);
		//判断是否需要添加审计数据
		boolean result = isAudit("健康评分","评分");
		if(result){
			generservice.insert_oprt_log("基线管理-健康评分","评分",userInfo.getUsername(),"开始评分",userInfo.getIpaddr(),po.getHealth_check_id(),inOrOut);
		}
		//zk创建节点
		String path = ZkClientUtils.createNode(po.getHealth_check_id());
		return path;
	}
	
	/**
	 * 停止评分删除节点
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/stopScore")
	public String stopScore(HeathcheckPo po,HttpServletRequest request) throws Exception{
		User userInfo = (User) request.getSession().getAttribute("user");
		po.setStatus(0);//状态，0等待开始，1正在进行评估，2评估完成
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		String inOrOut = Util.getInOrOut(request);
		int res = service.updateStatus(po);
		//判断是否需要添加审计数据
		boolean result = isAudit("健康评分","停止");
		if(result){
			generservice.insert_oprt_log("基线管理-健康评分","停止",userInfo.getUsername(),"停止评分",userInfo.getIpaddr(),po.getHealth_check_id(),inOrOut);
		}
		ZkClientUtils.dropNode(po.getHealth_check_id());
		return String.valueOf(res);
	}
	
	/**
	 * 根据模型id查询模型分项
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchModelById")
	public List<modelitemPo> searchModelById(modelitemPo po){
		List<modelitemPo> list = service.searchModelById(po);
		return list;
	}
	
	/**
	 * 获取优化建议
	 * 2017年12月13日14:34:44
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/getAdvice")
	public Map<String,String> getAdvice(HeathcheckPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<HeathcheckPo> list = service.search(po);
		int model_id =  list.get(0).getModel_id();
		po.setModel_id(model_id);
		Map<String,String> map = searchLeidatuItems(po);
		Map<String,String> map1 = searchLeidatuDate(po);
		for(String key : map1.keySet()){
			map.put("score", map1.get(key));
		}
		return map;
	}
	/**
	 * 查询雷达图模型分项
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchLeidatuItems")
	public Map<String,String> searchLeidatuItems(HeathcheckPo po){
		//定义返回map集合
		Map<String,String> maps = new HashMap<String, String>();
		try {
			List<modelitemPo> mitemList = service.searchModelByModelId(po);
			StringBuffer items = new StringBuffer();
			StringBuffer itemValue = new StringBuffer();
			if(mitemList != null){
				for (int i = 0; i < mitemList.size(); i++) {
					items.append(mitemList.get(i).getModel_item_name()+",");
					itemValue.append(mitemList.get(i).getTotal_score()+",");
				}
				if(items.length() >0 && !"".equals(items)){
					//删除末尾的,
					items.delete(items.length()-1, items.length());
					itemValue.delete(itemValue.length()-1, itemValue.length());
				}
			}
			maps.put("itemsName", items.toString());
			maps.put("itemsValue", itemValue.toString());
		} catch (Exception e) {
			logger.error(e);
		}
		//根据模型id查询模型分项
		return maps;
	}
	
	/**
	 * 首页雷达图评分数据，分项得分，分项扣分
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchLeidatuDate")
	public Map<String,String> searchLeidatuDate(HeathcheckPo po){
		//定义返回map集合
		Map<String,String> maps = new HashMap<String, String>();
		
		//根据模型id查询模型分项
		List<modelitemPo> mitemList = service.searchModelByModelId(po);
		//查询评分数据
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<HeathcheckPo> hcList = service.search(po);
		
		for (int a = 0; a < hcList.size(); a++) {
			
			//根据health_check_id查询最新的一次评分详情,扣分项
			HeathScoreDetailPo hcheckPo = new HeathScoreDetailPo();
			hcheckPo.setHealth_check_id(hcList.get(a).getHealth_check_id());
			hcheckPo.setModel_id(po.getModel_id());
			List<HeathScoreDetailPo> hcheckList = service.searchNewHeathDetail(hcheckPo);
			
			StringBuffer valsb = new StringBuffer();
			//判断是否有评分
			if(hcheckList.size()>0 && hcheckList != null){
				for (int i = 0; i < mitemList.size(); i++) {
					//没有扣分项
					StringBuffer noScoresb = new StringBuffer();
					for (int j = 0; j < hcheckList.size(); j++) {
						if(mitemList.get(i).getModel_item_id() == hcheckList.get(j).getModel_item_id()){
							noScoresb.append(mitemList.get(i).getModel_item_name()+",");
						}
					}
					if(noScoresb.length() == 0 || noScoresb == null){
						//没有扣分项就是满分
						int dScore = mitemList.get(i).getTotal_score();
						valsb.append(""+dScore+",");
					}else{
						int zScore = 0;//总分
						int kScore = 0;//扣分
						int dScore = 0;//得分
						zScore = mitemList.get(i).getTotal_score();
						//有扣分项,计算得分
						for (int j = 0; j < hcheckList.size(); j++) {
							//如果有扣分项，计算得分
							if(mitemList.get(i).getModel_item_id() == hcheckList.get(j).getModel_item_id()){
								//得分=总分-扣分
								kScore += hcheckList.get(j).getDeduct();
							}
						}
						dScore = zScore - kScore;
						if(dScore < 0){
							dScore = 0;
						}
						valsb.append(""+dScore+",");
					}
				}
				valsb.delete(valsb.length()-1, valsb.length());
				maps.put(hcList.get(a).getSystem_name(), valsb.toString());
			}else{
				//没有评分
			}
		}
		return maps;
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
	 * 优化建议相关
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/getItemScoer")
	public String getItemScoer(HeathcheckPo po){
		StringBuffer sb=new StringBuffer();
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		//查询最新的一次评分
		List<HeathcheckDeductPo> hdList = service.searchHeathScoreDedect(po);
		if(hdList != null && hdList.size() > 0){
			String record_time=hdList.get(0).getRecord_time();
			//查询健康评估对应模型下的分项信息
//			List<Map> list=service.searchHealthModelItemById(po.getHealth_check_id());
			String sql="select model_id,model_item_id,model_item_name,total_score from h_model_item where use_flag=1 "
					+ "and model_id=(select model_id from h_health_check where use_flag=1 and health_check_id="+po.getHealth_check_id()+" and rownum = 1) ORDER BY model_item_id asc";
			List<Map> list=service.customSearch(sql);
			if(list!=null && list.size()>0){
				String model_id=toString(list.get(0).get("MODEL_ID"));
				for (int i = 0,allsize=list.size(); i <allsize; i++) {
					String model_item_id=toString(list.get(i).get("MODEL_ITEM_ID"));//分项id
					String retStr=getItemMetric2(po.getHealth_check_id(), model_id, model_item_id, record_time);
					String ItemMetric="";
					if(!"".equals(retStr)){
						String[] arr=retStr.split("%TAB%");
						ItemMetric=arr[0];
					}
					sb.append("<div id=\"checkDetail_Div\"  class=\"layui-colla-item\">");
					sb.append("  <h2  class=\"layui-colla-title\">"+list.get(i).get("MODEL_ITEM_NAME")+"</h2>");
					sb.append(" <div class=\"layui-colla-content layui-show\" id=\"itemMetric\">");
					sb.append(ItemMetric);
					sb.append(" </div>");
					sb.append("</div>");
				}
			 }
		}
		return sb.toString();
	}
	
	/**
	 * 优化建议获取指标值 得分
	 * @param health_check_id
	 * @param model_id
	 * @param model_item_id
	 * @param record_time
	 * @return
	 * @throws Exception
	 */
	public String getItemMetric2(Integer health_check_id,String model_id,String model_item_id,String record_time){	
		//分项下所有指标
//		List<Map> list=service.searchHealthModelItemMetricById(model_item_id);
		String sql1="select metric_id,total_score,(select description from mon_index where use_flag=1 and index_id=h_model_item_metric.metric_id and rownum = 1) as metric_id_name "
				+ "from h_model_item_metric where use_flag=1 and model_item_id="+model_item_id;
		List<Map> list=service.customSearch(sql1);
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			String detail_sql="";
			String detail_sql2="";//各指标查询时间前10次的累积sql
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String record_timef = "";
			try {
				record_timef = date.format(date.parse(record_time));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for (int i = 0,allsize=list.size(); i <allsize; i++) {
				int metric_id = toInteger(list.get(i).get("METRIC_ID"));
				int total_score = toInteger(list.get(i).get("TOTAL_SCORE"));
					String sql="select deduct,metric_value from h_health_check_detail where health_check_id="+health_check_id+" "
					   + "and model_id="+model_id+" and model_item_id="+model_item_id+" and metric_id="+metric_id;
					detail_sql+=" select '"+metric_id+"' as metric_id,'"+total_score+"' as total_score,'"+list.get(i).get("METRIC_ID_NAME")+"' as metric_id_name,deduct,metric_value from "
							+ "("+sql+" and record_time=to_timestamp('"+record_timef+"', 'YYYY-MM-DD HH24:MI:SS') and rownum = 1 order by record_time desc ) a ";
					detail_sql2+="select sum(deduct) as deduct from ("+sql+" and record_time<=to_timestamp('"+record_timef+"', 'YYYY-MM-DD HH24:MI:SS') and rownum <=10 order by record_time desc) b";
				if(i!=allsize-1){
					detail_sql+=" union ";
					detail_sql2+=" union ";
				}
			}
			double item_sc_deduct=0;//分项各指标的累积分
			list=service.customSearch(detail_sql);
			 if(list!=null && list.size()>0){
			    sb.append("<table id=\"ItemMetricTable\" style=\"BORDER-COLLAPSE: collapse; width: 100%; font-size: 13px;\">");
				sb.append(" <thead>");
			    sb.append("<tr style=\"background-color: #f6f8fa;text-align: center;\">");
			    sb.append("<td style='width:35%;'>指标名称</td> <td style='width:10%;'>指标值</td> <td style='width:10%;'>分值</td> <td style='width:10%;'>得分</td> <td style='width:30%;'>建议</td>");
			    sb.append("</tr>");
		        sb.append(" </thead>");
		        sb.append("<tbody>");
		        
				for (int i = 0,allsize=list.size(); i <allsize; i++) {
					double total_score=toDouble(list.get(i).get("TOTAL_SCORE"));//总分
					double deduct=toDouble(list.get(i).get("DEDUCT"));//扣分值
					double metric_value=toDouble(list.get(i).get("METRIC_VALUE"));
					double sc_deduct=total_score-deduct;//得分
					sc_deduct=sc_deduct>0?sc_deduct:0;
					item_sc_deduct+=sc_deduct;
					double adv = Double.parseDouble(RoundX(sc_deduct / total_score, 2));
					if(adv >= 0.5 && adv < 0.85){
						sb.append("<tr style='color: #f3a106c7;'>");
					}else if(adv < 0.5){
						sb.append("<tr style='color: red;'>");
					}else{
						sb.append("<tr>");
					}
			        sb.append("<td>"+list.get(i).get("METRIC_ID_NAME")+"</td>");
			        sb.append("<td>"+RoundX(metric_value, 2)+"</td>");
			        sb.append("<td>"+RoundX(total_score, 2)+"</td>");
			        sb.append("<td>"+RoundX(sc_deduct, 2)+"</td>");
			        //判断得分 给出建议
			        String advice = "";
			        if(1.0 >= adv && adv > 0.85){
			        	advice = "<span style=''></span>";
			        }else if(0.85 > adv && adv >= 0.5){
			        	advice = "<span style='color: #f3a106c7;'>建议持续关注！</span>";
			        }else{
			        	advice = "<span style='color: red;'>建议优化指标！</span>";
			        }
			        sb.append("<td>"+advice+"</td>");
			        sb.append("</tr>");
				}
				sb.append("</tbody>");
			    sb.append("</table>");
			 }
			 
			 /* hwk 添加内容 start  */
		        //判断每个指标是否都有数据
			 String itemsql="select metric_id,total_score,(select description from mon_index where use_flag=1 and index_id=h_model_item_metric.metric_id and rownum =1) as metric_id_name "
					 + "from h_model_item_metric where use_flag=1 and model_item_id="+model_item_id;
				int metricval = 0;
//		        List<Map> metriclist=service.searchHealthModelItemMetricById(model_item_id);
				 List<Map> metriclist=service.customSearch(itemsql);
		        if(metriclist != null){
		        	if(metriclist.size() == list.size()){
		        	//指标都有数据
		        }else{
		        	for (int i = 0; i < metriclist.size(); i++) {
		        		StringBuffer itsb = new StringBuffer();
		        		for (int j = 0; j < list.size(); j++) {
		        			//判断哪个指标没有数据
	        				int metricid = Integer.parseInt(list.get(j).get("METRIC_ID").toString());
		        			int itemmetricid = Integer.parseInt(metriclist.get(i).get("METRIC_ID").toString());
		        			if(itemmetricid == metricid){
		        				itsb.append(metriclist.get(i).get("METRIC_ID")+",");
		        			}
						}
		        		if(itsb.length() > 0 && itsb != null){
		        			itsb.delete(itsb.length()-1, itsb.length());
		        		}else{
		        			metricval += Integer.parseInt(metriclist.get(i).get("TOTAL_SCORE").toString());
			        		}
		        		}
					}
		        }
		         /*hwk 添加内容 end*/
			 
		    //取累积扣分项
			detail_sql2="select sum(deduct) as sum_deduct from ("+detail_sql2+") c";
			list=service.customSearch(detail_sql2);
			double max_deduct=0,min_deduct=0,sum_deduct=0;
			
			try {
				sum_deduct=toDouble(list.get(0).get("sum_deduct"));
				sum_deduct += metricval;//总扣分
				item_sc_deduct += metricval;//当前得分
			} catch (Exception e) {
			}
//			list=service.searchRecordTime(health_check_id,model_id,model_item_id,record_timef);
			String record_times = "";
			try {
				record_times = date.format(date.parse(record_time));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String sql3="select to_char(record_time, 'YYYY-MM-DD HH24:MI:SS') as record_time from h_health_check_detail where health_check_id="+health_check_id+" and model_id="+model_id+" and model_item_id="+model_item_id+" "
					+ " and record_time<=to_timestamp('"+record_times+"', 'YYYY-MM-DD HH24:MI:SS') and rownum <=10 group by record_time order by record_time desc";
		list=service.customSearch(sql3);
			if(list!=null && list.size()>0){
				String detail_sql3="";
				for (int i = 0,allsize=list.size(); i <allsize; i++) {
						detail_sql3+="select sum(deduct) as deduct from h_health_check_detail where health_check_id="+health_check_id+" and model_id="+model_id+" and model_item_id="+model_item_id+" "
								+ " and record_time=to_timestamp('"+list.get(i).get("RECORD_TIME")+"', 'YYYY-MM-DD HH24:MI:SS')";
					if(i!=allsize-1){
						detail_sql3+=" union ";
					}
				}
				detail_sql3="select max(deduct) as max_deduct,min(deduct) as min_deduct from ("+detail_sql3+") a";
				list=service.customSearch(detail_sql3);
				try {
					max_deduct=toDouble(list.get(0).get("max_deduct"));
					min_deduct=toDouble(list.get(0).get("min_deduct"));
				} catch (Exception e) {
				}
			}
			sb.append("%TAB%"+item_sc_deduct+"%TAB%"+sum_deduct+"%TAB%"+max_deduct+"%TAB%"+min_deduct);
		}
		return sb.toString();
	}
	
}
