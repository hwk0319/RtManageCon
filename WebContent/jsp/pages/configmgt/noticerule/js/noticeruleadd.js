var oper = 'add';
var modulus;
var jwt;
$(function(){
	//判断是新增还是编辑
	var ids=$.getUrlParam('id');
	if(ids != 'undefined'){
	 	setDetailValue(ids);
	 	$("#id").val(ids);
	 	oper = 'edit';
	}
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	});
	//公钥
	modulus = $("#publicKey",parent.document).val();
	jwt = $("#jwt",parent.document).val();
})

function setDetailValue(ids) {
	var rs = load(ids);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
			//告警类型
		if(rs[0].type == 1){
			$("#warntype option[value='" + rs[0].type + "']")
			.attr("selected", true);
		}
		if(rs[0].type == 2){
			$("#warntype option[value='" + rs[0].type + "']")
			.attr("selected", true);
		}
		if(rs[0].type == 3){
			$("#warntype option[value='" + rs[0].type + "']")
			.attr("selected", true);
		}
		//告警等级
		if(rs[0].level == 1){
			$("#warnlevel option[value='" + rs[0].level + "']")
			.attr("selected", true);
		}
		if(rs[0].level == 2){
			$("#warnlevel option[value='" + rs[0].level + "']")
			.attr("selected", true);
		}
		if(rs[0].level == 3){
			$("#warnlevel option[value='" + rs[0].level + "']")
			.attr("selected", true);
		}
		if(rs[0].level == 4){
			$("#warnlevel option[value='" + rs[0].level + "']")
			.attr("selected", true);
		}
		//联系人姓名
		if(rs[0].address_id){
			$.ajax({
				url : getContextPath()+"/addressCon/search",
				type : "post",
				dataType : 'json',
				async : false,
				data : {
					"id" : rs[0].address_id
				},
				success : function(result) {
					$("#contactname").val(result[0].name)
					$("#nameId").val(result[0].id)
				}
			});
		}
		//通知方式
		if(rs[0].way == 1){
			$("#noticeway option[value='" + rs[0].way + "']")
			.attr("selected", true);
		}
		if(rs[0].way == 2){
			$("#noticeway option[value='" + rs[0].way + "']")
			.attr("selected", true);
		}
	}
}

//编辑加载数据
function load(ids) {
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/noticeruleCon/search",
		type : "post",
		dataType : 'json',
		async : false,
		data : {
			"id" : ids
		},
		success : function(result) {
			resultvalue = result;
		},
		error : function() {
			layer.alert("加载失败");
		}
	});
	return resultvalue;
}

function doSave() {
	if(!validateHandler()){
		return;
	}
	//告警类型
	var type = $("#warntype option:selected").val();
	//告警等级
	var level = $("#warnlevel option:selected").val();
	//通知方式
	var way = $("#noticeway option:selected").val();
	//联系人ID
	var address_id = $("#nameId").val();
	var id = $("#id").val();
	//拼接数据字段
  	var mData = ""+id+type+level+way+address_id+"";
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	if(id != ""){
		oper = 'edit';
	}else{
		oper = 'add';
	}
	var index;
	$.ajax({
  		url : getContextPath()+"/noticeruleCon/update",
  		type : "post",
  		dataType : 'json',
  		data : {
  			"oper" : oper,
  			"id" : id,
  			"type" : type,
  			"level" : level,
  			"way" : way,
  			"address_id" : address_id,
  			"mData" : mData,
  			"jwt" : jwt
  		},
  		beforeSend: function () {
  			index = layer.load(1, {
  			  shade: [0.3,'#fff']
  			});
  	    },
  	    complete:function () {
  	    	layer.close(index);
	    },
  		success : function(result) {
  			for (key in result) {
  				if(key == "success"){
	  				layer.alert(result[key], {
						title: "提示"
					},function(){
						layer.close(index);
						window.parent.doNotrSearch();
		  	  		    parent.layer.closeAll();
					});
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

