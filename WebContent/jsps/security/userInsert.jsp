<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/jsps/include/head.jsp"%>    
	
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jsLib/md5-min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jsLib/security.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jsps/js/security/userInsert.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jsps/js/dict/SYS_ROLE.js"></script>

<title>用户新增</title>
</head>
<body>
		<table class="table-edit">
			<tbody>
				<tr>
					<td nowrap>用户名：</td>
					<td><input type="text" id="username" autocomplete="off"  onblur="checkUN()" /></td>
					<td class="mianInfo">*<label id="msg"></label><label id="unmsg"></label></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input type="password" autocomplete="off"  id="password" /></td>
					<td class="mianInfo">*<label id="pwdmsg"></label></td>
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
						id="firstlogin" value="1" /> <input type="hidden" id="online" />
						<input type="hidden" id="status" value="1" /> <input
						type="hidden" id="err_num" value="0" /> <input type="hidden"
						id="modules"
						value="<%=request.getSession().getAttribute("model")%>" /></td>
				</tr>
			</tbody>

		</table>
			<div id="tip"></div>
		<div class="widget-content">
			<p>
				<button class="btn" onclick="doSave()">
					<i class="icon-ok"></i> 保存
				</button>
				<button id="close" class="btn" onclick="doClose()">
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