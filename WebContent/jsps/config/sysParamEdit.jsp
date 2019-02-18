<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/jsps/include/head.jsp"%>    
<%@ include file="/jsps/include/taglib.jsp"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/jsps/js/config/sysParamEdit.js"></script>
<title>系统参数编辑</title>
</head>
<body>
	  <table class="table-edit">
	  <tbody>
	    <tr>
	      <td nowrap>参数编号：</td><td><input disabled="true" type="text" id="code" /></td>
	      <td nowrap>参数名称：</td><td><input disabled="true" type="text" id="name" /></td>
	    </tr>
	    <tr>
	    
	      <td>参数值：</td><td id="tdParamValue"><input type="text" id="value" />
	      </td>
	      <td>参数单位：</td><td><input type="text" id="unit" /></td>
	    </tr>
	    <tr>
	      <td>说明：</td><td colspan="3"><textarea style="height:150px;width:100%"  id="description" ></textarea></td>
	      <input type="hidden" id="id" />
	      <input type="hidden" id="value_code" />
	    </tr>
	   </tbody>
	  </table>
		<div class="widget-content">
			<p>
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
			</p>
		</div>
</body>
<script type="text/javascript">
$(function() {
	//addDrop();//初始化类型下拉框
	setDetailValue();
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	});
});


function outParamField(code_type, options){
	if (code_type==""||code_type==null){
		document.getElementById("tdParamValue").innerHTML="<input type=\"text\" id=\"value\" />"
	} else {
		var ss = "<select id=\"value\">"+options+"<select/>";
		document.getElementById("tdParamValue").innerHTML=ss;
	}
}

</script>


</html>