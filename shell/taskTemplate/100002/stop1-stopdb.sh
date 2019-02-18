#!/bin/bash
#this script stop database instance
#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/stop1-stopdb_${logFileName}.log
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
echo $timestamp >  temp.log

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

if [ $num = 1 ];
then
	sh /root/return/task/commScript/callback/callLogManager.sh "$info" "${taskId}" "${stepId}" "INSTANCE ${INSTNAME1} STOPING." 
  sh /root/return/task/commScript/callback/callTaskManager.sh 1 "${taskId}" "${stepId}" 50 "INSTANCE ${INSTNAME1} STOPING." 
	
  #su - grid -c "srvctl stop instance -d ${DBNAME} -i ${INSTNAME1}"
  export ORACLE_SID=${INSTNAME1}
su - oracle -c "sqlplus -S / as sysdba" <<eof
set feedback off heading off pagesize 0;
shutdown immediate;
eof
  
  sh /root/return/task/commScript/callback/callLogManager.sh"$info" "${taskId}" "${stepId}"  "INSTANCE ${INSTNAME1} STOPED SUCCESS." 
  sh /root/return/task/commScript/callback/callTaskManager.sh 2 "${taskId}" "${stepId}" 100 "INSTANCE ${INSTNAME1} STOPED SUCCESS." 
	
elif [ $num = 0 ];
 then
	sh /root/return/task/commScript/callback/callLogManager.sh "info" "${taskId}" "${stepId}"  'no instance is runing,please check.' 
	sh /root/return/task/commScript/callback/callTaskManager.sh 3 "${taskId}" "${stepId}" 10 'no instance is runing,please check.' 
	
 exit 1
else
	sh /root/return/task/commScript/callback/callLogManager.sh  "error" "${taskId}" "${stepId}" 'more than 1 instance is runing ,please check.' 
	sh /root/return/task/commScript/callback/callTaskManager.sh 3 "${taskId}" "${stepId}" 10 'more than 1 instance is runing ,please check.' 
	exit 1
fi




