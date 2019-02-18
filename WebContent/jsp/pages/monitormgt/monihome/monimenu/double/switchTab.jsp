<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/layui/css/layui.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/double/css/reset.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/double/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/double/css/color.css">
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/layui/layui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/monitormgt/monihome/monimenu/double/js/tabs.js"></script>
<style>
body {
	background: -webkit-gradient(linear, 0% 100%, 0% 0%, from(#3c8dbc),to(#f6f6f8));
	background: -ms-linear-gradient(top,#f6f6f8 0%,#3c8dbc 100%); /*IE*/
}
/*****切换详情界面样式********/
#mask_div {
	position: absolute;
	width: 100%;
	height: 100%;
	z-index: 1000;
	background: #000000;
	opacity: 0.5;
	top: 0;
	left: 0;
	display: none;
}
#switch_page {
	position: absolute;
	height: 100%;
}
#switch_page #sw_page_head {
	width: 100%;
	height: 40px;
	overflow: hidden;
	border-bottom: 1px solid #ccc;
}
#sw_page_head span {
	line-height: 40px;
	font-weight: bold;
}
#sw_head_title {
	padding-left: 8px;
	font-size: 14px;
}
#sw_head_close {
	float: right;
	font-size: 25px;
	padding-right: 10px;
	cursor: pointer;
}

#sw_head_close:hover {
	color: red;
}

#switch_page #sw_page_body {
	width: calc(100% - 40px);
	height: 100%;
	overflow: hidden;
	margin: 0 auto;
}

#bomb_div1,#bomb_div2,#bomb_div3 {
	width: 100%;
	overflow: hidden;
/* 	margin-bottom: 5px; */
}

#bomb_div2 {
	height: 55%;
}
#bomb_div1 #div_core_b {
	float: right;
}

#bomb_div1 #div_core_line {
	width: 30%;
	float: left;
	margin-top: 65px;
}

#bomb_div1 #div_core_line div {
	height: 2px;
	border-bottom: 2px dashed #FE940D;
}

#bomb_div1 #box_name {
	position: absolute;
	width: 100px;
	height: 50px;
	color: #fff;
	line-height: 60px;
	text-align: center;
	font-weight: bold;
	background: url("../double/img/yun.png") 0% 0%/100% 100% no-repeat;
	    left: calc(50% - 50px);
}

#sw_page_body #group_db {
	margin-top: 50px;
	float: left;
}

#sw_page_body #grouptype {
	width: 100%;
}

#bomb_div2 #div2_title {
	/* width: calc(100% - 40px); */
	left: 20px;
	height: 35px;
	line-height: 35px;
	background: #d7e2ef;
}

#sw_body_process {
	width: calc(100% - 40px);
	height: 45%;
	overflow: hidden;
	overflow-y: auto;
	background: #dcecf5;
	position: absolute;
	left: 20px;
}

#sw_process {
	width: 100%;
	height: auto;
	right: 0;
	left: 10px;
	border-left: 2px solid #c0c0c0;
	position: absolute;
	margin-top: 10px;
}

#sw_process ul {
	width: 100%;
	list-style-type: none;
	padding: 0;
	margin: 0;
	float: left;
}

#sw_process ul li {
	width: 100%;
	height: auto;
	float: left;
	padding-bottom: 10px;
	position: relative;
}

#sw_process ul li:last-child {
	padding-bottom: 0;
}

#sw_process ul li i {
	width: 13px;
	height: 13px;
	background-repeat: no-repeat;
	position: absolute;
	z-index: 200;
	left: -8px;
	margin-top: 2px;
}

.cir_green {
	background-image: url(./img/cir_green.png);
}

.cir_red {
	background-image: url(./img/cir_red.png);
}

.cir_gray {
	background-image: url(./img/cir_gray.png);
}

#sw_process ul li div {
	padding-left: 10px;
	display: inline-block;
}

#sw_process ul li div span {
	display: inline-block;
	font-size: 13px;
	letter-spacing: normal;
	word-spacing: normal;
	color: #999;
}

#sw_process ul li div #cir_finsh {
	color: #333;
	font-weight: 700;
	margin-left: 5px;
}

#bomb_div3 div {
	width: 100%;
	overflow: hidden;
/* 	margin-left: 20px; */
}

