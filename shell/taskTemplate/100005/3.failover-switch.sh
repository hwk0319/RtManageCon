#!/bin/bash
#failover
#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/3.failover-switch_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================

#通过集群命令停止集群 
function stopDatabase()
{
  insName=$1
  stopMsg=`su - grid -c "srvctl stop database -d $insName"`
  
  if [[ -z ${stopMsg} ]]
    then
     echo "[info] stop database success."
     sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "stop database on $hostFlag success. "
  #   sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}" "${stepId}" 90  "stop database on $hostFlag success. "
  return 0
    else
    echo "[error] stop database fail. msg:${stopMsg}"
       sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "stop database on $hostFlag error. failMsg:${stopMsg}. "
  #     sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 90    "stop database on $hostFlag error. failMsg:${stopMsg}. "
  return 1
  fi
}
#通过集群命令启动集群 
function startDatabase()
{
  insName=$1
  startMsg=`su - grid -c "srvctl start database -d $insName"`
  
  if [[ -z ${startMsg} ]]
    then
     echo "[info] start database success."
     sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "start database on $hostFlag success. "
  #   sh /root/return/task/commScript/callback/callTaskManager.sh    2    "${taskId}" "${stepId}" 100  "start database on $hostFlag success. "
  return 0
    else
    echo "[error] start database fail. msg:${startMsg}"
     sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "stop database on $hostFlag error. failMsg:${startMsg}. "
  #   sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 90  "stop database on $hostFlag error. failMsg:${startMsg}. "
  return 1
  fi
}

###############################重启节点##########################################################
#                                                                                               #
# 由于节点状态与服务状态可能出现不一致状态，状态不一致的时候执行集群的start ,stop会报错。       #
# 根据是否返回报错信息进行判断，重启操作是否成功。逻辑如下：                                    #
#1-stop 成功 2-start  成功 返回后台成功。| 失败 返回后台失败                                    #
#       失败 2-start  3-stop 成功 4-start 成功 返回后台成功。| 失败 返回后台失败                #
#                            失败 返回后台失败                                                  #
#                                                                                               #
#################################################################################################
function restartService()
{
serName=`su - oracle -c "sqlplus -S / as sysdba" << eof
show parameter db_name;
eof`
  insName=`echo $serName | awk '{printf $NF}' `
  
  echo "[info] the oracle service is $insName"
  echo "[info] begin restart the database"
  
  
  stopDatabase  $insName
  if [[ $? = 0 ]]
  then
    echo "[info] stop database success."
    startDatabase $insName
    if [[ $? = 0 ]]
    then
     echo "[info] start database success."
     sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "restart $insName success."   
     sh /root/return/task/commScript/callback/callTaskManager.sh    2    "${taskId}" "${stepId}" 100  "restart $insName success."
     exit 0
    else
     echo "[error] start database fail. msg:${startMsg}"
     sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "restart $insName fail!."   
     sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 90  "restart $insName fail!."
     exit 1
    fi
  else
    echo "[error] stop database fail. msg:${stopMsg}"
    startDatabase $insName
    stopDatabase  $insName
    if [[ $? = 0 ]]
    then
      echo "[info] stop database success."
       startDatabase $insName
       if [[ $? = 0 ]]
       then
          echo "[info] start database success."
          sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "restart $insName success."   
          sh /root/return/task/commScript/callback/callTaskManager.sh    2    "${taskId}" "${stepId}" 100  "restart $insName success."
          exit 0
       else
          echo "[error] start database fail. msg:${startMsg}"
          sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "restart $insName fail!."   
          sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 90  "restart $insName fail!."
          exit 1
       fi
    else
       echo "[error] stop database fail. msg:${stopMsg}"
       sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "restart $insName fail!."   
       sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 80 "restart $insName fail!."
       exit 1
    fi
  fi
}



timestamp=$(date +'%Y-%m-%d %H:%M:%S')
echo $timestamp 
#source ${1}
hostFlag=$(hostname)
echo "hostflag="$hostFlag 
hostName=$(hostname)
echo "hostname="$hostName 
taskId=${2}
echo "task id="$taskId 
stepId=${3}
echo "step id"=$stepId 


if [ $(id -u) != "0" ]; then
    echo "You must be the superuser to run this script" >&2
    sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 0 "you must be root user to run this task."
    exit 1
fi

#===========================  alter system flush redo to 'standby';=========================== 
showdb=`su - oracle -c "sqlplus -S / as sysdba" << eof
show parameter db_unique_name;
eof`
dbname=`echo $showdb | awk '{print $NF}'`
echo "[info] the db_unique_name is ${dbname}"
s1=`su - oracle -c "sqlplus -S / as sysdba" << eof
alter system flush redo to '${dbname}';
eof`

if [ "System altered. " = "`echo $s1|tr "\n" " "`" ]
 then
 echo "[info] change database name success."
 sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "alter system flush redo to $dbname SUCCESS"
  sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}" "${stepId}" 20  "alter system flush redo to $dbname SUCCESS"
 else
 #出现问题可能导致，数据丢失，继续执行。
 echo "[error] change database name error.msg:${s1}"
 sh /root/return/task/commScript/callback/callLogManager.sh   "warn" "${taskId}" "${stepId}"  "alter system flush redo to $dbname FAILED,msg:${s1}"
 sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 20 "alter system flush redo to $dbname FAILED,msg:${s1}"
