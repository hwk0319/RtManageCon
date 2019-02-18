var set;
var taskId="";
$(function(){
	//页面加载完成之后执行
	initSelectModul() ;
	pageInit();
	clearInterval(set);
	set = setInterval("refreshPage()","12000");
	var oper="";
});

var selectId = null;
function pageInit()
{
	jQuery("#jqGridTaskList").jqGrid(
		      {
		        url : getContextPath()+"/taskmanage/taskList",
		  		postData : {
		  			"modul":$("#taskModul_search").val(),
		  			"name":$("#name_search").val(),
		  			"startTime" : $("#startTime").val(),
		  			"endTime" : $("#endTime").val(),
		  			"needPage":true
				},
				datatype : "json",
		        colModel: [
		                  	{ label: '任务ID', 	name: 'id', width: 0, editable : false,key: true ,hidden:true,align : "center"},
		                  	{ label: '任务名称', name: 'name',editable : true, width: 110,align : "center"},
		                  	{ label: '任务分类', name: 'modul', editable : true,width: 50,align : "center"},
		                  	{ label: '任务类型', name: 'type', editable : false,width: 50,formatter: taskTypeFormat,align : "center"},
		            		{ label: '任务状态', name: 'state', editable : false,width: 50,formatter: taskStateFormat ,align : "center"},
		           			{ label: '最近一次执行时间', name: 'lastTime',editable : false,formatter: lastTimeRunningFormat, width: 100,align : "center"},
		           			{ label: '任务描述', name: 'poDesc',index: 'poDesc',editable : true, width: 200,align : "center"},
		           			{ label: '任务进度', name: 'resultDesc',index: 'resultDesc',editable : false, width: 90,align : "center"},
		           			{label:'操作',name:'act',index:'act',index:'operate',width:150,formatter: operationFormat,align : "center"} 
		            		],
        		rownumbers : true,
        		rowNum:100,//一页显示多少条
        		rowList:[100,200,300],//可供用户选择一页显示多少条
         		autowidth:true,
         		caption:"<div style='width:95%;'>任务列表<span onclick=\"addOrViewTask('add','')\" style='float:right;cursor:pointer;'><i id='add' class='icon-plus-sign'></i>&nbsp;新增</span></div>",
         		height : '100%',
         		loadonce:true,
         		mtype:'post',//向后台请求数据的Ajax类型
         		viewrecords : true,
//         		rownumWidth : 25,
//         		multiselect : false,
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
     			},
         		onSelectRow: function(rowid, status){
         			selectId = rowid;
         		}
         	}).trigger("reloadGrid");
}

function taskTypeFormat(cellvalue, options, rowObject) {
	if (rowObject.type == 1) {
		return '普通任务';
	} else if (rowObject.type == 2) {
		return '定时任务';
	} else if (rowObject.type == 3) {
		return 'ZK任务';
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
		return '执行中';
	} else if (rowObject.state == 3) {
		return '执行成功';
	} else if (rowObject.state == 4) {
		return '执行失败';
	} else if (rowObject.state == 5) {
		return '执行失败';
	} else if (rowObject.state == 6) {
//		return '已停止';
		return '正在停止';
	} else if (rowObject.state == 7) {
		return '已停止';
	} else {
		return 'ERROR'
	}
}

function operationFormat(cellvalue, options, rowObject) {
	var taskId = rowObject.id;
	var edit = "<a href=# title='编辑' onclick=\"updateTask('"+ taskId+ "');\">编辑 </a>";
	var vi = "<a href=# title='详情' onclick=\"addOrViewTask('view','" + taskId + "')\">详情</a>";
	var start = "<a href=# title='执行' onclick=\"startTaskById('" + taskId + "');\">执行</a>";
	var pau = "<a href=# title='停止' onclick=\"stopTaskById('" + taskId + "');\">停止</a>";
	var del = "<a href=# title='删除' onclick=\"delTask('"+ taskId+ "')\">删除</a>";
	var returnStr;
	if (rowObject.type == 1)//普通任务
	{
		returnStr =  vi + " " + " " + pau + " " + del
	} else if (rowObject.type == 2) {
		returnStr =  vi +" " + pau + " " + del
	}
	else if (rowObject.type == 3) {
		returnStr =  vi + " " + pau + " " + del
	}
	//没有执行过的任务才有编辑
	if(rowObject.lastTime ==  null)
	{
		returnStr = returnStr+ " "+edit+" "+start;
	}
	return returnStr;
}

