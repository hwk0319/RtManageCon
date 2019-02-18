<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsps/css/style.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsps/taskmanage/task/createTask.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/layui-v2.3.0/layui/layui.all.js"></script>
<style type="text/css">
	.div-inline: {
		display: inline
	}
</style>
<title>任务</title>
</head>
<body>
<div style="width: 100%;height: 85%;">
	<div style="width: 95%;height: 100%;border:1px solid #ddd;margin:2%;position:relative;">
		<table id="taskTabs" class="table-edit" style="width: 100%;border-collapse:separate; border-spacing:0px 10px;margin-left: 10px">
				<tr>
					<td>任务分类：</td>
					<td style="font-size:12px;">
						<select name="element" id="taskModulSelect" validate="nn">
							<option value="">--请选择--</option>
						</select>
					</td>
					<td>任务模板：</td>
					<td style="font-size:12px;">
						<select name="element" id="taskTplSelect" validate="nn">
							<option value="">--请选择--</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>任务名称：</td>
					<td><input type="text" id="name" name="element" validate="nn" maxlength="60"style="width:168px !important;height:23px;" placeholder="请输入任务名称"/></td>
					<td>任务类型：</td>
					<td><select id="type" name="element" validate="nn">
							<option value="">--请选择--</option>
							<option value="1" selected>普通任务</option>
						</select>
					</td>
				</tr>
<!-- 				<tr id="cronExpressTR"> -->
<!-- 					<td>定时设置：</td> -->
<!-- 					<td><input  name="element" id="cronExpress"  style="width:182px;height:30px;background :#F0F0F0;disabled:true;" /></td> -->
<!-- 				</tr> -->
				<tr>
					<td>任务描述：</td>
					<td><textarea type="text" id="desc" name="element" maxlength="100" style="width:181px;" placeholder="请输入任务描述"></textarea>
					</td>
				</tr>
		</table>
		<button class="btn" id="nextStep" onclick="createTask()" style="position:absolute;right:2px;bottom:2px;" name="element" >下一步</button>
	</div>
	<div id="cron_iframe_show"  style="width: 70%;float:left">
	</div>
</div>
<div id="cron_iframe" style="display:none;">
  <Iframe id="iframe_cron" src="${pageContext.request.contextPath}/jsp/plugins/Cron/cron.jsp"; style="width:100%;height:300px" scrolling="no" frameborder="0"></iframe> 
</div>
</body>
<script>
//切换到下一个tab页
function doClick(){
	layui.use('element', function(){
		  var $ = layui.jquery,
		  element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
	      //切换到指定Tab项
	      element.tabChange('demo', 'creatStep'); //切换到：步骤
	});
}
</script>
</html>