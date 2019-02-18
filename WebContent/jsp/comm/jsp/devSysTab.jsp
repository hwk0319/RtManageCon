<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/layui/css/layui.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/layui/layui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<title>设备Tab</title>
</head>
<body id="deviceComm" style="margin-top:0px">
	<span class="layui-layer-setwin" style="z-index: 9999;">
		<a class="layui-layer-ico layui-layer-close layui-layer-close1"
		href="javascript:void(0);" onClick="parent.layer.close(parent.layer.index);"></a>
	</span>
	<div class="layui-tab" lay-filter="devsystab">
		<ul class="layui-tab-title">
			<li class="layui-this" id="liDev">设备</li>
			<li id="liSys">软件系统</li>
		</ul>
		<div class="layui-tab-content">
			<div class="layui-tab-item layui-show" id="deviceDiv" style="margin-top: -10px;margin-left: 1%;margin-right: 1%;"></div>
			<div class="layui-tab-item" id="systemDiv" style="margin-top: -10px;margin-left: 1%;margin-right: 1%;"></div>
		</div>
	</div>
	<input type="hidden" id="tabIndex" value=""/>
</body>
<script type="text/javascript">
	//注意：选项卡 依赖 element 模块，否则无法进行功能性操作
	layui.use('element', function(){
		$("#deviceDiv").load(getContextPath() + "/jsp/comm/jsp/deviceComm.jsp");
 	    $("#systemDiv").load(getContextPath() + "/jsp/comm/jsp/SystemComm.jsp");
	});
	
	$(function(){
		var uid = $("#devuid",parent.document).val();
		if(uid != ""){
			var uidd = uid.split(",")[0].substr(0, 1);
			if(uidd=='2'){
				$("#liDev").removeClass("layui-this");
				$("#liSys").addClass("layui-this");
				$("#deviceDiv").removeClass("layui-tab-item layui-show");
				$("#deviceDiv").addClass("layui-tab-item");
				$("#systemDiv").removeClass("layui-tab-item");
				$("#systemDiv").addClass("layui-tab-item layui-show");
			}
		}
		//tab点击时重新加载页面
		$("#liDev").click(function(){
			$("#tabIndex").val("1");
			$("#deviceDiv").empty();
			$("#deviceDiv").load(getContextPath() + "/jsp/comm/jsp/deviceComm.jsp");
		});
		$("#liSys").click(function(){
			$("#tabIndex").val("2");
			$("#systemDiv").empty();
	 	    $("#systemDiv").load(getContextPath() + "/jsp/comm/jsp/SystemComm.jsp");
		});
	});
	
</script>
</html>
