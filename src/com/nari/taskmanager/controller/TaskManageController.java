package com.nari.taskmanager.controller;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.chart.PieChart.Data;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import oracle.sql.TIMESTAMP;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.nari.common.po.RowBounds;
import com.nari.module.general.service.GeneranService;
import com.nari.module.indextype.po.IndextypePo;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.taskmanager.config.TaskConfig;
import com.nari.taskmanager.po.TaskLog;
import com.nari.taskmanager.po.TaskOperation;
import com.nari.taskmanager.po.TaskParam;
import com.nari.taskmanager.po.TaskStep;
import com.nari.taskmanager.service.TaskManageService;
import com.nari.util.MD5;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping("/taskmanage")
public class TaskManageController {
	private static Logger logger = Logger.getLogger(TaskManageController.class); 

	@Autowired
	TaskManageService taskManageService;
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	@ResponseBody
	@RequestMapping("/taskList")
	public JSONObject getTaskList(TaskOperation operation, HttpServletRequest request) throws Exception {
//		operation.setOrderBy("task_opera_id");
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		operation.setPosition(inOrOut);
		List<TaskOperation> tasks = taskManageService.getTask(operation);
		JSONObject obj = initPageSet(operation,tasks);
		//判断是否需要添加审计数据
		User userInfo = (User) request.getSession().getAttribute("user");
		boolean result = isAudit("任务列表","查询");
		if(result){
			generservice.insert_oprt_logs("任务管理-任务列表","查询",userInfo.getUsername(),"用户查询任务列表数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
		}
		return obj;
	}
	
	@ResponseBody
	@RequestMapping("/taskById")
	public JSONObject getTaskListById(TaskOperation operation) {
		JSONObject obj = new JSONObject();
		List<TaskOperation> tasks = taskManageService.getTask(operation);
		if(tasks != null && tasks.size()==1)
		{
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.SUCCESS);
			obj.put(TaskConfig.TASK_JSON_VALUE,tasks);
		}
		else
		{
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE,"查找任务时出错!");
		}
		return obj;
	}
	
	/**
	 * 获得所有任务模板
	 * 
	 * @param taskID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/taskTemplateList")
	public JSONObject getTaskTemplate(TaskOperation operation) {
		//operation.setTotalRecords(taskManageService.getTaskTplTotalCount(operation));
		//operation.setNeedPage(true);
		List<TaskOperation> taskTemplates = taskManageService.getTaskTemplate(operation);
		JSONObject obj = initPageSet(operation,taskTemplates);
		return obj;
	}

	/**
	 * 获取一个模板任务的步骤。
	 * 
	 * @param taskId
	 * @return List
	 */
	@ResponseBody
	@RequestMapping("/getTaskStepTemplate")
	public JSONObject getTaskStepTemplate(TaskStep step) {
		List<TaskStep> templateSteps = taskManageService.getTaskStepTemplat(step);
		JSONObject obj = initPageSet(step,templateSteps);
		return obj;
	}

	/**
	 * 新增一个Operation模板
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/taskOperationTemplate")
	public JSONObject taskOperationTemplate(TaskOperation operation,HttpServletRequest request) throws Exception {
		String inOrOut = Util.getInOrOut(request);
		User userInfo = (User) request.getSession().getAttribute("user");
		String oper = operation.getOper();
		JSONObject res;
		if (oper.equals("add")) {
			 res= taskManageService.addTaskTemplate(operation);
			generservice.insert_oprt_log("任务管理-模板列表","新增",userInfo.getUsername(),"新增了一条任务模板",userInfo.getIpaddr(),operation.getId(),inOrOut);
			return res;
		} else if ("edit".equals(oper)) {
			res= taskManageService.updateTaskOperationTemplate(operation);
			generservice.insert_oprt_log("任务管理-模板列表","编辑",userInfo.getUsername(),"修改了一条任务模板",userInfo.getIpaddr(),operation.getId(),inOrOut);
			return res;
		} else if ("del".equals(oper)) {
			res = taskManageService.removeTemplateTask(operation);
			generservice.insert_oprt_log("任务管理-模板列表","删除",userInfo.getUsername(),"删除了一条任务模板",userInfo.getIpaddr(),operation.getId(),inOrOut);
			return res;
		} else {
			return taskManageService.returnError();
		}
	}

	/**
	 * 新增一个Step模板
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/taskStepTemplate")
	public JSONObject taskStepTemplate(TaskStep stepTemplate) {
		String oper = stepTemplate.getOper();
		if (oper.equals("add")) {
			return taskManageService.addTaskStepTemplate(stepTemplate);
		} else if ("edit".equals(oper)) //
		{
			return taskManageService.updateTaskStepTemplate(stepTemplate);
		} else if ("del".equals(oper)) 
		{
			return taskManageService.removeTaskStepTemplate(stepTemplate);
		} else if ("editShell".equals(oper)) // 修改脚本内容
		{
			return taskManageService.updateTaskStepShellTemplateTxt(stepTemplate);
		} else if ("checkStepOrder".equals(oper)) // 修改脚本内容
		{
			return taskManageService.checkStepTplOrder(stepTemplate);//检测步骤号是否重复。
		} else {
			return taskManageService.returnError();
		}
	}

	/**
	 * 获取一个任务模板下的所有参数模板
	 * 
	 * @param taskId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTaskParamTemplate")
	public JSONObject getTaskParamTemplate(TaskParam param) {
		List<TaskParam> templateParams = taskManageService.getTaskParamTemplate(param);
		JSONObject obj = initPageSet(param,templateParams);
		return obj;
	}

	@ResponseBody
	@RequestMapping("/taskParamTemplate")
	public JSONObject taskParamTemplate(TaskParam paramTemplate) {
		String oper = paramTemplate.getOper();
		if (oper.equals("add")) {
			return taskManageService.addTaskParamTemplate(paramTemplate);
		} else if ("edit".equals(oper)) {
			return taskManageService.updateTaskParamTemplate(paramTemplate);
		} else if ("del".equals(oper)) {
			return taskManageService.removeTaskParamTemplate(paramTemplate);
		} else if ("checkParam".equals(oper)) {
			return taskManageService.checkParamTpl(paramTemplate);
		} else {
			return taskManageService.returnError();
		}
	}

	/**
	 * 新增一个Param模板
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addNewTaskParamTemplate")
	public JSONObject addNewTaskParamTemplate(TaskParam taskParamTemplate) {
		return taskManageService.addTaskParamTemplate(taskParamTemplate);
	}

	/**
	 * 获得所有的任务分类
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/taskModulList")
	public List<String> getModulList(String type) {
		Set<String> modulSet = new HashSet<String>();
		List<String> modul = new ArrayList<String>();
		if(TaskConfig.TASK_REQUEST_TYPE.equals(type))
		{
			List<TaskOperation> operations = taskManageService.getTask(new TaskOperation());
			for(TaskOperation opera : operations)
			{
				modulSet.add(opera.getModul());
			}
		}
		else if(TaskConfig.TPL_REQUEST_TYPE.equals(type))
		{
			List<TaskOperation> operations = taskManageService.getTaskTemplate(new TaskOperation());
			for(TaskOperation opera : operations)
			{
				modulSet.add(opera.getModul());
			}
		}
		else
		{
			
		}
		modul.addAll(modulSet);
		return modul;
	}

	/**
	 * 获得modu分类下的，所有任务模板。
	 * 
	 * @param modul
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/taskOperationList")
	public JSONObject getTaskOperationTplByModul(TaskOperation operation) {
		List<TaskOperation> operations = taskManageService.getTaskTemplate(operation);
		JSONObject obj = initPageSet(operation,operations);
		return obj;
	}

	/**
	 * 任务控制器， 负责任务Operation的添加修改等
	 * 
	 * @param paramTemplate
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/taskOperation")
	public JSONObject taskOperation(TaskOperation operation,HttpServletRequest request) throws Exception {
		String inOrOut = Util.getInOrOut(request);
		User userInfo = (User) request.getSession().getAttribute("user");
		String oper = operation.getOper();
		JSONObject res = null ;
		int task_type=operation.getType();
		String type="";
		if(task_type==1){
			type="普通任务";
		}else if(task_type==2){
			type="定时任务";
		}
		if ("add".equals(oper)) {
			res= taskManageService.addTask(operation);
			//判断是否需要添加审计数据
			boolean result = isAudit("任务列表","新增");
			if(result){
				generservice.insert_oprt_log("任务管理-任务列表","新增",userInfo.getUsername(),"新增了一条"+type,userInfo.getIpaddr(),operation.getId(),inOrOut);
			}
			return res;
		} else if ("edit".equals(oper)) {
			try {
				res= taskManageService.updateTaskOperation(operation);
			} catch (Exception e) {
				logger.error(e);
			}
			generservice.insert_oprt_log("任务管理-任务列表","编辑",userInfo.getUsername(),"修改了一条任务记录",userInfo.getIpaddr(),operation.getId(),inOrOut);
			return res;
		} else if ("del".equals(oper)) {
			res= taskManageService.removeTask(operation);
			generservice.insert_oprt_log("任务管理-任务列表","删除",userInfo.getUsername(),"删除了一条任务记录"+type,userInfo.getIpaddr(),operation.getId(),inOrOut);
			return res;
		} else if ("editCron".equals(oper)) {
			try {
				return taskManageService.updateTaskOperation(operation);
			} catch (Exception e) {
				logger.error(e);
			}
		} else {
			return taskManageService.returnError();
		}
		return res;
		
	}

	@ResponseBody
	@RequestMapping("/getTaskStepById")
	public JSONObject getTaskStepById(TaskStep step) {
		List<TaskStep> steps = taskManageService.getTaskStep(step);
		JSONObject obj = initPageSet(step,steps);
		return obj;
	}

	@ResponseBody
	@RequestMapping(value="/taskStep")
	public JSONObject taskStep(@Validated TaskStep step, BindingResult bindingResult) throws Exception {
		JSONObject obj = new JSONObject();
		String oper = step.getOper();
		if("add".equals(oper)){
			RsaDecryptTool rsaTool = new RsaDecryptTool();
			String mdate = rsaTool.decrypt(step.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(step.getTaskId()).append(step.getName()).append(step.getStepOrder()).append(step.getTimeOut()).append(step.getPoDesc());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
				obj.put(TaskConfig.TASK_JSON_VALUE, SysConstant.DATE_INTEGRITY);
				return obj;
			}
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(step.getName())){
				sb1.append("步骤名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(step.getPoDesc())){
				sb1.append("步骤说明不能含有特殊字符！").append("</br>");
			}
			//数据校验
			if (bindingResult.hasErrors()) {
				List<ObjectError> ErrorList = bindingResult.getAllErrors();
				for(ObjectError objectError:ErrorList){
					sb1.append(objectError.getDefaultMessage()).append("</br>");
				}    
			}
			if(sb1.toString() != null && !"".equals(sb1.toString())){
				obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
				obj.put(TaskConfig.TASK_JSON_VALUE, sb1.toString());
				return obj;
			}
		}
		
		if ("add".equals(oper)) {
			return taskManageService.addTaskStep(step);
		} else if ("edit".equals(oper)) {
			return taskManageService.updateTaskStep(step);
		} else if ("del".equals(oper)) {
			return taskManageService.removeTaskStep(step);
		} else if ("editShell".equals(oper)) {
			taskManageService.updateTaskStep(step);
			return taskManageService.saveStepShellFile(step);
		} else if ("checkStepOrder".equals(oper)) {
			return taskManageService.checkStepOrder(step);
		} else {
			return taskManageService.returnError();
		}
	}

	@ResponseBody
	@RequestMapping("/getTaskParamp")
	public JSONObject getTaskParamById(TaskParam param) {
		List<TaskParam> params = taskManageService.getTaskParam(param);
		JSONObject obj = initPageSet(param,params);
		return obj;
	}

	@ResponseBody
	@RequestMapping("/taskParam")
	public JSONObject taskParam(@Validated TaskParam param, BindingResult bindingResult) throws Exception {
		JSONObject obj = new JSONObject();
		String oper = param.getOper();
		if("add".equals(oper)){
			RsaDecryptTool rsaTool = new RsaDecryptTool();
			String mdate = rsaTool.decrypt(param.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(param.getTaskId()).append(param.getName()).append(param.getValue()).append(param.getPoDesc());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
				obj.put(TaskConfig.TASK_JSON_VALUE, SysConstant.DATE_INTEGRITY);
				return obj;
			}
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(param.getName())){
				sb1.append("参数项不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(param.getValue())){
				sb1.append("默认值不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(param.getPoDesc())){
				sb1.append("参数描述不能含有特殊字符！").append("</br>");
			}
			//数据校验
			if (bindingResult.hasErrors()) {
				List<ObjectError> ErrorList = bindingResult.getAllErrors();
				for(ObjectError objectError:ErrorList){
					sb1.append(objectError.getDefaultMessage()).append("</br>");
				}    
			}
			if(sb1.toString() != null && !"".equals(sb1.toString())){
				obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
				obj.put(TaskConfig.TASK_JSON_VALUE, sb1.toString());
				return obj;
			}
		}
		
		if ("add".equals(oper)) {
			obj = taskManageService.addTaskParam(param);
		} else if ("edit".equals(oper)) {
			obj= taskManageService.updateTaskParam(param);
		} else if ("del".equals(oper)) {
			obj= taskManageService.removeTaskParam(param);
		} else {
			obj= taskManageService.returnError();
		}
		if(obj.getString(TaskConfig.TASK_JSON_STATUS).equals(TaskConfig.ERROR))
		{
			return obj;
		}
		return taskManageService.updateParamFile(param);
	}

	/**
	 * 复制一个模板任务到value表， 包含step与param。 同时设置一些初始状态。设置任务状态为creating
	 * 
	 * @param id
	 * @return 新建任务的任务ID。
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/creatTaskFromTemplate")
	public JSONObject creatTaskFromTemplate(@Validated TaskOperation task, BindingResult bindingResult,HttpServletRequest request) throws Exception {
		JSONObject obj = new JSONObject();
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		String mdate = rsaTool.decrypt(task.getmData());//解密得到MD5密文
		//对传输过来的数据进行MD5加密
		StringBuffer sb = new StringBuffer();
		sb.append(task.getTplSel()).append(task.getId()).append(task.getName()).append(task.getType()).append(task.getPoDesc());
		String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
		String newMdata = MD5.GetMD5Code(a);
		if(!mdate.equals(newMdata)){
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, SysConstant.DATE_INTEGRITY);
			return obj;
		}
		
		//判断数据完整性
		StringBuffer sb1 = new StringBuffer();
		//特殊字符校验
		if(Util.validationStr(task.getName())){
			sb1.append("任务名称不能含有特殊字符！").append("</br>");
		}
		if(Util.validationStr(task.getPoDesc())){
			sb1.append("任务描述不能含有特殊字符！").append("</br>");
		}
		//数据校验
		if (bindingResult.hasErrors()) {
			List<ObjectError> ErrorList = bindingResult.getAllErrors();
			for(ObjectError objectError:ErrorList){
				sb1.append(objectError.getDefaultMessage()).append("</br>");
			}    
		}
		if(sb1.toString() != null && !"".equals(sb1.toString())){
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, sb1.toString());
			return obj;
		}
		
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		task.setPosition(inOrOut);
		
		return  taskManageService.creatTaskFromTemplate(task);
	}
	
	@ResponseBody
	@RequestMapping("/cloneTaskByTaskId")
	public JSONObject cloneTaskByTaskId(int taskId,TaskOperation taskOperation,HttpServletRequest request) throws Exception {
		logger.info("clone Task By TaskId :"+taskId);
		String inOrOut = Util.getInOrOut(request);
		JSONObject obj = new JSONObject();
		User userInfo = (User) request.getSession().getAttribute("user");
		String type="";
		try {
			int  operation = taskManageService.cloneTask(taskId,TaskConfig.TASK_STRATEGY_ALL_PARALLEL).getId();
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.SUCCESS);
			obj.put(TaskConfig.TASK_JSON_VALUE, operation);
			taskOperation.setId(operation);
			int task_type=taskManageService.getTask(taskOperation).get(0).getType();
			if(task_type==1){
				type="普通任务";
			}else if(task_type==2){
				type="定时任务";
			}
			generservice.insert_oprt_log("任务管理-任务列表","克隆",userInfo.getUsername(),"克隆了一条"+type,userInfo.getIpaddr(),operation,inOrOut);
		} catch (Exception e) {
			logger.error("clone Task By TaskId :"+taskId+" error!");
			logger.error(e);
			obj.put(TaskConfig.TASK_JSON_STATUS, TaskConfig.ERROR);
			obj.put(TaskConfig.TASK_JSON_VALUE, e.getMessage());
			return obj;
		}
		return obj;
		
	}

	/**
	 * 用户在页面上创建任务后点击确定，保存此任务。 修改任务的状态由0-creating 改为1-init。 保存参数到ini文件。
	 * 如果为定时任务加入到quartz中。
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/saveTaskFromCreating")
	public JSONObject saveTaskFromCreating(TaskOperation operation, HttpServletRequest request) throws Exception {
		String inOrOut = Util.getInOrOut(request);
		User userInfo = (User) request.getSession().getAttribute("user");
		JSONObject obj = taskManageService.saveTaskFromCreating(operation);
		System.out.println(obj);
		//判断是否保存成功，插入日志
		String sta = (String) obj.get("status");
		if(sta.equals("success")){
			//判断是否需要添加审计数据
			boolean result = isAudit("任务列表","新增");
			if(result){
				generservice.insert_oprt_logs("任务管理-任务列表","新增",userInfo.getUsername(),"新增了一条普通任务",userInfo.getIpaddr(),operation.getId(),SysConstant.OPRTLOG_SECCUSS,"1","0",inOrOut);
			}
		}else{
			//判断是否需要添加审计数据
			boolean result = isAudit("任务列表","新增");
			if(result){
				generservice.insert_oprt_logs("任务管理-任务列表","新增",userInfo.getUsername(),"新增了一条普通任务",userInfo.getIpaddr(),operation.getId(),SysConstant.OPRTLOG_FAIL,"1","0",inOrOut);
			}
		}
		return obj;
	}


/*	@ResponseBody
	@RequestMapping("/removeTaskById")
	public String removeTaskById(int id) {
		String result = taskManageService.removeTaskById(id);
		return result;
	}*/

	@ResponseBody
	@RequestMapping("/startTaskById")
	public JSONObject startTaskById(TaskOperation operation,HttpServletRequest request) throws Exception {
		String inOrOut = Util.getInOrOut(request);
		User userInfo = (User) request.getSession().getAttribute("user");
		JSONObject res;
		int task_type=taskManageService.getTask(operation).get(0).getType();
		String type="";
		if(task_type==1){
			type="普通任务";
		}else if(task_type==2){
			type="定时任务";
		}
		res= taskManageService.startTask(operation,false);//触发
		//判断是否需要添加审计数据
		boolean result = isAudit("任务列表","执行");
		if(result){
			generservice.insert_oprt_log("任务管理-任务列表","执行",userInfo.getUsername(),"执行了一条"+type,userInfo.getIpaddr(),operation.getId(),inOrOut);
		}
		return res;
	}
	
	@ResponseBody
	@RequestMapping("/stopTaskById")
	public JSONObject pauseTaskById(TaskOperation operation,HttpServletRequest request) throws Exception {
		String inOrOut = Util.getInOrOut(request);
		User userInfo = (User) request.getSession().getAttribute("user");
		JSONObject res;
		int task_type=taskManageService.getTask(operation).get(0).getType();
		String type="";
		if(task_type==1){
			type="普通任务";
		}else if(task_type==2){
			type="定时任务";
		}
		res= taskManageService.stopTask(operation);//停止
		//判断是否需要添加审计数据
		boolean result = isAudit("任务列表","停止");
		if(result){
			generservice.insert_oprt_log("任务管理-任务列表","停止",userInfo.getUsername(),"停止了一条"+type,userInfo.getIpaddr(),operation.getId(),inOrOut);
		}
		return res;
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/resumeTaskById")  
	public JSONObject resumeScheduleJob(TaskOperation operation,HttpServletRequest request) throws Exception
	{
		String inOrOut = Util.getInOrOut(request);
		User userInfo = (User) request.getSession().getAttribute("user");
		JSONObject res;
		res= taskManageService.resumeScheduleJob(operation);//恢复
		generservice.insert_oprt_log("任务管理-任务列表","恢复",userInfo.getUsername(),"恢复了一条定时任务",userInfo.getIpaddr(),operation.getId(),inOrOut);
		return res;
	}

	
	@RequestMapping(value = "/fileUpload")
	public @ResponseBody JSONObject uploadFile(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) throws Exception {
		String upLoadType = (String) request.getParameter("upLoadType");
		String fileType = (String) request.getParameter("fileType");
		
		int id = 0;
		try {
			id = Integer.valueOf(request.getParameter("id"));
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(),e);
			return taskManageService.returnError();
		}
		logger.info("upload file  upLoadType:"+upLoadType+" fileType:"+fileType + " id:"+id);
		return taskManageService.uploadFile(id, upLoadType,fileType, file);
	}
	
	
	
	@ResponseBody
	@RequestMapping("/getTaskLogByTaskId")
	public JSONObject getTaskLogByTaskId(TaskLog log) {
		List<TaskLog> tasks = taskManageService.getTaskLogByTaskId(log.getTaskId());
		JSONObject obi = new JSONObject();
		obi.put("status", TaskConfig.SUCCESS);
		obi.put("value", tasks);
		return obi;
	}
	
	@ResponseBody
	@RequestMapping("/cloneTaskOperationTemplate")
	public JSONObject cloneTaskOperationTemplate(TaskOperation operation) {
		 JSONObject obj =taskManageService.cloneTaskOperationTemplate(operation);
		return obj;
	}
	
	private JSONObject initPageSet(RowBounds operation,List<?> list)
	{
		int totalRecord = operation.getTotalRecords();
		int pageSize=operation.getPageSize();
		int currentPage = operation.getCurrentPage();//当前页数
		int totalPageNum = (totalRecord  +  pageSize  - 1) / pageSize;//总页数
		JSONObject obj = new JSONObject();
		obj.put("total", totalPageNum);
		obj.put("page", currentPage);
		obj.put("records",totalRecord);
		obj.put("rows",list);
		return obj;
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
	 * 查询任务数据并返回json数据
	 * @param po
	 * @param request
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value="/search1")
	public JSONObject search1(TaskOperation po, HttpServletRequest request){
		if(po.getPage() != 0){
			po.setPageNum(po.getPage());
		}
		if(po.getLimit() != 0){
			po.setPageSize(po.getLimit());
		}
		//查询总记录数
		List<TaskOperation> list1 = taskManageService.searchTotal(po);
		//分页查询
		List<TaskOperation> list = taskManageService.search1(po);

		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());//时间格式转换
//		config.setExcludes(new String[] {"startTime","endTime","nextTime"});//过滤掉对象 不转成JSONArray
		
		JSONArray json = JSONArray.fromObject(list,config); 
		
		JSONObject jsonObject = new JSONObject();  //创建Json对象
		jsonObject.put("code", 0);         //设置Json对象的属性
		jsonObject.put("msg", "");
		jsonObject.put("count", list1.size());//总记录数
		jsonObject.put("data", json);//json数据
		return jsonObject;
	}*/
}
