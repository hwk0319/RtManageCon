<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/noticerule/js/noticerule.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
	<style>
		.ui-jqgrid .ui-jqgrid-caption{
			background-color: #7ab8dd;
		}
	</style>
    <title>通知规则</title>
  </head>
  <body>
  	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">告警类型：</span>
					<select style="width:70%;font-size:12px;" id="type_search">
					</select>
				</div>
				<div class="w_p30">
					<span class="txt">告警等级：</span>
					<select style="width:70%;font-size:12px;" id="level_search">
					</select>
				</div>
				<div class="w_p30">
					<button id="notrSrch" class="btn"><i class="icon-search"></i> 查询</button>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListNotr"></table>
		<div id="jqGridPaperListNotr"></div>
	</div>
</body>
<script type="text/javascript">
  $(function(){
	initNotrfunction();
  });
</script>
</html>
