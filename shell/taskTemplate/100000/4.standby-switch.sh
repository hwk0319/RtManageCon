#!/bin/bash
#this script  switch standby to primary;

#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/4.standby-switch_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================

timestamp=$(date +'%Y-%m-%d %H:%M:%S')
echo $timestamp >  temp.log

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
	sh /root/return/task/commScript/callback/callTaskManager.sh   3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi

LOG1=/tmp/std-to-pri1.log
su - oracle -c "sqlplus -S / as sysdba" <<eof 
set feedback off heading off pagesize 0;
spool $LOG1;
alter database recover managed standby database  disconnect from session;
shutdown immediate
startup mount
alter database commit to switchover to primary; 
alter database open;
spool off;
eof

s3=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter database recover managed standby database  disconnect from session;
shutdown immediate
startup mount
alter database commit to switchover to primary; 
alter database open;
eof`
echo $s3

sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "alter database commit to switchover to primary"
sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 80 "alter database commit to switchover to primary" 
 

stdstatlog3=/tmp/stdstat3.log
stdstatlog4=/tmp/stdstat4.log
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $stdstatlog3;
select SWITCHOVER_STATUS from v\$database;
spool off;
eof

su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $stdstatlog4;
select DATABASE_ROLE from v\$database;
spool off;
eof

pristat3=`cat /tmp/stdstat3.log|tr -d ' '`
pristat4=`cat /tmp/stdstat4.log|tr -d ' '`
echo "pristat3: ${pristat3}" >>temp.log
echo "pristat4: ${pristat4}" >>temp.log

if [ "$pristat3" = "SESSIONSACTIVE" ] || [ "$pristat3" = "TOSTANDBY" ] && [ "$pristat4" = "PRIMARY" ]
then 
echo "[info] standby database is already switched to primary"
sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "standby database is already switched to primary" 
sh /root/return/task/commScript/callback/callTaskManager.sh  2 "${taskId}" "${stepId}" 100  "standby database is already switched to primary" 

else
echo "[error] standby swtichover failed,need manual interaction"
sh /root/return/task/commScript/callback/callLogManager.sh  "error" "${taskId}" "${stepId}"  "standby swtichover failed,need manual interaction" 
sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 10    "standby swtichover failed,need manual interaction" 
fi



