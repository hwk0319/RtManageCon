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
		$("#email").val(rs[0].email);
		$("#start_ip").val(rs[0].start_ip);
		$("#id").val(rs[0].id);
		$("#end_ip").val(rs[0].end_ip);
		$("#start_time").val(rs[0].start_time);
		$("#end_time").val(rs[0].end_time);
    }
}  

function doSave() {
	var email=$("#email").val()
	if(email!=""){
		var expemail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/; 
		var regemail = email.match(expemail);
		if(regemail==null){
			alert("邮箱格式不合法！");
	    	return;
		}
	}
	
	var start_ip = $("#start_ip").val();
	var end_ip = $("#end_ip").val();
	var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if (start_ip != "") {
		var reg = start_ip.match(exp);
		if (reg == null) {
			alert("起始IP地址格式不合法！");
			return;
		}
		if (end_ip == "") {
			alert("请输入结束IP！");
			return;
		}
	}
	if (end_ip != "") {
		var regend = end_ip.match(exp);
		if (regend == null) {
			alert("结束IP地址格式不合法！");
			return;
		}
		if (start_ip == "") {
			alert("请输入起始IP！");
			return;
		}
	}

	if ((start_ip != "") && (end_ip != "")) {
		var start_ipArray = start_ip.split(".");
		var startIpStr = "";
		for (var i = 0; i < 4; i++) {
			var curr_num = start_ipArray[i];
			var number_Bin = parseInt(curr_num);
			number_Bin = number_Bin.toString(2);
			var iCount = 8 - number_Bin.length;
			if (i != 0) {
				for (var j = 0; j < iCount; j++) {
					number_Bin = "0" + number_Bin;
				}
			}
			startIpStr += number_Bin;
		}
		var end_ipArray = end_ip.split(".");
		var endIpStr = "";
		for (var i = 0; i < 4; i++) {
			var curr_num = end_ipArray[i];
			var number_Bin = parseInt(curr_num);
			number_Bin = number_Bin.toString(2);
			var iCount = 8 - number_Bin.length;
			if (i != 0) {
				for (var j = 0; j < iCount; j++) {
					number_Bin = "0" + number_Bin;
				}
			}
			endIpStr += number_Bin;
		}
		if (endIpStr < startIpStr) {
			alert("结束IP不能小于起始IP");
			return;
		}
	}
	    
	var start_time = $("#start_time").val();
	var end_time = $("#end_time").val();
	var numberexp = "[^0-9]";
	if (start_time != "" || end_time != "") {
		if (start_time == "") {
			alert("请输入起始时间！");
			return;
		}
		if (end_time == "") {
			alert("请输入结束时间！");
			return;
		}
		if ((start_time.match(numberexp) != null)
				|| (end_time.match(numberexp) != null)) {
			alert("请输入正整数");
			return;
		}
		if (start_time == end_time) {
			alert("起始、结束时间不能相同");
			return;
		}
	}

	if ((start_time < 0) || (start_time > 23) || (end_time < 0)
			|| (end_time > 23)) {
		alert("时间请在0-23小时内");
		return;
	}
	
	$.ajax({
		url : getContextPath()+"/usersCon/update",
		type : "post",
		data : {
			"id" : $("#id").val(),
			"email" : $("#email").val(),
			"start_ip" : $("#start_ip").val(),
			"end_ip" : $("#end_ip").val(),
			"start_time" : $("#start_time").val(),
			"end_time" : $("#end_time").val()
		},
		success : function(result) {
			if(result){
				alert("保存成功！");
				window.parent.doSearch();
				parent.layer.closeAll();
			}
		},
		error : function() {
			alert("保存失败！");
		}
	});
}

function initCombox(){
	$("#role").append(SYS_ROLE_OP);	
	
}
