//var roleId="";
var index;
$(function(){
	//loading层
	index = layer.load(1, {
	  shade: [0.3,'#fff']
	});
	//获取双活配置信息
	getDoubleMgt();
	//判断双活id是否为空
	var did = $("#double_id").val();
	if(did != ""){
		//显示服务器处小框提示信息
		$(".core_A #db_info").show();
		$(".core_B #db_info").show();
		initfun();
		getDoubinfo(did);
	}else{
		//隐藏服务器处小框提示信息
		$(".core_A #db_info").hide();
		$(".core_B #db_info").hide();
		//关闭加载层
	  	layer.close(index);
	}
    roleId=$("#roleId").val();
});
function initfun(){
    //服务器处小框提示信息
    $(".core_A #db_info").css({left:($("#group_db").offset().left-250)+"px", top:($("#group_db").offset().top-$(".core_A").height()+25)+"px"});
    $(".core_B #db_info").css({right:($(".core_B #group_db").width()-10)+"px", top:($("#group_db").offset().top-$(".core_A").height()+25)+"px"});
}
//获取双活信息
function getDoubleMgt(){
	$.ajax({
		url:getContextPath()+"/doublemonCon/searchDoubleMgt",
		type:'POST',
		data:{},
		async:false,
		dataType:'json',
		success:function(data){
			if(data != null && data != ""){
				$("#double_id").val(data[0].id);
				// 双活
		  		getDoubleInfo();
				getDatebase();
		  		getDatebase1();
			}
		}
	});
}
/**
 * 初始化数据
 * @param double_id 双活ID
 */
function getDoubinfo(double_id){
		if(typeof($(".core_A #core_name div").html())==="undefined"){
			return;
		}
		$.ajax({
			url:getContextPath()+"/doublemonCon/getDoubinfo",
			type:"post",
			datatype:"json",
			data:{double_id:double_id},
			success:function(res){
				if(typeof(res)!=="undefined" && res!=="" && res!==null){
					var arr=res.split("%TAB%");
					//加载A中心
					try{
						var a_ar=eval(arr[0]);
						$(".core_A #core_name div").html(a_ar[0].core_name);
						$(".core_A #core_name div").attr("core_id",a_ar[0].core_id);
						$(".core_A #group_in #servers").html(a_ar[0].inStr);
						$(".core_A #group_db #servers").html(a_ar[0].dbStr);
						$(".core_A #db_info").html(a_ar[0].db_info);
						//判断是否配置了服务器设备
						if(a_ar[0].dbStr != "" && a_ar[0].inStr != ""){
							$("#svgPath").show();
							$("#svgPath2").show();
							if(a_ar[0].inLine == "in_db_err"){
								$("#svgPath2").attr("stroke","#f74c5e");
								$("#markerArrow3 path").css({fill:"#f74c5e"});
								$("#markerArrow4 path").css({fill:"#f74c5e"});
								$("#circle3").css({display:"none"});
								$("#circle4").css({display:"none"});
							}else{
								$("#svgPath2").attr("stroke","#36a3e9");
								$("#markerArrow3 path").css({fill:"#36a3e9"});
								$("#markerArrow4 path").css({fill:"#36a3e9"});
								setTimeout(function(){
									$("#circle3").show();
									$("#circle4").show();
								}, 1500)
							}
						}else{
							$("#svgPath").hide();
							$("#svgPath2").hide();
							$("#svgPathC").hide();
							$("#circle").css({display:"none"});
							$("#circle2").css({display:"none"});
							$("#circle3").css({display:"none"});
							$("#circle4").css({display:"none"});
						}
					}catch(e){console.log(e)}
					//加载B中心
					try{
						var b_ar=eval(arr[1]);
						$(".core_B #core_name div").html(b_ar[0].core_name);
						$(".core_B #core_name div").attr("core_id",b_ar[0].core_id);
						$(".core_B #group_in #servers").html(b_ar[0].inStr);
						$(".core_B #group_db #servers").html(b_ar[0].dbStr);
						$(".core_B #db_info").html(b_ar[0].db_info);
						//判断是否配置了服务器设备
						if(b_ar[0].dbStr != "" && b_ar[0].inStr != ""){
							$("#svgPathB").show();
							$("#svgPathB2").show();
							if(b_ar[0].inLine == "in_db_err"){
								$("#svgPathB2").attr("stroke","#f74c5e");
								$("#markerArrowB3 path").css({fill:"#f74c5e"});
								$("#markerArrowB4 path").css({fill:"#f74c5e"});
								$("#circleB3").css({display:"none"});
								$("#circleB4").css({display:"none"});
							}else{
								$("#svgPathB2").attr("stroke","#36a3e9");
								$("#markerArrowB3 path").css({fill:"#36a3e9"});
								$("#markerArrowB4 path").css({fill:"#36a3e9"});
								setTimeout(function(){
									$("#circleB3").show();
									$("#circleB4").show();
								}, 1500)
							}
						}else{
							$("#svgPathB").hide();
							$("#svgPathB2").hide();
							$("#svgPathC").hide();
							$("#circleB").css({display:"none"});
							$("#circleB2").css({display:"none"});
							$("#circleB3").css({display:"none"});
							$("#circleB4").css({display:"none"});
						}
					}catch(e){console.log(e)}
					if(a_ar[0].dbStr != "" && b_ar[0].dbStr != ""){
						$("#svgPathC").show();
						//AB两个中心连线
						if(a_ar[0].outLine == "out_db_err_l" || b_ar[0].outLine == "out_db_err_r"){
							$("#svgPathC").attr("stroke","#f74c5e");
							$("#markerArrowDB1 path").css({fill:"#f74c5e"});
							$("#markerArrowDB2 path").css({fill:"#f74c5e"});
							$("#circleC").css({display:"none"});
							$("#circleC1").css({display:"none"});
						}else{
							$("#svgPathC").attr("stroke","#46d083");
							$("#markerArrowDB1 path").css({fill:"#46d083"});
							$("#markerArrowDB2 path").css({fill:"#46d083"});
							setTimeout(function(){
								$("#circleC").show();
								$("#circleC1").show();
							}, 1500)
						}
					}else{
						$("#svgPathC").hide();
					}
				}
				svgLine();
			  	//关闭加载层
			  	layer.close(index);
			  	RefreshDouble(double_id);
			},
			error:function(){
				//关闭加载层
			  	layer.close(index);
			  	layer.msg("数据加载失败！");
			}
		});
}
/**
 * 定时刷新首页基本信息
 */
