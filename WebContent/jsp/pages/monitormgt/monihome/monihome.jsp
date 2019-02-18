<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/css/monihome.css">
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/jsp/comm/css/style.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/js/monihome.js"></script>
<title>监控管理</title>
</head>
<body>
<div class="nav">
	<div class="title" style="font-family:'STZhongsong';">
		<div class="title_ch">
			欢迎进入监控管理
		</div>
		<div class="title_en">
			Welcome to the monitoring and management
		</div>
	</div>
	<div class="hardware">
		<div class="server">
			<div class="info">
				<div class="error" style="background-color: red;display: none;"></div>
			</div>
			<div class="log  box-shadow">
				<img alt="" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/imgs/sev.png">
			</div>
			<span class="txt">服务器</span>
		</div>
		<div class="active-active">
			<div class="info">
				<div class="error" style="background-color: red;display: none;"></div>
			</div>
			<div class="log  box-shadow">
				<img alt="" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/imgs/doub.png">
			</div>
			<span class="txt">双活</span>
		</div>
		<div class="cluster">
			<div class="info">
				<div class="error" style="background-color: red;display: none;"></div>
			</div>
			<div class="log  box-shadow">
				<img alt="" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/imgs/clusmenu.png">
			</div>
			<span class="txt">组</span>
		</div>
		
		<div class="oracledb">
			<div class="info">
				<div class="error" style="background-color: red;display: none;"></div>
			</div>
			<div class="log  box-shadow">
				<div class="softlist">
					<div class="imgg">
						<img alt="" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/imgs/datebase1.png">
					</div>
				</div>
			</div>
			<span class="txt">数据库</span>
		</div>
	</div>
</div>
<div class="clear"></div>
</body>
</html>