//编辑任务
function updateTask(taskId){
	layer.open({
	    type: 2,
	    title: '任务', 
	    fix: false,
	    shadeClose: false,
	    area: ['460px','300px'],
//	    area: ['35%','40%'],
	    moveOut: true,
	    content: [getContextPath()+"/jsps/taskmanage/updateTask.jsp?taskId="+taskId,'no']
	});
}

//删除任务
function delTask(taskId){
	layer.confirm("确定删除当前任务？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
			url : getContextPath()+"/taskmanage/taskOperation",
			type : "POST",
			datatype : "json",
			data : {
				"id" : taskId,
				"oper" : "del"
			},
			success : function(data) {
				layer.msg("任务已删除！");
				doTaskListSearch();
			},error: function(){
				layer.msg("删除失败！");
				doTaskListSearch();
			}
		});
	});
}

function lastTimeRunningFormat(cellvalue, options, row) {
	if (cellvalue == null) {
		return "";
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
	var GridWidth = (width - 250);
	$("#jqGridTaskList").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});

/**
 * 刷新表格
 */
function refreshPage() {
	$('#jqGridTaskList').trigger("reloadGrid");
}

//启动一个任务
function startTaskById(id) {
	layer.confirm("确定执行当前任务？",{
		btn:['确定','取消']
	},function(){
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
		pageInit();
		layer.closeAll();
		layer.msg("任务已开始执行！");
	});
}

function stopTaskById(id) {
	layer.confirm("确定停止当前任务？",{
		btn:['确定','取消']
	},function(){
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
					layer.alert(data.value)
				}
			}
		});
		layer.closeAll();
		layer.msg("任务已停止！");
		$("#jqGridTaskList").trigger("reloadGrid");
	});
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
function cloneTaskById(id) {
	$.ajax({
		url : getContextPath() + "/taskmanage/cloneTaskByTaskId",
		type : "POST",
		datatype : "json",
		async : false,
		data : {
			"taskId" : id,
		},
		success : function(data) {
			if ('success' != data.status) {
				alert(data.value);
				return;
			}
			else
			{
				taskId = data.value;
				addOrViewTask("clone",taskId);
			}
		}
	});
}

function afterShowForm(formid) {
	var taskId = formid[0].elements["id_g"].value;
	var rowData = $("#jqGridTaskList").jqGrid('getRowData', taskId);
	if (rowData.type == "定时任务") {
		$("#FrmGrid_jqGridTaskList").append($("#cron_iframe").html());
		$("#editmodjqGridTaskList").css({
			width : "620px",
			height : "520px"
		});
		$("#FrmGrid_jqGridTaskList").css({
			width : "620px",
			height : "95%"
		});
		$("#TblGrid_jqGridTaskList").css({
			width : "620px",
			height : "100px"
		});
		$("#TblGrid_jqGridTaskList #cronExpress").css({
			background : "#F0F0F0"
		}).attr("disabled", "true");
	} else {
		$("#TblGrid_jqGridTaskList  #tr_cronExpress").css({
			"display" : 'none'
		});
		$("#editmodjqGridTaskList").css({
			width : "300px",
			height : "200px"
		});
		$("#FrmGrid_jqGridTaskList").css({
			width : "100%",
			height : "95%"
		});
		$("#TblGrid_jqGridTaskList").css({
			width : "100%",
			height : "95%"
		});
		//$("#editmodjqGridTaskList").css({});
	}
}

