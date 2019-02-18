<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css" />
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsLib/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsps/js/security/rolemanager/accredit.js"></script>
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsps/js/dict/SYS_ROLE.js"></script>
<title>角色授权</title>
</head>
<body>
<div align="center" style="margin-top: 20px;">
		<table class="table-edit">
			<tbody>
				<tr>
					<td nowrap>用户名：</td>
					<td><input type="text" id="username" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>角色：</td>
					<td><select id="roleid"></select></td>
				</tr>
				<tr>
					<td><input type="hidden" id="id" /></td>
				</tr>
			</tbody>
		</table>
		<div class="widget-content" style="margin-top:5%;">
			<p style="text-align:center;">
				<button class="btn" onclick="doSave()"><i class="icon-ok"></i> 保存</button>
				<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
			</p>
		</div>
</div>
</body>
<script type="text/javascript">
$(function() {
	//addDrop();//初始化类型下拉框
	initCombox();
	setDetailValue();
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		//window.close();
		parent.layer.closeAll();
	})
});


</script>
</html>