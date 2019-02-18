<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jsLib/md5-min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/jsLib/security.js"></script>
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsps/js/security/userList.js"></script>
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsps/js/dict/SYS_ROLE.js"></script>
<title>用户信息</title>
</head>
<body>
	<div class="grid_content search" style="margin:1%;font-size:14px;">
		<div class="title">
			<span style="color:#000;">查询信息与操作</span>
		</div>
		<div style="margin:1%;">
		          用户名：<input type="text" id="USERNAME_SEARCH" /> <button class="btn" style="margin-left:80px;"  onclick="doSearch()"><i class="icon-search"></i>查询</button>
		</div>
		<div class="" style="margin:1%;">
			<p>
			<!-- <button class="btn"  onclick="doSearch()"><i class="icon-search"></i>查询</button> -->
			操  &nbsp;&nbsp;作：<button class="btn"  onclick="doInsert()"><i class="icon-plus-sign"></i>新增</button>
			<button class="btn"  onclick="doEdit()"><i class="icon-edit"></i>编辑</button>
			<button class="btn"  onclick="doDelete()"><i class="icon-minus-sign"></i>注销</button>
			<button class="btn"  onclick="_doAccredit()"><i class="icon-edit"></i>角色授权</button>
			<button class="btn"  onclick="doLock()"><i class="icon-lock"></i>用户锁定</button>
			<button class="btn"  onclick="doUnlock()"><i class="icon-ok"></i>用户解锁</button>
<!-- 			<button class="btn"  onclick="doUpPass()"><i class="icon-lock"></i>修改密码</button> -->
			</p>
		</div>
	</div>	
	<div id="deviceMan" style="margin:1%;">
		<table id="jqGrid"></table>
   	    <div id="jqGridPager"></div>
	</div>
		
		<div id="tip"></div>
</body>
<script type="text/javascript">

$(function() {
	//addDrop();//初始化类型下拉框
	initCombox();
	creatGrid();
	
	//initSelect();//初始化新增下拉框
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		$("#alert").hide();
	})
});


</script>

</html>