var wid=0;
var hei=0;
var pagesize=100;//每页条数
var pagenum=1;//当前页
var pagecount=0;
var myChart1;
var myChart2;
var index;
$(function(){
	//loading层
	index = layer.load(1, {
	  shade: [0.3,'#fff']
	});
	wid=$(window).width()-$("#comm_menu").width();
	hei=$(window).height()-$("#commHead").height();
	var uid =$("#uid").val();
	getParam(uid);
});
/**
 * 获取数据库列表数据
 */
function getdataList(){
	var kw=$("#kw").val();
//	var data_type=$("#data_type").val();
	$.ajax({
		url:getContextPath()+"/paramdbCon/getdataList",
		type:"post",
		datatype:"json",
		data : {
//			data_type : data_type,
			pagesize : pagesize,
			pagenum : pagenum,
			kw : kw},
		success:function (data){
			var attr=data.split("&TAB&");
			pagecount=attr[1];
			$("#contentinfo").html(attr[0]);
			$("#allpagesize").html(pagecount);
			$("#current").html(pagenum);
			$(".total").html(attr[1]);
			$(".error").html(attr[2]);
		}
	});
}

/**
 * 获取数据库详细信息
 * @param uid 数据库ip地址
 */
function getParam(uid){
	var CPUVal="";
	var MEMORY_USAGE_PERCENTVal="";
	$.ajax({
		url:getContextPath()+"/paramdbCon/searchData",
		type:"post",
		data : {
			uid : uid,
			flag : 0
		},
		datatype:"json",
		success:function (result){
			var AA=result.split("%TAB%");
			var basicTab=AA[0];
			if(basicTab!=="" && typeof(basicTab)!=="undefined"){
				basicTab=eval(basicTab);
				$("#name").text(basicTab[0].name);
				$("#nameId").text(basicTab[0].name);
//				$("#zifuId").text("UTF-8");
//				$("#portId").text("1521");
				var instance_state=basicTab[0].instance_state;
				instance_state=instance_state==="OPEN"?"运行中":"未启动";
				$("#instance_state").text(instance_state);
				$("#ip").text(basicTab[0].ip);
				if(!myChart1){
					drawImage(basicTab[0].cpu_usage,basicTab[0].Memery_usage_percent);
			    }else{
			    	var str = $("#myChart1").html();
			    	if(str == ""){
			    		drawImage(basicTab[0].cpu_usage,basicTab[0].Memery_usage_percent);
			    	}else{
			    		//更新myChart1数据
			    		var option = myChart1.getOption();
			    		var data=[{value:basicTab[0].cpu_usage, name: 'CPU使用率'}]  
			    		option.series[0].data = data;   
			    		myChart1.setOption(option); 
			    	}
			    }
				if(!myChart2){
					drawImage(basicTab[0].Memery_usage_percent);
			    }else{
			    	var str = $("#myChart2").html();
			    	if(str == ""){
			    		drawImage(basicTab[0].Memery_usage_percent);
			    	}else{
			    		//更新myChart2数据
					    var option = myChart2.getOption();
					    var data=[{value:basicTab[0].Memery_usage_percent, name:'内存使用率'}]  
					    option.series[0].data = data;   
					    myChart2.setOption(option); 
			    	}
			    }
			}
			$("#normTable tbody").html(AA[1]);
			$(".spaceTab_div").html(AA[2]);
			$(".server_div").html(AA[3]);
			var p_hei=$(".db_body").height()+$(".db_head").height()+$(".db_head").height()+20;
			$("#iframe_dbMinuteParam",parent.document).css({height:p_hei+"px"});
//			RefreshParam(uid);
			//关闭加载层
		  	layer.close(index);
		},
		error:function(){
			//关闭加载层
		  	layer.close(index);
			layer.msg("数据加载失败！");
		}
	});
}
/**
 * 定时刷新基本信息
 */
//function RefreshParam(uid){
//    setTimeout(function(){
//       getParam(uid);
//    },10000);
//}

/**
 * 跳转服务器详情界面
 * @param uid
 * @param id
 */
function jumpServerPar(uid,id){
	//关闭软件定时器
	var softtime = window.top.softtime;
	for(var i=0;i<softtime.length;i++){
		clearInterval(softtime[i]);
	}
	$("#spsevuid").val(uid);
	$("#spsevid").val(id);
	$("#databaseUid").val($("#uid").val());
	$("#sevuid").val(uid);
	$("#sevid").val(id);
	$(".contain",parent.document).addClass("sethidden");
	$("#dbDetails",parent.document).removeClass("sethidden");
	$("#tt").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/serverParticulars.jsp");
}
/**
 * 仪表盘
 * @param CPUVal
 * @param MEMORY_USAGE_PERCENTVal
 */
