var indextype_arr={},index_arr={},type_arr={},level_arr={},task_arr={};
//layer回显用
var callbackShowParmas = {};
var obj_type;
var oper='add';
var modulus;
var jwt;
function initWarulFunction(){
	//公钥
	modulus = $("#publicKey",parent.document).val();
	jwt = $("#jwt",parent.document).val();
	
	indextype_arr=$._PubSelect("#indextype_search",getContextPath()+"/commonCon/searchIndexType","[{'getKey':'indextype_id','getVal':'name'}]");
	index_arr=$._PubSelect("",getContextPath()+"/commonCon/searchIndex","[{'getKey':'index_id','getVal':'description'}]");
	type_arr=$._PubSelect("",getContextPath()+"/commonCon/search?type=warn_type","[{'getKey':'value','getVal':'name'}]");
	level_arr=$._PubSelect("",getContextPath()+"/commonCon/search?type=warn_level","[{'getKey':'value','getVal':'name'}]");
	task_arr=$._PubSelect("",getContextPath()+"/warnruleCon/searchTask","[{'getKey':'task_id','getVal':'task_name'}]");
	creatwarnruleGrid("");
	$("#warnruleSrch").click(doSearchDev);
	//初始化下拉
	Initselect(indextype_arr,index_arr,level_arr,type_arr);
	//指标分类关联指标项下拉
	$("#indextype").change(function(){
		getindexinfo(this,$("#indexitem"));
	});
	//当指标项发生改变时，相关阈值框置灰
	$("#indexitem").change(function(){
		var index_warn_id=$(this).val();
		setread(index_warn_id);
	});
}
function creatwarnruleGrid(param){
	var task_name="";
	param=param===""?"":"?"+param;
	var index_warn_id1="";
	jQuery("#jqGridListWarRul").jqGrid({
		url:getContextPath()+"/warnruleCon/search"+param,
		datatype:'json',
		postData:{
			'indextype_id': $("#indextype_search").val(),
			'index_id': $("#index_search").val()
		},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'指标分类',name:'indextype_id',index:'indextype_id',width:100,align:'center',
					   formatter:function(cellvalue, options, rowObject){
	                    	var indextypename="";
		                    for (key in indextype_arr){
		                    	if(cellvalue==key){
		                    		indextypename=indextype_arr[key];
									break;
								}
		                    }
		                    return indextypename;
	                    }
				   },
				   {label:'指标项',name:'index_warn_id',index:'index_warn_id',width:100,align:'center',
					    formatter:function(cellvalue, options, rowObject){
	                    	var indexname="";
		                    for (key in index_arr){
		                    	if(cellvalue==key){
		                    		indexname=index_arr[key];
		                    		index_warn_id1=cellvalue;
									break;
								}
		                    }
		                    return indexname;
	                    }
				   },
				   {label:'指标项',name:'index_warn_id1',index:'index_warn_id1',width:100,align:'center',hidden:true,
					 	formatter:function(cellvalue, options, rowObject){
		                    return index_warn_id1;
	                    }
					},
				   {label:'阈值上限',name:'upper_limit',index:'upper_limit',width:100,align:'center'},
				   {label:'阈值下限',name:'lower_limit',index:'lower_limit',width:100,align:'center'},
				   {label:'标准值',name:'std_value',index:'std_value',width:100,align:'center'},
				   {label:'告警类型',name:'type',index:'type',width:100,align:'center',
					   formatter:function(cellvalue, options,rowObject){
		                   	var indexname="";
		                    for (key in type_arr){
		                    	if(cellvalue==key){
		                    		indexname=type_arr[key];
									break;
								}
		                    }
		                   	return indexname;
	                    }
				   },
				   {label:'告警等级',name:'level',index:'level',width:100,align:'center',
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
				   {label:'任务id',name:'task_id',index:'task_id',width:100,align:'center',hidden:true,
					   formatter:function(cellvalue, options,rowObject){
	                    	task_name="";
		                    for (key in task_arr){
		                    	if(cellvalue==key){
		                    		task_name=task_arr[key];
									break;
								}
		                    }
		                    return "";
	                    }
				   },
				   {label:'任务名称',name:'task_name',index:'task_id',width:100,align:'center',hidden:true,formatter:function(cellvalue, options,rowObject){
					   		$("#FrmGrid_jqGridListWarRul #uid").css("display","none");
		                    return task_name;
	                    }},
//				   {label:'名称',name:'uname',index:'uname',width:100,align:'center'},
				   {label:'统一ID',name:'uid',index:'uid',width:100,align:'center'},
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
			caption:"<div style='color:#000;width:95%;'>告警规则<span id='add' style='cursor:pointer;float:right;' onclick='warnruleAdd()'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
			height:'auto',
			autowidth:true,
			editurl:getContextPath()+"/warnruleCon/update"+param,//编辑地址
			pager : "#jqGridPaperListWarRul",
			gridComplete: function(){
	            var ids = $("#jqGridListWarRul").getDataIDs();
	            for(var i=0;i<ids.length;i++){
	                var cl = ids[i];
	                ed = "<a title='编辑' onclick='warnruleAdd("+cl+")' style='cursor:pointer;'>编辑 </a>";
	                vi = "<a title='删除' onclick='doDelete("+cl+")' style='cursor:pointer;'>删除</a>"; 
	                jQuery("#jqGridListWarRul").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
	            } 
	        }
	});
}
//删除
function doDelete(id){
	var jwt1 = getJWT();
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/warnruleCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt1
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
	  			window.parent.doSearchDev();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}
