#!/bin/bash

#调用远程主机的脚本
#参数
#	$1 	remoteIp 		远程主机的IP
#	$2	shellPath 	远程主机的脚本路径
# 	$3	shellParam 	远程主机的参数路径
function callRemoteScript()
	{
		localIp=${1}
		remoteIp=${2}
		remotePasswd=${3}
		shellPath=${4}
		shellParam=${5}
		taskId=${6}
		stepId=${7}
		basePath=$(cd "$(dirname "$0")"; pwd)
		yum -y install expect.x86_64
		sh ${basePath}/setupssh.sh root ${remotePasswd} "${localip} ${remoteIp}"
		if [ $? != 0 ]; then
       	echo "[error]  fail to run setupssh.sh detial see log  returnTask.log"
       	sh ${basePath}/callback/callLogManager.sh  "error" "${taskId}" "$stepId"  "fail to run setupssh.sh detial see log  returnTask.log."
       	sh ${basePath}/callback/callTaskManager.sh  3 "${taskId}" "$stepId" 0 "fail to run setupssh.sh, for detial see log returnTask.log"
      exit 1
    else
     		echo "[success]  build trust success."
		fi
		ssh ${remoteIp} "${shellPath} ${shellParam} $taskId $stepId 2>/dev/null 2>&1 &"
	exit 0
	}
	
function main()
	{
		callRemoteScript $@
	}
		
main $@
