function goBack(){
	$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monihome.jsp");
}
var datt;
var inputvaldata;
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
//	searchFunc(ked,getContextPath()+"/activeCon/search");
});
//展示图标功能
function showImg(){
	$.ajax({
		url:getContextPath()+"/activeCon/search",
		type:'POST',
		data:{
				searchText : $("#kw").val()
			 },
		dataType:'json',
		success:function(data){
			var totalNum = data.rows.length;
			$(".total").text(totalNum);
			$("#servinfo").html("");
			var childhtml = "";
				for (var index in data.rows) {
				    childhtml+="<div class='infomenu' style='cursor: pointer;' onclick='showDetail("+data.rows[index].id+")'>"+"<div id='imgg"+index+"'></div>"
				            +"<div class='infotxt' style='text-align: center;'>"
				            +data.rows[index].uid+"</br>&nbsp;<span class='name'>"+data.rows[index].name+ "</span></div></div>";
				}
				
				$("#servinfo").html(childhtml);
				var errorNum = 0;
				for(var index in data.rows){
					if(data.rows[index].healthstatus == 0){
						errorNum++;
					}
					
					$("#imgg"+index).css({
						"width":"60%",
						"height":"50%",
						"margin":"10% 20%"
					});
					if(data.rows[index].healthstatus==0){
						$("#imgg"+index).css({
							"background":"url("+getContextPath()+"/jsps/img/doude_red.png)no-repeat",
							"background-size":"100% 100%"
						});
					}else{
						$("#imgg"+index).css({
							"background":"url("+getContextPath()+"/jsps/img/doude_green.png)no-repeat",
							"background-size":"100% 100%"
						});
					}
				}
				$(".error").text(errorNum);
		}
	});
}
function showDetail(douid){
	var comtype = 'double';
	$('#tt').load(getContextPath()+"/jsp/pages/monitormgt/monihome/monimenu/double/double.jsp?double_id="+douid);
}
/**
 * 服务器添加检索功能
 * @param searchText：input输入框检索后台有关UID 的值
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