<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" id="cssLoad"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/operation_log/js/operation_log.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<title>设置存储容量</title>
<style>
input[type='number']{
	width:168px;
	height:23px;
}
</style>
</head>
<body>
	<table style="align: left; width: 80%;margin-top:5%;margin-left: 50px;font-size: 13px;margin-bottom: 10%;" >
		<tbody>
			<tr>
				<td style="width:100px;">存储容量：</td>
				<td>
					<input type="text" id="capacity"  min="0" validate="callback nn num" callback="validateNum('#capacity');" value=""/>
				</td>
			</tr>
			<tr>
				<td style="width:100px;">单位：</td>
				<td>
					<select id="danwei" validate="nn" style="width:182px; font-size: 13px;">
					<option value="">--请选择--</option>
					<option value="GB">GB</option>
					<option value="MB">MB</option>
				</select>
				</td>
			</tr>
			<input type="hidden" id="id"/>
		</tbody>
	</table>
	<div class="widget-content" style="margin-top:5%;">
	<hr class="ui-widget-content" style="margin:1px">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSavelogrl();"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script>
var modulus;
var jwt;
$(function() {
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
	  setDetailValue();
});

function doSavelogrl(){
	if(!validateHandler()){
		return;
	}
	var index;
	var value = $("#capacity").val() + $("#danwei").val();
	//拼接数据字段
	var id = $("#id").val();
	var type = "oprtlog_capacity";
	var label = "日志容量";
	var sort = "1";
	var name = "操作日志容量";
  	var mData = ""+id+type+label+value+name+"";
		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	if(id == ""){
	  	$.ajax({
	  		url : getContextPath()+"/dictCon/update",
	  		type : "post",
	  		data : {
	  			"oper" : "add",
	  			"id" : id,
	  			"type" : type,
	  			"value" : value,
	  			"label" : label,
	  			"sort" : sort,
	  			"name" : name,
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
	  			for(key in result){
	  				if(key == "success"){
		  				layer.alert(result[key], {
							title: "提示"
						},function(){
							layer.close(index);
							parent.layer.closeAll();
						});
	  				}else{
	  					layer.alert(result[key]);
	  				}
	  			}
	  		},
	  		error : function() {
	  			layer.alert('保存失败！');
	  			layer.close(index);
	  		}
	  	});
	}else{
		$.ajax({
	  		url : getContextPath()+"/dictCon/update",
	  		type : "post",
	  		data : {
	  			"oper" : "edit",
	  			"id" : id,
	  			"type" : type,
	  			"value" : value,
	  			"label" : label,
	  			"sort" : sort,
	  			"name" : name,
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
	  			for(key in result){
	  				if(key == "success"){
		  				layer.alert(result[key], {
							title: "提示"
						},function(){
							layer.close(index);
							parent.layer.closeAll();
						});
	  				}else{
	  					layer.alert(result[key]);
	  				}
	  			}
	  		},
	  		error : function() {
	  			layer.alert('保存失败！');
	  			layer.close(index);
	  		}
	  	});
	}
}

//编辑设置value
function setDetailValue() {
	var rs = load();//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		//密码回填
		$("#id").val(rs[0].id);
		$("#capacity").val(rs[0].value.replace(/[^0-9]/ig,""));
		var dw = rs[0].value.replace(/[^A-Z]/ig,"");
		$("#danwei option[value='"+dw+"']").attr("selected",true);
    }
}
//编辑加载数据
function load() {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/operationlogCon/searchCon",
		type : "post",
		async : false,
		data : {
			"type":"oprtlog_capacity"
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

//验证数字位数
function validateNum(id){
	var val = $(id).val();
	if(val.length > 5){
		return "存储容量位数不能超过5位!";
	}else if(val < 0){
		return "请输入大于0的数字!";
	}
}
</script>
</html>

