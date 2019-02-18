<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/healthScore/js/heathScore.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
	<style>
	</style>
	<title>健康评分</title>
  </head>
  <body>
    <div id="heathadd">
    	<table id="groupInfo" style="width: 100%;font-size: 13px;margin: 1%;">
			<tr>
				<td>评估目标ID：</td>
				<td>
					<input type="text" id="target_id"  placeholder="请点击添加" readonly="readonly" onClick="doInsertSys();" validate="nn" style="cursor: pointer;"/>
				</td>
				<td>模型：</td>
				<td>
					<input type="text" id="model_name"  placeholder="请点击添加" readonly="readonly" onClick="doInsertMod();" validate="nn" style="cursor: pointer;"/>
					<input type="hidden" id="model_id"/>
				</td>
			</tr>
			<tr>
				<td>类型：</td>
				<td>
					<select id="type" style="width:182px;"  validate="nn">
						<option value="">--请选择--</option>
<!-- 						<option value="1">历史评分</option> -->
						<option value="2" selected>周期评分</option>
					</select>
				</td>
				<td>周期：</td>
				<td><input type="text" id="cron" validate="nn" placeholder="请选择周期" readonly="readonly"/><button onClick="doCleak();" class="btn" style="height: 25;line-height: 1;width: 60;color: #7d7b7b;background: whitesmoke;">清 空</button></td>
			</tr>
			<tr id="historyScore" style="display:none;">
				<td>评估起点时间：</td>
				<td><input type="text" id="begin_time" name="begin_time" readonly="readonly" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:'true',maxDate:'%y-%M-%d'})"  validate=""/><span class="_validateflag" style="color: red">*</span></td>
				<td>评估截止时间：</td>
				<td><input type="text" id="end_time" readonly="readonly" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:'true',minDate:'#F{$dp.$D(\'begin_time\')}',maxDate:'%y-%M-%d'})"  validate=""/><span class="_validateflag" style="color: red">*</span></td>
			</tr>
		</table>
		<div id="croniframe" style="display:none;">
			<Iframe id="iframecron" class="ifCron" src="${pageContext.request.contextPath}/jsp/plugins/Cron/cron.jsp"; style="width:100%;height:60%;" scrolling="no" frameborder="0"></iframe>
		</div>
		<!-- 遮罩 -->
		<div id="shade" style="width:602px;height:286px;opacity: 0.3;background: #e6e5e5;position: absolute;z-index: 99999;margin-top: 8px;margin-left: 8px;"></div>
	</div>
	<input type="hidden" id="health_check_id"/>
	</br>
	<div class="widget-content" style="margin-top: -25px;">
		<hr class="ui-widget-content" >
		<p  style="text-align:center">
			<button class="btn" onclick="doSave()">
				<i class="icon-ok"></i> 保存
			</button>
			<button id="close" class="btn">
				<i class="icon-remove"></i> 关闭
			</button>
		</p>
	</div>
