<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="logoinfo" style="margin:1%;height:90%;border:1px solid #ddd;overflow:auto;">
	<p></p>
</div>
</body>
<script>
var set;
	$(function(){
		taskId = $("#shareTaskId",parent.document).val();
	  	refreshLog();
		clearInterval(set);
		set = setInterval("refreshLog()","12000");
	});
	function refreshLog()
	{
	  $.ajax({
	    type : 'POST',
	    url : getContextPath() + "/taskmanage/getTaskLogByTaskId",
	    dataType : 'json',
	    data : {
	      "taskId" : taskId
	    },
	    success : function(data) {
	      if("success"==data.status)
	        {
	      		taskLog = data.value;
	      		buildLogHtml(taskLog);
	        }
	      
	    },
	    error : function(text) {
	      alert("error:"+text);
	    }
	  });
	  
	}

  function buildLogHtml(logObj) {
    var logHtml="";
    for ( var index in logObj) {
      var log = logObj[index];
      var hostIp = log.hostIp;
      var logLevel = log.logLevel;
      if (logLevel == "ERROR" || logLevel == "FATAL" || logLevel == "error" || logLevel == "fatal" ) {
        logLevel = "<font style=\"color:red\">"+logLevel+"</font>";
      }
      var time=log.logTime;
      var taskId = log.taskId;
      var stepId = log.stepId;
      var detial = log.logDetial;
      logHtml = logHtml +time+" "+ hostIp+" "+ logLevel +" "+taskId+" "+" "+stepId+" " +detial +"<br>";
    }
	$("p").html(logHtml);
  }
</script>
</html>