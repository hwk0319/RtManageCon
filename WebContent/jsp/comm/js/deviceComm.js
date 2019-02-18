var sys_id;
function initDevfunction(){
	//页面加载完成之后执行
	creatDeviceGrid();
	$("#deviceSrch").click(doSearchDev);
	$("#tabs").tabs();
}

function creatDeviceGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListDev").jqGrid({
		url:getContextPath()+"/devicesCon/search",
		datatype:'json',//请求数据返回的类型
		postData : {
			'name' : $("#name_searchDev").val(),
			'in_ip' : $("#ip_searchDev").val(),
			'ids' : $("#ids",parent.document).val()
		},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'统一ID',name:'uid',index:'uid',width:110,align:'center'},
				   {label:'类型',name:'devicetypename',index:'devicetypename',width:110,align:'center'},
				   {label:'厂商',name:'factoryname',index:'factoryname',width:106,align:'center'},
				   {label:'设备型号',name:'model',index:'model',width:106,align:'center'},
				   {label:'主机名',name:'name',index:'name',width:110,align:'center'},
				   {label:'应用IP',name:'in_ip',index:'in_ip',width:110,align:'center'},
				],
		loadonce:true,
		multiselect: true,//复选框
		rownumbers: true,
//		rowNum:8,//一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		height:'205',
		autowidth:true,
		pager : "#jqGridPaperListDev",
        loadComplete: function () {  
        	if(indexType != null && typeof(indexType) != "undefined" && indexType != ""){
        		var ids=$("#jqGridListDev").jqGrid("getRowData");
        		$(ids).each(function(index,item){
        			for(var i=0;i<alarmSelects.length;i++){
        				if(item.uid == alarmSelects[i]){
        					$("#jqGridListDev").jqGrid('setSelection', item.id);
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
	var GridWidth = (width-230-20);
	$("#jqGridListDev").setGridWidth(GridWidth);
});

//重新加载页面
function doSearchDev(){
	$.jgrid.gridUnload('#jqGridListDev');
	creatDeviceGrid();
}

//保存勾选的数据
function doSaveDev() {
	//获取选中行的id
	var urltype = type;
	if(urltype=="warnlog"){
		var uids=$('#jqGridListDev').jqGrid('getGridParam','uid');
	}
	
	var ids = $('#jqGridListDev').jqGrid('getGridParam','selarrrow');
	var uidlist="";
	// 告警页面添加设备
	if(indexType != null && typeof(indexType) != "undefined" && indexType != ""){
		var rows = [];
		// 如果不选
		if(ids.length == 0){
			layer.msg("请选择设备！");
			return;
		}
	
		$(ids).each(function(index,item){
			rows.push($("#jqGridListDev").jqGrid('getRowData',item));
		});
		if(urltype == "devmoni"){
			for(var index = 0;index < ids.length; index++){
				rowData = $("#jqGridListDev").jqGrid('getRowData',ids[index]);
				//add by huangdaping,来自设备监控管理
				//将查询的值赋给上一个页面
				if(index == ids.length-1){
					uidlist+=rowData.uid;
				}else{
					uidlist+=rowData.uid+",";
				}
				$("#devuid",parent.document).val(uidlist);
				//当选择单台设备时候执行下面操作
				/*if(ids.length == 1){
					var devicename = rowData.devicetypename;
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
		var str = "";
		var idStr = "";
		var odlStr = $("#datastr",parent.document).val();
		var oldidStr = $("#ids",parent.document).val();
		if(ids.length == 0){
			layer.msg("请选择设备！");
			return;
		}
		$(ids).each(function(index,id){
			rowData = $("#jqGridListDev").jqGrid('getRowData',id);
			uidlist+=rowData.uid+",";
			//end
			var name = rowData.name;
			var in_ip = rowData.in_ip;
			var innerHtml = "";
			innerHtml += "<div id='div"+id+"' style='align: left; width: 130px; height:150px; border: solid thin #d1d5de; font-size: 13px;float:left;margin:1px;text-align:center;'>";
			innerHtml += "<a href='javaScript:void(0);' onClick='deleteDevice("+id+");'><div style='float:right;height:20px;'><img src='imgs/closexx.png' class='delImg' style='width: 20px; height:20px;'/></div></a>";
			innerHtml += "<a href='javaScript:void(0);' onClick='deviceInfo("+id+");' style='color: #928f8f;text-decoration: none;'>";
			innerHtml += "<div style='margin-top:20px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;'>";
			innerHtml += "<img src='imgs/mon/app.png' style='width:59px;height:66px;margin:10px;'/>";
			innerHtml += "</br>"+name+"<br/>"+in_ip+"";
			innerHtml += "</div>";
			innerHtml += "</a>";
			innerHtml += "</div>";
			$("#deviceInfo",parent.document).prepend(innerHtml);
			if(index == 0){
				str +="{id:'"+id+"'}";
				//获取id
				idStr += id;
			}else{
				str +=",{id:'"+id+"'}";
				//获取id
				idStr += ","+id;
			}
		})
		parent.delImg();
		if(oldidStr != ""){
			idStr += ","+oldidStr;
		}
		if(odlStr != ""){
			str +=","+odlStr;
		}
		//设备数据
		$("#datastr",parent.document).val(str);
		$("#ids",parent.document).val(idStr);
		if(typeof(saveAfter)!=="undefined"){
			saveAfter(rowData,ids,uidlist);
		}
	}
	//隐藏表单弹窗
	parent.layer.close(parent.layer.index);
  }

function show_iframe(type){
	$("iframe",window.parent.document).each(function(){
		var idname=$(this).attr("id");
		if(typeof(idname)!=="undefined" || idname!==""){
			if(idname.indexOf("layui-layer-iframe")>-1){
				if(type=="S"){
					$("#"+idname,window.parent.document).attr("src",getContextPath()+"/jsp/comm/jsp/SystemComm.jsp?type="+type,'no');
				}
			}
		}
	});
}

