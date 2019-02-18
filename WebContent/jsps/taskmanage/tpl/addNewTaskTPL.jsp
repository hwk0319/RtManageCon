<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script> 
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsLib/layui/layui.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/jsLib/layui/css/layui.css" media="all">
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>新任务模板</title>
  <style type="text/css">
  .h_auto{
  	overflow:auto;
  }
  .layui-tab-content,#taskTplPageDiv1,#taskTplPageDiv2,#taskTplPageDiv3{
  	height:100%;
  }
  body,html{
  	overflow:hidden;
  	height:100%;
  }
  #movehand{
  	height:100%;
  }
  .layui-tab-content{
  	height:85%;
  }
  #movehand ~ .layui-layer.layui-layer-page.layer-anim .layui-layer-setwin{
   position: absolute !important;
    right: 15px !important;
    top: 12px !important;
    font-size: 0 !important;
    line-height: initial !important;
  }
  
   #movehand ~ .layui-layer.layui-layer-page.layer-anim .layui-layer-setwin a{
    position: relative !important;
    width: 20px !important;
    height: 20px !important;
    margin-left: 10px !important;
    font-size: 12px !important;
    _overflow: hidden !important;
}

 #movehand ~ .layui-layer.layui-layer-page.layer-anim .layui-layer-ico{
    background-size: 140px !important;
    background-position: -40px 0 !important;
}
.ui-jqgrid .ui-jqgrid-caption{
		background-color: #7ab8dd;
	}
  </style>
</head>
</head>
<body>
	<div id="movehand" class="layui-tab" lay-filter="demo"">
		<ul class="layui-tab-title">
			<li  lay-id="creatModul" class="layui-this">创建模板</li>
			<li>步骤</li>
			<li>参数</li>
		</ul>
		<div  class="layui-tab-content">
			<div id="taskTplPageDiv1" class="layui-tab-item h_auto layui-show"> </div>
			<div id="taskTplPageDiv2" class="layui-tab-item h_auto "> </div>
			<div id="taskTplPageDiv3" class="layui-tab-item h_auto"> </div>
		</div>
	</div>
<script>
$(function(){
  layui.use(['layer', 'laypage', 'element'], function() {
    layer = layui.layer, laypage = layui.laypage, element = layui.element();
    if(parent.oper=="add")
    {
 		 $("#taskTplPageDiv1").load(getContextPath() + "/jsps/taskmanage/tpl/createTaskTpl.jsp");
    }
  	else
    {
   	 	element.tabDelete('demo', 'creatModul');
   		$("#taskTplPageDiv2").load(getContextPath() + "/jsps/taskmanage/tpl/creatNewStepTPL.jsp");
  		$("#taskTplPageDiv3").load(getContextPath() + "/jsps/taskmanage/tpl/creatNewParamTPL.jsp");
    }
  });

  
});
  var hasConfirm = false;//是否点击过下一步按钮
	  layui.use(['layer', 'laypage', 'element'], function() {
    var layer = layui.layer, laypage = layui.laypage, element = layui.element();

    //监听Tab切换
    element.on('tab(demo)', function(data) {
      if (data.index == 0) {
        //$("#taskTplPageDiv1").load(getContextPath() + "/jsps/taskmanage/tpl/createTaskTpl.jsp");
      } else if (data.index == 1 && hasConfirm) {
        $("#taskTplPageDiv2").load(getContextPath() + "/jsps/taskmanage/tpl/creatNewStepTPL.jsp");
      } else if (data.index == 2 && hasConfirm) {
       	$("#taskTplPageDiv3").load(getContextPath() + "/jsps/taskmanage/tpl/creatNewParamTPL.jsp");
      }
      else{
        //$("#taskTplPageDiv").html("");
      }
    });

  });
</script>
</body>
</html>   
