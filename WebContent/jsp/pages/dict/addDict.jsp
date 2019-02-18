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
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/dict/js/dict.js"></script>
<title>数据字典</title>
<style>
	input[type='number'],input[type='text']{
		height: 23px;
	}
	table tr{
		height:30px;
	}
</style>
</head>
<body>
	<table style="align: left; width: 80%;margin-left: 15%;font-size: 13px;margin-bottom: 5%;" >
		<tbody>
			<tr>
				<td style="width:100px;">类型：</td>
				<td>
					<input type="text" id="type" validate="nn nspcl" maxlength="15" value=""/>
				</td>
			</tr>
			<tr>
				<td style="width:100px;">标签：</td>
				<td>
					<input type="text" id="label" validate="nn nspcl" maxlength="15" value=""/>
				</td>
			</tr>
			<tr>
				<td style="width:100px;">名称：</td>
				<td>
					<input type="text" id="name" validate="nn nspcl" maxlength="15" value=""/>
				</td>
			</tr>
			<tr>
				<td style="width:100px;">值：</td>
				<td>
					<input type="text" id="value" validate="nn nspcl" maxlength="15" value=""/>
				</td>
			</tr>
			<tr>
				<td style="width:100px;">排序：</td>
				<td>
					<input type="text" id="sort" validate="num callback" callback="validateNum();" value=""/>
				</td>
			</tr>
			<tr>
				<td style="width:100px;">描述：</td>
				<td>
					<input type="text" id="description" validate="nspcl" maxlength="50" value=""/>
				</td>
			</tr>
			
			<input type="hidden" id="id"/>
		</tbody>
	</table>
	<div class="widget-content" style="">
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
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  if(id != 'undefined'){
		  setDetailValued(id);
	  }
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
});

function doSavelogrl(){
	if(!validateHandler()){
		return;
	}
	var index;
	var id = $("#id").val();
	var type = $("#type").val();
	var label = $("#label").val();
	var value = $("#value").val();
	var name = $("#name").val();
	var description = $("#description").val();
	var sort = $("#sort").val();
	if(id != ""){
		oper = "edit";
	}else{
		oper = "add";
	}
	//拼接数据字段
  	var mData = ""+id+type+label+value+name+description+"";
		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	
	  	$.ajax({
	  		url : getContextPath()+"/dictCon/update",
	  		type : "post",
	  		data : {
	  			"oper" : oper,
	  			"id" : id,
	  			"type" : type,
	  			"value" : value,
	  			"label" : label,
	  			"sort" : sort,
	  			"name" : name,
	  			"description" : description,
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
							window.parent.doSearch();
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

//编辑设置value
function setDetailValued(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		//密码回填
		$("#id").val(rs[0].id);
		$("#type").val(rs[0].type);
		$("#label").val(rs[0].label);
		$("#value").val(rs[0].value);
		$("#name").val(rs[0].name);
		$("#description").val(rs[0].description);
		$("#sort").val(rs[0].sort);
    }
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/dictCon/search",
		type : "post",
		async : false,
		data : {
			"id":id
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
function validateNum(){
	var val = $("#sort").val();
	if(val.length > 5){
		return "位数不能超过5位";
	}
}
</script>
</html>