/**
 * 显示设备
 */
function deviceList(){
	if($("#indextype").val().trim() == ""){
		layer.msg("请选择指标分类！");
	}else{
		var layurl = getContextPath()+"/jsp/comm/jsp/deviceComm.jsp";
		var title = "设备";
		var uidd = "";
		if($("#indextype").val()!=null){
			uidd = $("#indextype").val().split(",")[0].substr(0, 1);
			if(uidd=='2'){
				layurl = getContextPath()+"/jsp/comm/jsp/SystemComm.jsp";
				title = "软件系统";
			}
		}
		
		callbackShowParmas.alarmSelects = $("#devuid").val();
		callbackShowParmas.type = "warnlog";
		callbackShowParmas.indexType = $("#indextype").val().substring(0,1);
		var indextype = $("#indextype").val();
		if(indextype==null || indextype==" " || indextype==""){
			obj_type = null;
		}else{
			var cname = $($("#indextype option[value="+indextype+"]")[0]).html();
			searchobj(cname);
		}
		callbackShowParmas.obj_type = obj_type; //查询对应的对象类型
		layer.open({
	  	    type: 2,
	  	    title: title, 
	  	    fix: false,
	  	    id:'dev_sys_list',
	  	    shadeClose: false,
	  	    area: ['45%', '65%'],
		    btn: ['确定', '取消'],
		 	yes: function(index, layero){
		         var iframeWin = window[layero.find('iframe')[0]['name']];
		         if(uidd=='2'){
		        	 iframeWin.doSaveSys();
				 }else if(uidd=='1'){
					 iframeWin.doSaveDev();
				 }
	      },
	      btn2: function(){
	       	 layer.close();
	      },
	  	    content: [layurl,'no']
	  	});
	}
}
function searchobj(name){
	$.ajax({
		url:getContextPath()+"/indextypeCon/search",
		type:'POST',
		async:false,
		data:{
			"name":name,
		},
		dataType:'json',
		success : function(data) {
			//查询相关的对象类型
			obj_type = data[0].obj_type;
  		},
  		error : function() {
  			layer.alert("保存失败！");
  		}
	})
}
/**
 * 选择设备后的回填
 */
function backFun(selects){
	var uids = "";
	for(var i=0;i<selects.length;i++){
		uids = uids + selects[i].uid + ",";
	}
	$("#devuid").val(uids.substring(0, uids.length-1));
}

