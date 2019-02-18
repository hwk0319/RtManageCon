package com.nari.taskmanager.config;

/**
 * 记录预设任务的任务ID。
 * @author admin
 *
 */
public class PresetTaskConfig {
	
	//=================================数据库切换=======================================================
	
	/**
	 * 单机数据库切换
	 */
	public static final int ORACLE_DG_SWITCH_TASKID=100000;
	public static final String ORACLE_SWITCH_SHELLNAME_1 = "1.check-primary-stat.sh";
	public static final String ORACLE_SWITCH_SHELLNAME_2 = "2.check-standby-stat.sh";
	public static final String ORACLE_SWITCH_SHELLNAME_3 = "3.primary-switch.sh";
	public static final String ORACLE_SWITCH_SHELLNAME_4 = "4.standby-switch.sh";
	public static final String ORACLE_SWITCH_SHELLNAME_5 = "5.new-standby-ready.sh";
	
	/**
	 * rac数据库切换
	 */
	public static final int ORACLE_RDG_SWITCH_TASKID=100004;
	public static final String ORACLE_RDGSWITCH_SHELLNAME_1 = "1.check-primary-stat.sh";
	public static final String ORACLE_RDGSWITCH_SHELLNAME_2 = "2.check-standby-stat.sh";
	
	public static final String ORACLE_RDGSWITCH_SHELLNAME_2_5 = "3.check-redo-stat.sh";
	public static final String ORACLE_RDGSWITCH_SHELLNAME_3 = "3.other-node-shutdown.sh";
	public static final String ORACLE_RDGSWITCH_SHELLNAME_4 = "4.primary-switch.sh";
	public static final String ORACLE_RDGSWITCH_SHELLNAME_5 = "5.standby-switch.sh";
	public static final String ORACLE_RDGSWITCH_SHELLNAME_6 = "6.new-standby-ready.sh";
	public static final String ORACLE_RDGSWITCH_SHELLNAME_7 = "7.other-node-startup.sh";
	
	/**
	 * 数据库failover
	 */
	public static final int ORACLE_DG_FAILOVER_TASKID=100003;
	public static final String ORACLE_DG_FAILOVER_SHELLNAME_1 = "failover-switch.sh";
	
	/**
	 * rac数据库failover
	 */
	public static final int ORACLE_RDG_FAILOVER_TASKID=100005;
	public static final String ORACLE_RDG_FAILOVER_SHELLNAME_1 = "1.failover-check.sh";//只在操作节点执行
	public static final String ORACLE_RDG_FAILOVER_SHELLNAME_2 = "2.failover-otherNodeShudown.sh";//只在飞操作节点执行
	public static final String ORACLE_RDG_FAILOVER_SHELLNAME_3 = "3.failover-switch.sh";//只在操作节点执行
	
	
	//=================================一体机启停=======================================================
	public static final int RETURN_ALLINONE_START_TASKID=100001;
	public static final int RETURN_ALLINONE_STOP_TASKID=100002;
	public static final String USER_OPERATION_START="start";
	public static final String USER_OPERATION_STOP="stop";

	public static final String RETURN_START_SHELLNAME_1 = "start1-startvcs.sh";
	public static final String RETURN_START_SHELLNAME_2 = "start2-startcrs.sh";
	public static final String RETURN_START_SHELLNAME_3 = "start3-startdb.sh";
	
	public static final String RETURN_STOP_SHELLNAME_1 = "stop1-stopdb.sh";
	public static final String RETURN_STOP_SHELLNAME_2 = "stop2-stopcrs.sh";
	public static final String RETURN_STOP_SHELLNAME_3 = "stop3-stopvcs.sh";
	

}
