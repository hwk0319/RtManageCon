#!/bin/bash
#failover
#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/1.failover-switch_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================



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
showdb=`su - oracle -c "sqlplus -S / as sysdba" <<eof
show parameter db_unique_name;
eof`
dbname=`echo $showdb | awk '{print $NF}'`

s1=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter system flush redo to ${dbname};
eof`

if [ "Database altered. " = "`echo $s1|tr "\n" " "`" ]
 then
 echo "[info] change database name success."
 sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "alter system flush redo to $dbname SUCCESS"
  sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}" "${stepId}" 20  "alter system flush redo to $dbname SUCCESS"
 else
 echo "[error] change database name error.msg:${s1}"
 sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"  "alter system flush redo to $dbname FAILED,msg:${s1}"
 sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 20 "alter system flush redo to $dbname FAILED,msg:${s1}"
 exit 1
 fi
 
 
 #=========================== 查看是否有归档日志的GAP：;===========================
s2=`su - oracle -c "sqlplus -S / as sysdba" <<eof
select * from v\\$archive_gap;
eof`

if [ "no rows selected " = "`echo $s2|tr "\n" " "`" ]
  then
  echo "[info] there is no gap, failover will not cause data loss"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "there is no gap, failover will not cause data loss"
sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 40    "there is gap, failover may case data loss"
else
  echo "[warn] there is gap, failover will cause DATA LOSS!"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "there is gap, failover will cause DATA LOSS!"
  sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 40    "there is gap, failover will cause DATA LOSS!"
  exit 1
fi



#=========================== alter database recover managed standby database cancel;===========================
s3=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter database recover managed standby database cancel;
eof`

if [ "Database altered. " = "`echo $s3|tr "\n" " "`" ]
then 
  echo "[info] alter database recover managed standby database cancel. success"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "alter database recover managed standby database cancel. success"
  sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}"  "${stepId}"   60   "alter database recover managed standby database cancel. success"
else
  echo "[error] alter database recover managed standby database cancel. error msg: ${s3}"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"         "alter database recover managed standby database cancel. error msg: ${s3}"
  sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}"    60   "alter database recover managed standby database cancel. error msg: ${s3}"
  exit 1
fi




#=========================== alter database recover managed standby database finish;===========================
s4=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter database recover managed standby database finish;
eof`

if [ "Database altered. " = "`echo $s4|tr "\n" " "`" ]
then
	echo "[info] alter database recover managed standby database finish.success"
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "#"
	sh /root/return/task/commScript/callback/callTaskManager.sh  2 "${taskId}" "${stepId}" 80 "#"
else
  echo "[error] alter database recover managed standby database finish.error msg: ${s4}"
	sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}"  "${stepId}"     "alter database recover managed standby database finish.error msg: ${s4}"
	sh /root/return/task/commScript/callback/callTaskManager.sh     3    "${taskId}" "${stepId}" 80  "alter database recover managed standby database finish.error msg: ${s4}"
	exit 1
fi


#######################restart database######################################
showdb=`su - oracle -c "sqlplus -S / as sysdba" <<eof
show parameter db_name;
eof`
dbname=`echo $showdb | awk '{print $NF}'`

echo "[info] the oracle dbname is ${dbname}"
echo "[info] begin restart the database"


stopMsg=`su - grid -c "srvctl stop database -d ${dbname}"`

if [ -z ${stopMsg} ]
  then
   echo "[info] stop database success."	
   sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "stop database on $hostFlag success. "
   sh /root/return/task/commScript/callback/callTaskManager.sh    1    "${taskId}" "${stepId}" 90  "stop database on $hostFlag success. "
  else
  echo "[error] stop database fail. msg:${stopMsg}"
     sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "stop database on $hostFlag error. failMsg:${stopMsg}. "
   sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 90    "stop database on $hostFlag error. failMsg:${stopMsg}. "
exit 1
fi

startMsg=`su - grid -c "srvctl start database -d ${dbname}"`

if [ -z ${startMsg} ]
  then
   echo "[info] start database success."
   sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "start database on $hostFlag success. "
   sh /root/return/task/commScript/callback/callTaskManager.sh    2    "${taskId}" "${stepId}" 100  "start database on $hostFlag success. "
  else
  echo "[error] start database fail. msg:${startMsg}"
   sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "stop database on $hostFlag error. failMsg:${startMsg}. "
   sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 90  "stop database on $hostFlag error. failMsg:${startMsg}. "
exit 1
fi




