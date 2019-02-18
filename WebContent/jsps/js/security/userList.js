function creatGrid() {
	$("#jqGrid").jqGrid({
		url : getContextPath() + "/usersCon/search",
		mtype : "post",
		postData : {
			"username" :  $("#USERNAME_SEARCH").val()
		},
		datatype : "json",
		colModel : [ {
			label : '',
			name : 'id',
			width : 0,
			key : true,
			hidden : true
		}, {
			label : '用户名',
			name : 'username',
			width:2
		},
		{
			label : '角色',
			name : 'rolename',
			width:1
		}, {
			label : '电子邮箱',
			name : 'email',
			width:2
		}, {
			label : '起始IP',
			name : 'start_ip',
			width:1
		}, {
			label : '结束IP',
			name : 'end_ip',
			width:1
		}, {
			label : '起始时间',
			name : 'start_time',
			width:1
		}, {
			label : '结束时间',
			name : 'end_time',
			width:1
		}, {
			label : '状态',
			name : 'status',
			formatter : enteredKbFmatter,
			width:1
		} ],
		loadonce : true,
		viewrecords : true,
		autowidth:true,
		rownumbers: true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
		height : '100%',
		rowNum : 15,
		rownumbers : true,
		rownumWidth : 25,
		multiselect : false,
		reloadAfterSubmit : true,
		pager : "#jqGridPager"
	}).trigger("reloadGrid");

}

$(window).resize(function(){ 
	$("#jqGrid").setGridWidth($(window).width()-250);
});

function enteredKbFmatter(cellvalue, options, rowObject) {
	if (cellvalue == 1) {
		return "正常";
	}
	if (cellvalue == 0) {
		return "注销";
	}
	if (cellvalue == 2) {
		return "锁定";
	}

}
function initCombox() {
	$("#ROLE_SEARCH").append(SYS_ROLE_OP);
}

function doSearch() {
	var name= $("#USERNAME_SEARCH").val();
	if (name != "") {
		var exp = /[@'\"$&^*{}<>\\\:\;]+/;
		var reg = name.match(exp);
		if (reg) {
			alert("请不要输入特殊字符");
			return;
		}
	}
	$.jgrid.gridUnload('#jqGrid');
	creatGrid();
}

function deleteData(gridName, url, pastdata) {
	var returnvalue = false;
	if (!isSelectRow(gridName))
		return;
	// id =getSelectedRowID(gridName);
	var r = window.confirm("确定要注销么？");
	if (r) {
		$.ajax({
			url : url,
			type : "post",
			async : false,
			data : pastdata,
			dataType : "json",
			success : function(result) {
				// 刷新表格
				if (result > 0) {
					alert("注销成功！");
					returnvalue = true;
				} else {
					alert("执行注销失败");
					returnvalue = true;
				}
			},
			error : function() {
				alert("执行注销失败");
				returnvalue = false;
			}
		});
	}
	return returnvalue;
}
function doDelete() {
	var url = getContextPath() + "/usersCon/delete";
	var id = getSelectedRowID("#jqGrid");
	var postdata = {
		"id" : id,
		"status" : 0
	};
	var rs = deleteData("#jqGrid", url, postdata);
	if (rs) {
		doSearch();
	};
}

function doEdit() {
	if (!isSelectRow("#jqGrid"))
		return;
	id = getSelectedRowID("#jqGrid");

	layer.open({
		type : 2,
		title : '编辑',
		fix : false,
		shadeClose : false,
		area : [ '700px', '280px' ],
		content : [ getContextPath() + '/jsps/security/userEdit.jsp?id=' + id,
				'no' ]
	});
}

function doInsert() {
	layer.open({
		type : 2,
		title : '新增',
		fix : false,
		shadeClose : false,
		area : [ '700px', '320px' ],
		content : [ getContextPath() + '/jsps/security/userInsert.jsp', 'no' ]
	});
}

function doUpPass(){
	if (!isSelectRow("#jqGrid"))
		return;
	id = getSelectedRowID("#jqGrid");

	var gr = $("#jqGrid").getGridParam('selrow');

	var username = $("#jqGrid").getCell(gr, "username");
	layer.open({
		type : 2,
		title : '修改密码',
		fix : false,
		shadeClose : false,
		area : [ '500px', '300px' ],
		content : [
				getContextPath() + '/jsps/security/modifyPass.jsp?id=' + id
						+ '&name=' + username + '&pwd_old=1', 'no' ]
	});
}

function Accredit() {
	if (!isSelectRow("#jqGrid"))
		return;
	id = getSelectedRowID("#jqGrid");

	layer.open({
		type : 2,
		title : '角色授权',
		fix : false,
		shadeClose : false,
		area : [ '400px', '200px' ],
		content : [
				getContextPath()
						+ '/jsps/security/rolemanager/accredit.jsp?id=' + id,
				'no' ]
	});
}

function _doAccredit() {
	if (!isSelectRow("#jqGrid"))
		return;
	id = getSelectedRowID("#jqGrid");
	var rowDatas = $("#jqGrid").jqGrid('getRowData', id);
	var name = rowDatas["username"];

	$.ajax({
		async : false,
		url : getContextPath() + "/usersCon/checkLogin",
		type : "post",
		data : {
			"name" : name
		},
		success : function(result) {
			if (result != "success") {
				alert("用户在线，请勿更改权限");
				return;
			} else {
				Accredit();
			}
		},
		error : function() {
			return;
		}
	});

}

function doLock() {
	var id = getSelectedRowID("#jqGrid");
	var status = "2";
	
	if (!isSelectRow("#jqGrid"))
		return;
	var r = window.confirm("确定锁定用户？");
	if(!r){
		return
	}
	$.ajax({
		url : getContextPath() + "/usersCon/lock",
		type : "post",
		data : {
			"id" : id,
			"status" : status,
		},
		success : function(result) {
			alert("用户已锁定！");
			window.close();
			doSearch();
		},
		error : function() {
			alert("执行锁定失败！");
		}
	});
}

function doUnlock() {
	var id = getSelectedRowID("#jqGrid");
	if (!isSelectRow("#jqGrid")){
		return;
	}
	var rowDatas = $("#jqGrid").jqGrid('getRowData', id);
	var status=rowDatas["status"];
	if(status=="正常"){
		alert("用户为正常状态，无需解锁");
		return;
	}
	var r = window.confirm("确定解锁用户？");
	if(!r){
		return;
	}
	$.ajax({
		url : getContextPath() + "/usersCon/unlock",
		type : "post",
		data : {
			"id" : id,
			"status" : "1",
			"err_num" : "0",
			"firstlogin" : "1"
		},
		success : function(result) {
			alert("用户已解锁！");
			window.close();
			doSearch();
		},
		error : function() {
			alert("执行解锁失败！");
		}
	});
}
