#!/bin/bash
function main()
{ 
	initConfig $@
}

#设置双机信任
#在目标主机上创建文件夹,路径/root/return/task
#复制基础工具脚本到目标主机
#复制脚本到目标主机
#参数
#	$1 管理节点IP
# $2 目标节点IP
function initConfig()
{
	localip=${1}
	remoteIp=${2}
	remotePasswd=${3}
	taskPath=${4}
	remoteTaskPath=${5}
	taskId=${6}
	#exec 1>${taskPath}/returnTask.log
	#exec 2>${taskPath}/returnTask.log
	basePath=$(cd "$(dirname "$0")"; pwd)
	yum -y install expect.x86_64
	sh ${basePath}/setupssh.sh root ${remotePasswd} "${localip} ${remoteIp}"
	if [ $? != 0 ]; then
	echo "[error]  fail to run setupssh.sh"
	sh -x ${basePath}/callback/callLogManager.sh  "error" "${taskId}" "0"  "fail to run setupssh.sh. check the host ip and password or has installed expect.x86_64"
	sh -x ${basePath}/callback/callTaskManager.sh  3 "${taskId}" "0" 0 "fail to run setupssh.sh. check the host ip and password or has installed expect.x86_64"
	exit 1
	else
		echo "[success]  build trust success."
	fi
	#在目标主机上创建文件夹
	ssh  $remoteIp "mkdir -p ${remoteTaskPath}"
	if [ $? != 0 ]; then
	echo "[error]  fail to exec \"ssh -t $remoteIp mkdir -p ${remoteTaskPath}\"."
	sh ${basePath}/callback/callLogManager.sh  "error" "${taskId}" "0"  "fail to run setupssh.sh ."
	sh ${basePath}/callback/callTaskManager.sh  3 "${taskId}" "0" 10 "fail to run setupssh.sh."
	fi
	#拷贝基础工具脚本到目标主机
	manageScriptPath=`dirname $taskPath`
	scp -r ${manageScriptPath}/commScript $remoteIp:$remoteTaskPath
	if [ $? != 0 ]; then
	echo "[error]  fail to exec \"scp -r ${manageScriptPath}/commScript $remoteIp:$remoteTaskPath\", detial see log  returnTask.log"
	sh ${basePath}/callback/callLogManager.sh  "error" "${taskId}" "0"  "fail to scp commScript to remote device, detial see log returnTask.log."
	sh ${basePath}/callback/callTaskManager.sh  3 "${taskId}" "0" 10 "fail to run scp commScript to remote device , for detial see log returnTask.log"
	fi
	#拷贝任务脚本到目标主机
	scp -r $taskPath $remoteIp:$remoteTaskPath
	if [ $? != 0 ]; then
	echo "[error]  fail to exec \"scp -r $taskPath $remoteIp:$remoteTaskPath\", detial see log  returnTask.log"
	sh ${basePath}/callback/callLogManager.sh  "error" "${taskId}" "0"  "fail to scp taskScript to remote device, detial see log returnTask.log."
	sh ${basePath}/callback/callTaskManager.sh  3 "${taskId}" "0" 10 "fail to run scp taskScript to remote device , for detial see log returnTask.log"
		exit 1
	fi
	echo "[info] change mod for shell."
	ssh  $remoteIp "chmod -R +x $remoteTaskPath"
	echo "[info] call staskmanager success."
	sh ${basePath}/callback/callLogManager.sh "info" "${taskId}" "0"  "dispatch task to ${remoteIp}  success."
	sh -x ${basePath}/callback/callTaskManager.sh  2 "${taskId}" "0" 10 "dispatch task to ${remoteIp}  success."
exit 0
}
main $@
