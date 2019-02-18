function loadSelect() {
	var code_type = "oprt_type";
	var aa;
	$.ajax({
		url : getContextPath() + "/systemMan/findsyscodevalue",
		type : "post",
		dataType : "json",
		async : false,
		data : {
			"code_type" : code_type
		},
		success : function(data) {
			aa = data;
			$("#oprt_type").append("<option value=''>全部</option>");
			for (var i = 0; i < aa.length; i++) {
				$("#oprt_type").append(
						"<option value='" + aa[i].code + "'>" + aa[i].name
								+ "</option>");
			}
		}
	});
}

function creatGrid() {
	$("#jqGrid").jqGrid({
		url : getContextPath() + "/oprtLogCon/searchbytype",
		mtype : "post",
		postData : {
			"oprt_user" : $("#oprt_user_id").val(),
			"oprt_type" : $("#oprt_type").val(),
			"oprt_time_beg" : $("#startTime").val(),
			"oprt_time_end" : $("#endTime").val()
		},
		datatype : "json",
		colModel : [ {
			label : '',
			name : 'id',
			width : 0,
			key : true,
			hidden : true
		}, {
			label : '模块',
			name : 'module',
			width : 2
		}, {
			label : '操作类型',
			name : 'oprt_type',
			width : 1,
			formatter : oprt_typeformatter,
		}, {
			label : '操作时间',
			name : 'oprt_time',
			width : 2
		}, {
			label : '操作人',
			name : 'oprt_user',
			width : 1
		}, {
			label : 'IP',
			name : 'ip',
			width : 2
		}, {
			label : '操作内容',
			name : 'oprt_content',
			width : 3
		} ],
		loadonce : true,
		viewrecords : true,
		autowidth:true,
		height:'auto',
		rowNum : 15,
		rownumbers : true,
		rownumWidth : 25,
		multiselect : false,
		reloadAfterSubmit : true,
		pager : "#jqGridPager"
	}).trigger("reloadGrid");
	
	

}

function oprt_typeformatter(cellvalue, options, rowObject) {

	if (cellvalue == "1") {
		return "企图越权";
	} else if (cellvalue == "2") {
		return "增加";
	} else if (cellvalue == "3") {
		return "删除";
	} else if (cellvalue == "4") {
		return "修改";
	} else if (cellvalue == "5") {
		return "登录";
	} else if (cellvalue == "6") {
		return "注销";
	} else if (cellvalue == "7") {
		return "参数配置";
	} else if (cellvalue == "8") {
		return "用户管理";
	} else if (cellvalue == "9") {
		return "查询";
	}

}

function initCombox() {
	$("#MODULE_SEARCH").append(SYS_MODULE_OP);
	$("#OPRT_TYPE_SEARCH").append(OPRT_TYPE_OP);
}

function doSearch() {
	//校验操作人输入框不能含有特殊字符
	var operateUser = $("#oprt_user_id").val().trim();
	if(operateUser.length != 0){
		var exp = /[@'\"$&^*{}<>\\\:\;]+/;
		var reg = operateUser.match(exp);
		if (reg) {
			alert("操作人不能含有特殊字符");
			return;
		}
	}
	$.jgrid.gridUnload('#jqGrid');
	creatGrid();
}
