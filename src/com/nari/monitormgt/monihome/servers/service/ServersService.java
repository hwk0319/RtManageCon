package com.nari.monitormgt.monihome.servers.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.nari.module.device.po.DevicesPo;
import com.nari.module.systemmgt.po.SystemmgtPo;
import com.nari.monitormgt.monihome.servers.dao.ServersDao;
import com.nari.monitormgt.monihome.servers.po.DiscPo;
import com.nari.monitormgt.monihome.servers.po.IbCardPo;
import com.nari.monitormgt.monihome.servers.po.IndexDataPo;
import com.nari.monitormgt.monihome.servers.po.IndexInfoPo;
import com.nari.monitormgt.monihome.servers.po.IpCardPo;
import com.nari.monitormgt.monihome.servers.po.Pager;
import com.nari.monitormgt.monihome.servers.po.RaidInfoPo;
import com.nari.monitormgt.monihome.servers.po.RaiddPo;
import com.nari.monitormgt.monihome.servers.po.SsdPo;
import com.nari.util.MyPropertiesPersist;

@Service(value="monitorhomeService")
public class ServersService {
	@Resource(name = "monitorhomeDao")
	private  ServersDao dao;
	
	public Pager search(@Param("po") DevicesPo po){
	Pager pager = new Pager();
	//查询总记录数
	int total = dao.searchTotal(po);
	int pageCount = 0;
	//计算总页数
	if(total > 0){
	    pageCount = total % po.getPageSize() == 0 ? total / po.getPageSize() : total / po.getPageSize() + 1;
	}
	//计算从第几条记录开始查询
	if("ORACLE".equals(po.getDBTYPE())){
		int startTotal = (po.getPageNum() - 1) * po.getPageSize() + 1;
		int endTotal = startTotal + po.getPageSize() - 1;
		po.setStartTotal(startTotal);
		po.setEndTotal(endTotal);
	}else{
		int startTotal = (po.getPageNum() - 1) * po.getPageSize();
		po.setStartTotal(startTotal);
	}
	
	pager.setRows(dao.search(po));
	pager.setTotal(total);
	pager.setPageSize(po.getPageSize());
	pager.setPageNo(po.getPageNum());
	pager.setPageCount(pageCount);
	return pager;
}
	
	public int searchTotal(@Param("po") DevicesPo po){
		return dao.searchTotal(po);
	}
	
	public List<DevicesPo> searchList(@Param("po") DevicesPo po){
		return dao.searchList(po);
	};
	public List<DevicesPo> searchDetailById(@Param("po") DevicesPo po){
		return dao.searchDetailById(po);
	};
	
	public Map<String,Object> searchIndexInfo(@Param("po") IndexInfoPo po){
		Map<String, Object> map = new HashMap<String, Object>();
		List<DevicesPo> list = new ArrayList<>();
		list=dao.searchIndexInfo(po);
		map.put("indexInfo", list);
		return map;
	}
	
	public List<DevicesPo> searchByUid(@Param("po") DevicesPo po){
		return dao.searchByUid(po);
	}
	public List<SystemmgtPo> searchSoftInfo(@Param("po") DevicesPo po){
		return dao.searchSoftInfo(po);
	}
	
