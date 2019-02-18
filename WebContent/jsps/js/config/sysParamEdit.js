var id =$.getUrlParam('id');
function load() {
	
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/sysParamCon/search",
		type : "post",
		async : false,
		data : {
			"id":id,
			"code" : "",
			"name" : ""
		},
		dataType : "json",
 
		success : function(result) {
			if(result.length<1){
				//showTips("tip", "查询为空", "160px", "60px", "#D1DEE5");
				resultvalue = result;
			}else{
				resultvalue = result;
			}
		},
		error : function() {
			alert("加载失败");
		}
	});
	return resultvalue;
}
function setDetailValue() {
	var data = [];
	var rs = load();//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		$("#id").val(rs[0].id);
		$("#code").val(rs[0].code);
		$("#name").val(rs[0].name);
		$("#value_code").val(rs[0].value_code);
		$("#unit").val(rs[0].unit);
		$("#description").val(rs[0].description);
		var paramOptions = rs[0].paramOptions;
		outParamField(rs[0].code_type, paramOptions);
		$("#value").val(rs[0].value);
    }
}  

function doSave() {
	var value = $("#value").val();
	var unit = $("#unit").val();
	var description = $("#description").val();
	var exp = /[@'\"$&^*{}<>\\\:\;]+/;
	if(value != ""){
		if(value.match(exp)){
			alert("参数值请不要输入特殊字符");
		}
	}
	if(unit != ""){
		if(unit.match(exp)){
			alert("参数单位请不要输入特殊字符");
		}
	}
	if(description != ""){
		if(description.match(exp)){
			alert("说明请不要输入特殊字符");
		}
	}
	$.ajax({
		url : getContextPath()+"/sysParamCon/save",
		type : "post",
		data : {
			"id" : $("#id").val(),
			"code" : $("#code").val(),
			"name" : $("#name").val(),
			"value" : value,
			"value_code" : $("#value_code").val(),
			"unit" : unit,
			"description" : description
		},
		success : function(result) {
			alert("保存成功！");
			window.parent.doSearch();
			parent.layer.closeAll();
		},
		error : function() {
			alert("保存失败！");
		}
	});
}
