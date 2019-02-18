<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/plugins/jquery/css/jquery-ui-redmod.css">
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/healthScore/js/healthmodelConn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<style>
	.w_p30 {
		width:40%;
	}
</style>
<title>软件系统管理</title>
</head>
<body id="modelComm">
	    <div class="srch_con" style="margin-top: 0px;">
			<div class="prom-3" style="margin-left: 0px;width: 100%;margin-top: 0px;">
				<div class="w_p30">
					<span class="txt">模型名称:</span><input type="text" id="model_name"/>
				</div>
				<div class="w_p30">
					<button id="srch" class="btn" onclick="doSearchMod()">
						<i class="icon-search"></i>查询
					</button>
				</div>
			</div>
		</div>
		<div id="main_list">
			<table id="jqGridListModel"></table>
			<div id="jqGridPaperListModel"></div>
		</div>

	<script type="text/javascript">
  $(function(){
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.close(parent.layer.index);
	  });
	  ModelFunction();
  });
</script>
</body>
</html>