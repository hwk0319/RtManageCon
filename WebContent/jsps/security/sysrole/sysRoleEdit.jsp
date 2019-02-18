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
	input[type='text'],input[type='number']{
		height: 23px;
		width: 168px !important;
	}
</style>
</head>
<body>
<table class="table-edit" style="font-size:13px;margin-left: 50px;">
  	<tbody>
	     <tr>
	      <td nowrap>角色名称：</td><td><input  type="text" id="rolename" onblur="ckeckName()" /></td>
	      <td class="mianInfo">*<label id="msgname"></label></td>
	    </tr>
	    <tr>
				<td >角色类型：</td>
				<td><select id="roletype">
						<option value="">--请选择--</option>
						<option value="1">配置</option>
						<option value="2">业务</option>
						<option value="3">审计</option>
				</select></td>
				<td class="mianInfo">*<label id="msgtype"></label></td>
			</tr>
	    <tr>
	      <td>角色说明：</td><td><input  type="text" id="roledesc" /></td>
	      <td class="mianInfo"><label id="msg"></label></td>
	    </tr>
	</tbody>
</table>
<div class="widget-content" style="margin: 20px 2px;">
		<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p style="text-align:center;">
		<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 保存</button>
		<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
</div>
</body>
<script type="text/javascript">
var roleid;
$(function() {
	roleid =$.getUrlParam('roleid');
	setDetailValue();
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		/* window.close();
		$("#alert").hide(); */
		parent.layer.closeAll();
	})
});
//初始化明细信息
function setDetailValue(){
	$.ajax({
		url : getContextPath()+"/systemMan/sysRoleSearch",
		type : "post",
		async : false,
		data : {
			"roleid":roleid
		},
		dataType : "json",
		success : function(result) {
			if(result.length>0){
				$("#rolename").val(result[0].rolename);
				$("#roledesc").val(result[0].roledesc);
				$("#rolemenu").val(result[0].rolemenu);
				$("#roletype").val(result[0].roletype);
			}
		},
		error : function() {
			layer.alert("加载失败");
		}
	});
}
function ckeckName() {
	var rolename = $("#rolename").val();
	var exp = /[@'\"$&^*{}<>\\\:\;]+/;
	if (rolename != '') {
		var reg = rolename.match(exp);
		if (reg) {
			$("#msgname").html("请不要输入特殊字符");
		} else {
			$("#msgname").html("");
		}
	} else {
		$("#msgname").html("角色名称不能为空");
	}
}
function doSave() {
	var rolename = $("#rolename").val();
	var roletype = $("#roletype").val();
	var roledesc = $("#roledesc").val();
	var exp = /[@'\"$&^*{}<>\\\:\;]+/;
	if (rolename != '') {
		var reg = rolename.match(exp);
		if (reg) {
			$("#msgname").html("请不要输入特殊字符");
			return;
		} else {
			$("#msgname").html("");
		}
	} else {
		$("#msgname").html("角色名称不能为空");
		return;
	}
	if(roletype==null || roletype==""){
		$("#msgtype").html("请选择角色类型");
		return;
	}else{
		$("#msgtype").html("");
	}
	if (roledesc != '') {
		var reg = roledesc.match(exp);
		if (reg) {
			$("#msg").html("请不要输入特殊字符");
			return;
		} else {
			$("#msg").html("");
		}
	} else {
		$("#msg").html("");
	}
	$.ajax({
		url : getContextPath()+"/systemMan/updateSysRole",
		type : "post",
		data : {
			"roleid" : roleid,
			"rolename" : $("#rolename").val(),
			"roledesc" : $("#roledesc").val(),
			"rolemenu" : $("#rolemenu").val(),
			"roletype" : $("#roletype").val()
		},
		success : function(result) {
			if(result){
				layer.alert("保存成功！", {
					title: "提示"
				},function(){
					window.parent.doSearch();
					parent.layer.closeAll();
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