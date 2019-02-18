<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/warnlog/js/warnlog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/My97DatePicker/WdatePicker.js"></script>
<style>
	.ui-jqgrid .ui-jqgrid-caption{
		background-color: #7ab8dd;
	}
	.w_p40 select{
		height: 23px;
	    width: 65%;
	    font-size: 10px;
	    box-sizing: content-box;
	}
</style>
<title>异常故障</title>
</head>
<body>
	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom" style="float:left;z-index: 9999;">
				<div class="w_p40">
					<span class="txt"> 设备名称：</span>
					<input type="text" id="device_name_srch" >
				</div>
				<div class="w_p40">
					<span class="txt">处理状态：</span>
					<select id="process_status_srch">
					  <option value="">--请选择--</option>
					  <option value="0">新报</option>
					  <option value="1">已确认</option>
					  <option value="2">已消除</option>
					  <option value="3">已忽略</option>
					</select> 
				</div>
			</div class="prom">
			<div>
				<button onclick="doSearchWarnlog()" class="btn"><i class="icon-search"></i> 查询</button>
			</div>
			<div class="prom" style="margin-top:10px;">
				<div class="w_p40">
					<span class="txt">发生日期：</span>
					<input id="startTime" class="Wdate" type="text" readonly="readonly" onClick="">
				</div>
				<div class="w_p40">
					<span class="txt">结束日期：</span>
					<input id="endTime" class="Wdate" type="text" readonly="readonly" onClick="">
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListWarnlog"></table>
		<div id="jqGridPaperListWarnlog"></div>
	</div>
</body>
<script type="text/javascript">
  $(function(){
	 initWarnlogfunction();
	 
	 $("#startTime").click(function(){
		 var startTime = $("#startTime").val();
		 var endTime = $("#endTime").val();
		  if(startTime != "" && endTime != ""){
		 	  var d1 = new Date(startTime.replace(/\-/g, "\/"));  
		 	  var d2 = new Date(endTime.replace(/\-/g, "\/"));  
		 	   if(startTime!="" && endTime!="" && d1 >=d2)  
		 	  {  
		 	   layer.alert("开始时间不能大于结束时间！");  
		 	   $("#endTime").val("");
		 	  }
		  }else{
			  WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:'true',maxDate:'%y-%M-%d'});
		  }
	 });
	 
	 $("#endTime").click(function(){
		  var startTime = $("#startTime").val();
		  if(startTime == ""){
			  layer.msg("请先选择发生日期！");
		  }else{
			  WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:'true',minDate:'#F{$dp.$D(\'startTime\')}',maxDate:'%y-%M-%d'});
		  }
	 });
  });
</script>
</html>
