<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsps/js/config/sysParamList.js"></script>
<style>
	article{margin:8px 8px;}
	.sty2{margin-top:0px;}
</style>
<title>系统配置参数</title>
</head>
<body>
	<div class="search" style="margin-left: 10px;margin-right: 10px;">
		<div class="title">
			<span style="color:#000;">查询信息</span>
		</div>
		<div class="sty2" style="margin:1% 10%;">
		          &nbsp;&nbsp;参数编号：<input type="text" id="CODE_SEARCH" />
			&nbsp;&nbsp;&nbsp;&nbsp;参数名称：<input type="text" id="NAME_SEARCH" />
		</div>
		<div class="widget-content" style="margin:1% 25%;">
			<p>
			<button class="btn"  onclick="doSearch()"><i class="icon-search"></i>查询</button>
			&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn"  onclick="doEdit()"><i class="icon-edit"></i>编辑</button>
			</p>
		</div>
	</div>
	<div id="deviceMan" style="margin:1%">
		<table id="jqGrid"></table>
   	    <div id="jqGridPager"></div>
	</div>
		
	<div id="tip"></div>
	
 
</body>
<script type="text/javascript">

$(function() {
	creatGrid();
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	})
});


</script>

</html>