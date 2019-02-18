//由于此模式使用的是有关公用页面index.jsp，非弹出页面是故不好获取URL的参数，特直接使用对应的id
//edit by huangdaping
var id = 2000;
//点击不同模块触发不同链接（待改进，先实现）
$(function(){
	var Top=$(".soft-db").height()/2+20;
    var lef=$(".soft-db").width()/2;
    $(".soft-db .data_menu li").css({top:-Top+"px",left:-lef+"px"});
	//硬件相关跳转（服务器，集群，双活，一体机）
	$('.nav').delegate('.server','click',function(){
		var path=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/server.jsp";
		$("#tt").load(path);
		var str="<a style='font-family: Microsoft YaHei;font-size:14px;color:#3c8dbc;cursor: pointer;' onclick=\"loadsev('"+path+"')\">>服务器</a>";
		$("#firstTitle",window.document).html(str);
	});
	$('.nav').delegate('.active-active','click',function(){
		var path=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/active-act/active.jsp";
		$("#tt").load(path);
		var str="<a style='font-family: Microsoft YaHei;font-size:14px;color:#3c8dbc;cursor: pointer;' onclick=\"loadsev('"+path+"')\">&gt;双活</a>";
		$("#firstTitle",window.document).html(str);
	});
	$('.nav').delegate('.cluster','click',function(){
		$("#tt").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/group/groups.jsp");
	});
	$('.nav').delegate('.oracledb','click',function(){
		var href=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/databaseList.jsp";
		$("#tt").load(href);
	});
	/*******ldj改-start**********/
	//软件（数据库，文件系统，备份系统）
	$(".softlist a").on("click",function(){
//		var data_type=$(this).attr("type");
		var href=getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/database/databaseList.jsp";
		$("#tt").load(href,{'data_type':1});
		var str="<a style='font-family: Microsoft YaHei;font-size:14px;color:#3c8dbc;cursor: pointer;' onclick=\"loadsev('"+href+"?data_type="+1+"')\">&gt;数据库</a>";
		$("#firstTitle",window.document).html(str);
	});
	/*******ldj改-end**********/
	/*******hdp新增-start*******/
	$('.nav').delegate('.soft-filesys','click',function(){
		$("#tt").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/filesys/filesys.jsp");
	});
	$('.nav').delegate('.soft-backupsys','click',function(){
		$("#tt").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/standbysys/standbysys.jsp");
	});
	
	/*******hdp新增-end********/
	errrorNum();
	$("#onSelect").hide();
});
function loadsev(path){
	$("#tt").load(path);
}

/**
 * 获取服务器、双活、集群、一体机故障数量
 * @author：ldj  WarnlogCon
 */
function errrorNum(){
	$.ajax({
		url:getContextPath()+"/WarnlogCon/geterrrorNum",
		type:"post",
		datatype:"json",
		success:function (data){
			data=eval(data);
			var servNum=data[0].servNum;
			if(servNum==="" || servNum==0 || servNum==null){
				$(".server .error").css({display:"none"});
			}else{
				$(".server .error").css({display:"block"});
				$(".server .error").html(servNum);
			}
			var doubNum=data[0].doubNum;
			if(doubNum==="" || doubNum==0 || doubNum==null){
				$(".active-active .error").css({display:"none"});
			}else{
				$(".active-active .error").css({display:"block"});
				$(".active-active .error").html(data[0].doubNum);
			}
			var clusNum=data[0].clusNum;
			if(clusNum==="" || clusNum==0 || clusNum==null){
				$(".cluster .error").css({display:"none"});
			}else{
				$(".cluster .error").css({display:"block"});
				$(".cluster .error").html(clusNum);
			}
			var inteNum=data[0].inteNum;
			if(inteNum==="" || inteNum==0 || inteNum==null){
				$(".integrate .error").css({display:"none"});
			}else{
				$(".integrate .error").css({display:"block"});
				$(".integrate .error").html(data[0].inteNum);
			}
		}
	});
}
