#!/bin/bash
#this script make new standby ready

#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/5.new-standby-ready_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================

waitTime=30
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
    echo "You must be the superuser to run this script" >&2
 sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi

su - oracle -c "sqlplus -S / as sysdba" <<eof 
alter database open;
alter database recover managed standby database  disconnect from session;
eof
sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "alter database recover managed standby database  disconnect from session"
sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 30 "alter database recover managed standby database  disconnect from session" 
############################修改备库的状态为 READ ONLY WITH APPLY ###################################################################
#su - oracle -c "sqlplus -S / as sysdba" << eof
#alter database recover managed standby database using current logfile disconnect from session;
#eof

open_mode=`su - oracle -c "sqlplus -S / as sysdba" << eof
select OPEN_MODE from v\\$database;
quit;
eof`

echo "[info] database open_mode: ${open_mode} ."
if [[ `echo $open_mode | awk '{printf  $(NF-3)$(NF-2)$(NF-1)$(NF)}'` != "READONLYWITHAPPLY" ]]
then
sh /root/return/task/commScript/callback/callLogManager.sh  "eror" "${taskId}" "${stepId}"  "the standby database open_mode is not READ ONLY WITH APPLY.\\r\\n detial:${open_mode}"
sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 40   "the standby database open_mode is not READ ONLY WITH APPLY." 
exit 1
fi

sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "the standby database open_mode is READ ONLY WITH APPLY."
sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 40   ""

####################################################################################################################################

stdstatlog5=/tmp/stdstat5.log
stdstatlog6=/tmp/stdstat6.log
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $stdstatlog5;
select SWITCHOVER_STATUS from v\$database;
spool off;
eof
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
spool $stdstatlog6;
select DATABASE_ROLE from v\$database;
spool off;
eof


pristat3=`cat /tmp/stdstat5.log|tr -d ' '`
pristat4=`cat /tmp/stdstat6.log|tr -d ' '`


if [ "$pristat3" = "NOTALLOWED" ] || [ "$pristat3" = "RECOVERYNEEDED" ]&& [ "$pristat4" = "PHYSICALSTANDBY" ]
then 
 echo "[info] new standby database is  receving redo log"
 sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "new standby database is now receving redo log"
 sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 70 "new standby database is  receving redo log" 
else
 echo "[error] new standby database is NOT receving redo log"
 sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"  "new standby database is NOT receving redo log"
 sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 70 "new standby database is NOT receving redo log" 
  exit 1
fi

times=0
while true
do
 times=$((1+times))
 s5=`su - oracle -c "sqlplus -S / as sysdba" <<eof
 set feedback off heading off pagesize 0;
 select SWITCHOVER_STATUS from v\\$database;
eof`
s6=`echo $s5|tr -d ' '`
 if [ $s6 = "NOTALLOWED" ]
 then
  sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"      "media recovery complete" 
  sh /root/return/task/commScript/callback/callTaskManager.sh  2 "${taskId}" "${stepId}" 100       "media recovery complete" 
  exit 0
  break
 else 
    sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 80      "media recovery NOT complete,please wait"
   sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"    "media recovery NOT complete,please wait" 
  if [ $times -gt $waitTime ]
  then
    sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"   "media recovery fail." 
    sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 80      "media recovery fail." 
    exit 1
  fi
 fi
sleep 10
done
