<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/address/js/address.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<title>联系人</title>
<style>
input[type='text'],input[type='number']{
	height: 23px;
	width: 182px;
}
table tr {
    height: 40px;
}
</style>
</head>
<body>
	<table style="align: left; width: 80%; margin-left: 20%;margin-top: 5%;font-size: 13px;" >
		<tbody>
			<tr>
				<td>姓名：</td>
				<td>
					<input type="text" validate="nn nspcl" maxlength="15" id="name" placeholder="请输入姓名">
				</td>
			</tr>
			<tr>
				<td>手机号：</td>
				<td>
					<input type="number" validate="nn phone" id="phone" placeholder="请输入手机号">
				</td>
			</tr>
			<tr>
				<td>邮箱：</td>
				<td>
					<input type="text" validate="nn email" id="email" placeholder="请输入邮箱">
				</td>
			</tr>
			<tr>
				<td>地址：</td>
				<td><input type="text" id="address" validate="nspcl" maxlength="50" placeholder="请输入地址"></td>
			</tr>
			<input type="hidden" id="id"/>
		</tbody>
	</table>
	<div class="widget-content" style="margin-top:5%;">
	<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p style="text-align:center;">
			<button class="btn" onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
</html>
