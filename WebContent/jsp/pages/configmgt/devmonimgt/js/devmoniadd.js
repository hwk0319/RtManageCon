var oper = 'add';
var indextype_arr;
var selecttype=" ";
var flag = 0;
var modulus;
var jwt;

// layer回显用
var callbackShowParmas = {};
$(function(){
	//公钥
	modulus = $("#publicKey",parent.document).val();
	jwt = $("#jwt",parent.document).val();
	//判断是新增还是编辑
	var id=$.getUrlParam('id');
	//初始化下拉选择
	if(id != 'undefined'){
		initSelect(indextype_arr);
	 	setDetailValue(id);
	 	$("#ids").val(id);
	 	oper = 'edit';
	 	$('#devuid').removeAttr("onclick");
		var uid = $("#devuid").val().split(",")[0];
		indextype_arr = $._PubSelect($("#indextype"),getContextPath()+"/commonCon/searchIndexType?uid="+uid,"[{'isedHtml':'true','getKey':'indextype_id','getVal':'name'}]");
		initSelect(indextype_arr);
		$("#indextype").val(selecttype);
	}else{
		indextype_arr=$._PubSelect($("#indextype"),getContextPath()+"/commonCon/searchIndexType","[{'getKey':'indextype_id','getVal':'name'}]");
		initSelect(indextype_arr);
	}
	$("#close").click(function(){
		parent.layer.closeAll();
	})
})
function initSelect(indextype_arr){
	//指标分类
	$("#indextype").children('option').remove();
	for(key in indextype_arr){
		if(key ==" "){
			$("#indextype").append("<option value='"+key+"' selected>"+indextype_arr[key]+"</option>");
		}else{
			$("#indextype").append("<option value='"+key+"'>"+indextype_arr[key]+"</option>");
		}
	}
}
function deviceList(){
	callbackShowParmas.alarmSelects = $("#devuid").val();
	callbackShowParmas.type = "devmoni";
	callbackShowParmas.indexType = "1";
	var layurl = getContextPath()+"/jsp/comm/jsp/devSysTab.jsp";
	layer.open({
  	    type: 2,
  	    title: '', 
  	    closeBtn: 0,
  	    fix: false,
  	    id:'dev_sys_list',
  	    shadeClose: false,
  	    area: ['75%', '85%'],
	    btn: ['确定', '取消'],
	 	yes: function(index, layero){
	         var iframeWin = window[layero.find('iframe')[0]['name']];
	         var body = layer.getChildFrame('body', index); 
	         var tabIndex = body.find("#tabIndex").val();
	         if(tabIndex == "" || tabIndex == "1"){
	        	 iframeWin.doSaveDev();
	         }else if(tabIndex == "2"){
	        	 iframeWin.doSaveSys();
	         }
	      },
	      btn2: function(){
	       	 layer.closeAll();
	      },
  	    content: [layurl,'no']
  	});
}
function doSave(){
	if(!validateHandler()){
		return;
	}
	var type = null;
	var id = $("#ids").val();
	var uids = $("#devuid").val();
	var indextype = $("#indextype").val();
	var cyctime = $("#cyctime").val();
	var colltype = $("#colltype").val();
	var tp = uids.split(",")[0].substr(0, 1);
	if(tp == '1'){
		type="D";
	}else if(tp == '2'){
		type="S";
	}
	
	//拼接数据字段
  	var mData = ""+id+uids+indextype+cyctime+colltype+type+"";
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	//数据加密
	if(id != ""){
		oper = "edit";
	}else{
		oper = 'add';
	}
	//首先对应的先校验有没有原先的指标
	var mess="";
	if(id == ""){
		$.ajax({
			type:"post",
			url:getContextPath()+"/devmonimgtCon/checkDevIndex",
			data:{
				"id" : id,
				"uid" :  uids,
				"indextype_id" : indextype
			},
			async:false,
			success:function(res){
				mess=res;
			}
		});
	}
    if(mess == ""){
    	var index;
    	$.ajax({
        	type:"post",
        	url:getContextPath()+"/devmonimgtCon/update",
        	data:{
        		"oper" : oper,
        		"id" : id,
      			"uid" :  uids,
      			"indextype_id" : indextype,
      			"collect" : colltype,
      			"period" : cyctime,
      			"type":type,
      			"mdata":mData,
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
        	success:function(result){
        		for (key in result) {
      				if(key == "success"){
    	  				layer.alert(result[key], {
    						title: "提示"
    					},function(){
    						layer.close(index);
    						window.parent.doSearchDevmon();
                  			parent.layer.closeAll();
    					});
      				}else{
      					layer.alert(result[key]);
      				}
      			}
        	}
        });
	}else{
		layer.alert(mess);
	}
}

function setDetailValue(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		$("#colltype").val(rs[0].collect);
		$("#cyctime").val(rs[0].period);
		$("#devuid").val(rs[0].uid);
		$("#indextype").val(rs[0].indextype_id);
		selecttype = rs[0].indextype_id;
    }
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/devmonimgtCon/search",
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

function doCleak(){
	  $("#cyctime").val("");
}
//选择监控UID后根据UID判断更新指标分类下拉选择列表
function updateSelect(){
	var uid = $("#devuid").val().split(",")[0];
	indextype_arr = $._PubSelect($("#indextype"),getContextPath()+"/commonCon/searchIndexType?uid="+uid,"[{'isedHtml':'true','getKey':'indextype_id','getVal':'name'}]");
	initSelect(indextype_arr);
}