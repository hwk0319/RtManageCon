<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/css/monitorHead.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/server/css/serverParticulars.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<style>
	.goBack{
		width: 98%;
		border-bottom: 1px solid #ddd;
		margin: 1%;
	}
	.layui-layer-dialog .layui-layer-content .layui-layer-ico {
	    top: 16px;
	    left: 15px;
	    width: 50px;
	    height: 50px;
	}
	.layui-layer-dialog .layui-layer-content {
	    position: relative;
	    line-height: 24px;
	    word-break: break-all;
	    overflow: hidden;
	    font-size: 18px;
	    overflow-x: hidden;
	    margin-left: 25px;
	}
	.mainsoft{
		margin-bottom: 10px;
	}
</style>
<title>服务器监控详情</title>
</head>
<body>
<div class="conn">
	<div class="goBack">
		<ol class="breadcrumb">
		  <li><a href="javasctipt:void(0);" onclick="goMonBack();">监控主页</a></li>
		  <li><a href="javasctipt:void(0);" onclick="goServers();">服务器</a></li>
		  <li class="active">详情</li>
		  <li><a href="javasctipt:void(0);" onclick="goBack();">返回</a></li>
		</ol>
	</div>
	<div class="info">
		<div class="state">
			<div class="icon">
				<div class="oncover" style="cursor:pointer;"></div>
				<div class="stateinfo"></div>
				<div class="equipment" >
					<div class="baseinfo">
						<label id="db_server_name"></label><br>
						<label id="db_ip"></label>
					</div>
				</div>
			</div>
			<div class="emergency" >
				<div class="title" style="position:relative;" >
						<div class="titleinfo">
							<img alt="" src="${pageContext.request.contextPath}/imgs/monitor/gjxx.png" >
						</div>
						<div class="titleinfo">
							&nbsp;基础信息
						</div>
				</div>
				<div class="warninfo">
					<div class="temp">
						<div class="cputemp">
							<div class="warntitle">
								<span class="wt_line"></span>
								<span class="wt_txt">CPU温度(℃)</span>
							</div>
							<div class="tempvalue"></div>
						</div>
						<div class="powktemp">
							<div class="warntitle cons">
								<span class="wt_line"></span>
								<span class="wt_txt">电源功耗(kW)</span>
							</div>
							<div class="consvalue"></div>
						</div>
					</div>
					<div class="temp">
						<div class="voltemp">
							<div class="warntitle">
								<span class="wt_line"></span>
								<span class="wt_txt">电源风扇状态</span>
							</div>
							<div class="volevalue"></div>
						</div>
						<div class="boxtemp">
							<div class="warntitle">
								<span class="wt_line"></span>
								<span class="wt_txt">机箱温度(℃)</span>
							</div>
							<div class="boxvalue"></div>
						</div>
					</div>
					<div class="temp">
						<div class="powstemp">
							<div class="warntitle">
								<span class="wt_line"></span>
								<span class="wt_txt">电源状态</span>
							</div>
							<div class="powervalue"></div>
						</div>
						<div class="fantemp">
							<div class="warntitle">
								<span class="wt_line"></span>
								<span class="wt_txt">风扇转速(RPM)</span>
							</div>
							<div class="fanvalue"></div>
						</div>
					</div>
			</div>
		</div>
	</div>
	</div>
	<div class="chart">
		<div class="title">
			<div class="titleinfo">
				<img alt="" src="${pageContext.request.contextPath}/imgs/monitor/xnjk.png" >
			</div>
			<div class="titleinfo">
				&nbsp;性能监控
			</div>
		</div>
		<div id="myChart" style="height:calc(100% - 30px); width: 100%;"></div>
	</div>
	<div class="mainsoft">
		<div class="title">
			<div class="titleinfo">
				<img alt="" src="${pageContext.request.contextPath}/imgs/monitor/database.png" >
			</div>
			<div class="titleinfo">
			&nbsp;软件信息
			</div>
		</div>
		<div class="soft"></div>
	</div>
	<br/>
	<br/>
	<div style="height:20px"></div>	
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/server/js/serverParticulars.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/echarts-all.js"></script>
<script>
/**
 * 定时刷新基本信息
 */
var chartInterval = setInterval(function(){
	loadDetailInfo();
},10000);
window.top.servertime.push(chartInterval);
function goBack(){
	//关闭服务器定时器
	var svtimes = window.top.servertime;
	for(var i=0;i<svtimes.length;i++){
		clearInterval(svtimes[i]);
	}
	var returnType = $("#returnType").val();//返回类型
	var path = "";
	if(returnType == "servers"){
		$("#returnType").val("");
		path = getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/server.jsp";
	}else if(returnType == "index"){
		$("#returnType").val("");
		path = getContextPath()+ "/jsp/pages/monitormgt/monihome/monimenu/double/double.jsp";
	}else if(returnType == "group"){
		path = getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/server.jsp";
	}else if(returnType == "database"){
		path = getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/dbMinuteParam.jsp";
	}
	$("#tt").load(path);
}
function goServers(){
	//关闭服务器定时器
	var svtimes = window.top.servertime;
	for(var i=0;i<svtimes.length;i++){
		clearInterval(svtimes[i]);
	}
	var path=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/server.jsp";
	$("#tt").load(path);
}
</script>
</html>