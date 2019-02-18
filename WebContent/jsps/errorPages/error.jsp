<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<title>无权限跳转的页面</title>
</head>
<body>
	<div style="height: 100%;height: 100%;">
		<div style="height: 20%;width: 100% ;text-align: center;"></div>
		<div style="text-align: center;">
			<img alt="" style="width: 100%;" src="${pageContext.request.contextPath}/jsps/images/cross2.png">
		</div>
	</div>
	<div style="text-align: center;margin-top: 10px;">
		<a href="${pageContext.request.contextPath}/jsps/index.jsp">返 回</a>
	</div>
</body>
</html>