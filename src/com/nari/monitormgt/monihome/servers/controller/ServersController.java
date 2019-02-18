package com.nari.monitormgt.monihome.servers.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nari.controller.BaseController;
import com.nari.module.device.po.DevicesPo;
import com.nari.module.systemmgt.po.SystemmgtPo;
import com.nari.monitormgt.monihome.servers.po.DiscPo;
import com.nari.monitormgt.monihome.servers.po.IbCardPo;
import com.nari.monitormgt.monihome.servers.po.IndexDataPo;
import com.nari.monitormgt.monihome.servers.po.IndexInfoPo;
import com.nari.monitormgt.monihome.servers.po.IpCardPo;
import com.nari.monitormgt.monihome.servers.po.Pager;
import com.nari.monitormgt.monihome.servers.po.RaidInfoPo;
import com.nari.monitormgt.monihome.servers.po.RaiddPo;
import com.nari.monitormgt.monihome.servers.po.SsdPo;
import com.nari.monitormgt.monihome.servers.service.ServersService;
import com.nari.util.MyPropertiesPersist;
import com.nari.util.Util;


@Controller
@RequestMapping(value="monitorhomeCon")
public class ServersController extends BaseController {
	
	private Logger logger = Logger.getLogger(ServersController.class);

	@Resource(name="monitorhomeService")
	private ServersService service;
	
	@ResponseBody
	@RequestMapping(value="/search")
	public Pager search(DevicesPo po,Pager pager,HttpServletRequest request) throws Exception{
		if(pager.getPageNo() != null){
			po.setPageNum(pager.getPageNo());
		}
		//判断内外网
		String inOrOut = Util.getInOrOut(request);
		po.setPosition(inOrOut);
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		pager = service.search(po);
		return pager;
	}
	
	@ResponseBody
	@RequestMapping(value="/searchList")
	public List<DevicesPo> searchList(DevicesPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchList(po);
	}
	
	@ResponseBody
	@RequestMapping(value="/searchDetailById")
	public List<DevicesPo> searchDetailById(DevicesPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchDetailById(po);
	}
	
	/**
	 * 根据指标分类查询指标项
	 * 输入参数：name=(指标分类)
	 * 输出：指标项
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchIndexInfo")
	public Map<String,Object> searchIndexInfo(IndexInfoPo po){
		Map<String,Object> map = null;
		try {
			map = service.searchIndexInfo(po);
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
	
	
	/**
	 * 查询服务器关联的软件属性
	 * @param po
	 * 入参：uid
	 * 出参：system的一系列属性
	 */
	@ResponseBody
	@RequestMapping(value="/searchSoftInfo")
	public List<SystemmgtPo> searchSoftInfo(DevicesPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchSoftInfo(po);
	}
	
	/**
	 * 查询服务器下面的设备
	 * @param po 
	 * 输入参数：uid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchByUid")
	public List<DevicesPo> searchByUid(DevicesPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchByUid(po);
	}
	
	/**
	 * 收集指标项的CPU溫度值
	 * @param po
	 * @return
	 */
	/**
	 * 收集性能项的指標，主要是cpu使用率和内存使用率
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchCpuTemp")
	public Map<String,Object> searchCpuTemp(IndexDataPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchCpuTemp(po);
	}
	/**
	 * 查询仪表盘数据
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchEchartData")
	public Map<String,Object> searchEchartData(IndexDataPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchEchartData(po);
	}
	/**
	 * 收集ssd卡指标项的值
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchSsd")
	public List<SsdPo> searchSsd(SsdPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchSsd(po);
	}
	/**
	 * 收集ib卡指标项的值
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchIbCard")
	public List<IbCardPo> searchIbCard(IbCardPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchIbCard(po);
	}
	/**
	 * 收集ip卡指标项的值
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchIpCard")
	public List<IpCardPo> searchIpCard(IpCardPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchIpCard(po);
	}
	/**
	 * 收集磁盘卡指标项的值
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchDisc")
	public List<DiscPo> searchDisc(DiscPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchDisc(po);
	}
	/**
	 * 收集RAID指标项的值
	 * @param po
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/searchRaid")
	public List<RaidInfoPo> searchRaid(RaiddPo po){
		po.setDBTYPE(MyPropertiesPersist.DBTYPE);
		return service.searchRaid(po);
	}
}
