<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<!-- 编辑使用 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/layui/css/layui.css"/>
<!--这个是所有jquery插件的基础，首先第一个引入-->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/plugins/jquery/css/jquery-ui-redmod.css">
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/healthScore/js/heathScore.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/layui/layui.js"></script>
<style>
*::-webkit-scrollbar-track
{
  -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
  background-color: #F5F5F5;
}

*::-webkit-scrollbar
{
  width: 5px;
  background-color: #F5F5F5;
}

*::-webkit-scrollbar-thumb
{
   background-color: #c1c1c1;
}

body,html {
	margin: 0;
	padding: 0;
	width: 100%;
	height: 100%;
	overflow: auto;
}

table,table tr th,table tr td {
	border: 1px solid #d1d5de;
}

table {
	min-height: 25px;
	line-height: 25px;
	text-align: center;
	border-collapse: collapse;
}

.nodata {
	margin: 10% 40%;
	width: 20%;
	height: 20%;
	background: url('../healthScore/img/nodata.png') no-repeat;
	background-size: 100% 100%;
	margin-top: 20%;
}
#itemScore{
	width: 99%;
	height: auto;
    margin-left: 0.5%;
    margin-top: 5px;
}

body .winning-class {
/*     width: 80px; */
/*     height: 100px; */
/*     overflow: hidden; */
}
.layui-layer-dialog .layui-layer-content .layui-layer-ico {
    top: 16px;
    left: 15px;
    width: 50px;
    height: 50px;
}
.layui-layer-dialog .layui-layer-content {
    position: relative;
    line-height: 24px;
    word-break: break-all;
    overflow: hidden;
    font-size: 18px;
    overflow-x: hidden;
    margin-left: 25px;
}
</style>
<title>优化建议</title>
</head>
<body>
	<div id="itemScore" class="layui-collapse">
		<!-- 存放指标数据 -->
	</div>
</body>
<script type="text/javascript">
  $(function(){
	  var id = $.getUrlParam('id');
	  getModelItem(id);
  });
  
  /**
   * 获取健康评估对应模型的分项信息
   */
  function getModelItem(id){
	var layid;
  	$.ajax({
  		url:getContextPath()+"/heathcheckCon/getItemScoer",
  		type:"post",
  		datatype:"json",
  		data:{health_check_id:id},
  		// 请求发送之前
  		beforeSend:function(){
          	//加载层
            layid = layer.msg("正在加载，请稍后 ······", {
              skin:'winning-class',//自定义样式winning-class
			  icon: 16,
			  time: false, //取消自动关闭,
			  shade: 0.15,
			});
        }, 
  		success:function(res){
  			if(res != "" && res.length > 0){
	  			$("#itemScore").html(res);
	  			//折叠面板
	  			layui.use('element', function(){
	  			  var element = layui.element();
	  			});
  			}else{
  				$("#itemScore").removeClass("layui-collapse");
  				$("#itemScore").css("height","100%");
  				var strhtml="<div class='nodata'></div>";
  				$("#itemScore").html(strhtml);
  			}
  		},
  		// 请求完成后的回调函数 (请求成功或失败之后均调用)
        complete:function(){
        	layer.close(layid);//手动关闭
        }, 
  	});
  }
</script>
</html>