function RefreshDouble(double_id){
    setTimeout(function(){
    	getDoubinfo(double_id);
    },10000);
}
/**
 * 跳转服务器详情界面
 * @param uid
 * @param id
 */
function jumpServer(uid,id){
	//停止统一图像化界面动画
	$("#circle3").stop(true);
	$("#circle4").stop(true);
	$("#circleB3").stop(true);
	$("#circleB4").stop(true);
	$("#circleC").stop(true);
	$("#circleC1").stop(true);
	//返回类型，用来判断返回到那个页面
	$("#returnType").val("index");
	$("#sevuid").val(uid);
	$("#sevid").val(id);
	$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/serverParticulars.jsp");
}
//判断使用的是什么浏览器
function myBrowser(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    //判断是否是谷歌浏览器
    if (userAgent.indexOf("Chrome") > -1){
	    return "Chrome";
	}
    //IE11
    var isIE11 = (userAgent.toLowerCase().indexOf("trident") > -1 && userAgent.indexOf("rv") > -1); 
    //IE11以下
    var isIE = userAgent.indexOf("MSIE") > -1;
    if (isIE11 == true || isIE == true) {
        return "IE";
    }; //判断是否IE浏览器
}

//窗口改变大小时重新加载
$(window).resize(function(){
	$("#circle").stop(true);
	$("#circle2").stop(true);
	$("#circle3").stop(true);
	$("#circle4").stop(true);
	$("#circleB").stop(true);
	$("#circleB2").stop(true);
	$("#circleB3").stop(true);
	$("#circleB4").stop(true);
	$("#circleC").stop(true);
	$("#circleC1").stop(true);
	$("#circle").hide();
	$("#circle2").hide();
	$("#circle3").hide();
	$("#circle4").hide();
	$("#circleB").hide();
	$("#circleB2").hide();
	$("#circleB3").hide();
	$("#circleB4").hide();
	$("#circleC").hide();
	$("#circleC1").hide();
	getEcharts();
	initfun();
	svgLine();
});