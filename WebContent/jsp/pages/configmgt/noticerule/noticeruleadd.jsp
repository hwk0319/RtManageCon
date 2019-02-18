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
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/noticerule/js/noticeruleadd.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<style>
	input[type='text']{
		width:168px;
	}
	table tr {
	    height: 40px;
	}
</style>
<title>通知规则</title>
</head>
<body>
	<div style="">
	<table style="align: left; width: 80%;margin-left: 80px;font-size: 13px;margin-left: 20%;margin-top: 5%;" >
		<tbody>
			<tr>
				<td style="width:100px;">告警类型：</td>
				<td>
					<select id="warntype" validate="nn" style="width:182px; font-size: 13px;">
						<option>--请选择--</option>
						<option value="1">编码</option>
						<option value="2">设备</option>
						<option value="3">软件系统</option>
					</select>
				</td>
			</tr>
			<tr>
				<td style="">告警等级：</td>
				<td>
					<select id="warnlevel"  validate="nn" style="width:182px; font-size: 13px;">
						<option>--请选择--</option>
						<option value="1">信息</option>
						<option value="2">警告</option>
						<option value="3">错误</option>
						<option value="4">致命</option>
					</select>
				</td>
			</tr>
			<tr>
				<td style="">联系人姓名：</td>
				<td>
					<input type="text" id="contactname" value="" placeholder="请点击添加" onclick="addressList();" validate="nn" readOnly  style="width:168px;height: 23px; font-size: 13px;cursor:pointer;">
					<input type="hidden" id="nameId" value="">
				</td>
			</tr>
			<tr>
				<td style="">通知方式：</td>
				<td>
					<select id="noticeway"  validate="nn" style="width:182px; font-size: 13px;">
						<option>--请选择--</option>
						<option value="1">SMTP</option>
						<option value="2">短信</option>
					</select>
				</td>
			</tr>
			<input type="hidden" id="id"/>
			<input type="hidden" id="num"/>
		</tbody>
	</table>
	</div>
	<div class="widget-content" style="margin-top: 10%;">
	<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script>
//添加联系人
function addressList(){
	layer.open({
  	    type: 2,
  	    title: '', 
  	    id: "addressUsr",
  	    shadeClose: false,
  	    closeBtn: 0,
  	    moveOut:true,
  	    area:['550px','345px'],
//   	    area:['90%','100%'],
  	    btn: ['确定', '取消'],
	 	yes: function(index, layero){
	         var iframeWin = window[layero.find('iframe')[0]['name']];
	        	 iframeWin.doSave();
	    },
	    btn2: function(){
	       	 layer.close();
	    },
  	    content: [getContextPath()+"/jsp/comm/jsp/addressComm.jsp?userid="+"hun",'no']
  	});
}
</script>
</html>

