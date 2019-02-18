$(function() {
	$("#verification").val("");
	var ht = $(window).height();
	$(".bg")[0].style.paddingTop = ht/2-117+"px";
	document.onkeydown = function(e){ //添加回车登录事件
	    var ev = document.all ? window.event : e;
	    if(ev.keyCode==13) {
			login();
	     }
	};
	initModel();
});
//验证码触发事件
function checkCode(){
	var code = $("#verification").val();
	if(code.length==4){
		verfify();
	}
}
//校验验证码
function verfify(){
	var code = $("#verification").val();
	$.ajax({
		url : "../../login/verfify",
		type : "post",
		data : {code:code},
		success : function(result){
			if(result != "success"){
				layer.tips('验证码输入错误。', '#vertity_img');
				$("#verification").val("");
				$("#vertity_img").click();
			}
		}
	
	});
}
//初始化rsa公钥
function initModel(){
	$.ajax({
		url : "../../login/initModels",
		type :"post",
		data : {},
		success : function(result){
			$("#modulus").val(result);
		}
	});
}
$(window).resize(function(){
	var ht = $(window).height();
	$(".bg")[0].style.paddingTop = ht/2-117+"px";
});

//获得用户角色
function login() {
	var name = $("input[name='username']").val();
	var pwd = $("input[name='pwd']").val();
	var identifyCode =$("#verification").val();
	
	if (name == "" || name == null) {
		layer.tips('请输入用户名。', '#username');
		return;
	}
	if (pwd == "" || pwd == null) {
		layer.tips('请输入密码。', '#pwd');
		return;
	}
	if(identifyCode ==null || identifyCode == ""){
		layer.tips('请输入验证码。', '#vertity_img');
		return;
	}
	if(identifyCode.length != 4){
		layer.tips('验证码输入错误。', '#vertity_img');
		return ;
	}
	
	//RSA加密
	var modulus = $("[type=hidden]").val();
	RSAUtils.setMaxDigits(131); 
    var key      = new RSAUtils.getKeyPair("10001",'',modulus); 
    pwd = hex_md5(pwd)+pwd;
    pwd = RSAUtils.encryptedString(key, pwd); 
	var userName = RSAUtils.encryptedString(key, name);
	$.ajax({
		url : "../../login/login",
		type : "post",
		data : {
			"name" : userName,
			"pwd" : pwd,
			"identifyCode" : identifyCode
		},
		success : function(result) {
			var array = result[0].split(",");
			//是否第一次登陆  1代表带一次登录
			var firstLogin = array[0]; 
			//用户id
			var userId = array[1];
			var userName = array[2];
			
			if ($.trim(firstLogin) == "0"||$.trim(firstLogin) == "1"){
//			if (firstLogin.trim() == "0"||firstLogin.trim() == "1"){
				location.href = "../index.jsp";
			} else {
				if(result[0] == "密码错误"){
					layer.tips("用户名或密码错误", '#pwd');
				}else{
					layer.tips(result[0], '#username');
				}
			}
		},
		error : function() {
			layer.alert("登录失败");
		}
	});
}

