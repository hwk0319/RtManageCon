var modulus;
$(function() {
	//填充select输入。
	initSelectModul();
	$("#taskModulSelect").change(function() {
		var modul = $("#taskModulSelect").val();
		initSelectTask(modul);
	});
	//公钥
	modulus = $("#publicKey",parent.document).val();
	//隐藏时间表达式。
	$('#cronExpressTR').hide();
	//绑定type的change事件
	$('#type').change(function() {
		var type = $("#type").val();
		if ("1" == type) {
			$('#cronExpressTR').hide(500);
			hidenCronExpressPage();
			$("#cronExpress").val("");
		} 
	});
});

function initSelectModul() {
	$.ajax({
		url : getContextPath() + "/taskmanage/taskModulList",
		type : "POST",
		data : {
			"type" : "tpl"
		},
		success : function(data) {
//			jQuery("#taskModulSelect").empty();
//			jQuery("#taskModulSelect").append("<option value=' '>" + "--请选择--" + "</option>");
			$.each(data, function(i, item) {
				jQuery("#taskModulSelect").append("<option value=" + item + ">" + item + "</option>");
			});
		},
		error : function(text) {
			layer.alert(text);
		}
	});
}

function initSelectTask(modul) {
	$.ajax({
		url : getContextPath() + "/taskmanage/taskOperationList",
		type : "POST",
		data : {
			"modul" : modul
		},
		success : function(data) {
			jQuery("#taskTplSelect").empty();
			jQuery("#taskTplSelect").append("<option value=' '>" + "--请选择--" + "</option>");
			$.each(data.rows, function(i, item) {
				jQuery("#taskTplSelect").append("<option value=" + item.id + ">" + item.name + "</option>");
			});
		},
		error : function(text) {
			alert(text);
		}
	});
}

function showCronExpressPage() {
	$("#cron_iframe_show").append($("#cron_iframe").html());
	$("#cron_iframe_show #cronExpress").css({
		background : "#F0F0F0"
	}).attr("disabled", "true");
}
function hidenCronExpressPage() {
	$("#cron_iframe_show").html("");
	$(".table-edit #cronExpress").css({
		background : "#F0F0F0"
	}).attr("disabled", "true");
}

function disableAllInput() {
	$('[name=element]').each(function() {
		$(this).attr("disabled", "false");
	});
	$('#nextStep').attr("disabled", "false");
}

function createTask() {
	if(!validateHandler()){
		return;
	}
	var tplSel = $("#taskModulSelect").val();
	var tplId = $("#taskTplSelect").val();
	var name = $("#name").val();
	var type = $("#type").val();
	var cronExpress = $("#cronExpress").val();
	var desc = $("#desc").val();
	
	//拼接数据字段
  	var mData = ""+tplSel+tplId+name+type+desc+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	$.ajax({
		url : getContextPath() + "/taskmanage/creatTaskFromTemplate",
		type : "POST",
		data : {
			"id" : tplId,
			"name" : name,
			"type" : type,
			"cronExpress" : cronExpress,
			"poDesc" : desc,
			"oper" : "creatFromTpl",
			"tplSel" : tplSel,
			"mData" : mData
		},
		async : false,//设置为同步
		datatype : "json",
		success : function(data) {
			if (data.status == "success") {
				disableAllInput();
				taskId = data.value;
				hasConfirm = true;
				$("#shareTaskId", parent.document).val(taskId);
			} else {
				layer.alert(data.value);
			}
		},
		error : function(text) {
			layer.alert(text);
		}
	});
	$("#taskPageDiv2").load(getContextPath() + "/jsps/taskmanage/task/add/creatNewStep.jsp");
	$("#taskPageDiv3").load(getContextPath() + "/jsps/taskmanage/task/creatNewParam.jsp");
	doClick();
}

/**
 * 特殊字符检测
 * @param ctrlName 控件名称
 * @param info  提示信息
 * @param nullAble 是否允许为空
 * @returns {Boolean}
 */
function validateSpecialChars(ctrlName, info, nullAble){
	var aValue = $(ctrlName).val().trim();
	if ((aValue == "")&&(!nullAble)) {
		layer.msg(info+"不能为空！");
		return false;
	}
	if (aValue.length != 0) {
		var exp = /[@'\"$&^*{}<>\\\:\;]+/;
		var reg = aValue.match(exp);
		if (reg) {
			layer.msg(info+"不能含有特殊字符！");
			return false;
		} 
	}
	return true;
	
}
