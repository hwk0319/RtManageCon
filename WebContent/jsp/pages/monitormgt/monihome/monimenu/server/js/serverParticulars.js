var ip=null;
var device_code = $("#sevuid").val();
var device_id = $("#sevid").val();
if (device_code == null) {
	device_code = "";
}
var cpusageData = null;
var memusageData = null;
var fileusageData = null;
var layerId;
$(function(){
	//加载层
	layerId = layer.load(1, {
		  shade: [0.3,'#fff']
	});
	//目前只有对这个服务器进行了监控，其他的还没有完善，所以先用这个来进行测试
	$(".oncover").click(function(){
		deviceInfo(device_id);
	});
	createMyCharts();
	loadDetailInfo();
});

$(window).resize(function(){
	if(myChart)
		myChart.resize();
});

//统一调用页面获取数据
function loadDetailInfo(){
	searchWarnInfo(device_code);
	getEcahartData(device_code);
	$.ajax({
		url:getContextPath()+"/monitorhomeCon/searchDetailById",
		type:"post",
		data:{
			id:device_id
		},
		success : function(data){
			$("#db_server_name").text(data[0].name);
			$("#db_ip").text(data[0].in_ip);
			var device_state= data[0].serverstatus;
			if(device_state!='0'){
				$(".icon").css({
					"background":"url("+getContextPath()+"/jsps/img/fwqbj_green.png)",
					"background-size":"100% 100%"
				});
			}else{
				$(".icon").css({
					"background":"url("+getContextPath()+"/jsps/img/fwqbj_red.png)",
					"background-size":"100% 100%"
				});
			}
		} 
	});
//	doSearchPartInfo();
	getSoftInfo();
	if(myChart){
		chartIntervalFn();
	}
}

function getEcahartData(uuid){
	//仪表盘数据
	$.ajax({
		url:getContextPath()+"/monitorhomeCon/searchEchartData",
		type:"post",
		data:{
			uid:uuid
		},
		async:false,
		success:function(result){
			//查询CUP使用率，若有则显示对应数值，没有则显示为0
			if(result.cpuuseage.length>0){
				cpusageData = result.cpuuseage[0];
			}else{
				cpusageData = 0 ;
			}
			//查询内存使用率，若有则显示对应数值，没有则显示为0
			if(result.memuseage.length>0){
				memusageData = result.memuseage[0];
			}else{
				memusageData = 0 ;
			}
			//查询文件使用率，若有则显示对应数值，没有则显示为0
			if(result.fileuseage.length>0){
				fileusageData = result.fileuseage[0];
			}else{
				fileusageData = 0 ;
			}
		}
	});
}

//查询配带信息，其中具体字段需要到时从指标indexdata表里面去取
function doSearchPartInfo() {

}
//跳转到数据库页面
function showsys(uid){
	//关闭服务器定时器
	var svtimes = window.top.servertime;
	for(var i=0;i<svtimes.length;i++){
		clearInterval(svtimes[i]);
	}
	$("#uid").val(uid);
	$("#isParam").val("Y");
	var href=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/databaseList.jsp";
	$("#tt").load(href);
}

var myChart =  null;
var chartIntervalFn = function(){
	var _option = myChart.getOption();
	if(cpusageData!=0){
		_option.series[0].data = [{value: cpusageData}];//cpusageData.toFixed(2);
	}
	if(memusageData!=0){
		_option.series[1].data = [{value: memusageData}];//memusageData.toFixed(2);
	}
	if(fileusageData!=0){
		_option.series[2].data = [{value: fileusageData}];//fileusageData.toFixed(2);
	}
//	if(cpusageData!=0 || memusageData!=0 || fileusageData!=0){
		myChart.setOption(_option);
//	}
};

