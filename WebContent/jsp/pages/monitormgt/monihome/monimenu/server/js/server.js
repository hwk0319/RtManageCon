function goBack(){
	var returnType = $("#returnType").val();
	if(returnType == "group"){
		$("#returnType").val("");
		$("#tt").load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/group/groups.jsp");
	}else{
		$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monihome.jsp");
	}
	$("#firstTitle",window.parent.document).html("");
	$("#secondtitle",window.parent.document).html("");
}
var datt;
var inputvaldata;
var sevcomid = $("#sevcomid").val();

//加载页面执行
$(function(){
	//默认显示图标页面
	showImg();
	//搜索按钮显示不同的搜索项
	$("#search-btn").click(function(){
		showImg();
	});
	//检索下拉的使用
//	var ked = '#kw';
//	searchFunc(ked,getContextPath()+"/monitorhomeCon/search");
});
//展示图标功能
function showImg(){
	if(sevcomid == 'null'){
		sevcomid = '';
	}
	$.ajax({
		url:getContextPath()+"/monitorhomeCon/search",
		type:'POST',
		data:{
			searchText:$("#kw").val(),
			grpcomid : sevcomid
		},
		dataType:'json',
		success:function(result){
			var totalNum = result.rows.length;
			$(".total").text(totalNum);
			$("#servinfo").html("");
			var childhtml = "";
				for (var index in result.rows) {
				    childhtml+="<div class='infomenu' style='cursor: pointer;' onclick='showDetail("+result.rows[index].uid+","+result.rows[index].id+")'>"+"<div id='imgg"+index+"'></div>"
				            +"<div class='infotxt' style='text-align:center'>"
				            +"<span>"+result.rows[index].in_ip+"</span></br><span class='name'>"+result.rows[index].name+ "</span></div>"+"</div>";
				}
				
				$("#servinfo").html(childhtml);
				var errorNum = 0;
				//设置有关不同健康状态的显示不同灯（红为异常，绿表示正常）
				for(var index in result.rows){
					$("#imgg"+index).css({
						"width":"70%",
						"height":"60%",
						"margin":"5% 14%"
					});
					if(result.rows[index].serverstatus !='0'){
						$("#imgg"+index).css({
							"background":"url("+getContextPath()+"/jsps/img/app_green.png)no-repeat",
							"background-size":"100% 100%"
						});
					}else{
						$("#imgg"+index).css({
							"background":"url("+getContextPath()+"/jsps/img/app_red.png)no-repeat",
							"background-size":"100% 100%"
						});
						errorNum++;
					}
				}
				$(".error").text(errorNum);
		}
	});
}
//图文模块跳转
function showDetail(uid,id){
//	$("#sevcomid").val(id);
	$("#sevuid").val(uid);
	$("#sevid").val(id);
	//返回类型，用来判断返回到那个页面
	var returnType=  $("#returnType").val();
	 if(returnType == ""){
	   	 $("#returnType").val("servers");
	 }
	$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/serverParticulars.jsp");
}
//列表的链接跳转
function showServerDetail(cl){
	$.ajax({
		url:getContextPath()+"/monitorhomeCon/searchDetailById",
		type:"post",
		data:{
			id:cl,
		},
		success:function(data){
			showDetail(data.uid,cl);
		}
	});
}

/**
 * 服务器添加检索功能
 * @param searchText：input输入框检索后台有关name 的值
 * @param url：查询url
 * @author：hdp
 */
function searchFunc(inputid,url){
	var uidWid=$(inputid).width()+0;
	$(inputid).css({marginBottom:"0"}).attr("autocomplete","off");//去除Input输入的缓存
	$(inputid).after("<div id='g2' style=\"width:"+uidWid+"px;position: absolute;border:1px solid #cccccc;border-top: 0;background: #fff;margin-top:36px;margin-left:0px;overflow: hidden;z-index:9999;display:none;\"><ul style=\"width:"+uidWid+"px;list-style:none;padding:0;margin:0;overflow: hidden;\"></ul></div>");
	$(inputid).keyup(function(){
		var uid=$(inputid).val(); 
		if(uid!==""){
			$.ajax({
				type:"post",
				url:url,
				async:false,
				data:{searchText:uid},
				success:function(data){
					$("#g2 ul").html("");
					if(data.rows.length>0&&typeof(data)!=="undefined"){
						$("#g2").css({display:"block"});
// 						$("#g2 ul").append("<li read='false' style=\"width:100%;float:left;line-height:22px;cursor:pointer;\">&nbsp;&nbsp;</li>");
						var columns="";
						for(var i=0,alllen=data.rows.length;i<alllen;i++){
							var res=data.rows[i];
							var attr_col="";
							for(var key in res){
								columns+=key+",";
								var val=res[key];
								val=val===null?"":val;
								attr_col+=key+"='"+val+"' ";
							}
							$("#g2 ul").append("<li "+attr_col+" style=\"width:100%;float:left;line-height:32px;height:32px;cursor:pointer;font-size:13px;\">"+data.rows[i].name+"</li>");
						}
						$("#g2 ul li").on("click",function(){
							$("#kw").val($(this).html());
							$("#g2").css({'display':"none"});
						});
						$("#search-btn").click(function(){
							$("#g2").css({'display':"none"});
						});
					}else{
						$("#g2").css({'display':"none"});
					}
				}
			});
		}
	});
}