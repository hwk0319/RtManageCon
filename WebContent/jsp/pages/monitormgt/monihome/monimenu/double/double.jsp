<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<title>双活</title>
<style type="text/css">
body{
	width: 100%;
	height: 100%;
	margin: 0;
	padding: 0;
	font-size:13px;
	overflow: hidden;
	background: #01114d;
}
#core_div,#core_line{
	height:95%;
	width: 37%;
	float: left;
	margin-top: -1%;
}
#core_line{width: 26%;}
#core_name{
	width: calc(90% - 30px);
	height: 15%;
	overflow: hidden;
}
#core_name div{
	width:35%;
	height:100%;
	margin: 0 auto;
	color: #fff;
	font-size: 13px;
	font-weight: bold;
	text-align: center;
	margin-top: 10%;
}
#core_group{
	width:100%;
	height: 90%;
	overflow: hidden;
	padding: 0;
	margin: 0;
}
.core_A #group_out,#group_in,#group_db{
	width: 90%;
	height:calc(100% / 5 - 2px);
	overflow: visible;
	border-radius:3px;
}
.core_B #group_out,#group_in,#group_db{
	width: 90%;
	height:calc(100% / 5 - 0px);
	overflow: visible;
	border-radius:3px;
}

.core_A #servers{
	width: 90%;
	height: 75%;
	text-align: center;
}
.core_B #servers{
	width: 90%;
	height: 75%;
	text-align: center;
	float: right;
}
.core_B #grouptype{
	float: right;
}
#servers img{
	width: calc(100% / 4 - 15px);
	height: 90%;
	margin-left: 5px;
	margin-right: 5px;
	cursor: pointer;
}
#group_db{
	width:90%;
	margin-top: 15px;
}
#group_db #servers img{
	width:calc(100% / 4 - 15px); 
	height: 95%;
}
#grouptype{
	color: #87bcc7;
	width: 90%;
	text-align:center;
	white-space:nowrap;
	font-size: 10px;
}
.core_A #state_line{
	width: 80%;
	height: 20%;
	visibility: hidden;
	text-align: center;
}
.core_B #state_line{
	width: 80%;
	height: 20%;
	visibility: hidden;
	text-align: center;
}
#state_line img{
	width:10%;
	height:100%;
}
#line_1,#line_2,#line_3{
	width:100%;
	height:calc((87% - 160px) / 3 - 2px);
}
#line_2{
	margin:80px 0 80px 0;
}
#line_2 div{
	margin: 0 auto;
}
#line_2 img{width:100%;height:100%;}
#line_1 img{
	width: 70%;
	height: 13px;
	margin-left: 15%;
	margin-top: calc((100% - 13px)/4);
}
#line_3 div{
	height: 20px;
	border-bottom: 2px dashed #FE940D;
	margin-top:50%;
	color:#FE940D;
	text-align: center;
}
#indb_state_line{
	position: absolute;
	width: 100px;
	visibility: hidden;
}

#ab_line{
	width: 100%;
	height: 1px;
	border-bottom: 1px dashed #909090;
	position: absolute;
	z-index: 100;	
}
#db_info{
	color:#fff;
    min-width: 30%;
	border: 1px solid rgba(0,0,0,0.1);
	background: #3da8dc;
	position: absolute;
	z-index: 300;
	border-radius:5px;
	-webkit-box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
	font-size: 10px;
	display: none;
}
#db_info span{
	width: 100%;
	display:block;
	text-align: center;
	line-height: 18px;
	white-space: nowrap;
}
/*****DG切换按钮样式********/
#switch{
	text-align: center;
} 
#switch_butt{
	color: #f7f6f5;
	width:80px;
	height: 30px;
	border: 1px solid #3ea9dd;
	margin-top: 15px;
	margin-left:3px;
	background: #3ea9dd;
}
#switch_butt:hover{
  -webkit-box-shadow:0 0 10px #c1e0ed;  
  -moz-box-shadow:0 0 10px #c1e0ed;  
  box-shadow:0 0 10px #c1e0ed;  
}
.nameSpan{
	color:#87bcc7;
	font-family: Microsoft YaHei;
	font-size: 15px;
	text-align: center;
  	display: flex;
  	justify-content: space-between;
  	align-items: center;
    margin-left: 20px;
    margin-right: 20px;
}
#rightTable{
	width: calc(75% - 10px);
	height: calc(100% - 45px);
	float: left;
}
#double_body{
	width: 100%;
	height: calc(65% - 3px);
	overflow: hidden;
	font-family:  Microsoft YaHei;
	background-color: #01306b;
	margin-top: 3px;
}
/* 左边数据 table */
.leftTable{
	width:25%;
	height:calc(100% - 45px);
	margin-left: 3px;
	margin-right: 3px;
	float: left;
}
.zaiBei{
	background-color: #01306b;
	margin-top: 3px;
	color: #fff;	
	height:calc(50% - 6px);	
}
#dataTable{
	height:calc(50% - 7px);
	background-color: #01306b;
	margin-top: 3px;
	color: #fff;
}
.dbTable{
	height:20%;
	width: 100%;
}
.dbData{
	width: 100%;
}
.dbData td{
	width:50%;
	text-align:center;
}
#beiJingChart{
	height:90%;
}
#echartsTu{
	height:calc(100% - 30px);
	margin-top: 3px;
}
#buttomCore{
	width: 100%;
	height: calc(35% - 10px);
    float: left;
    margin-top: 3px;
}
.coreInfoA{
	width: calc(50%);
	height: 100%;
	background-color: #01306b;
    float: left;
    color: #fff;
    margin-right: 3px;
}
.coreInfoB{
	width: calc(50% - 3px);
	height: 100%;
	background-color: #01306b;
    float: left;
    color: #fff;
}