//创建仪表盘功能函数，使用mycharts
function createMyCharts(){
	//使用Echarts仪表显示CPU和内存使用率
	var cpuuse = 0;
	var memuse = 0;
	var fileuse = 0;
	myChart = echarts.init(document.getElementById('myChart'));
	//定义图表options
	var options = {
			title : {
			},
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
			                 center : [ '20%', '55%' ],//圆心坐标，支持绝对值（px）和百分比
			                 radius : '100%', //仪表盘的值径
			                 axisLine:{ lineStyle:{width:15} },
			                splitLine:{ length:20 },
			                 detail: {formatter:'{value}%',
			                	 textStyle:{fontSize:20}},
			                 data:    [{value: cpuuse, name: 'CPU使用率'}]  
			             },
			             {
			                 name: '内存使用率',
			                 type: 'gauge',
			                 center : [ '50%', '55%' ],//圆心坐标，支持绝对值（px）和百分比
							 radius : '100%', //仪表盘的直径
							 axisLine:{lineStyle:{width:15}},
							 splitLine:{ length:20 },
			                 detail: {formatter:'{value}%',
			                	 textStyle:{fontSize:20}},
			                 data:  [{value: memuse, name: '内存使用率'}]  
			             },
			             {
			                 name: '根目录使用率',
			                 type: 'gauge',
			                 center : [ '80%', '55%' ],//圆心坐标，支持绝对值（px）和百分比
							 radius : '100%', //仪表盘的直径
							 axisLine:{lineStyle:{width:15}},
							 splitLine:{ length:20 },
			                 detail: {formatter:'{value}%',
			                	 textStyle:{fontSize:20}},
			                 data:  [{value:fileuse, name: '根目录使用率'}]  
			             }
				
			         ]
	};
	myChart.setOption(options);
}
//软件信息模块
function getSoftInfo(){
	$.ajax({
		url:getContextPath()+"/monitorhomeCon/searchSoftInfo",
		type:"post",
		data:{
			uid:device_code
		},
		success : function(data){
			var len = data.length;
			if(len>0){
				var str='<div class="consoft">';
				for(var i=0;i<len;i++){
					if(data[i].systype == '1'){
						str+='<div class="showsoft" onclick="showsys('+data[i].uid+')">'+'<div class="dbinfoimg"></div><div class="infotxt">'
						+''+data[i].ip+'<br>&nbsp;'+data[i].name+'</div></div></div>';
					}else if(data[i].systype == '2'){
						str+='<div class="showsoft" onclick="showsys('+data[i].uid+')">'+'&nbsp;文件系统&nbsp; ip:'+data[i].ip+'&nbsp; 实例名:'+data[i].name+'</div></div>';
					}else if(data[i].systype == '3'){
						str+='<div class="showsoft" onclick="showsys('+data[i].uid+')">'+'&nbsp;备份系统&nbsp; ip:'+data[i].ip+'&nbsp; 实例名:'+data[i].name+'</div></div>';
					}else if(data[i].systype == '5'){
						str+='<div class="showsoft" onclick="showsys('+data[i].uid+')">'+'&nbsp;web容器&nbsp; ip:'+data[i].ip+'&nbsp; 实例名:'+data[i].name+'</div></div>';
					}
				}
				$(".soft").html(str);
			}else{
				$(".mainsoft").css("display",'none');
			}
		} 
	});
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function() {
	var width = document.body.clientWidth;
	var GridWidth = (width - 230) * 0.98 - 10;
});

