<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<!-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/plugins/jquery/css/jquery-ui-redmod.css"> -->
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<style>
/* 	*::-webkit-scrollbar { */
/* 	    width: 5px; */
/* 	    background-color: #F5F5F5; */
/* 	} */
/* 	*::-webkit-scrollbar-thumb { */
/* 	    background-color: #c1c1c1; */
/* 	} */
	.ui-jqgrid .ui-jqgrid-caption {
	    background-color: #7ab8dd;
	}
</style>
<title>指标分类</title>
</head>
<body>
    <div style="width: 100%;height: 50%;overflow: hidden;    height: 330px;">
       <div id="indextype" style="width:calc(100%/2 - 12px);float: left;margin-right:10px;">
    	    <table id="jqGridListIndextype"></table>
	    </div>
	    <div id="index" style="width:calc(100%/2 - 2px);overflow:hidden;">
	        <table id="jqGridListIndex"></table>   
	    </div>
    </div>
    <div style="width: 100%;height: 40px;">
		<p style="text-align:center;">
			<button class="btn" onclick="doOk()"><i class="icon-ok"></i> 确定</button>
			<button id="close" onclick="closeBut()" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
	<input type="hidden" value="" id="trIndex"/>
	<input type="hidden" value="" id="index_ids"/>
</body>
<script type="text/javascript">
  $(function(){
	  //获取URL传过来的参数
	  var trIndex=$.getUrlParam('trIndex');
	  var index_ids=$.getUrlParam('index_ids');
	  $("#trIndex").val(trIndex);
	  $("#index_ids").val(index_ids);
	    var hei=$("#indexList iframe",window.parent.document).height()-60;
	    $("#index,#indextype").css({height:hei+"px"});
		creatindexTypeGrid();
		creatindexGrid(1111111);
		var index=$("#indextype table:eq(0) thead #jqGridListIndextype_indextype_id").index();
		$("#jqGridListIndextype tbody").on("click","tr",function(){
		       var indextype_id=$(this).find("td:eq("+index+")").html();
		       $.jgrid.gridUnload("#jqGridListIndex");
			   creatindexGrid(indextype_id);
		});
  });
  function creatindexTypeGrid(){
		jQuery("#jqGridListIndextype").jqGrid({
			url:getContextPath()+"/indextypeCon/search",
			datatype:'json',//请求数据返回的类型
			postData:{},
			colModel:[
					   {label:"id",name:"id",width:0,key:true,hidden:true},
					   {label:"指标分类ID",name:"indextype_id",index:"indextype_id",width:50,align:"center"},
					   {label:"名称",name:"name",index:"name",width:50,align:"center"}
					],
				loadonce:false,
				rownumbers: true,
				sortname:"id",//初始化的时候排序的字段
				sortorder:"desc",//排序方式
				caption:"<div>指标分类</div>",
				height:"250px",
				autowidth:true,
				pager : ""
		});
	}
	function creatindexGrid(index_type){
		var index_ids=$("#index_ids").val();
		jQuery("#jqGridListIndex").jqGrid({
			url:getContextPath()+"/healthmodelCon/getindexinfo",
			datatype:'json',//请求数据返回的类型
			postData:{index_ids:index_ids,'index_type':index_type},
			colModel:[
					   {label:"指标项ID",name:"model_id",index:"model_id",width:50,align:"center",},
					   {label:"描述",name:"model_desc",index:"model_desc",width:50,align:"center"}
					],
				loadonce:false,
				rownumbers: true,
				rowNum:1000,//一页显示多少条
				sortname:"index_id",//初始化的时候排序的字段
				sortorder:"desc",//排序方式
				caption:"<div>指标项</div>",
				height:"250px",
				autowidth:true,
				pager : ""
		});
	}
	//窗口改变大小时重新设置Grid的宽度
	$(window).resize(function(){
		var width = document.body.clientWidth;
		var GridWidth = (width-230-20)*0.99;
		$("#jqGridListIndextype").setGridWidth(GridWidth);
		$("#jqGridListIndex").setGridWidth(GridWidth);
	});
	function closeBut(){
		parent.layer.closeAll();
	}
	function doOk(){
	    var index1=$("#index table:eq(0) thead #jqGridListIndex_model_id").index();
	    var index2=$("#index table:eq(0) thead #jqGridListIndex_model_desc").index();
	    var index_id=$("#jqGridListIndex tbody .ui-state-highlight").find("td:eq("+index1+")").html();
	    var description=$("#jqGridListIndex tbody .ui-state-highlight").find("td:eq("+index2+")").html();
	    if(index_id!=="" && typeof(index_id)!=="undefined"){
	    	var trIndex=$("#trIndex").val();
	    	$("#indexList", parent.document).parent().parent().find(".div_body #item_metric #jqGridListMetric tr:eq("+trIndex+") #metric_id input").removeAttr("value");
	        $("#indexList", parent.document).parent().parent().find(".div_body #item_metric #jqGridListMetric tr:eq("+trIndex+") #metric_id input").attr("value",index_id);
	        $("#indexList", parent.document).parent().parent().find(".div_body #item_metric #jqGridListMetric tr:eq("+trIndex+") #metric_id_name input").val(description);
	        closeBut();
	    }else{
	        layer.alert("请选择指标项!");
	    }
	}
</script>
</html>