fi
 
 
#=========================== 查看是否有归档日志的GAP：;===========================
s2=`su - oracle -c "sqlplus -S / as sysdba" << eof
select * from v\\$archive_gap;
eof`

if [ "no rows selected " = "`echo $s2|tr "\n" " "`" ]
  then
  echo "[info] there is no gap, failover will not cause data loss"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "there is no gap, failover will not cause data loss"
  sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 30    "there is gap, failover may case data loss"
else
  echo "[warn] there is gap, failover will cause DATA LOSS!"
  sh /root/return/task/commScript/callback/callLogManager.sh   "warn" "${taskId}" "${stepId}"  "there is gap, failover will cause DATA LOSS!"
  sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 30    "there is gap, failover will cause DATA LOSS!"
fi

#===========================alter database recover managed standby database cancel;然后停止应用归档===========================
s3=`su - oracle -c "sqlplus -S / as sysdba" << eof
alter database recover managed standby database cancel;
eof`

if [ "Database altered. " = "`echo $s3|tr "\n" " "`" ]
then 
  echo "[info] alter database recover managed standby database cancel. success"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "alter database recover managed standby database cancel. success"
  sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}"  "${stepId}"   40   "alter database recover managed standby database cancel. success"
else
#给出警告继续执行，（和主库已经没有关系了）。
  echo "[error] alter database recover managed standby database cancel. error msg: ${s3}"
  sh /root/return/task/commScript/callback/callLogManager.sh   "warn" "${taskId}" "${stepId}"         "alter database recover managed standby database cancel. error msg: ${s3}"
  sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}" "${stepId}"    40   "alter database recover managed standby database cancel. error msg: ${s3}"
fi

#=========================== alter database recover managed standby database finish;下面将STANDBY数据库切换为PRIMARY数据库===========================
s4=`su - oracle -c "sqlplus -S / as sysdba" << eof
alter database recover managed standby database finish;
eof`

if [ "Database altered. " = "`echo $s4|tr "\n" " "`" ]
then
  echo "[info] alter database recover managed standby database finish.success"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "alter database recover managed standby database finish success"
  sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 50 "alter database recover managed standby database finish success"
else
  echo "[error] alter database recover managed standby database finish.error msg: ${s4}"
sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}"  "${stepId}"     "alter database recover managed standby database finish.error msg: ${s4}"
   sh /root/return/task/commScript/callback/callTaskManager.sh     3    "${taskId}" "${stepId}" 50  "alter database recover managed standby database finish.error msg: ${s4}"
exit 1
fi


#=============================================================检查状态如果为正常finish就退出。
s5=`su - oracle -c "sqlplus -S / as sysdba" << eof
select database_role,open_mode from v\\$database;
eof`
statusCheck=`echo $s5 | awk '{printf $5$6$7}'`

if [[ "${statusCheck}" = "PRIMARYREADWRITE" ]]
  then 
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}"  "${stepId}"     "database failover finish,begin to restart the database."
  sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 60  "database failover finish,begin to restart the database."
   restartService
  exit 0
fi



#==============下面将STANDBY数据库切换为PRIMARY数据库===========================================================
s6=`su - oracle -c "sqlplus -S / as sysdba" << eof
alter database commit to switchover to primary with session shutdown;
eof`

if [ "Database altered. " = "`echo $s6|tr "\n" " "`" ]
then 
  echo "[info] alter database recover managed standby database cancel. success"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "lter database commit to switchover to primary  with session shutdown. success"
  sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}"  "${stepId}"   60   "lter database commit to switchover to primary  with session shutdown. success"
else
  echo "[error] alter database recover managed standby database cancel. error msg: ${s3}"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "alter database commit to switchover to primary  with session shutdown;. error msg: ${s6}"
  sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}"    60   "alter database commit to switchover to primary  with session shutdown;. error msg: ${s6}"
  exit 1
fi

#==所有节点都执行？
s7=`su - oracle -c "sqlplus -S / as sysdba" << eof
alter database open;
eof`
if [ "Database altered. " = "`echo $s7|tr "\n" " "`" ]
then 
  echo "[info] alter database recover managed standby database cancel. success"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "alter database open;. success"
  sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}"  "${stepId}"   90   "alter database open;. success"
else
  echo "[error] alter database recover managed standby database cancel. error msg: ${s3}"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "alter database open;;. error msg: ${s6}"
  sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}"    80   "alter database open;;. error msg: ${s6}"
  exit 1
fi

#======================================================检查状态是否切换成功。
s8=`su - oracle -c "sqlplus -S / as sysdba" << eof
select database_role,open_mode from v\\$database;
eof`
statusCheck=`echo $s8 | awk '{printf $5$6$7}'`

if [[ "${statusCheck}" = "PRIMARYREADWRITE" ]]
  then 
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "check database stat :${statusCheck}"
  sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 100           "check database stat :${statusCheck}"
 else
 sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"         "check database stat :${statusCheck}"
 sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 80   "check database stat :${statusCheck}"
exit 1
fi

#======================================重启节点
restartService