	public List<SsdPo> searchSsd(@Param("po") SsdPo po){
		//=====SSD卡基本信息======//
		IndexDataPo serPo = new IndexDataPo();
		serPo.setUid(po.getUid());
		serPo.setIndex_type(107);//SSD卡的indexType为107
		serPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<IndexDataPo> indexData = dao.getIndexByUidIndexType(serPo);
		
		List<SsdPo> ssdvalue = new ArrayList<>();
		
		for(int i=0; i<indexData.size(); i++)
		{
			int unit = indexData.get(i).getIndex_id()%10;
			if(indexData.get(i).getIndex_id()>=1070010 && unit==0)
			{
				SsdPo ssdPo = new SsdPo();
				ssdPo.setOsname(indexData.get(i++).getValue());//挂载地址
				ssdPo.setHealth(indexData.get(i++).getValue());//健康状态
				ssdPo.setSsdnum(indexData.get(i++).getValue());//SSD槽位号
				ssdPo.setSsdsn(indexData.get(i++).getValue());//序列号
				ssdPo.setTemps(indexData.get(i).getValue());//温度
				ssdvalue.add(ssdPo);
			}
		}
		
//		int num = dao.searchSsdNum(po);
//		po.setNum(num);
//			int[][] ssdindex = new int[6][5];
//			for(int i=0;i<6;i++){
//				ssdindex[i][0]=1070010+i*10;
//				for(int j=1;j<5;j++){
//					ssdindex[i][j]=ssdindex[i][0]+j;
//				}
//			}
//		
//			StringBuffer sb = new StringBuffer();
//			StringBuffer sb1 = new StringBuffer();
//			StringBuffer sb2 = new StringBuffer();
//			StringBuffer sb3 = new StringBuffer();
//			StringBuffer sb4 = new StringBuffer();
//			StringBuffer sb5 = new StringBuffer();
//			for (int i = 0; i < 5; i++) {
//				if(i == 0){
//					sb.append(ssdindex[0][i]);
//					sb1.append(ssdindex[1][i]);
//					sb2.append(ssdindex[2][i]);
//					sb3.append(ssdindex[3][i]);
//					sb4.append(ssdindex[4][i]);
//					sb5.append(ssdindex[5][i]);
//				}else{
//					sb.append(","+ssdindex[0][i]);
//					sb1.append(","+ssdindex[1][i]);
//					sb2.append(","+ssdindex[2][i]);
//					sb3.append(","+ssdindex[3][i]);
//					sb4.append(","+ssdindex[4][i]);
//					sb5.append(","+ssdindex[5][i]);
//				}
//			}
//			po.setIndex_id(sb.toString());
//			po.setIndex_id1(sb1.toString());
//			po.setIndex_id2(sb2.toString());
//			po.setIndex_id3(sb3.toString());
//			po.setIndex_id4(sb4.toString());
//			po.setIndex_id5(sb5.toString());
//			List<SsdPo> ssdvalue = dao.searchSsdValue(po);
			return ssdvalue;
	}
	public List<IbCardPo> searchIbCard(@Param("po") IbCardPo po){
		//=====IB卡基本信息======//
		IndexDataPo serPo = new IndexDataPo();
		serPo.setUid(po.getUid());
		serPo.setIndex_type(103);//IB卡的indexType为103
		serPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<IndexDataPo> indexData = dao.getIndexByUidIndexType(serPo);
		
		List<IbCardPo> ibcardvalue = new ArrayList<>();
		
		for(int i=0; i<indexData.size(); i++)
		{
			int unit = indexData.get(i).getIndex_id()%10;
			if(indexData.get(i).getIndex_id()>=1030010 && unit==0)
			{
				IbCardPo ibPo = new IbCardPo();
				ibPo.setIbname(indexData.get(i++).getValue());//网卡名称
				ibPo.setIbport(indexData.get(i++).getValue());//端口
				ibPo.setGid(indexData.get(i++).getValue());//gid
				ibPo.setState(indexData.get(i++).getValue());//逻辑状态
				ibPo.setPhysstate(indexData.get(i++).getValue());//物理状态
				ibPo.setRate(indexData.get(i).getValue());//端口速度
				ibcardvalue.add(ibPo);
			}
		}		
		
//		int num = dao.searchIbCardNum(po);
//			po.setNum(num);
//			int[] ibcardindex = new int[5];
//			int[] ibcardindex1 = new int[5];
//			int[] ibcardindex2 = new int[5];
//			int[] ibcardindex3 = new int[5];
//			int[] ibcardindex4 = new int[5];
//			int[] ibcardindex5 = new int[5];
//			int startssd = 1030009;
//			int startssd1 = 1030010;
//			int startssd2 = 1030011;
//			int startssd3 = 1030012;
//			int startssd4 = 1030013;
//			int startssd5 = 1030014;
//			
//			for (int i = 0,j = 0; i < 5; i++,j+=8) {
//				ibcardindex[i] = startssd+j;
//				ibcardindex1[i] = startssd1+j;
//				ibcardindex2[i] = startssd2+j;
//				ibcardindex3[i] = startssd3+j;
//				ibcardindex4[i] = startssd4+j;
//				ibcardindex5[i] = startssd5+j;
//			}
//			StringBuffer sb = new StringBuffer();
//			StringBuffer sb1 = new StringBuffer();
//			StringBuffer sb2 = new StringBuffer();
//			StringBuffer sb3 = new StringBuffer();
//			StringBuffer sb4 = new StringBuffer();
//			StringBuffer sb5 = new StringBuffer();
//			for (int i = 0; i < ibcardindex.length; i++) {
//				if(i == 0){
//					sb.append(ibcardindex[i]);
//					sb1.append(ibcardindex1[i]);
//					sb2.append(ibcardindex2[i]);
//					sb3.append(ibcardindex3[i]);
//					sb4.append(ibcardindex4[i]);
//					sb5.append(ibcardindex5[i]);
//				}else{
//					sb.append(","+ibcardindex[i]);
//					sb1.append(","+ibcardindex1[i]);
//					sb2.append(","+ibcardindex2[i]);
//					sb3.append(","+ibcardindex3[i]);
//					sb4.append(","+ibcardindex4[i]);
//					sb5.append(","+ibcardindex5[i]);
//				}
//			}
//			po.setIndex_id(sb.toString());
//			po.setIndex_id1(sb1.toString());
//			po.setIndex_id2(sb2.toString());
//			po.setIndex_id3(sb3.toString());
//			po.setIndex_id4(sb4.toString());
//			po.setIndex_id5(sb5.toString());
//			List<IbCardPo> ibcardvalue = dao.searchIbCardValue(po);
			return ibcardvalue;
	}
	