.kongJianChart{
	height:90%;
	width:45%;
	float: left;
/* 	margin-left: 15px; */
}
.kjcharts{
	height:95%;
	margin-top: 5px;
}

/* 在线人数 */
#all{
	width: 100%;
	height: 20%;
	margin: auto;
	border:solid 1px #1c80b1;
	border-radius: 7px;
	margin-top: 10px;
}
#all .t_num{
	position: relative;
	visibility:hidden;
}
#all .t_num i {
	width: 22px;
    height: 35px;
	display: inline-block;
	background-position: 0 0;
}
#all1{
	width: 100%;
	height: 20%;
	margin: auto;
	border:solid 1px #1c80b1;
	border-radius: 7px;
	margin-top: 10%;
}
#all1 .t_num{
	position: relative;
	visibility:hidden;
}
#all1 .t_num i {
	width: 22px;
    height: 35px;
	display: inline-block;
	background-position: 0 0;
}

/* 连线动画 */
.circle {
    width: 5px;
    height: 5px;
    background: #00ffdc;
    border-radius: 5px;
    -webkit-border-radius: 50px;
    animation: circle_animation 2s infinite;
	-webkit-animation: circle_animation 2s infinite;
}
@-webkit-keyframes circle_animation {
  from { background-color: #00ffdc; -webkit-box-shadow: 0 0 25px #00ffdc,0 0 20px #FFF; }
  50% { background-color: #36A3E9; -webkit-box-shadow: 0 0 25px #FFF,0 0 20px #00ffdc; }
  to { background-color: #00ffdc; -webkit-box-shadow: 0 0 25px #00ffdc,0 0 20px #FFF; }
}
.line {
  height: 1px;
  flex-grow: 1;
  background-color: #87bcc7;
}
.yun_png{
    width: 40%;
    height: 100%;
    position: absolute;
    left: 30%;
    top: 5px;
    border: none;
    visibility:hidden;
}
#double_body .core_B {
    position: absolute;
    right: 0;
}
#div_core_a{
	text-align:center;
}
#div_core_b{
	text-align:center;
}
.fzjhImg{
	width: 20%;
    position: absolute;
    margin-left: 80%;
}
.arrow{
    position: absolute;
    z-index: 99999;
    width: 20px;
    height: 15px;
    top: 0px;
    left: 0;
    display: none;
}
.arrow1{
    position: absolute;
    z-index: 99999;
    width: 15px;
    height: 20px;
    top: 0px;
    left: 0;
    display: none;
}
.xian {
	position: absolute;
    background-color: #36a3e9;
    display: none;
}
.xianc {
	position: absolute;
    background-color: #46d083;
    display: none;
}
</style>
</head>
  <body>
  		<div class="leftTable">
  			  <div class="zaiBei">
  			  	  <div class="nameSpan"><span class="line"></span><span>&nbsp;北京-上海灾备&nbsp;</span><span class="line"></div>
			  	  <div id="echartsTu"></div>
  			  </div>
		  	  <div id="dataTable">
				<div class="nameSpan"><span class="line"></span><span>&nbsp;北京双活&nbsp;</span><span class="line"></span></div>
		  	  	<div id="beiJingChart"></div>
		  	  </div>
  		</div>
      <div id="rightTable">
	      <div id="double_body">
	      	  <div id="dName" class="nameSpan"><span class="line"></span><span id="doubleName">&nbsp;电力交易双活&nbsp;</span><span class="line"></div>
	           <!-- A中心 -start-->
	      	  <div id="core_div" class="core_A" style="overflow: visible;">
	      	      <div id="core_name">
	      	      	<img class="yun_png">
	          	    <div></div>
	          	  </div>
	          	  <div id="core_group" style="overflow: visible;">
	          	     <div id="state_line">
	          	     	<img style="display: none;"/>
	          	     </div>
	          	     <!-- 内网服务器 -start-->
		          	 <div id="group_in" style="margin-top: -20px;">
	          	          <div id="servers"></div>
	          	          <div id="grouptype">应用服务器</div>
		          	 </div>
		          	 <!-- 内网服务器 -end-->
		          	 <div id="state_line">
		          	 </div>
		          	 <!-- 数据库服务器 -start-->
		          	 <div id="group_db" style="overflow: visible;">
	          	            <div id="servers"></div>
	        	            <div id="grouptype">数据库服务器</div>
	        	            <div id="db_info" style="overflow: visible;"></div>
		          	 </div>
		          	 <!-- 数据库服务器 -end-->
	          	  </div>
	          	  <!-- 数据库连线 -->
	          	  <div id="indb_state_line" style="position: absolute;top: 0;left: 0">
	          	  </div>
	      	  </div>
	      	   <!-- A中心 -end-->
	      	   <!-- 双活中间部分 -->
	      	  <div id="core_line">
		      	  <!-- 一键切换按钮 -->
			      <div id="switch">
			      	<input type="button" class="btn" onclick="switchTab()" id="switch_butt" value="一键切换"/>
			      </div> 
	      	  	<!-- 在线人数  -->
				<!-- <div id="all1">
					<div style="color:#fff;width: 100%;text-align: center;margin-top:5px;"> 在线人数</div></br>
					<span class="t_num t_num1"></span>
				</div> -->
	      	  </div>
	      	   <!-- B中心 -start-->
	      	  <div id="core_div" class="core_B" style="overflow: visible;">
	      	      <div id="core_name" style="float: right;">
	      	      	<img class="yun_png">
	          	    <div></div>
	          	  </div>
	          	  <div id="core_group" style="overflow: visible;">
	          	     <div id="state_line" style="float: right;">
	          	     	<img style="display: none;"/>
	          	     </div>
	          	     <!-- 内网服务器 -start-->
		          	 <div id="group_in" style="float: right;margin-top: -20px;">
	          	          <div id="servers"></div>
	          	          <div id="grouptype">应用服务器</div>
		          	 </div>
		          	 <!-- 内网服务器 -end-->
		          	 <div id="state_line" style="float: right;">
		          	 </div>
		          	 <!-- 数据库服务器 -start-->
		          	 <div id="group_db" style="overflow: visible;float: right;">
	          	            <div id="servers"></div>
	        	            <div id="grouptype">数据库服务器</div>
	        	            <div id="db_info" style="overflow: visible;"></div>
		          	 </div>
		          	 <!-- 数据库服务器 -end-->
	          	  </div>
	          	  <div id="indb_state_line" style="position: absolute;top: 0;left: 0;float: right;">
	          	  </div>
	      	  </div>
	      	   <!-- B中心 -end-->
	      </div>
 	      <div id="buttomCore">
	      	<div id="leftCore" class="coreInfoA">
	      		<div class="nameSpan"><span class="line"></span><span id="leftCoreInfoA">&nbsp;表空间使用情况&nbsp;</span><span class="line"></div>
	      		<div class="kjcharts">
		      		<div id="kongJianChart" class="kongJianChart"></div>
		      		<div id="kongJianChart_1" class="kongJianChart"></div>
	      		</div>
	      	</div>
	      	<div id="rightCore" class="coreInfoB">
	      		<div class="nameSpan"><span class="line"></span><span id="leftCoreInfoB">&nbsp;表空间使用情况&nbsp;</span><span class="line"></div>
	      		<div class="kjcharts">
		      		<div id="kongJianChart2" class="kongJianChart"></div>
		      		<div id="kongJianChart_2" class="kongJianChart"></div>
	      		</div>
	      	</div>
	      </div>
      </div>
      <!-- svg画线  A中心-->
      <svg id="svgLine" xmlns="http://www.w3.org/2000/svg" version="1.1" style="pointer-events: none;position:absolute;left:0; top:0; width:100%; height:100%">
	 	 <path id="svgPath2" d='' stroke='#36a3e9' stroke-width='3' fill='none' marker-start="url(#markerArrow3)"    
        			marker-end="url(#markerArrow4)"/>
	 	 <defs>  
			<marker id="markerArrow3" markerWidth="9" markerHeight="9" refx="2" refy="2" orient="auto">  
				 <path d="M0 2 L6 0 L4 2 L6 4 z" style="fill: #36a3e9;" />  
			</marker>  
			<marker id="markerArrow4" markerWidth="9" markerHeight="9" refx="4" refy="2" orient="auto">  
				 <path d="M0 0 L6 2 L0 4 L2 2 z" style="fill: #36a3e9;" />  
			</marker>
		 </defs>
	  </svg>
	  <!-- ie浏览器下设置图片 -->
	  <img id="arr3" class="arrow1" src=""/>
	  <img id="arr4" class="arrow1" src=""/>
	  <img id="arrb3" class="arrow1" src=""/>
	  <img id="arrb4" class="arrow1" src=""/>
	  <img id="arrc1" class="arrow" src=""/>
	  <img id="arrc2" class="arrow" src=""/>
	  <!-- ie下设置图片end -->
	  <!-- ie下设置连线 -->
	  <div class="xian" id="topXian4"></div>
	  <div class="xian" id="topXianb4"></div>
	  <div class="xianc" id="topXianc1"></div>
	  <!-- ie下设置连线end -->
  	  <div id="circles" style="position:absolute;left:0;top:0;">
     	 <div id="circle3" class="circle" style="position:absolute;display:none;"></div>
     	 <div id="circle4" class="circle" style="position:absolute;display:none;"></div>
      </div>
      
      <!-- B中心 -->
      <svg id="svgLineB" xmlns="http://www.w3.org/2000/svg" version="1.1" style="pointer-events: none;position:absolute;left:0; top:0; width:100%; height:100%">
	 	 <path id="svgPathB2" d='' stroke='#36a3e9' stroke-width='3' fill='none' marker-start="url(#markerArrowB3)"    
        			marker-end="url(#markerArrowB4)"/>
	 	 <defs>  
			<marker id="markerArrowB3" markerWidth="9" markerHeight="9" refx="2" refy="2" orient="auto">  
				 <path d="M0 2 L6 0 L4 2 L6 4 z" style="fill: #36a3e9;" />  
			</marker>  
			<marker id="markerArrowB4" markerWidth="9" markerHeight="9" refx="4" refy="2" orient="auto">  
				 <path d="M0 0 L6 2 L0 4 L2 2 z" style="fill: #36a3e9;" />  
			</marker>  
		 </defs>
	  </svg>
      <div id="circlesB" style="position:absolute;left:0;top:0;">
     	 <div id="circleB3" class="circle" style="position:absolute;display:none;"></div>
     	 <div id="circleB4" class="circle" style="position:absolute;display:none;"></div>
      </div>
      
      <!-- A-B中心数据库连线 -->
      <svg id="svgLineC" xmlns="http://www.w3.org/2000/svg" version="1.1" style="pointer-events: none;position:absolute;left:0; top:0; width:100%; height:100%">
	 	 <path id="svgPathC" d='' stroke='#46d083' stroke-width='3' fill='none' marker-start="url(#markerArrowDB1)"    
        			marker-end="url(#markerArrowDB2)"/>
	 	 <defs>  
			<marker id="markerArrowDB1" markerWidth="9" markerHeight="9" refx="2" refy="2" orient="auto">  
				 <path d="M0 2 L6 0 L4 2 L6 4 z" style="fill: #46d083;" />  
			</marker>  
			<marker id="markerArrowDB2" markerWidth="9" markerHeight="9" refx="4" refy="2" orient="auto">  
				 <path d="M0 0 L6 2 L0 4 L2 2 z" style="fill: #46d083;" />  
			</marker>  
		 </defs>
	  </svg>
      <div id="circlesB" style="position:absolute;left:0;top:0;">
     	 <div id="circleC" class="circle" style="position:absolute;display:none;"></div>
     	 <div id="circleC1" class="circle" style="position:absolute;display:none;"></div>
      </div>
       
     <input type="hidden" id="roleId" value="${sessionScope.user.roleid}"/>
     <input type="hidden" id="double_id"/>
  </body>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/double/double.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/echarts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/double/js/digitalScroll.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/double/js/animates.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
  <script>
  	$(function(){
  		//设置云图片
  		$(".yun_png").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/yun.png");
  		$(".yun_png").css("visibility","visible");
  		// 在线人数展示
//   		show_num(sum);
//   		show_num1(sum);
  		//获取在线人数
//   		setTimeout(function(){
//   			getUserNumbers();
//   		}, 5000);
  		//绘制地图
  		getEcharts();
  	});
  	
  	function getEcharts(){
  		//中国地图
  		var _mapChart = echarts.init(document.getElementById('echartsTu'));
  		$.get(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/js/china.json", function (chinaJson) {
  	        echarts.registerMap('china', chinaJson);
  	      	myCharts(_mapChart);
  	    });
  		//北京地图
  		var BeiJingMap = echarts.init(document.getElementById('beiJingChart'));
  		$.get(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/js/bei_jing_geo.json", function (chinaJson) {
  	        echarts.registerMap('beijing', chinaJson);
  	      	beiJingCharts(BeiJingMap);
  	    });
  	}
  	// 清除动画
  	var timesFn = [];
  	var circles = [];
  	//svg线
  	function svgLine(){
  		for(var m=0;m<circles.length;m++){
  		}
  		$("#circle3").stop(true);
  		$("#circle4").stop(true);
  		$("#circleB3").stop(true);
  		$("#circleB4").stop(true);
  		$("#circleC").stop(true);
  		$("#circleC1").stop(true);
  		timesFn = [];
  		circles = [];
  		//判断使用什么浏览器
		var ieOrChorme = myBrowser();
		if(ieOrChorme == "Chrome"){
	  		//A中心
	  		$("#circle3").css({"left": $(".core_A #state_line").offset().left + $(".core_A #state_line").width()/2 - $("#comm_menu").width()
	  						,"top": $(".core_A #group_db").offset().top - $(".core_A #state_line").height() - $("#commHead").height()});
	  		$("#circle4").css({"left": $(".core_A #state_line").offset().left + $(".core_A #state_line").width()/2 - $("#comm_menu").width()
	  						,"top": $(".core_A #group_db").offset().top - $("#commHead").height()});
	  		circles.push($("#circle3"));
	  		circles.push($("#circle4"));
	 		 //svg db
	 		 var pointdb = [parseInt($("#circle3").css("left").split("px")[0])+3.5,parseInt($("#circle3").css("top").split("px")[0])];
	 		 var pointdb_1 = [parseInt($("#circle4").css("left").split("px")[0])+3.5,parseInt($("#circle4").css("top").split("px")[0])+4];
	  		//连线动画 从上往下
	  		var _fn2 = setTimeout(function(){
		  		$.movies("#circle3", [
					{"left": pointdb[0]-3,"top": pointdb[1]+10}
				    ,{"left": pointdb_1[0]-3,"top": pointdb_1[1]-15}
			  	],[2500,1000]);
				//连线动画 从下往上
		  		$.movies("#circle4", [
				    {"left": pointdb_1[0]-3,"top": pointdb_1[1]-15}
					,{"left": pointdb[0]-3,"top": pointdb[1]+10}
			  	],[2500,1500]);
	  		}, 1500);
	  		timesFn.push(_fn2);
	  		$("#svgLine #svgPath2").attr("d","M"+pointdb[0]+" "+pointdb[1]+" L"+pointdb_1[0]+" "+pointdb_1[1]+"");
	  		/* //B中心 */
	  		$("#circleB3").css({"left": $(".core_B #state_line").offset().left + $(".core_B #state_line").width()/2 - $("#comm_menu").width()
					,"top": $(".core_B #group_db").offset().top - $(".core_B #state_line").height() - $("#commHead").height()});
			$("#circleB4").css({"left": $(".core_B #state_line").offset().left + $(".core_B #state_line").width()/2 - $("#comm_menu").width()
					,"top": $(".core_B #group_db").offset().top - $("#commHead").height()});
	  		circles.push($("#circleB3"));
	  		circles.push($("#circleB4"));	
	  		//svg db
			 var pointdbB = [parseInt($("#circleB3").css("left").split("px")[0])+3.5,parseInt($("#circleB3").css("top").split("px")[0])];
			 var pointdbB_1 = [parseInt($("#circleB4").css("left").split("px")[0])+3.5,parseInt($("#circleB4").css("top").split("px")[0])+4];
	 		//连线动画 从上往下
	 		var _fn4 = setTimeout(function(){
		  		$.movies("#circleB3", [
					{"left": pointdbB[0]-3,"top": pointdbB[1]+10}
				    ,{"left": pointdbB_1[0]-3,"top": pointdbB_1[1]-15}
			  		],[2500,1500]);
		  		$.movies("#circleB4", [
	   			    {"left": pointdbB_1[0]-3,"top": pointdbB_1[1]-15}
	   				,{"left": pointdbB[0]-3,"top": pointdbB[1]+10}
	   		  		],[2500,1500]);
	 		}, 1500);
	 		timesFn.push(_fn4);
	 		$("#svgLineB #svgPathB2").attr("d","M"+pointdbB[0]+" "+pointdbB[1]+" L"+pointdbB_1[0]+" "+pointdbB_1[1]+"");
	 		//A-B 数据库连线
	 		$("#circleC").css({"left": $(".core_A #group_in #servers").offset().left + $(".core_A #group_in #servers").width()*0.85 - $("#comm_menu").width()+10
					,"top": $(".core_A #group_db #servers").offset().top + $(".core_A #group_db #servers").height()/2 - $("#commHead").height()});
	 		$("#circleC1").css({"left": $(".core_B #group_in #servers").offset().left +($(".core_B #group_in #servers").width()*0.25) - $("#comm_menu").width()-45
	  				,"top": $(".core_B #group_db #servers").offset().top + $(".core_B #group_db #servers").height()/2 - $("#commHead").height()});
	 		circles.push($("#circleC"));
	  		circles.push($("#circleC1"));	
	 		var pointC = [parseInt($("#circleC").css("left").split("px")[0])-10,parseInt($("#circleC").css("top").split("px")[0])+3.5];
	 		var pointC1 = [parseInt($("#circleC1").css("left").split("px")[0])+15,parseInt($("#circleC1").css("top").split("px")[0])+3.5];
	 		var _fn5 = setTimeout(function(){
		  		$.movies("#circleC", [
				    {"left": pointC[0]+10,"top": pointC[1]-3}
					,{"left": pointC1[0]-15,"top": pointC1[1]-3}
			  		],[5500,5000]);
		  		$.movies("#circleC1", [
					{"left": pointC1[0]-15,"top": pointC1[1]-3},
				    {"left": pointC[0]+10,"top": pointC[1]-3}
			  		],[5000,5000]);
	 		}, 1500);
	 		timesFn.push(_fn5);
	 		$("#svgLineC #svgPathC").attr("d","M"+pointC[0]+" "+pointC[1]+" L"+pointC1[0]+" "+pointC1[1]+"");
		}else{
			$("#svgLine #svgPath").hide();
  			$("#svgLine #svgPath2").hide();
  			$("#svgLineB #svgPathB").hide();
  	  		$("#svgLineB #svgPathB2").hide();
  	  		$("#svgLineC #svgPathC").hide();
			$("#svgLine defs").hide();
			$("#svgLineB defs").hide();
			$("#svgLineC defs").hide();
			$("#svgLine #svgPath").hide();
			//a中心
			var pointdb = [parseInt($("#circle3").css("left").split("px")[0])+3.5,parseInt($("#circle3").css("top").split("px")[0])];
	 		var pointdb_1 = [parseInt($("#circle4").css("left").split("px")[0])+3.5,parseInt($("#circle4").css("top").split("px")[0])+4];
			$("#arr3").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/onUp.png");
  			$("#arr3").css({"left":pointdb[0]-7.5,"top":pointdb[1]-10,"display":"block"});
			$("#arr4").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/onDown.png");
  			$("#arr4").css({"left":pointdb_1[0]-7.5,"top":pointdb_1[1]-20,"display":"block"});
  			//设置连线
  	  		$("#topXian4").css({"display":"block","left":pointdb[0]-2,"top":pointdb[1],"width":"4px","height":pointdb_1[1]-pointdb[1]-10+"px"});
  			//b中心
  			 var pointdbB = [parseInt($("#circleB3").css("left").split("px")[0])+3.5,parseInt($("#circleB3").css("top").split("px")[0])];
			 var pointdbB_1 = [parseInt($("#circleB4").css("left").split("px")[0])+3.5,parseInt($("#circleB4").css("top").split("px")[0])+4];
			$("#arrb3").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/onUp.png");
  			$("#arrb3").css({"left":pointdbB[0]-7.5,"top":pointdbB[1]-10,"display":"block"});
			$("#arrb4").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/onDown.png");
  			$("#arrb4").css({"left":pointdbB_1[0]-7.5,"top":pointdbB_1[1]-20,"display":"block"});
  			//设置连线
  	  		$("#topXianb4").css({"display":"block","left":pointdbB[0]-2,"top":pointdbB[1]-4,"width":"4px","height":pointdbB_1[1]-pointdbB[1]-10+"px"});
  			//a-b数据库
  			var pointC = [parseInt($("#circleC").css("left").split("px")[0])-10,parseInt($("#circleC").css("top").split("px")[0])+3.5];
	 		var pointC1 = [parseInt($("#circleC1").css("left").split("px")[0])+15,parseInt($("#circleC1").css("top").split("px")[0])+3.5];
  			$("#arrc1").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/onLeftc.png");
  			$("#arrc1").css({"left":pointC[0]-10,"top":pointC[1]-7.5,"display":"block"});
  			$("#arrc2").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/onRightc.png");
			$("#arrc2").css({"left":pointC1[0]-10,"top":pointC1[1]-7.5,"display":"block"});
			//设置连线
			$("#topXianc1").css({"display":"block","left":pointC[0]+2,"top":pointC[1]-2,"width":pointC1[0]-pointC[0]+"px","height":"4px"});
		}
  	}
  	
  	//查询双活信息
  	function getDoubleInfo(){
  		$.ajax({
  			url:getContextPath()+"/doublemonCon/getDoubleInfo",
  			type:'POST',
  			data:{},
  			dataType:'json',
  			success:function(data){
  				if(data.core != "0"){
	  				$("#doubleName").html("&nbsp;"+data.name+"&nbsp;");
	  				core = data.core.split(",");
	  				$("#leftCoreInfoA").html("&nbsp;"+core[0]+"表空间使用情况"+"&nbsp;");
	  				$("#leftCoreInfoB").html("&nbsp;"+core[1]+"表空间使用情况"+"&nbsp;");
  				}else{
  					$("#doubleName").html("&nbsp;电力交易双活&nbsp;");
  					$("#leftCoreInfoA").html("&nbsp;"+"表空间使用情况"+"&nbsp;");
	  				$("#leftCoreInfoB").html("&nbsp;"+"表空间使用情况"+"&nbsp;");
  				}
  			}
  		});
  	}
  	
  	//echarts地图
  	function myCharts(myCharts){
  		var geoCoordMap = {
                '上海': [121.4648,31.2891],
                '北京': [116.4551,40.2539]
		};
		var BJData = [
                [{name:'北京',value:70}, {name:'上海'}],
                [{name:'上海',value:70}, {name:'北京'}]
		];
		var convertData = function (data) {
		    var res = [];
		    for (var i = 0; i < data.length; i++) {
		        var dataItem = data[i];
		        var fromCoord = geoCoordMap[dataItem[0].name];
		        var toCoord = geoCoordMap[dataItem[1].name];
		        if (fromCoord && toCoord) {
		            res.push({
		                fromName: dataItem[0].name,
		                toName: dataItem[1].name,
		                coords: [fromCoord, toCoord]
		            });
		        }
		    }
		    return res;
		};
		var series = [];
		[['北京', BJData]].forEach(function (item, i) {
		    series.push(
		    {
		        name: item[2],
		        type: 'lines',
		        zlevel: 2,
		        effect: {
		            show: true,
		            period: 6,
		            trailLength: 0.1,
		            symbol:'arrow',
		            symbolSize: 5
		        },
		        lineStyle: {
		            normal: {
		                color: '#60ff44',
		                width: 1,
		                opacity: 0.4,
		                curveness: 0.2
		            }
		        },
		        data: convertData(item[1])
		    },
		    {
		        type: 'effectScatter',
		        coordinateSystem: 'geo',
		        zlevel: 2,
		        rippleEffect: {
		            brushType: 'stroke'
		        },
		        label: {
		            normal: {
		                show: true,
		                position: 'right',
		                formatter: '{b}'
		            }
		        },
		        symbolSize: function (val) {
		            return 3 + val[2] / 10;
		        },
		        itemStyle: {
		            normal: {
		                color: '#60ff44'
		            }
		        },
		        data: item[1].map(function (dataItem) {
		            return {
		                name: dataItem[0].name,
		                value: geoCoordMap[dataItem[0].name].concat([dataItem[0].value])
		            };
		        })
		    });
		});
		option = {
		    backgroundColor: '#01306b',
		    title : {
		        text: '',
		        subtext: '',
		        left: 'center',
		        textStyle : {
		            color: '#fff'
		        }
		    },
		    tooltip : {
		        trigger: 'item'
		    },
		    geo: {
		        map: 'china',
		        label: {
		            emphasis: {
		                show: false
		            }
		        },
		        aspectScale:0.75,
// 		        roam: true,
		        itemStyle: {
		            normal: {
		                areaColor: '#004882',
		                borderColor: '#20a6C7'
		            },
		            emphasis: {
		                areaColor: '#2a333d'
		            }
		        }
		    },
		    series: series
		};
	  	  myCharts.setOption(option);
  	}
  	//北京地图
  	function beiJingCharts(myCharts){
  		var geoCoordMap = {
                '亦庄': [116.6487,39.6707],
                '白广路': [116.3643,39.8924]
		};
		var BJData = [
                [{name:'亦庄',value:50}, {name:'白广路'}],
                [{name:'白广路',value:50}, {name:'亦庄'}]
		];
		
		var convertData = function (data) {
		    var res = [];
		    for (var i = 0; i < data.length; i++) {
		        var dataItem = data[i];
		        var fromCoord = geoCoordMap[dataItem[0].name];
		        var toCoord = geoCoordMap[dataItem[1].name];
		        if (fromCoord && toCoord) {
		            res.push({
		                fromName: dataItem[0].name,
		                toName: dataItem[1].name,
		                coords: [fromCoord, toCoord]
		            });
		        }
		    }
		    return res;
		};
		
		var series = [];
		[['北京', BJData]].forEach(function (item, i) {
		    series.push(
		    {
		        name: item[2],
		        type: 'lines',
		        zlevel: 2,
		        effect: {
		            show: true,
		            period: 6,
		            trailLength: 0.1,
		            symbol:'arrow',
		            symbolSize: 5
		        },
		        lineStyle: {
		            normal: {
		                color: '#60ff44',
		                width: 1,
		                opacity: 0.4,
		                curveness: 0.2
		            }
		        },
		        data: convertData(item[1])
		    },
		    {
		        type: 'effectScatter',
		        coordinateSystem: 'geo',
		        zlevel: 2,
		        rippleEffect: {
		            brushType: 'stroke'
		        },
		        label: {
		            normal: {
		                show: true,
		                position: 'right',
		                formatter: '{b}'
		            }
		        },
		        symbolSize: function (val) {
		            return 3 + val[2] / 10;
		        },
		        itemStyle: {
		            normal: {
		                color: '#60ff44'
		            }
		        },
		        data: item[1].map(function (dataItem) {
		            return {
		                name: dataItem[0].name,
		                value: geoCoordMap[dataItem[0].name].concat([dataItem[0].value])
		            };
		        })
		    });
		});
		
		option = {
		    backgroundColor: '#01306b',
		    title : {
		        text: '',
		        subtext: '',
		        left: 'center',
		        textStyle : {
		            color: '#fff'
		        }
		    },
		    tooltip : {
		        trigger: 'item'
		    },
		    geo: {
		        map: 'beijing',
		        label: {
		            emphasis: {
		                show: false
		            }
		        },
		        aspectScale:0.75,
// 		        roam: true,
		        itemStyle: {
		            normal: {
		                areaColor: '#004882',
		                borderColor: '#20a6C7'
		            },
		            emphasis: {
		                areaColor: '#2a333d'
		            }
		        }
		    },
		    series: series
		};
	  	  myCharts.setOption(option);
  	}
  	//A中心获取饼图数据
  	function getDatebase(){
  		$.ajax({
  			url : getContextPath() + "/doublemonCon/getDatebase",
  			type : "post",
  			data : {
  				"core_tagging" : "L"
  			},
  			dataType : "json",
  			success : function(result) {
  				echarts1(result[0].uid, result[0].name);
  				echarts1_1(result[1].uid, result[1].name);
  			}
  		});
  	}
  	//B中心获取饼图数据
  	function getDatebase1(){
  		$.ajax({
  			url : getContextPath() + "/doublemonCon/getDatebase",
  			type : "post",
  			data : {
  				"core_tagging" : "R"
  			},
  			dataType : "json",
  			success : function(result) {
  				echarts2(result[0].uid, result[0].name);
  				echarts2_1(result[1].uid, result[1].name);
  			}
  		});
  	}
  	//饼图
  	function echarts1(uid, name){
  		var seriesData1 = [];
  		//获取数据
  		$.ajax({
  			url : getContextPath() + "/doublemonCon/getTablespace",
  			type : "post",
  			async : false,
  			data : {
  				"uid" : uid
  			},
  			dataType : "json",
  			success : function(result) {
  				seriesData1[0] = {value:result.used_rate, name:'使用率（%）'};
  				seriesData1[1] = {value:result.free_rate, name:'空闲率（%）'};
  			}
  		});
  		var echarts1 = echarts.init(document.getElementById('kongJianChart'));
  		var option = {
  			    title : {
  			        text: name,
  			        x:'center',
  			        bottom :0,
  			        textStyle:{
			        	fontFamily: 'Arial, Verdana, sans...',
			        	color: '#87bcc7',
			        },
  			    },
  			  	color:['#46D083','#00A59E'],
  			    tooltip : {
  			        trigger: 'item',
  			        formatter: "{a} <br/>{b} : {c}"
  			    },
  			    legend: {
  			        orient: 'vertical',
  			        left: 'right',
  			        data: ['使用率（%）','空闲率（%）'],
  			      	textStyle:{
			        	color: '#87bcc7',
			        	fontSize: 11
			        },
  			    },
  			    series : [
  			        {
  			            name: '表空间使用率',
  			            type: 'pie',
	  			        label:{
	  		                normal:{
	  		                show:false ,
	  		                position : 'outside'
	  		                },
	  		                emphasis:{
	  		                show :false
	  		                }
	  		            },
  			            radius : '55%',
  			            center: ['50%', '60%'],
  			            data: seriesData1,//value
  			            itemStyle: {
  			                emphasis: {
  			                    shadowBlur: 10,
  			                    shadowOffsetX: 0,
  			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
  			                }
  			            },
//   			          label: {
//   		                normal: {
//   		                    position: 'inner'
//   		                }
//   		            },
  			        },
  			    ]
  			};
  		echarts1.setOption(option);
  	}
  	function echarts1_1(uid, name){
  		var seriesData2 = [];
  		//获取数据
  		$.ajax({
  			url : getContextPath() + "/doublemonCon/getTablespace",
  			type : "post",
  			async : false,
  			data : {
  				"uid" : uid
  			},
  			dataType : "json",
  			success : function(result) {
  				seriesData2[0] = {value:result.used_rate, name:'使用率（%）'};
  				seriesData2[1] = {value:result.free_rate, name:'空闲率（%）'};
  			}
  		});
  		var echarts1 = echarts.init(document.getElementById('kongJianChart_1'));
  		var option = {
  			    title : {
  			        text: name,
  			        x:'center',
  			        bottom : 0,
  			        textStyle:{
			        	fontFamily: 'Arial, Verdana, sans...',
			        	color: '#87bcc7',
			        },
  			    },
  			  	color:['#46D083','#00A59E'],
  			    tooltip : {
  			        trigger: 'item',
  			        formatter: "{a} <br/>{b} : {c}"
  			    },
  			    legend: {
  			        orient: 'vertical',
  			        left: 'right',
  			        data: ['使用率（%）','空闲率（%）'],
  			      	textStyle:{
			        	color: '#87bcc7',
			        	fontSize: 11
			        },
  			    },
  			    series : [
  			        {
			            name: '表空间使用率',
			            type: 'pie',
			            label:{
	  		                normal:{
	  		                show:false ,
	  		                position : 'outside'
	  		                },
	  		                emphasis:{
	  		                show :false
	  		                }
	  		            },
			            radius : '55%',
			            center: ['50%', '60%'],
			            data: seriesData2,//value
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
  		echarts1.setOption(option);
  	}
  	function echarts2(uid, name){
		var seriesData1 = [];
  		//获取数据
  		$.ajax({
  			url : getContextPath() + "/doublemonCon/getTablespace",
  			type : "post",
  			async : false,
  			data : {
  				"uid" : uid
  			},
  			dataType : "json",
  			success : function(result) {
  				seriesData1[0] = {value:result.used_rate, name:'使用率（%）'};
  				seriesData1[1] = {value:result.free_rate, name:'空闲率（%）'};
  			}
  		});
  		var echarts2 = echarts.init(document.getElementById('kongJianChart2'));
  		var option = {
  			    title : {
  			    	text: name,
  			        x:'center',
  			        bottom : 0,
  			        textStyle:{
			        	fontFamily: 'Arial, Verdana, sans...',
			        	color: '#87bcc7',
			        }
  			    },
  			  	color:['#46D083','#00A59E'],
  			    tooltip : {
  			        trigger: 'item',
  			        formatter: "{a} <br/>{b} : {c}"
  			    },
  			    legend: {
  			        orient: 'vertical',
  			        left: 'right',
  			        data: ['使用率（%）','空闲率（%）'],
  			      	textStyle:{
			        	color: '#87bcc7',
			        	fontSize: 11
			        },
  			    },
  			    series : [
  			        {
  			            name: '表空间使用率',
  			            type: 'pie',
	  			        label:{
	  		                normal:{
	  		                show:false ,
	  		                position : 'outside'
	  		                },
	  		                emphasis:{
	  		                show :false
	  		                }
	  		            },
  			            radius : '55%',
  			            center: ['50%', '60%'],
  			            data:seriesData1,//value
  			            itemStyle: {
  			                emphasis: {
  			                    shadowBlur: 10,
  			                    shadowOffsetX: 0,
  			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
  			                }
  			            }
  			        },
  			    ]
  			};
  		echarts2.setOption(option);
  	}
  	function echarts2_1(uid, name){
		var seriesData2 = [];
  		//获取数据
  		$.ajax({
  			url : getContextPath() + "/doublemonCon/getTablespace",
  			type : "post",
  			async : false,
  			data : {
  				"uid" : uid
  			},
  			dataType : "json",
  			success : function(result) {
  				seriesData2[0] = {value:result.used_rate, name:'使用率（%）'};
  				seriesData2[1] = {value:result.free_rate, name:'空闲率（%）'};
  			}
  		});
  		var echarts2 = echarts.init(document.getElementById('kongJianChart_2'));
  		var option = {
  			    title : {
  			    	text: name,
  			        x:'center',
  			        bottom : 0,
  			        textStyle:{
			        	fontFamily: 'Arial, Verdana, sans...',
			        	color: '#87bcc7',
			        }
  			    },
  			  	color:['#46D083','#00A59E'],
  			    tooltip : {
  			        trigger: 'item',
  			        formatter: "{a} <br/>{b} : {c}"
  			    },
  			    legend: {
  			        orient: 'vertical',
  			        left: 'right',
  			      	data: ['使用率（%）','空闲率（%）'],
  			      	textStyle:{
			        	color: '#87bcc7',
			        	fontSize: 11
			        },
  			    },
  			    series : [
  			        {
  			            name: '表空间使用率',
  			            type: 'pie',
	  			        label:{
	  		                normal:{
	  		                show:false ,
	  		                position : 'outside'
	  		                },
	  		                emphasis:{
	  		                show :false
	  		                }
	  		            },
  			            radius : '55%',
  			            center: ['50%', '60%'],
  			            data:seriesData2,
//   			            data:[
//   			                {value:335, name:'空闲率'},
//   			                {value:310, name:'使用率'},
//   			            ],
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
  		echarts2.setOption(option);
  	}
  	
  	//获取在线人数
  	function getUserNumbers(){
  		$.ajax({
  			url:getContextPath()+"/doublemonCon/userconut",
  			type:'POST',
  			data:{},
  			dataType:'json',
  			success:function(data){
  				show_num(data+"");
//   				show_num1(data+"");
  			}
  		});
  	}
  	
  	//内网在线人数展示
	var sum = 0;
	var time = 1000;
	function show_num(n) {
		$(".t_num.t_num1").css("visibility","hidden");
		sum=n;
		var it = $(".t_num1 i");
		var len = String(n).length;
		for(var i = 0; i < len; i++) {
			if(it.length <= i) {
				$(".t_num1").append("<i></i>");
			}
			var num = String(n).charAt(i);
			//根据数字图片的高度设置相应的值
			var y = -parseInt(num) * 35;
			var obj = $(".t_num1 i").eq(i);
			obj.animate({
				backgroundPosition: '(0 ' + String(y) + 'px)'
			}, time, 'swing', function() {});
		}
		setTimeout(function(){
	  		$(".t_num.t_num1").css("visibility","visible");
	  		var t_num_w = $(".t_num.t_num1").width();
	  		var core_line_w = $("#core_line").width();
	  		$(".t_num.t_num1").css("left",core_line_w/2 - t_num_w/2);
	  		$("#all .t_num i").css("background","url("+getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/number1.png) no-repeat");
  		}, 1);
	}
	//外网在线人数
	function show_num1(n) {
		$(".t_num.t_num1").css("visibility","hidden");
		sum=n;
		var it = $(".t_num1 i");
		var len = String(n).length;
		for(var i = 0; i < len; i++) {
			if(it.length <= i) {
				$(".t_num1").append("<i></i>");
			}
			var num = String(n).charAt(i);
			//根据数字图片的高度设置相应的值
			var y = -parseInt(num) * 35;
			var obj = $(".t_num1 i").eq(i);
			obj.animate({
				backgroundPosition: '(0 ' + String(y) + 'px)'
			}, time, 'swing', function() {});
		}
		setTimeout(function(){
	  		$(".t_num.t_num1").css("visibility","visible");
	  		var t_num_w = $(".t_num.t_num1").width();
	  		var core_line_w = $("#core_line").width();
	  		$(".t_num.t_num1").css("left",core_line_w/2 - t_num_w/2);
	  		$("#all1 .t_num i").css("background","url("+getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/number1.png) no-repeat");
  		}, 1);
	}
	
	//一键切换页面
	function switchTab(){
		//判断是否满足切换条件
		var double_id=$("#double_id").val();//双活Id
		if(double_id==="" || typeof(double_id)==="undefined"){
			layer.alert("当前没有要执行切换操作的双活!");
			return;
		}
		//两中心数据库组id
		var group_id_a=$(".core_A #group_db #servers img:eq(0)").attr("group_id");
		if(group_id_a==="" || typeof(group_id_a)==="undefined"){
			layer.alert($(".core_A #core_name div").html()+"没有需要切换的设备!");
			return;
		}
		var group_id_b=$(".core_B #group_db #servers img:eq(0)").attr("group_id");
		if(group_id_b==="" || typeof(group_id_b)==="undefined"){
			layer.alert($(".core_B #core_name div").html()+"没有需要切换的设备!");
			return;
		}
		layer.open({
		    type: 2,
		    title: "一键切换", 
		    fix: false,
		    shadeClose: false,
		    area: ['900px','530px'],
// 		    area: ['70%','85%'],
		    moveOut: true,
		    content: [getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/switchTab.jsp",'no']
		});
	}
  </script>
</html>
