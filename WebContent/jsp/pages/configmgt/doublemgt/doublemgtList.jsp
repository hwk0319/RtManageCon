<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/doublemgt/js/doublemgt.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/group/js/group.js"></script>
<script src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<title>双活管理</title>
<style>
.ui-jqgrid .ui-jqgrid-caption{
	background-color: #7ab8dd;
}
</style>
</head>
<body>
	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">名称：</span>
					<input type="text" id="name_search" >
				</div>
					<div class="w_p30">
						<span class="txt">具体类型：</span>
						<select style="width:70%;font-size:12px;" id="doubtype">
							<option value=" " selected>--请选择--</option>
						</select>
					</div>
				<div class="w_p30">
					<button id="cluSrch" class="btn"><i class="icon-search"></i> 查询</button>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListDou"></table>
		<div id="jqGridPaperListDou"></div>
	</div>
	<div id="tabs" style="display:none;width:98%;margin:1%;padding:0;border-radius:5px">
	  <ul>
		    <li><a href="#tabs-1">组管理</a></li>
	  </ul>
	  <div id="tabs-1" class="tabCon">
			<div id="tip"></div>
		 	<iframe id="HideFrm" name="HideFrm" style="display: none"></iframe> 
			 	<table id="jqGridListGro"></table>
				<div id="jqGridPaperListGro"></div>
	  </div>
  </div>
</body>
<script type="text/javascript">
  $(function(){
	initfunction();
  });
</script>
</html>