//--------------温度状态-----------------//
//--------------1000027、28、29、30--------------//
//查询告警信息
function searchWarnInfo(uuid){
	//如果是温度的话，其中的index_id有四个值可取（1000027、28、29、30）
	$.ajax({
		url:getContextPath()+"/monitorhomeCon/searchCpuTemp",
		type:"post",
		data:{
			uid:uuid
		},
		success:function(result){
			//先定义一个公用的div主要用来表示后面没有取到数据时候的展示
			var nodata="";
			nodata+="<div class='nodataimg'></div>";
			//CPU温度的展示
			var templen = result.cputemp.length/2;
			if(templen>0){
				var cpustr = "";
				for(var i=0;i<templen;i++){
					cpustr+="<div class='cpuval"+" cpuvalue"+i+"'><div class='temppvalue'>"
						+result.cputemp[i*2]+"</div></div>";
				}
				$(".tempvalue").html(cpustr);
				for(var i=0;i<templen;i++){
					//针对每个指标项出现告警则显示红色状态,其中默认颜色为绿色表示状态好的
					//如若出现告警，则头部线展示红色
					if(result.cputemp[i*2+1]=='1'){
						$(".cpuvalue"+i).addClass("cpu_red");
						$(".cputemp .wt_line").addClass("line_red");
					}
				}
			}else{
				$(".tempvalue").html(nodata);
			}
			//电源功耗的展示
			var powerstr = "";
			var conslen = result.cpucons.length/2;
			if(conslen>0){
				var cpustr1 = "";
				for(var i=0;i<conslen;i++){
					cpustr1+="<div class='consdval"+" consdvalue"+i+"'><div class='cvtxt'>"+result.cpucons[2*i]+"</div></div>";
					if(result.cpucons[2*i]>0){
						powerstr+="<div class='powerdvalue'><img src='"+getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/img/power_green.png'/><div style='margin-left: 10px;'>ok</div></div>"
					}
				}
				$(".consvalue").html(cpustr1);
				$(".powervalue").html(powerstr);
				for(var i=0;i<conslen;i++){
					//如出现告警信息则边框表示红色，title的线变成红色
					if(result.cpucons[i*2+1]=='1'){
						$(".consdvalue"+i).addClass("border_redline");
						$(".powktemp .wt_line").addClass("line_red");
					}
				}
			}else{
				$(".consvalue").html(nodata);
				$(".powervalue").html(nodata);
			}
			
			if(conslen>2){
				$(".consdval").css({
					"width":"20%",
					"margin-left":"2%"
				});
				$(".powerdvalue").css({
					"width":"18%",
					"margin-left":"2%"
				})
			}
			//风扇转速的展示
			var fanlen = result.fan.length/2;
			if(fanlen>0){
				var fanstr = "";
				for(var i=0;i<fanlen;i++){
					fanstr+="<div class='fandval"+" fandvalue"+i+"'>"+result.fan[2*i]+"</div>";
				}
				$(".fanvalue").html(fanstr);
				for(var i=0;i<fanlen;i++){
					//如出现告警信息则字体表示红色，title的线变成红色
					if(result.fan[i*2+1]=='1'){
						$(".fandvalue"+i).addClass("font_red");
						$(".fantemp .wt_line").addClass("line_red");
					}
				}
			}else{
				$(".fanvalue").html(nodata);
			}
			//机箱温度的展示
			var boxlen = result.inputtemp.length/2;
			if(boxlen>0){
				var boxstr = "";
				for(var i=0;i<boxlen;i++){
					boxstr+="<div class='boxtempvalue'>"+
					"<div class='bv_img"+" bvimage"+i+"'></div>"+
					"<div class='bv_txt'><span>"+result.inputtemp[2*i]+"</span></div></div>";
				}
				$(".boxvalue").html(boxstr);
				for(var i=0;i<boxlen;i++){
					//如出现告警信息则图标表示红色
					if(parseInt(result.inputtemp[2*i]) > 65){
						$(".bvimage"+i).addClass("bvimg_red");
						$(".boxtemp .wt_line").addClass("line_red");
					}
				}
			}else{
				$(".boxvalue").html(nodata);
			}
			//电压状态的展示
			var volelen = result.vols.length;
			var volestr = "";
			
			if(volelen>0){
				for(var i=0;i<volelen;i++){
					if(result.vols[i] == 'unnormal'){
						volestr+="";
					}else{
						volestr+="<div class='voledvalue'>"+result.vols[i]+"</div>";
					}
				}
				$(".volevalue").html(volestr);
			}else{
				$(".volevalue").html(nodata);
			}
			layer.close(layerId);//关闭加载层；
		},
//		async: false
	});
}

function getDeviceHealth(){
	$.ajax({
		url:getContextPath()+"/monitorhomeCon/searchDetailById",
		type:"post",
		data:{
			id:device_id
		},
		success : function(data){
			
		} 
	});
}

//点击服务器查询详情
function deviceInfo(id){
	layer.open({
	    type: 2,
	    title: '设备详情', 
	    fix: false,
	    shadeClose: false,
	    area: ['610px', '320px'],
//	    area: ['45%', '50%'],
	    content: [getContextPath()+"/jsp/comm/jsp/deviceInfo.jsp?id="+id,'no']
	});
}
function getObjlength(obj){
	var n = 0;
	var count = 0;
	for(n in obj){  
	      if(obj.hasOwnProperty(n)){  
	         count++;  
	      }  
	   }  
	return count;
}
$(".title .getmore").click(function(){
	$("#tt").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/moreindex/moreindex.jsp",{id:device_id,uid:device_code},function(){});
});