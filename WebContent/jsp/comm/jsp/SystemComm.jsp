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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui-1.8.16.custom.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/SystemComm.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<style>
	input[type='text']{
	    width: 122px !important;
	    height: 20px !important;
	}
</style>
<title>软件系统</title>
</head>
<body id="systemComm">
		<div class="srch_con">
			<div class="prom-3" style="margin-left: 0px;width: 100%;margin-top: 0px;">
				<div class="w_p30">
					<span class="txt">名称:</span> <input type="text" id="name_searchSys">
				</div>
				<div class="w_p30">
					<span class="txt">应用IP:</span> <input type="text" id="ip_searchSys">
				</div>
				<div class="w_p30">
					<button id="sysSrch" class="btn">
						<i class="icon-search"></i>查询
					</button>
				</div>
			</div>
		</div>
		<div id="main_list" style="">
			<table id="jqGridListSys"></table>
			<div id="jqGridPaperListSys"></div>
		</div>
</body>
<script type="text/javascript">
var indexType;
var alarmSelects=[];
  $(function(){
	  var params = parent.callbackShowParmas;
	  if(params && typeof(params) != "undefined"){
		  type = params.type;
		  indexType = params.indexType;
		  alarmSelects = params.alarmSelects.split(",");
	  }else{
		  // 是否告警页面跳转过来的
		  if($.getUrlParam('alarmSelects') != null && typeof($.getUrlParam('alarmSelects')) != "undefined" && $.getUrlParam('alarmSelects') != ''){
		 	 alarmSelects = $.getUrlParam('alarmSelects').split(",");
		  }
		  indexType = $.getUrlParam('indexType');
	  }
	  if(type=="warnlog"){
		  $("#div").show();
		  if(indexType != null && typeof(indexType) != "undefined" && indexType != ""){
			  $("#devLi").remove();
			  $("#devLi a").css("opacity","0").attr("onclick", "").removeAttr("href");
		  }
	  }
	//add by huangdaping,来自设备监控管理
	  if(type=="devmoni"){
		  $("#div").show();
		  $("#intLi").hide();
	  }
	  //点击关闭，隐藏表单弹窗
	  $("#closeSys").click(function() {
		  parent.layer.close(parent.layer.index);
	  });
	  
	 if($.getUrlParam('ishiddenHeader')+"" == "true"){
		 $("#db").css("display","none");
		 $("#systemComm").css({"height":"95%","margin-top":"15px","margin-left":"15px"})
	 }
	initSysfunction();
  });
</script>
</html>
