function initAddressFun(){
	createAddressGrid();
	$("#srch").click(doSearch);
}
function createAddressGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListAddress").jqGrid({
		url:getContextPath()+"/addressCon/search",
		datatype:'json',//请求数据返回的类型
		postData : {
			'name' : $("#name_search").val(),
			'phone' : $("#phone_search").val()
		},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'姓名',name:'name',index:'name',width:30,align:'center'},
				   {label:'手机号',name:'phone',index:'phone',width:30,align:'center'},
				   {label:'邮箱',name:'email',index:'email',width:30,align:'center'},
				   {label:'地址',name:'address',index:'address',width:30,align:'center'}
				],
		loadonce:true,
		rownumbers: true,
//		rowNum:10,//一页显示多少条
		//rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: true,//复选框
//		caption:"<div style='color:#000;'>通讯录<span id='add' style='cursor:pointer;' onclick='AddressAdd()'> <i class='icon-plus-sign' style='margin-left:85%;'></i>&nbsp;新增</span></div>",
		height:'180px',
		autowidth:true,
//		editurl:getContextPath()+"/addressCon/update",//编辑地址
		pager: "#jqGridPagerListAddress", 
		gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#jqGridListAddress").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			}
	});
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = width-230-20;
	$("#jqGridListAddress").setGridWidth(GridWidth);
});
//重新加载页面
function doSearch(){
	$.jgrid.gridUnload('#jqGridListAddress');
	createAddressGrid();
}
//新增
function AddressAdd(id){
	getJWT();
    layer.open({
        type: 2,
        title: '联系人',
        fix: false,
        shadeClose: false,
        moveOut: true,
        area: ['75%', '80%'],
        content: [getContextPath()+"/jsp/pages/address/addressAdd.jsp?id="+id,'no']
    });
}
function doSave() {
  	//获取勾选的数据
  	//获取选中行的id 
	var rowData;
  	var ids=$('#jqGridListAddress').jqGrid('getGridParam','selarrrow');
  	if(ids.length>1){
  		layer.msg("请选择一位联系人！");
  		return;
  	}
  	if(ids.length == 0){
  		layer.msg("请选择一位联系人！");
  		return;
  	}
  	
  	$(ids).each(function(index,id){
		rowData = $("#jqGridListAddress").jqGrid('getRowData',id);
  	});

  	$("#contactname",parent.document).val(rowData.name);
  	$("#nameId",parent.document).val(ids[0])
  	
  	//隐藏表单弹窗
	parent.layer.close(parent.layer.index);
}
