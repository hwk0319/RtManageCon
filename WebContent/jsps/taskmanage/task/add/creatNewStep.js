$(function() {
	taskId = $("#shareTaskId",parent.document).val();
	operaType =  $("#taskOperationType",parent.document).val();
    creatTaskStepGrid(taskId);
})

//重新加载页面
function doSearch(){
	creatTaskStepGrid(taskId)
}

function creatTaskStepGrid(taskId)
{
	var lastsel1;
   $('html').bind('click', function(e) { //用于点击其他地方保存正在编辑状态下的行
          if ( lastsel1 != "" ) { //if a row is selected for edit 
              if($(e.target).closest('#jqGridTaskStep').length == 0) { //and the click is outside of the grid //save the row being edited and unselect the row  
	              jQuery('#jqGridTaskStep').jqGrid('saveRow', lastsel1); 
	              jQuery('#jqGridTaskStep').resetSelection(); 
	              lastsel1=""; 
              } 
          } 
   });
  jQuery("#jqGridTaskStep").jqGrid({
              url : getContextPath()+"/taskmanage/getTaskStepById",
              mtype: 'POST', 
              postData: {    
                   "taskId": taskId,
                   "id":0
                 },    
              datatype : "json",
              colModel: [
                        { label: '步骤ID',    name: 'id',     editable : false,   width: 0, hidden:true, key: true},
                        { label: '任务ID',    name: 'taskId',   editable : false,   width: 0, hidden:true},
                        { label: '步骤名称',    name: 'name',   editable : true,  width:150,editrules:{required:true},align:'left'},  
                        { label: '步骤号',     name: 'stepOrder',  editable : true , width:60,editrules : {custom:true, custom_func:validateStepOrder},align:'center'},
                        { label: '脚本名称',  name: 'shellName',  editable : false,   width:157,align:'left', formatter: shellNameFormat},
                        { label: '超时时间(s)',   name: 'timeOut',  editable : true,  width:50,editrules:{required:true},align:'center'},
                        { label: '执行信息',   name: 'resultDesc',  editable : false,  width:100,align:'center'},
                        { label: '脚本内容',  name: 'shellTxt', editable : false, width:0,editrules:{required:true},hidden:true},
                        { label: '步骤说明',    name: 'poDesc',   editable : true,  width:170,editrules:{required:true},align:'left'},
                        {label:'操作',name:'act',index:'act',width:50,align:'center'}
                        ],
            sortable:true,
            sortname :'id',
          viewrecords : true,
          autowidth:true,
          height : '100%',
          rowNum : 20,
          rownumbers : true,
          rownumWidth : 25,
          multiselect : false,
          reloadAfterSubmit : true,
          editurl : getContextPath()+"/taskmanage/taskStep",
          caption : "<div style='color:#000'>步骤列表 <a  href='javascript:void(0);' onclick='addNewTaskStep()'  style='text-decoration : none;color:#000;'><i id='add' class='icon-plus-sign' style='margin-left:75%;'></i>&nbsp;新增步骤</a></div>",
          loadonce:true,
          pager : "#jqGridTaskStepPager",
          ondblClickRow: function (rowid,iRow,iCol,e) {
            if(rowid && rowid != lastsel1)
            {  
                  jQuery('#jqGridTaskStep').saveRow(lastsel1);
                  jQuery('#jqGridTaskStep').editRow(rowid,true);
                  lastsel1=rowid;
            }
            else if(rowid == lastsel1)
            {
              jQuery('#jqGridTaskStep').editRow(rowid,true);
            }
            },
        gridComplete: function()
        {
                var ids = $("#jqGridTaskStep").getDataIDs();
                for(var i=0;i<ids.length;i++)
                {
                    var cl = ids[i];
                    vi = "<a  onclick=\"doDelete("+cl+")\" style=\"margin-left:3%;cursor:pointer;\">删除</a>"
                    jQuery("#jqGridTaskStep").jqGrid('setRowData',ids[i],{act:vi});
                }
            }
        }).trigger("reloadGrid");
  $("#gview_jqGridTaskStep").css({width:"100%",height:"95%"});
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function() {
	var width = document.body.clientWidth;
	var GridWidth = (width - 250);
	$("#jqGridTaskStep").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});


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
    creatTaskStepGrid (taskId);
    return [false,"步骤号只能为数字！"];
    }
  return [true,""];
}