#bomb_div3 button {
	float: right;
	width: 100px;
	height: 35px;
}
#group_db {
	width: 100%;
	height: calc(100%/ 5 - 2px);
	overflow: visible;
	border-radius: 3px;
}

#grouptype {
/* 	color: #f9fafb; */
/* 	width: 90%; */
	height: 40%;
	text-align: center;
	white-space: nowrap;
	font-size: 10px;
}

#div_core_a #group_db #db_info {
	color: #fff;
	border: 1px solid rgba(0, 0, 0, 0.1);
	background: #3da8dc;
	position: absolute;
	z-index: 300;
	border-radius: 5px;
	-webkit-box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
	top: 50px !important;
	left: calc(100% - 40px) !important;
}
#div_core_b #group_db #db_info{
	color: #fff;
	border: 1px solid rgba(0, 0, 0, 0.1);
	background: #3da8dc;
	position: absolute;
	z-index: 300;
	border-radius: 5px;
	-webkit-box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
	top: 50px !important;
	right: calc(100% - 40px) !important;
}

#db_info span {
	width: 100%;
	display: block;
	text-align: center;
	line-height: 18px;
	white-space: nowrap;
	font-size: 12px;
}

#servers img {
	width: calc(100%/ 4 - 5px);
	height: 90%;
	margin-left: 5px;
	margin-right: 5px;
}

.core_A #servers {
	width: 90%;
	height: 75%;
	text-align: center;
}

.core_B #servers {
	width: 90%;
	height: 75%;
	text-align: center;
	float: right;
}

#core_div,#core_line {
	height: 95%;
	width: 37%;
	float: left;
}

#core_line {
	width: 26%;
}

#bomb_div1 #div_core_a,#bomb_div1 #div_core_b {
	height: 100%;
	width: 35%;
	text-align: center;
	position: relative;
}

#core_name {
	width: 80%;
	height: 10%;
	overflow: hidden;
}

#core_name div {
	width: 35%;
	height: 100%;
	margin: 0 auto;
	color: #fff;
	font-size: 15px;
	font-weight: bold;
	text-align: center;
	line-height: 45px;
}

.yun_png {
	width: 40%;
	height: 100%;
	position: absolute;
	left: 30%;
	top: 0;
	border: none;
	visibility: hidden;
}
.btn {
    display: inline-block;
    padding: 4px 12px;
    margin-bottom: 0;
    font-size: 14px;
    font-weight: normal;
    line-height: 1.42857143;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    -ms-touch-action: manipulation;
    touch-action: manipulation;
    cursor: pointer;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    background-image: none;
    border: 1px solid transparent;
    border-radius: 4px;
}

