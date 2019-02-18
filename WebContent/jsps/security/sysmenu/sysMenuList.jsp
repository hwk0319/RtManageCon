<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/comm/css/quwery.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<title>菜单管理</title>
</head>
<body>
	<div style="margin-left: 10px; margin-right: 10px;">
		<div class="">
			<div style="height: 10px;"></div>
			<p>
<!-- 				<button class="btn" onclick="doAdd()"> -->
<!-- 					<i class="icon-plus"></i>增加 -->
<!-- 				</button> -->
				<button class="btn" onclick="doEdit()">
					<i class="icon-edit"></i>编辑
				</button>
			</p>
		</div>
		<div>
			<table id="jqGrid"></table>
			<div id="jqGridPager"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		creatGrid();
	});
	/**
	 * 初始化菜单列表
	 */
	function creatGrid() {
		jQuery("#jqGrid").jqGrid({
			url : getContextPath() + "/systemMan/searchGrid",
			treeGrid : true,
			treeGridModel : 'adjacency', //treeGrid模式，跟json元数据有关
			ExpandColumn : 'menuname',
			mtype : "post",
			datatype : "json",
			colModel : [ {
				label : '菜单编码',
				name : 'menucode',
				width : 0,
				key : true,
				hidden : true
			}, {
				label : '菜单名称',
				name : 'menuname',
				width : 180
			}, {
				label : '菜单类型',
				name : 'menutarget',
				width : 120,
				formatter : enteredKbFmatter
			}, {
				label : '菜单级别',
				name : 'menulevel',
				width : 120
			}, {
				label : '菜单顺序',
				name : 'menuorder',
				width : 120
			}, {
				label : '请求地址',
				name : 'menuurl',
				width : 450
			} ],
			treeReader : {
				level_field : "level",
				parent_id_field : "parentcode",
				leaf_field : "leaf",
				expanded_field : "expanded"
			},
			loadonce : false,
			viewrecords : true,
			autowidth : true,
			height : 'auto',
			rowNum : 15,
			pager : "#jqGridPager"
		}).trigger("reloadGrid");
	}

	function enteredKbFmatter(cellvalue, options, rowObject) {
		if (cellvalue == 1) {
			return "配置";
		}
		if (cellvalue == 2) {
			return "业务";
		}
		if (cellvalue == 3) {
			return "审计";
		} else {
			return "";
		}
	}
	
	$(window).resize(function(){ 
		$("#jqGrid").setGridWidth($(window).width()-250);
	});

	function doEdit() {
		if (!isSelectRow("#jqGrid"))
			return;
		var menucode = getSelectedRowID("#jqGrid");
		layer.open({
			type : 2,
			title : '编辑',
			fix : false,
			shadeClose : false,
			area : [ '500px', '300px' ],
			content : [getContextPath() + '/jsps/security/sysmenu/sysMenuEdit.jsp?menucode=' + menucode, 'no' ]
		});
	}
</script>
</html>