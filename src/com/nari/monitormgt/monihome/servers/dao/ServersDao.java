package com.nari.monitormgt.monihome.servers.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.systemmgt.po.SystemmgtPo;
import com.nari.monitormgt.monihome.servers.po.DiscPo;
import com.nari.monitormgt.monihome.servers.po.IbCardPo;
import com.nari.monitormgt.monihome.servers.po.IndexDataPo;
import com.nari.monitormgt.monihome.servers.po.IndexInfoPo;
import com.nari.monitormgt.monihome.servers.po.IpCardPo;
import com.nari.monitormgt.monihome.servers.po.RaidInfoPo;
import com.nari.monitormgt.monihome.servers.po.RaiddPo;
import com.nari.monitormgt.monihome.servers.po.SsdPo;

@Repository(value="monitorhomeDao")
public interface ServersDao{
	public List<DevicesPo> search(@Param("po") DevicesPo po);	
	public List<DevicesPo> searchList(@Param("po") DevicesPo po);
	public List<DevicesPo> searchDetailById(@Param("po") DevicesPo po);
	public int searchTotal(@Param("po") DevicesPo po);
	public List<DevicesPo> searchIndexInfo(@Param("po") IndexInfoPo po);
	public List<DevicesPo> searchByUid(@Param("po") DevicesPo po);
	public List<SystemmgtPo> searchSoftInfo(@Param("po") DevicesPo po);
	public int searchSsdNum(@Param("po") SsdPo po);
	public int searchIbCardNum(@Param("po") IbCardPo po);
	public int searchIpCardNum(@Param("po") IpCardPo po);
	public int searchDiscNum(@Param("po") DiscPo po);
	
	public List<IndexDataPo> searchValue(@Param("po") IndexDataPo po);
	public List<SsdPo> searchSsdValue(@Param("po") SsdPo po);
	public List<IbCardPo> searchIbCardValue(@Param("po") IbCardPo po);
	public List<IpCardPo> searchIpCardValue(@Param("po") IpCardPo po);
	public List<DiscPo> searchDiscValue(@Param("po") DiscPo po);
	public List<String> searchCpuTemp(@Param("po") IndexDataPo po);
	public int searchRaidNum(@Param("po") RaiddPo po);
/*	public List<RaiddPo> searchRaidValue(@Param("po") RaiddPo po);*/
	public List<RaiddPo> searchRaidLevelValue(@Param("po") RaiddPo po);
	public int searchRaidLevelNum(@Param("po") RaiddPo po);
	public List<SsdPo> searchssdIndex(@Param("po") SsdPo po);
	public List<RaidInfoPo> searchRaidValue(@Param("po") RaiddPo po);
	
	public List<IndexDataPo> getIndexByUidIndexType(@Param("po") IndexDataPo po);
}
 