function drawImage(CPUVal,MEMORY_USAGE_PERCENTVal){
	myChart1 = echarts.init(document.getElementById("myChart1")); 
	myChart2 = echarts.init(document.getElementById("myChart2")); 
	//定义图表options
	var options1 = {
			title : {},
			 tooltip : {
			        formatter: "{b} <br/> {c}%"
			    },
			    toolbox: {
			        feature: {
			            restore: {},
			            saveAsImage: {}
			        }
			    },
			    series: [
			             {
			                 name: 'CPU使用率',
			                 type: 'gauge',
			                 center : [ '50%', '60%' ],//圆心坐标，支持绝对值（px）和百分比
			                 radius : '100%', //仪表盘的值径
			                 axisLine:{ lineStyle:{width:15} },
			                 splitLine:{ length:20 },
			                 detail: {formatter:'{value}%',
			                 textStyle:{fontSize:20}},
			                 data:[{value: CPUVal, name: 'CPU使用率'}]  
			             }
			         ]
	};
	//定义图表options
	var options2 = {
			title : {},
			 tooltip : {
			        formatter: "{b} <br/> {c}%"
			    },
			    toolbox: {
			        feature: {
			            restore: {},
			            saveAsImage: {}
			        }
			    },
			    series: [
			             {
			                 name: '内存使用率',
			                 type: 'gauge',
			                 center : [ '50%', '60%' ],//圆心坐标，支持绝对值（px）和百分比
			                 radius : '100%', //仪表盘的值径
			                 axisLine:{ lineStyle:{width:15} },
			                 splitLine:{ length:20 },
			                 detail: {formatter:'{value}%',
			                 textStyle:{fontSize:20}},
			                 data:[{value: MEMORY_USAGE_PERCENTVal, name: '内存使用率'}]  
			             }
			         ]
	};
	myChart1.setOption(options1);
	myChart2.setOption(options2);
}
/**
 * 折线趋势图
 * @param uid
 */
function loadDayChart(uid) {
	$.ajax({
		url : getContextPath()+"/paramdbCon/findForTodayChart",
		type : "post",
		dataType : "json",
		async : false,
		data : {uid: uid},
		success : function(data) {
			var data=eval(data);
			for(var i = 0,allsize=data.length;i<allsize;i++){
				var yValues = new Array('0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0');
				var chartTitle=data[i].chartTitle;
				var elementId=data[i].elementId;
				for (var j = 0; j <24; j++) {
					yValues[j]=data[i]["avg_value"+j];
				}
				//构建图标
				var myChart = echarts.init(document.getElementById(elementId));
				option = {
					title: {
				        text: chartTitle,
				        textStyle: {
				        	fontSize: 14,
				        	fontWeight: 'bold',
				        }
				    },
				    tooltip: {trigger: 'axis'},
				    xAxis: {
				        type: 'category',
				        boundaryGap: false,
				        data: ['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23']
				    },
				    yAxis: {type: 'value',},
				    series: [
				        {
				            name: chartTitle,
				            type:'line',
				            data: yValues
				        }
				    ]
				};
				myChart.setOption(option);
				$("#"+elementId+" div")[0].style.width="100%";
				$("#"+elementId+" div")[0].style.height="100%";
				
				$("#"+elementId+" canvas")[0].style.width="100%";
				$("#"+elementId+" canvas")[0].style.height="100%";
			}
		},
		error : function(){
			console.log("数据出错！");
		}
	});
}
//返回按键
function goBack(){
	//关闭软件定时器
	var softtime = window.top.softtime;
	for(var i=0;i<softtime.length;i++){
		clearInterval(softtime[i]);
	}
	var returnType = $("#returnType").val();
	$("#isParam").val("N");
	if(returnType == "servers"){
		$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/serverParticulars.jsp");
	}else if(returnType == "group"){
		$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/serverParticulars.jsp");
	}else if(returnType == "index"){
		$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/serverParticulars.jsp");
	}else{
		$("#returnType").val("");
		$("#uid").val("");
		var href=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/databaseList.jsp";
		$("#tt").load(href);
	}
}

function retmonHome(){  
   $("#tt").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monihome.jsp");
}

//窗口改变大小时重新加载
//$(window).resize(function(){
//	var uid =$("#uid").val();
//	getParam(uid);
//});