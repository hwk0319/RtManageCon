function  creatGrid(){
	$("#jqGrid").jqGrid({
		url: getContextPath()+"/sysParamCon/searchGrid",
		mtype : "post",
		postData : {
			"id":"",
			"code" : $("#CODE_SEARCH").val(),
			"name" : $("#NAME_SEARCH").val()
		},
        datatype: "json",
        colModel: [		
      		{ label: '', name: 'id', width: 0, key: true ,hidden:true},
			{ label: '参数编号', name: 'code', width: 1 },
			{ label: '参数名称', name: 'name', width: 1 },
			{ label: '参数值', name: 'value', width: 1 },
			{ label: '参数单位', name: 'unit', width: 1 },
			{ label: '参数说明', name: 'description', width: 2 }
        ],
		loadonce: true,
		viewrecords: true,
        autowidth: true,
        rownumbers: true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
        height: '100%',
        rowNum: 15,
        rownumbers: true, 
        rownumWidth: 25, 
        multiselect: false,
        reloadAfterSubmit: true,                
        pager: "#jqGridPager"
    }).trigger("reloadGrid");
	//$("#jqGrid").setSelection(1);
}

$(window).resize(function(){ 
	$("#jqGrid").setGridWidth($(window).width()-250);
});


function doSearch() {
	var code= $("#CODE_SEARCH").val();
	var name= $("#NAME_SEARCH").val();
	if (name != "") {
		var exp = /[@'\"$&^*{}<>\\\:\;]+/;
		var reg = name.match(exp);
		if (reg) {
			alert("参数名称请不要输入特殊字符");
			return;
		}
	}
	if(code != ""){
		var numberexp = "[^0-9]";
		if(code.match(numberexp)){
			alert("参数编号请输入数字");
			return;
		}
	}
	$.jgrid.gridUnload('#jqGrid'); 
	creatGrid();
}

function doEdit(){
	if (!isSelectRow("#jqGrid")) return;
	id =getSelectedRowID("#jqGrid");
	
	layer.open({
	    type: 2,
	    title: '编辑',
	    fix: false,
	    shadeClose: false,
	    area: ['700px', '350px'],
	    content: [getContextPath()+'/jsps/config/sysParamEdit.jsp?id='+id,'no']
	});
}