package com.nari.taskmanager.quartz;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nari.module.common.po.CommonDictPo;
import com.nari.module.common.service.CommonService;
import com.nari.module.operationlog.po.OperationlogPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.module.warnlog.po.WarnlogPo;
import com.nari.module.warnlog.service.WarnlogService;
import com.nari.util.Util;

public class LogWarnJob  implements Job {

	public static  CommonService service;
	public static OperationlogService operService;
	public static WarnlogService warnService;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		CommonDictPo po = new CommonDictPo();
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
			String regEx1="[^A-Z]";
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
		OperationlogPo oppo = new OperationlogPo();
		try {
			oppo.setPosition(Util.getInOrOut());
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<OperationlogPo> operlist = operService.search(oppo);
		int operNum = 0;
		if(operlist != null && operlist.size() >0){
			operNum = operlist.size();
			//容量达到上限添加告警数据
			WarnlogPo warnPo = new WarnlogPo();
			warnPo.setDevice_id("7001");//操作日志菜单
			warnPo.setDevice_name("日志容量");
			warnPo.setWarn_part("操作日志");
			warnPo.setWarn_level("2");
			warnPo.setProcess_status(0);
			warnPo.setIsnoticed(false);
			warnPo.setOccur_times(1);
			try {
				warnPo.setPosition(Util.getInOrOut());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(operNum >= num){
				warnPo.setWarn_info("日志容量达到设置存储容量上限！");
				warnService.insert(warnPo);
			}else if(operNum + 200  > num){
				//即将超过设置容量时添加告警数据
				warnPo.setWarn_info("日志容量即将达到设置存储容量上限！");
				warnService.insert(warnPo);
			}
		}
	}

	
	
}
