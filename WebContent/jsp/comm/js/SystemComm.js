var type_arr={};
var sysType="";
function initSysfunction(){
	creatSysGrid()
	//页面加载完成之后执行
	$("body").on("click","#sysSrch",function(e){
		doSearchSys();
	});
	$("#tabs").tabs();
}

function creatSysGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListSys").jqGrid({
		url:getContextPath()+"/systemmgtCon/search",
		datatype:'json',//请求数据返回的类型
		postData : {
			'systemName' : $("#name_searchSys").val(),
			'systemIp' : $("#ip_searchSys").val()
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
	                   		case 5:indexname='web容器';sysType="web_type";break;
	                   		}
	                   	 return indexname;
	                    }
				   },
				   {label:'统一ID',name:'uid',index:'uid',width:90,align:'center'},
				   {label:'具体类型',name:'type',index:'type',width:90,align:'center'},
				   {label:'名称',name:'name',index:'name',width:90,align:'center'},
				   {label:'管理IP',name:'ip',index:'ip',width:100,align:'center'},
				],
		loadonce:true,
		loadonce:true,
		multiselect: true,
		rownumbers: true,
//		rowNum:8,//一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: true,//复选框
		height:'205',
		autowidth:true,
		pager : "#jqGridPaperListSys",
		loadComplete: function () {  
        	if(indexType != null && typeof(indexType) != "undefined" && indexType != ""){
        		var ids=$("#jqGridListSys").jqGrid("getRowData");
        		$(ids).each(function(index,item){
        			for(var i=0;i<alarmSelects.length;i++){
        				if(item.uid == alarmSelects[i]){
        					$("#jqGridListSys").jqGrid('setSelection', item.id);
        					break;
        				}
        			}
        		});
        	}
        }  
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
	creatSysGrid('');
}

function getTypeinfo(obj,idname){
	var type="";
	var systype=$(obj).val();
	if(systype==1){
		type="db_type";
	}else if(systype==2){
		type="file_type";
	}else if(systype==3){
		type="backups_type";
	}else if(systype==4){
		type="DB_Respool";
	}
	$(idname).html();
	if(systype!==""){
		$._PubSelect($(idname),getContextPath()+"/commonCon/searchSysType?type="+type,"[{'getKey':'value,type','getVal':'name'}]");
	}
}

//保存勾选的数据
function doSaveSys() {
	//获取选中行的id
	var urltype = type;
	var ids=$('#jqGridListSys').jqGrid('getGridParam','selarrrow');
	var uidlist="";
	// 告警页面添加设备
	if(indexType != null && typeof(indexType) != "undefined" && indexType != ""){
		var rows = [];
		// 如果不选，则全选
		if(ids.length == 0){
			layer.msg("请选择软件！");
			return;
		}
		$(ids).each(function(index,item){
			rows.push($("#jqGridListSys").jqGrid('getRowData',item));
		});
		if(urltype == "devmoni"){
			for(var index = 0;index < ids.length; index++){
				rowData = $("#jqGridListSys").jqGrid('getRowData',ids[index]);
				//将查询的值赋给上一个页面
				if(index == ids.length-1){
					uidlist+=rowData.uid;
				}else{
					uidlist+=rowData.uid+",";
				}
				$("#devuid",parent.document).val(uidlist);
				//当选择单台设备时候执行下面操作
				/*if(ids.length == 1){
					var devicename = rowData.systype;
					var dname = rowData.name;
					$(".dtype",parent.document).css({"display":"block"});
					$(".dname",parent.document).css({"display":"block"});
					$("#dtype",parent.document).val(devicename);
					$("#dname",parent.document).val(dname);
				}else if(ids.length>1){
					$(".dtype",parent.document).css({"display":"none"});
					$(".dname",parent.document).css({"display":"none"});
				}*/
			}
			parent.updateSelect();
		}else{
			parent.backFun(rows);
		}
	}else{
		var uidlist="";
		$(ids).each(function(index,id){
			uidlist+=rowData.uid+",";
		})
		if(typeof(saveAfterSys)!=="undefined"){
			saveAfterSys(rowData,ids,uidlist);
		}
	}
	//隐藏表单弹窗
	parent.layer.close(parent.layer.index);
  }


function show_iframe(type){
	$("iframe",window.parent.document).each(function(){
		var montype = $.getUrlParam('type');
		var idname=$(this).attr("id");
		if(idname.indexOf("layui-layer-iframe")>-1){
			if(type=="D"){
				$("#"+idname,window.parent.document).attr("src",getContextPath()+"/jsp/comm/jsp/deviceComm.jsp?type="+montype,'no');
			}
		}
	});
}
