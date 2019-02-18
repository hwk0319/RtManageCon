<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/css/monitorHead.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/database/js/database.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/echarts-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<style type="text/css">
	body {
		width: 100%;
		height: 100%;
	}
	td {
		height:25px;
		border-bottom: solid thin #d1d5de;
		border-right: solid thin #d1d5de;
	}
	table {
		border:solid thin #d1d5de;
		margin-bottom: 20px;
	}
	.dbParamStyle:HOVER {
		cursor: pointer;
	}
	.db_body table{font-size: 13px;}
	.server_info:hover{
		box-shadow:#666 0px 0px 10px;  
		-webkit-box-shadow:#666 0px 0px 10px;  
		-moz-box-shadow:#666 0px 0px 10px;  
	}
	.tdname{
		width:18%;
	}
	.tdvalue{
		width:30%;
	}
	tbody td{
		padding-left:10px;
		padding-right:10px;
	}
	.server_div{
		margin-bottom: 25px;
	}
	.spaceTab_div table td{
		width:100px;
	}
</style>
<title>数据库详情</title>
</head>
<body id="dbMinuteParam">
	<div class="param_body_div" style="width:100%;overflow-y:auto; ">
		<div class="db_head" style="margin: 1%;border-bottom:1px solid #ddd;">
			<ol class="breadcrumb">
			  <li><a href="javasctipt:void(0);" onclick="goMonBack();">监控主页</a></li>
			  <li><a href="javasctipt:void(0);" onclick="goDbBack();">数据库</a></li>
			  <li class="active">详情</li>
			  <li><a href="javasctipt:void(0);" onclick="goBack();">返回</a></li>
			</ol>
		</div>
		<div class="db_body" style="width: 96%;margin: 1%;">
		    <div style="width: calc(100% - 50% - 25px);height: 100%;float: left;margin: 1%;">
		        <div>
					 <table id="basicTable" style="BORDER-COLLAPSE:collapse; height: 40px; cellPadding: 1; align: left; width: 100%;">
						<thead><tr><td colspan="4" style="background-color: #63bfe2"><b>&nbsp;&nbsp;基本信息</b></td></thead>
						<tbody>
						   <tr>
								<td class="tdname">实例名</td><td class="tdvalue"><span id="name"></span></td>
								<td class="tdname">实例ID</td><td class="tdvalue"><span id="nameId"></span></td>
							</tr>
							<tr>
								<td class="tdname">状态</td><td class="tdvalue"><span id="instance_state"></span></td>
								<td class="tdname">IP地址</td><td class="tdvalue"><span id="ip"></span></td>
							</tr>
							<!-- <tr>
								<td class="tdname">字符集</td><td  class="tdvalue"><span id="zifuId"></span></td>
								<td class="tdname">端口</td><td class="tdvalue"><span id="portId"></span></td>
							</tr> -->
						</tbody>
					</table>
				</div>
		        <div>
					<table id="normTable" style="BORDER-COLLAPSE: collapse; width: 100%;">
						<thead><tr><td colspan="4" style="background-color: #63bfe2;"><b>&nbsp;&nbsp;指标信息</b></td></tr></thead>
						<tbody></tbody>
					</table>
				</div>
		    </div>
		    <div style="width: calc(100% - 50% - 25px);height: 90%;float: right;">
				<div id="myChart1" style="height: 200px;width:50%;float:left;"></div>
				<div id="myChart2" style="height: 200px;width:50%;float:left;"></div>
			</div>
			<!-- 存放表空间信息 -->
		    <div class="spaceTab_div" style="width: 100%;margin: 1%;"></div>
		    <!-- 存放对应服务器信息 -->
		    <div class="server_div" style="width: 100%;margin: 1%;overflow: hidden;border: 1px solid #ccc;"></div>
		    <br/>
		    <br/>
		</div>
	</div>
</body>
<script type="text/javascript">
    var p_hei=$("#tt",parent.document).height();
	$(".param_body_div").css({height:p_hei+"px"});
	$(".db_back").click(function(){
		goBack();
	});
	var uid =$("#uid").val();
	var chartInterval = setInterval(function(){
		getParam(uid);
	},10000);
	window.top.softtime.push(chartInterval);
	
	function goDbBack(){
		//关闭软件定时器
		var softtime = window.top.softtime;
		for(var i=0;i<softtime.length;i++){
			clearInterval(softtime[i]);
		}
		var path=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/databaseList.jsp";
		$("#tt").load(path);
	}
</script>
</html>