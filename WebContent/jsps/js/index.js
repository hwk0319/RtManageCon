//服务器详情页面定时器清除标识
var servertime=[];
//数据库详情的定时器
var softtime = [];
var userId;
var userName;
var winHeightMon;
var winHeightdDev;
//页面加载时调用
$(function() {
	var al_wid=$("#commHead").width()-$(".head_left").width()-($(".head_title").width()+30)-($(".logOut").width()+100)-50;
	$(".div_alarm").css({width:al_wid+"px"});
//	iscLogin();
	loadMenu();
	getUsername();
	winHeightMon = document.body.clientHeight;
	winHeightdDev = document.body.clientHeight;
	var userRoleId = $("#userRoleId").val();
	//运维人员
	if(userRoleId == "2"){
		$("#welcom").hide();
		$("#tt").load(getContextPath()+ "/jsp/pages/monitormgt/monihome/monimenu/double/double.jsp");
		getWarnLog();
		//定时刷新告警信息
		setInterval(function(){
			getWarnLog();
		}, 1000*60*30);
	}
	var menuName = "";
	$('#comm_menu').delegate('.sysmenu', 'click', function(obj) {
		if($(".submenu").length==0||menuName != this.id){
			var id = $(this).attr("id");
			loadChildMenu(id);
		}else{
			$(".submenu").remove();
		}
		menuName = this.id;
	});
	// 初始化公钥
	initPublicKey();
	//关闭服务器定时器
	for(var i=0;i<servertime.length;i++){
		clearInterval(servertime[i]);
	}
	//关闭软件定时器
	for(var i=0;i<softtime.length;i++){
		clearInterval(softtime[i]);
	}
});
//获取告警日志
function getWarnLog(){
    $.ajax({
		url:getContextPath()+"/WarnlogCon/getalarm",
		type:"post",
		data:{},
		datatype:"json",
		success:function (data){
			if(data != "" && data != null){
				//日志告警，弹出提示框
				var len = data.indexOf("日志容量");
				if(len > 0){
					layer.alert(data);
				}
				$(".div_alarm marquee").html(data);
			}else{
//				$(".div_alarm marquee").html("");
//				$(".div_alarm marquee").html("这里展示相关告警信息！");
			}
		}
	});
}
//获取公钥
function initPublicKey(){
	$.ajax({
		url : getContextPath()+"/login/initModel",
		type :"post",
		data : {},
		success : function(result){
			$("#publicKey").val(result);
		}
	});
}

/**
 *ISC登录
 */
function iscLogin(){
	$.ajax({
		url:getContextPath()+"/login/iscLogin",
		async: false,
		type:"post",
		data:{},
		success:function(data){
			userId = data.id;
			userName = data.username;
		}
	});
}

//加载一级菜单
function loadMenu() {
	$("#comm_menu").html("");
	$.ajax({
	    url : getContextPath()+"/sysMenu/menu",
		type : "post",
		dataType : "json",
		success : function(data) {
			var menuhtml = "";
			for ( var index in data) {
				menuhtml += "<div class='sysmenu' id='"
				+data[index].menucode+"'><img src='"+getContextPath()+"/"+data[index].menuimage
				+"'><a id='"+ data[index].menuurl + "'>" + data[index].menuname + "</a></div>";
			}
			$("#comm_menu").html(menuhtml);
		}
	});
}

