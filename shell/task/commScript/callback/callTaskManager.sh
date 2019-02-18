#!/bin/bash
if  [ $# -ne 5 ] ; then
    echo "Usage:"
    echo "Error params"
    echo "'$1' msgType"
    echo "'$2' taskId"
    echo "'$3' stepId"
    echo "'$4' percent"
    echo "'$5' msg"    
    exit 1
fi
echo "$*" >> temp.log
timestamp=$(date +'%Y-%m-%d %H:%M:%S')
hostFlag=$(hostname)
msgType=$1
taskId=$2
stepId=$3
percent=$4
msg=$5
basePath=$(cd "$(dirname "$0")"; pwd)
source ${basePath}/hostConfig.ini
soapaction="http://soap.taskmanager.nari.com/taskSoapService/taskRunResult"
hosturl=http://${hostServerAddr}/RtManageCon/services/TaskService?wsdl
res=`curl -H 'Content-Type: text/xml' -H 'charset=utf-8' -H 'SOAPAction: "${soapaction}"' -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.taskmanager.nari.com" xmlns:xsd="http://po.taskmanager.nari.com/xsd"><soapenv:Header/><soapenv:Body><soap:taskRunResult><soap:result><xsd:msg>'"${msg}"'</xsd:msg><xsd:hostFlag>'${hostFlag}'</xsd:hostFlag><xsd:msgType>'${msgType}'</xsd:msgType><xsd:percent>'${percent}'</xsd:percent><xsd:stepId>'${stepId}'</xsd:stepId><xsd:taskId>'${taskId}'</xsd:taskId><xsd:timestamp>'"${timestamp}"'</xsd:timestamp></soap:result></soap:taskRunResult></soapenv:Body></soapenv:Envelope>' ${hosturl}`
times=0
while true
	do
    if [[ -z `echo $res | grep '<ns:return>OK</ns:return>'` ]]
    then
     
     if [ $times -gt 4 ]
      then
      echo "[error] can not send msg to manage host ${hostServerAddr}"
      exit 1
     fi
     echo "[error] cannot get success return msg.  reuqest again!"
     sleep 100
     res=`curl -H'Content-Type: text/xml;charset=utf-8;SOAPAction: "'${soapaction}'"' -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.taskmanager.nari.com" xmlns:xsd="http://po.taskmanager.nari.com/xsd"><soapenv:Header/><soapenv:Body><soap:taskRunResult><soap:result><xsd:msg>'"${msg}"'</xsd:msg><xsd:hostFlag>'${hostFlag}'</xsd:hostFlag><xsd:msgType>'${msgType}'</xsd:msgType><xsd:percent>'${percent}'</xsd:percent><xsd:stepId>'${stepId}'</xsd:stepId><xsd:taskId>'${taskId}'</xsd:taskId><xsd:timestamp>'"${timestamp}"'</xsd:timestamp></soap:result></soap:taskRunResult></soapenv:Body></soapenv:Envelope>' ${hosturl}`
     times=$(($times+1))
    else
     echo "[info] get response OK"
   exit 0
    fi
done
