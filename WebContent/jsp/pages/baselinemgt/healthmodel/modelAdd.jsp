<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/healthmodel/healthmodel.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<style type="text/css">
	.div_head{border: 1px solid #f5f5f5;height:50px;border-bottom:1px solid #ccc;}
	.div_head .nav-tabs li{list-style: none;float: left;}
	.div_head .nav-tabs li a{
		font-size: 14px;
		color: #999898;
		background:#fff;
		padding: 10px 15px;
		border: 1px solid #ddd;
		position: relative;
		text-decoration:none
	}
	.div_head .nav-tabs li a:hover{background:#F0FFF0;}
	.div_head .nav-tabs li.active a{
		color: #fff;
		border: none;
		background: #3C8DBC;
	}
	.div_head .nav-tabs li.active a:after{
		content: "";
		border-top: 10px solid #3C8DBC;
		border-left: 10px solid transparent;
		border-right: 10px solid transparent;
		position: absolute;
		bottom: -10px;
		left: 43%;
	}
	.div_body{width: 100%;height:calc(100% - 145px);overflow: hidden;}
	.tab_active{display: none;}
	#health_model,#model_item,#item_metric{
	   width:98%;
	   height:100%;
	   margin:0 auto;
	   margin-top:10px;
	   overflow:auto;
	}
	.div_body table{width: 100%;font-size: 13px;}
	#item_metric{overflow: hidden;}
	#item_metric .Item_div{
	   width: 15%;
	   height:97%;
	   overflow:hidden;
	   border: 1px solid #ccc;
	   margin-right:5px;
	   float: left;
	   border-radius: 5px;
	}
	#item_metric .item_title{width: 100%;height: 25px;background:#7ab8dd;text-align: center;color: #fff;font-size: 15px;line-height: 25px;text-indent: 2px;}
	#item_metric .item_body{
	    width: 100%;
	    height:calc(100% - 25px);
	    overflow:hidden;
	    overflow-y:auto;
	    color:#222222;
	}
	#item_metric .item_body span{
	    font-size: 14px;
	    text-indent: 2px;
	    width: 100%;
	    float: left;
	    line-height: 23px;
	    overflow: hidden;
	    white-space: nowrap;
	}
	#item_metric .item_body span:hover{background:rgb(255, 251, 158);cursor: pointer;}
	.spanbgcol{background:rgb(255, 251, 158);}
	#item_metric .Metric_div{
	   width: calc(100% - 15% -5px);
	   height: 97%;
	   overflow:hidden;
	   overflow-y:auto;
	   border: 1px solid #ccc;
	   border-radius: 5px;
	}
	.sethidden{display: none;}
	.ui-state-highlight, .ui-widget-content .ui-state-highlight, .ui-widget-header .ui-state-highlight{
	    background: #fff;
	    border:1px solid #a6c9e2;
	}
	*::-webkit-scrollbar {
	    width: 5px;
	    background-color: #F5F5F5;
	}
	*::-webkit-scrollbar-thumb {
	    background-color: #c1c1c1;
	}
	.ui-jqgrid .ui-jqgrid-caption {
	    background-color: #7ab8dd;
	}
	</style>
	<title>健康模型</title>
  </head>
  <body>
     <div class="div_head">
		<ul class="nav-tabs">
			<li class="active"><a href="javascript:void(0);">健康模型</a></li>
			<li><a href="javascript:void(0);">模型分项</a></li>	
			<li><a href="javascript:void(0);">模型分项指标</a></li>			
		</ul>
	 </div>
	 <div class="div_body">
	     <!-- 健康模型-start -->
	     <div id="health_model">
	          <table style="padding: 20px;">
				<tbody>
					<tr>
						<td>模型名称：</td>
						<td><input type="text" id="model_name" style="width: 182px;" validate="nn nspcl" maxlength="15" placeholder="请输入模型名称"/></td>
						<td>描述：</td>
						<td><input type="text" id="model_desc" style="width: 182px;" validate="nspcl" maxlength="50" placeholder="请输入描述"></td>
					</tr>
					<tr>
						<td>总分：</td>
						<td><input type="text" readonly="readonly" id="total_score" style="width: 182px;" placeholder="请输入总分"></td>
					</tr>
					<input type="hidden" id="model_id" value=""/>
				</tbody>
			</table>
	     </div>
	     <!-- 健康模型-end -->
	     
	     <!-- 模型分项-start -->
	     <div id="model_item" class="tab_active">
	         <table id="jqGridListItem"></table>
	     </div>
	     <!-- 模型分项-end -->
	     
	     <!-- 模型分项指标-start -->
	     <div id="item_metric" class="tab_active">
	          <div class="Item_div">
	            <div class="item_title">模型分项</div>
	            <div class="item_body">
	            </div>
	          </div>
	          <div class="Metric_div">
	          	<table id="jqGridListMetric"></table>
	          </div>
	     </div>
	     <!-- 模型分项指标-end -->
	 </div>
	
	<div class="widget-content">
	   <hr class="ui-widget-content" style="margin:1px">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave(this)"><i class="icon-ok"></i> 保存</button>
			<button id="close" onclick="btnClose()" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
	<input type="hidden" id="tr_index" value=""/>
	<input type="hidden" id="status" value=""/>
  </body>
  <script type="text/javascript">
     $(function(){
    	 //获取URL传过来的参数
	   	 var model_id=$.getUrlParam('model_id');
	   	 $("#model_id").val(model_id);
	   	 var tr_index=$.getUrlParam('tr_index');
	   	 $("#tr_index").val(tr_index);
         searchModel($("#health_model #model_id").val());//若是编辑时初始化健康模型数据
         var status = $.getUrlParam("status");
         $("#status").val(status);
         $(".nav-tabs").on("click","li",function(){
              var index=$(this).index();
              //健康模型
              if(index==0){
                 $("#health_model").removeClass("tab_active");
                 $("#model_item").addClass("tab_active");
                 $("#item_metric").addClass("tab_active");
                 $(".nav-tabs li").removeClass("active");
            	 $(this).addClass("active");
              }
              //模型分项
              else if(index==1){
                 var model_name=$("#model_name").val().trim();
                 if(model_name===""){
                     layer.alert("请先维护模型名称！");
                     return;
                 }else{
                 	 $("#model_item").removeClass("tab_active");
	                 $("#health_model").addClass("tab_active");
	                 $("#item_metric").addClass("tab_active");
	                 creatItemGrid();
	                 $(".nav-tabs li").removeClass("active");
            	 	 $(this).addClass("active");
                 }
              }
              //模型分项指标
              else if(index==2){
             	 var model_name=$("#model_name").val().trim();
                 if(model_name===""){
                     layer.alert("请先维护模型名称！");
                     return;
                 }else{
                 	 if($("#jqGridListItem tbody tr:not(.jqgfirstrow)").length==0){
	                 	 $(".nav-tabs li:eq(1)").click();
					 }
	                 if(loaditemBody()){
	                     $("#item_metric").removeClass("tab_active");
		                 $("#model_item").addClass("tab_active");
		                 $("#health_model").addClass("tab_active");
	                     creatMetricGrid();
	                     $(".nav-tabs li").removeClass("active");
            	 		 $(this).addClass("active");
	                 }
                 }
              }
         });
         $("#item_metric .item_body").on("click","span",function(){
              $("#item_metric .item_body span").removeClass("spanbgcol");
              $(this).addClass("spanbgcol");
              getmetricinfo($(this).attr("id"));
         });
     });
  </script>
</html>