//点击加载二级菜单
function loadChildMenu(obj) {
	$(".sysmenu_click").removeClass("sysmenu_click");
	$("#"+obj).addClass("sysmenu_click");
	$.ajax({
		url : getContextPath()+"/sysMenu/findmenubyp",
		type : "post",
		dataType : "json",
		data : {
			parentcode : obj
		},
		success : function(data) {
			$(".submenu").remove();
			if (data.length > 0) {
				var childmenuhtml = "<div class='submenu'>";
				for ( var index in data) {
					childmenuhtml += "<a onclick='menuclick(this)' class='"+data[index].menucode+"' id='"
							+ data[index].menuurl
							+ "'><div style='display: block;'>"
							+ data[index].menuname + "</div></a>";
				}
				childmenuhtml += "</div>";
				$("#" + obj).after(childmenuhtml);
			}else{
				menuclick($("#"+obj)[0].lastChild);
			}
		},
		error:function(){
			layer.alert('登录超时，请重新登录！', function(){
				window.location.href = getContextPath()+"/signout";
			});
			setTimeout(function(){
				window.location.href = getContextPath()+"/signout";
			}, 2000);
		}
	});
}
//点击菜单事件
function menuclick(obj) {
	//停止统一图像化界面动画
	$("#circle3").stop(true);
	$("#circle4").stop(true);
	$("#circleB3").stop(true);
	$("#circleB4").stop(true);
	$("#circleC").stop(true);
	$("#circleC1").stop(true);
	
	//关闭服务器定时器
	for(var i=0;i<servertime.length;i++){
		clearInterval(servertime[i]);
	}
	//关闭软件定时器
	for(var i=0;i<softtime.length;i++){
		clearInterval(softtime[i]);
	}
	if(obj.textContent != "统一图形化界面"){
		if($(".submenu_click").length != 0){
			$(".submenu_click").removeClass("submenu_click");
		}
		obj.firstElementChild.className = "submenu_click";
		//加载当前在哪一个菜单，并显示
		$("#mainTitle").html(obj.parentElement.previousSibling.textContent);
		$("#subTitle").html(obj.textContent);
		$("#firstTitle").html("");
		$("#secondtitle").html("");
	}else{
//		$("#tt").load(getContextPath()+ "/jsp/pages/monitormgt/monihome/monimenu/double/double.jsp");
//		getWarnLog();
//		//定时刷新告警信息
//		setInterval(function(){
//			getWarnLog();
//		}, 1000*60*30);
		location.reload();
		return;
	}
	var url = getContextPath()+ "/" + $(obj).attr("id");
	if (url.indexOf(".jsp") > 0) {
		$('#tt').load(url);
		if(url.indexOf("monihome") > 0){
			if(winHeightdDev < winHeightMon){
					$('#tt')[0].style.height = document.body.clientHeight + "px";
			}
		}else{
			setTimeout(function(){
				winHeightdDev = document.body.clientHeight - 50
				$('#tt')[0].style.height = document.body.clientHeight - 50  + "px";
				$('#tt')[0].style.width = document.body.clientWidth - 230 + "px";
			}, 500);
		}
	}
	$("#subTitle").click(function(){
		$('#tt').load(url);
		$("#firstTitle").html("");
		$("#secondtitle").html("");
	});
}

//窗口改变大小时重新设置div的宽高
$(window).resize(function(){
//	$('#tt')[0].style.width = document.body.clientWidth - 230 + "px";
//	$('#tt')[0].style.height = document.body.clientHeight - 50 + "px";
//	var h_tt = document.body.clientHeight;
	$('#tt').css({
		"width":"calc(100% - 230px)",
		"height":"100%"
	})
	var al_wid=$("#commHead").width()-$(".head_left").width()-($(".head_title").width()+30)-($(".logOut").width()+100)-50;
	$(".div_alarm").css({width:al_wid+"px"});
});

//注销操作
function logOut(){
	layer.confirm('确定要注销吗？', {
		btn: ['确定','取消'] //按钮
	}, function(){
		$.ajax({
			url:getContextPath()+"/login/logout",
			type:"post",
			async:false,
			success : function(result){
				if (result[0] == "success") {
					window.location.href = getContextPath();
				}
			},
			error : function(){
				window.location.href = getContextPath();
			}
		});
	}, function(){
		return;
	});
}

//修改密码
function modifyPass(){
	layer.open({
	    type: 2,
	    title: '修改密码',
	    fix: false,
	    shadeClose: false,
	    area: ['500px', '320px'],
	    content: [getContextPath()+'/jsps/security/modifyPass.jsp?id='+userId+'&name='+userName,'no']
	});
}
/**
 * 跳转异常故障功能
 * @param devic_id
 */
function jumpWarnLog(devic_id){
	if(typeof($("#comm_menu .5001").html())==="undefined"){
		 $("#comm_menu #5000").click();
	}    
    setTimeout(function(){
    	$("#comm_menu .5001").attr("search",devic_id);
    	$("#comm_menu .5001").click();
    },50);
}

//登录超时
function timeOut(){
	window.location.href = getContextPath();
//	window.location.href = getContextPath()+"/signout";
}

//获取session，判断是否存在，
function getSession(){
	var res;
	$.ajax({
		url:getContextPath()+"/login/getSession",
		type:"post",
		data:{},
		datatype:"json",
		async:false,
		success:function (data){
			res = data;
		}
	});
	return res;
}

//获取用户
function getUsername(){
	$.ajax({
		url:getContextPath()+"/login/getUsername",
		type:"post",
		data:{},
		datatype:"json",
		success:function (data){
			$("#usernameText").html("<font class=''>"+data.username+"</font>");
		}
	});
}