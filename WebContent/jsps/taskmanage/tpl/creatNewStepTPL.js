$(function(){
	taskId = $("#shareTaskIdTpl",parent.document).val();
  creatTaskStepTemplateGrid(taskId);
})

function creatTaskStepTemplateGrid(taskId) {
  $('html').bind('click', function(e) { // 用于点击其他地方保存正在编辑状态下的行
    if (lastsel1 != "") {
      if ($(e.target).closest('#jqGridTaskStepTemplate').length == 0) { 
        jQuery('#jqGridTaskStepTemplate').jqGrid('saveRow', lastsel1);
        jQuery('#jqGridTaskStepTemplate').resetSelection();
        lastsel1 = "";
      }
    }
  });
  var lastsel1;
  jQuery("#jqGridTaskStepTemplate")
      .jqGrid(
          {
            url : getContextPath()+ "/taskmanage/getTaskStepTemplate", 
            mtype : 'POST',
            postData : {
              "taskId" : taskId
            },
            datatype : "json",
            colModel : 
              [ 
                {label : 'id',         name : 'id',index : 'id',key : true,editable : false,width : 0,hidden : true}, 
                {label : '模板Id',     name : 'taskId',index : 'taskId',editable : false,width : 0,hidden : true},
                {label : '步骤名称',   name : 'name',index : 'name',editable : true,width : 80,align : 'center',editrules : {required : true}}, 
                {label : '步骤号',     name : 'stepOrder',index : 'stepOrder',editable : true,width : 50,align : 'center',editrules : {custom:true, custom_func:validateStepOrder}}, 
                {label : '脚本名称',   name : 'shellName',index : 'shellName',editable : false,width : 140,align : 'left',editrules : {required : true},formatter : shellNameFormat}, 
                {label : '脚本内容',   name : 'shellTxt',index : 'shellTxt',editable : false,width : 0,hidden : true}, 
                {label : '超时时间(s)',name : 'timeOut',index : 'timeOut',editable : true,width : 60,align : 'center',editrules : {required : true}}, 
                {label : '步骤说明',   name : 'poDesc',index : 'poDesc',editable : true,width : 100,align : 'center',editrules : {required : true}}, 
                {label : '操作',       name : 'act',index : 'act',index : 'operate',width : 70,align : 'center'}
            ],
            sortable : true,
            sortname : 'id',
            viewrecords : true,
            autowidth : true,
            height : '100%',
            rowNum : 20,
            rownumbers : true,
            rownumWidth : 25,
            multiselect : false,
            reloadAfterSubmit : true,
            editurl : getContextPath()+ "/taskmanage/taskStepTemplate",
            caption : "<div style='color:#151414;'>步骤模板 <a  href='javascript:void(0);' onclick='addNewTaskStepTemplate()'  style='text-decoration : none;color:#151414;'> <i id='add' class='icon-plus-sign' style='margin-left:70%;'></i>&nbsp;新增步骤</a></div>",
            pager : "#jqGridTaskStepTemplatePager",
            ondblClickRow : function(rowid, iRow, iCol, e) {
              if (rowid && rowid != lastsel1) {
                jQuery('#jqGridTaskStepTemplate').saveRow(lastsel1);
                lastsel1 = rowid;
                jQuery('#jqGridTaskStepTemplate').editRow(rowid, true);
              } else if (rowid == lastsel1) {
                jQuery('#jqGridTaskStepTemplate').editRow(rowid, true);
              }
            },
            gridComplete : function() {
              var ids = $("#jqGridTaskStepTemplate").getDataIDs();
              for (var i = 0; i < ids.length; i++) {
                var cl = ids[i];
                vi = "<a  onclick=\"jQuery('#jqGridTaskStepTemplate').jqGrid('delGridRow', '"+ cl+ "', {closeOnEscape : true});\" style=\"margin-left:3%;cursor:pointer;\">删除</a>"
                jQuery("#jqGridTaskStepTemplate").jqGrid('setRowData', ids[i], {act : vi});
              }
            }
          }).trigger("reloadGrid");
}




/**
* 校验步骤号。步骤号不能重复
* @param value
* @param name
* @returns {Array}
*/
function validateStepOrder(value,name)
{
 var reg = new RegExp("^[0-9]*$");
 if(!reg.test(value))
   {
   creatTaskStepTemplateGrid(taskId);
   return [false,"步骤号只能为数字！"];
   }
 return [true,""];
 /*var result = "";
 //var ids = $("jqGridTaskStepTemplate").jqGrid('getGridParam','selarrrow');
 var id=$('#jqGridTaskStepTemplate').jqGrid('getGridParam','selrow');
 $.ajax({
   type: 'POST',
   url: getContextPath() + "/taskmanage/taskStepTemplate",
   dataType: 'json',
   async: false,//设置为同步
   data: {
     "taskId": taskId,
     "oper": "checkStepOrder",
     "stepOrder":value
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
   }*/
}


