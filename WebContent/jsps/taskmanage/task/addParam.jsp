<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/task/add/creatNewStep.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<title>添加参数</title>
<style>
	input[type='text']{
		width:168px;
		height: 23px;
	}
	table tr{
		height: 40px;
	}
</style>
</head>
<body>
	<table style="align: left; width: 100%; font-size: 13px;" >
		<tbody>
			<tr>
				<td>参数项：</td>
				<td>
					<input type="text"  validate="nspcl nn" id="name" maxlength="15" placeholder="请输入参数项"/>
				</td>
			</tr>
			<tr>
				<td>默认值：</td>
				<td>
					<input type="text"  validate="nspcl nn" id="value" maxlength="15" placeholder="请输入默认值"/>
				</td>
			</tr>
			<tr>
				<td>参数描述：</td>
				<td>
					<input type="text"  validate="nspcl nn" id="poDesc" maxlength="50" placeholder="请输入参数描述"/>
				</td>
			</tr>
			
			<input type="hidden"  id="taskids"/>
		</tbody>
	</table>
	<div class="widget-content" style="margin-top:35px;">
	<hr class="ui-widget-content" style="margin:1px">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 确定</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script type="text/javascript">
var taskid = "";
var modulus;
$(function() {
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	});
	
	taskid = $.getUrlParam("taskId");
	$("#taskids").val(taskid);
	//公钥
	modulus = $("#publicKey",parent.parent.document).val();
});

//保存
function doSave() {
	if(!validateHandler()){
		return;
	}
	var taskId = $("#taskids").val();
	var name = $("#name").val();
	var value = $("#value").val();
	var poDesc = $("#poDesc").val();
	//拼接数据字段
  	var mData = ""+taskId+name+value+poDesc+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);

	$.ajax({
	    type : 'POST',
	    url : getContextPath()+"/taskmanage/taskParam",
	    dataType : 'json',
	    data : {
	      "id" : "0",
	      "taskId": taskId,
	      "name" : name,
	      "value" : value,
	      "poDesc" : poDesc,
	      "mData" : mData,
	      "oper" : "add"
	    },
	    success : function(data) {
	    	if (data.status == "success") {
	    		parent.layer.closeAll();
	    		parent.createTaskParamGrid(taskId);
			} else {
				layer.alert(data.value);
			}
	    },
	    error : function(text) {
	    	layer.alert("保存失败！");
	    }
	  });
}
</script>
</html>
