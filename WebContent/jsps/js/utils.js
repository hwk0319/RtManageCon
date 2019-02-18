/**
 * ajax 请求错误
 */
/*$(document).ajaxError(function(e,xhr,opt,errorMsg){
	layer.alert('登录超时，请重新登录！', function(){
		$(window).attr('location',getContextPath()+"/signout");
	});
	setTimeout(function(){
		$(window).attr('location',getContextPath()+"/signout");
	}, 2000);
});*/

/**
 * 获取应用路路径，后面不带/
 * @returns
 */ 
function getContextPath() {
	    var pathName = document.location.pathname;
	    var index = pathName.substr(1).indexOf("/");
	    var result = pathName.substr(0,index+1);
	    return result;
}

/**
 * 获取页面参数
 */
(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURI(r[2]); return null;
    }
})(jQuery);

function getSelectedRowID(gridName) {
	var id=$(gridName).jqGrid('getGridParam','selrow');
	return id;
}

function getSelectedRowData(gridName, fieldname){
	var id=$(gridName).jqGrid('getGridParam','selrow');
	var rowDatas = $(gridName).jqGrid('getRowData', id);
	var row = rowDatas[fieldname];
	return row;
}

function isSelectRow(gridName){
	var id = getSelectedRowID(gridName);
	if ((id==null)||(id=="")||(id=="undefined")){
		layer.alert("未选中数据！");
		return false;
	}
	return true;
	/*
	var selectedRowIds = $(gridName).jqGrid("getGridParam","selarrrow");  
	var len = selectedRowIds.length;  
	if (len==0){
		alert("未选中数据！");
		return false;
	}	
	return true;*/
}

function setCheckbox(obj,value){
	if(value=="1"){
		obj.prop("checked",true);
	} else {
		obj.prop("checked",false);
	}
}

function getCheckBoxValue(obj){
  if (obj.prop("checked") == true){
	  return "1";
  } else {
	  return "0";
  };
}

function setCheckboxX(obj,value){
	if(value=="true"){
		obj.prop("checked",true);
	} else {
		obj.prop("checked",false);
	}
}

function getCheckBoxValueX(obj){
  if (obj.prop("checked") == true){
	  return "true";
  } else {
	  return "false";
  };
}

function showTips(id,text,top,left){
	$("#"+id).css({
		'top' : top,
		'left' : left,
		'background':'#D1DEE5',
		'position': 'absolute',
		'display': 'none',
		'width': '450px',
		'height': '30px',
		'text-align': 'center',
		'border': '1px solid #C6BAC7',
		'line-height': '30px',
		'overflow': 'hidden',
		'color': 'black',
		'font-size': '16px',
		'font-weight': 'bold',
	})
	$("#"+id).text(text);
	$("#"+id).fadeIn("1000");
	 setTimeout(function () {
	        $("#"+id).fadeOut("1000");
	    }, 2000);
}

function deleteData(gridName, url, pastdata) {
	if (!isSelectRow(gridName))
		return;
	layer.confirm('确定要删除么？', {
		btn: ['确定','取消'] //按钮
	}, function(){
		$.ajax({
			url : url,
			type : "post",
			async : false,
			data : pastdata,
			dataType : "json",
			success : function(result) {
				
				// 刷新表格
				if (result > 0) {
					layer.alert("删除成功！");
					$.jgrid.gridUnload(gridName);
					doSearch();
				} else {
					layer.alert("执行删除失败");
				}
			},
			error : function() {
				layer.alert("执行删除失败");
			}
		});
	}, function(){
		return;
	});
}

function getNowDate(){
	var d = new Date();
	var mon = d.getMonth()+1;
    if (mon < 10) {
    	mon = '0' + mon;
    }
	return d.getFullYear()+"-"+mon+"-"+d.getDate();
}

function getNowMonth(){
	var d = new Date();
	var mon = d.getMonth()+1;
    if (mon < 10) {
    	mon = '0' + mon;
    }
	
	return d.getFullYear()+""+mon;
}
/**
 * 前一个月
 * @param oldMonth YYYYMM格式的月份
 * @returns {String}
 */
function priorOneMonth(oldMonth){
	var year = oldMonth.substring(0,4);
	var month =  oldMonth.substring(4,6);
	var days = new Date(year, month, 0);	
	 days = days.getDate(); //获取当前日期中月的天数
     var year2 = year;
     var month2 = parseInt(month) - 1;
     if (month2 == 0) {
         year2 = parseInt(year2) - 1;
         month2 = 12;
     }
     
     if (month2 < 10) {
         month2 = '0' + month2;
     }
     var t2 = year2 +"" + month2;
    // alert(t2);
     return t2;	
}

