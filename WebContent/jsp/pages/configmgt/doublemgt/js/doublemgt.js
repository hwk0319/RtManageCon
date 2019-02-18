var type_arr={};
var double_type="double_type"
var flag=0;
function initfunction(){
	type_arr=$._PubSelect("#doubtype",getContextPath()+"/commonCon/search?type="+double_type,"[{'getKey':'value','getVal':'name'}]");
	//页面加载完成之后执行
	creatDouGrid();
	$("body").on("click","#cluSrch",function(e){
		doSearchDou();
	});
	$("#tabs").tabs();
	//页签切换处理
	$("#tabs ul").on("click","li",function(){
		if (isSelectRow("#jqGridListDou")) {
			doSearchdouble();
		}
	});
}
function creatDouGrid(){
	jQuery("#jqGridListDou").jqGrid({
		url:getContextPath()+"/doublemgtCon/search",
		datatype:'json',//请求数据返回的类型
		postData:{'name': $("#name_search").val(),'doubtype': $("#doubtype").val()},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true,
					   	editable:true,//可编辑
				   },
				   {label:'统一ID',name:'uid',index:'uid',width:100,align:'center',
					   	editable:true,//可编辑
	                    edittype:'text',//类型 文本框
	                    editoptions:{size:20,maxlength:64},
	                    editrules:{required:true},
	                    formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
				   },
				   {label:'名称',name:'name',index:'name',width:100,align:'center',
					   	editable:true,//可编辑
	                    edittype:'text',//类型 文本框
	                    editoptions:{size:20,maxlength:100},
	                    editrules:{required:true},
	                    formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
				   },
				   {label:'具体类型',name:'doubtype',index:'doubtype',width:100,align:'center',
					   editable:true,
	                    edittype:'select',
	                    editoptions:{value:type_arr},
	                    editrules:{required:true},
	                    formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"},
	                    formatter:function(cellvalue, options, rowObject){
		                   	var type="";
		                    for (key in type_arr){
		                    	if(cellvalue==key){
		                    		type=type_arr[key];
									break;
								}
		                    }
		                   	return type;
	                    }
				   },
				   {label:'描述',name:'description',index:'description',width:200,align:'center',
					    editable:true,
	                    edittype:'textarea',
	                    editoptions:{size:20,maxlength:255},
				   },
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
		rownumbers: true,
		pgbuttons:false,
//		altRows:true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: false,//复选框
		caption:"<div style='color:#000;width:95%;'>双活管理<span  id='douAdd' onclick='douAdd()' style='float:right;cursor:pointer;'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",//表格的标题名字
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/doublemgtCon/update",//编辑地址
		pager : "#jqGridPaperListDou",
		gridComplete: function(){
            var ids = $("#jqGridListDou").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                ed = "<a href=# title='编辑' style='text-decoration: none;' onclick='douAdd("+cl+")'>编辑 </a>";
                vi = "<a href=# title='删除' style='text-decoration: none;' onclick='doDeleteDou("+cl+");'>删除</a>"; 
                jQuery("#jqGridListDou").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            } 
        }
	});
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListDou").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//删除
function doDeleteDou(id){
	var jwt = getJWT();
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/doublemgtCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
				window.parent.doSearchDou();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}
//重新加载页面
function doSearchDou(){
	$.jgrid.gridUnload('#jqGridListDou');
	creatDouGrid();
}
//新增功能独立出来
function douAdd(id){
	getJWT();
	var topH=$(".search").height()+$("#commHead").height()+$("#note").height()+10;
	layer.open({
	    type: 2,
	    title: '双活管理', 
	    fix: false,
	    shadeClose: false,
	    moveOut: true,
	    area: ['880px','550px'],
//	    area: ['65%', '85%'],
	    content: [getContextPath()+"/jsp/pages/configmgt/doublemgt/doubleAdd.jsp?id="+id,'no']
	});
}
var integtype;
//编辑设置value
function setDetailValue(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		$("#name").val(rs[0].name);
		$("#description").val(rs[0].description);
		doubtype = rs[0].doubtype;
	}
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/doublemgtCon/search",
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

