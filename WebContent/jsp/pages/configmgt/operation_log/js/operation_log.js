function initlogfunction(){
	creatlogGrid();
}

function creatlogGrid(){
	jQuery("#jqGridListlog").jqGrid({
		url:getContextPath()+"/operationlogCon/search",
		datatype:'json',
		postData : {
			'time' : $("#time_search").val(),
			'oprt_user' : $("#name_search").val(),
			'oprt_type' : $("#optType").val(),
			'type' : $("#type").val(),
			'levels' : $("#levels").val()
		},
		colModel:[
		          {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'oprt_id',name:'oprt_id',width:0,key:false,hidden:true},
				   {label:'名称',name:'module',index:'module', width:100,align:'center'},
				    {label:'操作类型',name:'oprt_type',index:'oprt_type', width:50,align:'center'},
				    {label:'操作时间',name:'time',index:'time', width:100,align:'center'},
				    {label:'操作人员',name:'oprt_user',index:'oprt_user', width:70,align:'center'},
				    {label:'操作人主机IP',name:'ip',index:'ip', width:80,align:'center'},
				    {label:'操作内容',name:'oprt_content',index:'oprt_content', width:120,align:'left'},
				    {label:'是否成功',name:'flag',index:'flag', width:50,align:'center',
				    	formatter:function(cellvalue, options, rowObject){
		                   	var indexname="";
		                    if(cellvalue == "0"){
		                    	indexname="失败";
		                    }else if(cellvalue == "1"){
		                    	indexname="成功";
		                    }else{
		                    	indexname="成功";
		                    }
		                   	return indexname;
	                    }
				    },
				    {label:'事件等级',name:'levels',index:'levels', width:50,align:'center',
				    	formatter:function(cellvalue, options, rowObject){
				    		var indexname="";
				    		if(cellvalue == "1"){
				    			indexname="轻微";
				    		}else if(cellvalue == "2"){
				    			indexname="一般";
				    		}else if(cellvalue == "3"){
				    			indexname="严重";
				    		}else{
				    			indexname="日志";
				    		}
				    		return indexname;
				    	}
				    },
				    {label:'事件类型',name:'type',index:'type', width:50,align:'center',
				    	formatter:function(cellvalue, options, rowObject){
				    		var indexname="";
				    		if(cellvalue == "0"){
				    			indexname="系统事件";
				    		}else if(cellvalue == "1"){
				    			indexname="业务事件";
				    		}else{
				    			indexname="业务事件";
				    		}
				    		return indexname;
				    	}
				    }
				],
			rownumbers: true,
//			altRows:true,
			sortname: 'time',
			sortable:true,
			loadonce:true,
			rowNum:100,//一页显示多少条
			caption:"<div style='color:#000;width:95%;'>操作日志<span style='cursor:pointer;float:right;' onclick='addrl()'> <i class='icon-plus-sign'></i>&nbsp;设置存储容量</span></div>",
			rowList:[100,200,300],//可供用户选择一页显示多少条
			sortname:'id',//初始化的时候排序的字段
			sortorder:'desc',//排序方式
			mtype:'post',//向后台请求数据的Ajax类型
			viewrecords:true,//定义是否要显示总记录数
			height:'auto',
			autowidth:true,
			pager : "#jqGridPaperListlog",
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListlog").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});

/**
 * 查询
 */
function dologSearch(){
	$.jgrid.gridUnload('#jqGridListlog');
	isOff();
}
/**
 * 重置
 */
function doClear(){
	$(".prom input").val("");
	$(".prom select").each(function(){
		$(this).find("option:first").prop("selected","selected");  
	});
}
//设置存储容量
function addrl(){
	getJWTStr();
	layer.open({
  	    type: 2,
  	    title: '存储容量', 
  	    fix: false,
  	    shadeClose: false,
  	    area: ['410px', '250px'],
//  	    area: ['30%', '40%'],
  	    content: [getContextPath()+"/jsp/pages/configmgt/operation_log/addrl.jsp",'no']
  	});
}

//获取JWT
function getJWTStr(){
	var res;
	$.ajax({
		url:getContextPath()+"/commonConLog/createJWT",
		type:"post",
		data:{},
		datatype:"json",
		async:false,
		success:function (data){
			$("#jwt").val(data);
			res = data;
		}
	});
	return res;
}
