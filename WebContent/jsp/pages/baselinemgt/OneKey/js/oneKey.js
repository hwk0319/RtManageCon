function initDevfunction(){
	//页面加载完成之后执行
	creatDeviceGrid();
	$("#srch").click(doSearch);
}

function creatDeviceGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListHea").jqGrid({
		url:getContextPath()+"/heathcheckCon/search",
		datatype:'json',//请求数据返回的类型
		postData : {
			'system_name' : $("#healthid_search").val(),
			'model_name' : $("#modelname_search").val()
		},
		colModel:[
				   {label:'id',name:'health_check_id',width:0,key:true,hidden:true},
				   {label:'评估目标ID',name:'target_id',index:'target_id',width:70,align:'center'},
				   {label:'评估目标名称',name:'system_name',index:'system_name',width:80,align:'center'},
//				   {label:'模型ID',name:'model_id',index:'model_id',width:100,align:'center'},
				   {label:'模型名称',name:'model_name',index:'model_name',width:80,align:'center'},
				   {label:'评估起点时间',name:'begintime',index:'begintime',width:80,align:'center'},
				   {label:'评估截止时间',name:'endtime',index:'endtime',width:80,align:'center'},
				   {label:'周期',name:'cron',index:'cron',width:80,align:'center'},
				   {label:'总分',name:'totalScore',index:'totalScore',width:50,align:'center'},
				   {label:'评估得分',name:'total_score',index:'total_score',width:50,align:'center'},
				   {label:'状态',name:'status',index:'status',width:80,align:'center',
					   formatter:function(cellvalue, options, rowObject){
		                   	 var indexname;
		                   	 switch(cellvalue){
		                   	 	case null:indexname='';break;
			                   	case 0:indexname='等待开始';break;
		                   		case 1:indexname='正在进行评估';break;
		                   		case 2:indexname='评估完成';break;
		                   		}
		                   	 return indexname;
		               }
				   },
				   {label:'描述',name:'description',index:'description',width:80,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:200,align:'center'}
				],
		loadonce:true,
		rownumbers: true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'health_check_id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		caption:"<div style='color:#000;'>健康评分<span  id='heathAdd'><i class='icon-plus-sign' onclick='doInsert()' style='margin-left:88%;cursor:pointer;'></i>&nbsp;新增</span></div>",
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/heathcheckCon/update",//编辑地址
		pager : "#jqGridPaperListHea",
		gridComplete: function(){
            var ids = $("#jqGridListHea").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                JY = "<a href=# title='优化建议' onclick='doAdvice("+ids[i]+");'>优化建议 </a>";
                vw = "<a href=# title='详情' onclick='doView("+ids[i]+");'>详情 </a>";
                pf = "<a href=# title='评分' onclick='doScore("+ids[i]+");'>评分 </a>";
                sz = "<a href=# title='停止' onclick='stopScore("+ids[i]+");'>停止 </a>";
                ed = "<a href=# title='编辑' onclick='doInsert("+ids[i]+");'>编辑 </a>";
                vi = "<a href=# title='删除' onclick='doDel("+ids[i]+");'>删除</a>"; 
                jQuery("#jqGridListHea").jqGrid('setRowData',ids[i],{act:JY+" "+vw+" "+pf+" "+sz+" "+ed+" "+vi});
            } 
        }
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-230-20)*0.96;
	$("#jqGridListHea").setGridWidth(GridWidth);
});

//重新加载页面
function doSearch(){
	$.jgrid.gridUnload('#jqGridListHea');
	creatDeviceGrid();
}

