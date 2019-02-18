var sysType="";
function initSysfunction(){
	creatSysGrid()
	//页面加载完成之后执行
	$("#tabs").tabs();
}

function creatSysGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListSys").jqGrid({
		url:getContextPath()+"/systemmgtCon/search",
		datatype:'json',//请求数据返回的类型
		postData : {
						'systemName' : $("#name_search").val(),
						'in_ip' : $("#ip_search").val()
				   },
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'系统类别',name:'systype',index:'systype',width:100,align:'center',
	                    //将类型的值通过name形式进行展示
	                    formatter:function(cellvalue, options, rowObject){
	                   	 var indexname;
	                   	 switch(cellvalue){
	                   		case 1:indexname='数据库系统';sysType="db_type";break;
	                   		case 2:indexname='文件系统';sysType="file_type";break;
	                   		case 3:indexname='备份系统';sysType="backups_type";break;
	                   		case 4:indexname='数据库资源池';sysType="DB_Respool";break;
	                   		}
	                   	 return indexname;
	                    }
				   },
				   {label:'统一ID',name:'uid',index:'uid',width:100,align:'center'},
				   {label:'具体类型',name:'type',index:'type',width:100,align:'center'},
				   {label:'名称',name:'name',index:'name',width:100,align:'center'},
				   {label:'管理IP',name:'ip',index:'ip',width:100,align:'center'}
				],
		//loadonce:false,
		loadonce:true,
		multiselect: true,
		rownumbers: true,
//		rowNum:100,//一页显示多少条
//		rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: true,//复选框
		height:'190',
		autowidth:true,
		pager : "#jqGridPaperListSys",
	});
}


//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-230-20)*0.96;
	$("#jqGridListSys").setGridWidth(GridWidth);
});

//重新加载页面
function doSearchSys(){
	$.jgrid.gridUnload('#jqGridListSys');
	creatSysGrid();
}

//保存
function doSave() {
	//获取选中行的id
	var ids=$('#jqGridListSys').jqGrid('getGridParam','selarrrow');
	if(ids.length!=1){
		layer.msg("请选择一条数据！");
		return false;
	}
	var rowData = $("#jqGridListSys").jqGrid('getRowData',ids);
	var uid = rowData.uid;
	$("#target_id",parent.document).val(uid);
	//隐藏表单弹窗
	parent.layer.close(parent.layer.index);
  }