/**
 * 下一个月
 * @param oldMonth YYYYMM格式的月份
 * @returns {String}
 */
function nextOneMonth(oldMonth){
	var year = oldMonth.substring(0,4);
	var month =  oldMonth.substring(4,6);
	var days = new Date(year, month, 0);	
	 days = days.getDate(); //获取当前日期中月的天数
     var year2 = year;
     var month2 = parseInt(month) + 1;
     if (month2 == 13) {
         year2 = parseInt(year2) + 1;
         month2 = 1;
     }
     if (month2 < 10) {
         month2 = '0' + month2;
     }
     var t2 = year2 +"" + month2;
    // alert(t2);
     return t2;	
}
/**
 * 设置前一月
 * @param ctrl
 */
function setPriorMonth(ctrl){
	var nowMonth = $("#"+ctrl).val();
	if (nowMonth==""){
		$("#"+ctrl).val(getNowMonth);
		return;
	}
	var mon = priorOneMonth(nowMonth);
	 $("#"+ctrl).val(mon);
}
/**
 * 设置后一个月
 * @param ctrl
 */
function setNextMonth(ctrl){
	var nowMonth = $("#"+ctrl).val();
	if (nowMonth==""){
		$("#"+ctrl).val(getNowMonth);
		return;
	}
	var mon = nextOneMonth(nowMonth);
	 $("#"+ctrl).val(mon);
}


function setNextDay(ctrl){
	var today = $("#"+ctrl).val();
	if (today==""){
		$("#"+ctrl).val(getNowDate());
		return;
	}
	
	var t = new Date(today);
	var tm = new Date(t.getFullYear(),t.getMonth(),t.getDate()+1);

	var m='0'+(tm.getMonth()+1);
	var d='0'+tm.getDate()

	$("#"+ctrl).val(tm.getFullYear()+'-'+m.substr(m.length-2)+'-'+d.substr(d.length-2));

}


function setPriorDay(ctrl){
	var today = $("#"+ctrl).val();
	if (today==""){
		$("#"+ctrl).val(getNowDate());
		return;
	}
	var t = new Date(today);
	var yesterday_milliseconds = t.getTime()-1000*60*60*24;
	var yesterday = new Date();
	yesterday.setTime(yesterday_milliseconds);
	var strYear = yesterday.getFullYear();
	var strDay = yesterday.getDate();
	var strMonth = yesterday.getMonth()+1;
	if(strMonth<10)
	{strMonth="0"+strMonth;}
	if(strDay<10)
	{strDay="0"+strDay}
	datastr = strYear+"-"+strMonth+"-"+strDay;
	$("#"+ctrl).val(datastr);
	}

function getDeviceCateg(device_code){	
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/deviceCon/getDeviceCateg",
		type : "post",
		async : false,
		data : {
			code : device_code
		},
		dataType : "json", 
		success : function(result) {
				resultvalue = result.ret_msg;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("加载失败"+textStatus);
			 //alert(XMLHttpRequest.status);
             //alert(XMLHttpRequest.readyState);
             //alert(textStatus);			
		}
	});
	return resultvalue;	
}



function getDeviceParamLimit(deviceCode, paramCode){
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/deviceParamCon/search",
		type : "post",
		async : false,
		data : {
			device_code : deviceCode,
			param_code: paramCode
		},
		dataType : "json", 
		success : function(result) {
			resultvalue = result;
			//return setDeviceParamLimit(result);
			//	resultvalue = result.ret_msg;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("加载失败"+textStatus);
			 //alert(XMLHttpRequest.status);
             //alert(XMLHttpRequest.readyState);
             //alert(textStatus);			
		}
	});
	return resultvalue;	
	
}

function setDeviceParamLimit(deviceCode, paramCode){
	var list = getDeviceParamLimit(deviceCode, paramCode);
	//var topValue = 100;
	if (list.length>0){
		return {"top_value":list[0].top_value,"bottom_value":list[0].bottom_value};
	} else {
		return {"top_value":100,"bottom_value":0};
	}

}

/**
 * 获取设备参数
 * @param device_code
 * @returns
 */
function getDeviceParam(device_code){
	var resultvalue;
	$.ajax({
		url : getContextPath()+"/monitoCon/deviceDetail",
		type : "post",
		async : false,
		data : {
			code : device_code
		},
		dataType : "json",
 
		success : function(result) {
				resultvalue = result;
		},
		error : function() {
			alert("加载失败");
		}
	});
	return resultvalue;		
}
/**
 * 将设备参数组成JSON格式
 * @param device_code
 */
function parseDeviceParam(device_code){
	var deviceParams = getDeviceParam(device_code);
	var json = {};
	var detail = deviceParams.detail;
	
	for(var i=0;i<detail.length;i++){
		var name = detail[i].param_code||"";
		var value= detail[i].param_value||"";
		var unit = detail[i].param_unit||"";
		json[name] = value+unit;
	}	
	return json;
	//alert(json.CPU_USAGE_PERCENT);
}

