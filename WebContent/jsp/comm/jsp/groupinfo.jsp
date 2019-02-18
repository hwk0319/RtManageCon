<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/group/js/group.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<style type="text/css">
		td {
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
		}
	</style>
	<title>组详情</title>
  </head>
  <body>
  	<div>	
  		<img src="jsps/img/group.png" style='width:140px;height:65px;margin:25px;float: left;margin-top:45px'/>
  	</div>
	<div style="margin-top:25px;width: 58%;height: 100%;float: left;">
		<table id="normTable" style="BORDER-COLLAPSE: collapse; width: 100%; font-size: 13px;">
			<tr>
				<td colspan="4" style="background-color: #f6f8fa;"><b>详细信息</b></td>
			</tr>
			<tr style="width:190px;">
				<td>名称：<span id=name></span></td>
			</tr>
			<tr>
				<td>组类型：<span id=grouptypeName></span></td>
			</tr>
			<tr>
				<td>具体类型：<span id=grotype></span></td>
			</tr>
			<tr>
				<td>描述：<span id=description></span></td>
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
	  searchGroupInfo(id);
  });

  //查询设备
   function searchGroupInfo(id){
	  $.ajax({
			url:getContextPath()+"/groupCon/searchById",
			type:'POST',
			data:{id:id},
			dataType:'json',
			success:function(data){
				if(data.length != null){
					$("#grouptypeName").html(data[0].grouptypeName);
					$("#name").html(data[0].name);
					$("#grotype").html(data[0].grotypeName);
					$("#description").html(data[0].description);
				}
			}
		});
  }
  
  </script>
  
</html>
