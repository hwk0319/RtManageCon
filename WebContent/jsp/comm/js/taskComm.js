/**
 * 
 */
var set;
var taskId="";
$(function(){
	//页面加载完成之后执行
	initSelectModul() ;
	pageInit();
	clearInterval(set);
	set = setInterval("refreshPage()","12000");
	$(".ui-jqgrid-bdiv").css({overflow:"hidden"});
  var oper="";

});

var selectId = null;
function pageInit()
{
	jQuery("#jqGridTaskList").jqGrid(
		      {
		        url : getContextPath()+"/taskmanage/taskList",
		    	mtype : "post",
		  		postData : {
		  			"modul":$("#taskModul_search").val(),
		  			"name":$("#name_search").val(),
		  			"startTime" : $("#startTime").val(),
		  			"endTime" : $("#endTime").val()
				},
				datatype : "json",
		          colModel: [
		                  	{ label: '任务ID', 	name: 'id', width: 0, editable : false,key: true ,hidden:true},
		                  	{ label: '任务名称', name: 'name',editable : true, width: 100},
		                  	{ label: '任务分类', name: 'modul', editable : false,width: 100},
		                  	{ label: '任务类型', name: 'type', editable : false,width: 100,formatter: taskTypeFormat},
		            		{ label: '任务状态', name: 'state', editable : false,width: 100,formatter: taskStateFormat ,align : "center"},
		           			{ label: '创建时间', name: 'createTime',editable : false,formatter:function(cellvalue, options, row){return new Date(cellvalue).toLocaleString()}, width: 100 },
		           			{ label: '最近一次执行时间', name: 'lastTime',editable : false,formatter: lastTimeRunningFormat, width: 100},
		           			{ label: '任务进度', name: 'percent',index: 'percent',editable : false, hidden:true,width: 0},
		           			{ label: '任务进度', name: 'percentShow',index: 'percentShow',editable : false,formatter: taskPercentFormat, width: 200},
		           			{ label: '任务描述', name: 'desc',index: 'desc',editable : false, width: 100},
		            		],
         		autowidth:true,
         		//caption:"<div style=''>任务列表<a  href='javascript:void(0);' onclick=\"addOrViewTask('add','')\"  style='text-decoration : none;margin-left:85%;'><i id='add' class='icon-plus-sign'></i>&nbsp;新增任务</a></div>",
         		height : '100%',
         		rowNum : 20,
         		rownumbers : true,
         		rownumWidth : 25,
         		multiselect : true,
         		reloadAfterSubmit : true,
         		editurl : getContextPath()+"/taskmanage/taskOperation",
         		pager : "#jqGridTaskPaperList",
         			gridComplete: function(){
                        var ids = $("#jqGridTaskList").getDataIDs();
                        var rowDate = $("#jqGridTaskList").getRowData();
                        for(var i=0;i<ids.length;i++){
                            var cl = ids[i];
                            var percent = rowDate[i].percent;
                            $("#"+cl+"_percent").myProgress({speed: 0, percent: percent,width: "99%"});
                        }
         			}
         		
         	}).trigger("reloadGrid");
}


function taskTypeFormat(cellvalue, options, rowObject) {
	if (rowObject.type == 1) {
		return '普通任务'
	} else if (rowObject.type == 2) {
		return '定时任务'
	} else {

		return ''
	}
}

function taskStateFormat(cellvalue, options, rowObject) {
	if (rowObject.state == 0) {
		return '创建失败';
	} else if (rowObject.state == 1) {
		return '等待执行 ';
	} else if (rowObject.state == 2) {
		return '执行中'
	} else if (rowObject.state == 3) {
		return '执行成功'
	} else if (rowObject.state == 4) {
		return '执行失败'
	} else if (rowObject.state == 5) {
		return '执行失败'
	} else if (rowObject.state == 6) {
		return '已停止'
//		return '正在停止'
	} else if (rowObject.state == 7) {
		return '已停止'
	} else {
		return 'ERROR'
	}
}

function operationFormat(cellvalue, options, rowObject) {
	var taskId = rowObject.id;
	var del = "<a href=# title='删除' onclick=\"jQuery('#jqGridTaskList').jqGrid('delGridRow', '"
			+ taskId
			+ "', {closeOnEscape : true,afterComplete : function (response, postdata, formid) {refreshPage ()}});\">删除</a>";
	var vi = "<a href=# title='详情' onclick=\"addOrViewTask('view','" + taskId
			+ "')\">详情</a>";
	var start = "<a href=# title='执行' onclick=\"startTaskById('" + taskId
			+ "');\">执行</a>";
	var startNow = "<a href=# title='触发' onclick=\"startTaskById('" + taskId
			+ "');\">触发</a>";
	var pau = "<a href=# title='停止' onclick=\"stopTaskById('" + taskId
			+ "');\">停止</a>";
	var resu = "<a href=# title='恢复' onclick=\"resumeTaskById('" + taskId
			+ "');\">恢复</a>";
	if (rowObject.type == 1)//普通任务
	{
		return vi + " " + start + " " + pau + " " + del
	} else if (rowObject.type == 2) {
		return vi + " " + startNow + " " + pau + " " + resu + " " + del
	}
}

function lastTimeRunningFormat(cellvalue, options, row) {
	if (cellvalue == null) {
		return "null";
	} else {
		return new Date(cellvalue).toLocaleString();
	}

}

