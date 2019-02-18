#!/bin/bash
#this script check standby database status

#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/2.check-standby-stat_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================

timestamp=$(date +'%Y-%m-%d %H:%M:%S')
echo $timestamp >  temp.log

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
	sh /root/return/task/commScript/callback/callTaskManager.sh   3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi

stdstatlog1=/tmp/stdstat1.log
stdstatlog2=/tmp/stdstat2.log
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $stdstatlog1;
select SWITCHOVER_STATUS from v\$database;
spool off;
eof
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $stdstatlog2;
select DATABASE_ROLE from v\$database;
spool off;
eof


stdstat1=`cat /tmp/stdstat1.log|sed s/[[:space:]]//g`
stdstat2=`cat /tmp/stdstat2.log|sed s/[[:space:]]//g`



if [ "$stdstat1" = "NOTALLOWED" ] && [ "$stdstat2" = "PHYSICALSTANDBY" ]
then 
echo "[info] standby database is ready to switchover."
sh /root/return/task/commScript/callback/callTaskManager.sh   2 "${taskId}" "${stepId}" 100 "standby database is ready to switchover" 
sh /root/return/task/commScript/callback/callLogManager.sh    "info" "${taskId}" "${stepId}"  "standby database is ready to switchover" 
else
echo "[info] standby database is not ready to switchover."
sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 10 "standby database is NOT ready to switchover" 
sh /root/return/task/commScript/callback/callLogManager.sh    "error" "${taskId}" "${stepId}"  "standby database is NOT ready to switchover" 
fi