//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListWarRul").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//查询
function doSearchDev(){
	$.jgrid.gridUnload('#jqGridListWarRul');
	creatwarnruleGrid("");
}
//添加任务
function TaskList(){
	layer.open({
  	    type: 2,
  	    title: '', 
  	    fix: false,
  	    shadeClose: false,
  	    area: ['1000px', '550px'],
  	    content: [getContextPath()+"/jsp/comm/jsp/taskComm.jsp",'no']
  	});
}
//添加
function warnruleAdd(id){
	getJWT();
	layer.open({
	    type: 2,
	    title: '告警规则', 
	    fix: false,
	    shadeClose: false,
	    area: ['880px','530px'],
//	    area: ['65%','83%'],
	    moveOut: true,
	    content: [getContextPath()+"/jsp/pages/configmgt/warnrule/warnruleAdd.jsp?id="+id,'no'],
	});
}

// 显示的设备类型
var indexType;
function setread(index_id){
	if(index_id!=""){
		var warn_rule=0;
		var upper_limit="";
		var lower_limit="";
		var std_value=null;
		$.ajax({
			url:getContextPath()+"/indexCon/search?index_id="+index_id,
			type:"post",
			async:false,
			success:function(res){
				var warn_rule=res[0]["warn_rule"];
				if(parseInt(warn_rule)==1){
					$("#upper_limit").val(upper_limit).removeAttr("readonly").css({"background":"#fff"});
					$("#lower_limit").val(lower_limit).removeAttr("readonly").css({"background":"#fff"});
					$("#std_value").val("").attr("readonly","readonly").css({"background":"#F5F5F5"});
					$("#upper_limit").attr("validate",'callback nn');
					$("#lower_limit").attr("validate",'callback nn');
					$("#std_value").removeAttr("validate");
					Validatas.reLoad();
				}else if(parseInt(warn_rule)==2){
					$("#upper_limit").val("").attr("readonly","readonly").css({"background":"#F5F5F5"});
					$("#lower_limit").val("").attr("readonly","readonly").css({"background":"#F5F5F5"});
					$("#std_value").val(std_value).removeAttr("readonly").css({"background":"#fff"});
					$("#std_value").attr("validate",'nn nspcl');
					$("#upper_limit").removeAttr("validate");
					$("#lower_limit").removeAttr("validate");
					Validatas.reLoad();
				}else{
					$("#upper_limit").val("").removeAttr("readonly").css({"background":"#fff"});
					$("#lower_limit").val("").removeAttr("readonly").css({"background":"#fff"});
					$("#std_value").val("").removeAttr("readonly").css({"background":"#fff"});
					$("#upper_limit").removeAttr("validate");
					$("#lower_limit").removeAttr("validate");
					$("#std_value").attr("validate",'nspcl');
					Validatas.reLoad();
				}
			}
		});
	}
}
//根据指标分类筛选指标项
function getindexinfo(obj,str){
  var index_type=$(obj).val();
  $(str).html("<option value=' ' selected>--请选择--</option>");
  if(index_type!==""){
	 var  a=$._PubSelect(str,getContextPath()+"/indexCon/searchindex?index_type="+index_type,"[{'getKey':'index_id','getVal':'description','attr':'warn_rule'}]");
  }
}
//初始化下拉框
function Initselect(indextype_arr,index_arr,level_arr,type_arr){
	//初始化指标分类下拉项
	$("#indextype").children('option').remove();
	for(key in indextype_arr){
		if(key ==" "){
			$("#indextype").append("<option value='"+key+"' selected>"+indextype_arr[key]+"</option>");
		}else{
			$("#indextype").append("<option value='"+key+"'>"+indextype_arr[key]+"</option>");
		}
	}
	//初始化指标项下拉项
	$("#indexitem").children('option').remove();
	for(key in index_arr){
		if(key ==" "){
			$("#indexitem").append("<option value='"+key+"' selected>"+index_arr[key]+"</option>");
		}else{
			$("#indexitem").append("<option value='"+key+"'>"+index_arr[key]+"</option>");
		}
	}
	//初始化告警等级下拉项
	$("#warnlevel").children('option').remove();
	for(key in level_arr){
		if(key ==" "){
			$("#warnlevel").append("<option value='"+key+"' selected>"+level_arr[key]+"</option>");
		}else{
			$("#warnlevel").append("<option value='"+key+"'>"+level_arr[key]+"</option>");
		}
	}
	//初始化告警类型下拉项
	$("#warntype").children('option').remove();
	for(key in type_arr){
		if(key ==" "){
			$("#warntype").append("<option value='"+key+"' selected>"+type_arr[key]+"</option>");
		}else{
			$("#warntype").append("<option value='"+key+"'>"+type_arr[key]+"</option>");
		}
	}
}
function doSave(){
	if(!validateHandler()){
		return;
	}
	var id = $("#sid").val();
	if(id != ""){
		oper = "edit";
	}
	//获取前端录入数据
	var indextype = $("#indextype").val();
	var index = $("#indexitem").val();
	var upper_limit = $("#upper_limit").val();
	var lower_limit = $("#lower_limit").val();
	var std_value = $("#std_value").val();
	var devuid = $("#devuid").val();
	if(devuid == ""){
		devuid = "--";
	}
	var warnlevel = $("#warnlevel").val();
	var warntype = $("#warntype").val();

	//拼接数据字段
  	var mData = ""+id+indextype+index+upper_limit+lower_limit+std_value+devuid+warnlevel+warntype+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
  	var indexs;
  	var jwt2 = $("#jwt",parent.document).val();
	$.ajax({
		url:getContextPath()+"/warnruleCon/update?oper="+oper,
		type:'POST',
		data:{
			"id":id,
			"uid":devuid,
			"indextype_id":indextype,
			"index_warn_id":index,
			"upper_limit":upper_limit,
			"lower_limit":lower_limit,
			"std_value":std_value,
			"type":warntype,
			"mData":mData,
			"level":warnlevel,
  			"jwt" : jwt2
		},
		beforeSend: function () {
			indexs = layer.load(1, {
  			  shade: [0.3,'#fff']
  			});
  	    },
  	    complete:function () {
  	    	layer.close(indexs);
	    },
		dataType:'json',
		success : function(result) {
			for (key in result) {
  				if(key == "success"){
	  				layer.alert(result[key], {
						title: "提示"
					},function(){
						layer.close(index);
						window.parent.doSearchDev();
	  		   			parent.layer.closeAll();
					});
	  				$(".select").val(" ");
	  	  			$(".input").val("");
  				}else{
  					layer.alert(result[key]);
  				}
  			}
  		},
  		error : function() {
  			layer.alert("保存失败！");
  		}
	});
}
function setDetailValue(id) {
		$("#indextype").attr("disabled","disabled").css({"background":"#F5F5F5"});
		$("#indexitem").attr("disabled","disabled").css({"background":"#F5F5F5"});
		$("#devuid").attr("disabled","disabled").css({"background":"#F5F5F5"});
		$.ajax({
			url:getContextPath()+"/warnruleCon/search?id="+id,
			type:"post",
			async:false,
			success:function(data){
				//查询值的回填
				$("#indextype").val(data[0].indextype_id);
				$("#indexitem").val(data[0].index_warn_id);
				setread(data[0].index_warn_id);
				$("#warntype").val(data[0].type);
				$("#warnlevel").val(data[0].level);
				$("#upper_limit").val(data[0].upper_limit);
				$("#lower_limit").val(data[0].lower_limit);
				$("#std_value").val(data[0].std_value);
				$("#devuid").val(data[0].uid);
				$("#sid").val(data[0].id);
//				setread(data[0].index_warn_id);
				}
			});
}
