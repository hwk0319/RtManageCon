#!/bin/bash
#this script  switch standby to primary;

#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/5.standby-switch_${logFileName}.log
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
 sh /root/return/task/commScript/callback/callTaskManager.sh   3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi

s1=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter database recover managed standby database cancel;
shutdown immediate;
startup mount;
eof`

echo "[info] alter database recover managed standby database cancel : $s1"
sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 40 "alter database recover managed standby database cancel : $s1" 

s2=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter database recover managed standby database disconnect from session;
eof`

echo "[info] alter database recover managed standby database  disconnect from session: $s2"
sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 60 "alter database recover managed standby database  disconnect from session: $s2" 

s3=`su - oracle -c "sqlplus -S / as sysdba" <<eof
alter database commit to switchover to primary;
alter database open;
eof`

echo "[info] alter database commit to switchover to primary. $s3"


sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "alter database commit to switchover to primary: $s3"
sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 80 "alter database commit to switchover to primary: $s3" 
 

times=0
waitTime=20
while true
do
times=$((1+times))
sl=`su - oracle -c "sqlplus -S / as sysdba" <<eof
select SWITCHOVER_STATUS,DATABASE_ROLE from v\\$database;
eof`
ff=`echo $sl | awk '{ printf $5 $6 $7 }'`

s6=`echo $s5|tr -d ' '`
 if [ "$ff" = "TOSTANDBYPRIMARY" ] || [ "$ff" = "SESSIONSACTIVEPRIMARY" ]
 then
   echo "[info] standby database is already switched to primary"
   sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "standby database is already switched to primary" 
   sh /root/return/task/commScript/callback/callTaskManager.sh  2 "${taskId}" "${stepId}" 100  "standby database is already switched to primary" 
   exit 0
 else 
    echo "[info] standby switch NOT complete,please wait"
    sh /root/return/task/commScript/callback/callTaskManager.sh  1 "${taskId}" "${stepId}" 80      "standby switch NOT complete,please wait: $ff"
    sh /root/return/task/commScript/callback/callLogManager.sh   "info" "${taskId}" "${stepId}"    "standby switch NOT complete,please wait: $ff" 
  if [ $times -gt $waitTime ]
   then
     echo "[error] standby swtichover failed,need manual interaction"
     echo "SWITCHOVER_STATUS:$pristat3   DATABASE_ROLE:$pristat4"
     sh /root/return/task/commScript/callback/callLogManager.sh  "error" "${taskId}" "${stepId}"  "standby swtichover failed,need manual interaction SWITCHOVER_STATUS:$pristat3   DATABASE_ROLE:$pristat4" 
     sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 80    "standby swtichover failed,need manual interaction SWITCHOVER_STATUS:$pristat3   DATABASE_ROLE:$pristat4" 
     exit 1
  fi
 fi
sleep 10
done



