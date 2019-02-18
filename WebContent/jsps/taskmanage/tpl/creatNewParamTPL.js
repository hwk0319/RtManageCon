$(function(){
	taskId = $("#shareTaskIdTpl",parent.document).val();
  createTaskParamTemplateGrid(taskId);
})

/**
 * 创建模板对应的参数表格。
 * 
 * @param id
 */
function createTaskParamTemplateGrid(taskId) {
  $('html').bind('click', function(e) { //用于点击其他地方保存正在编辑状态下的行
    if (lastsel2 != "") { //if a row is selected for edit 
      if ($(e.target).closest('#jqGridTaskParamTemplate').length == 0) { //and the click is outside of the grid //save the row being edited and unselect the row  
        jQuery('#jqGridTaskParamTemplate').jqGrid('saveRow', lastsel2);
        jQuery('#jqGridTaskParamTemplate').resetSelection();
        lastsel2 = "";
      }
    }
  });
  var lastsel2;
  $("#jqGridTaskParamTemplate").jqGrid(
          {
            url : getContextPath() + "/taskmanage/getTaskParamTemplate",
            mtype : 'POST',
            postData : {
              "taskId" : taskId
            },
            datatype : "json",
            colModel : 
              [ {label : 'id',name : 'id',index : 'id',key : true,editable : false,width : 0,hidden : true},
                {label : 'taskId',name : 'taskId',index : 'taskId',width : 0,hidden : true},
                {label : '参数项',name : 'name',index : 'name',editable : true,width : 210,editrules:{required:true}}, 
                {label : '默认值',name : 'value',index : 'value',editable : true,editrules:{required:true},width : 215}, 
                {label : '参数描述',name : 'poDesc',index : 'poDesc',editable : true,editrules:{required:true},width : 200}, 
                {label : '操作',name : 'act',index : 'act',index : 'operate',width : 130,align : 'center'} 
               ],
            sortable : true,
            sortname : "id",
            caption : "<div style='color:#151414;'>参数模板<a  href='javascript:void(0);' onclick='addNewTaskParamTemplate()'  style='text-decoration : none;color:#151414;'><i id='add' class='icon-plus-sign' style='margin-left:20%;'></i>&nbsp;新增参数</a>  <a  href='javascript:void(0);' onclick='selectFile(\"sqlFile\")'  style='text-decoration : none;color:#151414;'> <i id='upload' class='icon-upload'  style='margin-left:5%;'></i>&nbsp;上传SQL文件</a> <input id=\"sqlFile\" type=\"file\" name=\"file\" onchange='uploadSqlFile(\"sqlFile\")' enctype=\"multipart/form-data\" hidden=\"hidden\">  <a  href='javascript:void(0);' onclick='selectFile(\"iniFile\")'  style='text-decoration : none;color:#151414;'> <i id='upload' class='icon-upload'  style='margin-left:5%;'></i>&nbsp;上传参数文件</a> <input id=\"iniFile\" type=\"file\" name=\"file\" onchange='uploadParamFile(\"iniFile\")' enctype=\"multipart/form-data\" hidden=\"hidden\"> </div>",
            viewrecords : true,
            height : 'auto',
            autowidth : true,
            rowNum : 20,
            rownumbers : true,
            rownumWidth : 25,
            multiselect : false,
            reloadAfterSubmit : true,
            editurl : getContextPath() + "/taskmanage/taskParamTemplate",
            //loadonce:true,
            //navOptions: { reloadGridOptions: { fromServer: true } },
            pager : "#jqGridTaskParamTemplatePager",
            ondblClickRow : function(rowid, iRow, iCol, e) {
              if (rowid && rowid != lastsel2) {
                jQuery('#jqGridTaskParamTemplate').saveRow(
                    lastsel2);
                jQuery('#jqGridTaskParamTemplate').editRow(
                    rowid, true);
                lastsel2 = rowid;
              } else if (rowid == lastsel2) {
                jQuery('#jqGridTaskParamTemplate').editRow(
                    rowid, true);
              }
            },
             gridComplete: function() {
                  var ids = $("#jqGridTaskParamTemplate").getDataIDs();
                  for (var i = 0; i < ids.length; i++) {
                    var cl = ids[i];
                    vi = "<a  onclick=\"jQuery('#jqGridTaskParamTemplate').jqGrid('delGridRow', '" + cl
                            + "', {closeOnEscape : true});\" style=\"margin-left:3%;cursor:pointer;\">删除</a>"
                    jQuery("#jqGridTaskParamTemplate").jqGrid('setRowData', ids[i], {act: vi});
                  }
             }
          }).trigger("reloadGrid");
  
}


