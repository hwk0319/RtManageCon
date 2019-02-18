package com.nari.module.operationlog.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.nari.module.common.po.CommonDictPo;
import com.nari.module.common.service.CommonService;
import com.nari.module.operationlog.po.OperationlogPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.module.warnlog.po.WarnlogPo;
import com.nari.module.warnlog.service.WarnlogService;

@Controller
public class OprtlogCapacityWarn {
	
	@Resource(name="commonService")
	private CommonService service;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	@Resource(name="WarnlogService")
	private WarnlogService warnService;
	
	public void oprtlogWarn(CommonDictPo po) throws Exception{
		po.setType("oprtlog_capacity");
		//查询设置容量进行计算大小
		int num = 0;
		List<CommonDictPo> list = service.search(po);
		if(list != null && list.size() > 0){
			String capacity = list.get(0).getValue();
			String regEx="[^0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(capacity);
			num = Integer.parseInt(m.replaceAll("").trim());//数量
			
			String regEx1="[^0-9]";
			Pattern p1 = Pattern.compile(regEx1);
			Matcher m1 = p1.matcher(capacity);
			String dw = m1.replaceAll("").trim();//单位
			//单位换算成K
			if(dw.equals("GB")){
				num = num * 1024 * 1024;
			}else if(dw.equals("MB")){
				num = num * 1024;
			}else if(dw.equals("KB")){
				num = num * 1;
			}
		}
		//查询操作日志数量 ,一条数据按1K来计算
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OperationlogPo oppo = new OperationlogPo();
		List<OperationlogPo> operlist = operService.search(oppo);
		int operNum = 0;
		if(operlist != null && operlist.size() >0){
			operNum = operlist.size();
			//容量达到上限添加告警数据
			WarnlogPo warnPo = new WarnlogPo();
			warnPo.setDevice_name("日志容量");
			warnPo.setWarn_part("操作日志");
			warnPo.setWarn_level("2");
			Date date = new Date();
			Date date2=df.parse(df.format(date));
			warnPo.setWarn_time(date2.toString());
			warnPo.setProcess_status(0);
			warnPo.setIsnoticed(false);
			warnPo.setNewest_warntime(date2.toString());
			warnPo.setOccur_times(1);
			if(operNum >= num){
				warnPo.setWarn_info("日志容量达到设置存储容量上限");
				warnService.insert(warnPo);
			}else if(operNum + 200  > num){
				//即将超过设置容量时添加告警数据
				warnPo.setWarn_info("日志容量即将达到设置存储容量上限");
				warnService.insert(warnPo);
			}
		}
	}

}
