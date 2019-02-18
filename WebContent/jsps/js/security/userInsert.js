function setDetailValue() {

}

function checkUN() {
	var name = $("#username").val();
	if (name == "" || name == null) {
		$("#msg").html("");
		$("#unmsg").html("用户名不能为空！");
		return;
	} else {
		$("#unmsg").html("");
	}
	var reg = new RegExp('^[a-zA-Z][a-zA-Z0-9_]*$');
	if (!reg.test(name)) {
		$("#msg").html("");
		$("#unmsg").html("用户名只能字母开头，可包含数字、字母和下划线");
		return;
	} else {
		$("#msg").html("");
		$("#unmsg").html("");
	}
	if (name.length < 4 || name.length > 16) {
		$("#msg").html("");
		$("#unmsg").html("用户名请设4到16个字符");
		return;
	} else {
		$("#msg").html("");
		$("#unmsg").html("");
	}
	ckeckajax(name, true);
}

function ckeckajax(name, _async) {
	ret = "";
	$.ajax({
		async : _async,
		url : getContextPath() + "/usersCon/checkUN",
		type : "post",
		data : {
			"name" : name
		},
		success : function(result) {
			if (result != "success") {
				$("#unmsg").html("");
				$("#msg").html("用户名已存在，请更换用户名！");
				ret = "false";
				return;
			} else {
				$("#unmsg").html("");
				$("#msg").html("");
			}
		},
		error : function() {
			return;
		}
	});
	return ret;
}

function doSave() {
	var name = $("#username").val();
	var ret = ckeckajax(name, false);
	if (ret == "false") {
		return;
	}
	var password = $("#password").val();
	if (name == "" || name == null) {
		// showTips("tips","用户名不能为空", "215px", "50px", "#D1DEE5");
		$("#unmsg").html("用户名不能为空！");
		return;
	} else {
		$("#unmsg").html("");
	}

	var reg = new RegExp('^[a-zA-Z][a-zA-Z0-9_]*$');
	if (!reg.test(name)) {
		$("#unmsg").html("用户名只能字母开头，可包含数字、字母和下划线");
		return;
	} else {
		$("#unmsg").html("");
	}
	if (name.length < 4 || name.length > 16) {
		$("#unmsg").html("用户名请设4到16个字符");
		return;
	} else {
		$("#unmsg").html("");
	}
	if (password == "" || password == null) {
		$("#pwdmsg").html("密码不能为空！");
		// showTips("tips","密码不能为空", "215px", "50px", "#D1DEE5");
		return;
	} else {
		$("#pwdmsg").html("");
	}

	if (password == name) {
		$("#pwdmsg").html("用户名和密码不能相同！");
		return;
	} else {
		$("#pwdmsg").html("");
	}

	var regex = new RegExp('^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$');
	if (!regex.test(password)) {
		$("#pwdmsg").html("密码应为字母、数字组合，且大于8位小于20位");
		// showTips("tips","密码应为字母、数字组合，且大于8位小于20位", "165px", "25px",
		// "#D1DEE5");
		return;
	} else {
		$("#pwdmsg").html("");
	}

	var email = $("#email").val()
	if (email != "") {
		var expemail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
		var regemail = email.match(expemail);
		if (regemail == null) {
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

	// RSA加密
	var modulus = $("#modules").val();
	RSAUtils.setMaxDigits(131);
	var key = new RSAUtils.getKeyPair("10001", '', modulus);
	password = RSAUtils.encryptedString(key, password);
	var userName = RSAUtils.encryptedString(key, name);

	$.ajax({
		url : getContextPath() + "/usersCon/insert",
		type : "post",
		data : {
			"username" : userName,
			"password" : password,
			"email" : email,
			"start_ip" : start_ip,
			"end_ip" : end_ip,
			"start_time" : start_time,
			"end_time" : end_time,
			"firstlogin" : $("#firstlogin").val(),
			"online" : $("#online").val(),
			"status" : $("#status").val(),
			"err_num" : $("#err_num").val()
		},
		success : function(result) {
			if(result ){
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

function initCombox() {
	$("#role").append(SYS_ROLE_OP);

}
