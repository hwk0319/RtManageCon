<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/task/add/creatNewStep.js"></script>
<title>步骤</title>
<style>
	html,body{
		width: 100%;
		height: 100%;
		padding: 0;
		margin: 0;
	}
	.ui-widget-header {
	    background: #7ab8dd;
	    color: black;
	}
</style>
</head>
<body>
	<div style="margin:1%">
		<table id="jqGridTaskStep"></table>
		<div id="jqGridTaskStepPager"></div>
	</div>
</body>
</html>