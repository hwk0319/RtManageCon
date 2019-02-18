<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- jquery插件包-必要 -->
<!--这个是所有jquery插件的基础，首先第一个引入-->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<!-- jqGrid组件基础样式包-必要 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<!-- jqGrid插件包-必要 -->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<!--jqGrid主题包-非必要  -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui-1.8.16.custom.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>

<!-- jqGrid插件的多语言包-非必要 -->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/tpl/creatNewParamTPL.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增参数</title>
</head>
<body>
	<div style="margin:1%;">
		<table id="jqGridTaskParamTemplate"></table>
	   	<div id="jqGridTaskParamTemplatePager"></div>
	</div>
</body>
</html>