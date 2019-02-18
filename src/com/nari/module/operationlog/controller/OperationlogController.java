package com.nari.module.operationlog.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.common.po.CommonDictPo;
import com.nari.module.common.service.CommonService;
import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.po.OperationlogPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping(value="operationlogCon")
public class OperationlogController extends BaseController {
	
	private Logger logger = Logger.getLogger(OperationlogController.class);

	@Resource(name="operationlogService")
	private OperationlogService service;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@Resource(name="commonService")
	private CommonService conservice;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<OperationlogPo> search(OperationlogPo po, HttpServletRequest request) throws Exception{	
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		String time=po.getTime();
		if(!"".equals(time)){
			if(MyPropertiesPersist.DBTYPE.equalsIgnoreCase("ORACLE")){
				po.setSql(" and oprt_time between to_date('"+time+" 00:00:00','yyyy-MM-dd HH24:mi:ss') and to_date('"+time+" 23:59:59','yyyy-MM-dd HH24:mi:ss')");
			}else{
				po.setSql(" and oprt_time between '"+time+" 00:00:00' and '"+time+" 24:00:00'");
			}
		}
		//操作类型查询
		if(po.getOprt_type() != null && !"".equals(po.getOprt_type())){
			String optType = "";
			if(po.getOprt_type().equals("0")){
				optType = "查询";
			}else if(po.getOprt_type().equals("1")){
				optType = "登录";
			}else if(po.getOprt_type().equals("2")){
				optType = "注销";
			}else if(po.getOprt_type().equals("3")){
				optType = "新增";
			}else if(po.getOprt_type().equals("4")){
				optType = "修改";
			}else if(po.getOprt_type().equals("5")){
				optType = "删除";
			}else if(po.getOprt_type().equals("6")){
				optType = "评分";
			}else if(po.getOprt_type().equals("7")){
				optType = "停止";
			}else if(po.getOprt_type().equals("8")){
				optType = "执行";
			}else if(po.getOprt_type().equals("9")){
				optType = "确认";
			}else if(po.getOprt_type().equals("10")){
				optType = "消除";
			}else if(po.getOprt_type().equals("11")){
				optType = "忽略";
			}else if(po.getOprt_type().equals("12")){
				optType = "统计";
			}else if(po.getOprt_type().equals("13")){
				optType = "导出Excel";
			}
			po.setOprt_type(optType);
		}
		//事件等级查询
		if(po.getLevels() != null && !"".equals(po.getLevels())){
			String levels = "";
			if(po.getLevels().equals("0")){
				levels = "logs";
				po.setLevels(levels);
			}
		}
		
		List<OperationlogPo> list = service.search(po);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("操作日志","查询");
		if(result){
			generservice.insert_oprt_logs("日志分析-操作日志","查询",userInfo.getUsername(),"用户查询操作日志数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return list;
	}

	
	/**
	 * 查询系统事件和业务事件数量
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchTypeCounts")
	public List<OperationlogPo> searchTypeCounts(OperationlogPo po){
		List<OperationlogPo> list = null;
		try {
			list = service.searchTypeCounts(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 根据类型查询事件数量
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/searchCounts")
	public List<OperationlogPo> searchCounts(OperationlogPo po, HttpServletRequest request) throws Exception{
		String inOrOut = Util.getInOrOut(request);
		List<OperationlogPo> list = null;
		try {
			list = service.searchCounts(po);
			//判断是否需要添加审计数据
			User userInfo = (User) request.getSession().getAttribute("user");
			boolean result = isAudit("操作日志","统计");
			if(result){
				generservice.insert_oprt_logs("日志分析-操作日志","统计",userInfo.getUsername(),"用户统计操作日志数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
			}
			
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 查询审计策略
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/searchAudit")
	public List<AuditPo> searchAudit(AuditPo po, HttpServletRequest request) throws Exception{
		String inOrOut = Util.getInOrOut(request);
		List<AuditPo> list = null;
		try {
			list = service.searchAudit(po);
			//判断是否需要添加审计数据
			User userInfo = (User) request.getSession().getAttribute("user");
			boolean result = isAudit("审计策略","查询");
			if(result){
				generservice.insert_oprt_logs("日志分析-审计策略","查询",userInfo.getUsername(),"用户查询审计策略数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 
	 * @param po
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/update")
	public int update(AuditPo po, HttpServletRequest request, String oper) throws Exception{
		String inOrOut = Util.getInOrOut(request);
		int res=0;
		//判断是否重复提交
		if(!Util.getSessionJWT(request, po.getJwt())){
			res = -2;
			return res;
		}
		User userInfo = (User) request.getSession().getAttribute("user");
		po.setCreateBy(String.valueOf(userInfo.getId()));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			po.setCreatedate(date2);
			po.setUpdateDate(date2);
		} catch (Exception e) {
		}
		
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		if(oper != null && oper.equals("saveOrUpdate")){
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId() == null ? "" : po.getId()).append(po.getName()).append(po.getErjiname()).append(po.getIsAudit()).append(po.getAuditThose() == null ? "" : po.getAuditThose());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				return -1;
			}
			
			//通过id判断操作为添加还是修改
			if(po.getId() == null){
				res = service.insertAudit(po);
				//判断是否需要添加审计数据
				boolean result = isAudit("审计策略","新增");
				if(result){
					generservice.insert_oprt_log("日志分析-审计策略","新增",userInfo.getUsername(),"新增了一条审计记录",userInfo.getIpaddr(),po.getId(),inOrOut);
				}
			}else{
				po.setUpdateBy(String.valueOf(userInfo.getId()));
				res = service.updateAudit(po);
				generservice.insert_oprt_log("日志分析-审计策略","编辑",userInfo.getUsername(),"修改了一条审计记录",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
		}else if(oper != null && oper.equals("del"))
		{
			po.setUpdateBy(String.valueOf(userInfo.getId()));
			res = service.deleteAudit(po);
			generservice.insert_oprt_log("日志分析-审计策略","删除",userInfo.getUsername(),"删除了一条审计记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}
		//保存/编辑成功移除jwt
		request.getSession().removeAttribute("jwt");
		return res;
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
		List<AuditPo> list = service.searchAuditByErjiname(po);
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
	 * 判断所选择的菜单是否添加了审计数据
	 * @param erjiName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/doAudit")
	public boolean doAudit(String erjiName){
		boolean res = false;
		AuditPo po = new AuditPo();
		po.setErjiname(erjiName);
		List<AuditPo> list = service.searchAuditByErjiname(po);
		if(list != null && list.size() > 0){
			res = true;
		}
		return res;
	}
	
	/**
	 * 日志导出Excel
	 * @param po
	 * @return 
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/exportExcel")
	public String exportExcel(OperationlogPo po, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String inOrOut = Util.getInOrOut(request);
		//查询需要导出的日志
		List<OperationlogPo> operList = service.search(po);
		//导出
		HSSFWorkbook wb = new HSSFWorkbook();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");

		String fileName = "操作日志备份.xls";
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		HSSFSheet sheet = wb.createSheet("操作日志");
		sheet.setDefaultRowHeight((short) (2 * 256));
		sheet.setColumnWidth(0, 30 * 160);
		sheet.setColumnWidth(1, 30 * 160);
		sheet.setColumnWidth(2, 30 * 160);
		sheet.setColumnWidth(3, 30 * 160);
		sheet.setColumnWidth(4, 30 * 160);
		sheet.setColumnWidth(5, 30 * 160);
		sheet.setColumnWidth(6, 30 * 160);
		sheet.setColumnWidth(7, 30 * 160);
		sheet.setColumnWidth(8, 30 * 160);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 16);
		HSSFRow row = sheet.createRow((int) 0);
		sheet.createRow((int) 1);
		sheet.createRow((int) 2);
		sheet.createRow((int) 3);
		sheet.createRow((int) 4);
		sheet.createRow((int) 5);
		sheet.createRow((int) 6);
		sheet.createRow((int) 7);
		sheet.createRow((int) 8);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("名称 ");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("操作类型");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellStyle(style);
		cell.setCellValue("操作时间");
		cell = row.createCell(3);
		cell.setCellStyle(style);
		cell.setCellValue("操作人员");
		cell = row.createCell(4);
		cell.setCellStyle(style);
		cell.setCellValue("操作类容");
		cell = row.createCell(5);
		cell.setCellStyle(style);
		cell.setCellValue("操作人主机IP ");
		cell = row.createCell(6);
		cell.setCellStyle(style);
		cell.setCellValue("是否成功 ");
		cell = row.createCell(7);
		cell.setCellStyle(style);
		cell.setCellValue("事件等级 ");
		cell = row.createCell(8);
		cell.setCellStyle(style);
		cell.setCellValue("事件类型");

		for (int i = 0; i < operList.size(); i++)
		{
			HSSFRow row1 = sheet.createRow((int) i + 1);
			OperationlogPo op = operList.get(i);
			row1.createCell(0).setCellValue(op.getModule());
			row1.createCell(1).setCellValue(op.getOprt_type());
			row1.createCell(2).setCellValue(op.getTime());
			row1.createCell(3).setCellValue(op.getOprt_user());
			row1.createCell(4).setCellValue(op.getOprt_content());
			row1.createCell(5).setCellValue(op.getIp());
			String flag = op.getFlag();
			if(flag != null && !"".equals(flag)){
				if(flag.equals("0")){
					flag = "失败";
				}else if(flag.equals("1")){
					flag = "成功";
				}else{
					flag = "成功";
				}
			}else{
				flag = "成功";
			}
			row1.createCell(6).setCellValue(flag);
			String levels = op.getLevels();
			if(levels != null && !"".equals(levels)){
				if(levels.equals("1")){
					levels="轻微";
				}else if(levels.equals("2")){
					levels="一般";
				}else if(levels.equals("3")){
					levels="严重";
				}else{
					levels="日志";
				}
			}else{
				levels="日志";
			}
			row1.createCell(7).setCellValue(levels);
			String type = op.getType();
			if(type != null && !"".equals(type)){
				if(type.equals("0")){
					type="系统事件";
				}else if(type.equals("0")){
					type="业务事件";
				}else{
					type="业务事件";
				}
			}else{
				type="业务事件";
			}
			row1.createCell(8).setCellValue(type);
		}
		try{
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
		}catch (Exception e){
			logger.error(e);
		}
		
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("操作日志","导出");
		if(result){
			generservice.insert_oprt_logs("日志分析-操作日志","导出",userInfo.getUsername(),"操作日志数据导出Excel",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		
		return null;
	}
	
	/**
	 * 根据类型获取数据字典
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchCon")
	public List<CommonDictPo> search(CommonDictPo po, HttpServletRequest request){
		List<CommonDictPo> list = null;
		User userInfo = (User) request.getSession().getAttribute("user");
		int roleid = userInfo.getRoleid();
		boolean flag = false;
		if(roleid == SysConstant.SJGLY){
			String[] ytype = {"oprtlog_capacity","oprt_type","operlogOff","systemLog_type"};
			for (String type : ytype) {
				if(po.getType().equals(type)){
					flag = true;
					break;
				}
			}
		}
		try {
			if(flag){
				list = conservice.search(po);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
}