/* 负载均衡 **/
.box_nameA {
	width: 100px;
	height: 50px;
	color: #fff;
	line-height: 60px;
	text-align: center;
	font-weight: bold;
	background: url("../double/img/yun.png") 0% 0%/100% 100% no-repeat;
	margin-left: calc(50% - 50px);
}
.box_nameB {
	width: 100px;
	height: 50px;
	color: #fff;
	line-height: 60px;
	text-align: center;
	font-weight: bold;
	background: url("../double/img/yun.png") 0% 0%/100% 100% no-repeat;
	margin-left: calc(50% - 50px);
}
.fzjh_core_line {
	width: 20%;
	float: left;
	margin-top: 65px;
}
#leftA{
	width: 40%;
    float: left;
        text-align: center;
}
#rightB{
	width: 40%;
    float: left;
    text-align: center;
}
.fzjhImg{
	width: 25%;
}
.liucheng{
	width: calc(100% - 40px);
    height: 45%;
    overflow: hidden;
    overflow-y: auto;
    background: #dcecf5;
    position: absolute;
    left: 20px;
}
#fzjh_div1{
	height:35%;
}
#fzjh_div2{
	height: 45%;
	margin-bottom: 10px;
}
.content {
    height: 95%;
}
.xiexianDiv2{
	width:20%;
	height:5%;
	text-align: center;
    margin-left: 30%;
    float: left;
	background: linear-gradient(170deg, transparent 49.5%, #a09fa0 49.5%, #a09fa0 50.5%, transparent 50.5%);
}
.xiexianDiv{
	width:20%;
	height:5%;
	float: right;
    margin-right: 30%;
	background: linear-gradient(10deg, transparent 49.5%, #a09fa0 49.5%, #a09fa0 50.5%, transparent 50.5%);
}
#group_in{
	width: 50%;
    height: 90%;
	overflow: visible;
	border-radius:3px;
}
#servers{
	height: 75%;
	text-align: center;
}
#group_in #servers img {
    width: calc(100%/ 4 - 35px);
    height: 70%;
    margin-left: 5px;
    margin-right: 5px;
    margin-top: 5px;
}
#group_in #grouptype {
    text-align: center;
    white-space: nowrap;
    font-size: 10px;
    margin-top: -10px;
}
#group_in #core_name {
    width: 100%;
    height: 55%;
    overflow: hidden;
    margin-top: -50px;
    text-align: center;
}
#group_in #core_name img {
	width: 25%;
	height: 100%;
	left: 30%;
	top: 0;
	border: none;
}
</style>
<title>一键切换</title>
</head>
<body>
	<div class="tabs">
		<!-- Start tab1 ADG切换-->
		<div id="tab1" class="container">
			<div class="label" id="l1">
				<h5>
					<a href="#tab1" id="a1">数据库</a>
				</h5>
			</div>
			<div class="content left" id="content1">
				<div id="switch_page">
			         <div id="sw_page_body">
			             <div id="bomb_div1"></div>
			             <div id="bomb_div31"></div>
			             <div id="bomb_div2">
							<div id="sw_body_process">
								<div id="sw_process">
									<ul>
										<li><i class="cir_gray"></i>
											<div>
												<span id=""> 检测主数据库状态，耗时 0 秒 (init)</span>
											</div></li>
										<li><i class="cir_gray"></i>
										<div>
												<span id=""> 检测备数据库状态，耗时 0 秒 (init)</span>
											</div></li>
										<li><i class="cir_gray"></i>
										<div>
												<span id=""> 关闭非操作节点，耗时 0 秒 (init)</span>
											</div></li>
										<li><i class="cir_gray"></i>
										<div>
												<span id=""> 主数据库切换为备数据库，耗时 0 秒 (init)</span>
											</div></li>
										<li><i class="cir_gray"></i>
										<div>
												<span id=""> 备数据库切换为主数据库，耗时 0 秒 (init)</span>
											</div></li>
										<li><i class="cir_gray"></i>
										<div>
												<span id=""> 切换后的备数据库执行redo，耗时 0 秒 (init)</span>
											</div></li>
										<li><i class="cir_gray"></i>
										<div>
												<span id=""> 启动非操作节点，耗时 0 秒 (init)</span>
											</div></li>
									</ul>
								</div>
							</div>
						</div>
			             <div id="bomb_div3"></div>
			         </div>
			    </div>
			</div>
		</div>
		<!-- End tab1 -->
		<!-- Start tab2 负载均衡切换-->
		<!-- <div id="tab2" class="container">
			<div class="label" id="l2">
				<h5>
					<a href="#tab2" id="a2" onClick="showFzjhPage();">负载均衡</a>
				</h5>
			</div>
			<div class="content" id="content2">
				<div id="fzjh_body" style='height: 100%;'>
		             <div id="fzjh_div1"></div>
		             <div id="fzjh_div31"></div>
		             <div id="fzjh_div2"></div>
		             <div id="fzjh_div3"></div>
		         </div>
			</div>
		</div> -->
		<!-- End tab2 -->
		<!-- Start tab2 f5负载均衡切换-->
		<div id="tab2" class="container">
			<div class="label" id="l2">
				<h5>
					<a href="#tab2" id="a2" onClick="showF5();">f5负载均衡</a>
				</h5>
			</div>
			<div class="content" id="content2">
				<div id="f5fzjh_body" style='height: 100%;margin-top: 1%;display:none;'>
					<div style="text-align:center;">
		            	<span style="margin-left: -15px;">f5</span>
		            	<img class='' src='../double/img/fzjh.png' style="width: 8%;"/>
					</div>
					<div class="xiexianDiv2"></div>
					<div class="xiexianDiv"></div>
					<div id="appServers" style="height: 20%;font-size:10px;"></div>
					<div style="background: #dcecf5;height:45%;margin-left: 3%;margin-right: 3%;">
						<div class='' style="left: 20px;height: 35px;line-height: 5px;background: #d7e2ef;">任务流程详情：</div>
						<div id="f5info">
							<div id="sw_body_process">
								<div id="sw_process">
									<ul>
										<li>
											<i class="cir_gray"></i>
											<div>
												<span id=""> 开始进行负载均衡切换 (init)</span>
											</div>
										</li>
										<li>
											<i class="cir_gray"></i>
											<div>
												<span id=""> 负载均衡切换成功 (init)</span>
											</div>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div id="">
						<div id="">
							<input type="button" class="btn" onclick="oneKeySwitch();" value="一键切换"
								style="width: 100px;font-size: 16px;font-weight: bold;float: right;margin-top: 50px;margin-right: 20px;">
							<input type="button" class="btn" onclick="doF5Switch();" value="执&nbsp;&nbsp;&nbsp;行"
								style="width: 100px;font-size: 16px;font-weight: bold;float: right;margin-top: 50px;margin-right: 20px;">
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- End tab2 -->
	</div>
