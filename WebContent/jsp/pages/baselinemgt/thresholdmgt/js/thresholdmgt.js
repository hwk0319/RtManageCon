var type_arr={};
var type_arr2={};
var threshold_level="threshold_level";
var method_way="method_way";
var model_arr={};
var model_item={};
$(function(){
	type_arr=$._PubSelect("#level_search",getContextPath()+"/commonCon/search?type="+threshold_level,"[{'getKey':'value','getVal':'name'}]");
	deviceInit();
	$("#srch").click(doSearch);
	$("#reset").click(doClear);
	$("#reflash").click(doSearch);

	$("#close").click(function(){
		$("#main_list").css({display:"block"});
		$("#main_edit").css({display:"none"});
    });
});
function deviceInit(){
	var model_name="";
	var model_item_name="";
	//创建jqGrid组件
	jQuery("#jqGridList").jqGrid({
		url : getContextPath()+"/thresholdCon/search",
		datatype:'json',//请求数据返回的类型
		postData:{
			'model_item_name':$("#name_search").val(),
			'model_name':$("#model_search").val()
		},
		colModel:[
		     {
		    	 label:'编号',name:'rule_id',index:'rule_id',width:0,key:true,hidden:true, editable:true
		     },
		/*     {
		    	 label:'指标阈值名称',name:'rule_name',index:'rule_name', width:100,align:'center',
		    	 editable:true,//可编辑
                 editoptions:{size:20,maxlength:128},
                 edittype:'64',//类型 文本框
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },*/
		     {
		    	 label:'模型ID',name:'model_id',index:'model_id', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{value:model_arr,size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}
              
		     },
		     {label:'模型名称',name:'model_name',index:'model_name',width:100,align:'center',
				   	editable:true,
				   	edittype:'64',
				    editoptions:{size:20,maxlength:128,readonly:true,},
				    editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"+" <input id=\"add\" style=\"width:19px;margin-top:-3px;height:19px;border:0px;margin-left:-5px\" type=\"image\" onclick='model_index()' src='"+getContextPath()+"/imgs/add.png' /> "}
			   },
			   
		    {
		    	 label:'模型分项ID',name:'model_item_id',index:'model_item_id', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{value:model_item,size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}
              
		     },
		     {
		    	 label:'模型分项名称',name:'model_item_name',index:'model_item_name', width:100,align:'center',
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     {
		    	 label:'模型分项总分',name:'total_score',index:'total_score', width:100,align:'center',
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     {
		    	 label:'指标项ID',name:'metric_id',index:'metric_id', width:100,align:'center',
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     {
		    	 label:'指标项描述',name:'description',index:'description', width:100,align:'left',
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     {
		    	 label:'指标项总分',name:'index_total_score',index:'index_total_score', width:100,align:'center',
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'一级计算方法',name:'method1',index:'method1', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'二级计算方法',name:'method2',index:'method2', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'严重计算方法',name:'method3',index:'method3', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'一级条件值',name:'metric_value1',index:'metric_value1', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'二级条件值',name:'metric_value2',index:'metric_value2', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'严重条件值',name:'metric_value3',index:'metric_value3', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'一级扣分值',name:'deduct1',index:'deduct1', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'二级扣分值',name:'deduct2',index:'deduct2', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     
		     {
		    	 label:'严重扣分值',name:'deduct3',index:'deduct3', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     {
		    	 label:'条件',name:'status',index:'status', width:100,align:'center',hidden:true,
		    	 editable:true,//可编辑
                 edittype:'64',//类型 文本框
                 editoptions:{size:20,maxlength:128,readonly:true,},
                 editrules:{required:true}, 
                 formoptions:{elmsuffix:"<span style='color:red;font-size:16px;'>*</span>"}
		     },
		     {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
		],
		loadonce:true,
		rownumbers: true,
		rowNum:100,
		rowList:[100,200,300],
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//显示总记录数据
		caption:"<div style='width:95%;'>指标阈值" +
				"<span id='add' style='float:right;line-height:19px;cursor:pointer;'  onclick='add()'> <i  class='icon-plus-sign'  ></i>&nbsp;新增</span></div>",
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/thresholdCon/update",
		pager:"#jqGridPager",
		gridComplete: function(){
            var ids = $("#jqGridList").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
              var  pa="<a href=# title='参数' onclick='parameter("+cl+")'>参数 </a>";
              ed = "<a href=# title='编辑' onclick='add("+cl+")'>编辑 </a>";
              vi = "<a href=# title='删除' onclick=\"del(this,'"+cl+"')\">删除</a>"; 
                jQuery("#jqGridList").jqGrid('setRowData',ids[i],{act:pa+"  "+ed+"  "+vi});
            } 
        },
	});
}

function parameter(rule_id){
	layer.open({
	    type: 2,
	    title: '参数', 
	    fix: false,
	    shadeClose: false,
	    moveOut: true,
	    area: ["600px","360px"],
	    content: [getContextPath()+"/jsp/pages/baselinemgt/thresholdmgt/parameter.jsp?rule_id="+rule_id]
	});
}
function del(obj,cl){
	var rowData = $("#jqGridList").jqGrid('getRowData',cl);
	var status=rowData.status;
	if(status==1){
		layer.msg("正在评分中，无法删除！");
	}else{
		//删除
		layer.confirm("确定删除这条数据？",{
			btn:['确定','取消']
		},function(){
			$.ajax({
		  		url : getContextPath()+"/thresholdCon/update?oper=del",
		  		type : "post",
		  		dataType : 'json',
		  		async: false ,
		  		data : {
		  			"id" : cl
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
/*function afterShowForm(){
	var model_id =$("#model_id").val();
	var status;
	$.ajax({
		url:getContextPath()+"/thresholdCon/searchstatus",
		type:"post",
		async:false,
		data:{"model_id":model_id},
		success:function(list){
			 status=list[0].status;
			 layer.alert(status);
		}
	})
	if(status==2){
		$("#TblGrid_jqGridList").before("<div style=\"text-align: center;color: red;\">评分中，无法修改！</div>")
		$("#add").css("display","none");
		$("#TblGrid_jqGridList #rule_name").attr("readonly","true");
		$("#TblGrid_jqGridList #metric_value").attr("readonly","true");
		$("#TblGrid_jqGridList #method").attr("disabled","true");
		$("#TblGrid_jqGridList #warn_level").attr("disabled","true");
	}
}*/
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridList").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});

//重新加载页面
function doSearch(){
	$.jgrid.gridUnload('#jqGridList');
	deviceInit();
}
//新增
function add(rule_id){
	if(rule_id!=""){
		var rowData = $("#jqGridList").jqGrid('getRowData',rule_id);
		var rule_name=rowData.rule_name;
		var model_id=rowData.model_id;;
		var model_item_id=rowData.model_item_id;;
		var index_id=rowData.metric_id;;
		var method1=rowData.method1;
	  	var method2=rowData.method2;
	  	var method3=rowData.method3;
	  	var metric_value1=rowData.metric_value1;
	  	var metric_value2=rowData.metric_value2;
	  	var metric_value3=rowData.metric_value3;
	  	var deduct1=rowData.deduct1;
	  	var deduct2=rowData.deduct2;
	  	var deduct3=rowData.deduct3;
	  	var status= rowData.status;
	  	if(status=='1'){
	  		layer.alert("评分中，无法修改");
	  		return;
	  	}
	}
		
	var wid=$("#tt",parent.document).width();
	var hei=$("#tt",parent.document).height();
	layer.open({
		    type: 2,
		    title: '指标阈值', 
		    fix: false,
		    shadeClose: false,
		    moveOut: true,
		    area: ["900px","520px"],
//		    area: [wid+"px",hei+"px"],
	  	    content: [getContextPath()+"/jsp/pages/baselinemgt/thresholdmgt/model_indexAdd.jsp?model_id="+model_id
	  	          +"&model_item_id="+model_item_id
  	              +"&index_id="+index_id
  	              +"&method1="+method1
  	              +"&method2="+method2
  	              +"&method3="+method3
  	              +"&metric_value1="+metric_value1
  	              +"&metric_value2="+metric_value2
  	              +"&metric_value3="+metric_value3
			  	  +"&deduct1="+deduct1
			      +"&deduct2="+deduct2
			  	  +"&deduct3="+deduct3
			  	  +"&rule_id="+rule_id
			  	  +"&rule_name="+rule_name
  	              ]

  	});

}
/*** 查看 ***/
function doView(id){
	layer.alert(id);
}
/*** 重置 ***/
function doClear(ids){
	$(".prom input").val('');
}
/*** 删除 ***/
function doDel(ids){
	$.jgrid.gridUnload('#jqGridList');
	doSearch();
}
