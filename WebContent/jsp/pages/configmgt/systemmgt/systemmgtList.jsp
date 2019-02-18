<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
response.setHeader("Pragma","No-cache");    
response.setHeader("Cache-Control","no-cache");    
response.setDateHeader("Expires", -10);   
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<meta http-equiv="expires" content="0">  
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/systemmgt/js/systemmgtList.js"></script>
<!-- 设备管理 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/device/js/device.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<title>软件系统管理</title>
<style>
.ui-jqgrid .ui-jqgrid-caption{
	background-color: #7ab8dd;
}
</style>
</head>
<body>
	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">主机名：</span> <input type="text" id="name_search">
				</div>
				<div class="w_p30">
					<span class="txt">应用IP：</span> <input type="text" id="ip_search">
				</div>
				<div class="w_p30">
					<button id="srch" class="btn">
						<i class="icon-search"></i> 查询
					</button>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListSys"></table>
		<div id="jqGridPaperListSys"></div>
	</div>
	<div id="tabs" style="display:none;width:98%;margin:1%;padding:0;border-radius:5px">
		  <ul>
			    <li><a href="#tabs-1">设备管理</a></li>
		  </ul>
		  <div id="tabs-1" class="tabCon">
			  	<!-- 设备管理-start -->
				<div id="tip"></div>
			 	<iframe id="HideFrm" name="HideFrm" style="display: none"></iframe> 
				 	<table id="jqGridListDev"></table>
					<div id="jqGridPaperListDev"></div>
		  </div>
   </div>
<script type="text/javascript">
  $(function(){
	initSysfunction();
  });
</script>
</body>
</html>
