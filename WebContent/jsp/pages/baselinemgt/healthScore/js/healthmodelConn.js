function ModelFunction(){
	creatModelGrid();
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-230-20)*0.96;
	$("#jqGridListModel").setGridWidth(GridWidth);
});
//查询
function doSearchMod(){
	$.jgrid.gridUnload('#jqGridListModel');
	creatModelGrid();
}
function creatModelGrid(){
	jQuery("#jqGridListModel").jqGrid({
		url:getContextPath()+"/heathcheckCon/searchModel",
		datatype:'json',
		postData:{'model_name': $("#model_name").val()},
		colModel:[
				   {label:'model_id',name:'model_id',width:0,key:true,hidden:true},
				   {label:'模型名称',name:'model_name',index:'model_name',width:100,align:'center'},
				   {label:'总分',name:'total_score',index:'total_score',width:100,align:'center'},
				   {label:'描述',name:'model_desc',index:'model_desc',width:200,align:'center'}
				],
			rownumbers: true,
//			rowNum:100,//一页显示多少条
//			rowList:[100,200,300],//可供用户选择一页显示多少条
			sortname:'id',//初始化的时候排序的字段
			sortorder:'desc',//排序方式
			mtype:'post',//向后台请求数据的Ajax类型
			viewrecords:true,//定义是否要显示总记录数
			multiselect: true,//复选框
			height:'190',
			autowidth:true,
			pager : "#jqGridPaperListModel",
	});
}

//保存
function doModelSave() {
	//获取选中行的id
	var ids=$('#jqGridListModel').jqGrid('getGridParam','selarrrow');
	if(ids.length!=1){
		layer.msg("请选择一条数据！");
		return false;
	}
	var rowData = $("#jqGridListModel").jqGrid('getRowData',ids);
	var id = rowData.model_id;
	var name = rowData.model_name;
	$("#model_id",parent.document).val(id);
	$("#model_name",parent.document).val(name);
	//隐藏表单弹窗
	parent.layer.close(parent.layer.index);
  }
