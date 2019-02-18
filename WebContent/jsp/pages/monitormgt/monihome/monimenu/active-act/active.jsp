<!-- <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> -->
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/css/monitorHead.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/active-act/css/active.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/active-act/js/active.js"></script>
<style>
	body{
		background:-webkit-gradient(linear, 0% 100%, 0% 0%,from(#3c8dbc), to(#f6f6f8));
		background: -ms-linear-gradient(top,#f6f6f8 0%,#3c8dbc 100%); /*IE*/
	}
	.ui-widget-header{
		background: #7ab8dd;
	}
</style>
<title>双活</title>
</head>
<body style="position:relative;">
	<div class="contain">
		<div class="search">
			<ol class="breadcrumb">
				  <li><a href="javasctipt:void(0);" onclick="goMonBack();">监控主页</a></li>
				  <li class="active">双活</li>
				  <li><a href="javasctipt:void(0);" onclick="goMonBack();">返回</a></li>
			</ol>
		</div>
		<blockquote class="layui-elem-quote layui-quote-nm">
			<div class="hint">您好，欢迎访问双活监控页面，共 <span class="total"></span> 个双活，其中故障双活数为：<span class="error" style="color:red"></span></div>
			<div class="search-txt">
				<div class="txt">
					<input id="kw" type="text" placeholder="请输入双活名称" style="height: 23px;"/>
				</div>
				<button id="search-btn" class="btn"><i class="icon-search"></i> 查询</button>
			</div>
		</blockquote>
		<div class="servshow">
			<div class="servinfo" id="servinfo">
			</div>
		</div>
	</div>
</body>
</html>