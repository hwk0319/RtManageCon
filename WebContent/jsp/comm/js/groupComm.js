var type="group_type"
var flag = 0;//初始化一个动作，用来表示tab是否展示
function initGrofunction(){
	//页面加载完成之后执行
	creatGroupGrid();
	$("body").on("click","#srch",function(e){
		doSearch();
	});
	$("#tabs").tabs();
	//页签切换处理
	$("#tabs ul").on("click","li",function(){
		if (isSelectRow("#jqGridListGro")) {
			doGroupCong();
		}
	});
}
function creatGroupGrid(){
	jQuery("#jqGridListGro").jqGrid({
		url:getContextPath()+"/groupCon/search",
		datatype:'json',
		postData:{
//				  'name': $("#name_search").val(),
				  'grotypeName': $("#name_search").val(),
				  'grotype': $("#grotype").val(),
				  'uid': $("#uid_search").val(),
				  'ids' : $("#ids",parent.document).val()
				  },
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true,
					   	editable:true,//可编辑
				   },
				   {label:'统一ID',name:'uid',index:'uid',width:80,align:'center',
					   	editable:false,//可编辑
	                    edittype:'text',//类型 文本框
	                    editoptions:{size:20,maxlength:64},
				   },
				   
				   {label:'名称',name:'name',index:'name',width:80,align:'center',
					   	editable:true,//可编辑
	                    edittype:'text',//类型 文本框
	                    editoptions:{size:20,maxlength:100},
	                    editrules:{required:true},
	                    formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
				   },
				   {label:'具体类型',name:'grotype',index:'grotype',width:80,align:'center',
	                    formatter:function(cellvalue, options, rowObject){
	                    	var indexname;
		                   	 switch(cellvalue){
		                   		case 1:indexname='主从';break;
		                   		case 2:indexname='并列';break;
		                   		}
		                   	 return indexname;
	                    }
	                    
				   },
				   {label:'描述',name:'description',index:'description',width:90,align:'center',
					    editable:true,
	                    edittype:'textarea',
	                    editoptions:{size:20,maxlength:255},
				   },
			
				   
				],
		rownumbers: true,
//		rowNum:10,//一页显示多少条
//		rowList:[10,20,30],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: true,//复选框
		height:'210',
		autowidth:true,
		pager : "#jqGridPaperListGro",
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListGro").setGridWidth(GridWidth);
});
/**
 * 查询
 */
function doSearch(){
	$.jgrid.gridUnload('#jqGridListGro');
	creatGroupGrid();
}