</body>
  <script>
  var modulus;
  var jwt;
  $(function() {
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
// 	  $("#end_time").click(function(){
// 		  valiDate();
// 	  });
	  
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
// 		  window.parent.doSearch();
		  parent.layer.closeAll();
	  });
	  //页面添加cron控件
	  $("#heathadd").append($("#croniframe").html());
	  
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  if(id != 'undefined'){
	  	 setDetailValue(id);
	  	 $("#type option[value='"+type+"']").attr("selected",true);
	  	 if(type == "1"){
	  		$("#historyScore").show();
	  		$("#shade").show();
	  	 }else{
	  		$("#cornScore").show();
	  		$("#shade").hide();
	  	 }
	  }
	  
	  $("#type").change(function(){
		  var tp = $(this).val();
		  if(tp == "1"){
			  $("#historyScore").show();
			  $("#cornScore").hide();
			  $("#cron").val("");
			  $("#shade").show();
			  $("#begin_time").attr("validate","nn");
			  $("#end_time").attr("validate","nn");
			  $("#cron").attr("validate","");
		  }else if(tp == "2"){
			  $("#historyScore").hide();
			  $("#cornScore").show();
			  $("#begin_time").val("");
			  $("#end_time").val("");
			  $("#shade").hide();
			  $("#begin_time").attr("validate","");
			  $("#end_time").attr("validate","");
			  $("#cornScore").attr("validate","nn");
		  }else{
			  $("#historyScore").hide();
			  $("#cornScore").hide();
			  $("#shade").show();
		  }
	  });
	  
	  var tp = $("#type").val();
	  if(tp == 1){
		  $("#shade").show();
	  }else{
		  $("#shade").hide();
		  $("#cornScore").show();
	  }
  });
  
  //验证日期
  function valiDate(){
	  var begin_time = $("#begin_time").val();
	  var end_time = $("#end_time").val();
	  if(begin_time == ""){
		  layer.alert("请先选择评估起点时间！");
		  return;
	  }
	  var d1 = new Date(begin_time.replace(/\-/g, "\/"));  
	  var d2 = new Date(end_time.replace(/\-/g, "\/"));  
	   if(beginDate!=""&&endDate!=""&&d1 >=d2)  
	  {  
	   layer.alert("开始时间不能大于结束时间！");  
	   return false;  
	  }
  }

  //选择软件系统
  function doInsertSys(){
	 layer.open({
  	    type: 2,
  	    title: "软件系统", 
//   	    fix: false,
  	    shadeClose: false,
  	    id:"system",
  	    zIndex: 19891014,
  	    moveOut: true,
  	 	area: ['75%', '85%'],
	    btn: ['确定', '取消'],
	 	yes: function(index, layero){
	         var iframeWin = window[layero.find('iframe')[0]['name']];
	        	 iframeWin.doSave();
	      },
	      btn2: function(){
	       	 layer.close();
	      },
  	    content: [getContextPath()+"/jsp/pages/baselinemgt/healthScore/System.jsp",'no'],
  	});
  }
  
  //选择模型
  function doInsertMod(){
	  layer.open({
  	    type: 2,
  	    title: "健康模型", 
//   	    fix: false,
  	    shadeClose: false,
  	    id:"model",
  	    zIndex: 19891014,
  	    moveOut: true,
  	    area: ['75%', '85%'],
	    btn: ['确定', '取消'],
	 	yes: function(index, layero){
	         var iframeWin = window[layero.find('iframe')[0]['name']];
	        	 iframeWin.doModelSave();
	      },
	      btn2: function(){
	       	 layer.close();
	      },
  	    content: [getContextPath()+"/jsp/pages/baselinemgt/healthScore/modelConn.jsp",'no'],
  	});
  }

  //保存
  function doSave(){
	  if(!validateHandler()){
			return;
		}
	var id = $("#health_check_id").val();
	//评估目标ID
	var target_id = $("#target_id").val();
	//模型ID
	var model_name = $("#model_id").val();
	//类型
	var tp = $("#type").val();
	//周期
	var cron = $("#cron").val();
	if(tp == "1"){
		//评估起点时间
		if (!validateNull("#begin_time","评估起点时间", false)){
		  		return;
		}
		//评估截止时间
		if (!validateNull("#end_time","评估截止时间", false)){
		  		return;
		}
		//比较时间大小
		var startTime = $("#begin_time").val();
		var endTime = $("#end_time").val();
		var st = new Date(startTime).getTime();
		var et = new Date(endTime).getTime();
		if(st > et){
			alert("评估起点时间不能大于评估截止时间！");
			return;
		}
	}else if(tp == "2"){
		//周期
		if (!validateNull("#cron","周期", false)){
		  		return;
		}
	}
	//拼接数据字段
  	var mData = ""+id+target_id+model_name+cron+"";
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	var index;
  	$.ajax({
  		url : getContextPath()+"/heathcheckCon/update?oper=saveOrUpdate",
  		type : "post",
  		data : {
  			health_check_id : $("#health_check_id").val(),
  			target_id : $("#target_id").val(),
  			model_id : $("#model_id").val(),
  			cron : $("#cron").val(),
  			mData : mData,
  			"jwt" : jwt
  		},
  		dataType : 'json',
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
  				layer.alert("保存失败，数据完整性被破坏，请刷新页面后重试！");
  				layer.close(index);
  			}else if(result == "-2"){
  				layer.alert("保存失败，请不要重复提交！");
  				layer.close(index);
  			}else{
  				layer.alert("保存成功！", function(){
  	  				window.parent.doSearch();
  	  	  		    parent.layer.closeAll();
  				});
  			}
  		},
  		error : function() {
  			layer.alert("保存失败！");
  		}
  	});
  }
  
  function doCleak(){
	  $("#cron").val("");
  }
  </script>
  
</html>
