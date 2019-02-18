$(function() {
	initModulSelect();
  // 页面加载完成之后执行
  pageInit();
  var taskId="";
  var oper="";
});

function pageInit() {
	//用于点击其他地方保存正在编辑状态下的行
/*	$('html').bind('click', function(e) { 
		if (lastsel != "") { 
			if ($(e.target).closest('#jqGridTaskTemplateList').length == 0) {
				jQuery('#jqGridTaskTemplateList').jqGrid('saveRow', lastsel);
				jQuery('#jqGridTaskTemplateList').resetSelection();
				lastsel = "";
			}
		}
	});
	var lastsel;*/
	
	
	jQuery("#jqGridTaskTemplateList").jqGrid(
					{
						url : getContextPath() + "/taskmanage/taskTemplateList",
						mtype : "post",
				  		postData : {
				  			"modul":$("#taskModul_search").val(),
				  			"name":$("#name_search").val()
						},
						datatype : "json",
						colModel : [
						             {label : '模板ID',name : 'id',width : 0,key : true,hidden : true}, 
						             {label : '模板名称',name : 'name',editable : true,width : 100,editrules:{required:true}}, 
						             {label : '模板分类',name : 'modul',editable : true,width : 100,editrules:{required:true}},
						             {label : '模板描述',name : 'poDesc',editable : true,width : 100,editrules:{required:true}},
						             {label : '操作',name : 'act',index : 'act',index : 'operate',width : 100,align : 'center'} 
						           ],
						sortable : true,
						sortname : "id",
						viewrecords : true,
						autowidth : true,
						height : '100%',
						rowNum : 15,
						rownumbers : true,
						rownumWidth : 25,
						multiselect : false,
						reloadAfterSubmit : true,
						caption : "<div style='color:#000;width:95%;'>任务模板 <span onclick=\"operWithTaskTpl('add','');\"  style='float:right;color:#000;cursor:pointer;'><i id='add' class='icon-plus-sign'></i>&nbsp;新增模板 </span></div>",
						//loadonce:false,
						//navOptions: { reloadGridOptions: { fromServer: true } },
						pager : "#jqGridTaskTemplatePaperList",
						editurl : getContextPath() + "/taskmanage/taskOperationTemplate",
						/*onSelectRow : function(rowid, status) {
							doInitChildGrid();
						},
						ondblClickRow : function(rowid, iRow, iCol, e) {
							if (rowid && rowid != lastsel) {
								jQuery('#jqGridTaskTemplateList').saveRow(
										lastsel);
								jQuery('#jqGridTaskTemplateList').editRow(
										rowid, true);
								lastsel = rowid;
							} else if (rowid == lastsel) {
								jQuery('#jqGridTaskTemplateList').editRow(
										rowid, true);
							}
						},*/
						gridComplete: function() {
                var ids = jQuery("#jqGridTaskTemplateList").getDataIDs();
                for (var i = 0; i < ids.length; i++) {
                  var cl = ids[i];
                  del = "<a  onclick=\"jQuery('#jqGridTaskTemplateList').jqGrid('delGridRow', '" + cl+ "', {closeOnEscape : true});\" style=\"margin-left:3%;cursor:pointer;\">删除</a>";
                  ed = "<a  onclick=\"jQuery('#jqGridTaskTemplateList').jqGrid('editGridRow', '" + cl+ "', {closeAfterEdit:true,closeOnEscape:true,afterShowForm:afterShowForm});\" style=\"margin-left:3%;cursor:pointer;\">编辑</a>";
                  vi = "<a  onclick=\"operWithTaskTpl('edit','"+cl+"');\" style=\"margin-left:3%;cursor:pointer;\">详情</a>";
                  clone = "<a  onclick=\"cloneTaskTemplate('"+cl+"');\" style=\"margin-left:3%;cursor:pointer;\">克隆</a>";
                  jQuery("#jqGridTaskTemplateList").jqGrid('setRowData', ids[i], {act:vi+" "+ed+" "+ del+" "});
                }
            }
					}).trigger("reloadGrid");
}




// 窗口改变大小时重新设置Grid的宽度
$(window).resize(function() {
	var width = document.body.clientWidth;
	var GridWidth = width - 230 - 20;
	$("#jqGridTaskTemplateList").setGridWidth(GridWidth);
});
 


function operWithTaskTpl(opera,opTaskId){
  oper=opera;
  taskId=opTaskId;
  $("#shareTaskIdTpl").val(taskId);
  layer.open({
    title: "模板",
//    closeBtn:false,
    resize :true,
    type: 2,
    offset: '100px',
    //skin: "layui-layer-lan",
    //area: 'auto',
    id: 'LAY_layuipro' ,
    area: ['60%', '70%'],
    btn: ['保存 ', '取消'],
    yes: function(index, layero) {
      $("#jqGridTaskTemplateList").trigger("reloadGrid");
      layer.close(index);
    },
    btn2: function(index, layero) {
    	if(opera== "add")
    		{
    		
    		if($("#shareTaskIdTpl").val())
    		{
    			removeTaskTplByTaskId($("#shareTaskIdTpl").val());
    		}
    		}
    },
    cancel: function(){ 
    	if(opera== "add")
    		{
	    		if($("#shareTaskIdTpl").val())
	    		{
	    			removeTaskTplByTaskId(taskId);
	    		}
    		}
    },

    content: [getContextPath() + "/jsps/taskmanage/tpl/addNewTaskTPL.jsp"]
  });
}

function removeTaskTplByTaskId(taskId)
{
	 $.ajax({
		    type : 'POST',
		    url : getContextPath() + "/taskmanage/taskOperationTemplate",
		    dataType : 'json',
		    async:false,
		    data : {
		      "id" : taskId,
		      "oper" : "del"
		    },
		    success : function(data) {
		    	if(data.status == 'success')
		    	{
		    		$("#jqGridTaskTemplateList").trigger("reloadGrid");
		    	}
		    	else
		    		{
		    		alert(data.value);
		    		}
		    },
		    error : function(text) {
		      alert(text);
		    }
		  });
}

function initModulSelect()
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

function cloneTaskTemplate(taskId)
{
	 $.ajax({
		    type : 'POST',
		    url : getContextPath() + "/taskmanage/cloneTaskOperationTemplate",
		    dataType : 'json',
		    async:false,
		    data : {
		      "id" : taskId
		    },
		    success : function(data) {
		    	if(data.status == 'success')
		    	{
		    		$("#jqGridTaskTemplateList").trigger("reloadGrid");
		    	}
		    	else
		    		{
		    		alert(data.value);
		    		}
		    },
		    error : function(text) {
		      alert(text);
		    }
		  });
}

/*** 重置 ***/
function doTaskListSearchReset(){
	$(".prom input").val('');
	$("#taskModul_search").val('');
}

function doTaskListSearch()
{
	$.jgrid.gridUnload('#jqGridTaskTemplateList');
	pageInit();
}
