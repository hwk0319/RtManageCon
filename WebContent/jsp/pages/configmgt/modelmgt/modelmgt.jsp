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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/address/css/address.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/modelmgt/js/modelmgtt.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<style>
.ui-jqgrid .ui-jqgrid-caption{
	background-color: #7ab8dd;
}
</style>
<title>设备型号</title>
</head>
<body>
	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">类型：</span>
					<select style="width:70%;font-size:12px;" id="selectId" onchange="getfactory(this)">
						<option value=" " selected>--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<span class="txt">厂商：</span>
					<select style="width:70%;font-size:12px;" id="factory_select">
					<option value=" " selected>--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<button id="srch" class="btn"><i class="icon-search"></i> 查询</button>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridList"></table>
		<div id="jqGridPager"></div>
	</div>
</body>
</html>
