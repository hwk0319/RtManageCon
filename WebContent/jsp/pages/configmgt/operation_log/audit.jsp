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
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jquery/jquery-1.8.3.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<style>
	.ui-jqgrid-bdiv{overflow: hidden !important;}
	.ui-jqgrid .ui-jqgrid-caption{
		background-color: #7ab8dd;
	}
	.w_p30{
		margin-left: 2%;
	}
	.w_p30 select{
		width: 65%; 
		font-size: 13px;
	}
	</style>
	<title>审计策略</title>
  </head>
  <body>
  	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">一级菜单：</span>
					<select id="name">
						<option value="">--请选择--</option>
						<option value="1">配置管理</option>
						<option value="2">任务管理</option>
						<option value="3">异常告警</option>
						<option value="4">系统优化</option>
						<option value="5">日志分析</option>
						<option value="6">系统管理</option>
					</select>
				</div>
				<div class="w_p30">
					<span class="txt">二级菜单:</span>
					<select id="erjiname">
						<option value="">--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<button onclick="dologSearch()" class="btn" style="margin-left: 25%;"><i class="icon-search"></i> 查询</button>
				</div>
			</div>
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">是否审计：</span>
					<select id="isAudit">
						<option value="">--请选择--</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div id="main_list" style="margin:1%">
		<table id="jqGridListaudit"></table>
		<div id="jqGridPaperListaudit"></div>
	</div>
