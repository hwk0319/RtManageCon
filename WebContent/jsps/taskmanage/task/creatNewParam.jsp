<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/task/creatNewParam.js"></script>
<style>
*::-webkit-scrollbar {
    width: 5px;
    background-color: #F5F5F5;
}
*::-webkit-scrollbar-thumb {
    background-color: #c1c1c1;
}
</style>
<title>参数</title>
</head>
<body>
<div class="" style="margin:1%;">
	<table id="jqGridTaskParam"></table>
	<div id="jqGridTaskParamPager"></div>
</div>
</body>
</html>