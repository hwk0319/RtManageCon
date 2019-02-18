#!/bin/bash
#failover
#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/1.failover-check_${logFileName}.log
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
#====判断是否mount
checkmount=`su - oracle -c "sqlplus -S / as sysdba" << eof
select database_role,open_mode from v\\$database;
eof`
needstat=`echo ${checkmount} | awk '{ printf $5 " " $6 " " $7}'`;
if [[ "${needstat}" = "PHYSICAL STANDBY MOUNTED" ]]
  then 
   echo "[info] check database status ${needstat}"
   sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "chech database status success:${needstat}"
   sh /root/return/task/commScript/callback/callTaskManager.sh    2    "${taskId}" "${stepId}" 100  "chech database status success:${needstat}"
else
echo "[info] the database open_mode not correct need restart.:"
sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "chech database status fail:${needstat}，begin to restart the database"
#没有mount进行重启
shutdown=`su - oracle -c "sqlplus -S / as sysdba" << eof
shutdown immediate;
eof`
  if [[ -n `echo $shutdown | grep "ORACLE instance shut down"` ]]
  then
    echo "[info] shutdown success."
      sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "database shutdown success."
  else
  sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "database shutdown error.：$shutdown"
  sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 30  "database shutdown error.：$shutdown"
exit 1
  fi
  
startup=`su - oracle -c "sqlplus -S / as sysdba" << eof
startup mount;
eof`
  if [[ -n `echo $startup | grep "Database mounted."`  ]]
  then
  echo "[info] database startup success."
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "database startup success.$startup"
  else
  echo "[error] the database starup error.:${startup}"
  sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "database startup error.:$startup"
  sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 60 "database shutdown error. $startup"
  exit 1
  fi
#重启后再检查  
  checkmount=`su - oracle -c "sqlplus -S / as sysdba" << eof
select database_role,open_mode from v\\$database;
eof`
needstat=`echo ${checkmount} | awk '{ printf $5 " " $6 " " $7}'`;
if [[ "${needstat}" = "PHYSICAL STANDBY MOUNTED" ]]
  then 
   echo "[info] check database status ${needstat}"
   sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"     "chech database status success:${needstat}"
   sh /root/return/task/commScript/callback/callTaskManager.sh    2    "${taskId}" "${stepId}" 100  "chech database status success:${needstat}"
else
   echo "[error] check database status ${needstat},after restart."
   sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"     "check database status error,: ${needstat},after restart."
   sh /root/return/task/commScript/callback/callTaskManager.sh    3    "${taskId}" "${stepId}" 60  "check database status error,: ${needstat},after restart."
exit 1
fi
  
  
fi