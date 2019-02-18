<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/layui/css/layui.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/baselinemgt/OneKey/css/bootstrap.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/baselinemgt/OneKey/css/style.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/healthScore/js/heathScore.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/echarts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/echarts-liquidfill.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/layui/layui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/layui/lay/modules/element.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/OneKey/js/prefixfree.min.js"></script>
<style>
a:hover {
    text-decoration: none;
}
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
   background-color: #3c8dbc;
}
body{
/* 	background-image:''; */
/*  	background-image:url(../jsp/pages/baselinemgt/OneKey/img/background.png); */
	background-size: 100% 100%;  
  	-moz-background-size: 100% 100%;  
  	-webkit-background-size: 100% 100%;  
}
h1{
	margin-top: 22%;
    margin-left: 43%;
    position: absolute;
    color: #499af5;
}
.view {
	width: 100%;
	height: 100%;
	position: absolute;
}
.top{
	width:100%;
	height: 30%;
}
.oneKeybtn{
	width:150px;
	height:40px;
	background-color:#0ad6f3;
	float:left;
	text-align: center;
    border-radius: 15px;
    color: #fff;
    line-height: 40px;
    font-size: 20px;
    font-weight: bold;
    margin-top: 6%;
    cursor:pointer;
}
.bottom{
	width:100%;
	height: 70%;
	overflow: auto;
	background-color: #f5f5f5;
	    /* 透明 **/
	opacity: 0.85;
    filter:alpha(Opacity=85);
    -moz-opacity: 0.85;
    display: none;
}
.pictuer{
	width:25%;
	height:100%;
	float:left;
	margin-left: 30px;
}
.ingstyle:hover{
	transform:scale(1.05,1.05);
}
.ingstyle{
	width:50%;
	height:50%;
	margin-left: 25%;
    margin-top: 10%;
}
.imgtext{
	text-align:center;
	color:#7b7b7b;
	width: 100%;
    height: 20%;
    margin-top: 10px;
    font-size: 13px;
}
.bottomShade{
	width:100%;
	height:31%;
	background-color:#f5f5f5;
	z-index: -1;
	/* 透明 **/
    filter:alpha(Opacity=75);
    -moz-opacity: 0.75;
    opacity: 0.75;
	    
	bottom: 0;
    position: fixed;
}
.leida{
	background-image: linear-gradient(0deg, transparent 24%, rgba(32, 255, 77, 0.15) 25%, rgba(32, 255, 77, 0.15) 26%, transparent 27%, transparent 74%, rgba(32, 255, 77, 0.15) 75%, rgba(32, 255, 77, 0.15) 76%, transparent 77%, transparent), linear-gradient(90deg, transparent 24%, rgba(32, 255, 77, 0.15) 25%, rgba(32, 255, 77, 0.15) 26%, transparent 27%, transparent 74%, rgba(32, 255, 77, 0.15) 75%, rgba(32, 255, 77, 0.15) 76%, transparent 77%, transparent);
    background-size: 2rem 2rem;
    background-position: -5.2rem -5.2rem;
    width: 150px;
    height: 150px;
    position: absolute;
    padding: 0;
    margin: 0;
    background-color: #395267;
    font-size: 10px;
    border-radius: 50%;
    margin-left: 10%;
    margin-top: 2%;
    z-index: 2;
    display:none;
}
.radar {
  background: -webkit-radial-gradient(center, rgba(32, 255, 245, 0.3) 0%, rgba(32, 255, 77, 0) 85%), 
  			-webkit-repeating-radial-gradient(rgba(32, 255, 77, 0) 5.8%, rgba(32, 255, 77, 0) 18%, #0ad6f3 18.6%, rgba(32, 255, 77, 0) 18.9%), 
  			-webkit-linear-gradient(90deg, rgba(32, 255, 77, 0) 49.5%, #0ad6f3 50%, #0ad6f3 50%, rgba(32, 255, 77, 0) 50.2%), 
  			-webkit-linear-gradient(0deg, rgba(32, 255, 77, 0) 49.5%, #0ad6f3 50%, #0ad6f3 50%, rgba(32, 255, 77, 0) 50.2%);
  width: 75vw;
  height: 75vw;
  max-height: 24vh;
  max-width: 24vh;
  position: relative;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  border-radius: 50%;
  border: 0.2rem solid #0ad6f3;
  overflow: hidden;
}
.radar:before {
  content: ' ';
  display: block;
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  animation: blips 5s infinite;
  animation-timing-function: linear;
  animation-delay: 1.4s;
}
.radar:after {
  content: ' ';
  display: block;
  background-image: linear-gradient(44deg, rgba(0, 255, 51, 0) 50%, #0ad6f3 100%);
  width: 50%;
  height: 50%;
  position: absolute;
  top: 0;
  left: 0;
  animation: radar-beam 5s infinite;
  animation-timing-function: linear;
  transform-origin: bottom right;
  border-radius: 100% 0 0 0;
}
@keyframes radar-beam {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
@keyframes blips {
  14% {
    background: radial-gradient(2vmin circle at 75% 70%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%);
  }
  14.0002% {
    background: radial-gradient(2vmin circle at 75% 70%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%), radial-gradient(2vmin circle at 63% 72%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%);
  }
  25% {
    background: radial-gradient(2vmin circle at 75% 70%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%), radial-gradient(2vmin circle at 63% 72%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%), radial-gradient(2vmin circle at 56% 86%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%);
  }
  26% {
    background: radial-gradient(2vmin circle at 75% 70%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%), radial-gradient(2vmin circle at 63% 72%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%), radial-gradient(2vmin circle at 56% 86%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%);
    opacity: 1;
  }
  100% {
    background: radial-gradient(2vmin circle at 75% 70%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%), radial-gradient(2vmin circle at 63% 72%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%), radial-gradient(2vmin circle at 56% 86%, #ffffff 10%, #0ad6f3 30%, rgba(255, 255, 255, 0) 100%);
    opacity: 0;
  }
}

/* 动画 **/
.base {
  height: 9em;
  left: 50%;
  margin: -7.5em;
  padding: 3em;
  position: absolute;
  top: 40%;
  width: 15em;
  transform: rotateX(45deg) rotateZ(45deg);
  transform-style: preserve-3d;
  font-size: 10px;
}

.cube,
.cube:after,
.cube:before {
  content: '';
  float: left;
  height: 3em;
  position: absolute;
  width: 3em;
}

/* Top */
.cube {
  background-color: #05afd1;
  position: relative;
  transform: translateZ(3em);
  transform-style: preserve-3d;
  transition: .25s;
  box-shadow: 13em 13em 1.5em rgba(0, 0, 0, 0.1);
  animation: anim 1s infinite;
}
.cube:after {
  background-color: #049dbc;
  transform: rotateX(-90deg) translateY(3em);
  transform-origin: 100% 100%;
}
.cube:before {
  background-color: #048ca7;
  transform: rotateY(90deg) translateX(3em);
  transform-origin: 100% 0;
}
.cube:nth-child(1) {
  animation-delay: 0.05s;
}
.cube:nth-child(2) {
  animation-delay: 0.1s;
}
.cube:nth-child(3) {
  animation-delay: 0.15s;
}
.cube:nth-child(4) {
  animation-delay: 0.2s;
}
.cube:nth-child(5) {
  animation-delay: 0.25s;
}
.cube:nth-child(6) {
  animation-delay: 0.3s;
}
.cube:nth-child(7) {
  animation-delay: 0.35s;
}
.cube:nth-child(8) {
  animation-delay: 0.4s;
}
.cube:nth-child(9) {
  animation-delay: 0.45s;
}

@keyframes anim {
  50% {
    transform: translateZ(0.5em);
  }
}
/* 折叠面板**/
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
#zhejie{
	width: 100%;
	height: 100%;
	overflow: auto;
	
/* 	background-color: #fff; */
/*     filter: alpha(Opacity=75); */
/*     -moz-opacity: 0.75; */
/*     opacity: 0.8; */
}
.layui-collapse {
    border: none;
    border-radius: 0px;
}
.layui-colla-title {
	font-weight: bold;
    font-size: 17px;
    height: 32px;
    line-height: 32px;
    background-color: #8bc9f3;
}
.indexInfo{
	font-size: 16px;
    color: black;
}
.serverSpan{
	display:block;
	float:left;
}
.warn {
    width: 45%;
    float: left;
    font-size: 14px;
    margin-left: 30px;
}

/* 加载中 **/
.spinner {
  margin: 100px auto;
  width: 50px;
  height: 60px;
  text-align: center;
  font-size: 10px;
}
 
.spinner > div {
  background-color: #499af5;
  height: 100%;
  width: 6px;
  display: inline-block;
  -webkit-animation: stretchdelay 1.2s infinite ease-in-out;
  animation: stretchdelay 1.2s infinite ease-in-out;
}
 
.spinner .rect2 {
  -webkit-animation-delay: -1.1s;
  animation-delay: -1.1s;
}
 
.spinner .rect3 {
  -webkit-animation-delay: -1.0s;
  animation-delay: -1.0s;
}
 
.spinner .rect4 {
  -webkit-animation-delay: -0.9s;
  animation-delay: -0.9s;
}
 
.spinner .rect5 {
  -webkit-animation-delay: -0.8s;
  animation-delay: -0.8s;
}
 
@-webkit-keyframes stretchdelay {
  0%, 40%, 100% { -webkit-transform: scaleY(0.4) } 
  20% { -webkit-transform: scaleY(1.0) }
}
 
@keyframes stretchdelay {
  0%, 40%, 100% {
    transform: scaleY(0.4);
    -webkit-transform: scaleY(0.4);
  }  20% {
    transform: scaleY(1.0);
    -webkit-transform: scaleY(1.0);
  }
}
.databaseSco{
	display:block;
	width:30%;
	float: left;
}
.titals{
	font-weight: 100;
    font-size: 15px;
}

</style>
<title>一键检测</title>
</head>
<body>
	<div id="bodybak" style="width:100%;height:100%;">
	<div class="leida">
		<div class="radar"></div>
	  	<div style="text-align:center;"></div>
  	</div>
  	
	<div class="view">
		<div class="top">
			<div id="circleDev" style="height:100%;width:70%;float:left;"></div>
			<div class="oneKeybtn">一 键 检 测</div>
		</div>
		<div class="bottom">
			<!-- 进度条 -->
<!-- 			<div class="progress" id="progress" style="display:none;width:100%;"> -->
<!-- 				  <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" id="jindu" style="width: 0%"> -->
<!-- 				    <span class="sr-only"></span> -->
<!-- 				  </div> -->
<!-- 			</div> -->
			<!-- 开始动画 -->
			<div class="donghua" style="width:100%;height:90%;display:none;">
				<div class='base'>
				  <div class='cube'></div>
				  <div class='cube'></div>
				  <div class='cube'></div>
				  <div class='cube'></div>
				  <div class='cube'></div>
				  <div class='cube'></div>
				  <div class='cube'></div>
				  <div class='cube'></div>
				  <div class='cube'></div>
			   </div>
			   <h1>Loading ......</h1>
			</div>
			<!-- 折叠 -->
			<div class="layui-collapse" style="display:none;" id="zhejie">
				  <div class="layui-colla-item" id="network" style="display:none;">
				    <h2 class="layui-colla-title">网络<span class='titals'></h2>
				    <div class="layui-colla-content layui-show"  id="network1" >
						<!-- 日志信息 -->
						<div class='indexInfo' style='width:80%;margin-left:10%;'>
							<!-- 加载中 -->
							<div class="spinner" id="spinner1">
							  <div class="rect1"></div>
							  <div class="rect2"></div>
							  <div class="rect3"></div>
							  <div class="rect4"></div>
							  <div class="rect5"></div>
							  <h1 style="position: absolute;margin-top: -10%;margin-left: -65%;width: 150px;">Loading ......</h1>
							</div>
							<div class="bottomDiv" id="indexInfo3"></div>
						</div>
					</div>
				  </div>
				  <div class="layui-colla-item" id="servers" style="display:none;">
				    <h2 class="layui-colla-title">服务器<span class='titals'></h2>
				    <div class="layui-colla-content layui-show"  id="servers1">
						<!-- 日志信息 -->
						<div class='indexInfo' style='width:80%;margin-left:10%;height:80%;'>
							<!-- 加载中 -->
							<div class="spinner" id="spinner">
							  <div class="rect1"></div>
							  <div class="rect2"></div>
							  <div class="rect3"></div>
							  <div class="rect4"></div>
							  <div class="rect5"></div>
							  <h1 style="position: absolute;margin-top: -10%;margin-left: -65%;width: 150px;">Loading ......</h1>
							</div>
							<div class="bottomDivindexInfo2" id="indexInfo2"></div>
						</div>
					</div>
				  </div>
				  
				  <div class="layui-colla-item" id="database">
				    <h2 class="layui-colla-title">数据库<span class='titals'></span></h2>
				    <div class="layui-colla-content layui-show" id="database1">
						<!-- 日志信息 -->
						<div class='indexInfo' style='width:80%;margin-left:10%;display:none;'>
							<div class="bottomDiv" id="indexInfo1"></div>
						</div>
					</div>
				  </div>
			</div>
		</div>
		<!-- 底部白色区域 -->
		<div class="bottomShade">
			<div class="pictuer">
				<img class="ingstyle" id="fwq" src=""/></br>
				<div class="imgtext"><span>服务器</span></div>
			</div>
			<div class="pictuer">
				<img class="ingstyle" id="sjk" src=""/></br>
				<div class="imgtext"><span>数据库服务器</span></div>
			</div>
			<div class="pictuer">
				<img class="ingstyle" id="wl" src=""/></br>
				<div class="imgtext"><span>网络</span></div>
			</div>
		</div>
	</div>
	</div>
</body>
<script type="text/javascript">
	//总分100分 数据库占40% 服务器40% 网络20%
	var score = "100";
	$(function() {
		
// 		document.body.style.background = "url("+getContextPath()+"/jsp/pages/baselinemgt/OneKey/img/background.png)";
		$("body").attr("background-image","url("+getContextPath()+"/jsp/pages/baselinemgt/OneKey/img/background.png)");
		$("#fwq").attr("src", getContextPath()+"/jsp/pages/baselinemgt/OneKey/img/server.png");
		$("#sjk").attr("src", getContextPath()+"/jsp/pages/baselinemgt/OneKey/img/datebase.png");
		$("#wl").attr("src", getContextPath()+"/jsp/pages/baselinemgt/OneKey/img/newwork.png");
		
		initCricle(score);
		$(".oneKeybtn").click(function(){
			var htm = $(".oneKeybtn").text();
			//重新开始检测
			if(htm == "检 测 完 成" || htm == "正 在 检 测"){
				layer.confirm("确定重新开始一键检测？",{
					btn:['确定','取消']
				},function(){
					layer.closeAll();
					$(".indexInfo").empty();
					$("#database #database1 .indexInfo").html("<div class='bottomDiv' id='indexInfo1'></div>");
					$("#servers #servers1 .indexInfo").html("<div class='spinner' id='spinner'><div class='rect1' style='margin-left: 3px;'></div><div class='rect2' style='margin-left: 3px;'></div><div class='rect3' style='margin-left: 3px;'></div><div class='rect4' style='margin-left: 3px;'></div><div class='rect5' style='margin-left: 3px;'></div><h1 style='position: absolute;margin-top: -10%;margin-left: -65%;width: 150px;'>Loading ......</h1></div><div class='bottomDivindexInfo2' id='indexInfo2'></div>");
					$("#servers").hide();
					$("#network #network1 .indexInfo").html("<div class='spinner' id='spinner1'><div class='rect1' style='margin-left: 3px;'></div><div class='rect2' style='margin-left: 3px;'></div><div class='rect3' style='margin-left: 3px;'></div><div class='rect4' style='margin-left: 3px;'></div><div class='rect5' style='margin-left: 3px;'></div><h1 style='position: absolute;margin-top: -10%;margin-left: -65%;width: 150px;'>Loading ......</h1></div><div class='bottomDiv' id='indexInfo3'></div>");
					$("#network").hide();
					$("#zhejie").hide();
					oneKeyClick();
				},function(){
					layer.closeAll();
				});
			}else{
				oneKeyClick();
			}
		});
	});
	
	//一键检测
	function oneKeyClick(){
		$(".oneKeybtn").html("正 在 检 测");
		$(".leida").show();
// 		$(".bottomShade").css("height","60%");
		$(".bottomShade").hide();
		$(".bottom").show();
		$(".pictuer").hide();
		$("#progress").show();
		$("#database .titals").html("&nbsp;&nbsp;&nbsp;&nbsp;正在进行数据库检测......");
		$("#jindu").css("width","2%");
		$(".donghua").show();
		setTimeout(function(){
				//添加评分任务
				$.ajax({
			  		url : getContextPath()+"/heathScoreViewCon/doScore",
			  		type : "post",
			  		data : {},
			  		dataType : 'json',
			  		beforeSend:function(){
			  			$("#jindu").css("width","5%");
			  		},
			  		success : function(result) {
			  			$("#jindu").css("width","10%");
			  			//开始评分任务
			  			startScore(result);
			  		},
			  		error : function() {
			  			layer.alert("评分任务添加失败，请刷新页面后再试！");
			  		}
			  	});
		 }, 1000);
	}
	
	var idsStr = "";
	//评分任务添加完成后开始评分
	function startScore(ids){
		for(var i = 0; i < ids.length; i++){
			idsStr += ids[i] + ",";
		}
		idsStr = idsStr.substring(0, idsStr.length-1);
		setTimeout(function(){
			$.ajax({
				url : getContextPath() + "/heathScoreViewCon/startScore",
				type : "post",
				data : {ids : idsStr},
				dataType : "text",
				beforeSend:function(){
		  			$("#jindu").css("width","15%");
		  			$(".donghua").hide();
		  			$("#zhejie").show();
		  			//折叠面板
		  			layui.use('element', function(){
		  			  var element = layui.element();
		  			});
		  			getIndexs();
		  		},
				success : function(result) {
					$(".bottom").append(result);
				},
				error : function() {
					
				}
			});
		}, 1000);
	}
	
	var res;
	var reslength;
	function doit(index){
		return res[index];
	}
	
	//查询指标数据
	function getIndexs(){
		$.ajax({
			url : getContextPath() + "/indexCon/search",
			type : "post",
			data : {index_type:"200"},
			dataType : "json",
			async : false,
			beforeSend:function(){
				$("#jindu").css("width","25%");
			},
			success : function(result) {
				$("#jindu").css("width","30%");
				res = result;
				reslength = result.length;
				$(".indexInfo").show();
				var index = 0;
				var forDoit = setInterval(function(){
					if(index >= reslength){
						$("#jindu").css("width","35%");
						$("#database .titals").html("&nbsp;&nbsp;&nbsp;&nbsp;数据库检测完成！");
						window.clearInterval(forDoit);
						//查询得分
// 						getScore();
						
						getServers();
						return;
					}
					var show = doit(index);
					var divClass = "div"+index+"";
					var str = "<div class='"+divClass+"' style='margin-top: 5px;margin-left: 30px;'><span style='width:30%;display: block;float: left;'>"+show.index_id+"</span>"+show.description+"</div>";
					if(index == 0){
						$("#indexInfo1").before(str);
					}else{
						var sum = parseInt(index) - 1;
						divClass = "div"+sum+"";
						$("."+divClass+"").before(str);
					}
					index ++;
				}, 50);
			},
			error : function() {
				
			}
		});
	}
	
	//服务器评分
	function getServers(){
		$("#servers .titals").html("&nbsp;&nbsp;&nbsp;&nbsp;正在进行服务器检测......");
		$("#jindu").css("width","40%");
		$("#servers").show();
		$("#spinner").show();
		$.ajax({
			url : getContextPath() + "/heathScoreViewCon/getServers",
			type : "post",
			data : {},
			dataType : "json",
			beforeSend:function(){
				$("#jindu").css("width","45%");
			},
			success : function(result) {
				$("#jindu").css("width","50%");
				res = result;
				reslength = result.length;
				var index = 0;
				var forDoit = setInterval(function(){
					if(index >= reslength){
						$("#servers .titals").html("&nbsp;&nbsp;&nbsp;&nbsp;服务器检测完成！");
						$("#jindu").css("width","70%");
						window.clearInterval(forDoit);
						getNetwork();
						return;
					}else{
						var show = doit(index);
						var divClass = "divser"+index+"";
						var str = "<div class='"+divClass+"' style='margin-top: 5px;height:130px;'><span class='warn' style='font-weight: bold;font-size: 15px;'>统一ID："+show.uid+"</span><span class='warn' style='font-weight: bold;font-size: 15px;'>主机名："+show.name+"</span></div>";
						//隔离线
						if(index != 0){
							str += "<hr width='100%'>";
						}
						getWarnlog(show.uid,divClass,str,index);
						index ++;
					}
				}, 3000);
			},
			error : function() {
				
			}
		});
	}
	
	var serverScores = 100;
	var delScore = 2;
	//获取告警信息
	function getWarnlog(uid, divClass, str, index){
		$.ajax({
			url:getContextPath()+"/heathScoreViewCon/searchCpuTemp",
			type:"post",
			data:{
				uid : uid
			},
			success:function(result){
				$("#spinner").hide();
				$("#jindu").css("width","60%");
				//服务器数据
				if(index == 0){
					$(".bottomDivindexInfo2").before(str);
				}else{
					var sum = parseInt(index) - 1;
					divClass = "divser"+sum+"";
					$("."+divClass+"").before(str);
				}
				divClass = "divser"+index+"";
				
				//CPU温度的展示
				var templen = result.cputemp.length/2;
				var cpustr = "<div id='temppvalue' class='warn'>CPU温度(℃)：";
				if(templen>0){
					for(var i=0;i<templen;i++){
						if(parseInt(result.cputemp[i*2]) > 65){
							cpustr += "<span style='color:red;'>"+result.cputemp[i*2] + "</span> ";
							serverScores = parseInt(serverScores) - parseInt(delScore);//扣分
						}else{
							cpustr += result.cputemp[i*2] + " ";
						}
					}
						cpustr +="</div>";
					$("."+divClass+"").append(cpustr);
				}else{
					$("."+divClass+"").append(cpustr);
				}
				//电源功耗的展示
				var conslen = result.cpucons.length/2;
				var cpustr1 = "<div id='cvtxt' class='warn'>电源功耗(kW)：";
				var powerstr = "<div id='powerdvalue' class='warn'>电源状态：";
				if(conslen>0){
					for(var i=0;i<conslen;i++){
						cpustr1 += result.cpucons[2*i] + " ";
						if(result.cpucons[2*i]>0){
							powerstr +="ok" + " ";
						}
					}
						cpustr1 += "</div>";
						powerstr+="</div>";
					$("."+divClass+"").append(cpustr1);
					$("."+divClass+"").append(powerstr);
				}else{
					$("."+divClass+"").append(cpustr1);
					$("."+divClass+"").append(powerstr);
				}
				//电源风扇状态展示
				var volelen = result.vols.length;
				var volestr = "<div id='voledvalue' class='warn'>电源风扇状态：";
				if(volelen>0){
					for(var i=0;i<volelen;i++){
						if(result.vols[i] == 'unnormal'){
							volestr += "";
						}else{
							if(result.vols[i] != "ok"){
								volestr += "<span style='color:red;'>"+result.vols[i] + "</span> ";
								serverScores = parseInt(serverScores) - parseInt(delScore);//扣分
							}else{
								volestr += result.vols[i] + " ";
							}
						}
					}
					volestr += "</div>";
					$("."+divClass+"").append(volestr);
				}else{
					$("."+divClass+"").append(volestr);
				}
				//机箱温度的展示
				var boxlen = result.inputtemp.length/2;
				var boxstr = "<div id='bv_txt' class='warn'>机箱温度(℃)：";
				if(boxlen>0){
					for(var i=0;i<boxlen;i++){
						if(parseInt(result.inputtemp[2*i]) > 65){
							boxstr += "<span style='color:red;'>"+result.inputtemp[2*i] + " "+"</span></div>";
							serverScores = parseInt(serverScores) - parseInt(delScore);//扣分
						}else{
							boxstr += "<span>"+result.inputtemp[2*i] + " "+"</span></div>";
						}
					}
						boxstr += "</div>";
					$("."+divClass+"").append(boxstr);
				}else{
					$("."+divClass+"").append(boxstr);
				}
				
				//风扇转速的展示
				var fanlen = result.fan.length/2;
				var fanstr = "<div id='fandval' class='warn'>风扇转速(RPM)：";
				if(fanlen>0){
					for(var i=0;i<fanlen;i++){
						fanstr += result.fan[2*i] + " ";
					}
						fanstr += "</div>";
					$("."+divClass+"").append(fanstr);
				}else{
					$("."+divClass+"").append(fanstr);
				}
				//查询CUP使用率，若有则显示对应数值，没有则显示为0
				var cpusageData = "<div id='cpuused' class='warn'>CUP使用率（%）：";
				if(result.cpuuseage.length>0){
					if(parseInt(result.cpuuseage[0]) > 80){
						cpusageData += "<span style='color:red;'>"+result.cpuuseage[0]+"</span>";
						serverScores = parseInt(serverScores) - parseInt(delScore);//扣2分
					}else{
						cpusageData += result.cpuuseage[0];
					}
				}else{
					cpusageData += 0 ;
				}
					cpusageData += "</div>" ;
				$("."+divClass+"").append(cpusageData);
				
				//查询内存使用率，若有则显示对应数值，没有则显示为0
				var memusageData = "<div id='neiused' class='warn'>内存使用率（%）：";
				if(result.memuseage.length>0){
					if(parseInt(result.memuseage[0]) > 80){
						memusageData += "<span style='color:red;'>"+result.memuseage[0]+"</span>";
						serverScores = parseInt(serverScores) - parseInt(delScore);//扣2分
					}else{
						memusageData += "<span>"+result.memuseage[0]+"</span>";
					}
				}else{
					memusageData += 0 ;
				}
					memusageData += "</div>" ;
				$("."+divClass+"").append(memusageData);
				
				//查询文件使用率，若有则显示对应数值，没有则显示为0
				var fileusageData = "<div id='wenused' class='warn'>文件使用率（%）：";
				if(result.fileuseage.length>0){
					if(parseInt(result.fileuseage[0]) > 80){
						fileusageData += "<span style='color:red;'>"+result.fileuseage[0]+"</span>";
						serverScores = parseInt(serverScores) - parseInt(delScore);//扣2分
					}else{
						fileusageData += "<span>"+result.fileuseage[0]+"</span>";
					}
				}else{
					fileusageData += 0 ;
				}
				fileusageData += "</div>" ;
				$("."+divClass+"").append(fileusageData);
			},
			error : function() {
				
			}
		});
	}
	
	//网络检测
	var len = 0;
	var num = 0;
	var avg = 0;
	var index1 = 0;
	function getNetwork(){
		$("#network .titals").html("&nbsp;&nbsp;&nbsp;&nbsp;正在进行网络检测......");
		$("#jindu").css("width","77%");
		$("#network").show();
		$("#spinner1").show();
		
		$.ajax({
			url:getContextPath()+"/devicesCon/search",
			type:"post",
			data:{},
			async : false,
			success:function(result){
				$("#jindu").css("width","80%");
				len = result.length;
				avg = parseInt(100 / len);
				for(var i = 0; i < result.length; i++){
					var id = result[i].id;
					$.ajax({
						url:getContextPath()+"/cmd_ping/ping",
						type:"post",
						data:{"id" : id},
						success:function(result){
							$("#jindu").css("width","85%");
							$("#spinner1").hide();
							
							for(var k in result){
								var name = "<div class='' style='width:100%;'>";
								var val = "";
								name += "<span class='warn'>主机名：" + k +"</span>";
								val = result[k];
								if(val == "网络连接超时！"){
									name += "<span class='warn'>连接平均速度（ms）：<span style='color:red;'>" +val +"</span></span>" + "</div></br>";
								}else{
									name += "<span class='warn'>连接平均速度（ms）：" +val +"</span>" + "</div></br>";
									index1 ++ ;
								}
								$("#indexInfo3").append(name);
							}
							
							num ++;
						}
					});
				}
				
				var forDoit = setInterval(function(){
					if(num >= len){
						window.clearInterval(forDoit);
						//计算分数
						var networkScore = avg * index1 * 0.2;
						if(parseInt(serverScores) < 0){
							serverScores = 0;
						}else{
							serverScores = parseInt(serverScores) * 0.4;
						}
						databaseScore = 40;
						score = parseInt(serverScores) + parseInt(networkScore) + parseInt(databaseScore);
						//设置分数
						setOpt(score);
						
						$("#network .titals").html("&nbsp;&nbsp;&nbsp;&nbsp;网络检测完成！");
						$("#jindu").css("width","100%");
						$("#progress").hide();
						$(".leida").hide();
						$(".oneKeybtn").html("检 测 完 成");
						return;
					}
				}, 1000);
			}
		});
	}
	//查询数据库得分
	var databaseScore;
	function getScore(){
		var todo = setInterval(function(){
			$.ajax({
				url : getContextPath() + "/heathScoreViewCon/getScore",
				type : "post",
				data : {str : idsStr},
				dataType : "json",
				async : false,
				beforeSend:function(){},
				success : function(result) {
					var sco = [];
					var sum = 0;
					if(result != null && result.length > 0){
						var htm = "<div class='indexInfo' style='margin-left: 10%;'>";
						for (var i = 0; i < result.length; i++) {
							if(result[i].total_score != null && result[i].total_score !=""){
								htm += "<div style='margin-top:20px;'><span class='databaseSco'>统一ID："+result[i].target_id+"</span><span class='databaseSco'>名称："+result[i].system_name+"</span><span class='databaseSco'>得分："+result[i].total_score+"</span></div>";
								sco[i] = result[i].total_score;
								sum += parseInt(result[i].total_score);
							}else{
								return;
							}
							if(sco.length == result.length){
								window.clearInterval(todo);
							}
						}
						htm += "</div></br>";
						$(".indexInfo").html(htm);
						databaseScore = sum / result.length;
					}
				},
				error : function() {
					
				}
			}); 
		}, 3000);
	}
	
	//设置分数
	var chart = echarts.init(document.getElementById('circleDev'));
	function setOpt(score){
		a = parseInt(score) * 0.01;
		b = parseFloat(a) - 0.03;
		var color1 = "#1dafe6";
		var color2 = "#0ad6f3";
		var color3 = "#0ad6f3";
		if(parseInt(score) < 50){
			color1 = "#FF8B44";
			color2 = "#FFBB6F";
			color3 = "#FFBB6F";
		}
		var option = {
			    series: [{
			        data: [a, b],//波浪
			        color: [color1,color2],//颜色
			        label: {
			            normal: {
			                formatter: function() {
			                    return score;//分数
			                }
			            }
			        },
			        outline: {
			            itemStyle: {
			                borderColor: color3,//边框颜色
			            },
			        },
			    }]
			};
	  chart.setOption(option);
	}
	//圆形分数图
	var a;
	var b;
	function initCricle(score){
		if(parseInt(score) == 100){
			a = 0.93;
			b = 0.91;
		}else{
			a = parseInt(score) * 0.01;
			b = parseFloat(a) - 0.03;
		}
		//展示图形
// 		  var chart = echarts.init(document.getElementById('circleDev'));
		  var option = {
				    series: [{
				        type: 'liquidFill',
				        animation: true,
				        waveAnimation: true,
				        data: [a, b],//波浪
				        color: ['#1dafe6','#0ad6f3'],
				        center: ['50%', '50%'],
				        waveLength: '60%',
				        amplitude: 3,//波浪高度
				        radius: '80%',
				        label: {
				            normal: {
				                formatter: function() {
				                    return score;
				                },
				                textStyle: {
				                    fontSize: 45,
				                    color: '#fff',
				                },
				                position: ['50%', '50%']
				            }
				        },
				        outline: {
				            itemStyle: {
				                borderColor: '#0ad6f3',
				                borderWidth: 2
				            },
				            borderDistance: 0
				        },
				        itemStyle: {
				            normal: {
				                backgroundColor: '#fff',
				            }
				        }
				    }]
				};
		  chart.setOption(option);
	}
</script>
</html>
