<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/comm/css/quwery.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<title>角色管理</title>
</head>
<body>
	<div style="margin-left: 10px;margin-right: 10px;">
	<div style="height: 10px;"></div>
	<div>
		<p>
<!-- 			<button class="btn"  onclick="doInsert()"><i class="icon-plus-sign"></i>新增</button> -->
<!-- 			<button class="btn"  onclick="doEdit()"><i class="icon-edit"></i>编辑</button> -->
<!-- 			<button class="btn"  onclick="doDelete()"><i class="icon-minus-sign"></i>删除</button> -->
			<button class="btn"  onclick="relateMenu()"><i class="icon-check"></i>关联菜单</button>
		</p>
	</div>
	<div >
		<table id="jqGrid"></table>
   	    <div id="jqGridPager"></div>
	</div>
	</div>
</body>
<script type="text/javascript">
$(function() {
	doSearch();
});
/**
 * 初始化菜单列表
 */
function doSearch(){
	$.jgrid.gridUnload('#jqGrid'); 
	jQuery("#jqGrid").jqGrid({
		url : getContextPath()+"/systemMan/sysRoleSearch",
		mtype : "post",
		datatype: "json",
        colModel:[
			{ label: '角色标识', name: 'roleid', width: 80, key: true },
      		{ label: '角色名称', name: 'rolename', width: 100 },
      		{ label: '角色类型', name: 'roletype', width: 80 , formatter:enteredKbFmatter },
      		{ label: '角色说明', name: 'roledesc', width: 100 },
      		{ label: '角色可操作功能', name: 'rolemenu', width: 300  , formatter:_formatter}
      		],
	   	loadonce: true,
	   	viewrecords: true,
        autowidth: true,
        rownumbers: true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
        height: '100%',
        rowNum: 15,
        rownumbers: true, 
        rownumWidth: 25, 
        multiselect: false,
        reloadAfterSubmit: true,                
        pager: "#jqGridPager"
	}).trigger("reloadGrid");
}

$(window).resize(function(){ 
	$("#jqGrid").setGridWidth($(window).width()-250);
});


function enteredKbFmatter (cellvalue, options, rowObject) {  
    if(cellvalue == 1){  
        return "配置";   
    }
    if(cellvalue == 2){  
        return "业务";   
    }
    if(cellvalue == 3){  
        return "审计";   
    }else{
    	return "";
    }
} 

function _formatter(cellvalue, options, rowObject){
	var rolemenu="";
	if(cellvalue!=null && cellvalue.length>0){
		var cellArray = cellvalue.split(",");
		for (var i = 0; i < cellArray.length; i++) {
			var menucode=cellArray[i];
			if(menucode==1000){
				rolemenu+="统一图形化界面";
			}
			if(menucode==2000){
				rolemenu+="监控管理";
			}
			if(menucode==3000){
				rolemenu+="配置管理";
			}
			if(menucode==4000){
				rolemenu+="系统管理";
			}
			if(menucode==5000){
				rolemenu+="异常告警";
			}
			if(menucode==6000){
				rolemenu+="基线管理";
			}
			if(menucode==7000){
				rolemenu+="日志分析";
			}
			if(menucode==11000){
				rolemenu+="任务管理";
			}
			if(i!=(cellArray.length-1)){
				rolemenu+="，";
			}
		}
	}
	return rolemenu;
}

function doInsert(){
	layer.open({
	    type: 2,
	    title: '新增',
	    fix: false,
	    shadeClose: false,
	    area: ['500px', '300px'],
	    content: [getContextPath()+'/jsps/security/sysrole/sysRoleAdd.jsp','no']
	});
}
function doEdit(){
	if (!isSelectRow("#jqGrid")) return;
	var roleid =getSelectedRowID("#jqGrid");
	
	layer.open({
	    type: 2,
	    title: '编辑',
	    fix: false,
	    shadeClose: false,
	    area: ['500px', '300px'],
	    content: [getContextPath()+'/jsps/security/sysrole/sysRoleEdit.jsp?roleid='+roleid,'no']
	});
}
function doDelete(){
	var roleid =getSelectedRowID("#jqGrid");
	if (!isSelectRow("#jqGrid")) return;
	var r = window.confirm("确定要删除数据么？");
	if (r) {
		$.ajax({
			url : getContextPath()+"/systemMan/deleSysRole",
			type : "post",
			data : {
				"roleid":roleid
			},
			success : function(result) {
				if(result){
					alert("删除成功！");
					doSearch();
				}
			},
			error : function() {
				alert("执行删除失败");
			}
		});
	}
}
//关联菜单
function relateMenu(){
	var roleid =getSelectedRowID("#jqGrid");
	if (!isSelectRow("#jqGrid")) return;
	var rowDatas = $("#jqGrid").jqGrid('getRowData', roleid);
	var roletype=rowDatas["roletype"];
	if(roletype == "配置"){  
		roletype= 1;   
    }
    if(roletype == "业务"){  
    	roletype= 2;  
    }
    if(roletype == "审计"){  
    	roletype= 3;   
    }
	layer.open({
	    type: 2,
	    title: '关联菜单',
	    fix: false,
	    shadeClose: false,
	    area: ['670px', '490px'],
	    content: [getContextPath()+'/jsps/security/sysrole/roleMenuList.jsp?roleid='+roleid+'&roletype='+roletype,'no']
	});
}
</script>
</html>