/*** 删除 ***/
function doDel(id){
	var rs = load(id);
	if(rs[0].status == 1){
		layer.msg("正在进行评估，不能删除！");
		return;
	}else{
		layer.confirm("确定删除这条数据？",{
			btn:['确定','取消']
		},function(){
			$.ajax({
		  		url : getContextPath()+"/heathcheckCon/update?oper=del",
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
}
//优化建议
function doAdvice(id){
	var rs = load(id);
	//判断是否已经进行评估过
	if(rs[0].total_score == "" || rs[0].total_score == null){
		layer.msg("未进行评估，请先开始评估！");
		return;
	}
	layer.open({
	    type: 2,
	    title: '优化建议', 
	    id:"heathAdvice",
	    shadeClose: false,
	    area: ['80%','95%'],
	    zIndex: 19891011,
	    moveOut: true,
	    content: [getContextPath()+"/jsp/pages/baselinemgt/healthScore/heathAdvice.jsp?id="+id,'no']
	});
}
//详情
function doView(id){
	layer.open({
	    type: 2,
	    title: '健康评分详情', 
	    id:"heathView",
	    shadeClose: false,
	    area: ['747px','450px'],
	    zIndex: 19891011,
	    moveOut: true,
	    content: [getContextPath()+"/jsp/pages/baselinemgt/healthScore/heathView.jsp?id="+id,'no']
	});
}
//评分
function doScore(id){
	var rs = load(id);
	if(rs[0].status == 1){
		layer.msg("正在进行评估！");
		return;
	}else{
		layer.confirm("确定开始评估吗？",{
			btn:['确定','取消']
		},function(){
			$.ajax({
				url : getContextPath() + "/heathcheckCon/doScore",
				type : "post",
				async : false,
				data : {
					"health_check_id" : id
				},
				dataType : "text",
				success : function(result) {
					if(result != null){
						layer.msg("正在进行评估中！");
						doSearch();
					}
				},
				error : function() {
					layer.alert("加载失败！");
					doSearch();
				}
			});
		});
	}
}

//停止
function stopScore(id){
	var rowData = $("#jqGridListHea").jqGrid('getRowData',id);
	var rs = load(id);
	if(rs[0].status == 0){
		layer.msg("评估未开始！");
		return;
	}else if(rs[0].status == 1){
		layer.confirm("确定停止评估吗？",{
			btn:['确定','取消']
		},function(){
			$.ajax({
				url : getContextPath() + "/heathcheckCon/stopScore",
				type : "post",
				async : false,
				data : {
					"health_check_id" : id
				},
				dataType : "json",
				success : function(result) {
					layer.msg("已停止评估！");
					doSearch();
				},
				error : function() {
					layer.alert("加载失败！");
					doSearch();
				}
			});
		});
	}
}

//添加
function doInsert(id){
	//判断编辑还是新增
	if(id != undefined){
		var rs = load(id);
		if(rs[0].status == 1){
			alert("正在进行评估，不能编辑！");
			return;
		}
	}
	layer.open({
	    type: 2,
	    title: '健康评分', 
	    id:"heathScore",
	    shadeClose: false,
	    area: ['55%','83%'],
	    zIndex: 19891012,
	    moveOut: true,
	    content: [getContextPath()+"/jsp/pages/baselinemgt/healthScore/heathscoreAdd.jsp?id="+id,'no']
	});
}

var type;
//编辑设置value
function setDetailValue(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		$("#health_check_id").val(rs[0].health_check_id);
		$("#target_id").val(rs[0].target_id);
		$("#model_id").val(rs[0].model_id);
		$("#model_name").val(rs[0].model_name);
		if(rs[0].begintime != "" && rs[0].begintime != null){
			$("#begin_time").val(rs[0].begintime);
			$("#end_time").val(rs[0].endtime);
			type = "1";
		}else{
			$("#cron").val(rs[0].cron);
			type = "2";
		}
		$("#description").val(rs[0].description);
    }
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/heathcheckCon/search",
		type : "post",
		async : false,
		data : {
			"health_check_id" : id
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

//告警详情
function creatHeathDevGrid(id){
	//创建jqGrid组件
	jQuery("#jqGridListHeaDet").jqGrid({
		url:getContextPath()+"/heathcheckCon/searchDetalByCheckId",
		datatype:'json',//请求数据返回的类型
		postData : {
			'health_check_id' : id
		},
		colModel:[
				   {label:'id',name:'health_check_id',width:0,hidden:true},
//				   {label:'评估目标名称',name:'system_name',index:'system_name',width:100,align:'center'},
				   {label:'模型名称',name:'model_name',index:'model_name',width:80,align:'center'},
				   {label:'模型分项名称',name:'model_item_name',index:'model_item_name',width:80,align:'center'},
				   {label:'指标项ID',name:'metric_id',index:'metric_id',width:70,align:'center'},
				   {label:'指标项描述',name:'mon_indexname',index:'mon_indexname',width:100,align:'center'},
//				   {label:'指标重要性等级',name:'severity',index:'severity',width:100,align:'center'},
				   {label:'扣分值',name:'deduct',index:'deduct',width:40,align:'center'},
				   {label:'评分时间',name:'record_time',index:'record_time',width:115,align:'center'},
				   {label:'指标值',name:'metric_value',index:'metric_value',width:50,align:'center'}
				],
		loadonce:true,
		rownumbers: true,
//		rowNum:10,//一页显示多少条
		sortname:'health_check_id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		height:'300',
		autowidth:true,
		pager : "#jqGridPaperListHeaDet",
	});
}
