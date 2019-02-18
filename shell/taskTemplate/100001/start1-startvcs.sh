#!/bin/bash
#this script start local vcs
#===========================log=========================
dirName=$(cd "$(dirname "$0")"; pwd)
logFileName=$(date +'%Y-%m-%d-%H-%M-%S')
logFile=${dirName}/start1-startvcs_${logFileName}.log
exec 3>&1 4>&2 &> ${logFile} 2>&1
#===========================log=========================

if [ $(id -u) != "0" ]; then
    echo "You must be the superuser to run this script" >&2
	sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 0 "you must be root user to run this task." 
    exit 1
fi

GETLOG(){
errmsg=`cat ./err.log`
sucmsg=`cat ./suc.log`
}

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
echo $"step id"=stepId 

INSTNAME1=`ps -ef|grep pmon|grep -v grep|grep oracle|grep -v ASM|awk '{print $8}'|cut -b 10-`
DBNAME=${INSTNAME1%?}


num=`ps -ef|grep pmon|grep -v grep|grep oracle|grep -v ASM|wc -l`

if [ $num = 0 ];
then
	sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "NODE $(hostName) VCS STARTING." 
	sh /root/return/task/commScript/callback/callTaskManager.sh   1 "${taskId}" "${stepId}" 10 "NODE $(hostName) VCS STARTING." 
	hastart 1>./suc.log 2>./err.log
 if [ $? = 0 ]
 then
 	sh /root/return/task/commScript/callback/callLogManager.sh  "info" "${taskId}" "${stepId}"  "NODE $(hostName) VCS STARTED SUCCESS."
 	sh /root/return/task/commScript/callback/callTaskManager.sh  2 "${taskId}" "${stepId}" 100 "NODE $(hostName) VCS STARTED SUCCESS." 
 NODE $(hostName) VCS STARTING.
 else
 GETLOG
 msgDtial=$errmsg
 sh /root/return/task/commScript/callback/callLogManager.sh "error" "${taskId}" "${stepId}"  "${msgDtial}" >> temp.log
 sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 100 "NODE $(hostName) VCS STARTED FAILED." 

 fi
else 
	sh /root/return/task/commScript/callback/callLogManager.sh   "error" "${taskId}" "${stepId}"  'database is already running'
	sh /root/return/task/commScript/callback/callTaskManager.sh  3 "${taskId}" "${stepId}" 10 'database is already running' 
exit 1
fi
