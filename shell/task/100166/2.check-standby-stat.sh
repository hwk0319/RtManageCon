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
    echo "You must be the superuser to run this script"
 sh /root/return/task/commScript/callback/callTaskManager.sh   3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi

##########################################################################################################
#查询redo状态
#for (( i=1;i<=${racnum};i++))
#do
#logv=`su - oracle -c "sqlplus -S / as sysdba" << eof
##select max(sequence#) from v\\$archived_log where applied='YES' and thread#=$i;
#eof`
#logver=`echo $logv | awk '{ printf $NF }'`
#echo "[info] the redo log sequence is ${logver} for thread $i"
# sh /root/return/task/commScript/callback/callTaskManager.sh  4 "${taskId}" "${stepId}" 20 "standbyRedo-${i}:${logver}"
# sleep 1
#done
#########################################################################################################################
#archiveInfo=`su - oracle -c "sqlplus -S / as sysdba" << eof
#select max(first_change#),max(next_change#) from v\\$archived_log where applied='YES';
#quit;
#eof`
#echo "[info] the archiveInfo:$archiveInfo"
#first_change=`echo $archiveInfo | awk '{printf $(NF-1) }'`
#next_change=`echo $archiveInfo | awk '{printf $(NF) }'`
#echo "[info] the first_change is ${first_change}"
#echo "[info] the next_change  is ${next_change}"
#sh /root/return/task/commScript/callback/callTaskManager.sh   4 "${taskId}" "${stepId}" 20 "sta_first_change:$first_change";
#sh /root/return/task/commScript/callback/callTaskManager.sh   4 "${taskId}" "${stepId}" 20 "sta_next_change:$next_change";

########################################################################################################################
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
sh /root/return/task/commScript/callback/callLogManager.sh    "info" "${taskId}" "${stepId}"  "standby database : SWITCHOVER_STATUS : $stdstat1, DATABASE_ROLE : $stdstat2 " 
sh /root/return/task/commScript/callback/callTaskManager.sh   2 "${taskId}" "${stepId}" 100    "standby database : SWITCHOVER_STATUS : $stdstat1, DATABASE_ROLE : $stdstat2 " 
else
echo "[info] standby database is not ready to switchover."
sh /root/return/task/commScript/callback/callLogManager.sh    "error" "${taskId}" "${stepId}"  "standby database is NOT ready to switchover" 
sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 50 "standby database is NOT ready to switchover" 
fi

#standby 查询完成后 通知后台进行判断
#sh /root/return/task/commScript/callback/callTaskManager.sh  5 "${taskId}" "${stepId}" 100 "redoThread:check"
#sleep 5

#sh /root/return/task/commScript/callback/callTaskManager.sh  5 "${taskId}" "${stepId}" 90 "change:check"
#sleep 5











