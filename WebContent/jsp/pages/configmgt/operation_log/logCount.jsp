<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>操作日志</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!-- 编辑使用 -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
	<!--这个是所有jquery插件的基础，首先第一个引入-->
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/operation_log/js/operation_log.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/echarts.js"></script>
  <style>
  	table,table tr th,table tr td {
		border: 1px solid #d1d5de;
	}
	table {
		min-height: 25px;
		line-height: 25px;
		text-align: center;
		border-collapse: collapse;
		font-size: 12px;
	}
  </style>
  </head>
<body>
	<div style="font-size:13px;margin-left: 5px;">时间周期：
		<select id="zhouqi">
			<option value=''>--请选择--</option>
			<option value='7s'>一周内</option>
			<option value='30s'>一月内</option>
			<option value='365s'>一年内</option>
		</select>
	</div>
	<div id="countChart" style="width:50%;height:90%;float: left;position: relative;">
	
	</div>
	<div id="countChart1" style="width:50%;height:90%;float: left;">
	
	</div>
</body>
<script type="text/javascript">
$(function(){
	getEcharts();
	getEcharts1();
	
	$("#zhouqi").change(function(){
		getEcharts();
		getEcharts1();
	});
});
  
  function getEcharts(){
	  var typeCount = [];
	  $.ajax({
			url:getContextPath()+"/operationlogCon/searchTypeCounts",
			type:'POST',
			data:{
				"zhouqi" : $("#zhouqi").val()
			},
			dataType:'json',
			async:false,
			success:function(data){
				typeCount[0]={value:data[0].systems, name:'系统事件'};
				typeCount[1]={value:data[0].business, name:'业务事件'};
			}
		});
		//展示折线图
		var countChart = echarts.init(document.getElementById('countChart'));
		var option = {
			    title : {
// 			        text: '某站点用户访问来源',
			        //subtext: '纯属虚构',
// 			        x:'center'
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        left: 'left',
			        data: ['系统事件','业务事件']
			    },
			    series : [
			        {
			            name: '类型',
			            type: 'pie',
			            radius : '35%',
			            center: ['50%', '60%'],
			            label: {
			                normal: {
			                    formatter: '{b} ({c})',
			                    backgroundColor: '#eee',
			                    borderColor: '#aaa',
			                    borderWidth: 1,
			                    borderRadius: 4,
			                    rich: {
			                        b: {
			                            fontSize: 20,
			                            lineHeight: 33
			                        }
			                    }
			                }
			            },
			            data: typeCount,
			            itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ],
			    color:['#d48265','#91c7ae'],
			};
		countChart.setOption(option);
  }
  function getEcharts1(){
	  var type = ['查询','登录','注销','新增','编辑','删除','统计','导出Excel','越权访问','IP地址变动过大','连续登陆失败','其它'];
	  var typeCount = [];
	  $.ajax({
			url:getContextPath()+"/operationlogCon/searchCounts",
			type:'POST',
			data:{
				"zhouqi" : $("#zhouqi").val()
			},
			dataType:'json',
			async:false,
			success:function(data){
				var other = parseInt(data[0].counts) - parseInt(data[0].logins) - parseInt(data[0].logout) - parseInt(data[0].adds) - parseInt(data[0].edit) - parseInt(data[0].deletes) - parseInt(data[0].yuequan) - parseInt(data[0].ipChange) - parseInt(data[0].loginFail) - parseInt(data[0].searchs)- parseInt(data[0].doCounts)- parseInt(data[0].exports);
				typeCount[0] = {value:data[0].searchs, name:'查询'};
				typeCount[1] = {value:data[0].logins, name:'登录'};
				typeCount[2] = {value:data[0].logout, name:'注销'};
				typeCount[3] = {value:data[0].adds, name:'新增'};
				typeCount[4] = {value:data[0].edit, name:'编辑'};
				typeCount[5] = {value:data[0].deletes, name:'删除'};
				typeCount[6] = {value:data[0].doCounts, name:'统计'};
				typeCount[7] = {value:data[0].exports, name:'导出Excel'};
				typeCount[8] = {value:data[0].yuequan, name:'越权访问'};
				typeCount[9] = {value:data[0].ipChange, name:'IP地址变动过大'};
				typeCount[10] = {value:data[0].loginFail, name:'连续登陆失败'};
				typeCount[11] = {value:other, name:'其它'};
			}
		});
		var countChart = echarts.init(document.getElementById('countChart1'));
		var option = {
			    title : {
// 			        text: '某站点用户访问来源',
			        //subtext: '纯属虚构',
// 			        x:'center'
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        orient: 'horizontal',
// 			        left: 'left',
			        data: type //['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
			    },
			    series : [
			        {
			            name: '类型',
			            type: 'pie',
			            radius : '35%',
			            label: {
			                normal: {
			                    formatter: '{b} ({c})',
			                    backgroundColor: '#eee',
			                    borderColor: '#aaa',
			                    borderWidth: 1,
			                    borderRadius: 4,
			                    rich: {
			                        b: {
			                            fontSize: 20,
			                            lineHeight: 33
			                        }
			                    }
			                }
			            },
			            center: ['50%', '60%'],
			            data: typeCount,
// 			            	[
// 			                {value:335, name:'直接访问'},
// 			            ],
			            itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ]
			};
		countChart.setOption(option);
  }
 
</script>
</html>
