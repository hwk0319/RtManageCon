<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/device/js/device.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<style>
	.ui-jqgrid .ui-jqgrid-caption{
		background-color: #7ab8dd;
	}
	.w_p30 select{
		height: 23px;
	    width: 65%;
	    font-size: 10px;
	    box-sizing: content-box;
	}
</style>
<title>设备管理</title>
</head>
<body>
	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">类型：</span>
					<select id="devicetype_search">
						<option value="">--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<span class="txt">厂商：</span>
					<select id="factory_search">
						<option value="">--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<button id="deviceSrch" class="btn"><i class="icon-search"></i> 查询</button>
				</div>
			</div>
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">主机名：</span>
					<input type="text" id="name_search"/>
				</div>
				<div class="w_p30">
					<span class="txt">应用IP：</span>
					<input type="text" id="ip_search" >
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListDev"></table>
		<div id="jqGridPaperListDev"></div>
	</div>
	<div id="tabs" style="display:none;width:98%;margin:1%;padding:0;border-radius:5px">
		<ul>
			<li><a href="#tabs-1">设备管理</a></li>
		</ul>
		<div id="tabs-1">
			<!-- 设备-start -->
			<div id="tip"></div>
			<iframe id="HideFrm" name="HideFrm" style="display: none"></iframe>
			<table id="jqGridListGro"></table>
			<div id="jqGridPaperListGro"></div>
			<!-- 设备-end -->
		</div>
	</div>
</body>
<script type="text/javascript">
  $(function(){
	  initDevfunction();
	  //类型选择事件
	  $("#devicetype_search").change(function(){
		  var devicetype = $(this).val();
		  $("#factory_search [value != '']").remove();
		  initFactory(devicetype);
	  });
  });
  
  //初始化厂商
  function initFactory(tp){
		var type;
		if(tp == "1"){
			type = "server_factory";//服务器
		}else if(tp == "10"){
			type = "loadB_factory";//负载均衡
		}else{
			type = "";
		}
		//获取数据字典值并显示在下拉框
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data : {
					type : type
			},
			dataType : 'json',
			success : function(data) {
					for (var i = 0; i < data.length; i++) {
						$("#factory_search").append("<option  value='"+data[i].value+"'>"+ data[i].name + "</option>");
					}
			}
		});
  }
</script>
</html>
