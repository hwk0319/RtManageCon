<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/devmonimgt/js/devmoniadd.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<style>
	input[type='text']{
		height: 23px;
	}
	.per_45{
		width:45%;
	}
	.fl{
		float:left;
	}
	.topbox{
		font-size:13px;
	}
	.topbox div{
		margin-top: 1%;
	    margin-right: 1%;
	    margin-left: 1%;
	}
	.div_hidd{
		display:none;
	}
	.devtxt,.dtxt,.dntxt,.indextxt,.cyctxt,.colltxt{
	    display:inline-block;
		width:90px;
	}
	.seleop{
		font-size:13px;
	}
</style>
<title>添加设备监控</title>
</head>
<body>
	<div class="topbox">
		<div class="devuid per_45 fl">
			<span class="devtxt" >监控UID ：</span>
			<input type="text" validate="nn" id="devuid" style="cursor: pointer;" placeholder="请点击添加监控" onclick="deviceList()"  readonly/>
		</div>
		<div class="indextype per_45 fl">
			<span class="indextxt" >指标分类：</span>
			<select id="indextype" validate="nn" style="width:196px;"></select>
		</div>
		<div class="cycle per_45 fl">
			<span class="cyctxt">采集周期：</span>
			<input type="text"  validate="nn" id="cyctime"  placeholder="请选择周期" disabled/><button onClick="doCleak();" class="btn" style="height: 25;line-height: 1;width: 60;color: #7d7b7b;background: whitesmoke;">清 空</button>
		</div>
		<div class="colltype per_45 fl">
			<span class="colltxt">采集方式：</span>
			<select id="colltype" validate="nn" style="width:196px;">
				<option value="0">--请选择--</option>
				<option value="1" selected>集中</option>
<!-- 				<option value="2">单独</option> -->
			</select>
		</div>
		<!-- <div class="dtype div_hidd per_45 fl ">
			<span class="dtxt">类型：</span>
			<input type="text"  id="dtype" disabled/>
		</div>
		<div class="dname div_hidd per_45 fl">
			<span class="dntxt">主机名：</span>
			<input type="text"  id="dname" disabled/>
		</div> -->
	</div>
	</br>
	<iframe id="iframe_cron" src="/RtManageCon/jsp/plugins/Cron/cron.jsp" style="width:100%;height:300px" scrolling="no" frameborder="0"></iframe>
	<div class="widget-content" style="">
	<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
	<input type="hidden" id="ids" />
</body>
</html>
