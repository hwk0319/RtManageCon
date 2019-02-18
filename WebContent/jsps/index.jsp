<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/index.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/index.js"></script>
<style>
	body{
		background:-webkit-gradient(linear, 0% 100%, 0% 0%,from(#3c8dbc), to(#f6f6f8));/*Chrome*/
	    background: -ms-linear-gradient(top,#f6f6f8 0%,#3c8dbc 100%); /*IE*/
	}
</style>
<title>系统管理</title>
</head>
<body>
	<div id="commHead">
		<div class="head_left"></div>
		<div id="head_right">
			<font class="head_title" style="float: left;">电力交易双活系统管理</font>
			<!-- 这里是告警信息提示-start -->
			<div class="div_alarm" style="overflow: hidden;float: left;line-height: 20px;margin-left:30px;margin-top:25px;color: #fff;font-size: 13px;">
				<marquee onMouseOut="this.start()" onMouseOver="this.stop()"><!-- 这里展示相关告警信息！ --></marquee>
			</div>
			<!-- 这里是告警信息提示-end -->
			<div class="logOut" style="float:right;margin-right:20px;cursor:pointer;font-size: 17px;" onclick="logOut()" title="注销">
	      		<div style="margin-top:17px;float:left;">
	      	 		<img src="${pageContext.request.contextPath}/imgs/logout.png" />
	      	 	</div>
	      	 	<div style="float:left;margin-left:4px;">
	      	 		<font class="font_style">注销</font>
	      	 	</div>
      	  	</div>
      	  	<div class="username" style="float:right;font-size: 17px;margin-right: 20px;">
				<div style="float:left;margin-top:15px;">
	      	 		<img src="${pageContext.request.contextPath}/imgs/user.png" />
	      	 	</div>
	      	 	<div style="float:left;margin-left:4px;margin-top:13px;" id="usernameText"></div>
			</div>
		</div>
	</div>
	<div id="comm_menu"></div>
	<div id="tt" style="overflow: auto;height: 100%;">
		<div id="welcom" class="title" style="font-family:'STZhongsong';text-align: center;margin-top: 5%;">
			<div class="title_ch" style="font-size: 20pt;">
				欢迎进入系统管理
			</div>
			<div class="title_en">
				Welcome to system management
			</div>
		</div>
	</div>
		<!-- 底部固定区域 -->
		<!-- <div class="layui-footer">
			Copyright © 2018 All rights reserved
		</div> -->
	<!-- 公钥 -->
	<input type="hidden" id="publicKey" value=""/>
	<input type="hidden" id="userRoleId" value="${sessionScope.user.roleid}"/>
	<!-- jwt -->
	<input type="hidden" id="jwt" value=""/>
	<input type="hidden" id="spsevuid" value="" />
	<input type="hidden" id="spsevid" value="" />
	<input type="hidden" id="devId" value="" />
	<input type="hidden" id="sevuid" value="" />
	<input type="hidden" id="sevcomid" value="" />
	<input type="hidden" id="isParam" value="" />
	<input type="hidden" id="uid" value="" />
	<input type="hidden" id="returnType" value="" />
	<input type="hidden" id="sevid" value="" />
	<input type="hidden" id="databaseUid" value="" />
</body>
</html>