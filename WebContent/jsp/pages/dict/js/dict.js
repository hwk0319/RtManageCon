function initDictFunction(){
	creatDictGrid();
	//查询类型下拉选择项
	$._PubSelect("#type_search",getContextPath()+"/dictCon/customSql","[{'getKey':'type','getVal':'label'}]");
}
function creatDictGrid(){
	jQuery("#jqGridDictList").jqGrid({
		url:getContextPath()+"/dictCon/search",
		datatype:'json',//请求数据返回的类型
		postData : {
			'type' : $("#type_search").val(),
			'label' : $("#label_search").val()},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'类型',name:'type',index:'type',width:100,align:'center'},
				   {label:'标签',name:'label',index:'label',width:100,align:'center'},
				   {label:'名称',name:'name',index:'name',width:100,align:'center'},
				   {label:'值',name:'value',index:'value',width:100,align:'center'},
				   {label:'排序',name:'sort',index:'sort',width:100,align:'center'},
				   {label:'描述',name:'description',index:'description',width:200,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
		loadonce:true,
		rownumbers: true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'asc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: false,//复选框
		caption:"<div style='color:#000;background-color: #7ab8dd;width:95%;height:100%;'>数据字典<span  id='add' style='float:right;cursor:pointer;' onclick='DictAdd()'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/dictCon/update",//编辑地址
		pager : "#jqGridPaperDictList",
		gridComplete: function(){
            var ids = $("#jqGridDictList").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                ed = "<a href=# title='编辑' onclick='DictAdd("+cl+");'>编辑 </a>";
                vi = "<a href=# title='删除' onclick='doDeleteDict("+cl+");'>删除</a>"; 
                jQuery("#jqGridDictList").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            } 
        },
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridDictList").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//添加
function DictAdd(id){
	getJWT();
	layer.open({
  	    type: 2,
  	    title: '数据字典', 
  	    fix: false,
  	    shadeClose: false,
  	    area: ['550px', '380px'],
//  	    area: ['40%', '60%'],
  	    content: [getContextPath()+"/jsp/pages/dict/addDict.jsp?id="+id,'no']
  	});
}
//重新加载页面
function doSearch(){
	$.jgrid.gridUnload('#jqGridDictList');
	creatDictGrid();
}

//删除
function doDeleteDict(id){
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/dictCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
				window.parent.doSearch();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}