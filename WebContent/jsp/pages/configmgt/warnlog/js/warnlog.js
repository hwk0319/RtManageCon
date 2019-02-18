var level_arr={};
function initWarnlogfunction(){
	level_arr=$._PubSelect("",getContextPath()+"/commonCon/search?type=warn_level","[{'getKey':'value','getVal':'name'}]");
	creatWarnlogGrid();
}
function creatWarnlogGrid(){
	var device_name=$("#device_name_srch").val();
	var process_status=$("#process_status_srch").val();
	var warn_timeS=$("#startTime").val();
	var warn_timeE=$("#endTime").val();
	var device_id=$("#comm_menu .5001").attr("search");
	jQuery("#jqGridListWarnlog").jqGrid({
		url:getContextPath()+"/WarnlogCon/search",
		datatype:'json',
		postData:{
					"device_id" : device_id,
					"device_name" : device_name,
					"process_status" : process_status,
					"warn_timeS" : warn_timeS,
					"warn_timeE" : warn_timeE
			},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true,},
				   {label:'名称',name:'device_name',index:'device_name',width:80,align:'center'},
				   {label:'故障部位',name:'warn_part',index:'warn_part',width:80,align:'center'},
				   {label:'告警等级',name:'warn_level',index:'warn_level',width:50,align:'center',
					   editable:true,
	                    edittype:'select',
	                    editoptions:{value:level_arr},
	                    formatter:function(cellvalue, options,rowObject){
		                   	var indexname="";
		                    for (key in level_arr){
		                    	if(cellvalue==key){
		                    		indexname=level_arr[key];
									break;
								}
		                    }
		                   	return indexname;
	                    }
				   },
				   {label: '初次发生日期',name : 'warn_time',width:100,align:"center",},
				   {label:'处理状态',name:'process_status',index:'process_status',width:0,hidden:true},
				   {label:'处理状态',name:'process_status',index:'process_status_ms',width:60,align:'center',
					   formatter:function(cellvalue, options, rowObject){
		                   	 var indexname="";
		                   	 switch(cellvalue){
		                   		case 0:indexname='新报';break;
		                   		case 1:indexname='已确认';break;
		                   		case 2:indexname='已消除';break;
		                   		case 3:indexname='已忽略';break;
		                   		}
		                   	 return indexname;
		                 }
				   },
				   {label:'是否通知',name:'isnoticed',index:'isnoticed',width:60,align:'center',
					    formatter:function(cellvalue, options, rowObject){
		                   	 var indexname="";
		                   	 switch(cellvalue){
		                   		case true:indexname='已通知';break;
		                   		case false:indexname='未通知';break;
		                   		}
		                   	 return indexname;
		                 }
				   },
				   {label:'最新发生日期',name:'newest_warntime',index:'newest_warntime',width:100,align:'center'},
				   {label:'发生次数',name:'occur_times',index:'occur_times',width:100,align:'center'},
				   {label:'详细信息',name:'warn_info',index:'warn_info',width:180,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
				rownumbers: true,
//				altRows:true,
				rowNum:100,//一页显示多少条
				rowList:[100,200,300],//可供用户选择一页显示多少条
				sortname:'id',//初始化的时候排序的字段
				sortorder:'desc',//排序方式
				mtype:'post',//向后台请求数据的Ajax类型
				viewrecords:true,//定义是否要显示总记录数
				multiselect: false,//复选框
				caption:"<div style='width:95%;'>异常故障<span style='cursor:pointer;float:right;float:right;'></span></div>",
				height:'auto',
				autowidth:true,
				editurl:"",
				pager : "#jqGridPaperListWarnlog",
				gridComplete: function(){
		            var ids = $("#jqGridListWarnlog").getDataIDs();
		            for(var i=0;i<ids.length;i++){
		                var cl = ids[i];
		                var rowData = $("#jqGridListWarnlog").jqGrid("getRowData",cl);
		                var status = rowData.process_status;
		                if(status == "新报"){
		                	ok = "<a title='确认' style=\"cursor:pointer;\" onclick=\"doSolve(this,'1')\">确认</a>";
		                	cl = "<a title='消除' style=\"cursor:pointer;\" onclick=\"doSolve(this,'2')\">消除</a>"; 
		                	hi = "<a title='忽略' style=\"cursor:pointer;\" onclick=\"doSolve(this,'3')\">忽略</a>"; 
		                	jQuery("#jqGridListWarnlog").jqGrid('setRowData',ids[i],{act:ok+" "+cl+" "+hi});
		                }else if(status == "已确认"){
		                	jQuery("#jqGridListWarnlog").jqGrid('setRowData',ids[i],{act:"已确认"});
		                }else if(status == "已消除"){
		                	jQuery("#jqGridListWarnlog").jqGrid('setRowData',ids[i],{act:"已消除"});
		                }else if(status == "已忽略"){
		                	jQuery("#jqGridListWarnlog").jqGrid('setRowData',ids[i],{act:"已忽略"});
		                }
		            } 
		        },
	});
	$("#comm_menu .5001").attr("search","")
}
//查询
function doSearchWarnlog(){
	$.jgrid.gridUnload('#jqGridListWarnlog');
	creatWarnlogGrid("");
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListWarnlog").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
/**
 * 更新状态
 * @param status
 */
function doSolve(obj,status){
	var selRow=$(obj).parent().parent();
	var status_index=$("#gview_jqGridListWarnlog table thead #jqGridListWarnlog_process_status:eq(0)").index();
	var process_status=$(selRow).find("td:eq("+status_index+")").html();
	var id=$(selRow).find("td[aria-describedby='jqGridListWarnlog_id']").html();
	if (!(process_status=="0")){
		layer.alert("非新报状态不能处理！");
		return;
	}
	
	var type = "";
	if(status == 1){
		type = "确认";
	}else if(status == 2){
		type = "消除";
	}else if(status == 3){
		type = "忽略";
	}
	
	layer.confirm("确定"+type+"这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
			url:getContextPath()+"/WarnlogCon/update",
			type:"post",
			data:{
				"id" : id,
				"process_status":status,
			},
			success:function(result){
				if(result ==1){
					layer.msg("已"+type+" !");
					doSearchWarnlog();
					//主页获取最新告警信息
					window.parent.getWarnLog();
				}else{
					layer.alert("处理失败！");
				}
			}
		});
	});
}