	public List<IpCardPo> searchIpCard(@Param("po") IpCardPo po){
		//=====IP卡基本信息======//
		IndexDataPo serPo = new IndexDataPo();
		serPo.setUid(po.getUid());
		serPo.setIndex_type(106);//IP卡的indexType为106
		serPo.setDBTYPE(MyPropertiesPersist.DBTYPE);
		List<IndexDataPo> indexData = dao.getIndexByUidIndexType(serPo);
		
		List<IpCardPo> ipcardvalue = new ArrayList<>();
		
		for(int i=0; i<indexData.size(); i++)
		{
			int unit = indexData.get(i).getIndex_id()%10;
			if(indexData.get(i).getIndex_id()>=1060010 && unit==0)
			{
				IpCardPo ipPo = new IpCardPo();
				ipPo.setIpport(indexData.get(i++).getValue());//端口号
				ipPo.setPorttype(indexData.get(i++).getValue());//端口类型
				ipPo.setRate(indexData.get(i).getValue());//端口速度
				ipcardvalue.add(ipPo);
			}
		}	
//		int num = dao.searchIpCardNum(po)/3;
//			po.setNum(num);
//			po.setNum(num);
//			int[][] ipindex = new int[20][3];
//			for(int i=0;i<20;i++)
//			{
//				ipindex[i][0] = 1060010+i*10;
//				for(int j=1;j<3;j++){
//					ipindex[i][j]=ipindex[i][0]+j;
//				}
//			}
//			StringBuffer sb = new StringBuffer();
//			StringBuffer sb1 = new StringBuffer();
//			StringBuffer sb2 = new StringBuffer();
//			StringBuffer sb3 = new StringBuffer();
//			StringBuffer sb4 = new StringBuffer();
//			StringBuffer sb5 = new StringBuffer();
//			StringBuffer sb6 = new StringBuffer();
//			StringBuffer sb7 = new StringBuffer();
//			StringBuffer sb8 = new StringBuffer();
//			StringBuffer sb9 = new StringBuffer();
//			StringBuffer sb10 = new StringBuffer();
//			StringBuffer sb11 = new StringBuffer();
//			StringBuffer sb12 = new StringBuffer();
//			StringBuffer sb13 = new StringBuffer();
//			StringBuffer sb14 = new StringBuffer();
//			StringBuffer sb15 = new StringBuffer();
//			StringBuffer sb16 = new StringBuffer();
//			StringBuffer sb17 = new StringBuffer();
//			StringBuffer sb18 = new StringBuffer();
//			StringBuffer sb19 = new StringBuffer();
//			for (int i = 0; i < 3; i++) {
//				if(i == 0){
//					sb.append(ipindex[0][i]);
//					sb1.append(ipindex[1][i]);
//					sb2.append(ipindex[2][i]);
//					sb3.append(ipindex[3][i]);
//					sb4.append(ipindex[4][i]);
//					sb5.append(ipindex[5][i]);
//					sb6.append(ipindex[6][i]);
//					sb7.append(ipindex[7][i]);
//					sb8.append(ipindex[8][i]);
//					sb9.append(ipindex[9][i]);
//					sb10.append(ipindex[10][i]);
//					sb11.append(ipindex[11][i]);
//					sb12.append(ipindex[12][i]);
//					sb13.append(ipindex[13][i]);
//					sb14.append(ipindex[14][i]);
//					sb15.append(ipindex[15][i]);
//					sb16.append(ipindex[16][i]);
//					sb17.append(ipindex[17][i]);
//					sb18.append(ipindex[18][i]);
//					sb19.append(ipindex[19][i]);
//				}else{
//					sb.append(","+ipindex[0][i]);
//					sb1.append(","+ipindex[1][i]);
//					sb2.append(","+ipindex[2][i]);
//					sb3.append(","+ipindex[3][i]);
//					sb4.append(","+ipindex[4][i]);
//					sb5.append(","+ipindex[5][i]);
//					sb6.append(","+ipindex[6][i]);
//					sb7.append(","+ipindex[7][i]);
//					sb8.append(","+ipindex[8][i]);
//					sb9.append(","+ipindex[9][i]);
//					sb10.append(","+ipindex[10][i]);
//					sb11.append(","+ipindex[11][i]);
//					sb12.append(","+ipindex[12][i]);
//					sb13.append(","+ipindex[13][i]);
//					sb14.append(","+ipindex[14][i]);
//					sb15.append(","+ipindex[15][i]);
//					sb16.append(","+ipindex[16][i]);
//					sb17.append(","+ipindex[17][i]);
//					sb18.append(","+ipindex[18][i]);
//					sb19.append(","+ipindex[19][i]);
//				}
//			}
//			po.setIndex_id(sb.toString());
//			po.setIndex_id1(sb1.toString());
//			po.setIndex_id2(sb2.toString());
//			po.setIndex_id3(sb3.toString());
//			po.setIndex_id4(sb4.toString());
//			po.setIndex_id5(sb5.toString());
//			po.setIndex_id6(sb6.toString());
//			po.setIndex_id7(sb7.toString());
//			po.setIndex_id8(sb8.toString());
//			po.setIndex_id9(sb9.toString());
//			po.setIndex_id10(sb10.toString());
//			po.setIndex_id11(sb11.toString());
//			po.setIndex_id12(sb12.toString());
//			po.setIndex_id13(sb13.toString());
//			po.setIndex_id14(sb14.toString());
//			po.setIndex_id15(sb15.toString());
//			po.setIndex_id16(sb16.toString());
//			po.setIndex_id17(sb17.toString());
//			po.setIndex_id18(sb18.toString());
//			po.setIndex_id19(sb19.toString());
//			List<IpCardPo> ipcardvalue = dao.searchIpCardValue(po);
			return ipcardvalue;
		}
	public List<DiscPo> searchDisc(@Param("po") DiscPo po){
		//=====Disc卡基本信息======//
		int num = dao.searchDiscNum(po);
			po.setNum(num);
			int[][] discindex = new int[5][8];
			for(int i=0;i<5;i++)
			{
				discindex[i][0] = 1050010+i*10;
				for(int j=1;j<8;j++){
					discindex[i][j]=discindex[i][0]+j;
				}
			}
			StringBuffer sb = new StringBuffer();
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			StringBuffer sb3 = new StringBuffer();
			StringBuffer sb4 = new StringBuffer();
			for(int i = 0; i < 8; i++){
				if(i == 0){
					sb.append(discindex[0][i]);
					sb1.append(discindex[1][i]);
					sb2.append(discindex[2][i]);
					sb3.append(discindex[3][i]);
					sb4.append(discindex[4][i]);	
				}else{
					sb.append(","+discindex[0][i]);
					sb1.append(","+discindex[1][i]);
					sb2.append(","+discindex[2][i]);
					sb3.append(","+discindex[3][i]);
					sb4.append(","+discindex[4][i]);
				}
			}
			po.setIndex_id(sb.toString());
			po.setIndex_id1(sb1.toString());
			po.setIndex_id2(sb2.toString());
			po.setIndex_id3(sb3.toString());
			po.setIndex_id4(sb4.toString());
			List<DiscPo> discvalue = dao.searchDiscValue(po);
			return discvalue;
		
	}
	