function shellNameFormat(cellvalue, options, rowObject) 
{
  var shellName = rowObject.shellName;
  var id=rowObject.id;
  if ("" != shellName && null != shellName ) 
  {
    return shellName;
  }
  else
  {
    return "<i class=\"icon-folder-open\" onclick=\"selectStepFile('" + id + "sFile')\" style=\"margin-left:3%;\"></i> "+"<input id=\"" + id+ "sFile\" type=\"file\" onchange=\"uploadShellFile('" + id + "sFile')\" name=\"file\" enctype=\"multipart/form-data\" hidden=\"hidden\">"
  }
}

function stepStateFormat(cellvalue, options, rowObject)
{
	if (rowObject.state == 0) {
		return '创建中';
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



function addNewTaskStep()
{
    addNewTaskStep1();
}

//添加步骤
function addNewTaskStep1(){
	layer.open({
	    type: 2,
	    title: '步骤', 
	    fix: false,
	    shadeClose: false,
	    area: ['450px','290px'],
//	    area: ['55%','80%'],
	    moveOut: true,
	    content: [getContextPath()+"/jsps/taskmanage/task/add/addStep.jsp?taskId="+taskId,'no']
	});
}

function editShellTxt(id)
{
  var rowDatas = $("#jqGridTaskStep").getRowData(id);
  var shellTxt = rowDatas.shellTxt;
  layer.open({
      type: 1
      ,title: "编辑" //不显示标题栏
      ,closeBtn: true
      ,area: '600px;'
      ,shade: 0.8
      ,moveOut :true
      ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
      ,resize: true
      ,btn: ['保存', '取消']
      ,btnAlign: 'c'
      ,moveType: 0 //拖拽模式，0或者1
      ,content: "<textarea  id=\"" +id+"shellText\" rows=\"16\" cols=\"200\"> "+shellTxt+" </textarea>"
      ,
      success: function(layero, index){
         
        },
      yes: function(index, layero){
          updateShellTxt(id);
          layer.close(index); //如果设定了yes回调，需进行手工关闭
        }
  
    });
}

/**
 * 修改text后提交到后台。
 * @param id
 */
function updateShellTxt(id) {
  $.ajax({
    type : 'POST',
    url : getContextPath() + "/taskmanage/taskStep",
    dataType : 'text',
    data : {
      "id" : id,
      "oper" : "editShell",
      "shellTxt" : $("#" + id + "shellText").val()
    },
    success : function(data) {
      $.jgrid.gridUnload('#jqGridTaskStep');
      creatTaskStepGrid(taskId);
    },
    error : function(text) {
    	layer.alert(text);
    }
  });
}


function selectStepFile (id)
{
  $ ('#' + id).click();
}

function uploadShellFile (id)
{
	//upload2(id);
  var fileStr = $ ('#' + id).val ();
  if (null == fileStr || fileStr == "")
  {
    layer.alert ("请先选择要上传的脚本文件！");
    return;
  }
  else if(fileStr.toLowerCase().substring(fileStr.length-3,fileStr.length) != ".sh")
  {
	layer.alert ("Error，无法解析的文件，文件必须以‘.sh’结尾！");
    return;
  }
  //这里传入的id因为添加了’sFile‘,所以往后台发送时要把是’sFile‘去掉。
  var idNum = id.substring(0,id.length-5);
  $.ajaxFileUpload({
      url : getContextPath () + '/taskmanage/fileUpload', //用于文件上传的服务器端请求地址?upLoadType=shell&id='+id
      type: 'post',
        data : {
          "upLoadType":"shell",
          "fileType":"sh",
          "id":idNum
        },
      secureuri : false, //是否需要安全协议，一般设置为false
      fileElementId : id, //文件上传域的ID
      dataType : 'json', //返回值类型 一般设置为json
      success : function (data, status) //服务器成功响应处理函数
      {
        if(data.status=="success")
          {
          creatTaskStepGrid (taskId);
          var filename=fileStr.replace(/.*(\/|\\)/, "");
          $("#shellName"+id).html(filename)
          }
        else
        	{
        		layer.alert(data.value);
        	}
        
      }
  });
}

//删除
function doDelete(id){
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/taskmanage/taskStep",
	  		type : "post",
	  		dataType : 'json',
	  		data : {
	  			"id" : id,
	  			"oper" : "del"
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
	  			doSearch();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  			doSearch();
	  		}
	  	});	
	});
}