function addNewTaskStepTemplate() {
  jQuery("#jqGridTaskStepTemplate").jqGrid('editGridRow', "new", {
    height : 210,
    width : 300,
    reloadAfterSubmit : true,
    closeAfterAdd : true,
    editData : {
    	"id":0,
      "taskId" : taskId
    }
  });
  creatTaskStepTemplateGrid(taskId);
}


function selectFile(id) {
  $('#' + id).click();
}

function uploadShellFile(id) {
  var fileStr = $('#' + id).val();
  if (null == fileStr || fileStr == "") {
    alert("请先选择要上传的脚本文件！");
    return;
  } else if (fileStr.toLowerCase().substring(fileStr.length - 3,
      fileStr.length) != ".sh") {
    alert("Error,无法解析的文件，文件必须以'.sh'结尾！");
    return;
  }
  //这里传入的id因为添加了’sFile‘,所以往后台发送时要把是’sFile‘去掉。
  var idNum = id.substring(0, id.length - 5);
  $.ajaxFileUpload({
    url : getContextPath() + '/taskmanage/fileUpload', //用于文件上传的服务器端请求地址?upLoadType=shell&id='+id
    type : 'post',
    data : {
      "upLoadType" : "shellTemplate",
      "id" : idNum,
      "fileType":"sh"
    },
    secureuri : false, //是否需要安全协议，一般设置为false
    fileElementId : id, //文件上传域的ID
    dataType : 'json', //返回值类型 一般设置为json
    success : function(data, status) //服务器成功响应处理函数
    {
      if(data.status=="success")
        {
        $.jgrid.gridUnload('#jqGridTaskStepTemplate');
        creatTaskStepTemplateGrid(taskId);
        }
      else
        {
        alert(data.value);
        }
    },
    error : function(data, status, e)//服务器响应失败处理函数
    {
      alert(data);
    }
  })
  return false;
}

function editShellTxt(id) {
  var rowDatas = $("#jqGridTaskStepTemplate").getRowData(id);
  var shellTxt = rowDatas.shellTxt;
  layer.open({
    type: 1,
    moveOut:true,
    title: "编辑",
    closeBtn: true,
    area: '600px;',
    shade: 0.8,
    moveOut: true,
    id: 'LAY_layuipro',// 设定一个id，防止重复弹出
    resize: true,
    btn: ['保存', '取消'],
    btnAlign: 'c',
    moveType: 0,// 拖拽模式，0或者1
    content: "<textarea  id=\"" +id+"shellText\" rows=\"16\" cols=\"200\"> "+shellTxt+" </textarea>",
    success: function(layero, index) {
    },
    yes: function(index, layero) {
      updateShellTxt(id);
      layer.close(index); // 如果设定了yes回调，需进行手工关闭
    }

  });
}

/**
 * 修改text后提交到后台。
 * 
 * @param id
 */
function updateShellTxt(id) {
  $.ajax({
    type: 'POST',
    url: getContextPath() + "/taskmanage/taskStepTemplate",
    dataType: 'json',
    data: {
      "id": id,
      "oper": "editShell",
      "shellTxt": $("#" + id + "shellText").val()
    },
    success: function(data) {
      creatTaskStepTemplateGrid(taskId);
    },
    error: function(text) {
      alert(text);
    }
  });
}

function shellNameFormat(cellvalue, options, rowObject) {
  var shellName = rowObject.shellName;
  var id = rowObject.id;
  if ("" != shellName && null != shellName) {
    return shellName + "<i class=\"icon-edit\"  onclick=\"editShellTxt('" + id + "')\" style=\"margin-left:3%;\"></i>" + "<i class='icon-upload' onclick=\"selectFile('" + id + "sFile')\" style=\"margin-left:3%;\"></i> "+"<input id=\"" + id+ "sFile\" type=\"file\" onchange=\"uploadShellFile('" + id + "sFile')\" name=\"file\" enctype=\"multipart/form-data\" hidden=\"hidden\">"
  } else {
    return "<i class=\"icon-folder-open\"  onclick=\"selectFile('" + id + "sFile')\" style=\"margin-left:3%;\"></i> " + "<input id=\"" + id+ "sFile\" type=\"file\" onchange=\"uploadShellFile('" + id + "sFile')\" name=\"file\" enctype=\"multipart/form-data\" hidden=\"hidden\">"
  }
}



