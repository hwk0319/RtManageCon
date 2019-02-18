package com.nari.module.common.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.common.po.CommonDictPo;
import com.nari.module.common.po.ViewUidPo;
import com.nari.module.common.service.CommonService;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.indextype.po.IndexPo;
import com.nari.module.indextype.po.IndextypePo;
import com.nari.module.model.po.ModelPo;
import com.nari.po.User;
import com.nari.util.JWTUtil;
import com.nari.util.SysConstant;
import com.nari.util.Util;


@Controller
@RequestMapping(value="commonCon")
public class CommonController extends BaseController {
	
	private Logger logger = Logger.getLogger(CommonController.class);

	@Resource(name="commonService")
	private CommonService service;
	
	/**
	 * 根据类型获取数据字典
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/search")
	public List<CommonDictPo> search(CommonDictPo po, HttpServletRequest request){
		List<CommonDictPo> list = null;
//		User userInfo = (User) request.getSession().getAttribute("user");
//		int roleid = userInfo.getRoleid();
//		boolean flag = false;
//		if(roleid == SysConstant.YWGLY){
//			String[] ytype = {"device_type","server_factory","switchboard_factory","device_opersys","double_type","group_type","grouptype","db_type","warn_level","warn_type"};
//			for (String type : ytype) {
//				if(po.getType().equals(type)){
//					flag = true;
//					break;
//				}
//			}
//		}
		
		try {
//			if(flag){
				list = service.search(po);
//			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 获取设备型号
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchModel")
	public List<ModelPo> searchModel(ModelPo po){
		List<ModelPo> list = null;
		try {
			list = service.searchModel(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 根据厂商获取设备型号
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchModelByFactory")
	public List<ModelPo> searchModelByFactory(ModelPo po){
		List<ModelPo> list = null;
		try {
			list = service.searchModelByFactory(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 获取设备表里面的服务器
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchServer")
	public List<DevicesPo> searchServer(DevicesPo po){
		List<DevicesPo> list = null;
		try {
			list = service.searchServer(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 获取指标分类
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchIndexType")
	public List<IndextypePo> searchIndexType(IndextypePo po){
		List<IndextypePo> list = null;
		try {
			if(po.getUid() != null && !"".equals(po.getUid())){
				//根据uid第一个数字来判断是硬件还是软件：   1开头的是硬件，2开头的是软件
				String types = po.getUid().substring(0, 1);
				if("1".equals(types)){
					po.setType("hardware");//硬件
				}else if("2".equals(types)){
					po.setType("software");//软件
				}
			}
			list = service.searchIndexType(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	/**
	 * 获取软件系统分类
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchSysType")
	public List<CommonDictPo> searchSysType(CommonDictPo po){
		List<CommonDictPo> list = null;
		try {
			list = service.searchSysType(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 查询服务器和交换机厂商
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/customSearch")
	public List<CommonDictPo> customSearch(CommonDictPo po){
		List<CommonDictPo> list = null;
		try {
			String type=toString(po.getType());
			if(!Util.sql_inj(type)){
				if(type!=null && !"".equals(type)){
				List<String> types = Arrays.asList(type.split(","));
				po.setTypes(types);
				list = service.customSearch(po);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	/**
	 * 获取指标项目
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchIndex")
	public List<IndexPo> searchIndex(IndexPo po){
		List<IndexPo> list = null;
		try {
			list = service.searchIndex(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 查询视图 根据uid
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchViewByUid")
	public List<ViewUidPo> searchViewByUid(ViewUidPo po){
		List<ViewUidPo> list = null;
		try {
			list = service.searchViewByUid(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * 创建JWT
	 * @param request
	 * @return
	 * 2018年5月28日11:27:35
	 */
	@ResponseBody
	@RequestMapping(value="/createJWT")
	public String createJWT(HttpServletRequest request){
		User userInfo = (User) request.getSession().getAttribute("user");
		String id = userInfo.getId()+"";
		String jwt = JWTUtil.createJWT(userInfo.getUsername(), id, userInfo.getRolename());
		jwt = Util.xssEncode(jwt);
		request.getSession().setAttribute("jwt", jwt);
		return jwt;
	}
}
