var type_arr={};
var type="group_type"
var flag = 0;//初始化一个动作，用来表示tab是否展示
function initGrofunction(){
	type_arr=$._PubSelect("",getContextPath()+"/commonCon/search?type="+type,"[{'getKey':'value','getVal':'name'}]");
	//页面加载完成之后执行
	creatGroupGrid();
	$("body").on("click","#srch",function(e){
		doSearch();
	});
	$("#tabs").tabs();
	//页签切换处理
	$("#tabs ul").on("click","li",function(){
		if (isSelectRow("#jqGridListGro")) {
			doGroupCong();
		}
	});
}
function creatGroupGrid(){
	jQuery("#jqGridListGro").jqGrid({
		url:getContextPath()+"/groupCon/search",
		datatype:'json',
		postData:{'name': $("#name_search").val(),'in_ip': $("#ip_search").val()},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'统一ID',name:'uid',index:'uid',width:100,align:'center'},
				   {label:'名称',name:'name',index:'name',width:100,align:'center'},
				   {label:'组类型',name:'grouptypeName',index:'grouptypeName',width:100,align:'center'},
				   {label:'具体类型',name:'grotypeName',index:'grotypeName',width:100,align:'center'},
				   {label:'描述',name:'description',index:'description',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
		rownumbers: true,
//		altRows:true,
		rowNum:100,//一页显示多少条
//		rowList:[10,20,30],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: false,//复选框
		caption:"<div style='color:#000;width:95%;'>组管理<span  id='groupAdd' style='float:right;cursor:pointer;' onclick='groupAdd()'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",//表格的标题名字
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/groupCon/update",//编辑地址
		pager : "#jqGridPaperListGro",
		gridComplete: function(){
            var ids = $("#jqGridListGro").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                ed = "<a href=# title='编辑' style='text-decoration: none;' onclick='groupAdd("+cl+")'>编辑 </a>";
                vi = "<a href=# title='删除' style='text-decoration: none;' onclick='doDeleteGro("+cl+");'>删除</a>"; 
                jQuery("#jqGridListGro").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            } 
        }
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListGro").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//删除
function doDeleteGro(id){
	var jwt = getJWT();
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/groupCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt
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
/**
 * 刷新
 */
function doSearch(){
	$.jgrid.gridUnload("#jqGridListGro");
	creatGroupGrid();
}

/**
 * tab新增按钮
 */
function groupAdd(id){
	getJWT();
	layer.open({
	    type: 2,
	    title: '组管理', 
	    fix: false,
	    shadeClose: false,
	    area: ['880px','530px'],
//	    area: ['65%', '85%'],
	    content: [getContextPath()+"/jsp/pages/configmgt/group/groupAdd.jsp?id="+id,'no']
	});
}

var grotype;
var grouptype;
//编辑设置value
function setDetailValue(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		$("#name").val(rs[0].name);
		$("#description").val(rs[0].description);
		grotype = rs[0].grotype;
		grouptype = rs[0].grouptype;
    }
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/groupCon/search",
		type : "post",
		async : false,
		data : {
			"id" : id
		},
		dataType : "json",
		success : function(result) {
			resultvalue = result;
		},
		error : function() {
			alert("加载失败");
		}
	});
	return resultvalue;
}

//设备详情
function deviceInfo(id){
	layer.open({
	    type: 2,
	    title: '设备详情', 
	    fix: false,
	    shadeClose: false,
	    area: ['65%', '60%'],
	    content: [getContextPath()+"/jsp/comm/jsp/deviceInfo.jsp?id="+id,'no']
	});
}