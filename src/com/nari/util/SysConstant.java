package com.nari.util;

/**
 * 配置系统常量
 * @author ydd
 *
 */
public class SysConstant {
	//获取池信息频率
	public static final long POOLSLEEP=1000*60*60*60;
	
	//计算节点组的设备类型
	public static final String DB_GROUP_CATEG = "0200";	
	//数据库主库
	public static final String DB_MAIN = "01";
	//数据库备库
	public static final String DB_STAN = "02";
	//数据库服务器的设备类型
	public static final String DB_CATEG = "0201";
	//健康状态
	public static final String HEALTH_STATE = "INFERRED_HEALTH_STATE";
	
	//存储节点组的设备类型
	public static final String MEM_CATEG = "0300";
	//存储服务器的设备类型
	public static final String MEM_SERVER_CATEG = "0301";
	
	//交换机的设备类型
	public static final String NET_CATEG = "0401";
	
	//监控页面展示
	public static final int ORDER_A = 0;//在监控主页展示
	public static final int ORDER_B = 1;//在悬浮框展示
	//是否展示在明细页面
	public static final int IS_DETAIL = 1;//在明细页面展示
	//VKVM获取服务器带外指标线程的频率
	public static final long DEVICEOUTSLEEP = 1*60*1000;
	//VKVM获取服务器带内指标线程的频率
	public static final long DEVICEINSLEEP = 3*60*1000;
	//获取数据库指标线程的频率
	public static final long DBSLEEP = 2*60*1000;
	//获取SSD卡指标线程的频率
	public static final long SSDSLEEP = 2*60*1000;
	//获取IB网卡状态线程的频率
	public static final long IBSLEEP = 100000L;
	//DG线程频率	
	public static final long DGSLEEP = 1000*60;
	//交换机获取指标的频率
	public static final long ICSLEEP = 1000*300;
	// 交换机状态：正常
	public static final String SWITCH_NORMAL = "正常";
	// 交换机状态：故障
	public static final String SWITCH_FALUT = "故障";
	// 超过阀值信息
	public static final String RS_DELAY_MESS = "超过阀值";
	
	public static final String IDENTIFY_CODE = "validatecoderecruit";
	
	//会话超时配置项代码
//	public static final String SESSION_CODE = "60";
	public static final String SESSION_CODE = "1800";
	//内存使用量
	public static final String MEMORY_USED = "MEMORY_USED";
	//内存总量
	public static final String MEMORY_TOTAL = "MEMORY_TOTAL";
	//内存利用率
	public static final String MEMORY_USAGE_PERCENT = "MEMORY_USAGE_PERCENT";
	//存储总量
	public static final String STORAGE_USED = "STORAGE_USED";
	//存储使用量
	public static final String STORAGE_TOTAL = "STORAGE_TOTAL";
	//cpu总量
	public static final String CPU_TOTAL = "CPU_TOTAL";
	//cpu使用量
	public static final String CPU_USAGE = "CPU_USAGE";
	//cpu使用率
	public static final String CPU_USAGE_PERCENT = "CPU_USAGE_PERCENT";
	//存储使用量
	public static final String STORAGE_USAGE_PERCENT = "STORAGE_USAGE_PERCENT";
	//服务器状态：连接
	
	public static final String SYSTEM_HEALTH = "SYSTEM_HEALTH";//UNHEALTH
	public static final String SYSTEM_HEALTH_NAME = "操作系统状态";//UNHEALTH
	
	//交换机指标参数
	public static final String  SnmpPort = "161";
	public static final String  SnmpVersion = "v1";
	public static final String  CommunityString = "111111";
	public static final String  IPMIKEY = "0000000000000000000000000000000000000000";
	//带内指标参数
	public static final String sysState = "sysState";
	
	public static final String DB_CPU = "DB_CPU"; 
	
	public static final String DB_MEM = "MEMORY_USAGE_PERCENT";
	
	//IOPS参数
	public static final String IOPS="IOPS";
	//MBPS参数
	public static final String MBPS="MBPS";
	
	//日志是否添加成功
	public static final String OPRTLOG_SECCUSS = "1";
	public static final String OPRTLOG_FAIL = "0";
	
	//事件类型
	public static final String SYSTEM_EVENT = "0";
	public static final String BUSSIENCE_EVENT = "1";
	
	//异常事件等级
	public static final String LOGS_EVENTS = "0";//日志
	public static final String IPCHANGE_ENENTS = "1";//ip变动
	public static final String LOGINFAIL_ENENTS = "2";//连续登陆失败
	public static final String YUEQUAN_ENENTS = "3";//越权访问
	
	//页面提示信息
	public static final String SAVE_SUCCESS = "保存成功！";
	public static final String SAVE_FILE = "保存失败！";
	public static final String DATE_INTEGRITY = "保存失败，数据完整性被破坏，请刷新页面后重试！";
	public static final String FILEUPLOAD_FILE = "上传失败，文件类型错误！";
	public static final String CHONTFUTIJIOA = "保存失败，请不要重复提交！";
	
	//系统角色
	public static final int XTGLY = 1;//系统管理员
	public static final int YWGLY = 2;//业务管理员
	public static final int SJGLY = 3;//审计管理员
	
	//内外网
	public static final int IN = 0;//内网
	public static final int OUT = 1;//外网
}
