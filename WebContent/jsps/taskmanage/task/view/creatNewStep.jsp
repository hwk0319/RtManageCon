<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsps/taskmanage/js/myProgress.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/jquery.myProgress.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/task/view/creatNewStep.js"></script>
<title>步骤详情</title>
</head>
<style>
	body,html{
		width:100%;
		height:100%;
		margin:0;
		padding:0;
	}
	.ui-jqgrid .ui-jqgrid-caption{
		background-color: #7ab8dd;
	}
	.ui-widget-header {
	    background: #7ab8dd;
	    color: black;
	}
</style>
<body>
<div style="margin:1%;">
	<table  id="jqGridTaskStep"></table>
	<div  id="jqGridTaskStepPager"></div>
</div>
</body>
</html>