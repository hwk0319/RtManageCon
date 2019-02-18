<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head> 
    <base href="<%=basePath%>">
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/group/js/group.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<style type="text/css">
	td {
		width: 95px;
		height:25px;
		border-bottom: solid thin #d1d5de;
		border-right: solid thin #d1d5de;
	}
	table {
		border:solid thin #d1d5de;
		margin-bottom: 40px;
	}
	.dbParamStyle:HOVER {
		cursor: pointer;
	}
	html {
		width: 100%;
	}
	body {
		width: 100%;
		background-color: #f5f5f5;
	}
	</style>
  </head>
  <body>
  	<div>
  		<img src="imgs/mon/app.png" style='width:119px;height:126px;margin:45px;float: left;margin-top:40px'/>
  	</div>
	<div style="width: 58%;height: 100%;float: left;margin-top: 10px;">
		<table id="normTable" style="BORDER-COLLAPSE: collapse; width: 100%; font-size: 13px;">
			<tr>
				<td colspan="4" style="background-color: #f6f8fa;"><b>详细信息</b></td>
			</tr>
			<tr style="width:190px;">
				<td>类型：<span id=devicetype></span></td>
				<td>厂商：<span id=factory></span></td>
			</tr>
			<tr>
				<td>型号：<span id=model></span></td>
				<td>主机名：<span id=name></span></td>
			</tr>
			<tr>
				<td>应用IP：<span id=in_ip></span></td>
				<td>带外IP：<span id=out_ip></span></td>
			</tr>
			<tr>
				<td>应用用户名：<span id=in_username></span></td>
				<td>带外用户名：<span id=out_username></span></td>
			</tr>
			<tr>
				<td>SN码：<span id=sn></span></td>
				<td>操作系统：<span id=opersys></span></td>
			</tr>
			<tr>
				<td>位置信息：<span id=position></span></td>
				<td>资产编号：<span id=assetno></span></td>
			</tr>
		</table>
	</div>

</body>
  <script>
  $(function() {
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
	
	  //获取id
	  var id=$.getUrlParam('id');
	  //查询设备详情
	  searchDeviceInfo(id);

  });

   //查询设备
   function searchDeviceInfo(id){
	  $.ajax({
			url:getContextPath()+"/devicesCon/searchById",
			type:'POST',
			data:{id:id},
			dataType:'json',
			success:function(data){
				if(data != null && data.length > 0){
					$("#devicetype").html(data[0].devicetypename);
					$("#factory").html(data[0].factoryname);
					$("#model").html(data[0].model);
					$("#name").html(data[0].name);
					$("#sn").html(data[0].sn);
					$("#in_ip").html(data[0].in_ip);
					$("#in_username").html(data[0].in_username);
					$("#opersys").html(data[0].opersysname);
					$("#out_ip").html(data[0].out_ip);
					$("#out_username").html(data[0].out_username);
					$("#assetno").html(data[0].assetno);
					var position = "";
					if(data[0].position == "0"){
						position = "内网";
					}else{
						position = "外网";
					}
					$("#position").html(position);
				}
			}
		});
  }
  
  </script>
  
</html>
