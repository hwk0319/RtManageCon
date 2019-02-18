<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/comm/css/quwery.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<title>菜单编辑</title>
<style>
	input[type='text']{
		width:168px !important;
		height:23px;
	}
</style>
</head>
<body>
	<table class="table-edit" style="font-size:13px;margin-left: 50px;">
	  	<tbody>
		    <tr>
		      <td nowrap>菜单名称：</td><td><input  type="text" id="menuname" onblur="ckeckName()"/></td>
		      <td class="mianInfo">*<label id="msgname"></label></td>
		    </tr>
		    <tr>
		      <td>菜单类型：</td>
		      <td>
		      	<select id="menutarget" >
		      		<option value="">请选择</option>
		      		<option value="1">配置</option>
		      		<option value="2">业务</option>
		      		<option value="3">审计</option>
		      	</select>
		      </td>
		      <td class="mianInfo">*<label id="msgtype"></label></td>
		    </tr>
		    <tr>
		      <td>菜单排序：</td><td><input  type="text" id="menuorder" /></td>
		      <td class="mianInfo"><label id="msgnumber"></label></td>
		    </tr>
		</tbody>
	</table>
	<div class="widget-content" style="margin-top:10%;">
	<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p style="text-align:center;">
			<button class="btn" onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script type="text/javascript">
var menucode;
$(function() {
	menucode =$.getUrlParam('menucode');
	setDetailValue();
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	})
});
//初始化明细信息
function setDetailValue(){
	$.ajax({
		url : getContextPath()+"/systemMan/searchGrid",
		type : "post",
		async : false,
		data : {
			"menucode":menucode
		},
		dataType : "json",
		success : function(result) {
			if(result.length>0){
				$("#menuname").val(result[0].menuname);
				$("#menuorder").val(result[0].menuorder);
				$("#menutarget").val(result[0].menutarget);
			}
		},
		error : function() {
			layer.alert("加载失败");
		}
	});
}

function ckeckName(){
	var menuname = $("#menuname").val();
	var exp = /[@'\"$&^*{}<>\\\:\;]+/;
	
	if (menuname != '') {
		var reg = menuname.match(exp);
		if (reg) {
			$("#msgname").html("请不要输入特殊字符");
		}else{
			$("#msgname").html("");
		}
	}else{
		$("#msgname").html("菜单名称不能为空");
	}
}

function doSave() {
	var menuname = $("#menuname").val();
	var menutarget =$("#menutarget").val();
	var menuorder = $("#menuorder").val();
	var exp = /[@'\"$&^*{}<>\\\:\;]+/;
	if (menuname != '') {
		var reg = menuname.match(exp);
		if (reg) {
			$("#msgname").html("请不要输入特殊字符");
			return;
		}else{
			$("#msgname").html("");
		}
	}else{
		$("#msgname").html("菜单名称不能为空");
		return;
	}
	if(menutarget == ""){
		$("#msgtype").html("请选择菜单类型");
		return;
	}else{
		$("#msgtype").html("");
	}
	if(menuorder != ""){
		var numberexp = "[^0-9]";
		if(menuorder.match(numberexp)){
			$("#msgnumber").html("请输入数字");
			return;
		}else{
			$("#msgnumber").html("");
		}
	}else{
		$("#msgnumber").html("");
	}
	$.ajax({
		url : getContextPath()+"/systemMan/update",
		type : "post",
		data : {
			"menucode" : menucode,
			"menuname" : menuname,
			"menuorder" : menuorder,
			"menutarget" :menutarget
		},
		success : function(result) {
			if(result){
				layer.alert("保存成功！", {
					title: "提示"
				},function(){
					parent.layer.closeAll();
					window.opener.doSearch();
				});
			}
		},
		error : function() {
			layer.alert("保存失败！");
		}
	});
}
</script>

</html>