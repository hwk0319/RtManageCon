var flag=0;
var lef=230,wid=0,top=50,hei=0;
var modulus;
var jwt;
function mod(){
	//公钥
	modulus = $("#publicKey",parent.document).val();
	jwt = $("#jwt",parent.document).val();
}
function initindexTypeFun(){
	wid=$(window).width()-lef-30;
	hei=$(window).height()-177;
	creatIndTGrid("");
	$("body").on("click","#indTypeSrch",function(e){
		doSearchindex();
	});
	$("#tabs").tabs();
}
function creatIndTGrid(param){
	param=param===""?"":"?"+param;
	jQuery("#jqGridListIndextype").jqGrid({
		url:getContextPath()+"/indextypeCon/search"+param,
		datatype:'json',//请求数据返回的类型
		postData:{'indextype_id': $("#indextype_id_search").val(),'name': $("#name_search").val()},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'指标分类ID',name:'indextype_id',index:'indextype_id',width:100,align:'center'},
				   {label:'名称',name:'name',index:'name',width:100,align:'center'},
				   {label:'描述',name:'remark',index:'remark',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
//			loadonce:true,
			rownumbers: true,
//			altRows:true,//开启隔行阴影
			rowNum:100,//一页显示多少条
			rowList:[100,200,300],//可供用户选择一页显示多少条
			sortname:'id',//初始化的时候排序的字段
			sortorder:'desc',//排序方式
			mtype:'post',//向后台请求数据的Ajax类型
			viewrecords:true,//定义是否要显示总记录数
			caption:"<div style='color:#000;width:95%;'>指标分类<span id='add' style='cursor:pointer;float:right;' onclick='indTypeAdd()'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
			height:'auto',
			autowidth:true,
			editurl:getContextPath()+"/indextypeCon/update",//编辑地址
			pager : "#jqGridPaperListIndextype",
			gridComplete: function(){
	            var ids = $("#jqGridListIndextype").getDataIDs();
	            for(var i=0;i<ids.length;i++){
	                var cl = ids[i];
	                ed = "<a title='编辑' style='cursor:pointer;' onclick='indTypeAdd("+cl+")'>编辑 </a>";
	                vi = "<a title='删除' style='cursor:pointer;' onclick='doDelete("+cl+")'>删除</a>"; 
	                jQuery("#jqGridListIndextype").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
	            } 
	        }
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListIndextype").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//重新加载页面
function doSearchindex(){
	$.jgrid.gridUnload('#jqGridListIndextype');
	creatIndTGrid("");
}
function afterShowForm(){
	doindTypeCong("E");
}
function indTypeAdd(id){
	getJWT();
	layer.open({
  	    type: 2,
  	    title: '指标分类', 
  	    fix: false,
  	    shadeClose: false,
//  	    area: ['70%', '85%'],
  	    area: ['940px', '560px'],
  	    content: [getContextPath()+"/jsp/pages/configmgt/indextype/indexTypeAdd.jsp?id="+id,'no']
  	});
}
/**
 * 保存前校验
 */
var fn_beforeSubmit=function(postdata,formid){
	var id=postdata["id"];
	var indextype_id=postdata["indextype_id"];
	var mess="";
	$.ajax({
    	type:"post",
    	url:getContextPath()+"/indextypeCon/checkindexType?id="+id+"&indextype_id="+indextype_id,
    	async:false,
    	success:function(res){
    		mess=res;
    	}
	});
    if(mess===""){
    	return [true,""];
	}else{
		alert(mess);
		return [false,""];
	}
}
/**
 * 保存后
 */
var fn_afterSubmit=function(response,postdata){
	/**
	 * 处理指标项数据
	 */
	var index_arr="";
	$("#indexInfo .formName").each(function(){
		var id=$(this).find("#id").val();
		var index_id=$(this).find("#index_id").val();
		var description=$(this).find("#description").val();
		var warn_rule=$(this).find("#warn_rule").val();
		var upper_limit=$(this).find("#upper_limit").val();
		var lower_limit=$(this).find("#lower_limit").val();
		var std_value=$(this).find("#std_value").val();
		var remark=$(this).find("#remark").val();
		index_arr+="id:"+id+"&COL&index_id:"+index_id+"&COL&description:"+description+"&COL&warn_rule:"+warn_rule+"&COL&";
		index_arr+="upper_limit:"+upper_limit+"&COL&lower_limit:"+lower_limit+"&COL&std_value:"+std_value+"&COL&remark:"+remark+"&TAB&";
	});
//	var index_type=$("#TblGrid_jqGridListIndextype #indextype_id").val();
	var index_type=$("#indextype_id").val();
	$.ajax({
		type:"post",
		url:getContextPath()+"/indexCon/updates",
		async:false,
		data:{index_type:index_type,index_arr:index_arr},
		success:function(data){
		}
	});
	return [true,""];
}
/**
 * 加载指标项数据
 * @param type:新增/编辑
 */
function doindTypeCong(type){
	if(type==="E"){
		var indextype_id=$("#indextype_id").val();
		$.ajax({
			type:"post",
			url:getContextPath()+"/indexCon/search",
			async:false,
			data:{index_type:indextype_id},
			success:function(data){
				if(typeof(data)!=="undefined" && data!==""){
					var Astr="";
					var pub_sty="width:100%;display:block;float:left;overflow:hidden;";
					for(var i=0,allsize=data.length;i<allsize;i++){
						var index_id=data[i]["index_id"];
						var description=data[i]["description"] == null ? "" : data[i]["description"];
						var warn_rule=data[i]["warn_rule"];
						var upper_limit=data[i]["upper_limit"];
						var lower_limit=data[i]["lower_limit"];
						var std_value=data[i]["std_value"];
						var remark=data[i]["remark"] == null ? "" : data[i]["remark"];
						Astr+="<div id=\"index_div"+i+"\" class='cong_info'   style=\"width:calc((100% / 5) - 15px);height:100px;float:left;margin:10px 0 0 10px;border:1px solid #E0E0E0;border-radius:5px;overflow:hidden;line-height:25px;text-indent:3px;font-size: 13px;\">";
						Astr+="<input value=\""+data[i]["id"]+"\" type=\"hidden\" id=\"id\"/>";
					    Astr+="<span class='index_id' style='"+pub_sty+"background-color:#f6f8fa;width:85%;'>"+index_id+"</span><input value=\""+index_id+"\" type=\"hidden\" id=\"index_id\"/>";
					    Astr+="<span onclick=\"delIndex(this)\" class=\"del_index\" style=\"white-space:nowrap;background-color:#f6f8fa;width:15%;float:left;cursor:pointer;height:25px;\"><img style=\"width:20px; height:20px;float:right;\" src=\""+getContextPath()+"/imgs/closexx.png\"/></span>";
						Astr+="<div onclick=\"show_index(this,'E')\" style=\"width:100%;cursor:pointer;\">";
					    Astr+="<span class='description' style='"+pub_sty+"white-space:nowrap;'>描述："+description+"</span><input value=\""+description+"\" type=\"hidden\" id=\"description\"/>";
					    Astr+="<input value=\""+warn_rule+"\" type=\"hidden\" id=\"warn_rule\"/>";
					    Astr+="<input value=\""+upper_limit+"\" type=\"hidden\" id=\"upper_limit\"/>";
						Astr+="<input value=\""+lower_limit+"\" type=\"hidden\" id=\"lower_limit\"/>";
						Astr+="<input value=\""+std_value+"\" type=\"hidden\" id=\"std_value\"/>";
						Astr+="<span class='remark' style='"+pub_sty+"height:50px;'>详细阐述：<br/>"+remark+"</span><input value=\""+remark+"\" type=\"hidden\" id=\"remark\"/>";
						Astr+="</div>";
						Astr+="</div>";
					}
					$("#indexInfo").append(Astr);
					$("#indexInfo .del_index img").hover(function(){  
			            $(this).css("background-color","#FF8888"); 
			        },function(){  
			        	$(this).css("background-color","#f6f8fa"); 
			        });
				}
			}
		});
	}
}
/**
 * 指标项的新增/编辑处理
 * @param obj
 * @param type:I(新增)/E(编辑)
 */
function show_index(obj,type){
	var param="";
	var id="",index_id="",description="",warn_rule="",upper_limit="",lower_limit="",std_value="",remark="",divIndex="";
	if(type==="E"){
		id=$(obj).parent().find("#id").val();
		index_id=$(obj).parent().find("#index_id").val();
		description=$(obj).parent().find("#description").val();
		description=encodeURI(description);
		warn_rule=$(obj).parent().find("#warn_rule").val();
		upper_limit=$(obj).parent().find("#upper_limit").val();
		if(upper_limit == "null"){
			upper_limit = "";
		}
		lower_limit=$(obj).parent().find("#lower_limit").val();
		if(lower_limit == "null"){
			lower_limit = "";
		}
		std_value=$(obj).parent().find("#std_value").val();
		if(std_value == "null"){
			std_value = "";
		}
		remark=$(obj).parent().find("#remark").val();
		remark=encodeURI(remark);
		divIndex=$(obj).parent().attr("id");
	}
	param="?id="+id+"&index_id="+index_id+"&description="+description+"&warn_rule="+warn_rule+"&upper_limit="+upper_limit+"&lower_limit="+lower_limit+"&std_value="+std_value+"&remark="+remark+"&divIndex="+divIndex+"&type="+type;
	layer.open({
	    type: 2,
	    title: '指标项', 
	    fix: false,
	    shadeClose: false,
	    area: ['600px', '350px'],
	    btn: ['确定', '取消'],
	    yes: function(index, layero){
  	 		//调用保存方法
	         var iframeWin = window[layero.find('iframe')[0]['name']];
	         iframeWin.btnOk();
        },
        btn2: function(){
         	 layer.closeAll();
        },
	    content: [getContextPath()+"/jsp/pages/configmgt/indextype/indexAdd.jsp"+param,'no']
	});
}
/**
 * 从表确定
 */
function btnOk() {
	if(!validateHandler()){
		return;
	}
	var retbool=true;
	var index_id = $("#index_id").val();
	if(index_id === ""){
		alert("请输入指标分类ID！");
		retbool=false;
		return;
	}
	var name = $("#name").val();
	if(name == ""){
		alert("请输入名称！");
		retbool=false;
		return;
	}
	var upper_limit = $("#upper_limit").val();
	var lower_limit = $("#lower_limit").val();
	if(upper_limit!="" && isNaN(upper_limit)==true){
		alert("阙值上限非数值！");
		retbool=false;
		return;
	}
	if(lower_limit!="" && isNaN(lower_limit)==true){
		alert("阙值下限非数值！");
		retbool=false;
		return;
	}
	if(upper_limit!=="" && lower_limit!==""){
		var A=parseFloat(upper_limit)-parseFloat(lower_limit);
		if(A<0){
			alert("阈值上限不能小于阈值下限!");
			retbool=false;
			return;
		}
	}
	var std_value = $("#std_value").val();
/*	if(std_value!="" && isNaN(std_value)==true){
		alert("标准值非数值！");
		retbool=false;
		return;
	}*/
	if(retbool==true){
		var id=$("#id").val();
		var index_id=$("#index_id").val();
		var description=$("#description").val();
		var warn_rule=$("#warn_rule").val();
		var upper_limit=$("#upper_limit").val();
		var lower_limit=$("#lower_limit").val();
		var std_value=$("#std_value").val();
		var remark=$("#remark").val();
		var divIndex=$("#divIndex").val();
		var parObj=$("iframe",window.parent.document).parent().parent().parent();
		if(divIndex!=="" && typeof(divIndex)!=="undefined"){//修改
			var A=$(parObj).find("#"+divIndex).attr("class");
			if(A.indexOf("formName")<0){
				$(parObj).find("#"+divIndex).addClass("formName");
			}
			$(parObj).find("#"+divIndex+" #id");
			$(parObj).find("#"+divIndex+" .index_id").text(index_id);
			$(parObj).find("#"+divIndex+" #index_id").val(index_id);
			$(parObj).find("#"+divIndex+" .description").text("描述："+description);
			$(parObj).find("#"+divIndex+" #description").val(description);
			$(parObj).find("#"+divIndex+" #warn_rule").val(warn_rule);
			$(parObj).find("#"+divIndex+" #upper_limit").val(upper_limit);
			$(parObj).find("#"+divIndex+" #lower_limit").val(lower_limit);
			$(parObj).find("#"+divIndex+" #std_value").val(std_value);
			$(parObj).find("#"+divIndex+" .remark").html("详细阐述：<br/>"+remark);
			$(parObj).find("#"+divIndex+" #remark").val(remark);
		}else{//新增
			var num=$(parObj).find("#indexInfo .cong_info").length+1;
			var pub_sty="width:100%;display:block;float:left;overflow:hidden;";
			var Astr="";
				Astr+="<div id=\"index_div"+num+"\" class='cong_info formName' style=\"width:calc((100% / 6) - 15px);height:100px;float:left;margin:10px 0 0 10px;border:1px solid #E0E0E0;border-radius:5px;overflow:hidden;line-height:25px;text-indent:3px;font-size:13px;\">";
				Astr+="<input value=\"\" type=\"hidden\" id=\"id\"/>";
				Astr+="<span class='index_id' style='"+pub_sty+"background-color:#f6f8fa;width:85%;'>"+index_id+"</span><input value=\""+index_id+"\" type=\"hidden\" id=\"index_id\"/>";
				Astr+="<span onclick=\"delIndex(this)\" class=\"del_index\" style=\"white-space:nowrap;background-color:#f6f8fa;width:15%;float:left;cursor:pointer;height:25px;\"><img style=\"width:20px; height:20px;float:right;\" src=\""+getContextPath()+"/imgs/closexx.png\"/></span>";
			    Astr+="<div onclick=\"show_index(this,'E')\" style=\"width:100%;cursor:pointer;\">";
				Astr+="<span class='description' style='"+pub_sty+"white-space:nowrap;'>描述："+description+"</span><input value=\""+description+"\" type=\"hidden\" id=\"description\"/>";
				Astr+="<input value=\""+warn_rule+"\" type=\"hidden\" id=\"warn_rule\"/>";
				Astr+="<input value=\""+upper_limit+"\" type=\"hidden\" id=\"upper_limit\"/>";
				Astr+="<input value=\""+lower_limit+"\" type=\"hidden\" id=\"lower_limit\"/>";
				Astr+="<input value=\""+std_value+"\" type=\"hidden\" id=\"std_value\"/>";
				Astr+="<span class='remark' style='"+pub_sty+"'>详细阐述："+remark+"</span><input value=\""+remark+"\" type=\"hidden\" id=\"remark\"/>";
				Astr+="</div>";
				Astr+="</div>";
			$(parObj).find("#indexInfo").prepend(Astr);
			$(parObj).find("#indexInfo .del_index img").hover(function(){  
				$(this).css("background-color","#FF8888"); 
	        },function(){  
	        	$(this).css("background-color","#f6f8fa"); 
	        });
		}
		btnClose();
	}
}
/**
 * 指标项删除
 * @param obj
 */
function delIndex(obj){
	layer.confirm("确定删除指标项吗？",{
		btn:['确定','取消']
	},function(){
		layer.closeAll();
		var id=$(obj).parent().find("#id").val();
		if(id!=="" && typeof(id)!=="undefined"){
			$.ajax({
				type:"post",
				url:getContextPath()+"/indexCon/update",
				async:false,
				data:{id:id,oper:"del"},
				success:function(data){
					$(obj).parent().remove();
					layer.msg("删除成功！");
				}
			});
		}else{
			$(obj).parent().remove();
		}
	},function(){
		layer.closeAll();
	});
}
function changeInput(obj){
	var warn_rule=parseInt($(obj).val());
	if(warn_rule===1){
		$("#upper_limit").removeAttr("readonly").css({"background-color":"#fff"});
		$("#lower_limit").removeAttr("readonly").css({"background-color":"#fff"});
		$("#std_value").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
		//添加必填标识
		 $(".upValFlag").html("*");
		 $(".downValFlag").html("*");
		 $(".stdValFlag").html("");
	}else if(warn_rule===2){
		$("#upper_limit").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
		$("#lower_limit").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
		$("#std_value").removeAttr("readonly").css({"background-color":"#fff"});
		//添加必填标识
		 $(".stdValFlag").html("*");
		 $(".upValFlag").html("");
		 $(".downValFlag").html("");
	}else{
		$("#upper_limit").removeAttr("readonly").css({"background-color":"#fff"});
		$("#lower_limit").removeAttr("readonly").css({"background-color":"#fff"});
		$("#std_value").removeAttr("readonly").css({"background-color":"#fff"});
		//移除必填标识
		 $(".upValFlag").html("");
		 $(".downValFlag").html("");
		 $(".stdValFlag").html("");
	}
}
//关闭
function btnClose(){
	$("iframe",window.parent.document).parent().parent().parent().find(".layui-layer-shade").remove();
	$("iframe",window.parent.document).parent().parent().remove();
}

//保存指标分类
function doSave(){
	//页面校验
	if(!validateHandler()){
		return;
	}
	//保存之前校验  判断指标分类ID是否重复
	var id = $("#indexTypeId").val();
	var indextype_id = $("#indextype_id").val();
	var name = $("#name").val();
	var remark = $("#remark").val();
	//拼接数据字段
  	var mData = ""+id+indextype_id+name+"";
  		mData = encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	var mess="";
	$.ajax({
    	type:"post",
    	url:getContextPath()+"/indextypeCon/checkindexType?id="+id+"&indextype_id="+indextype_id,
    	async:false,
    	success:function(res){
    		mess=res;
    	}
	});
    if(mess !=""){
    	layer.alert(mess , {
			title: "提示"
		});
		return;
	}
    var index;
	$.ajax({
		type:"post",
		url:getContextPath()+"/indextypeCon/update?oper=saveOrUpdate",
		async:false,
		data:{
			'indextype_id':$("#indextype_id").val(),
			'name':$("#name").val(),
			'remark':$("#remark").val(),
			'mData':mData,
			'id':$("#indexTypeId").val(),
  			"jwt" : jwt
		},
		beforeSend: function () {
  			index = layer.load(1, {
  			  shade: [0.3,'#fff']
  			});
  	    },
  	    complete:function () {
  	    	layer.close(index);
	    },
		success:function(result){
			for (key in result) {
  				if(key == "success"){
						var msg = addIndex();
		  				if(msg){
			  				layer.alert(result[key], {
			  					title: "提示"
							},function(){
		  						window.parent.doSearchindex();
		  			   			parent.layer.closeAll();
							})
		  				}else{
//		  					layer.alert("保存失败！");
		  				}
  				}else{
  					layer.alert(result[key]);
  				}
  			}
		},
		error:function(){
			layer.alert("保存失败！");
		}
	});
}
//保存指标项数据
function addIndex(){
	var res = true;
	/**
	 * 处理指标项数据
	 */
	var index_arr="";
	$("#indexInfo .formName").each(function(){
		var id=$(this).find("#id").val();
		var index_id=$(this).find("#index_id").val();
		var description=$(this).find("#description").val();
		var warn_rule=$(this).find("#warn_rule").val();
		var upper_limit=$(this).find("#upper_limit").val();
		var lower_limit=$(this).find("#lower_limit").val();
		var std_value=$(this).find("#std_value").val();
		var remark=$(this).find("#remark").val();
		index_arr+="id:"+id+"&COL&index_id:"+index_id+"&COL&description:"+description+"&COL&warn_rule:"+warn_rule+"&COL&";
		index_arr+="upper_limit:"+upper_limit+"&COL&lower_limit:"+lower_limit+"&COL&std_value:"+std_value+"&COL&remark:"+remark+"&TAB&";
	});
	var index_type=$("#indextype_id").val();
	//拼接数据字段
  	var mData = ""+index_type+index_arr+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	$.ajax({
		type:"post",
		url:getContextPath()+"/indexCon/updates",
		async:false,
		data : {
			"mData" : mData,
			index_type : index_type,
			index_arr : index_arr,
  			"jwt" : jwt
		},
		success:function(result){
			for (key in result) {
  				if(key == "success"){
	  				layer.alert(result[key], {
						title: "提示"
					},function(){
						layer.close(index);
					});
  				}else{
  					res = false;
  					layer.alert(result[key]);
  				}
  			}
		}
	});
	return res;
}

//删除
function doDelete(id){
	var jwt = getJWT();
	layer.confirm("确定删除这数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/indextypeCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
				window.parent.doSearchindex();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}