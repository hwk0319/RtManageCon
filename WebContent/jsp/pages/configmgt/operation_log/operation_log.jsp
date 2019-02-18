<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/My97DatePicker/WdatePicker.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/operation_log/js/operation_log.js"></script>
	<style>
		.ui-jqgrid-bdiv{overflow: hidden !important;}
		.ui-jqgrid .ui-jqgrid-caption{
			background-color: #7ab8dd;
		}
	</style>
	<title>操作日志</title>
  </head>
  <body>
  	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3" style="margin-left: 7%;">
				<div class="w_p30">
					<span class="txt">操作日期：</span>
					<input id="time_search" class="Wdate" type="text" readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd ',readOnly:'true',maxDate:'%y-%M-%d'})">
				</div>
				<div class="w_p30">
					<span class="txt">操作人员:</span>
					<input id="name_search">
				</div>
				<div class="w_p30">
					<span class="txt">事件等级：</span>
					<select style="width:70%;font-size:12px;" id="levels">
						<option value="">--请选择--</option>
						<option value="0">日志</option>
						<option value="1">轻微</option>
						<option value="2">一般</option>
						<option value="3">严重</option>
					</select>
				</div>
			</div>
			<div class="prom-3" style="margin-left: 7%;">
				<div class="w_p30">
					<span class="txt">操作类型：</span>
					<select style="width:70%;font-size:12px;" id="optType">
						<option value="">--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<span class="txt">事件类型：</span>
					<select style="width:70%;font-size:12px;" id="type">
						<option value="">--请选择--</option>
						<option value="1">业务事件</option>
						<option value="0">系统事件</option>
					</select>
				</div>
				<div class="w_p30">
					<button onclick="dologSearch()" class="btn" style=""><i class="icon-search"></i> 查询</button>
<!-- 					<button onclick="dologCount()" class="btn" id='countid'><i class="icon-list-alt"></i> 统计</button> -->
<!-- 					<button onclick="exportExcel()" class="btn" id='exportid'><i class="icon-share"></i> 导出Excel</button> -->
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListlog"></table>
		<div id="jqGridPaperListlog"></div>
	</div>
</body>
<script type="text/javascript">
  $(function(){
	isOff();
	initSelect();
  });
  
  function initSelect(){
	//获取数据字典值并显示在下拉框
		$.ajax({
			url:getContextPath()+"/operationlogCon/searchCon",
			type:'POST',
			data:{type:"oprt_type"},
			dataType:'json',
			success:function(data){
				for(var i=0;i<data.length;i++)
				{
					$("#optType").append("<option value='"+data[i].value+"'>"+data[i].name+"</option>");
				}
			}
		});
  }
  
  //日志统计
  function dologCount(){
	  layer.open({
	  	    type: 2,
	  	    title: '统计', 
	  	    fix: false,
	  	    shadeClose: false,
	  	    area: ['70%', '80%'],
	  	    content: [getContextPath()+"/jsp/pages/configmgt/operation_log/logCount.jsp",'no']
	  	});
  }
  
  //导出Excel
  function exportExcel(){
	  var index;
	  $.ajax({
			url:getContextPath()+"/operationlogCon/search",
			type:'POST',
			data:{
				'time' : $("#time_search").val(),
				'oprt_user' : $("#name_search").val(),
				'oprt_type' : $("#optType").val(),
				'type' : $("#type").val(),
				'levels' : $("#levels").val()
			},
			dataType:'json',
			beforeSend: function () {
	  			index = layer.load(1, {
	  			  shade: [0.3,'#fff']
	  			});
	  	    },
	  	    complete:function () {
	  	    	layer.close(index);
		    },
			success:function(data){
				if(data != null && data.length > 0){
					location.href = getContextPath()+"/operationlogCon/exportExcel";
				}else{
					layer.alert("没有需要导出的数据！");
				}
			}
		});
  }
  
  //判断日志开关是否要显示日志
  function isOff(){
	    //获取数据字典值
		$.ajax({
			url:getContextPath()+"/operationlogCon/searchCon",
			type:'POST',
			data : {
						type : "operlogOff"
					},
			dataType : 'json',
			async : false,
			success : function(data) {
				if(data[0].value == "off"){
					operlogOff();
					//禁用按钮
					$("#countid").attr("disabled","disabled");
					$("#exportid").attr("disabled","disabled");
				}else{
					initlogfunction();
				}
			}
		});
  }
  
  //不显示日志数据
  function operlogOff(){
		jQuery("#jqGridListlog").jqGrid({
			url:getContextPath()+"/operationlogCon/search",
			datatype:'local',
			postData : {
				'time' : $("#time_search").val(),
				'oprt_user' : $("#name_search").val(),
				'oprt_type' : $("#optType").val(),
				'type' : $("#type").val(),
				'levels' : $("#levels").val()
			},
			colModel:[
			          {label:'id',name:'id',width:0,key:true,hidden:true},
					   {label:'oprt_id',name:'oprt_id',width:0,key:false,hidden:true},
					   {label:'名称',name:'module',index:'module', width:100,align:'center'},
					    {label:'操作类型',name:'oprt_type',index:'oprt_type', width:50,align:'center'},
					    {label:'操作时间',name:'time',index:'time', width:100,align:'center'},
					    {label:'操作人员',name:'oprt_user',index:'oprt_user', width:70,align:'center'},
					    {label:'操作内容',name:'oprt_content',index:'oprt_content', width:100,align:'center'},
					    {label:'操作人主机IP',name:'ip',index:'ip', width:100,align:'center'},
					    {label:'是否成功',name:'flag',index:'flag', width:50,align:'center',
					    	formatter:function(cellvalue, options, rowObject){
			                   	var indexname="";
			                    if(cellvalue == "0"){
			                    	indexname="失败";
			                    }else if(cellvalue == "1"){
			                    	indexname="成功";
			                    }else{
			                    	indexname="成功";
			                    }
			                   	return indexname;
		                    }
					    },
					    {label:'事件等级',name:'levels',index:'levels', width:50,align:'center',
					    	formatter:function(cellvalue, options, rowObject){
					    		var indexname="";
					    		if(cellvalue == "1"){
					    			indexname="轻微";
					    		}else if(cellvalue == "2"){
					    			indexname="一般";
					    		}else if(cellvalue == "3"){
					    			indexname="严重";
					    		}else{
					    			indexname="日志";
					    		}
					    		return indexname;
					    	}
					    },
					    {label:'事件类型',name:'type',index:'type', width:50,align:'center',
					    	formatter:function(cellvalue, options, rowObject){
					    		var indexname="";
					    		if(cellvalue == "0"){
					    			indexname="系统事件";
					    		}else if(cellvalue == "1"){
					    			indexname="业务事件";
					    		}else{
					    			indexname="业务事件";
					    		}
					    		return indexname;
					    	}
					    }
					],
				rownumbers: true,
//				altRows:true,
				sortname: 'time',
				sortable:true,
				loadonce:true,
				rowNum:100,//一页显示多少条
				caption:"<div style='color:#000;width:95%;'>操作日志<span style='cursor:pointer;float:right;' onclick='addrl()'> <i class='icon-plus-sign'></i>&nbsp;设置存储容量</span></div>",
				rowList:[100,200,300],//可供用户选择一页显示多少条
				sortname:'id',//初始化的时候排序的字段
				sortorder:'desc',//排序方式
				mtype:'post',//向后台请求数据的Ajax类型
				viewrecords:true,//定义是否要显示总记录数
				height:'auto',
				autowidth:true,
				pager : "#jqGridPaperListlog",
		});
	}
</script>
</html>
