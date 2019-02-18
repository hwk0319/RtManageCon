package com.nari.module.doublemgt.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.doublemgt.po.CorePo;
import com.nari.module.doublemgt.po.DoublemgtPo;
import com.nari.module.doublemgt.service.DoublemgtService;
import com.nari.module.general.service.GeneranService;
import com.nari.module.operationlog.po.AuditPo;
import com.nari.module.operationlog.service.OperationlogService;
import com.nari.po.User;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping(value="doublemgtCon")
public class DoublemgtController extends BaseController{
	
	Logger logger = Logger.getLogger(DoublemgtController.class);

	@Resource(name="doublemgtService")
	private DoublemgtService service;
	@Resource(name="operationlogService")
	private OperationlogService operService;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<DoublemgtPo> search(DoublemgtPo po, HttpServletRequest request){
		List<DoublemgtPo> list = null;
		try {
			//判断内外网
			String inOrOut = Util.getInOrOut(request);
			po.setPosition(inOrOut);
			list = service.search(po);
			//判断是否需要添加审计数据
			User userInfo = (User) request.getSession().getAttribute("user");
			boolean result = isAudit("双活管理","查询");
			if(result){
				generservice.insert_oprt_logs("配置管理-双活管理","查询",userInfo.getUsername(),"用户查询双活管理数据",userInfo.getIpaddr(),0, SysConstant.OPRTLOG_SECCUSS, SysConstant.BUSSIENCE_EVENT, SysConstant.LOGS_EVENTS,inOrOut);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/search_group_core")
	public List<DoublemgtPo> search_group_core(DoublemgtPo po){
		List<DoublemgtPo> list = null;
		try{
			list = service.search_group_core(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated DoublemgtPo po, BindingResult bindingResult, HttpServletRequest request, String oper) throws Exception{
		Map<String,String> map=new HashMap<String,String>();
		//判断是否重复提交
		if(!Util.getSessionJWT(request, po.getJwt())){
			map.put("errors", SysConstant.CHONTFUTIJIOA);
			return map;
		}
		User userInfo = (User) request.getSession().getAttribute("user");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Date date2= new Date();
		try {
			date2=df.parse(df.format(date));
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
			/*RsaDecryptTool rsaTool = new RsaDecryptTool();
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getId() == null ? "" : po.getId()).append(po.getName()).append(po.getDoubtype()).append(po.getDescription() == null ? "" : po.getDescription());
			sb.append(po.getCore_l()).append(po.getCore_r()).append(po.getGroup_ids_L()).append(po.getGroup_ids_R());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getName())){
				sb1.append("名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getDescription())){
				sb1.append("描述不能含有特殊字符").append("</br>");
			}
			if(Util.validationStr(po.getCore_l())){
				sb1.append("第一个中心名称不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getCore_r())){
				sb1.append("第二个中心名称不能含有特殊字符！").append("</br>");
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
			
			String group_ids_L = po.getGroup_ids_L();//A中心组ID
			String group_ids_R = po.getGroup_ids_R();//B中心组ID
			String core_r = po.getCore_r();//A中心名称
			String core_l = po.getCore_l();//B中心名称
			
			if(po.getId()==null){
				po.setCreate_by(userInfo.getId());
				res=service.insert(po);//新增双活
				int double_id=po.getId();
				/**
				 * 增加A中心
				 */
				if(core_l!=null){
					CorePo corepoA=new CorePo();
					corepoA.setCreate_date(date2);
					corepoA.setUpdate_date(date2);
					corepoA.setDBTYPE(MyPropertiesPersist.DBTYPE);
					corepoA.setDouble_id(double_id);
					corepoA.setCreate_by(userInfo.getId());
					corepoA.setCore(core_l);
					corepoA.setCore_tagging("L");
					service.insertcore(corepoA);
					//中心-组关联
					if(!"".equals(group_ids_L) && group_ids_L!=null){
						 group_ids_L=group_ids_L.substring(0,group_ids_L.length()-1);
						 String [] id_arr_L=group_ids_L.split(",");
						 service.deletegroup(corepoA);
						 for (int i = 0,allsize=id_arr_L.length; i <allsize; i++) {
								int group_id=Integer.parseInt(id_arr_L[i]);//组id
								corepoA.setGroup_id(group_id);
								res=service.insertcoreGroup(corepoA);
							}
					}
				}
	 			/**
				 * 增加B中心
				 */
				if(core_r!=null){
					CorePo corepoB=new CorePo();
					corepoB.setCreate_date(date2);
					corepoB.setUpdate_date(date2);
					corepoB.setDBTYPE(MyPropertiesPersist.DBTYPE);
					corepoB.setDouble_id(double_id);
					corepoB.setCreate_by(userInfo.getId());
					corepoB.setCore(core_r);
					corepoB.setCore_tagging("R");
					service.insertcore(corepoB);
					//中心-组关联
					if(!"".equals(group_ids_R) && group_ids_R!=null){
						 group_ids_R=group_ids_R.substring(0,group_ids_R.length()-1);
						 String [] id_arr_R=group_ids_R.split(",");
						 service.deletegroup(corepoB);
						 for (int i = 0,allsize=id_arr_R.length; i <allsize; i++) {
								int group_id=Integer.parseInt(id_arr_R[i]);//组id
								corepoB.setGroup_id(group_id);
								res=service.insertcoreGroup(corepoB);
							}
					}
				}
				//判断是否需要添加审计数据
				boolean result = isAudit("双活管理","新增");
				if(result){
					generservice.insert_oprt_log("配置管理-双活管理","新增",userInfo.getUsername(),"新增了一条双活记录",userInfo.getIpaddr(),po.getId(),inOrOut);
				}
			}else if(po.getId() !=null){
				int id_l = po.getId_L();//A中心ID
				int id_r = po.getId_R();//B中心ID
				po.setUpdate_by(userInfo.getId());
				res=service.update(po);
				/**
				 * 修改A中心
				 */
				if(core_l!=null){
					CorePo corepoA=new CorePo();
					corepoA.setCreate_date(date2);
					corepoA.setUpdate_date(date2);
					corepoA.setCreate_by(userInfo.getId());
					corepoA.setCore(core_l);
					corepoA.setId(id_l);
					service.updatecore(corepoA);
					//中心-组关联
					if(!"".equals(group_ids_L) && group_ids_L!=null){
						 group_ids_L=group_ids_L.substring(0,group_ids_L.length()-1);
						 String [] id_arr_L=group_ids_L.split(",");
						 service.deletegroup(corepoA);
						 for (int i = 0,allsize=id_arr_L.length; i <allsize; i++) {
								int group_id=Integer.parseInt(id_arr_L[i]);//组id
								corepoA.setGroup_id(group_id);
								
								res=service.insertcoreGroup(corepoA);
							}
					}
				}
				/**
				 * 修改B中心
				 */
				if(core_r!=null){
					CorePo corepoB=new CorePo();
					corepoB.setCreate_date(date2);
					corepoB.setUpdate_date(date2);
					corepoB.setCreate_by(userInfo.getId());
					corepoB.setCore(core_r);
					corepoB.setId(id_r);
					service.updatecore(corepoB);
					//中心-组关联
					if(!"".equals(group_ids_R) && group_ids_R!=null){
						 group_ids_R=group_ids_R.substring(0,group_ids_R.length()-1);
						 String [] id_arr_R=group_ids_R.split(",");
						 service.deletegroup(corepoB);
						 for (int i = 0,allsize=id_arr_R.length; i <allsize; i++) {
								int group_id=Integer.parseInt(id_arr_R[i]);//组id
								corepoB.setGroup_id(group_id);
								
								res=service.insertcoreGroup(corepoB);
							}
					}
				}
				generservice.insert_oprt_log("配置管理-双活管理","编辑",userInfo.getUsername(),"修改了一条双活记录",userInfo.getIpaddr(),po.getId(),inOrOut);
			}
			//保存/编辑成功移除jwt
			request.getSession().removeAttribute("jwt");
		}else if(oper != null && oper.equals("del")){
			po.setUpdate_by(userInfo.getId());
			res = service.delete(po);
			res = service.deletecore(po);
			res =service.deletecoregroup(po);
			generservice.insert_oprt_log("配置管理-双活管理","删除",userInfo.getUsername(),"删除了一条双活记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else{
			res = 0;
		}
		
		if(res != 0){
			map.put("success", SysConstant.SAVE_SUCCESS);
		}else{
			map.put("success", SysConstant.SAVE_FILE);
		}
		return map;
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
