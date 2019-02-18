var id =$.getUrlParam('id');
function load() {
	
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/usersCon/search",
		type : "post",
		async : false,
		data : {
			"id":id
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
		$("#username").val(rs[0].username);
		$("#roleid").val(rs[0].roleid);
		$("#id").val(rs[0].id);
    }
}  

function doSave() {
	
	var roleid=$("#roleid").val();
	var id="#"+roleid+"";
	var rolename=$(id).select().text();
	$.ajax({
		url : getContextPath()+"/usersCon/accredit",
		type : "post",
		data : {
			"id" : $("#id").val(),
			"roleid" : $("#roleid").val(),
			"rolename" : rolename
		},
		success : function(result) {
			if(result){
				alert("授权成功！");
				window.parent.doSearch();
				parent.layer.closeAll();
			}
		},
		error : function() {
			alert("授权失败！");
		}
	});
}

function initCombox(){
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/systemMan/sysRoleSearch",
		type : "post",
		async : false,
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
	var op="<option value =''>请选择...</option> "
	if (resultvalue.length>0){
		for(var i=0;i<resultvalue.length;i++){
			op=op+"<option id='"+resultvalue[i].roleid+"' value ='"+resultvalue[i].roleid+"'>"+resultvalue[i].rolename+"</option>";
		}
    }
	$("#roleid").append(op);	
	
}
