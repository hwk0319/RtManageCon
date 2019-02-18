<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsps/css/style.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>步骤</title>
</head>
	<body style="margin: 0 0 50px 10px;">
	<div style="width: 95%;border:1px solid #ddd;margin:2%;position:relative;">
	  <table cellspacing="10">
	  <tbody>
			<tr style="width: 45%;float: left;margin-top:5%;margin-left:5%;">
				<td style="">模板名称：</td>
				<td><input type="text" id="name" /></td>
			</tr>
			<tr  style="width: 45%;float: left; margin-top:5%;margin-left:5%;">
				<td>模板类型：</td>
				<td><input type="text" id="type" /></td>
			</tr>
			<tr  style="width: 45%;float: left;margin-top:5%;margin-left:5%;">
				<td>描述： </td>
				<td colspan='2'><textarea type="text" id="desc" style="margin-left:15%;width: 184px;"/></td>
			</tr>
			<tr>
				<td><button class="btn" id="nextStep" style="float: right;margin-top: 5%;margin-right: 2%;margin-bottom: 2%;">下一步</button></td>
				<td></td>
			</tr>
		</tbody>
	  </table>
	</div>
 
</body>
<script>
  $(function() {
    //用户已经提交置灰。
    if (hasConfirm == true) {
      disableAllInput();
    }
  });

  $('#nextStep').bind("click", function() {
    var tplName = $('#name').val();
    var tplModul = $('#type').val();
    var tplDesc = $('#desc').val();
    if (!(checkStrIsNotEmpty(tplName) || checkStrIsNotEmpty(tplModul) || checkStrIsNotEmpty(tplDesc))) {
      layer.msg("数据不能为空！");
      return;
    }
    taskId = addNewTaskTpl(tplName, tplModul, tplDesc);
    hasConfirm = true;
    disableAllInput();
  });
  
  

  function disableAllInput() {
      $('[type=text]').each(function(){
        $(this).attr("disabled", "false");
      });
      $('#nextStep').attr("disabled", "false");
  }
  
  function addNewTaskTpl(name,modul,desc)
  {
    var taskId;
    $.ajax({
      type: 'POST',
      url: getContextPath() + "/taskmanage/taskOperationTemplate",
      dataType: 'json',
      async: false,//设置为同步
      data: {
        "name": name,
        "modul": modul,
        "poDesc":desc,
        "oper":"add"
      },
      success: function(data) {
        var taskId =  data.value.id;
        $("#shareTaskIdTpl",parent.document).val(taskId);
      },
      error: function(text) {
        layer.alert(text);
      }
    });
    return taskId;
  }
  
  function checkStrIsNotEmpty(str)
  {
    if(str == null || str.length==0)
      {
      return false;
      }
    return true;
  }
</script>
</html>