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
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/warnrule/js/warnrulee.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<style>
	.ui-jqgrid .ui-jqgrid-caption{
		background-color: #7ab8dd;
	}
	select{
		font-size: 13px;
		height: 33px;
	}
	input{
		font-size: 13px !important;
	}
</style>
<title>告警规则</title>
</head>
<body>
	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">指标分类：</span>
					<select style="width:70%;font-size:12px;" id="indextype_search" onchange="getindexinfo(this,'#index_search')">
						<option value=" " selected>--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<span class="txt">指标项：</span>
					<select style="width:70%;font-size:12px;" id="index_search">
						<option value=" " selected>--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<button id="warnruleSrch" class="btn"><i class="icon-search"></i> 查询</button>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListWarRul"></table>
		<div id="jqGridPaperListWarRul"></div>
	</div>
	<input type="hidden" id="sid" />
</body>
<script type="text/javascript">
  $(function(){
	initWarulFunction();
  });
	//验证数字位数
	function validateNum(){
		var val = $("#upper_limit").val();
		if(val.length > 5){
			return "位数不能超过5位";
		}
	}
	function validateNum1(){
		var val = $("#lower_limit").val();
		if(val.length > 5){
			return "位数不能超过5位";
		}
	}
</script>
</html>
