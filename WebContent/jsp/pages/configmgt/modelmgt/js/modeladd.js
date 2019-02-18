var oper = 'add';
var modulus;
var jwt;
$(function() {
	//判断是新增还是编辑
	var id=$.getUrlParam('id');
	if(id != 'undefined'){
	 	setDetailValue(id);
	 	$("#id").val(id);
	 	oper = 'edit';
	}else{
		//初始化下拉框的值
		initCombox();
	}
	$("#devicetype").click(function() {
		var dt = $("#devicetype").val();
		//因为使用append方法添加，因此需要清除
 		$("#factory option[value!='']").remove();
		initFactory(dt);
	});
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	});
	//公钥
	modulus = $("#publicKey",parent.document).val();
	jwt = $("#jwt",parent.document).val();
});
var devicetypeSel;
var factorySel;
function setDetailValue(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		initCombox();
		devicetypeSel = rs[0].devicetype;
		$("#devicetype option[value='"+devicetypeSel+"']").attr("selected",true);
		initFactory(devicetypeSel);
		factorySel = rs[0].factory;
		$("#factory option[value='" + factorySel + "']").attr("selected", true);
		$("#id").val(rs[0].id);
		$("#msize").val(rs[0].capacity);
		$("#port").val(rs[0].portnum);
		//新增字段
		$("#model").val(rs[0].model);
		$("#name").val(rs[0].name);
    }
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/modelCon/search",
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
function initCombox(){
	//获取数据字典值并显示在下拉框
	$.ajax({
		url:getContextPath()+"/commonCon/search",
		type:'POST',
		data:{type:'device_type'},
		dataType:'json',
		async:false,
		success:function(data){
				for(var i=0;i<data.length;i++)
				{
					if(data[i].value != "0"){
						$("#devicetype").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
					}
				}
		}
	});
	
	//获取数据字典值并显示在下拉框
	/*$.ajax({
		url:getContextPath()+"/commonCon/search",
		type:'POST',
		data:{type:"server_factory"},
		dataType:'json',
		async:false,
		success:function(data){
			for(var i=0;i<data.length;i++)
			{
				$("#factory").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
				//编辑的时候默认选择
				if(factorySel==data[i].value){
					$("#factory option[value='"+factorySel+"']").attr("selected",true);
				}
			}
		}
	});*/

}
//保存
function doSave() {
	if(!validateHandler()){
		return;
	}
	//类型
	var devicetype = $("#devicetype").val();
	//厂商
	var factory = $("#factory").val();
	//型号
	var model = $("#model").val();
	//容量
	var msize = $("#msize").val();
	//端口数
	var port = $("#port").val();
	var id = $("#id").val();
	//拼接数据字段
  	var mData = ""+id+devicetype+factory+model+msize+port+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	if(id != ""){
		oper = "edit";
	}else{
		oper = "add";
	}
  	var index;
	$.ajax({
  		url : getContextPath()+"/modelCon/update",
  		type : "post",
  		dataType : 'json',
  		async: false ,
  		beforeSend: function () {
  			index = layer.load(1, {
  			  shade: [0.3,'#fff']
  			});
  	    },
  	    complete:function () {
  	    	layer.close(index);
	    },
  		data : {
  			"oper" : oper,
  			"id" : id,
  			"devicetype" : devicetype,
  			"factory" : factory,
  			"model" : model,
  			"capacity" : msize,
  			"portnum" : port,
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
						window.parent.doSearch();
	  		   			parent.layer.closeAll();
					});
  				}else{
  					layer.alert(result[key]);
  				}
  			}
  		},
  		error : function() {
  			layer.alert("保存失败！", {
				title: "提示"
			});
  		}
  	});	
}

//初始化厂商
function initFactory(tp){
		var type;
		if(tp == "1"){
			type = "server_factory";
			//服务器
		}else if(tp == "2"){
			//交换机
			type = "switchboard_factory";
		}else if(tp == "10"){
			//交换机
			type = "loadB_factory";
		}else{
			type = "";
		}
		//获取数据字典值并显示在下拉框
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			async: false ,
			data : {
						type : type
					},
			dataType : 'json',
			success : function(data) {
						for (var i = 0; i < data.length; i++) {
							$("#factory").append("<option  value='"+data[i].value+"'>"+ data[i].name + "</option>");
						}
					}
				});
}
