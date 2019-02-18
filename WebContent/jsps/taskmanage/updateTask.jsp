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
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/taskmanage/js/taskList.js"></script>
<title>修改任务</title>
<style>
input[type='text']{
	height: 23px !important;
}
.ui-widget-header {
    background: #7ab8dd;
    color: black;
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
				<td>任务名称：</td>
				<td>
					<input type="text"  validate="nspcl nn" id="name" maxlength="100"/>
				</td>
			</tr>
			<tr>
				<td>任务分类：</td>
				<td>
					<select id="type" name="element" style="width: 196px !important;height:33px" validate="nspclcn nn">
							<option value="">--请选择--</option>
							<option value="1">普通任务</option>
<!-- 							<option value="2">定时任务</option> -->
					</select>
				</td>
			</tr>
			<tr>
				<td>任务描述：</td>
				<td>
					<input type="text" id="poDesc" validate="nspcl" maxlength="200"/>
				</td>
			</tr>
			
			<input type="hidden"  id="taskids"/>
		</tbody>
	</table>
	<div class="widget-content" style="margin-top:25px;">
	<hr class="ui-widget-content" style="margin:1px">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 确定</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script type="text/javascript">
var taskid = "";
$(function() {
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	});
	
	//判断是新增还是编辑
	  var taskId=$.getUrlParam("taskId");
	  if(taskId != 'undefined'){
		 $("#taskids").val(taskid);
	  	 setDetailValue(taskId);
	  };
});

//保存
function doSave() {
	if(!validateHandler()){
		return;
	}
	var taskId = $("#taskids").val();

	$.ajax({
	    type : 'POST',
	    url : getContextPath()+"/taskmanage/taskOperation",
	    dataType : 'json',
	    data : {
	      "id" : taskId,
	      "taskId": taskId,
	      "name" : $("#name").val(),
	      "type" : $("#type").val(),
	      "poDesc" : $("#poDesc").val(),
	      "oper" : "edit"
	    },
	    success : function(data) {
	    	layer.alert("保存成功！", function(){
	   			parent.layer.closeAll();
				parent.pageInit();
			});
	    },
	    error : function(text) {
	    	layer.alert("保存失败！");
	    }
	  });
}

var type = "";
//编辑设置value
function setDetailValue(taskId) {
	var rs = load(taskId);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.rows.length>0){
		//密码回填
		$("#taskids").val(rs.rows[0].id);
		$("#name").val(rs.rows[0].name);
		type = rs.rows[0].type;
		$("#type option[value='"+type+"']").attr("selected",true);
		$("#poDesc").val(rs.rows[0].poDesc);
    }
}
//编辑加载数据
function load(taskId) {
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/taskmanage/taskList",
		type : "post",
		async : false,
		data : {
			"id" : taskId
		},
		dataType : "json",
		success : function(result) {
			resultvalue = result;
		},
		error : function() {
			layer.alert("加载失败");
		}
	});
	return resultvalue;
}


</script>
</html>