</body>
<script type="text/javascript">
	$(function(){
		roleId=$("#roleId",parent.document).val();
		showSwitchAdg();
	});
	
	function showF5(){
		$("#f5fzjh_body").show();
		showF5Page();
		$(".yun").attr("src", getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/img/yun.png");
	}
	
	function showSwitchAdg(){
		var str="<div style='height: 30px;margin-top: 10px;font-size: 15px;'>";
		str+="<span>切换方式：</span>";
		str+="<input type=\"radio\" name=\"swMode\" value=\"1\" checked=\"checked\"/>switchover &nbsp;&nbsp;&nbsp;&nbsp;";
		str+="<input type=\"radio\" name=\"swMode\" value=\"2\"/>failover";
		str+=" </div>";
		$("#bomb_div31").html(str);
		str="";
		str+="<div id=\"executDiv\">";
		str+="<input type='button' class='btn' onclick=\"doexecuteSwitch(1)\" value='执&nbsp;&nbsp;&nbsp;行' style='width: 100px;font-size: 16px;font-weight: bold;float: right;'/>";
		str+=" </div>";
		$("#bomb_div3").html(str);
		refexeS=true;
		loadSwitch();
	}
	/**
	 *load切换监控界面
	 */
	var refexeS=true;//用于标识任务已全部执行完后不再做刷新流程信息区域
	function loadSwitch(){
		var str="<div id=\"div_core_a\">";
			str+="<div id=\"box_name\" class='box_name'>"+$(".core_A #core_name div",parent.document).html()+"</div>";
			str+="<div id=\"group_db\">";
			str+=$(".core_A #group_db",parent.document).html();
			str+="</div>";
			str+="</div>";
			str+="<div id=\"div_core_line\"><div></div></div>";
			str+="<div id=\"div_core_b\">";
			str+="<div id=\"box_name\" class='box_name'>"+$(".core_B #core_name div",parent.document).html()+"</div>";
			str+="<div id=\"group_db\">";
			str+=$(".core_B #group_db",parent.document).html();
			str+="</div>";
			str+="</div>";
		$("#bomb_div1").html(str);
		if(refexeS || refexeS==true){
			str="<div id=\"div2_title\"></div>";
			str+="<div id=\"sw_body_process\"><div id=\"sw_process\"><ul></ul></div></div>";
			$("#bomb_div2").html(str);
			executeSwitch(2);//获取切换任务拼接流程图信息
		}
	}
	/***
	 * 调用切换操作或查询任务进度详情
	 * @param type 标识：1（先调用任务再查询）/2（直接查询任务信息）
	 */
	function executeSwitch(type){
		var retbool=true;
		var swMode="",standby_a="",standby_b="";
		if(type==1){
			swMode=$("input[name='swMode']:checked").val();
			if(swMode==="" || typeof(swMode)==="undefined"){
				layer.alert("请选择切换方式!");
				retbool=false;
				return;
			}
			standby_a=$(".core_A #group_db #db_info span:eq(0)",parent.document).attr("value");//A中心主备标识
			if(standby_a==="NO"){
				layer.alert($(".core_A #core_name div").html()+"未做主备!");
				retbool=false;
				return;
			}
			standby_b=$(".core_B #group_db #db_info span:eq(0)",parent.document).attr("value");//B中心主备标识
			if(standby_b==="NO"){
				layer.alert($(".core_B #core_name div").html()+"未做主备!");
				retbool=false;
				return;
			}
		}
		if(retbool==true){
			var double_id=$("#double_id",parent.document).val();//双活Id
			var group_id_a=$(".core_A #group_db #servers img:eq(0)",parent.document).attr("group_id");//A中心数据库组id
			var group_id_b=$(".core_B #group_db #servers img:eq(0)",parent.document).attr("group_id");//B中心数据库组id
			$.ajax({
				url:getContextPath()+"/doublemonCon/executeSwitch",
				type:"post",
				datatype:"json",
				data:{
					double_id : double_id,
					group_id_a : group_id_a,
					group_id_b : group_id_b,
					swMode : swMode,
					standby_a : standby_a,
					standby_b : standby_b,
					type : type,
					refexeS : refexeS
				},
				success : function(res) {
					var json = eval(res);
					var status = json[0].status;
					var value = json[0].value;
					if (status === "success") {
						var values = value.split("%TAB%");
// 						var val2 = values[2];
// 						if(val2 != "false"){
							if (refexeS || refexeS == true) {
								$("#bomb_div2 #sw_process ul").html(values[0]);
								var val = values[1];
								if (val == undefined) {
									val = "";
								}
								$("#div2_title").html("任务流程详情 " + val);
							}
// 						}else{
// 							layer.alert("数据库切换失败！");
// 						}
						refexeS = values[2];
						$("#bomb_div2").css({display : "block"});
					} else {
						console.log(value);
					}
					RefreshshowSwitch();
				},
				error : function() {
					layer.alert("数据库切换失败！");
					console.log("数据出错！");
				}
			});
		}
	}

	/**
	 * 执行按钮
	 * @param type 标识：1（先调用任务再查询）/2（直接查询任务信息）
	 */
	function doexecuteSwitch(type) {
		var swMode = $("input[name='swMode']:checked").val();
		var firmStr = swMode === "1" ? "（switchover）" : "（failehover）";
		layer.confirm("数据库" + firmStr + "切换，确定继续执行？", {
			btn : [ "确定", "取消" ]
		}, function() {
			layer.closeAll();
			executeSwitch(type);
		});
	}

	/**
	 * 刷新切换监控界面
	 */
	var bolshowSwitch = true;
	function RefreshshowSwitch() {
		setTimeout(function() {
			if (bolshowSwitch == true) {
				loadSwitch();
			}
		}, 5000);
	}

	//负载均衡切换页面
	function showFzjhPage() {
		var str = "<div id='leftA'>";
		str += "<div class='box_nameA'>"
				+ $(".core_A #core_name div", parent.document).html()
				+ "</div>";
		str += "<img class='fzjhImg' src='../double/img/fzjh.png'/>";
		str += "<div>负载均衡</div>";
		str += "</div>";
		str += "<div class='fzjh_core_line'><div></div></div>";
		str += "<div id='rightB'>";
		str += "<div class='box_nameB'>"
				+ $(".core_B #core_name div", parent.document).html()
				+ "</div>";
		str += "<img class='fzjhImg' src='../double/img/fzjh.png'/>";
		str += "<div>负载均衡</div>";
		str += "</div>";
		$("#fzjh_div1").html(str);
		showLiuCheng();
	}

	function showLiuCheng() {
		var str = "";
		str += "<div style='height: 40px;margin-bottom: 5px;margin-left: 20px;'>";
		str += "<span>切换方式：</span>";
		str += "<input type='radio' name='fangAn' value='1'/>方案一 A中心读写，B中心只读";
		str += "&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='fangAn' value='2'/>方案二 A中心读写，B中心故障</br>";
		str += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='fangAn' value='3'/>方案三 A中心只读，B中心读写";
		str += "&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='fangAn' value='4'/>方案四 A中心故障，B中心读写";
		str += " </div>";
		$("#fzjh_div31").html(str);
		str = "";
		str = "<div class='liucheng'>任务流程详情：";
		str += "<div class='switchres'></div>";
		str += "</div>";
		$("#fzjh_div2").html(str);
		str = "";
		str += "<div id=\"executDiv\">";
		str += "<input type='button' class='btn' onclick='doFuZaiJunHengSwitch();' value='执&nbsp;&nbsp;&nbsp;行' style='width: 100px;font-size: 16px;font-weight: bold;margin-left: 5px;'/>";
		str += " </div>";
		$("#fzjh_div3").html(str);
	}

	/**
	 * 负载均衡执行按钮
	 * @param type 标识：1（先调用任务再查询）/2（直接查询任务信息）
	 */
	function doFuZaiJunHengSwitch(type) {
		var swMode = $("input[name='fangAn']:checked").val();
		var firmStr = "";
		if (parseInt(swMode) == 1) {
			firmStr = "方案一";
		} else if (parseInt(swMode) == 2) {
			firmStr = "方案二";
		} else if (parseInt(swMode) == 3) {
			firmStr = "方案三";
		} else if (parseInt(swMode) == 4) {
			firmStr = "方案四";
		}
		layer.confirm(firmStr + "切换操作谨慎执行，确定继续？", {
			btn : [ '确定', '取消' ]
		}, function() {
			layer.closeAll();
			$.ajax({
				url : getContextPath() + "/doublemonCon/doFuZaiJunHengSwitch",
				type : "post",
				datatype : "json",
				data : {
					"swMode" : swMode
				},
				success : function(res) {
					if (res = "seccuss") {
						$(".switchres").html("切换成功！");
					} else {
						$(".switchres").html("切换失败！");
					}
				},
				error : function() {
					$(".switchres").html("切换失败！");
				}
			});
		});
	}

	//显示f5切换页面内容
	function showF5Page() {
		var inHtml = "<div id='group_in' style='float: left;'>";
		inHtml += "<div id='core_name'>";
		inHtml += "<img class='yun'/>";
		inHtml += "<div class='yunName' style='margin-top: -45px;'>"
				+ $(".core_A #core_name div", parent.document).html()
				+ "</div>";
		inHtml += "</div>";
		inHtml += $(".core_A #group_in", parent.document).html();
		inHtml += "</div>";
		inHtml += "<div id='group_in' style='float: left;'>";
		inHtml += "<div id='core_name'>";
		inHtml += "<img class='yun'/>";
		inHtml += "<div class='yunName' style='margin-top: -45px;'>"
				+ $(".core_B #core_name div", parent.document).html()
				+ "</div>";
		inHtml += "</div>";
		inHtml += $(".core_B #group_in", parent.document).html();
		inHtml += "</div>";
		$("#appServers").html(inHtml);
	}
	//f5执行切换
	function doF5Switch() {
		var inHtml = "<ul><li><i class='cir_green'></i><div><span id=''> 开始进行负载均衡切换 (init)</span></div></li>";
		layer.confirm("f5负载均衡切换，确定继续？", {
			btn : [ '确定', '取消' ]
		}, function() {
			layer.closeAll();
			$("#f5info #sw_process").html(inHtml);
			executeF5Swithc();
		});
	}
	//执行f5切换
	function executeF5Swithc() {
		var inHtml = "<ul><li><i class='cir_green'></i><div><span id=''> 开始进行负载均衡切换 (init)</span></div></li>";
		inHtml += "<li><i class='cir_green'></i><div><span id=''> 负载均衡切换成功 (init)</span></div></li></ul>";
		var inHtml2 = "<ul><li><i class='cir_green'></i><div><span id=''> 开始进行负载均衡切换 (init)</span></div></li>";
		inHtml2 += "<li><i class='cir_red'></i><div><span id=''> 负载均衡切换失败 (init)</span></div></li></ul>";
		$.ajax({
			url : getContextPath() + "/doublemonCon/doF5Switch",
			type : "post",
			datatype : "json",
			data : {},
			success : function(res) {
				if (res = "seccuss") {
					$("#f5info #sw_process").html(inHtml);
					layer.alert("一键切换成功！");
				} else {
					$("#f5info #sw_process").html(inHtml2);
				}
			},
			error : function() {
				$("#f5info #sw_process").html(inHtml2);
				layer.alert("负载均衡切换失败！");
			}
		});
	}

	//一键切换
	function oneKeySwitch() {
		layer.confirm("确定执行一键切换吗？", {
			btn : [ '确定', '取消' ]
		}, function() {
			layer.closeAll();
			executeSwitch(1);
			executeF5Swithc();
		});
	}
</script>
</html>
