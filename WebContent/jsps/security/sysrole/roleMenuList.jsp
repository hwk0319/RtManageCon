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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/comm/css/quwery.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<title>关联菜单</title>
</head>
<body>
	<div style="margin:1%;">
		<table id="jqGrid"></table>
   	    <div id="jqGridPager"></div>
	</div>
	<div class="widget-content">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script type="text/javascript">
var roleid;
var roletype;
$(function() {
	roleid =$.getUrlParam('roleid');
	roletype=$.getUrlParam('roletype');
	if(roletype == 1){  
		roletype=  "配置";   
    }
    if(roletype == 2){  
    	roletype=  "业务";   
    }
    if(roletype == 3){  
    	roletype= "审计";   
    }
	creatGrid();
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		/* window.close();
		$("#alert").hide(); */
		parent.layer.closeAll();
	})
});
/**
 * 初始化菜单列表
 */
function creatGrid(){
	jQuery("#jqGrid").jqGrid({
		url : getContextPath()+"/systemMan/roleMenuGrid",
		mtype : "post",
		datatype: "json",
        colModel:[
			{ label: '菜单编码', name: 'menucode', width: 110, key: true , align:"center"},
      		{ label: '菜单名称', name: 'menuname', width: 165 },
      	    { label: '请求地址', name: 'menuurl', width: 170  },
      	  	{ label: '菜单类型', name: 'menutarget', width: 110 , formatter:enteredKbFmatter, align:"center"}
      		],
	   	loadonce: true,
	   	viewrecords: true,
        width: '100%',
        height: '100%',
        rowNum: 15,
        rownumbers: true, 
        rownumWidth: 25, 
        multiselect: true,
        reloadAfterSubmit: true,                
        pager: "#jqGridPager"
	}).trigger("reloadGrid");
}

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

//关联菜单保存
function doSave(){
	if (!isSelectRow("#jqGrid")) return;
	var selrow =jQuery("#jqGrid").getGridParam('selarrrow');
	//var rowDatas = $("#jqGrid").jqGrid('getRowData', selrow);
	
	for(var i=0;i<selrow.length;i++){
		var menutarget="";
		var rowDatas = $("#jqGrid").jqGrid('getRowData', selrow[i]);
		 if(rowDatas["menutarget"]!="首页" && i==0){
			menutarget=rowDatas["menutarget"];
		}else if(rowDatas["menutarget"]!="首页" && menutarget=="" && i==1){
			menutarget=rowDatas["menutarget"];
		}
		if(menutarget!="" && menutarget!=rowDatas["menutarget"] && rowDatas["menutarget"]!="首页"){
			layer.alert("请选择同类型菜单");
			return;
		}
		if( menutarget!="" && roletype!=menutarget && menutarget!="首页" ){
			layer.alert("请选择于角色同类型菜单");
			return;
		}
	}
	var roleMenu="";
	for(var i=0;i<selrow.length;i++){
		if(i == selrow.length-1){
			roleMenu = roleMenu + selrow[i];
		}else{
			roleMenu = selrow[i]+","+ roleMenu;
		}
	}
	$.ajax({
		url : getContextPath()+"/systemMan/updateRoleMenu",
		type : "post",
		data : {
			"roleid":roleid,
			"rolemenu" : roleMenu
		},
		success : function(result) {
			if(result){
				layer.alert("保存成功！", {
					title: "提示"
				},function(){
					window.parent.doSearch();
					parent.layer.closeAll();
				});
			}
		},
		error : function() {
			layer.alert("保存失败！");
		}
	});
}
</script>
</html>