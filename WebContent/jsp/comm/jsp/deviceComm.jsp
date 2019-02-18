<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/layui/layui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/deviceComm.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<style>
	input[type='text']{
	    width: 122px !important;
	    height: 20px !important;
	}
</style>
<title>设备</title>
</head>
<body id="deviceComm" style="">
	<div class="srch_con">
		<div class="prom-3" style="margin-left: 0px;width: 100%;margin-top: 0px;">
			<div class="w_p30">
				<span class="txt">主机名:</span> <input type="text" id="name_searchDev">
			</div>
			<div class="w_p30">
				<span class="txt">应用IP:</span> <input type="text" id="ip_searchDev">
			</div>
			<div class="w_p30">
				<button id="deviceSrch" class="btn">
					<i class="icon-search"></i>查询
				</button>
			</div>
		</div>
	</div>
	<div id="" style="">
		<table id="jqGridListDev"></table>
		<div id="jqGridPaperListDev"></div>
	</div>
</body>
<script type="text/javascript">
 var type;
 var rowData;
 var indexType;
 var alarmSelects=[];
  $(function(){
	  var params = parent.callbackShowParmas;
	  $("#div").hide();
	  //点击关闭，隐藏表单弹窗
	  $("#closeDev").click(function() {
		  parent.layer.close(parent.layer.index);
	  });
	  if(params && typeof(params) != "undefined"){
		  type = params.type;
		  indexType = params.indexType;
		  alarmSelects = params.alarmSelects.split(",");
	  }else{
		  //判断从哪个页面点进来
		  type = $.getUrlParam('type');
		  indexType = $.getUrlParam('indexType');
		  // 是否告警页面跳转过来的
		  if($.getUrlParam('alarmSelects') != null && typeof($.getUrlParam('alarmSelects')) != "undefined" && $.getUrlParam('alarmSelects') != ''){
		 	 alarmSelects = $.getUrlParam('alarmSelects').split(",");
		  }
	  }
	  if(type=="warnlog"){
		  $("#div").show();
		  if(indexType != null && typeof(indexType) != "undefined" && indexType != ""){
			  $("#sysLi a").css("opacity","0").attr("onclick", "").removeAttr("href");
		  }
	  }
	  //add by huangdaping,来自设备监控管理
	  if(type=="devmoni"){
		  $("#div").show();
		  $("#intLi").hide();
	  }
	  //创建jqgrid数据表格
	  initDevfunction();
  });
</script>
</html>
