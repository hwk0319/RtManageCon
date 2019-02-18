<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>系统日志</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/My97DatePicker/WdatePicker.js"></script>
	<style>
		.ui-jqgrid-bdiv{overflow: hidden !important;}
		.ui-jqgrid .ui-jqgrid-caption{
			background-color: #7ab8dd;
		}
		.sysLog{
			width:98%;
			height:calc(100% - 120px);
			border: 1px solid #ddd;
			margin-left:15px;
			border-top-left-radius: 5px;
	    	border-top-right-radius: 5px;
	    	background-color: #fff;
		}
		.sysLogDiv{
			font-size: 13px;
			width:100%;
	 		height:calc(100% - 25px);
			overflow: auto;
			background: #f5f5f5;
			word-wrap:break-word;
		}
		.nodata {
			margin: 10% 40%;
			width: 20%;
			height: 30%;
			background: url('/RtManageCon/imgs/nodata.png') no-repeat;
			background-size: 100% 100%;
		}
		
		::-webkit-scrollbar {
            width: 10px; /*对垂直流动条有效*/
            height: 10px; /*对水平流动条有效*/
        }
        /*定义滚动条的轨道颜色、内阴影及圆角*/
        ::-webkit-scrollbar-track{
            -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
        }
       /*定义滑块颜色、内阴影及圆角*/
        ::-webkit-scrollbar-thumb{
            border-radius: 7px;
            -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
            background-color: #E8E8E8;
        }
	</style>
  </head>
  <body>
  	<fieldset class="layui-elem-field">
	  <legend>查询</legend>
	  <div class="layui-field-box">
	   	<div class="">
			<div class="prom-3">
				<div class="w_p30">
					<span class="txt">日期：</span>
					<input id="time_search" class="Wdate" type="text" readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd ',readOnly:'true',maxDate:'%y-%M-%d'})">
				</div>
				<div class="w_p30">
					<span class="txt">日志类型：</span>
					<select style="width:70%;font-size:12px;" id="systemLogType">
						<option value="">--请选择--</option>
					</select>
				</div>
				<div class="w_p30">
					<button onclick="dologSearch()" class="btn"><i class="icon-search"></i> 查询</button>
				</div>
			</div>
		</div>
	  </div>
	</fieldset>
	<div class="sysLog">
		<div class="title" style="height:25px;"><span style="color:#0e0e0e;">系统日志</span>
		</div>
		<div id="sysLog" class="sysLogDiv"></div>
	</div>
</body>
<script type="text/javascript">
  $(function(){
	  initSysSelect();
	  getSystemLog();
  });
  
  function dologSearch(){
	  getSystemLog();
  }
  
  function getSystemLog(){
	  var layid;
	  $.ajax({
			url : getContextPath() + "/systemlogs/logs",
			type : "post",
			data : {
				"date" : $("#time_search").val(),
				"systemLogType" : $("#systemLogType").val()
			},
			dataType : "json",
			beforeSend:function(){
	          	//加载层
				layid = layer.load(1, {
					  shade: [0.3,'#fff']
				});
	        }, 
			success : function(result) {
				if(result[0] == null){
					var strhtml="<div class='nodata'></div>";
					$("#sysLog").html(strhtml);
				}else{
					$("#sysLog").html("");
					for(var i=0;i<result.length;i++){
						var index = i+1;
						var log="<div style=''><div style='width: 50px;height:100%;float:left;'>&nbsp;"+index+". </div> <div style='margin-left: 50px;'><p>"+result[i]+"</p></div></div>";
						$("#sysLog").append(log);
					}
				}
				layer.close(layid);//手动关闭f
			},
			error : function() {
				console.log("ERROR");
			}
		});
  }
  
  function initSysSelect(){
		//获取数据字典值并显示在下拉框
			$.ajax({
				url:getContextPath()+"/operationlogCon/searchCon",
				type:'POST',
				data:{type:"systemLog_type"},
				dataType:'json',
				success:function(data){
					for(var i=0;i<data.length;i++)
					{
						$("#systemLogType").append("<option value='"+data[i].value+"'>"+data[i].name+"</option>");
					}
				}
			});
	  }
//窗口改变大小时重新设置Grid的宽度
  $(window).resize(function(){
	  $(".sysLog").css("height","70%");
  });
</script>
</html>
