 $(function(){
	taskId = $("#shareTaskId",parent.document).val();
    var taskOperation=TaskOperation(taskId);
    if(taskOperation.lastTime  != null)
    {
    	//之前运行过，需改为不可修改。
    	createTaskParamGridNoEdit(taskId);
    }
    else
    {
    	createTaskParamGrid(taskId);
    }
});

//重新加载页面
 function doSearchParam(){
	 createTaskParamGrid(taskId)
 }

function createTaskParamGrid(taskId)
{
   var lastsel2;
   $('html').bind('click', function(e) { //用于点击其他地方保存正在编辑状态下的行
          if ( lastsel2 != "" ) { //if a row is selected for edit 
              if($(e.target).closest('#jqGridTaskParam').length == 0) { //and the click is outside of the grid //save the row being edited and unselect the row  
              jQuery('#jqGridTaskParam').jqGrid('saveRow', lastsel2); 
              jQuery('#jqGridTaskParam').resetSelection(); 
              lastsel2=""; 
              } 
          } 
   });
  $("#jqGridTaskParam").jqGrid({
        url: getContextPath()+"/taskmanage/getTaskParamp",
        mtype: 'POST', 
        postData: {    
             "taskId": taskId ,
             "id":0
           },    
        datatype : "json",
        colModel: [   
          { label: 'id', name: 'id',index:'id',key:true, editable : false,width: 0, hidden:true},
          { label: 'taskId', name: 'taskId',index:'taskId', width: 0, hidden:true},
          { label: '参数项',  name: 'name',index:'name',editrules:{required:true},editable : true, width: 250 },  
          { label: '默认值',   name: 'value',index:'value', editrules:{required:true},editable : true,width:137 },  
          { label: '参数描述', name: 'poDesc', index:'poDesc',editrules:{required:true},editable : true,width: 250 },
          {label:  '操作',   name:'act',index:'act',index:'operate',width:100,align:'center'}
          ],
        caption:"<div style='overflow:hidden;height:19px;color:#000;width:95%;'>参数列表" +
				"<span style='margin-right:25px;float:right;line-height:19px;cursor:pointer;' onclick='selectParamFile(\"iniFile\")'><i id='upload' class='icon-upload' style='line-height:19px;'></i>&nbsp;上传参数文件</span><input id=\"iniFile\" type=\"file\" name=\"file\" onchange='uploadParamFile(\"iniFile\")' enctype=\"multipart/form-data\" hidden=\"hidden\">" +
				"<span style='margin-right:10px;float:right;line-height:19px;cursor:pointer;' onclick='selectParamFile(\"sqlFile\")'><i id='upload' class='icon-upload' style='line-height:19px;'></i>&nbsp;上传SQL文件</span><input id=\"sqlFile\" type=\"file\" name=\"file\" onchange='uploadSqlFile(\"sqlFile\")' enctype=\"multipart/form-data\" hidden=\"hidden\">"+
				"<span style='margin-right:10px;margin-top:2px;float:right;line-height:9px;cursor:pointer;' onclick='addNewTaskParam()'><i class='icon-plus-sign' style='line-height:19px;'></i>&nbsp;新增参数</span></div>",
        viewrecords: true,
        autowidth:true,
        rownumbers: true,
        loadonce:true,
        multiselect: false,
        reloadAfterSubmit: true,   
        editurl : getContextPath()+"/taskmanage/taskParam",
        rowNum:100,//一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
        pager: "#jqGridTaskParamPager",
        ondblClickRow: function (rowid,iRow,iCol,e) {
        if(rowid && rowid != lastsel2)
          {  
                jQuery('#jqGridTaskParam').saveRow(lastsel2);
                jQuery('#jqGridTaskParam').editRow(rowid,true);
                lastsel2=rowid;
          }
        else if(rowid == lastsel2)
        {
          jQuery('#jqGridTaskParam').editRow(rowid,true);
        }
          },
    gridComplete: function(){
            var ids = $("#jqGridTaskParam").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                vi = "<a onclick=\"doDeleteParam("+cl+")\" style=\"margin-left:3%;cursor:pointer\">删除</a>"
                jQuery("#jqGridTaskParam").jqGrid('setRowData',ids[i],{act:vi});
            } 
        }
    }).trigger("reloadGrid");
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function() {
	var width = document.body.clientWidth;
	var GridWidth = (width - 250);
	$("#jqGridTaskParam").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
 
 function createTaskParamGridNoEdit(taskId)
 {
	 $("#jqGridTaskParam").jqGrid({
		 url: getContextPath()+"/taskmanage/getTaskParamp",
		 mtype: 'POST', 
		 postData: {    
			 "taskId": taskId  ,
			 "id":0
		 },    
		 datatype : "json",
		 colModel: [   
		            { label: 'id', name: 'id',index:'id',key:true, editable : false,width: 0, hidden:true},
		            { label: 'taskId', name: 'taskId',index:'taskId', width: 0, hidden:true},
		            { label: '参数项',  name: 'name',index:'name',editrules:{required:true},editable : true, width: 250 },  
		            { label: '默认值',   name: 'value',index:'value', editrules:{required:true},editable : true,width: 180 },  
		            { label: '参数描述', name: 'poDesc', index:'poDesc',editrules:{required:true},editable : true,width: 300 }
		            ],
        caption:"<div style='color:#000;'>参数列表</div>",
        viewrecords: true,
        loadonce:true,
        height:'auto',
        autowidth:true,
        rownumbers: true, 
        rownumWidth: 50, 
        multiselect: false,
        reloadAfterSubmit: true,   
        loadonce:true,
        pager: "#jqGridTaskParamPager"
	 }).trigger("reloadGrid");
 }


function addNewTaskParam()
{
    addNewTaskParam1();
}

function addNewTaskParam1()
{
	layer.open({
	    type: 2,
	    title: '参数', 
	    fix: false,
	    shadeClose: false,
	    area: ['450px','290px'],
	    moveOut: true,
	    content: [getContextPath()+"/jsps/taskmanage/task/addParam.jsp?taskId="+taskId,'no']
	});
}


function configParamBySql()
{
	
}

function selectParamFile (id)
{
  $ ('#' + id).click();
}

function uploadParamFile(id) {
	var fileStr = $('#' + id).val();
	if (null == fileStr || fileStr == "") {
		layer.alert("请选择要上传的脚本文件！");
		return;
	} else if (fileStr.toLowerCase().substring(fileStr.length - 4,
			fileStr.length) != ".ini") {
		layer.alert("Error，无法解析的文件，文件必须以'.ini'结尾！");
		return;
	}
	
	$.ajaxFileUpload({
		url : getContextPath() + '/taskmanage/fileUpload', //用于文件上传的服务器端请求地址
		type : 'post',
		data : {
			"upLoadType" : "param",
			"id" : taskId,
			"fileType":"ini"
		},
		secureuri : false, //是否需要安全协议，一般设置为false
		fileElementId : id, //文件上传域的ID
		dataType : 'json', //
		success : function(data, status) //服务器成功响应处理函数
		{
			if (data.status == "success") {
				$.jgrid.gridUnload('#jqGridTaskParam');
				createTaskParamGrid(taskId);
			} else {
				layer.alert(data.value);
			}
		},
		 error: function(data, status, e){   
			 layer.alert(e);  
         }  
	});
}

function uploadSqlFile(id) {
	var fileStr = $('#' + id).val();
	if (null == fileStr || fileStr == "") {
		layer.alert("请选择要上传的SQL文件！");
		return;
	} else if (fileStr.toLowerCase().substring(fileStr.length - 4,
			fileStr.length) != ".sql") {
		layer.alert("Error，无法解析的文件，文件必须以'.sql'结尾！");
		return;
	}
	
	$.ajaxFileUpload({
		url : getContextPath() + '/taskmanage/fileUpload', //用于文件上传的服务器端请求地址
		type : 'post',
		data : {
			"upLoadType" : "param",
			"id" : taskId,
			"fileType":"sql"
		},
		secureuri : false, //是否需要安全协议，一般设置为false
		fileElementId : id, //文件上传域的ID
		dataType : 'json', //
		success : function(data, status) //服务器成功响应处理函数
		{
			if (data.status == "success") {
				$.jgrid.gridUnload('#jqGridTaskParam');
				createTaskParamGrid(taskId);
			} else {
				layer.alert(data.value);
			}
		},
		 error: function(data, status, e){   
			 layer.alert(e);  
         }  
	});
}

//删除
function doDeleteParam(id){
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/taskmanage/taskParam",
	  		type : "post",
	  		dataType : 'json',
	  		data : {
	  			"id" : id,
	  			"oper" : "del"
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
	  			doSearchParam();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  			doSearchParam();
	  		}
	  	});	
	});
}
