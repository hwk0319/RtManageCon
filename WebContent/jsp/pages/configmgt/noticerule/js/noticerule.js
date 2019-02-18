var type_arr={},level_arr={},way_arr={},address_arr={};
function initNotrfunction(){
	creatNotrGrid();
	type_arr=$._PubSelect($("#type_search"),getContextPath()+"/dictCon/search?use_flag=true&type=warn_type","[{'getKey':'value','getVal':'name'}]");
	level_arr=$._PubSelect($("#level_search"),getContextPath()+"/dictCon/search?use_flag=true&type=warn_level","[{'getKey':'value','getVal':'name'}]");
//	way_arr=$._PubSelect($("#way_search"),getContextPath()+"/dictCon/search?use_flag=true&type=inform_way","[{'getKey':'value','getVal':'name'}]");
//	address_arr=$._PubSelect("",getContextPath()+"/addressCon/search","[{'isedHtml':'false'}]");
	$("body").on("click","#notrSrch",function(e){
		doNotrSearch();
	});
}
function creatNotrGrid(){
	var address_name="";
	jQuery("#jqGridListNotr").jqGrid({
		url:getContextPath()+"/noticeruleCon/search",
		datatype:'json',
		postData : {
			'type' : $("#type_search").val(),
			'level' : $("#level_search").val()
		},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'规则编码',name:'notice_bh',index:'notice_bh',hidden:true},
				   {label:'告警类型',name:'warntypeName',index:'warntypeName',width:100,align:'center'},
				   {label:'告警等级',name:'warnlevelName',index:'warnlevelName',width:100,align:'center'},
				   {label:'联系人ID',name:'address_id',index:'address_id',width:100,align:'center',hidden:true},
				   {label:'联系人姓名',name:'address_name',index:'address_name',width:100,align:'center'},
				   {label:'通知方式',name:'wayName',index:'wayName',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
			rownumbers: true,
			altRows:true,
			altclass:'jqgridRowColor',
			rowNum:100,//一页显示多少条
			rowList:[100,200,300],//可供用户选择一页显示多少条
			sortname:'id',//初始化的时候排序的字段
			sortorder:'desc',//排序方式
			mtype:'post',//向后台请求数据的Ajax类型
			viewrecords:true,//定义是否要显示总记录数
			multiselect: false,//复选框
			caption:"<div style='color:#000;width:95%;'>通知规则<span style='cursor:pointer;float:right;' onclick='noticeAdd()'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
			height:'auto',
			autowidth:true,
			editurl:getContextPath()+"/noticeruleCon/update",//编辑地址
			pager : "#jqGridPaperListNotr",
			gridComplete: function(){
	            var ids = $("#jqGridListNotr").getDataIDs();
	            for(var i=0;i<ids.length;i++){
	                var cl = ids[i];
	                ed = "<a title='编辑' onclick='noticeAdd("+cl+")' style='cursor:pointer;'>编辑 </a>";
	                vi = "<a title='删除' onclick='doDelete("+cl+")' style='cursor:pointer;'>删除</a>"; 
	                jQuery("#jqGridListNotr").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
	            } 
	        },
	});
}

//删除
function doDelete(id){
	var jwt = getJWT();
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/noticeruleCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
				window.parent.doNotrSearch();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListNotr").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
/**
 * 查询
 */
function doNotrSearch(){
	$.jgrid.gridUnload('#jqGridListNotr');
	creatNotrGrid();
}
/**
 * 新增
 */
function noticeAdd(id){
	getJWT();
	layer.open({
  	    type: 2,
  	    title: '通知规则', 
  	    fix: false,
  	    id: "noticeAdds",
  	    shadeClose: false,
//  	    area: ['45%', '60%'],
  	    area: ['600px','400px'],
  	    content: [getContextPath()+"/jsp/pages/configmgt/noticerule/noticeruleadd.jsp?id="+id,'no']
  	});
}
/**
 * 保存前
 */
var fn_beforeSubmit=function(postdata,formid){
/*	var id=postdata["id"];
	var notice_bh=postdata["notice_bh"];
	var mess="";
	var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
	if(notice_bh.match(reg)){
		$.ajax({
	    	type:"post",
	    	url:getContextPath()+"/noticeruleCon/checknoticerule?id="+id+"&notice_bh="+notice_bh,
	    	async:false,
	    	success:function(res){
	    		mess=res;
	    	}
		});
	}else{
		mess="规则编码只能由字母+数字组成！";
	}
    if(mess===""){
    	return [true,""];
	}else{
		return [false,mess];
	}*/
	return [true,""];
}

