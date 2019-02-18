<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<head>
<title>用户登录</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/login.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/md5-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/security.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/login.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
</head>
<style>
.layui-footer {
	height: 30px;
	line-height: 30px;
	padding: 0 15px;
	text-align:center;
	color: #908e8e;
	font-size: 14px;
	margin-top: 100px;
}
</style>
<body class="bg">
	<div class="div1">
		<div class="div2">
			<div class="divInput">用户名</div><input id="username" name="username" type="text" class="style1" />
			<img alt="用户图片" src="${pageContext.request.contextPath}/jsps/images/user.png" class="style2" />
		</div>
		<div class="div2">
			<div class="divInput">密码</div><input id="pwd" name="pwd" type="password" class="style1" />
			<img alt="密码图片" src="${pageContext.request.contextPath}/jsps/images/password.png" class="style2" />
		</div>
		<div class="identify_code">
			<div class="divInput">验证码</div><input id="verification" type="text" onkeyup="checkCode()"  maxlength=4 class="style1" style="width: 158px;"/>
			<img id="vertity_img" src="../../validate.so" onclick="this.src='../../validate.so?'+new Date().getTime()">
		</div>
		<div class="div4">
			<input type="button" value="登&nbsp;&nbsp;&nbsp;&nbsp;录" class="button" onclick="login()" />
		</div>
		<input type="hidden" id="modulus"/>
	</div> 
	<div id="res"></div>
	<div id="tips"></div>
	  	<!-- 底部固定区域 -->
	<div class="layui-footer">
		Copyright © 2018 All rights reserved
	</div>
</body>
<script type="text/javascript">
</script>
</html>