#!/bin/bash
#this script  switch primary to standby;

#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/3.primary-switch_${logFileName}.log
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
	sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi
##################################################################################################################################################
s1=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter database commit to switchover to physical standby with session shutdown;
eof`;

echo "[info] the operation result" ${s1}
sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "alter database commit to switchover to physical standby with session shutdown result  ${s1}"
if [ "Database altered. " = "`echo $s1|tr "\n" " "`" ]
 then
 	sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "alter database commit to switchover to physical standby with session shutdown SUCCESS" 
  sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 50 "alter database commit to switchover to physical standby with session shutdown" 
 else
 	sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"  "alter database commit to switchover to physical standby with session shutdown FAILED" 
 	sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 40 "alter database commit to switchover to physical standby with session shutdown FAILED" 
 	exit 1
 fi

##################################################################################################################################################
s2=`su - oracle -c "sqlplus -S / as sysdba" <<eof
startup nomount
alter database mount standby database;
eof`

if [[ `echo $s2|sed -n '/^ORACLE instance started.*Database altered/p'` ]]
then 
	sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "alter database mount standby database" 
	sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 80 "alter database mount standby database" 

else 
sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"  "alter database mount standby database FAILED" 
sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 80 "alter database mount standby database FAILED" 

fi

pristatlog3=/tmp/pristat3.log
pristatlog4=/tmp/pristat4.log
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $pristatlog3;
select SWITCHOVER_STATUS from v\$database;
spool off;
eof

su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $pristatlog4;
select DATABASE_ROLE from v\$database;
spool off;
eof


pristat3=`cat /tmp/pristat3.log|tr -d ' '`
pristat4=`cat /tmp/pristat4.log|tr -d ' '`

echo "pristat3: ${pristat3}" 
echo "pristat4: ${pristat4}" 

if [ "$pristat3" = "RECOVERYNEEDED" ] && [ "$pristat4" = "PHYSICALSTANDBY" ]
then 
echo "[info] primary database is already switched to standby"
sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "primary database is already switched to standby" 
sh /root/return/task/commScript/callback/callTaskManager.sh  2 "${taskId}" "${stepId}" 100 "primary database is already switched to standby" 

else
echo "[error] primary swtichover failed,need manual interaction"
sh /root/return/task/commScript/callback/callLogManager.sh  "error" "${taskId}" "${stepId}"  "primary swtichover failed,need manual interaction" 
sh /root/return/task/commScript/callback/callTaskManager.sh 3 "${taskId}" "${stepId}" 10 "primary swtichover failed,need manual interaction" 
fi