function taskPercentFormat(cellvalue, options, row) {
	return "<div class=\"progress-out\" id=\""
			+ row.id
			+ "_percent\"><div class=\"percent-show\"><span>0</span>%</div><div class=\"progress-in\"></div></div>"
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function() {
	var width = document.body.clientWidth;
	var GridWidth = width - 230 - 20;
	$("#jqGridTaskList").setGridWidth(GridWidth);
});

/**
 * 刷新表格
 */
function refreshPage() {
	$('#jqGridTaskList').trigger("reloadGrid");
}

//启动一个任务
function startTaskById(id) {
	$.ajax({
		url : getContextPath() + "/taskmanage/startTaskById",
		type : "POST",
		datatype : "json",
		async : false,
		data : {
			"id" : id,
		},
		success : function(data) {
			if ('success' != data.status) {
				alert(data.value)
			}
		}
	});
	//$("#jqGridTaskList").trigger("reloadGrid");
	pageInit();
}

function stopTaskById(id) {
	$.ajax({
		url : getContextPath() + "/taskmanage/stopTaskById",
		type : "POST",
		datatype : "json",
		async : false,
		data : {
			"id" : id,
		},
		success : function(data) {
			if ('success' != data.status) {
				alert(data.value)
			}
		}
	});
	$("#jqGridTaskList").trigger("reloadGrid");
}

function resumeTaskById(id) {
	$.ajax({
		url : getContextPath() + "/taskmanage/resumeTaskById",
		type : "POST",
		datatype : "json",
		async : false,
		data : {
			"id" : id,
		},
		success : function(data) {
			if ('success' != data.status) {
				alert(data.value)
			}
		}
	});
	$("#jqGridTaskList").trigger("reloadGrid");
}

function addOrViewTask(opera, thisTaskId) {
	$("#shareTaskId").val(thisTaskId);
	$("#taskOperationType").val(opera);
	oper = opera;
	taskId = thisTaskId;
	layer.open({
		title : false,
		closeBtn : false,
		scrollbar : false,
		type : 2,
		id : 'LAY_layuipro',
		//skin: "layui-layer-lan",
		//area: 'auto',
		area : [ '50%', '60%' ],
		btn : [ '保存 ', '取消' ],
		yes : function(index, layero) {
			if (oper == "add") {
				saveTask(index, layero);
				$("#jqGridTaskList").trigger("reloadGrid");
			}
			layer.close(index);
		},
		btn2 : function(index, layero) {
			if (oper == "add") {
				cancleTask(index, layero);
				$("#jqGridTaskList").trigger("reloadGrid");
			}
		},
		cancel : function() {
			//右上角关闭回调
			if (oper == "add") {
				cancleTask(index, layero);
				$("#jqGridTaskList").trigger("reloadGrid");
			}
			//return false 开启该代码可禁止点击该按钮关闭
		},

		content : [ getContextPath() + "/jsps/taskmanage/task/newTask.jsp" ]
	});
}

function saveTask(index, layero) {
	//	 var body = layer.getChildFrame('body', index);
	//     var iframeWin = window[layero.find('iframe')[0]['name']]; 
	//     taskId=iframeWin.taskId;
	taskId = $("#shareTaskId").val();
	if (!taskId) {
		return;
	}
	$.ajax({
		url : getContextPath() + "/taskmanage/saveTaskFromCreating",
		type : "POST",
		datatype : "json",
		async : false,
		data : {
			"id" : taskId,
		},
		success : function(data) {
			if (data.status == 'success') {
				parent.layer.closeAll()
			} else {
				alert(data.value);
			}
		},
		error : function(text) {
			alert(text);
		}
	});

}

function cancleTask(index, layero) {
	/*	var body = layer.getChildFrame('body', index);
	 var iframeWin = window[layero.find('iframe')[0]['name']]; 
	 taskId=iframeWin.taskId;*/
	taskId = $("#shareTaskId").val();
	if (!taskId) {
		return;
	}
	$.ajax({
		url : getContextPath() + "/taskmanage/taskOperation",
		type : "POST",
		datatype : "json",
		async : false,
		data : {
			"id" : taskId,
			"oper" : "del"
		},
		success : function(data) {
			if (data.status == 'success') {
				parent.layer.closeAll()
			} else {
				alert(data.value);
			}
		}
	});
}




//查询模块
function initSelectModul() 
{
	$.ajax({
		url : getContextPath() + "/taskmanage/taskModulList",
		type : "POST",
		data:{"type":"task"},
		success : function(data) {
			//var jsonObj = eval ("(" + data + ")");
			$.each(data, function(i, item) {
				jQuery("#taskModul_search").append("<option value=" + item + ">" + item + "</option>");
			});
		},
		error : function(text) {
			alert(text);
		}
	});
}
function doSave() {
  	//获取勾选的数据
  	//获取选中行的id 
  	var ids=$('#jqGridTaskList').jqGrid('getGridParam','selarrrow');
  	if(ids.length>1){
  		alert("请选择一条任务！");
  		return;
  	}
  	$(ids).each(function(index,id){
		rowData = $("#jqGridTaskList").jqGrid('getRowData',id);
  	});
  	$("#task_name",parent.document).val(rowData.name);
	$("#task_id",parent.document).val(ids);
	//隐藏表单弹窗
	parent.layer.closeAll();
}

/*** 重置 ***/
function doTaskListSearchReset(){
	$(".prom input").val('');
	$("#taskModul_search").val('');
}

function doTaskListSearch()
{
	$.jgrid.gridUnload('#jqGridTaskList');
	pageInit();
}

