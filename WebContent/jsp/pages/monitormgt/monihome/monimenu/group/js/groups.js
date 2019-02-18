function goBack(){
	$("#sevcomid").val("");
	$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monihome.jsp");
}
var datt;
$(function(){
	showImg();
	$("#search-btn").click(function(){
		showImg();
	});
	//检索下拉的使用
//	var ked = '#kw';
//	searchFunc(ked,getContextPath()+"/groupsCon/search");
});
function showImg(){
	$.ajax({
		url:getContextPath()+"/groupsCon/search",
		type:'POST',
		data:{
			searchText:$("#kw").val()
//			searchId : comid,
//			searchType : comtype
			},
		dataType:'json',
		success:function(data){
			$("#servinfo").html("");
			var childhtml = "";
				for (var index in data.rows) {
				    childhtml+="<div class='infomenu' style='cursor: pointer;' onclick='showDetail("+data.rows[index].id+")'>"+"<div id='imgg"+index+"'></div>"
				            +"<div class='infotxt' style='text-align: center;font-size: 13px;'>"
				            +data.rows[index].uid+"&nbsp;</br>"+data.rows[index].name+ "</div>"+"</div>";
				}
				$("#servinfo").html(childhtml);
				var errorNum = 0;
				for(var index in data.rows){
					if(data.rows[index].sn == 0){
						errorNum++;
					}
					$("#imgg"+index).css({
						"width":"75%",
						"height":"60%",
						"margin":"5% 14%"
					});
					if(data.rows[index].sn==0){
						$("#imgg"+index).css({
							"background":"url("+getContextPath()+"/jsps/img/grpd_red.png)no-repeat",
							"background-size":"100% 100%"
						});
					}else{
						$("#imgg"+index).css({
							"background":"url("+getContextPath()+"/jsps/img/grpd_green.png)no-repeat",
							"background-size":"100% 100%"
						});
					}
				}
				var totalNum = data.rows.length;
				$(".error").text(errorNum);
				$(".total").text(totalNum);
		}
	});
}
function showDetail(id){
	$("#returnType").val("group");
	$("#sevcomid").val(id);
	$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/server/server.jsp");
}
/**
 * 服务器添加检索功能
 * @param searchText：input输入框检索后台有关uid的值
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
							$("#g2 ul").append("<li "+attr_col+" style=\"width:100%;float:left;line-height:32px;height:32px;cursor:pointer;\">"+data.rows[i].uid+"</li>");
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