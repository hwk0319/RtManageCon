#!/bin/bash
if  [ $# -ne 4 ] ; then
	    echo "Usage:"
    	echo "Error params"
      echo "1--level"
      echo "2--taskId"
      echo "3--stepId"
      echo "4--msgDetial"
    exit 1
fi
basePath=$(cd "$(dirname "$0")"; pwd)
echo "$*" >> temp.log
echo "basePath: ${basePath} " >> temp.log
timestamp=$(date +'%Y-%m-%d %H:%M:%S')
hostFlag=$(hostname)
hostName=$(hostname)
level=$1
taskId=$2
stepId=$3
msgDetial=$4
source ${basePath}/hostConfig.ini
soapaction="http://soap.taskmanager.nari.com/taskSoapService/saveTaskLog"
hosturl=http://${hostServerAddr}/RtManageCon/services/TaskService?wsdl
curl -H'Content-Type: text/xml' -H 'charset=utf-8' -H 'SOAPAction: "${soapaction}"' -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.taskmanager.nari.com" xmlns:xsd="http://po.taskmanager.nari.com/xsd"><soapenv:Header/><soapenv:Body><soap:saveTaskLog><soap:log><xsd:logDetial>'"${msgDetial}"'</xsd:logDetial><xsd:hostName>'${hostName}'</xsd:hostName><xsd:logLevel>'"${level}"'</xsd:logLevel><xsd:hostFlag>'${hostFlag}'</xsd:hostFlag><xsd:stepId>'${stepId}'</xsd:stepId><xsd:taskId>'${taskId}'</xsd:taskId><xsd:logTime>'"${timestamp}"'</xsd:logTime></soap:log></soap:saveTaskLog></soapenv:Body></soapenv:Envelope>' ${hosturl}
exit 0
