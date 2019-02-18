<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/jsps/include/head.jsp"%>    
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />

<script type="text/javascript" language="javascript"
	src="${pageContext.request.contextPath}/jsps/js/security/userEdit.js"></script>
<script type="text/javascript" language="javascript"
	src="${pageContext.request.contextPath}/jsps/js/dict/SYS_ROLE.js"></script>

<title>用户编辑</title>
</head>
<body>
	<table class="table-edit">
		<tbody>
			<tr>
				<td nowrap>用户名：</td>
				<td><input type="text" id="username" readonly="readonly" /></td>
				<td class="mianInfo">*<label id="msg"></label></td>
			</tr>
			<tr>
				<td>电子邮箱：</td>
				<td><input type="text" id="email"></td>
			</tr>
			<tr>
				<td>起始IP：</td>
				<td><input id="start_ip"></td>
			</tr>
			<tr>
				<td>结束IP：</td>
				<td><input id="end_ip"></td>
			</tr>
			<tr>
				<td>时间限制：</td>
				<td><input id="start_time" type="number" max=23 min=0
					style="width: 82px;">--<input id="end_time" type="number"
					max=23 min=0 style="width: 82px;"></td>
			</tr>
			<tr>
				<td><input type="hidden" id="id" /> <input type="hidden"
					id="modules"
					value="<%=request.getSession().getAttribute("model")%>" /></td>
			</tr>
		</tbody>
	</table>
	<div class="widget-content">
		<p>
			<button class="btn" onclick="doSave()">
				<i class="icon-ok"></i> 保存
			</button>
			<button id="close" class="btn">
				<i class="icon-remove"></i> 关闭
			</button>
		</p>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//addDrop();//初始化类型下拉框
		initCombox();
		setDetailValue();
		//点击关闭，隐藏表单弹窗
		$("#close").click(function() {
			parent.layer.closeAll();
		})
	});
</script>


</html>