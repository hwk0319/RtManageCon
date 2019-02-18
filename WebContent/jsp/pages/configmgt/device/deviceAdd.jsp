<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/device/js/device.js"></script>
<title>添加设备</title>
<style>
input[type='text']{
	width:168px;
}
</style>
</head>
<body>
	<table style="align: left; width: 100%; font-size: 13px;" >
		<tbody>
			<tr>
				<td>类型：</td>
				<td>
					<select id="devicetype"  validate="nn" style="width:182px;">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>厂商：</td>
				<td>
					<select id="factory"  validate="nn" style="width:182px;">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>型号：</td>
				<td><select id="model"  validate="nn" style="width:182px;">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>SN码：</td>
				<td><input type="text"  validate="nn" id="sn"></td>
			</tr>
			<tr>
				<td>资产编号：</td>
				<td><input type="text" id="assetno"></td>
			</tr>
			<tr id="portId">
				<td>端口数：</td>
				<td><input type="text" id="port"></td>
			</tr>
			
			<input type="hidden" id="id"/>
			<input type="hidden" id="num"/>
		</tbody>
	</table>
	<div class="widget-content" style="margin-top:35px;">
	<hr class="ui-widget-content" style="margin:1px">
		<p style="text-align:center;">
			<button class="btn"  onclick="doSave()"><i class="icon-ok"></i> 确定</button>
			<button id="close" class="btn" ><i class="icon-remove"></i> 关闭</button>
		</p>
	</div>
</body>
<script type="text/javascript">
$(function() {
	//初始化下拉框的值
	initCombox();
	
	 $("#factory").change(function(){
		  //清除select选项
		  $("#model option[value!='']").remove();
		  //根据厂商查询型号
		  var value = $(this).val();
		  initModel(value);
	  });
	
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	});
	
	
	//类型改变事件
	$("#devicetype").change(function(){
		var dt = $("#devicetype").val();
		//判断是否添加最大数量
		var obj = searchMaxNum(dt);
		if(dt == "3"){
			var ibs = $("#ibNum",parent.document).val();
			if(ibs != ""){
				obj = obj + parseInt(ibs);
			}
			if(obj >= 4){
				alert("IB卡已添加最大数量，请勿继续添加！");
				return;
			}
		}else if(dt == "4"){
			var ips = $("#ipNum",parent.document).val();
			if(ips != ""){
				obj = obj + parseInt(ips);
			}
			if(obj >= 20){
				alert("IP卡已添加最大数量，请勿继续添加！！");
				return;
			}
		}else if(dt == "5"){
			var ssds = $("#ssdNum",parent.document).val();
			if(ssds != ""){
				obj = obj + parseInt(ssds);
			}
			if(obj >= 6){
				alert("SSD已添加最大数量，请勿继续添加！");
				return;
			}
		}else if(dt == "6"){
			var cipans = $("#cipanNum",parent.document).val();
			if(cipans != ""){
				obj = obj + parseInt(cipans);
			}
			if(obj >= 5){
				alert("磁盘已添加最大数量，请勿继续添加！");
				return;
			}
		}else if(dt == "7"){
			var raids = $("#raidNum",parent.document).val();
			if(raids != ""){
				obj = obj + parseInt(raids);
			}
			if(obj >= 11){
				alert("RAID卡已添加最大数量，请勿继续添加！");
				return;
			}
		}

		if(dt == "3" || dt == "4"){
			$("#portId").show();
		}else{
			$("#portId").hide();
			$("#port").val("");
		}
		
	});

	//判断是新增还是编辑
	var id=$.getUrlParam('id');
	if(id != 'undefined'){
	 	setDetailValue(id);
	 	$("#id").val(id);
	}
});

