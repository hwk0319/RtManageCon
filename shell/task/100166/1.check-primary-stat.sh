#!/bin/bash
#this script check primary database status
#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/1.check-primary-stat_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================



timestamp=$(date +'%Y-%m-%d %H:%M:%S')
echo $timestamp 
source ${1}
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

##########################################################################################################
#查询redo状态
#for (( i=1;i<=${racnum};i++))
#do
#logv=`su - oracle -c "sqlplus -S / as sysdba" << eof
#select max(sequence#) from v\\$archived_log where applied='YES' and thread#=$i;
#eof`
#echo "[info] query archived log : ${logv}"
#logver=`echo $logv | awk '{ printf $NF }'`
#echo "[info] the redo log sequence is ${logver} for thread $i"
# sh /root/return/task/commScript/callback/callTaskManager.sh  4 "${taskId}" "${stepId}" 20 "primaryRedo-${i}:${logver}"
# sleep 1
#done
#############################################################################################################
#archiveInfo=`su - oracle -c "sqlplus -S / as sysdba" << eof
#select max(first_change#),max(next_change#) from v\\$archived_log;
#quit;
#eof`
#echo "[info] the archiveInfo:$archiveInfo"

#first_change=`echo $archiveInfo | awk '{printf $(NF-1) }'`
#next_change=`echo $archiveInfo | awk '{printf $(NF) }'`

#echo "[info] the first_change is ${first_change}"
#echo "[info] the next_change  is ${next_change}"
#sh /root/return/task/commScript/callback/callTaskManager.sh   4 "${taskId}" "${stepId}" 20 "pri_first_change:$first_change";
#sh /root/return/task/commScript/callback/callTaskManager.sh   4 "${taskId}" "${stepId}" 20 "pri_next_change:$next_change";

##############################################################################################################


pristatlog1=/tmp/pristat1.log
pristatlog2=/tmp/pristat2.log
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $pristatlog1;
select SWITCHOVER_STATUS from v\$database;
spool off;
eof

su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $pristatlog2;
select DATABASE_ROLE from v\$database;
spool off;
eof

pristat1=`cat /tmp/pristat1.log|tr -d ' '`
pristat2=`cat /tmp/pristat2.log|tr -d ' '`
echo "pristat1 : $pristat1"
echo "pristat2 : $pristat2"

if [ "$pristat1" = "SESSIONSACTIVE" ] || [ "$pristat1" = "TOSTANDBY" ] && [ "$pristat2" = "PRIMARY" ]
then
 echo "[info] primary host ready to switch."
sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"  "primary database is ready to switchover"
sh /root/return/task/commScript/callback/callTaskManager.sh   2 "${taskId}" "${stepId}" 100 "primary database is ready to switchover"
else
 echo "[error] primary host not ready to switch."
sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"  "primary database is NOT ready to switchover"
sh /root/return/task/commScript/callback/callTaskManager.sh   3 "${taskId}" "${stepId}" 10 "primary database is NOT ready to switchover"
fi