	public Map<String,Object> searchCpuTemp(@Param("po") IndexDataPo po){
		Map<String,Object> map = new HashMap<String, Object>();
		//======cpu温度=======//
		List<String> cpuvalue = new ArrayList<String>();
		int[] cpuTempindex ={1000011,1000012,1000013,1000014};
		int i = 0;
		for(int e : cpuTempindex){
			po.setIndex_id(e);
			List<IndexDataPo> indexinfo = dao.searchValue(po);
			if(indexinfo.size()>0){
				cpuvalue.add(indexinfo.get(0).getValue());
				cpuvalue.add(indexinfo.get(0).getIswarn().toString());
			}
			i++;
		}
		//=====电源功耗======//
		List<String> consvalue = new ArrayList<String>();
		int[] cpuConsindex ={1000041,1000042};
		for(int e : cpuConsindex){
			po.setIndex_id(e);
			List<IndexDataPo> indexinfo = dao.searchValue(po);
			if(indexinfo.size()>0){
			consvalue.add(indexinfo.get(0).getValue());
			consvalue.add(indexinfo.get(0).getIswarn().toString());
			}
		}
		//======风扇状态=====//
		List<String> fanvalue = new ArrayList<String>();
		int[] fanindex ={1000081,1000082,1000083,1000084,1000085,1000086,1000087,1000088,1000089,1000090};
		for(int e : fanindex){
			po.setIndex_id(e);
			List<IndexDataPo> indexinfo = dao.searchValue(po);
			if(indexinfo.size()>0){
			fanvalue.add(indexinfo.get(0).getValue());
			fanvalue.add(indexinfo.get(0).getIswarn().toString());
			}
		}
		//=====电压状态======//
		List<String> volsvalue = new ArrayList<String>();
		int[] cpuVolsindex ={1000031,1000032,1000033,1000034};
		for(int e : cpuVolsindex){
			po.setIndex_id(e);
			List<IndexDataPo> indexinfo = dao.searchValue(po);
			if(indexinfo.size()>0){
				volsvalue.add(indexinfo.get(0).getValue());
			}
		}
		//=======电源状态=======//
		List<String> powervalue = new ArrayList<String>();
		int[] powerindex ={1000051,1000052};
		for(int e : powerindex){
			po.setIndex_id(e);
			List<IndexDataPo> indexinfo = dao.searchValue(po);
			if(indexinfo.size()>0){
				powervalue.add(indexinfo.get(0).getValue());
				powervalue.add(indexinfo.get(0).getIswarn().toString());
			}
		}
		//=======进风口温度======//
		List<String> inputTempvalue = new ArrayList<String>();
		int[] inputTempindex ={1000010};
		for(int e : inputTempindex){
			po.setIndex_id(e);
			List<IndexDataPo> indexinfo = dao.searchValue(po);
			if(indexinfo.size()>0){
			inputTempvalue.add(indexinfo.get(0).getValue());
			inputTempvalue.add(indexinfo.get(0).getIswarn().toString());
			}
		}/**/
		//统一塞到map中去返回给前台
		map.put("cpucons", consvalue);
		map.put("cputemp", cpuvalue);
		map.put("fan", fanvalue);
		map.put("vols", volsvalue);
		map.put("inputtemp", inputTempvalue);
		map.put("power", powervalue);
		return map;
	}

