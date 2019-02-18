<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/task/add/creatNewStep.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<style>
	input[type='text'],input[type='number']{
		width:168px;
		height: 23px;
	}
	table tr{
		height: 40px;
	}
</style>
<title>添加步骤</title>
</head>
<body>
	<table style="align: left; width: 100%; font-size: 13px;" >
		<tbody>
			<tr>
				<td>步骤名称：</td>
				<td>
					<input type="text"  validate="nspcl nn" id="name" maxlength="15" placeholder="请输入步骤名称"/>
				</td>
			</tr>
			<tr>
				<td>步骤号：</td>
				<td>
					<input type="text"  validate="num nn" min="0" id="stepOrder" placeholder="请输入步骤号"/>
				</td>
			</tr>
			<tr>
				<td>超时时间（s）：</td>
				<td>
					<input type="text"  validate="num nn" min="0" id="timeOut" placeholder="请输入超时时间"/>
				</td>
			</tr>
			<tr>
				<td>步骤说明：</td>
				<td>
					<input type="text"  validate="nspcl nn" id="poDesc" maxlength="50" placeholder="请输入步骤说明"/>
				</td>
			</tr>
			<input type="hidden"  id="taskids"/>
		</tbody>
	</table>
	<div class="widget-content" style="">
	<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 确定</button>
			<button id="close1" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script type="text/javascript">
var taskid = "";
var modulus;
$(function() {
	taskid = $.getUrlParam("taskId");
	$("#taskids").val(taskid);
	//公钥
	modulus = $("#publicKey",parent.parent.document).val();
	//点击关闭，隐藏表单弹窗
	$("#close1").click(function() {
		parent.layer.closeAll();
	});
});

//保存
function doSave() {
	if(!validateHandler()){
		return;
	}
	var taskId = $("#taskids").val();
	var name = $("#name").val();
	var stepOrder = $("#stepOrder").val();
	if(parseInt(stepOrder) > 10){
		layer.msg("步骤号不能大于10！");
		return;
	}
	//校验步骤号。步骤号不能重复
    var step = validateStepNum(stepOrder);
    if(step != ""){
    	layer.msg(step);
    	return;
    }
	var timeOut = $("#timeOut").val();
	if(parseInt(timeOut) > 300){
		layer.msg("超时时间不能大于300s!");
		return;
	}
	var poDesc = $("#poDesc").val();
	
	//拼接数据字段
  	var mData = ""+taskId+name+stepOrder+timeOut+poDesc+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);

	$.ajax({
	    type : 'POST',
	    url : getContextPath()+"/taskmanage/taskStep",
	    dataType : 'json',
	    data : {
	      "id" : "0",
	      "taskId": taskId,
	      "name" : name,
	      "stepOrder" : stepOrder,
	      "timeOut" : timeOut,
	      "poDesc" : poDesc,
	      "mData" : mData,
	      "oper" : "add"
	    },
	    success : function(data) {
	    	if (data.status == "success") {
	    		parent.layer.closeAll();
	    		parent.creatTaskStepGrid(taskId);
			} else {
				layer.alert(data.value);
			}
	    },
	    error : function(text) {
	    	layer.alert("保存失败！");
	    }
	  });
}

//校验步骤号。步骤号不能重复
function validateStepNum(stepOrder){
	var reg = new RegExp("^[0-9]*$");
	if(!reg.test(stepOrder))
    {
    	return "步骤号只能为数字！";
    }
	var datas = $('#jqGridTaskStep',parent.document).jqGrid('getRowData');
	var numArray = [];
	$(datas).each(function(index, date){
		numArray[index] = date.stepOrder;
	});
	if(numArray.indexOf(stepOrder) > -1){
		return "步骤号不能重复!";
	}
	return "";
}
</script>
</html>
