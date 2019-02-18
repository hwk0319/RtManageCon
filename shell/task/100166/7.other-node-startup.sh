#!/bin/bash
#this script start database instance
#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/7.other-node-startup_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================

if [ $(id -u) != "0" ]; then
    echo "You must be the superuser to run this script" >&2
 sh /root/return/task/commScript/callback/callTaskManager.sh 3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi

GETLOG(){
errmsg=`cat ./err.log`
sucmsg=`cat ./suc.log`
}

timestamp=$(date +'%Y-%m-%d %H:%M:%S')

#source ${1}
hostFlag=$(hostname)
echo "hostflag="$hostFlag 
hostName=$(hostname)
echo "hostname="$hostName 
taskId=${2}
echo "task id="$taskId 
stepId=${3}
echo $"step id"=stepId 

INSTNAME1=`ps -ef|grep pmon|grep -v grep|grep oracle|grep -v ASM|awk '{print $8}'|cut -b 10-`
DBNAME=${INSTNAME1%?}

num=`ps -ef|grep pmon|grep -v grep|grep oracle|grep -v ASM|wc -l`

if [ $num = 0 ];
then
 sh /root/return/task/commScript/callback/callLogManager.sh "info" "${taskId}" "${stepId}"  "INSTANCE ${INSTNAME1} STARTING." 
 sh /root/return/task/commScript/callback/callTaskManager.sh 1 "${taskId}" "${stepId}" 10 "INSTANCE ${INSTNAME1} STARTING." 

#su - grid -c "srvctl start instance -d ${DBNAME} -i ${INSTNAME1}" 1>./suc.log 2>./err.log
 export ORACLE_SID=${INSTNAME1}
 su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
startup;
eof
 sh /root/return/task/commScript/callback/callLogManager.sh "info" "${taskId}" "${stepId}"   "INSTANCE ${INSTNAME1} STARTED SUCCESS." 
 sh /root/return/task/commScript/callback/callTaskManager.sh  2 "${taskId}" "${stepId}" 100  "INSTANCE ${INSTNAME1} STARTED SUCCESS." 
 exit 0
else
 sh /root/return/task/commScript/callback/callLogManager.sh "error" "${taskId}" "${stepId}"  'instance is already runing ,please check.' 
 sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 10   'instance is already runing ,please check.' 
 exit 1
fi