function addOrViewTask(opera, thisTaskId) {
	$("#shareTaskId").val(thisTaskId);
	$("#taskOperationType").val(opera);
	oper = opera;
	taskId = thisTaskId;
	//查看详情
	if(opera == "view"){
		layer.open({
			title : "任务",
			type : 2,
			id : 'LAY_layuipro',
			area : [ '815px', '480px' ],
//			area : [ '60%', '70%' ],
			yes : function(index, layero) {
				if (oper == "add") {
					saveTask(index, layero);
					$("#jqGridTaskList").trigger("reloadGrid");
					//pageInit();
				}
				else if(oper == "clone")
				{
					$("#jqGridTaskList").trigger("reloadGrid");
				}
//				layer.close(index);
			},
			btn2 : function(index, layero) {
				if (oper == "add" || oper == "clone") {
					cancleTask(index, layero);
					$("#jqGridTaskList").trigger("reloadGrid");
					//pageInit();
				}
			},
			content : [ getContextPath() + "/jsps/taskmanage/task/newTask.jsp" ]
		});
	}else{
		layer.open({
			title : "任务",
			type : 2,
			id : 'LAY_layuipro',
			area : [ '815px', '480px' ],
//			area : [ '60%', '70%' ],
			btn : [ '保存 ', '取消' ],
			yes : function(index, layero) {
				if (oper == "add") {
					var isSave = saveTask(index, layero);
					if(!isSave){
						return;
					}
					$("#jqGridTaskList").trigger("reloadGrid");
					//pageInit();
				}
				else if(oper == "clone")
				{
					$("#jqGridTaskList").trigger("reloadGrid");
				}
				layer.close(index);
			},
			btn2 : function(index, layero) {
				if (oper == "add" || oper == "clone") {
					cancleTask(index, layero);
					$("#jqGridTaskList").trigger("reloadGrid");
					//pageInit();
				}
			},
			content : [ getContextPath() + "/jsps/taskmanage/task/newTask.jsp" ]
		});
	}
}

function saveTask(index, layero) {
	taskId = $("#shareTaskId").val();
	if (!taskId) {
		layer.msg("请输入数据后点击下一步！");
		return false;
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
				layer.alert('保存成功！', function(){
					parent.layer.closeAll();
					doTaskListSearch();
//					$("#jqGridTaskList").trigger("reloadGrid");
				});
			} else {
				layer.alert(data.value);
			}
		},
		error : function(text) {
			layer.alert(text);
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
				parent.layer.closeAll();
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
		data:{"type":"tpl"},
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

function doTaskListSearch()
{
	$.jgrid.gridUnload('#jqGridTaskList');
	pageInit();
}

function testOracelSwitch()
{
	$.ajax({
		url : getContextPath() + "/execTask/switchOracleDg",
		type : "POST",
		data:{
			"primaryId":"33",  //16--72
			"standbyId":"36",
			"doubleId":"9"
			},
		success : function(data) {
		},
		error : function(data) {
		}
	});

}

function startOrStopReturnAIO1()
{
	$.ajax({
		url : getContextPath() + "/execTask/startOrStopReturnAIO",
		type : "POST",
		data:{
			"groupId":"33",  //16--72
			"opera":"start"
		},
		success : function(data) {
		},
		error : function(data) {
		}
	});
}


function startOrStopReturnAIO2()
{
	$.ajax({
		url : getContextPath() + "/execTask/startOrStopReturnAIO",
		type : "POST",
		data:{
			"groupId":"33",  //16--72
			"opera":"stop"
		},
		success : function(data) {
		},
		error : function(data) {
		}
	});
}
function testOracelFailover()
{
	$.ajax({
		url : getContextPath() + "/execTask/failOverOracleDg",
		type : "POST",
		data:{
			"primaryId":"33",  //16--72
			"standbyId":"36",
			"doubleId":"9"
			},
		success : function(data) {
		},
		error : function(data) {
		}
	});
}

