package com.nari.module.paramdb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.paramdb.po.paramdbPo;
import com.nari.module.paramdb.service.paramdbService;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.Util;

@Controller
@RequestMapping(value="paramdbCon")
public class paramdbController extends BaseController {
	private Logger logger = Logger.getLogger(paramdbController.class);
	@Resource(name="paramdbService")
	private paramdbService service;
	/**
	 * 获取数据库列表数据
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/getdataList")
	public String getDataList(HttpServletRequest req,paramdbPo po) throws Exception{
		StringBuffer sb=new StringBuffer();
		if(po.getKw()!=null && !"".equals(po.getKw())){
			po.setSearchText(po.getKw());
		}
		int allsize = 0,errnum= 0;
		//判断内外网
		String inOrOut = Util.getInOrOut(req);
		po.setPosition(inOrOut);
		List<paramdbPo> list=service.searchDataList(po);
		//故障台数
		List<paramdbPo> list3=service.searchDataError(po);
		errnum = list3 == null ? 0 : list3.get(0).getNum();
		allsize = list == null ? 0 : list.size();//总数
		if(list!=null){
			for (int i = 0; i <allsize; i++) {
				sb.append("<div class=\"infomenu\" id=\""+list.get(i).getUid()+"\">");
				sb.append("<div class=\"infoimg\"></div>");
				sb.append("<div class=\"infotxt\">");
				sb.append("<span class=\"db_ip\">"+toString(list.get(i).getIp())+"</span>");
				sb.append("<span class=\"db_name\">"+toString(list.get(i).getName())+"</span>");
				sb.append("</div>");
				sb.append("</div>");
			}
		}
		return sb.toString()+"&TAB&"+allsize+"&TAB&"+errnum+"";
	}

	/**
	 * 获取数据库详情数据库
	 */
	@ResponseBody
	@RequestMapping(value="/searchData")
	public void searchData(int flag,paramdbPo po,HttpServletRequest req,HttpServletResponse res) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		if(flag==2){
			List<paramdbPo> list=service.searchMgtSystems(po);
			res.getWriter().write(getallDatas(list));
		}else{
			res.getWriter().write(getOrclData(po));
		}
	}
	public String getallDatas(List<paramdbPo> list){
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			paramdbPo po=new paramdbPo();
				po.setUid(list.get(0).getUid());
				String data_name=toString(list.get(0).getName());
				String type_name="",str="";
				type_name="Oracle";
				str=getOrclData(po).split("%TAB%")[1];
				sb.append("<div class='dbinfo'><div class='dbtitle1'><span class='dbtype_txt'>"+type_name+"</span>&nbsp;&nbsp;"+data_name+"</div>");
				sb.append("<table id='normTable' style='BORDER-COLLAPSE: collapse; width: 100%;'>");
				sb.append("<thead><tr><td colspan='2' style='background-color: #f6f8fa;'><b>&nbsp;&nbsp;指标信息</b></td></tr></thead>");
				sb.append("<tbody>"+str+"</tbody>");
				sb.append("</table></div>");
		}
		return sb.toString();
	}
	
	public String getOrclData(paramdbPo po){
		StringBuffer sb=new StringBuffer();
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<paramdbPo> list=service.searchOrcl(po);
		List<paramdbPo> list1=null;
		String cpu_usage="",session_num="",SGA_hit="",user_time="",system_time="",iops="",io_speed="";
		String Memery_usage_percent="",Response_Time="",Idle_Time="",PGA_hit="",DB_cache_match="",share_pool_match="";
		/**********取基本信息和指标信息-start************/
		if(list!=null && list.size()>0){
			cpu_usage=toString(list.get(0).getCpu_usage());
			if(cpu_usage.startsWith(".")){
				cpu_usage = "0"+cpu_usage;
			}
			session_num=toString(list.get(0).getSession_num());
			SGA_hit=toString(list.get(0).getSGA_hit());
			user_time=normNum(toString(list.get(0).getUser_time()),1,0);
			system_time=normNum(toString(list.get(0).getSystem_time()),1,0);
			iops=toString(list.get(0).getIops());
			io_speed=toString(list.get(0).getIo_speed());
			Memery_usage_percent=toString(list.get(0).getMemery_usage_percent());
			Response_Time=normNum(toString(list.get(0).getResponse_Time()),1,0);
			Idle_Time=toString(list.get(0).getIdle_Time());
			PGA_hit=toString(list.get(0).getPGA_hit());
			DB_cache_match=normNum(toString(list.get(0).getDB_cache_match()),100,2);
			share_pool_match=normNum(toString(list.get(0).getShare_pool_match()),1,2);
			sb.append("[{\"name\":\""+list.get(0).getReserver1()+"\"");
			sb.append(",\"instance_state\":\""+list.get(0).getInstance_state()+"\"");
			sb.append(",\"ip\":\""+list.get(0).getIp()+"\"");
			sb.append(",\"cpu_usage\":\""+cpu_usage+"\"");
			sb.append(",\"Memery_usage_percent\":\""+Memery_usage_percent+"\"");
			sb.append("}]");
		}
		sb.append("%TAB%");
		sb.append("<tr><td>CPU使用率：<span id=\"cpu_usage\">"+cpu_usage+"%</span></td>");
		sb.append("<td>连接数： <span id=\"session_num\">"+session_num+"</span></td></tr>");
		sb.append("<tr><td>SGA命中率： <span id=\"SGA_hit\">"+SGA_hit+"%</span></td>");
		sb.append("<td>用户时间： <span id=\"user_time\">"+user_time+"%</span></td></tr>");
		sb.append("<tr><td>系统时间：<span id=\"system_time\">"+system_time+"%</span></td>");
		sb.append("<td>IOPS：<span id=\"iops\">"+iops+"次/每秒</span></td></tr>");
		sb.append("<tr><td>IO总量：<span id=\"io_speed\">"+io_speed+"M</span>");
		sb.append("</td><td>内存使用率：<span id=\"Memery_usage_percent\">"+Memery_usage_percent+"%</span></td></tr>");
		sb.append("<tr><td>响应时间： <span id=\"Response_Time\">"+Response_Time+"s</span>");
		sb.append("</td><td>空闲时间率：<span id=\"Idle_Time\">"+Idle_Time+"%</span></td></tr>");
		sb.append("<tr><td>PGA命中率：<span id=\"PGA_hit\">"+PGA_hit+"%</span>");
		sb.append("</td><td>DBCACHE命中率：<span id=\"DB_cache_match\">"+DB_cache_match+"%</span></td></tr>");
		sb.append("<tr><td>share_pool命中率： <span id=\"share_pool_match\">"+share_pool_match+"%</span></td><td></td></tr>");
		sb.append("%TAB%");
		/**********取基本信息和指标信息-end************/
		
		/**********取表空间信息-start************/
		list1=service.searchMonindexdata(po);
		if(list1!=null && list1.size()>0){
			po.setDBTYPE(MyPropertiesPersist.DBTYPE);
			list1=service.searchOrcl1(po);
			sb.append("<table id=\"spaceTable\" style=\"BORDER-COLLAPSE: collapse; width: 100%;\">");
			sb.append("<thead>");
			sb.append("<tr><td colspan=\"7\" style=\"background-color: #63bfe2;\"><b>&nbsp;&nbsp;表空间信息</b></td></tr>");
			sb.append("<tr style=\"text-align: center;\">");
			sb.append("<td>表名</td><td>类型</td><td>空间总量(M)</td><td>块总量</td><td>已使用空间(M)</td><td>使用率(%)</td><td>空闲率(M)</td>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			if(list1!=null && list1.size()>0){
				for (int i = 0,allsize=list1.size(); i <allsize; i++) {
					try {
						//判断表空间数据是否为空
						if(list1.get(i).getTable_name() != "" && list1.get(i).getTable_name() != null){
							sb.append("<tr>");
							sb.append("<td>"+toString(list1.get(i).getTable_name())+"</td>");
							sb.append("<td>"+toString(list1.get(i).getTable_type())+"</td>");
							sb.append("<td style=\"text-align:right;\">"+toString(list1.get(i).getSum_space())+"</td>");
							sb.append("<td style=\"text-align:right;\">"+toString(list1.get(i).getSum_blocks())+"</td>");
							sb.append("<td style=\"text-align:right;\">"+toString(list1.get(i).getUsed_space())+"</td>");
							sb.append("<td style=\"text-align:right;\">"+toString(list1.get(i).getUsed_rate())+"</td>");
							sb.append("<td style=\"text-align:right;\">"+toString(list1.get(i).getFree_space())+"</td>");
							sb.append("</tr>");
						}
					} catch (Exception e) {
					}
				}
			}
			sb.append("</tbody>");
		}
		/**********取表空间信息-end************/
		
		/**********取服务器信息-start************/
		sb.append("%TAB%");
		List<paramdbPo> list2=service.searchMgtdevice(po);
		if(list2!=null && list2.size()>0){
			sb.append("<div style=\"width: 100%;height: 25px;text-indent:10px;border-bottom: 1px solid #ccc;font-weight:bold;line-height:25px;background-color: #63bfe2;\">服务器信息</div>");
			sb.append("<div class=\"server_body\" style=\"width: 100%;height: auto;padding-top: 10px;\">");
			for (int i = 0,allsize=list2.size(); i <allsize; i++) {
				sb.append("<div class='server_info' onclick=\"jumpServerPar('"+list2.get(i).getUid()+"','"+list2.get(i).getId()+"')\" style=\"width:calc(100% / 8 - 15px);height: 130px;float: left;overflow: hidden;border:solid thin #d1d5de;text-align: center;margin:0 0 10px 10px;font-size:13px;cursor:pointer;border-radius: 5px;\">");
				sb.append("<div style=\"width:90%;height: 70px;margin: 0 auto;margin-top:15px;\"><img src=\"/RtManageCon/imgs/mon/app.png\"></div>");
				sb.append("<span style=\"width:100%;line-height:20px;display:block;\">"+list2.get(i).getIp()+"</span>");
				sb.append("<span style=\"width:100%;line-height: 20px;display:block;\">"+list2.get(i).getName()+"</span>");
				sb.append("</div>");
			}
			sb.append("</div>");
		}
		/**********取服务器信息-end************/
		return sb.toString();
	}
	public String getMysqlData(paramdbPo po){
		StringBuffer sb=new StringBuffer();
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<paramdbPo> list=service.searchMysql(po);
		String Memery_usage_percent="",session_num="",system_time="",TPS="",QPS="";
		if(list!=null && list.size()>0){
			Memery_usage_percent=toString(list.get(0).getCapacity_used());
			session_num=toString(list.get(0).getSession_num());
			system_time=toString(list.get(0).getSystem_time());
			TPS=toString(list.get(0).getTPS());
			QPS=toString(list.get(0).getQPS());
			sb.append("[{\"name\":\""+list.get(0).getReserver1()+"\"");
			String instance_state=toString(list.get(0).getConnect_failed());
			instance_state=instance_state.equals("1")?"OPEN":"CLOSE";
			sb.append(",\"instance_state\":\""+instance_state+"\"");
			sb.append(",\"ip\":\""+list.get(0).getIp()+"\"");
			sb.append(",\"cpu_usage\":\"0\"");
			sb.append(",\"Memery_usage_percent\":\""+Memery_usage_percent+"\"");
			sb.append("}]");
		}
		sb.append("%TAB%");
		sb.append("<tr><td>内存使用率：<span id=\"Memery_usage_percent\">"+Memery_usage_percent+"%</span></td>");
		sb.append("<td>会话数： <span id=\"session_num\">"+session_num+"</span></td></tr>");
		sb.append("<tr><td>系统时间： <span id=\"system_time\">"+system_time+"%</span></td>");
		sb.append("<td>TPS： <span id=\"TPS\">"+TPS+"</span></td></tr>");
		sb.append("<tr><td>QPS：<span id=\"QPS\">"+QPS+"</span></td><td></td></tr>");
		sb.append("%TAB%");
		return sb.toString();
	}
	public String normNum(String num,int c,int ws){
		String norm="";
		try {
			norm=Num45(toString(Double.parseDouble(num)*c),2);
		} catch (Exception e) {
		}
		return norm;
	}
	/**
	 * 获取各指标趋时图
	 * @date：2017年7月3日
	 */
	@ResponseBody
	@RequestMapping(value="/findForTodayChart")
	public void findForTodayChart(paramdbPo po,HttpServletRequest req,HttpServletResponse res) throws Exception{	
		res.setContentType("text/html;charset=UTF-8");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date=df.format(new Date());
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		sb.append("{\"chartTitle\":\"当前连接数\",\"elementId\":\"sessionCount\""+getTrend(po.getUid(),"2010003",date)+"},");
		sb.append("{\"chartTitle\":\"内存利用率\",\"elementId\":\"memoryUsageCount\""+getTrend(po.getUid(),"2010009",date)+"},");
		sb.append("{\"chartTitle\":\"CPU利用率\",\"elementId\":\"cpuUsageCount\""+getTrend(po.getUid(),"2010001",date)+"},");
		sb.append("{\"chartTitle\":\"SGA命中率\",\"elementId\":\"sgaHitCount\""+getTrend(po.getUid(),"2010004",date)+"}");
		sb.append("]");
		res.getWriter().write(sb.toString());
	}
	/**
	 * 趋势图信息查询
	 * @param date：日期
	 * @date：2017年7月3日
	 */
	public String getTrend(String uid,String index_id,String date){
		paramdbPo po=new paramdbPo();
		po.setUid(uid);
		po.setIndex_id(Integer.parseInt(index_id));
		po.setRecord_time(date);
		String str="";
		try {
			List<paramdbPo> list=service.searchTrend(po);
			if(list!=null && list.size()>0){
				for (int j = 0,allsize=list.size(); j <allsize; j++) {
					try {
						str+=",\"avg_value"+list.get(j).getNum()+"\":\""+Num45(toString(list.get(j).getValue()),2)+"\"";
					} catch (Exception e) {
						str+=",\"avg_value"+list.get(j).getNum()+"\":\"0\"";
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return str;
	}
	
}