	public List<RaidInfoPo> searchRaid(RaiddPo po) {
		//=====Raid基本信息======//
				int num = dao.searchRaidNum(po);
					po.setNum(num);
					int[][] raidindex = new int[25][9];
					for(int i=0;i<25;i++)
					{
						raidindex[i][0] = 1040080+i*10;
						for(int j=1;j<9;j++){
							raidindex[i][j]=raidindex[i][0]+j;
						}
					}
					List<RaidInfoPo> raidvalue = dao.searchRaidValue(po);
					int Rnum = raidvalue.size();
					for(int i = Rnum-1;i>=0;i--){	
						String mapped_device = raidvalue.get(i).getMapped_device();
						if(mapped_device== null || mapped_device.isEmpty()){
							raidvalue.remove(i);
						}
					}
					return raidvalue;
	}

	public Map<String, Object> searchEchartData(IndexDataPo po) {
		Map<String,Object> map = new HashMap<String, Object>();
		//======cpu使用率=======//
		List<String> cpuuseage = new ArrayList<String>();
		int cpuusindex =3000004;
		po.setIndex_id(cpuusindex);
		List<IndexDataPo> cupusinfo = dao.searchValue(po);
		if(cupusinfo.size()>0){
			cpuuseage.add(cupusinfo.get(0).getValue());
		}
		//======内存使用率=======//
		List<String> memuseage = new ArrayList<String>();
		int memusindex =3000005;
		po.setIndex_id(memusindex);
		List<IndexDataPo> memusinfo = dao.searchValue(po);
		if(memusinfo.size()>0){
			memuseage.add(memusinfo.get(0).getValue());
		}
		//======文件使用率=======//
		List<String> fileuseage = new ArrayList<String>();
		int fileusindex =3000006;
		po.setIndex_id(fileusindex);
		List<IndexDataPo> fileusinfo = dao.searchValue(po);
		if(fileusinfo.size()>0){
			fileuseage.add(fileusinfo.get(0).getValue());
		}
		
		map.put("cpuuseage", cpuuseage);
		map.put("memuseage", memuseage);
		map.put("fileuseage", fileuseage);
		return map;
	}
}