function initCombox(){
	//获取数据字典值并显示在下拉框
	$.ajax({
		url:getContextPath()+"/commonCon/search",
		type:'POST',
		data:{type:'device_type'},
		dataType:'json',
		success:function(data){
				for(var i=0;i<data.length;i++)
				{
					if(data[i].value != "1" && data[i].value != "2"){
						$("#devicetype").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
					}
					//编辑的时候默认选择
					if(devicetype==data[i].value){
						$("#devicetype option[value='"+devicetype+"']").attr("selected",true);
					}
				}
				if(devicetype == "3" || devicetype == "4"){
					$("#portId").show();
				}else{
					$("#portId").hide();
				}
		}
	});
	
	//获取数据字典值并显示在下拉框
	$.ajax({
		url:getContextPath()+"/commonCon/search",
		type:'POST',
		data:{type:'server_factory'},
		dataType:'json',
		success:function(data){
			for(var i=0;i<data.length;i++)
			{
				$("#factory").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
				//编辑的时候默认选择
				if(factory==data[i].value){
					$("#factory option[value='"+factory+"']").attr("selected",true);
				}
			}
			var sel = $("#factory").val();
			if(sel !=""){
				 initModel(sel);
		    }
		}
	});

}
//保存
function doSave() {
	if(!validateHandler()){
		return;
	}
	//类型
	var devicetype = $("#devicetype").val();
	if(devicetype == ""){
		alert("请选择类型！");
		return;
	}
	var dt = devicetype;
	var obj = searchMaxNum(dt);
	if(dt == "3"){
		var ibs = $("#ibNum",parent.document).val();
		if(ibs != ""){
			obj = obj + parseInt(ibs);
		}
		if(obj >= 4){
			alert("IB卡已添加最大数量，请勿继续添加！");
			return;
		}
	}else if(dt == "4"){
		var ips = $("#ipNum",parent.document).val();
		if(ips != ""){
			obj = obj + parseInt(ips);
		}
		if(obj >= 20){
			alert("IP卡已添加最大数量，请勿继续添加！！");
			return;
		}
	}else if(dt == "5"){
		var ssds = $("#ssdNum",parent.document).val();
		if(ssds != ""){
			obj = obj + parseInt(ssds);
		}
		if(obj >= 6){
			alert("SSD已添加最大数量，请勿继续添加！");
			return;
		}
	}else if(dt == "6"){
		var cipans = $("#cipanNum",parent.document).val();
		if(cipans != ""){
			obj = obj + parseInt(cipans);
		}
		if(obj >= 5){
			alert("磁盘已添加最大数量，请勿继续添加！");
			return;
		}
	}else if(dt == "7"){
		var raids = $("#raidNum",parent.document).val();
		if(raids != ""){
			obj = obj + parseInt(raids);
		}
		if(obj >= 11){
			alert("RAID卡已添加最大数量，请勿继续添加！");
			return;
		}
	}
	
	//厂商
	var factory = $("#factory").val();
	if(factory == ""){
		alert("请选择厂商！");
		return;
	}
	//型号
	var model = $("#model").val();
	if(model == ""){
		alert("请选择型号！");
		return;
	}
	//sn码
	if (!validateSpecialChar("#sn","SN码", false)){
		return;
	}
	
	//保存添加数量
	if(dt == "3"){
		var ibs = $("#ibNum",parent.document).val();
		if(ibs == ""){
			ibs = 1;
		}else{
			ibs = eval(parseInt(ibs)+1);
		}
		$("#ibNum",parent.document).val(ibs);
	}else if(dt == "4"){
		var ips = $("#ipNum",parent.document).val();
		if(ips == ""){
			ips = 1;
		}else{
			ips = eval(parseInt(ips)+1);
		}
		$("#ipNum",parent.document).val(ips);
	}else if(dt == "5"){
		var ssds = $("#ssdNum",parent.document).val();
		if(ssds == ""){
			ssds = 1;
		}else{
			ssds = eval(parseInt(ssds)+1);
		}
		$("#ssdNum",parent.document).val(ssds);
	}else if(dt == "6"){
		var cipans = $("#cipanNum",parent.document).val();
		if(cipans == ""){
			cipans = 1;
		}else{
			cipans = eval(parseInt(cipans)+1);
		}
		$("#cipanNum",parent.document).val(cipans);
	}else if(dt == "7"){
		var raids = $("#raidNum",parent.document).val();
		if(raids == ""){
			raids = 1;
		}else{
			raids = eval(parseInt(raids)+1);
		}
		$("#raidNum",parent.document).val(raids);
	}
	
	var id = $("#id").val();
	var editType = $("#editType",parent.document).val();
	
	var index = $("#index",parent.document).val();
	if(index == ""){
		index = 0;
	}else{
		index = eval(parseInt(index)+1);
	}
	//把数据保存到添加服务器页面
	fushuDeviceinfo(id,editType,index);
	//关闭页面
	parent.layer.closeAll();
}

//根据厂商查询型号
function initModel(value){
	  $.ajax({
			url:getContextPath()+"/commonCon/searchModelByFactory",
			type:'POST',
			data:{factory:value},
			dataType:'json',
			success:function(data){
				for(var i=0;i<data.length;i++)
				{
					$("#model").append("<option value='"+data[i].factory+"'>"+data[i].model+"</option>");
					if(model == data[i].model){
						$("#model option[value='"+data[i].factory+"']").attr("selected",true);
					}
				}
			}
		});
}

//根据设备类型判断是否达到最大添加数量
function searchMaxNum(type){
	var num;
	$.ajax({
		url:getContextPath()+"/devicesCon/searchMaxNum",
		type:'POST',
	    data : {
				devicetype : type,
				uid : $("#uid" ,parent.document).val()
			   },
		dataType : 'json',
		async:false,
		success : function(data) {
			num = data;
		  }
		});
	return num;
	}
</script>
</html>
