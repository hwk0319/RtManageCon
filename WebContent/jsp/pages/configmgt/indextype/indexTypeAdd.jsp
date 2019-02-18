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
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
<title>指标项</title>
  <style type="text/css">
		input[type='text'],input[type='number']{
			height: 23px;
			width: 182px;
		}
		.remark{
			text-overflow: ellipsis;
			white-space: nowrap;
		}
    </style>
</head>
<body>
	<div style="width:100%;border:solid thin #d1d5de;margin-left: 5px;width: 99%;">
			<div class="title" style='background-color: #f6f8fa; font-size: 13px; height: 23px; line-height: 20px;padding-left: 3px;'>指标分类</div>
			<div style="width:100%;overflow-y: auto;">
				<table style="font-size: 13px;width: 100%;">
					<tbody>
						<tr>
							<td width="110px">指标分类ID：</td>
							<td><input type="text" id="indextype_id"  min="0" value="" validate="callback nn num" callback="validateNum();" placeholder="请输入指标分类ID"></td>
							<td width="110px">名称：</td>
							<td><input type="text" id="name" value="" validate="nn nspcl" maxlength="15" placeholder="请输入名称"></td>
						</tr>
						<tr>
							<td>描述：</td>
							<td><input type="text" id="remark" value="" validate="nspcl" maxlength="35" placeholder="请输入描述"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	<div class="index_cong" style="width:99%;height:330px;margin:auto;border:solid 1px #d1d5de;overflow:hidden;margin-top:5px;">
		<span style='background-color:#f6f8fa;font-size:13px;line-height:28px;display:block;padding-left: 3px;'>指标项<span style="cursor:pointer;float:right;" class="addIndex"><i class='icon-plus-sign'></i>&nbsp;新增&nbsp;</span></span>
<!-- 		<div class="addIndex" style='display:block;width:25px;height:25px;position:absolute;right:0;margin-right:15px;margin-top:-27px;cursor:pointer;color:#fff;background:#bbbbbb;border-radius:25px;text-align:center;line-height:23px;font-size:30px;'>+</div> -->
		<div style="width:100%;margin:0 auto;height:300px;overflow:auto;" id="indexInfo"></div>
	</div>
	<div class="widget-content" style="text-align:center;">
	<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
		<p>
			<button class="btn" onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" onclick="btnClose()"><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
	<input type="hidden" id="indexTypeId" value=""/>
</body>
<script type="text/javascript">
	$(function(){
		$(".index_cong .addIndex").click(function(){
			show_index(this,'I');
		});
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  if(id != 'undefined'){
		  $("#indexTypeId").val(id);
	  	 setDetailValue(id);
	  	 doindTypeCong("E");
	  };
	  mod();
	});
	//编辑设置value
	function setDetailValue(id) {
		var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
		if (rs.length>0){
			$("#indextype_id").val(rs[0].indextype_id);
			$("#name").val(rs[0].name);
			$("#remark").val(rs[0].remark);
// 			$("#obj_type").val(rs[0].obj_type);
	    }
	}
	//编辑加载数据
	function load(id) {
		var resultvalue;
		$.ajax({
			url : getContextPath() + "/indextypeCon/search",
			type : "post",
			async : false,
			data : {
				"id" : id
			},
			dataType : "json",
			success : function(result) {
				resultvalue = result;
			},
			error : function() {
				layer.alert("数据加载失败！" , {
					title: "提示"
				});
			}
		});
		return resultvalue;
	}
	//验证数字位数
	function validateNum(){
		var val = $("#indextype_id").val();
		if(val.length > 5){
			return "位数不能超过5位";
		}else if(val < 0){
			return "请输入大于0的数字";
		}
	}
</script>
</html>
