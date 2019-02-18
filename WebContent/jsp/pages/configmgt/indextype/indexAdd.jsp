<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/indextype/js/indextype.js"></script> 
<title>指标项</title>
  <style type="text/css">
        .line{
            height: 25px;
            line-height: 25px;
            margin: 3px;
        }
        .imp{
            padding-left: 25px;
        }
        .col{
            width: 95px;
        }
        ul {
            list-style:none;
            padding-left:10px;
        }
        li {
            height:20px;
        }
		input[type='text'],input[type='number']{
			height: 23px;
			width: 168px;
		}
		table tr{
			height: 40px;
		}
    </style>
</head>
<body>
	<table style="font-size: 13px;">
		<tbody>
			<tr>
				<td>指标项ID：</td>
				<td><input type="text" id="index_id" min="0" value="" validate="callback num nn" callback="validateNum('#index_id');" placeholder="请输入指标项ID"/></td>
				<td>描述：</td>
				<td><input type="text" id="description" value="" validate="nn nspcl" maxlength="60" placeholder="请输入描述"/></td>
			</tr>
			<tr>
				<td>使用方式：</td>
				<td>
				  <select id="warn_rule" onchange="changeInput(this)" style="width: 182px;font-size: 13px;" validate="nn">
				  	<option value="">--请选择--</option>
				  	<option value="0">不用</option>
				  	<option value="1">上下阙值</option>
				  	<option value="2">标准值</option>
				  </select>
                </td>
				<td>阙值上限：</td>
				<td><input type="text" id="upper_limit" value="" validate="callback num" callback="validateNum('#upper_limit');" placeholder="请输入阙值上限"/><span class='upValFlag' style="color: red"></span>
				</td>
			</tr>
			<tr>
				<td>阙值下限：</td>
				<td><input type="text" id="lower_limit" value="" validate="callback num" maxlength="10" callback="upDownVal()" placeholder="请输入阙值下限"/><span class='downValFlag' style="color: red"></span>
				</td>
				<td>标准值：</td>
				<td><input type="text" id="std_value" validate="" maxlength="60" value="" placeholder="请输入标准值"/><span class='stdValFlag' style="color: red"></span>
				</td>
			</tr>
			<tr>
				<td>详细阐述：</td>
				<td><textarea id="remark" style="width: 175px;height: 40px;" maxlength="100" validate="nspcl" placeholder="请输入详细阐述"></textarea></td>
			</tr>
			<input type="hidden" id="id" value=""/>
			<input type="hidden" id="divIndex" value=""/>
			<input type="hidden" id="warn_rule" value=""/>
		</tbody>
	</table>
</body>
<script type="text/javascript">
  $(function(){
	  //设置值
	 setIndexValue();
	  //使用方式改变事件
	 $("#warn_rule").change(function(){
		 var val = $(this).val();
		 if(val == 1){
			 $("#upper_limit").attr("validate","nn callback");
			 $("#lower_limit").attr("validate","nn callback");
			 $("#std_value").attr("validate","");
			 //添加必填标识
			 $(".upValFlag").html("*");
			 $(".downValFlag").html("*");
			 $(".stdValFlag").html("");
		 }else if(val == 2){
			 $("#std_value").attr("validate","nspcl nn");
			 $("#upper_limit").attr("validate","");
			 $("#lower_limit").attr("validate","");
			 //添加必填标识
			 $(".stdValFlag").html("*");
			 $(".upValFlag").html("");
			 $(".downValFlag").html("");
		 }else{
			 $("#upper_limit").attr("validate","");
			 $("#lower_limit").attr("validate","");
			 $("#std_value").attr("validate","");
			 $("#upper_limit").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
			 $("#lower_limit").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
			 $("#std_value").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
			 //移除必填标识
			 $(".upValFlag").html("");
			 $(".downValFlag").html("");
			 $(".stdValFlag").html("");
		 }
	 }); 
	  
	  //如果使用方式为不用，置灰文本框
	 var warn_rule = $("#warn_rule").val();
	 if(warn_rule == 0){
		 $("#upper_limit").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
		 $("#lower_limit").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
		 $("#std_value").val("").attr("readonly","readonly").css({"background-color":"#F5F5F5"});
	 }
  });
	
  var warn_rule = $("#warn_rule").val();
  $("#warn_rule").val(warn_rule);
  changeInput($("#warn_rule"));
  
  function upDownVal(){
	  var warn_rule = $("#warn_rule").val();
	  var upper_limit = parseFloat($("#upper_limit").val());
	  var lower_limit = parseFloat($("#lower_limit").val());
	  if(warn_rule == 1){
		  if(lower_limit > upper_limit){
			  return "阈值下限不能大于阈值上限!";
		  }
	  }
  }
  
	//验证数字位数
	function validateNum(str){
		var val = $(str).val();
		if(val.length > 8){
			return "位数不能超过8位";
		}else if(val < 0){
			return "请输入大于0的数字";
		}
	}
	
	//编辑设置值
	function setIndexValue(){
		var index_id = $.getUrlParam("index_id");
		$("#index_id").val(index_id);
		var description = $.getUrlParam("description");
		$("#description").val(description);
		var upper_limit = $.getUrlParam("upper_limit");
		$("#upper_limit").val(upper_limit);
		var lower_limit = $.getUrlParam("lower_limit");
		$("#lower_limit").val(lower_limit);
		var std_value = $.getUrlParam("std_value");
		$("#std_value").val(std_value);
		var remark = $.getUrlParam("remark");
		$("#remark").val(remark);
		var id = $.getUrlParam("id");
		$("#id").val(id);
		var divIndex = $.getUrlParam("divIndex");
		$("#divIndex").val(divIndex);
		var warn_rule = $.getUrlParam("warn_rule");
		$("#warn_rule").val(warn_rule);
	}
</script>
</html>
