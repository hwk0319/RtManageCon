<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsps/taskmanage/js/myProgress.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui-1.8.16.custom.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/layui/css/layui.css" media="all">
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/ajaxfileupload.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script> 
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsLib/layui/layui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/TaskOperation.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/jquery.myProgress.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<title>新任务模板</title>
<style type="text/css">
  .h_auto{
  	overflow:auto;
  }
  .layui-tab-content,#taskPageDiv1,#taskPageDiv2,#taskPageDiv3,#taskPageDiv4{
  	height:100%;
  	width: 100%;
  }
  body,html{
  	overflow:hidden;
  	height:100%;
  	width: 100%;
  }
  #movehand{
  	height:100%;
  	width: 100%;
  }
  .layui-tab-content{
  	height:85%;
  	width: 100%;
  }
</style>
</head>
<body>
	<div id="movehand"  class="layui-tab"  lay-filter="demo" >
		<ul class="layui-tab-title ">
			<li lay-id="creatModul" class="layui-this">创建任务</li>
			<li lay-id="creatStep">步骤</li>
			<li>参数</li>
			<li lay-id="runlog">执行日志</li>
		</ul>
		<div class=" layui-tab-content "  >
	 		<div id='taskPageDiv1' class="layui-tab-item h_auto layui-show"> </div>
   			<div id='taskPageDiv2' class="layui-tab-item h_auto"> </div>
   			<div id='taskPageDiv3' class="layui-tab-item h_auto" ></div>
   			<div id='taskPageDiv4' class="layui-tab-item h_auto"> </div>
		</div>
	</div>
<script type="text/javascript">
var taskId;
var hasConfirm = false;//是否点击过下一步按钮
$(function(){
  	layui.use(['layer', 'laypage', 'element'], function() {
	    layer = layui.layer, laypage = layui.laypage, element = layui.element();
	    if(parent.oper=="add")
	    {
	      	element.tabDelete('demo', 'runlog');
	      	$("#taskPageDiv1").load(getContextPath() + "/jsps/taskmanage/task/createTask.jsp");
	    }
	  	else if(parent.oper=="view")
	    {
	   	 	element.tabDelete('demo', 'creatModul');
	   	 	taskId=parent.taskId;
	   	 	$("#taskPageDiv2").load(getContextPath() + "/jsps/taskmanage/task/view/creatNewStep.jsp");
	   		$("#taskPageDiv3").load(getContextPath() + "/jsps/taskmanage/task/creatNewParam.jsp");
	   		$("#taskPageDiv4").load(getContextPath() + "/jsps/taskmanage/task/runLogpage.jsp");
	    }
	  	else if(parent.oper=="clone")
	  	{
	   	 	element.tabDelete('demo', 'creatModul');
	   	 	element.tabDelete('demo', 'runlog');
	   	 	taskId=parent.taskId;
	   	 	$("#taskPageDiv2").load(getContextPath() + "/jsps/taskmanage/task/view/creatNewStep.jsp");
	   		$("#taskPageDiv3").load(getContextPath() + "/jsps/taskmanage/task/creatNewParam.jsp");
	  	};
  });
});
</script>
</body>
</html>  