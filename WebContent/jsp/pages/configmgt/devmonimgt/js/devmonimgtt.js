var indextype_arr={},uid_arr={}//指标分类
cname_arr={};//采集方式
var type_arr={};
var wid=0;
var rowData;
function initDevmonfunction(){
	indextype_arr=$._PubSelect($("#indextype_id"),getContextPath()+"/commonCon/searchIndexType","[{'getKey':'indextype_id','getVal':'name'}]");
	type_arr=$._PubSelect("",getContextPath()+"/devmonimgtCon/searchUidBytype","[{'isedHtml':'false','retType':'arr','getKey':'uid','getVal':'uid'}]");
	uid_arr=$._PubSelect("",getContextPath()+"/devmonimgtCon/searchUid","[{'getKey':'uid','getVal':'name'}]");
	cname_arr={"0":"--请选择--","1":"集中","2":"单独"};
	creatDevmonGrid();
	$("body").on("click","#devmonSrch",function(e){
		doSearchDevmon();
	});
	wid=$("#tt").width();
}
function creatDevmonGrid(){
	jQuery("#jqGridListDevmon").jqGrid({
		url:getContextPath()+"/devmonimgtCon/search",
		datatype:'json',
		postData:{
			'indextype_id':$("#indextype_id").val(),
			'device_name': $("#device_name").val(),
			'type':$("#type").val()
		},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'type',name:'type',width:0,hidden:true},
				   {label:'UID',name:'uid',index:'uid',width:100,align:'center'},
				   {label:'名称',name:'device_name',index:'device_name',width:100,align:'center'},
				   {label:'类型',name:'devicetype',index:'devicetype',width:100,align:'center'},
				   {label:'指标分类ID',name:'indextype_id',index:'indextype_id',width:100,align:'center'},
				   {label:'指标分类',name:'indextype_id',index:'indextype_id',width:100,align:'center',
					   	formatter:function(cellvalue, options, rowObject){
		                   	var indexname="";
		                    for (key in indextype_arr){
		                    	if(cellvalue==key){
									indexname=indextype_arr[key];
									break;
								}
		                    }
		                   	return indexname;
	                    }
				   },
				   {label:'采集方式',name:'collect',index:'collect',width:100,align:'center',
					   formatter:function(cellvalue, options, rowObject){
		                   	var indexname="";
		                    for (key in cname_arr){
		                    	if(cellvalue==key){
									indexname=cname_arr[key];
									break;
								}
		                    }
		                   	return indexname;
	                    }
				   },
				   {label:'周期',name:'period',index:'period',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
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
		caption:"<div style='overflow:hidden;height:19px;color:#000;width:95%;'>监控管理" +
				"<span id='devmonAdd' style='margin-right:25px;float:right;line-height:19px;cursor:pointer;float:right;' onclick='devmonAdd()'><i class='icon-plus-sign' style='line-height:19px;'></i>&nbsp;新增</span>" +
				"<span style='margin-right:10px;float:right;line-height:19px;cursor:pointer;float:right;' onclick='writeDB()'> <i class='icon-download' style='line-height:19px;'></i>&nbsp;下载</span>"+//表格的标题名字
				"<span style='margin-right:10px;margin-top:2px;color:#000;float:right;line-height:9px;cursor:pointer;float:right;' onclick='writeZK()'> <i class='icon-upload ' style='line-height:19px;'></i>&nbsp;上传</span></div>",
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/devmonimgtCon/update",
		pager : "#jqGridPaperListDevmon",
		gridComplete: function(){
            var ids = $("#jqGridListDevmon").getDataIDs();
            for(var i=0;i<ids.length;i++){
            	 var cl = ids[i];
            	 ed = "<a href=# title='编辑' style='text-decoration: none;' onclick='devmonAdd("+ids[i]+");'>编辑 </a>";
                 vi = "<a href=# title='删除' style='text-decoration: none;' onclick='doDelete("+ids[i]+");'>删除 </a>";
	             jQuery("#jqGridListDevmon").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            } 
        },
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListDevmon").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//重新加载页面
function doSearchDevmon(){
	$.jgrid.gridUnload('#jqGridListDevmon');
	creatDevmonGrid();
}

function afterShowForm(){
	var uid=$("#uid").val();
	var type=$("#type").val();
	var parobj=$("iframe",window.parent.document).parent().parent().parent();
	if(type=="D"){
		 var arr="";
		$.ajax({
			url:getContextPath()+"/devicesCon/search",
			type:'POST',
			data:{"uid":uid},
			dataType:'json',
			success:function(data){
				for(var i=0;i<data.length;i++)
				{
					arr+="<tr class=\"add_class\" rowpos=\"4\" class=\"FormData\" id=\"tr_indextype_id\">";
					arr+="<td class=\"CaptionTD\"><label>类型</label></td>";
					arr+="<td class=\"DataTD\">&nbsp;<input type=\"text\" value=\""+data[i].devicetype+"\" readonly=\"true\" size=\"20\" maxlength=\"64\"  style=\"background: #f0f0f0\" class=\"FormElement ui-widget-content ui-corner-all\"></td>";
					arr+="</tr>";
					arr+="<tr class=\"add_class\" rowpos=\"4\" class=\"FormData\" id=\"tr_indextype_id\">";
					arr+="<td class=\"CaptionTD\"><label>主机名</label></td>";
					arr+="<td class=\"DataTD\">&nbsp;<input type=\"text\" value=\""+data[i].devicetypename+"\" readonly=\"true\" size=\"20\" maxlength=\"64\" style=\"background: #f0f0f0\" class=\"FormElement ui-widget-content ui-corner-all\"></td>";
					arr+="</tr>";
				}
				$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #tr_indextype_id").before(arr);
			}
		});
	}
	if(type=="S"){
		var arr="";
		$.ajax({
			url:getContextPath()+"/systemmgtCon/search",
			type:'POST',
			data:{"uid":uid},
			dataType:'json',
			success:function(data){
				for(var i=0;i<data.length;i++)
				{
					arr+="<tr class=\"add_class\" rowpos=\"4\" class=\"FormData\" id=\"tr_indextype_id\">";
					arr+="<td class=\"CaptionTD\"><label>名称</label></td>";
					arr+="<td class=\"DataTD\">&nbsp;<input type=\"text\" value=\""+data[i].name+"\" readonly=\"true\" size=\"20\" maxlength=\"64\" style=\"background: #f0f0f0\" class=\"FormElement ui-widget-content ui-corner-all\"></td>";
					arr+="</tr>";
				}
				
				$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #tr_indextype_id").before(arr);
				
			}
		});
	}

	$("#abc").css("display","none");
	$("#FrmGrid_jqGridListDevmon").append($("#cron_iframe").html());
	$("#TblGrid_jqGridListDevmon #type").css({background:"#F0F0F0"}).attr("disabled","true");
	$("#TblGrid_jqGridListDevmon #uid").css({background:"#F0F0F0"}).attr("disabled","true");
	$("#TblGrid_jqGridListDevmon #cronExpress").css({background:"#F0F0F0"}).attr("disabled","true");
}
/**
 * 下载
 */
 function writeDB(){
   	  $.ajax({
	  		url : getContextPath()+"/devmonimgtCon/zkdownload",
	  		type : "post",
	  		data : {isBol:"Y"},
	  		success : function(result) {
		  		if(result.indexOf("&TAB&")<0){
		  			layer.confirm(result,{
		  				btn:['确定','取消']
		  			},function(){
		  				var index;
		  				$.ajax({
	   		   		  		url : getContextPath()+"/devmonimgtCon/zkdownload",
	   		   		  		type : "post",
	   		   		  		data : {isBol:"N"},
			   		   		beforeSend: function () {
			   	      			index = layer.load(1, {
			   	      			  shade: [0.3,'#fff']
			   	      			});
			   	      	    },
			   	      	    complete: function () {
			   	      	    	layer.close(index);
			   	    	    },
			   		   		success : function(data) {
			   		   			layer.msg(data);
				   		   		window.parent.doSearchDevmon();
			   		   		}
	   		  		 	});
		  			});
		  		}else{
		  			layer.alert(result.replace("&TAB&",""));
		  		}
	  		},
	  		error : function() {
	  			layer.alert("下载失败！");
	  		}
  	});
	 
 }
 /**
  * 上传
  */
function writeZK(){
	 var ids = $("#jqGridListDevmon").jqGrid('getDataIDs');
	 var uid="";
	 var index;
	 $(ids).each(function(index,id){
			rowData = $("#jqGridListDevmon").jqGrid('getRowData',id);
			uid+=rowData.uid+",";
	  	 });
	 if(ids.length==0){
		 layer.confirm("确定清空Zookeeper节点？",{
				btn:['确定','取消']
			},function(){
				$.ajax({
	   		  		url : getContextPath()+"/devmonimgtCon/zkupload",
	   		  		type : "post",
	   		  		data : {
	   		  			"uid" : uid,
	   		  		},
		   		  	beforeSend: function () {
		      			index = layer.load(1, {
		      			  shade: [0.3,'#fff']
		      			});
		      	    },
		      	    complete: function () {
		      	    	layer.close(index);
		    	    },
	   		  		success : function(result) {
	   		  			layer.msg(result);
	   		  		},
	   		  		error : function() {
	   		  			layer.msg("上传失败！");
	   		  		}
	   		  	});
		 });
	 }else{
		 layer.confirm("确定将 "+ids.length+" 条数据上传至Zookeeper？",{
				btn:['确定','取消']
			},function(){
				$.ajax({
    		  		url : getContextPath()+"/devmonimgtCon/zkupload",
    		  		type : "post",
    		  		data : {
    		  			"uid" : uid,
    		  		},
    		  		beforeSend: function () {
		      			index = layer.load(1, {
		      			  shade: [0.3,'#fff']
		      			});
		      	    },
		      	    complete: function () {
		      	    	layer.close(index);
		    	    },
    		  		success : function(result) {
    		  			layer.msg(result);
    		  		},
    		  		error : function() {
    		  			layer.msg("上传失败！");
    		  		}
    		  	});
		 });
	 }
}

/**
 * 新增
 */
function devmonAdd(ids){
	getJWT();
	layer.open({
        type: 2,
        title: '设备监控',
        fix: false,
        shadeClose: false,
        moveOut: true,
        area: ['880px','550px'],
//        area: ['60%', '87%'],
        content: [getContextPath()+"/jsp/pages/configmgt/devmonimgt/devmoniAdd.jsp?id="+ids,'no']
    });
}
/**
 * 删除
 */
function doDelete(ids){
	var jwt = getJWT();
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		parent.layer.closeAll();
		var index;
		//先调用相关接口判断是否被占用
		$.ajax({
	  		url : getContextPath()+"/devmonimgtCon/update",
	  		type : "post",
	  		dataType : 'json',
	  		data : {
	  			"oper" : "del",
	  			"id" : ids,
	  			"jwt" : jwt
	  		},
	  		beforeSend: function () {
      			index = layer.load(1, {
      			  shade: [0.3,'#fff']
      			});
      	    },
      	    complete: function () {
      	    	layer.close(index);
    	    },
	  		success : function(result) {
	  			layer.msg('删除成功！');
	  	   		doSearchDevmon();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	})
}

/**
 * 保存前
 */
var fn_beforeSubmit=function(postdata,formid){
	var id=postdata["id"];
	var uid=postdata["uid"];
	var indextype_id=postdata["indextype_id"];
	var mess="";
    $.ajax({
    	type:"post",
    	url:getContextPath()+"/devmonimgtCon/checkDevIndex?id="+id+"&uid="+uid+"&indextype_id="+indextype_id,
    	async:false,
    	success:function(res){
    		mess=res;
    	}
    });
    if(mess===""){
    	return [true,""];
	}else{
		alert(mess);
		return [false,mess];
	}
}

function getTypeinfo(obj){
	var type=$(obj).val();
	$(idname).html();
/*	if(systype!==""){
		$._PubSelect($(idname),getContextPath()+"/commonCon/searchSysType?type="+type,"[{'getKey':'value,type','getVal':'name'}]");
	}*/
}

/**
 * 保存后
 */
var fn_afterSubmit=function(response,postdata){
	return [true,""];
}
/**
 * 设备添加
 * 
 */
function saveAfter(rowData,ids,uidlist){
	var parobj=$("iframe",window.parent.document).parent().parent().parent();
	if(ids.length>1){
		var type="D";
		$(parobj).find("#TblGrid_jqGridListDevmon .add_class").remove();
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #type").val(type);
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #uid").val(uidlist);
	}else{	
		var val=rowData.uid;
		var devicetype=rowData.devicetypename;
		var name = rowData.name
		var type="D";
		var arr="";
		arr+="<tr class=\"add_class\" rowpos=\"4\" class=\"FormData\" id=\"tr_indextype_id\">";
		arr+="<td class=\"CaptionTD\"><label>类型</label></td>";
		arr+="<td class=\"DataTD\">&nbsp;<input type=\"text\" value=\""+devicetype+"\" readonly=\"true\" size=\"20\" maxlength=\"64\" class=\"FormElement ui-widget-content ui-corner-all\"></td>";
		arr+="</tr>";
		arr+="<tr class=\"add_class\" rowpos=\"4\" class=\"FormData\" id=\"tr_indextype_id\">";
		arr+="<td class=\"CaptionTD\"><label>主机名</label></td>";
		arr+="<td class=\"DataTD\">&nbsp;<input type=\"text\" value=\""+name+"\" readonly=\"true\" size=\"20\" maxlength=\"64\" class=\"FormElement ui-widget-content ui-corner-all\"></td>";
		arr+="</tr>";
		$(parobj).find("#TblGrid_jqGridListDevmon .add_class").remove();
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #uid").val(val);
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #type").val(type);
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #tr_indextype_id").before(arr);
	}
	var uid = uidlist.split(",")[0];
	indextype_arr = $._PubSelect($(parobj).find("#indextype_id"),getContextPath()+"/commonCon/searchIndexType?uid="+uid,"[{'isedHtml':'true','getKey':'indextype_id','getVal':'name'}]");
}
/**
 * 软件添加
 */
function saveAfterSys(rowData,ids,uidlist){
	var parobj=$("iframe",window.parent.document).parent().parent().parent();
	if(ids.length>1){
		var type="S";
		$(parobj).find("#TblGrid_jqGridListDevmon .add_class").remove();
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #uid").val(uidlist);
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #type").val(type);
	}else{	
		var uid=rowData.uid;
		var name=rowData.name;
		var type="S";
		var arr="";
		$("#TblGrid_jqGridListDevmon .add_class").remove();
		arr+="<tr class=\"add_class\" rowpos=\"4\" class=\"FormData\" id=\"tr_indextype_id\">";
		arr+="<td class=\"CaptionTD\"><label>名称</label></td>";
		arr+="<td class=\"DataTD\">&nbsp;<input type=\"text\" value=\""+name+"\" readonly=\"true\" size=\"20\" maxlength=\"64\" class=\"FormElement ui-widget-content ui-corner-all\"></td>";
		arr+="</tr>";
		$(parobj).find("#TblGrid_jqGridListDevmon .add_class").remove();
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #uid").val(uid);
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #type").val(type);
		$(parobj).find("#editmodjqGridListDevmon table:eq(0) tbody #tr_indextype_id").before(arr);
	}
	var uid = uidlist.split(",")[0];
	indextype_arr = $._PubSelect($(parobj).find("#indextype_id"),getContextPath()+"/commonCon/searchIndexType?uid="+uid,"[{'isedHtml':'true','getKey':'indextype_id','getVal':'name'}]");
}