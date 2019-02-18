/**
 * 获取页面参数
 */
(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURI(r[2]); return null;
    }
})(jQuery);

/**
 * 获取应用路路径，后面不带/
 * @returns
 */ 
function getContextPath() {
	    var pathName = document.location.pathname;
	    var index = pathName.substr(1).indexOf("/");
	    var result = pathName.substr(0,index+1);
	    return result;
}

function ModelFunction(){
	creatModelGrid();
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-230-20);
	$("#jqGridListModel").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//查询
function doSearchMod(){
	$.jgrid.gridUnload('#jqGridListModel');
	creatModelGrid();
}
function creatModelGrid(){
	jQuery("#jqGridListModel").jqGrid({
		url:getContextPath()+"/healthmodelCon/searchModel",
		datatype:'json',
		postData:{'model_name': $("#model_name").val()},
		colModel:[
				   {label:'model_id',name:'model_id',width:0,key:true,hidden:true},
				   {label:'status',name:'status',width:0,hidden:true},
				   {label:'模型名称',name:'model_name',index:'model_name',width:100,align:'center'},
				   {label:'总分',name:'total_score',index:'total_score',width:100,align:'center'},
				   {label:'描述',name:'model_desc',index:'model_desc',width:200,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
			rownumbers: true,
			rowNum:100,//一页显示多少条
			rowList:[100,200,300],//可供用户选择一页显示多少条
			sortname:'id',//初始化的时候排序的字段
			sortorder:'desc',//排序方式
			mtype:'post',//向后台请求数据的Ajax类型
			viewrecords:true,//定义是否要显示总记录数
			multiselect: false,//复选框
			caption:"<div style='width:95%;'>健康模型<span id='add' style='cursor:pointer;float:right;float:right;' onclick=\"modelAdd(this,'')\"> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
			height:'auto',
			autowidth:true,
			pager : "#jqGridPaperListModel",
			gridComplete: function(){
	            var ids = $("#jqGridListModel").getDataIDs();
	            for(var i=0;i<ids.length;i++){
	                var cl = ids[i];
	                ed = "<a title='编辑' href=\"javascript:void(0);\" onclick='modelAdd(this,"+cl+");'>编辑 </a>";
	                vi = "<a title='删除' href=\"javascript:void(0);\" onclick=\"delModel(this,'"+cl+"')\">删除</a>"; 
	            	var rowData = $("#jqGridListModel").jqGrid('getRowData',cl);
	                jQuery("#jqGridListModel").jqGrid('setRowData',cl,{act:ed+"  "+vi});
	            } 
	        },
	});
}
/**
 * 删除
 * @param obj
 * @param model_id
 */
function delModel(obj,model_id){
	var index1=$("#gview_jqGridListModel table:eq(0) #jqGridListModel_status").index();
	var status=$(obj).parent().parent().find("td:eq("+index1+")").html();
	if(status==1){
		layer.alert("该模型正在进行评估中,不允许删除！");
	}else if(status==2){
		layer.alert("该模型已评估完成,不允许删除！");
	}else{
		layer.confirm("确定删除这条数据？",{
			btn:['确定','取消']
		},function(){
			$.ajax({
				type:"post",
				url:getContextPath()+"/healthmodelCon/delModel",
				async:false,
				data:{model_id:model_id},
				success:function(res){
					if(parseInt(res)>0){
						$(obj).parent().parent().remove();
					}
					layer.msg('删除成功！');
					window.parent.doSearchMod();
				},
				error : function() {
		  			layer.msg('删除失败！');
		  		}
			});
		});
	}
}
/**
 * 复制
 * @param obj
 * @param model_id
 */
function copyModel(){
	if(!validateHandler()){
		return;
	}
	var model_name=$("#copy_model_name").val();
	if(model_name!==""){
		var model_id=$("#copy_model_id").val();
		var model_desc=$("#copy_model_desc").val();
		$.ajax({
			type:"post",
			url:getContextPath()+"/healthmodelCon/copyModel",
			async:false,
			data:{model_id:model_id,model_name:model_name,model_desc:model_desc},
			success:function(){
				$("#tt",parent.document).parent().find("#comm_menu .6001").click();
				$(".show_bombBox").css({display:'none'});
			}
		});
	}else{
		layer.msg("模型名称不能为空!");
	}
}
/**
 * 新增/编辑
 */
function modelAdd(obj,model_id){
	var wid=$("#tt",parent.document).width() - 100;
	var hei=$("#tt",parent.document).height() - 50;
	var tr_index=-1;
	var status=-1;
	if(model_id!==""){
		tr_index=$(obj).parent().parent().index();
		var index1=$("#gview_jqGridListModel table:eq(0) #jqGridListModel_status").index();
		status=$(obj).parent().parent().find("td:eq("+index1+")").html();
	}
	layer.open({
	    type: 2,
	    title: '健康模型', 
	    fix: false,
	    id:"modelAdd",
	    shadeClose: false,
	    area: ["900px","520px"],
//	    area: [wid+"px",hei+"px"],
	    moveOut: true,
	    content: [getContextPath()+"/jsp/pages/baselinemgt/healthmodel/modelAdd.jsp?model_id="+model_id+"&tr_index="+tr_index+"&status="+status,'no']
	});
}
function searchModel(model_id){
	if(model_id!==""){
		$.ajax({
			type:"post",
			url:getContextPath()+"/healthmodelCon/searchModel",
	    	async:false,
	    	data:{model_id:model_id},
	    	success:function(res){
	    		if(typeof(res)!=="undefined" && res!==""){
	    			$("#health_model #model_name").val(res[0]["model_name"]);
	    			$("#health_model #model_desc").val(res[0]["model_desc"]);
	    			$("#health_model #total_score").val(res[0]["total_score"]);
	    		}
			}
		});
	}
}
/******************模型分项处理区域-start*******************/
function creatItemGrid(){
	jQuery("#jqGridListItem").jqGrid({
		url:"",
		datatype:'json',
		postData:{model_id:$("#health_model #model_id").val()},
		colModel:[
				   {label:'model_item_id',name:'model_item_id',width:0,key:true,hidden:true},
				   {label:'模型ID',name:'model_id',index:'model_id',width:0,hidden:true},
				   {label:'分项名称',name:'model_item_name',index:'model_item_name',width:100,align:'center'},
				   {label:'总分',name:'total_score',index:'total_score',width:100,align:'center'},
				   {label:'分项描述',name:'model_item_desc',index:'model_item_desc',width:200,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
			rownumbers: true,
			rowNum:1000,//一页显示多少条
			sortname:'model_item_id',//初始化的时候排序的字段
			sortorder:'desc',//排序方式
			mtype:'post',//向后台请求数据的Ajax类型
			multiselect: false,//复选框
			caption:"<span id='add' style='cursor:pointer;float:right;margin-right:25px;' onclick=\"itemAdd()\"> <i class='icon-plus-sign'></i>&nbsp;新增</span>",
			height:'auto',
			autowidth:true,
			gridComplete: function(){
	            var ids = $("#jqGridListItem").getDataIDs();
	            for(var i=0;i<ids.length;i++){
	                var id = ids[i];
	                vi = "<a title='删除' onclick=\"delItem(this)\">删除</a>"; 
	                jQuery("#jqGridListItem").jqGrid('setRowData',id,{act:vi});
	            } 
	        },
	});
	if($("#jqGridListItem tbody tr:not(.jqgfirstrow)").length==0){
		$.ajax({
			type:"post",
			url:getContextPath()+"/healthmodelCon/searchItem",
	    	async:false,
	    	data:{model_id:$("#health_model #model_id").val()},
	    	success:function(res){
	    		$("#jqGridListItem tbody").append(res);
			}
		});
	}
}
/**
 * 模型分项新增按钮操作
 */
var model_item_id=0;
function itemAdd(){
	if(model_item_id>0){
		model_item_id=parseInt(model_item_id)+1;
	}else{
		$.ajax({
			type:"post",
			url:getContextPath()+"/healthmodelCon/getid",
	    	async:false,
	    	data:{tabname:"h_model_item"},
	    	success:function(res){
	    		model_item_id=res;
			}
		});
	}
	var index=$("#jqGridListItem tbody tr").length;
	var str="<tr class=\"jqgrow ui-row-ltr ui-widget-content saveEdit\">";
	str+="<td style=\"text-align:center;width: 35px;\" class=\"jqgrid-rownum ui-state-default\">"+index+"</td>";
	str+="<td id=\"model_item_id\" style=\"display:none;\"><input type=\"text\" value=\""+model_item_id+"\"/></td>";
	str+="<td id=\"model_item_name\" style=\"text-align:center;width: 100px;\"><input type=\"text\" style=\"width:95%;height:100%;border:0;\" validate=\"nn nspcl\" maxlength=\"20\"/><span class=\"_validate_flag\" style=\"color: red\">*</span></td>";
	str+="<td id=\"total_score\" readonly=\"readonly\" style=\"text-align:center;width: 100px;\"><input type=\"text\" readonly=\"readonly\" style=\"width:100%;height:100%;border:0;\"/></td>";
	str+="<td id=\"model_item_desc\" style=\"text-align:center;width: 200px;\"><input type=\"text\" style=\"width:100%;height:100%;border:0;\"  maxlength=\"100\"/></td>";
	str+="<td  style=\"text-align:center;\"><a id=\"edit_item\" onclick=\"itemEdit('E',this)\" style=\"display:none;\">编辑</a><a onclick=\"itemEdit('D',this)\" style=\"cursor:pointer;\">删除</a></td>";
	str+="</tr>";
	$("#jqGridListItem tbody").append(str);
}
function doclickinfo(obj,idname){
	$(obj).find("#"+idname).click();
}
/**
 * 模型分项 编辑or删除操作
 * @param edit
 * @param obj
 */
var del_item_arr=new Array();
function itemEdit(edit,obj){
	var par_obj=$(obj).parent().parent();
	if(edit==="E"){
//		$(par_obj).find(".isedit").each(function(){
//			$(this).removeClass("isedit");
//			$(par_obj).addClass("saveEdit");
//			var val=$(this).html();
//			var read=$(this).attr("readonly");
//			if(typeof(read)!=="undefined"){
//				read="readonly='readonly'";
//			}else{read="";}
//			$(this).html("<input value=\""+val+"\" "+read+" type=\"text\" style=\"width:95%;height:100%;border:0;\" validate=\"nn\" maxlength=\"20\"/><span class=\"_validate_flag\" style=\"color: red\">*</span>");
//		});
	}else{
		var model_item_id="";
		if($(par_obj).attr("class").indexOf("saveEdit")>-1){
			model_item_id=$(par_obj).find("#model_item_id input").val();
		}else{
			model_item_id=$(par_obj).find("#model_item_id").html();
		}
		del_item_arr.push(model_item_id);
		$(par_obj).remove();
	}
}
/******************模型分项处理区域-end*******************/

/******************模型分项指标处理区域-start*******************/
function loaditemBody(){
	var str="";
	var bool=true;
	$("#jqGridListItem tbody tr:not(.jqgfirstrow)").each(function(){
		var index=$(this).index();
		var model_item_name="";
		if($(this).find("#model_item_name input").length>0){
			model_item_name=$(this).find("#model_item_name input").val();
		}else{
			model_item_name=$(this).find("#model_item_name").html();
		}
		if(model_item_name===""){
			layer.alert("第"+index+"行分项名称未填写，请填写完整!");
			bool=false;
			return false;
		}
		var model_item_id="";
		if($(this).find("#model_item_id input").length>0){
			model_item_id=$(this).find("#model_item_id input").val();
		}else{
			model_item_id=$(this).find("#model_item_id").html();
		}
		str+="<span id="+model_item_id+">"+model_item_name+"</span>";
	});
	if(bool){
		$("#item_metric .item_body").html(str);
		return bool;
	}
}
function creatMetricGrid(){
	jQuery("#jqGridListMetric").jqGrid({
		url:"",
		datatype:'json',
		postData:{'model_name': $("#model_name").val()},
		colModel:[
				   {label:'分项id',name:'model_item_id',index:'model_item_id',width:0,hidden:true},
				   {label:'指标',name:'metric_id',index:'metric_id',width:0,hidden:true},
				   {label:'指标',name:'metric_id_name',index:'metric_id_name',width:100,align:'center'},
				   {label:'总分',name:'total_score',index:'total_score',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
			rownumbers: true,
			rowNum:1000,//一页显示多少条
			sortname:'model_item_id',//初始化的时候排序的字段
			sortorder:'desc',//排序方式
			mtype:'post',//向后台请求数据的Ajax类型
			multiselect: false,//复选框
			caption:"<span id='add' style='cursor:pointer;float:right;margin-right:25px;' onclick=\"metricAdd()\"> <i class='icon-plus-sign'></i>&nbsp;新增</span>",
			height:'auto',
			autowidth:true,
			gridComplete: function(){
	            var ids = $("#jqGridListMetric").getDataIDs();
	            for(var i=0;i<ids.length;i++){
	                var id = ids[i];
	                ed = "";
	                vi = "<a title='删除' onclick=\"metricEdit(this)\">删除</a>"; 
	                jQuery("#jqGridListMetric").jqGrid('setRowData',id,{act:ed+"  "+vi});
	            } 
	        },
	});
}
function getmetricinfo(model_item_id){
	var metric_ids="";
	$("#jqGridListMetric tbody #"+model_item_id).each(function(){
		var metric_id=$(this).find("#metric_id input").val();
		if(typeof(metric_id)==="undefined"){
			metric_id=$(this).find("#metric_id").html();
		}
		if(metric_id!==""){
			metric_ids+=metric_id+",";
		}
	});
	var wid=$("#jqGridListMetric_metric_id_name").width();
	$.ajax({
		type:"post",
		url:getContextPath()+"/healthmodelCon/searchMetric",
    	async:false,
    	data:{model_item_id:model_item_id,metric_ids:metric_ids,wid:wid},
    	success:function(res){
    		$("#jqGridListMetric tbody tr").addClass("sethidden");
    		$("#jqGridListMetric tbody").append(res);
    		$("#jqGridListMetric tbody #"+model_item_id).removeClass("sethidden");
		}
	});
}
/**
 * 模型分项指标 新增操作
 */
function metricAdd(){
	var model_item_id=$("#item_metric .item_body .spanbgcol").attr("id");
	if(typeof(model_item_id)==="undefined" || model_item_id===""){
		layer.alert("请选择模型分项!");
		return;
	}else{
		var wid=$("#jqGridListMetric_metric_id_name").width();
		var index=$("#jqGridListMetric tbody tr").length;
		var str="<tr id=\""+model_item_id+"\"  class=\"jqgrow ui-row-ltr ui-widget-content saveEdit\">";
		str+="<td style=\"text-align:center;width: 35px;\" class=\"jqgrid-rownum ui-state-default\">"+index+"</td>";
		str+="<td id=\"model_item_id\" style=\"display:none;\"><input type=\"text\" value=\""+model_item_id+"\"/></td>";
		str+="<td id=\"metric_id\" style=\"display:none;\"><input type=\"text\"/></td>";
		str+="<td id=\"metric_id_name\" style=\"text-align:center;width: "+wid+"px;\" readonly=\"readonly\" onclick=\"getindex(this)\"><input type=\"text\" readonly=\"readonly\" style=\"width:95%;height:100%;border:0;cursor:pointer;\" validate=\"nn nspcl\" maxlength=\"20\"/><span class=\"_validate_flag\" style=\"color: red\">*</span></td>";
		str+="<td id=\"total_score\" style=\"text-align:center;width: "+wid+"px;\"><input type=\"text\" style=\"width:95%;height:100%;border:0;\" validate=\"nn num\"/><span class=\"_validate_flag\" style=\"color: red\">*</span></td>";
		str+="<td  style=\"text-align:center;width:"+wid+"px\"><a id=\"edit_metric\" onclick=\"metricEdit('E',this)\" style=\"display:none;\">编辑</a><a onclick=\"metricEdit('D',this)\" style=\"cursor:pointer;\">删除</a></td>";
		str+="</tr>";
		$("#jqGridListMetric tbody").append(str);
	}
}
/**
 * 模型分项指标 编辑or删除操作
 * @param edit
 * @param obj
 */
var del_metric_arr=new Array();
function metricEdit(edit,obj){
	var par_obj=$(obj).parent().parent();
	if(edit==="E"){
//		$(par_obj).find(".isedit").each(function(){
//			$(par_obj).addClass("saveEdit");
//			var val=$(this).html();
//			$(this).removeClass("isedit");
//			var read=$(this).attr("readonly");
//			if(typeof(read)!=="undefined"){
//				read="readonly='readonly'";
//			}else{read="";}
//			$(this).html("<input value=\""+val+"\" "+read+"  type=\"text\" style=\"width:100%;height:100%;border:0;\"/>");
//		});
	}else{
		var model_item_id="",metric_id="";
		if($(par_obj).attr("class").indexOf("saveEdit")>-1){
			model_item_id=$(par_obj).find("#model_item_id input").val();
			metric_id=$(par_obj).find("#metric_id input").val();
		}else{
			model_item_id=$(par_obj).find("#model_item_id").html();
			metric_id=$(par_obj).find("#metric_id").html();
		}
		del_metric_arr.push("update h_model_item_metric set use_flag='0' where model_item_id="+model_item_id+" and metric_id="+metric_id);
		$(par_obj).remove();
	}
}
/**
 * 获取指标项信息
 * @param obj
 */
function getindex(obj){
	if($(obj).parent().attr("class").indexOf("saveEdit")>-1){//判定只有是编辑状态下才允许选择指标项
		var trIndex=$(obj).parent().index();
		var id=$(obj).parent().attr("id");
		var index_ids="";
		$("#jqGridListMetric #"+id).each(function(){
			var index_id=$(this).find("#metric_id input").val();
			if(typeof(index_id)==="undefined"){
				index_id=$(this).find("#metric_id").html();
			}
			if(index_id!==""){
				index_ids+=index_id+",";
			}
		})
		layer.open({
	  	    type: 2,
	  	    title: '指标项', 
	  	    fix: false,
	  	    id:'indexList',
	  	    shadeClose: false,
	  	    area: ["750px","455px"],
	  	    content: [getContextPath()+"/jsp/pages/baselinemgt/healthmodel/indextypeList.jsp?trIndex="+trIndex+"&index_ids="+index_ids,'no']
	  	});
	}
}
/******************模型分项指标处理区域-end*******************/
/**
 * 保存
 */
function doSave(obj){
	if(!validateHandler()){
		return;
	}
    $(obj).unbind();
	var bool=true;
	/**
	 * 健康模型 
	 */
	var model_id=$("#health_model #model_id").val();
	var model_name=$("#health_model #model_name").val();
	var model_desc=$("#health_model #model_desc").val();
	var modeStr="[{\"model_id\":\""+model_id+"\",\"model_name\":\""+model_name+"\",\"model_desc\":\""+model_desc+"\"}]";
	//编辑的时候判断该模型是否正在评分使用中，如果正在评分中则不能编辑
	if(model_id != ""){
		var status = $("#status").val();
		if(parseInt(status)===1){
	    	layer.alert("该模型正在被评分使用中，暂时不能进行编辑！");
	    	return;
		}
	}
	/**
	 * 模型分项
	 */
	var item_metric=new Array();
	$("#jqGridListItem tbody tr:not(.jqgfirstrow)").each(function(){
		var i_index=$(this).index();
		var model_item_name="";
		if($(this).attr("class").indexOf("saveEdit")>-1){
			model_item_name=$(this).find("#model_item_name input").val();
		}else{
			model_item_name=$(this).find("#model_item_name").html();
		}
		if(model_item_name===""){
			layer.alert("【模型分项】中第"+i_index+"行分项名称未填写，请填写完整!");
			bool=false;
			return false;
		}else{
			var metric_ids="";
			var model_item_id="";
			if($(this).attr("class").indexOf("saveEdit")>-1){
				model_item_id=$(this).find("#model_item_id input").val();
			}else{
				model_item_id=$(this).find("#model_item_id").html();
			}
			/****模型分项指标-start****/
			var metric_arr=new Array();
			$("#jqGridListMetric tbody #"+model_item_id).each(function(){
				var metric_id="";
				if($(this).attr("class").indexOf("saveEdit")>-1){
					var m_index=$(this).index();
					var model_item_id=$(this).find("#model_item_id input").val();
					if(model_item_id===""){
						layer.alert("【模型分项指标】中第"+m_index+"行未选择指标!");
						bool=false;
						return false;
					}
					metric_id=$(this).find("#metric_id input").val();
					var total_score=$(this).find("#total_score input").val();
					if(isNaN(total_score)){
						layer.alert("【模型分项指标】中第"+m_index+"行总分请填写正确数值!");
						bool=false;
						return false;
					}
					metric_arr.push("{\"model_item_id\":\""+model_item_id+"\",\"metric_id\":\""+metric_id+"\",\"total_score\":\""+total_score+"\"}");
				}else{
					metric_id=$(this).find("#metric_id").html();
				}
				metric_ids+=metric_id+",";
			});
			/****模型分项指标-end****/
			var model_item_desc="";
			if($(this).attr("class").indexOf("saveEdit")>-1){
				model_item_desc=$(this).find("#model_item_desc input").val();
			}else{
				model_item_desc=$(this).find("#model_item_desc").html();
			}
			item_metric.push("{\"model_item_id\":\""+model_item_id+"\",\"model_item_name\":\""+model_item_name+"\",\"model_item_desc\":\""+model_item_desc+"\",\"metric_ids\":\""+metric_ids+"\",\"model_item_metric\":["+metric_arr.join(",")+"]}");
		}
	});
	
	if(bool){
		var item_metric="["+item_metric.join(",")+"]";
		$.ajax({
			type:"post",
			url:getContextPath()+"/healthmodelCon/saveinfo",
			async:false,
			data:{modeStr:modeStr,item_metric:item_metric,del_item_arr:del_item_arr.join(","),del_metric_arr:del_metric_arr.join("%TAB%")},
			success:function (res){
				if(model_id===""){
					$("#modelAdd",parent.document).parent().parent().find("#comm_menu .6001").click();
				}else{
					var f_obj=$("#modelAdd",parent.document).parent().parent().find("#gview_jqGridListModel");
					var index1=$(f_obj).find("table:eq(0) #jqGridListModel_model_name").index();
					var index2=$(f_obj).find("table:eq(0) #jqGridListModel_total_score").index();
					var index3=$(f_obj).find("table:eq(0) #jqGridListModel_model_desc").index();
					var tr_index=$("#tr_index").val();
					$(f_obj).find("#jqGridListModel tbody tr:eq("+tr_index+") td:eq("+index1+")").html(model_name);
					$(f_obj).find("#jqGridListModel tbody tr:eq("+tr_index+") td:eq("+index2+")").html(res);
					$(f_obj).find("#jqGridListModel tbody tr:eq("+tr_index+") td:eq("+index3+")").html(model_desc);
				}
				layer.alert('保存成功！', function(){
					window.parent.doSearchMod();
					btnClose();
				});
			},
			error : function() {
	  			layer.alert('保存失败！');
	  		}
		});
	}
}
//关闭
function btnClose(){
	parent.layer.closeAll();
}
