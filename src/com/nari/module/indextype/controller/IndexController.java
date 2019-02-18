package com.nari.module.indextype.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.general.service.GeneranService;
import com.nari.module.indextype.po.IndexPo;
import com.nari.module.indextype.service.IndexService;
import com.nari.po.User;
import com.nari.util.MD5;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.SysConstant;
import com.nari.util.Util;
import com.nari.utils.RsaDecryptTool;

@Controller
@RequestMapping(value="indexCon")
public class IndexController extends BaseController{
	
	private Logger logger = Logger.getLogger(IndexController.class);
	
	@Resource(name="indexService")
	private IndexService service;	
	
	@ResponseBody
	@RequestMapping(value="/search")
	public List<IndexPo> search(IndexPo po){
		List<IndexPo> list = null;
		try {
			list = service.search(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	@ResponseBody
	@RequestMapping(value="/searchindex")
	public List<IndexPo> searchindex(IndexPo po){
		List<IndexPo> list = null;
		try {
			list = service.searchindex(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	@Resource(name = "GeneranService")
	private   GeneranService generservice;
	@ResponseBody
	@RequestMapping(value="/update")
	public Map<String,String> update(@Validated IndexPo po, BindingResult bindingResult, HttpServletRequest request, String oper) throws Exception{	
		Map<String,String> map=new HashMap<String,String>();
		//判断是否重复提交
		if(!Util.getSessionJWT(request, po.getJwt())){
			map.put("errors", SysConstant.CHONTFUTIJIOA);
			return map;
		}
		User userInfo = (User) request.getSession().getAttribute("user");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			po.setCreate_date(date2);
			po.setUpdate_date(date2);
		} catch (Exception e) {
		}
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		String inOrOut = Util.getInOrOut(request);
		int res=0;
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		if(oper != null && oper.equals("add"))
		{
			String mdate = rsaTool.decrypt(po.getmData());//解密得到MD5密文
			//对传输过来的数据进行MD5加密
			StringBuffer sb = new StringBuffer();
			sb.append(po.getIndex_type()).append(po.getIndex_arr());
			String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
			String newMdata = MD5.GetMD5Code(a);
			if(!mdate.equals(newMdata)){
				map.put("integrity", SysConstant.DATE_INTEGRITY);
				return map;
			}
			StringBuffer sb1 = new StringBuffer();
			//特殊字符校验
			if(Util.validationStr(po.getDescription())){
				sb1.append("描述不能含有特殊字符").append("</br>");
			}
			if(Util.validationStr(po.getStd_value())){
				sb1.append("标准值不能含有特殊字符！").append("</br>");
			}
			if(Util.validationStr(po.getRemark())){
				sb1.append("详细阐述不能含有特殊字符！").append("</br>");
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
			}
			
			po.setCreate_by(userInfo.getId());
			res = service.insert(po);
			
		}else if(oper != null && oper.equals("edit"))
		{ 
			po.setUpdate_by(userInfo.getId());
			res = service.update(po);
		}else if(oper != null && oper.equals("del"))
		{
			po.setUpdate_by(userInfo.getId());
			
			List<IndexPo> indexList = service.search(po);
			int index_type = indexList.get(0).getIndex_type();
			
			res = service.delete(po);
			generservice.insert_oprt_log("配置管理-指标分类-指标项","删除",userInfo.getUsername(),"删除了指标分类"+index_type+"下一条指标项记录",userInfo.getIpaddr(),po.getId(),inOrOut);
		}else
		{
			res = 0;
		}
		//保存/编辑成功移除jwt
		request.getSession().removeAttribute("jwt");
		if(res != 0){
			map.put("success", SysConstant.SAVE_SUCCESS);
		}else{
			map.put("success", "指标项保存失败！");
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/updates")
	public Map<String,String> updates(@Validated IndexPo IndexPo, BindingResult bindingResult, HttpServletRequest req,HttpServletResponse res) throws Exception{	
		Map<String,String> map=new HashMap<String,String>();
		res.setContentType("text/html;charset=UTF-8");
		User userInfo = (User) req.getSession().getAttribute("user");
		String index_arr=IndexPo.getIndex_arr();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			Date date2=df.parse(df.format(date));
			IndexPo.setCreate_date(date2);
			IndexPo.setUpdate_date(date2);
		} catch (Exception e) {
		}
		IndexPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
		String inOrOut = Util.getInOrOut(req);
		RsaDecryptTool rsaTool = new RsaDecryptTool();
		String mdate = rsaTool.decrypt(IndexPo.getmData());//解密得到MD5密文
		//对传输过来的数据进行MD5加密
		StringBuffer sb = new StringBuffer();
		sb.append(IndexPo.getIndex_type()).append(IndexPo.getIndex_arr());
		String a = URLEncoder.encode(sb.toString(),"UTF-8");//中文转义
		String newMdata = MD5.GetMD5Code(a);
		if(!mdate.equals(newMdata)){
			map.put("integrity", SysConstant.DATE_INTEGRITY);
			return map;
		}
		
		int ress = 0;
		if(!"".equals(index_arr)){
			String[] arr1=index_arr.split("&TAB&");
			for (int i = 0,allsize=arr1.length; i <allsize; i++) {
				String[] arr2=arr1[i].split("&COL&");
				for (int j = 0,arSize=arr2.length; j <arSize; j++) {
					try {
						String[] arr3=arr2[j].split(":");
						String key=arr3[0];
						String val="";
						try {
							val=arr3[1];
						} catch (Exception e) {
						}
						if(key.equals("id")){
							if(val.equals("")){
								IndexPo.setId(null);
							}else{
								IndexPo.setId(BaseController.toInteger(val));
							}
						}else if(key.equals("index_id")){
							IndexPo.setIndex_id(BaseController.toInteger(val));
						}else if(key.equals("description")){
							IndexPo.setDescription(val);
						}else if(key.equals("remark")){
							IndexPo.setRemark(val);
						}else if(key.equals("warn_rule")){
							if(val.equals("")){
								IndexPo.setWarn_rule(null);
							}else{
								IndexPo.setWarn_rule(BaseController.toInteger(val));
							}
						}else if(key.equals("upper_limit")){
							if(val.equals("")){
								IndexPo.setUpper_limit(null);
							}else{
								IndexPo.setUpper_limit(BaseController.toInteger(val));
							}
						}else if(key.equals("lower_limit")){
							if(val.equals("")){
								IndexPo.setLower_limit(null);
							}else{
								IndexPo.setLower_limit(BaseController.toInteger(val));
							}
						}else if(key.equals("std_value")){
							if(val.equals("")){
								IndexPo.setStd_value(null);;
							}else{
								IndexPo.setStd_value(val);
							}
						}
					} catch (Exception e) {
					}
				}
				
				StringBuffer sb1 = new StringBuffer();
				//特殊字符校验
				if(Util.validationStr(IndexPo.getDescription())){
					sb1.append("描述不能含有特殊字符").append("</br>");
				}
				if(IndexPo.getStd_value() != null && !"".equals(IndexPo.getStd_value())){
					if(Util.validationStr(IndexPo.getStd_value())){
						sb1.append("标准值不能含有特殊字符！").append("</br>");
					}
				}
				if(IndexPo.getRemark() != null && !"".equals(IndexPo.getRemark())){
					if(Util.validationStr(IndexPo.getRemark())){
						sb1.append("详细阐述不能含有特殊字符！").append("</br>");
					}
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
				}
				
				if(IndexPo.getId()==null){//新增
					IndexPo.setCreate_by(userInfo.getId());
					ress = service.insert(IndexPo);
					generservice.insert_oprt_log("配置管理-指标分类-指标项","新增",userInfo.getUsername(),"指标分类"+IndexPo.getIndex_type()+"下新增了一条指标项记录",userInfo.getIpaddr(),IndexPo.getId(),inOrOut);
				}else{//修改
					IndexPo.setUpdate_by(userInfo.getId());
					if("".equals(IndexPo.getRemark())){
						IndexPo.setRemark(null);
					}
					ress = service.update(IndexPo);
					generservice.insert_oprt_log("配置管理-指标分类-指标项","编辑",userInfo.getUsername(),"指标分类"+IndexPo.getIndex_type()+"下修改了一条指标项记录",userInfo.getIpaddr(),IndexPo.getId(),inOrOut);
				}
			}
		}
		res.getWriter().write("");
		if(ress != 0){
			map.put("success", SysConstant.SAVE_SUCCESS);
		}else{
			map.put("success", "指标项保存失败！");
		}
		return map;
	}
}
