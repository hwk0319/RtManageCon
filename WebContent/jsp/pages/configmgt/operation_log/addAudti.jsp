<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!-- 编辑使用 -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<script src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
	<!--这个是所有jquery插件的基础，首先第一个引入-->
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
	<style>
		input[type='text']{
			width: 182px;
		}
	</style>
	<title>审计策略</title>
  </head>
  <body>
	<table style="align: left; width: 80%;margin-top:5%;margin-left: 80px;font-size: 13px;margin-bottom: 5%;" >
		<tbody>
			<tr>
				<td style="width:100px;">一级菜单：</td>
				<td>
					<select id="name" validate="nn" style="width:182px; font-size: 13px;">
						<option>--请选择--</option>
						<option value="1">配置管理</option>
						<option value="2">任务管理</option>
						<option value="3">异常告警</option>
						<option value="4">系统优化</option>
						<option value="5">日志分析</option>
						<option value="6">系统管理</option>
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:100px;">二级菜单：</td>
				<td>
					<select id="erjiname" validate="nn" style="width:182px; font-size: 13px;">
						<option>--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td style="">是否审计：</td>
				<td>
					<select id="isAudit"  validate="nn" style="width:182px; font-size: 13px;">
						<option>--请选择--</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</td>
			</tr>
			<tr>
				<td style="">审计内容：</td>
				<td>
					<div id="auditThoseDiv">
						<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;
						<input type="checkbox" validate="" name="auditThose" value="新增"/>新增&nbsp;
						<input type="checkbox" validate="nn" name="auditThose" value="修改" checked disabled/>修改&nbsp;
						<input type="checkbox" validate="nn" name="auditThose" value="删除" checked disabled/>删除&nbsp;
					</div>
				</td>
			</tr>
			<input type="hidden" id="id" value=""/>
		</tbody>
	</table>
	<div class="widget-content" style="margin-top:5%;">
	<hr class="ui-widget-content" style="margin:1px">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 保存</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script type="text/javascript">
  var modulus;
  var jwt;
  $(function(){
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
      //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
		
	  $("#name").change(function(){
		  var val = $(this).val();
		  var htm = "";
		  if(val == "1"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='设备管理'>设备管理</option>";
			  htm += "<option value='软件系统管理'>软件系统管理</option>";
			  htm += "<option value='组管理'>组管理</option>";
			  htm += "<option value='双活管理'>双活管理</option>";
			  htm += "<option value='设备型号管理'>设备型号管理</option>";
			  htm += "<option value='设备监控管理'>设备监控管理</option>";
			  htm += "<option value='告警规则管理'>告警规则管理</option>";
			  htm += "<option value='通知规则管理'>通知规则管理</option>";
			  htm += "<option value='指标分类'>指标分类</option>";
			  htm += "<option value='联系人管理'>联系人管理</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "2"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='任务列表'>任务列表</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "3"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='异常故障'>异常故障</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "4"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='健康评分'>健康评分</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "5"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='操作日志'>操作日志</option>";
			  htm += "<option value='系统日志'>系统日志</option>";
			  htm += "<option value='审计策略'>审计策略</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "6"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='菜单管理'>菜单管理</option>";
			  htm += "<option value='角色管理'>角色管理</option>";
			  htm += "<option value='数据字典'>数据字典</option>";
			  $("#erjiname").html(htm);
		  }else{
			  htm = "";
			  htm = "<option value=''>--请选择--</option>";
			  $("#erjiname").html(htm);
		  }
	  });
	  
	  $("#erjiname").change(function(){
		  var val = $(this).val();
		  if(val == "任务列表"){
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  htm += "<input type='checkbox' validate='' name='auditThose' value='新增'/>新增&nbsp;";
		  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='修改' checked disabled/><span class='_validate_flag' style='color: red'>*</span>修改&nbsp;";
		  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='删除' checked disabled/><span class='_validate_flag' style='color: red'>*</span>删除&nbsp;";
		  	  htm += "<input type='checkbox' validate='' name='auditThose' value='执行'/>执行&nbsp;";
		  	  htm += "<input type='checkbox' validate='' name='auditThose' value='停止'/>停止&nbsp;";
			  $("#auditThoseDiv").html(htm);
		  }else if(val == "健康评分"){
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  htm += "<input type='checkbox' validate='' name='auditThose' value='新增'/>新增&nbsp;";
		  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='修改' checked disabled/><span class='_validate_flag' style='color: red'>*</span>修改&nbsp;";
		  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='删除' checked disabled/><span class='_validate_flag' style='color: red'>*</span>删除&nbsp;";
		  	  htm += "<input type='checkbox' validate='' name='auditThose' value='评分'/>评分&nbsp;";
		  	  htm += "<input type='checkbox' validate='' name='auditThose' value='停止'/>停止&nbsp;";
		  	  $("#auditThoseDiv").html(htm);
		  }else if(val == "操作日志"){
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
		  	  htm += "<input type='checkbox' validate='' name='auditThose' value='统计'/>统计&nbsp;";
		  	  htm += "<input type='checkbox' validate='' name='auditThose' value='导出Excel'/>导出Excel&nbsp;";
		  	  $("#auditThoseDiv").html(htm);
		  }else if(val == "系统日志"){
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
		  	  $("#auditThoseDiv").html(htm);
		  }else if(val == "异常故障"){
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  htm += "<input type='checkbox' validate='' name='auditThose' value='确认'/>确认&nbsp;";
			  htm += "<input type='checkbox' validate='' name='auditThose' value='消除'/>消除&nbsp;";
		  	  htm += "<input type='checkbox' validate='' name='auditThose' value='忽略'/>忽略&nbsp;";
		  	  $("#auditThoseDiv").html(htm);
		  }else{
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='新增'/>新增&nbsp;";
			  htm += "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
		  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='修改' checked disabled/><span class='_validate_flag' style='color: red'>*</span>修改&nbsp;";
		  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='删除' checked disabled/><span class='_validate_flag' style='color: red'>*</span>删除&nbsp;";
		  	  $("#auditThoseDiv").html(htm);
		  }
	  });
	  
	  $("#isAudit").change(function(){
		  var erjiname = $("#erjiname").val();
		  var isAudit = $(this).val(); 
		  if(isAudit == "1"){
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  	  htm += "<input type='checkbox' validate='' name='auditThose' value='新增'/>新增&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='修改' checked disabled/><span class='_validate_flag' style='color: red'>*</span>修改&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='删除' checked disabled/><span class='_validate_flag' style='color: red'>*</span>删除&nbsp;";
		  	  if(erjiname == "健康评分"){
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='评分'/>评分&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
			  }else if(erjiname == "任务列表"){
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='执行'/>执行&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
			  }else if(erjiname == "操作日志"){
				  htm += "<input type='checkbox' validate='nn' name='auditThose' value='统计'/>统计&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='导出Excel'/>导出Excel&nbsp;";
			  }else if(erjiname == "系统日志"){
				  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  }else if(erjiname == "异常故障"){
				  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
				  htm += "<input type='checkbox' validate='' name='auditThose' value='确认'/>确认&nbsp;";
				  htm += "<input type='checkbox' validate='' name='auditThose' value='消除'/>消除&nbsp;";
			  	  htm += "<input type='checkbox' validate='' name='auditThose' value='忽略'/>忽略&nbsp;";
			  }
			  $("#auditThoseDiv").html(htm);
		  }else{
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  	  htm += "<input type='checkbox' validate='' name='auditThose' value='新增'/>新增&nbsp;";
			  if(erjiname == "健康评分"){
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='评分'/>评分&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
			  }else if(erjiname == "任务列表"){
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='执行'/>执行&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
			  }else if(erjiname == "操作日志"){
				  htm += "<input type='checkbox' validate='nn' name='auditThose' value='统计'/>统计&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='导出Excel'/>导出Excel&nbsp;";
			  }else if(erjiname == "系统日志"){
				  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  }else if(erjiname == "异常故障"){
				  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
				  htm += "<input type='checkbox' validate='' name='auditThose' value='确认'/>确认&nbsp;";
				  htm += "<input type='checkbox' validate='' name='auditThose' value='消除'/>消除&nbsp;";
			  	  htm += "<input type='checkbox' validate='' name='auditThose' value='忽略'/>忽略&nbsp;";
			  }
		  	$("#auditThoseDiv").html(htm);
		  }
	  });
	  
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  if(id != 'undefined'){
	  	 setDetailValue(id);
	  	 $("#id").val(id);
	  };
	  //判断当前菜单是否添加了审计数据
	  $("#erjiname").change(function(){
		  var erjiName = $(this).val();
		  $.ajax({
		  		url : getContextPath() + "/operationlogCon/isAudit",
		  		type : "post",
		  		data : {
		  			"erjiName" : erjiName,
		  			"methodName" : ""
		  		},
		  		dataType : "json",
		  		success : function(result) {
		  			if(result){
			  			layer.alert("当前菜单已添加审计数据，请重新选择！");
			  			return;
		  			}
		  		},
		  		error : function() {
		  			layer.alert("数据加载失败");
		  		}
		  	});
	  });
  });
  
  //保存
  function doSave() {
  	if(!validateHandler()){
  		return;
  	}
  	
   	//一级菜单
   	var name = $("#name").val();
   	if(name == "1"){
   		name = "配置管理";
   	}else if(name == "2"){
   		name = "任务管理";
   	}else if(name == "3"){
   		name = "异常告警";
   	}else if(name == "4"){
   		name = "系统优化";
   	}else if(name == "5"){
   		name = "日志分析";
   	}else if(name == "6"){
   		name = "系统管理";
   	}
     //二级菜单
   	var erjiname = $("#erjiname").val();
     //是否审计
   	var isAudit = $("#isAudit").val();
   	//审计内容
   	var auditThose = "";
   	
   	var auditThoses = $("input[name='auditThose']:checked").each(function(j) {  
   	    if (j >= 0) {  
   	    	auditThose += $(this).val() + "，"; 
   	    }  
   	});  
   	auditThose = auditThose.substring(0, auditThose.length-1);
   	var id = $("#id").val();
   	
    //拼接数据字段
  	var mData = ""+id+name+erjiname+isAudit+auditThose+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
   	//新增验证是否重复添加
   	if(id == ""){
   		var res = searchAudit();
   	   	if(res){
   	   		return;
   	   	}
   	}
    	var index;
    	$.ajax({
    		url : getContextPath()+"/operationlogCon/update",
    		type : "post",
    		data : {
    			"oper" : "saveOrUpdate",
    			"id" : id,
    			"name" : name,
    			"erjiname" : erjiname,
    			"isAudit" : isAudit,
    			"auditThose" : auditThose,
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
    			if(result == "-1"){
    				layer.alert("保存失败，数据被篡改，请刷新页面后重试！");
    				layer.close(index);
    			}else if(result == "-2"){
    				layer.alert("保存失败，请不要重复提交！");
    				layer.close(index);
    			}else{
    				layer.alert('保存成功！', function(){
    					window.parent.dologSearch();
    					parent.layer.closeAll();
    				});
    			}
    		},
    		error : function() {
    			layer.alert('保存失败！');
    			layer.close(index);
    		}
    	});
    }
  
  //编辑设置value
  function setDetailValue(id) {
  	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
  	if (rs.length>0){
  		$("#id").val(rs[0].id);
  		var name = rs[0].name;
  		if(name == "配置管理"){
  	   		$("#name option[value='1']").attr("selected", true);
  	   	}else if(name == "任务管理"){
  	   		$("#name option[value='2']").attr("selected", true);
  	   	}else if(name == "异常告警"){
  	   		$("#name option[value='3']").attr("selected", true);
  	   	}else if(name == "系统优化"){
  	   		$("#name option[value='4']").attr("selected", true);
  	   	}else if(name == "日志分析"){
  	   		$("#name option[value='5']").attr("selected", true);
  	   	}else if(name == "系统管理"){
  	   		$("#name option[value='6']").attr("selected", true);
  	   	}
  		var erjiname = rs[0].erjiname;
  		var val = $("#name").val();
  		var htm = "";
		  if(val == "1"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='设备管理'>设备管理</option>";
			  htm += "<option value='软件系统管理'>软件系统管理</option>";
			  htm += "<option value='组管理'>组管理</option>";
			  htm += "<option value='双活管理'>双活管理</option>";
			  htm += "<option value='设备型号管理'>设备型号管理</option>";
			  htm += "<option value='设备监控管理'>设备监控管理</option>";
			  htm += "<option value='告警规则管理'>告警规则管理</option>";
			  htm += "<option value='通知规则管理'>通知规则管理</option>";
			  htm += "<option value='指标分类'>指标分类</option>";
			  htm += "<option value='联系人管理'>联系人管理</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "2"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='任务列表'>任务列表</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "3"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='异常故障'>异常故障</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "4"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='健康评分'>健康评分</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "5"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='操作日志'>操作日志</option>";
			  htm += "<option value='系统日志'>系统日志</option>";
			  htm += "<option value='审计策略'>审计策略</option>";
			  $("#erjiname").html(htm);
		  }else if(val == "6"){
			  htm = "<option value=''>--请选择--</option>";
			  htm += "<option value='菜单管理'>菜单管理</option>";
			  htm += "<option value='角色管理'>角色管理</option>";
			  htm += "<option value='数据字典'>数据字典</option>";
			  $("#erjiname").html(htm);
		  }else{
			  htm = "";
			  htm = "<option value=''>--请选择--</option>";
			  $("#erjiname").html(htm);
		  }
		  $("#erjiname option[value='"+erjiname+"']").attr("selected", true);
		  var isAudit = rs[0].isAudit;
		  $("#isAudit option[value='"+isAudit+"']").attr("selected", true);
		  if(isAudit == "1"){
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  	  htm += "<input type='checkbox' validate='' name='auditThose' value='新增'/>新增&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='修改' checked disabled/><span class='_validate_flag' style='color: red'>*</span>修改&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='删除' checked disabled/><span class='_validate_flag' style='color: red'>*</span>删除&nbsp;";
			  	  if(erjiname == "健康评分"){
				  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='评分'/>评分&nbsp;";
				  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
				  }else if(erjiname == "任务列表"){
				  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='执行'/>执行&nbsp;";
				  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
				  }else if(erjiname == "操作日志"){
					  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
					  htm += "<input type='checkbox' validate='nn' name='auditThose' value='统计'/>统计&nbsp;";
				  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='导出Excel'/>导出Excel&nbsp;";
				  }else if(erjiname == "系统日志"){
					  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
				  }
			  	  $("#auditThoseDiv").html(htm);
		  }else{
			  var htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  	  htm += "<input type='checkbox' validate='' name='auditThose' value='新增'/>新增&nbsp;";
			  if(erjiname == "健康评分"){
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='评分'/>评分&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
			  }else if(erjiname == "任务列表"){
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='执行'/>执行&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='停止'/>停止&nbsp;";
			  }else if(erjiname == "操作日志"){
				  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
				  htm += "<input type='checkbox' validate='nn' name='auditThose' value='统计'/>统计&nbsp;";
			  	  htm += "<input type='checkbox' validate='nn' name='auditThose' value='导出Excel'/>导出Excel&nbsp;";
			  }else if(erjiname == "系统日志"){
				  htm = "<input type='checkbox' validate='' name='auditThose' value='查询'/>查询&nbsp;";
			  }
		  	$("#auditThoseDiv").html(htm);
		  }
		  var auditThose = rs[0].auditThose;
		  var isAud = [];
		  	isAud = auditThose.split("，");
			for (var i = 0; i < isAud.length; i++) {
				$("input[value='"+isAud[i]+"']").attr("checked", true);
			}
      }
  }
  //编辑加载数据
  function load(id) {
  	var resultvalue;
  	$.ajax({
  		url : getContextPath() + "/operationlogCon/searchAudit",
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
  			layer.alert("加载失败");
  		}
  	});
  	return resultvalue;
  }
  
  //判断是否添加了审计数据
  function searchAudit() {
	  var res = false;
	  var erjiName = $("#erjiname").val();
	  $.ajax({
	  		url : getContextPath() + "/operationlogCon/doAudit",
	  		type : "post",
	  		async : false,
	  		data : {
	  			"erjiName" : erjiName
	  		},
	  		dataType : "json",
	  		success : function(result) {
	  			if(result){
		  			layer.alert("当前菜单已添加审计数据，请重新选择！");
		  			res = true;
	  			}
	  		},
	  		error : function() {
	  			layer.alert("数据加载失败");
	  		}
	  	});
	  return res;
  }
</script>
</html>
