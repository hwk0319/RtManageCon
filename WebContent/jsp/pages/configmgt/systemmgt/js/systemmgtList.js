var type_arr={};
var sysType="";
var mm;
var device_mm;
function initSysfunction(){
	//页面加载完成之后执行
	creatSysGrid()
	$("body").on("click","#srch",function(e){
		doSearchSys();
	});
	$("#tabs").tabs();
}

function creatSysGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListSys").jqGrid({
		url:getContextPath()+"/systemmgtCon/search",
		datatype:'json',//请求数据返回的类型
		postData:{'name': $("#name_search").val(),'in_ip': $("#ip_search").val()},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'统一ID',name:'uid',index:'uid',width:100,align:'center'},
				   {label:'系统类别',name:'systype',index:'systype',width:100,align:'center',
					   	//将类型的值通过name形式进行展示
	                    formatter:function(cellvalue, options, rowObject){
	                   	 var indexname;
	                   	 switch(cellvalue){
	                   		case 1:indexname='数据库系统';sysType="db_type";break;
	                   		case 2:indexname='文件系统';sysType="file_type";break;
	                   		case 3:indexname='备份系统';sysType="backups_type";break;
	                   		case 4:indexname='数据库资源池';sysType="DB_Respool";break
	                   		case 5:indexname='web容器';sysType="web_type";break;
	                   		}
	                   	 return indexname;
	                    }
				   },
				   {label:'具体类型',name:'type',index:'type',width:100,align:'center'},
				   {label:'名称',name:'name',index:'name',width:100,align:'center'},
				   {label:'管理IP',name:'ip',index:'ip',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',width:100,align:'center'}
				],
		//loadonce:false,
		rownumbers: true,
		altRows:true,
		altclass:'jqgridRowColor',
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: false,//复选框
		caption:"<div style='width:95%;'>软件系统<span  id='sysAdd' style='float:right;cursor:pointer;' onclick='systemmgtAdd();'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",//表格的标题名字
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/systemmgtCon/update",//编辑地址
		pager : "#jqGridPaperListSys",
		gridComplete: function(){
            var ids = $("#jqGridListSys").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                ed = "<a href=# title='编辑' style='text-decoration: none;' onclick='systemmgtAdd("+cl+");'>编辑 </a>";
                vi = "<a href=# title='删除' style='text-decoration: none;' onclick='doDeleteSys("+cl+");'>删除</a>"; 
                jQuery("#jqGridListSys").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            } 
        }
	});
}

function afterShowForm(){
	$("#FrmGrid_jqGridListSys table:eq(0) tbody #tr_systype select").change(function(){
		getTypeinfo($(this),"#tr_type select");
	});
	var type=$("#FrmGrid_jqGridListSys table:eq(0) tbody #tr_type select").val();
	getTypeinfo($("#FrmGrid_jqGridListSys table:eq(0) tbody #tr_systype select"),$("#FrmGrid_jqGridListSys table:eq(0) tbody #tr_type select"));
	$("#FrmGrid_jqGridListSys table:eq(0) tbody #tr_type select").val(type);
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListSys").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});

//删除
function doDeleteSys(id){
	var jwt = getJWT();
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/systemmgtCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
				window.parent.doSearchSys();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}

//重新加载页面
function doSearchSys(){
	$.jgrid.gridUnload('#jqGridListSys');
	creatSysGrid();
}

//添加
function systemmgtAdd(id){
	getJWT();
	var rowData = $("#jqGridListSys").jqGrid('getRowData',id);
	var uid=rowData.uid;
	layer.open({
	    type: 2,
	    title: '软件系统', 
	    fix: false,
	    shadeClose: false,
	    moveOut: true,
	    area: ['880px','550px'],
//	    area: ['65%', '85%'],
	    content: [getContextPath()+"/jsp/pages/configmgt/systemmgt/systemmgtAdd.jsp?id="+id+"&uid="+uid,'no']
	});
}

var systype;
var stype;
//编辑设置value
function setDetailValue(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		mm = rs[0].password;
		device_mm = rs[0].reserver3;
		systype = rs[0].systype;
		stype = rs[0].type;
		$("#name").val(rs[0].name);
		$("#ip").val(rs[0].ip);
		$("#username").val(rs[0].username);
		$("#reserver1").val(rs[0].reserver1);
		$("#reserver2").val(rs[0].reserver2);
		$("#port").val(rs[0].port);
  }
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/systemmgtCon/search",
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
			layer.alert("加载失败");
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