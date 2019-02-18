<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/css/monitorHead.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/database/css/database.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/database/js/database.js"></script>
    <style>
   </style>
   <title>数据库</title>
  </head>
<body style="overflow:auto;">
  <div class="contain">
	<div class="contain_head">
		<div class="search">
			<ol class="breadcrumb">
				  <li><a href="javasctipt:void(0);" onclick="goMonBack();">监控主页</a></li>
				  <li class="active">数据库</li>
				  <li><a href="javasctipt:void(0);" onclick="goMonBack();">返回</a></li>
			</ol>
		</div>
		<blockquote class="layui-elem-quote layui-quote-nm">
			<div class="hint">您好，欢迎访问数据库监控页面，共 <label class="total"></label> 个数据库，其中故障数为：<label class="error" style="color:red"></label></div>
			<div class="search-txt">
				<div class="txt">
					<input id="kw" type="text" placeholder="请输入数据库名称" style="height: 23px;"/>
				</div>
				<button id="search-btn" class="btn"><i class="icon-search"></i> 查询</button>
			</div>
		</blockquote>
	</div>
	<!-- 主体内容-start -->
	<div class="contentshow">
		<div class="contentinfo" id="contentinfo">
		</div>
	</div>
	<!-- 主体内容-end -->
  </div>
    <div id="dbDetails" class="sethidden" style="width:100%;overflow:hidden;"></div>
	<div id="dbMoni" class="sethidden" style="width:100%;overflow:hidden;"></div>
</body>
<script type="text/javascript">
 $(function(){
    $(".contain").css({width:"98%",height:"95%"});
//     $("#dbDetails").css({width:wid+"px",height:hei+"px"});
    $(".contain_head").css({width:"100%"});
    $(".pageDiv").css({width:(wid-20)+"px"});
    
    getdataList();
    
    $("body").on("click",".infomenu",function(){
		 var uid=$(this).attr("id");
    	 $("#uid").val(uid);
    	 var returnType=  $("#returnType").val();
    	 if(returnType == ""){
	    	 $("#returnType").val("database");
    	 }
		 $(".contain").addClass("sethidden");
		 $("#dbDetails").removeClass("sethidden");
		 $("#dbDetails").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/dbMinuteParam.jsp");
	});
	//从其他界面跳转至数据库详情页处理
	var isParam = $("#isParam").val();
	if(isParam === "Y"){
	    var uid = $("#uid").val();
	    var returnType = $("#returnType").val();
		 $(".contain").addClass("sethidden");
		 $("#dbDetails").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/dbMinuteParam.jsp?uid="+uid+"&returnType="+returnType);
		 $("#dbDetails").removeClass("sethidden");
	}
	
	$("#search-btn").click(function(){
		getdataList();
	});
	
	//关闭加载层
  	layer.close(index);
 });
</script>
</body>
</html>
