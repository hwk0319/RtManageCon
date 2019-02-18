<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/modelmgt/js/modeladd.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<title>设备型号</title>
<style>
	input[type='text'],
	input[type='number']{
		height: 23px;
		width: 168px;
	}
	table tr {
	    height: 40px;
	}
</style>
</head>
<body>
	<div style="">
	<table style="align: left; width: 80%; margin-left: 20%;font-size: 13px;" >
		<tbody>
			<tr>
				<td>类型：</td>
				<td>
					<select id="devicetype"  validate="nn" style="width:182px;font-size: 13px;">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>厂商：</td>
				<td>
					<select id="factory"  validate="nn" style="width:182px;font-size: 13px;">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>型号：</td>
				<td>
					<input type="text" validate="nn nspclcn" maxlength="15" id="model" placeholder="请输入型号">
				</td>
			</tr>
			<tr>
				<td>容量：</td>
				<td><input type="text" id="msize" validate="callback num" callback="validateNum();" placeholder="请输入容量"></td>
			</tr>
			<tr id="portId">
				<td>端口数：</td>
				<td><input type="text" id="port" validate="callback num" callback="validateNum1();" placeholder="请输入端口数"></td>
			</tr>
			<input type="hidden" id="id"/>
			<input type="hidden" id="num"/>
		</tbody>
	</table>
	</div>
	<div class="widget-content" style="">
	<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p style="text-align:center;">
			<button class="btn" onclick="doSave()"><i class="icon-ok"></i> 确定</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script>
//验证数字位数
function validateNum(){
	var val = $("#msize").val();
	if(val.length > 5){
		return "位数不能超过5位";
	}
}
function validateNum1(){
	var val = $("#port").val();
	if(val.length > 5){
		return "位数不能超过5位";
	}
}
</script>
</html>