</body>
<script type="text/javascript">
  $(function(){
	  creatlogGrid();
	  
	  $("#name").change(function(){
		  var val = $(this).val();
		  var htm = "";
		  if(val == "1"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='设备管理'>设备管理</option>";
			  htm += "<option value='软件系统管理'>软件系统管理</option>";
			  htm += "<option value='组管理'>组管理</option>";
			  htm += "<option value='双活管理'>双活管理</option>";
			  htm += "<option value='设备型号管理'>设备型号管理</option>";
			  htm += "<option value='设备监控管理'>设备监控管理</option>";
			  htm += "<option value='告警规则管理'>告警规则管理</option>";
			  htm += "<option value='通知规则管理'>通知规则管理</option>";
			  htm += "<option value='指标分类'>指标分类</option>";
			  htm += "<option value='联系人管理'>联系人管理</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "2"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='任务列表'>任务列表</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "3"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='异常故障'>异常故障</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "4"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='健康评分'>健康评分</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "5"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='操作日志'>操作日志</option>";
			  htm += "<option value='系统日志'>系统日志</option>";
			  htm += "<option value='审计策略'>审计策略</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "6"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='菜单管理'>菜单管理</option>";
			  htm += "<option value='角色管理'>角色管理</option>";
			  htm += "<option value='数据字典'>数据字典</option>";
			  $("#erjiname").html(htm);
		  }else{
			  htm = "";
			  htm = "<option value=''>--请选择--</option>";
			  $("#erjiname").html(htm);
		  }
	  });
  });
  
  function creatlogGrid(){
	  	var name = $("#name").val();
	   	if(name == "1"){
	   		name = "配置管理";
	   	}else if(name == "2"){
	   		name = "任务管理";
	   	}else if(name == "3"){
	   		name = "异常告警";
	   	}else if(name == "4"){
	   		name = "系统优化";
	   	}else if(name == "5"){
	   		name = "日志分析";
	   	}else if(name == "6"){
	   		name = "系统管理";
	   	}
		jQuery("#jqGridListaudit").jqGrid({
			url:getContextPath()+"/operationlogCon/searchAudit",
			datatype:'json',
			postData : {
				'name' : name,
				'erjiname' : $("#erjiname").val(),
				'isAudit' : $("#isAudit").val()
			},
			colModel:[
			          {label:'id',name:'id',width:0,key:true,hidden:true},
					   {label:'一级菜单',name:'name',index:'module', width:80,align:'center'},
					   {label:'二级菜单',name:'erjiname',index:'module', width:80,align:'center'},
					    {label:'是否审计',name:'isAudit',index:'oprt_type', width:50,align:'center',
					    	formatter:function(cellvalue, options, rowObject){
			                   	var indexname="";
			                    if(cellvalue == "1"){
			                    	indexname="是";
			                    }else{
			                    	indexname="否";
			                    }
			                   	return indexname;
		                    }},
					    {label:'审计内容',name:'auditThose',index:'time', width:100,align:'center'},
// 					    {label:'备注',name:'remark',index:'oprt_user', width:100,align:'center'},
					    {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
					],
				rownumbers: true,
//				altRows:true,
				sortname: 'time',
				sortable:true,
				loadonce:true,
				rowNum:100,//一页显示多少条
				caption:"<div style='color:#000;width:95%;'>审计策略<span style='cursor:pointer;float:right;' onclick='doInserta();'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
				rowList:[100,200,300],//可供用户选择一页显示多少条
				sortname:'id',//初始化的时候排序的字段
				sortorder:'desc',//排序方式
				mtype:'post',//向后台请求数据的Ajax类型
				viewrecords:true,//定义是否要显示总记录数
				height:'auto',
				autowidth:true,
				pager : "#jqGridPaperListaudit",
				gridComplete: function(){
		            var ids = $("#jqGridListaudit").getDataIDs();
		            for(var i=0;i<ids.length;i++){
		                var cl = ids[i];
		                ed = "<a href='javascript:void(0)' title='编辑' onclick='doInserta("+cl+");'>编辑 </a>";
		                vi = "<a href='javascript:void(0)' title='删除' onclick='doDeletea("+cl+");'>删除</a>"; 
		                jQuery("#jqGridListaudit").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
		            } 
		        }
		});
	}
	//窗口改变大小时重新设置Grid的宽度
	$(window).resize(function(){
		var width = document.body.clientWidth;
		var GridWidth = (width-250);
		$("#jqGridListaudit").setGridWidth(GridWidth);
		var height = document.body.clientHeight;
		$('#tt')[0].style.height = height - 50  + "px";
	});

	/**
	 * 查询
	 */
	function dologSearch(){
		$.jgrid.gridUnload("#jqGridListaudit");
		creatlogGrid();
	}
	
	//添加审计策略
	function doInserta(id){
		getJWTStrs();
		layer.open({
		    type: 2,
		    title: "审计策略", 
		    fix: false,
		    shadeClose: false,
		    area: ['500px','320px'],
// 		    area: ['37%','50%'],
		    moveOut: true,
		    content: [getContextPath()+"/jsp/pages/configmgt/operation_log/addAudti.jsp?id="+id,'no']
		});
	}
	
	//删除
	function doDeletea(id){
		var jwt = getJWTStrs();
		var oper = "del";
		layer.confirm("确定删除这条数据？",{
			btn:['确定','取消']
		},function(){
			$.ajax({
		  		url : getContextPath()+"/operationlogCon/update",
		  		type : "post",
		  		dataType : 'json',
		  		async: false ,
		  		data : {
		  			"oper" : oper,
		  			"id" : id,
	      			"jwt" : jwt
		  		},
		  		success : function(result) {
		  			if(result == "-2"){
	    				layer.alert("保存失败，请不要重复提交！");
	    				layer.close(index);
	    			}else{
	    				layer.msg('删除成功！');
						window.parent.dologSearch();
	    			}
		  		},
		  		error : function() {
		  			layer.msg('删除失败！');
		  		}
		  	});	
		});
	}
	
	//获取JWT
	function getJWTStrs(){
		var res;
		$.ajax({
			url:getContextPath()+"/commonConLog/createJWT",
			type:"post",
			data:{},
			datatype:"json",
			async:false,
			success:function (data){
				$("#jwt").val(data);
				res = data;
			}
		});
		return res;
	}
</script>
</html>