/**
 * 将设备参数组成JSON格式，不带单位
 * @param device_code
 */
function parseDeviceParam_1(device_code){
	var deviceParams = getDeviceParam(device_code);
	var json = {};
	var detail = deviceParams.detail;
	
	for(var i=0;i<detail.length;i++){
		var name = detail[i].param_code||"";
		var value= detail[i].param_value||"";
		json[name] = value;
	}	
	return json;
	//alert(json.CPU_USAGE_PERCENT);
}

jQuery.ajaxSetup({
	contentType : "application/x-www-form-urlencoded;charset=utf-8",
	complete : function(XMLHttpRequest, textStatus) {
		var sessionstatus = XMLHttpRequest
				.getResponseHeader("requeststatus"); // 通过XMLHttpRequest取得响应头，sessionstatus，
		if (sessionstatus == "yuequan") {
			// 如果超时就处理 ，指定要跳转的页面
			window.location.replace("/RtManageCon/jsps/errorPages/error.jsp");
		}
	}
});

/**
 * 特殊字符检测
 * @param ctrlName 控件名称
 * @param info  提示信息
 * @param nullAble 是否允许为空
 * @returns {Boolean}
 */
function validateSpecialChar(ctrlName, info, nullAble){
	var aValue = $(ctrlName).val().trim();
	if ((aValue == "")&&(!nullAble)) {
		alert(info+"不能为空！");
		return false;
	}
	if (aValue.length != 0) {
		var exp = /[@'\"$&^*{}<>\\\:\;]+/;
		var reg = aValue.match(exp);
		if (reg) {
			alert(info+"不能含有特殊字符！");
			return false;
		} 
	}
	return true;
	
}
/**
 * 数字字段验证
 * @param ctrlName 控件名称
 * @param info 提示信息
 * @param nullAble 是否允许为空
 * @returns {Boolean}
 */
function validateNumberField(ctrlName, info, nullAble){

	var aValue = $(ctrlName).val().trim();
	if ((aValue == "")&&(!nullAble)) {
		alert(info+"不能为空！");
		return false;
	}
	if (aValue.length != 0 ) {
		var exp = "^[0-9]*$";
		var re = new RegExp(exp);
		var reg = aValue.search(re);
		if(reg == -1){
			alert(info + "只能为数字！");
			return false;
		}
	}
	return true;
}
/**
 * IP字段验证
 * @param ctrlName 控件名称
 * @param info 提示信息
 * @param nullAble 是否允许为空
 * @returns {Boolean}
 */
function validateIpField(ctrlName, info, nullAble){

	var aValue = $(ctrlName).val().trim();
	if (aValue == "") {
		layer.alert(info+"不能为空！");
		return false;
	}
	if(aValue.length != 0){
		var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		var reg = aValue.match(exp);
		if(reg == null){
			layer.alert(info+"格式不正确！");
			return false;
		}
	}
    return true;
}

//获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == ""){
	    url = window.location.search;
    }else{	
    	url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]); return null;
}

/**
 * 验证是否为空
 * @param ctrlName 控件名称
 * @param info  提示信息
 * @param nullAble 是否允许为空
 * @returns {Boolean}
 */
function validateNull(ctrlName, info, nullAble){
	var aValue = $(ctrlName).val().trim();
	if ((aValue == "")&&(!nullAble)) {
		alert(info+"不能为空！");
		return false;
	}
	return true;
}

//获取session，判断是否存在，
function getSession(){
	var res;
	$.ajax({
		url:getContextPath()+"/login/getSession",
		type:"post",
		data:{},
		datatype:"json",
		async:false,
		success:function (data){
			res = data;
		}
	});
	return res;
}

//验证数字位数
/*function validateNumLength(str){
	var val = $(str).val();
	if(val.length > 8){
		return "位数不能超过8位";
	}
}*/

//获取JWT
function getJWT(){
	var res;
	$.ajax({
		url:getContextPath()+"/commonCon/createJWT",
		type:"post",
		data:{},
		datatype:"json",
		async:false,
		success:function (data){
			$("#jwt").val(data);
			res = data;
		}
	});
	return res;
}

//获取随机数
function echartnum(){
	var seed = new Date().getTime();
	seed = (seed*9301+49297) % 233280;
	return Math.ceil(seed/233280.0 * 10000000000000000) * 0.0000000000000001;
}
//统一返回监控主页
function goMonBack(){
	var path=getContextPath()+"/jsp/pages/monitormgt/monihome/monihome.jsp";
	$("#tt").load(path);
}