function addNewTaskParamTemplate() {

  jQuery("#jqGridTaskParamTemplate").jqGrid('editGridRow', "new", {
    height : 160,
    width : 300,
    closeAfterAdd : true,
    reloadAfterSubmit : true,
    editData : {
    	"id":0,
      "taskId" : taskId
    }
  });
  $('jqGridTaskParamTemplate').trigger("reloadGrid");
}

function selectFile(id) {
  $('#' + id).click();
}

function uploadParamFile(id) {
  var fileStr = $('#' + id).val();
  if (null == fileStr || fileStr == "") {
    alert("请先选择要上传的脚本文件！");
    return;
  } else if (fileStr.toLowerCase().substring(fileStr.length - 4,
      fileStr.length) != ".ini") {
    alert("Error,无法解析的文件，文件必须以'.ini'结尾！");
    return;
  }

  $.ajaxFileUpload({
    url : getContextPath() + '/taskmanage/fileUpload', //用于文件上传的服务器端请求地址
    type : 'post',
    data : {
      "upLoadType" : "paramTemplate",
      "id" : taskId,
      "fileType":"ini"
    },
    secureuri : false, //是否需要安全协议，一般设置为false
    fileElementId : id, //文件上传域的ID
    dataType : 'json', //返回值类型 一般设置为json
    success : function(data, status) //服务器成功响应处理函数
    {
    	if(data.status == "success")
    		{
    			alert("脚本文件上传成功。");
    		}
    	else
    		{
    		alert(data.value);
    		
    		}
    	$.jgrid.gridUnload('#jqGridTaskParamTemplate');
    	createTaskParamTemplateGrid(taskId);
    }
  })
}



function uploadSqlFile(id) {
	  var fileStr = $('#' + id).val();
	  if (null == fileStr || fileStr == "") {
	    alert("请先选择要上传的脚本文件！");
	    return;
	  } else if (fileStr.toLowerCase().substring(fileStr.length - 4,fileStr.length) != ".sql") {
	    alert("Error,无法解析的文件，文件必须以'.sql'结尾！");
	    return;
	  }

	  $.ajaxFileUpload({
	    url : getContextPath() + '/taskmanage/fileUpload', //用于文件上传的服务器端请求地址
	    type : 'post',
	    data : {
	      "upLoadType" : "paramTemplate",
	      "id" : taskId,
	      "fileType":"sql"
	    },
	    secureuri : false, //是否需要安全协议，一般设置为false
	    fileElementId : id, //文件上传域的ID
	    dataType : 'json', //返回值类型 一般设置为json
	    success : function(data, status) //服务器成功响应处理函数
	    {
	    	if(data.status == "success")
	    		{
	    		$.jgrid.gridUnload('#jqGridTaskParamTemplate');
	    		createTaskParamTemplateGrid(taskId);
	    		}
	    	else
	    		{
	    		alert(data.value);
	    		
	    		}
	      //$('jqGridTaskParamTemplate').trigger("reloadGrid");
	    }
	  })
	}


function validateParamTpl(value,name)
{
 var result = "";
 //var ids = $("jqGridTaskStepTemplate").jqGrid('getGridParam','selarrrow');
 $.ajax({
   type: 'POST',
   url: getContextPath() + "/taskmanage/taskParamTemplate",
   dataType: 'json',
   async: false,//设置为同步
   data: {
     "taskId": taskId,
     "oper": "checkParam",
     "name":value
   },
   success: function(data) {
	   if(data.status=="success")
	   		{
		   result = "success";
	   		}
	   else
		   {
		   result = data.value;
		   }
   }
 });
 if(result != "success")
   {
   creatTaskStepTemplateGrid(taskId);
   return [false,result];
   }
 else
   {
   return [true,""];
   }
}