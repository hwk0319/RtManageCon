<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsps/taskmanage/js/myProgress.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/layui/lay/modules/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/jquery.myProgress.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/taskList.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/My97DatePicker/WdatePicker.js"></script>
<style>
.ui-jqgrid .ui-jqgrid-caption{
	background-color: #7ab8dd;
}
.ui-jqdialog-title{
	color: black;
}
.prom {
    width: 100%;
    height: 2.5em;
    margin-left: 0%;
}
.ui-widget-header {
    background: #7ab8dd;
    color: black;
}
input[type='text']{
	height: 23px;
	width: 65%;
}
</style>
<title>任务列表</title>
</head>
<body>
	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom">
				<div class="prom-3">
					<div class="w_p30">
						<span class="txt">任务分类:</span> <select
							style="width: 70%; font-size: 12px;" id="taskModul_search">
							<option value="" selected>--请选择--</option>
						</select>
					</div>
					<div class="w_p30">
						<span class="txt">任务名称:</span> <input type="text" id="name_search">
					</div>
					<div class="w_p30">
						<button id="taskSearch" onclick="doTaskListSearch()" class="btn">
								<i class="icon-search"></i> 查询
						</button>
					</div>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div class="tasklistjq" style="margin:1%">
		<table id="jqGridTaskList"></table>
		<div id="jqGridTaskPaperList"></div>
	</div>
	<input type="hidden" id="shareTaskId" />
	<input type="hidden" id="taskOperationType" />
	<div id="cron_iframe" style="display:none;">
	  <Iframe id="iframe_cron" src="${pageContext.request.contextPath}/jsp/plugins/Cron/cron.jsp"; style="width:100%;height:300px" scrolling="no" frameborder="0"></iframe> 
	</div>
</body>
<script>
</script>
</html>