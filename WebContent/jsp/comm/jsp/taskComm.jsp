<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui-1.8.16.custom.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsps/taskmanage/js/myProgress.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/jquery.myProgress.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/taskComm.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/My97DatePicker/WdatePicker.js"></script>
<title>清单详情</title>
</head>
<body>
	<div class="search">
		<div class="title">
			<span>查询信息</span>
		</div>
		<div class="srch_con">
			<div class="prom">
				<div class="w_p40">
					<span class="txt">任务类型:</span> <select
						style="width: 70%; font-size: 12px;" id="taskModul_search">
						<option value="" selected>--请选择--</option>
					</select>
				</div>
				<div class="w_p40">
					<span class="txt">任务名称:</span> <input type="text" id="name_search">
				</div>
			</div>
			<div class="prom">
				<div class="w_p40">
					<span class="txt">创建时间:</span> <input id="startTime" class="Wdate"
						type="text"
						onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')||\'%y-%M-%d\'}',readOnly:'true'})">
				</div>
				<div class="w_p40">
					<span class="txt">至:</span> <input id="endTime" class="Wdate"
						type="text"
						onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'startTime\')}',readOnly:'true'})">
				</div>
			</div>
			<div class="prom">
				<div class="find">
					<div class="find-mid">
						<button id="taskSearch" onclick="doTaskListSearch()" class="btn">
							<i class="icon-search"></i>查询
						</button>
						<button id="taskReset" onclick="doTaskListSearchReset()" class="btn">
							<i class="icon-refresh"></i>重置
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<table id="jqGridTaskList"></table>
	<div id="jqGridTaskPaperList"></div>
	<input type="hidden" id="shareTaskId" />
	<input type="hidden" id="taskOperationType" />
	
	<div class="widget-content">
		<p style="text-align:center">
			<button class="btn" onclick="doSave()">
				<i class="icon-ok"></i> 保存
			</button>
			<button id="close" class="btn">
				<i class="icon-remove"></i 关闭
			</button>
		</p>
	</div>
</body>
 <script type="text/javascript">
  $(function(){
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
  });

 </script>
</html>