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
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/device/js/device.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
	<style>
		input[type='text'],
		input[type='password']{
			width:182px;
		}
	</style>
  </head>
  <body>
    <div style="height: 85%;border: solid thin #d1d5de;">
    	<table id="deviceInfo" style="align: left; width: 100%; font-size: 13px;">
			<tr style="height: 30px;">
				<td colspan="8" style="background-color: #f6f8fa">设备</td>
			</tr>
			<tr>
				<td>类型：</td>
				<td>
					<select id="devicetype" style="width:182px;" validate="nn">
						<option value="">--请选择--</option>
					</select>
				</td>
				<td>厂商：</td>
				<td>
					<select id="factory" style="width:182px;" validate="nn">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>型号：</td>
				<td><select id="model" style="width:182px;" validate="nn">
						<option value="">--请选择--</option>
					</select>
				</td>
				<td>操作系统：</td>
				<td>
					<select id="opersys" style="width:182px;" validate="nn">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>应用IP：</td>
				<td><input type="text" id="in_ip" validate="nn ip" placeholder="请输入应用IP"></td>
				<td>应用用户名：</td>
				<td><input type="text" id="in_username" validate="nn nspclcn" maxlength="15" placeholder="请输入应用用户名"></td>
			</tr>
			<tr>
				<td>应用密码：</td>
				<td id="ipdtd"><input type="password" id="in_password" validate="nn" maxlength="20" placeholder="请输入应用密码"></td>
				<td>带外IP：</td>
				<td><input type="text" id="out_ip" validate="nn ip" placeholder="请输入带外IP"></td>
			</tr>
			<tr>
				<td>带外用户名：</td>
				<td><input type="text" id="out_username" validate="nn nspclcn" maxlength="15" placeholder="请输入带外用户名"></td>
				<td>带外密码：</td>
				<td id="opdtd"><input type="password" id="out_password" validate="nn" maxlength="20" placeholder="请输入带外密码"></td>
			</tr>
			<tr>
				<td>主机名：</td>
				<td><input type="text" id="name" validate="nn nspclcn"  maxlength="15" placeholder="请输入主机名"></td>
				<td>SN码：</td>
				<td><input type="text" title="" id="sn" validate="nn nspclcn" maxlength="50" placeholder="请输入SN码"></td>
			</tr>
			<tr>
				<td>端口：</td>
				<td><input type="text" id="port" validate="nn mum"  maxlength="15" placeholder="请输入端口"></td>
				<td>资产编号：</td>
				<td><input type="text" id="assetno" validate="nspclcn"  maxlength="15" placeholder="请输入资产编号"></td>
			</tr>
			<!-- 负载均衡字段 -->
			<tr class="tr6" style="display:none;">
				<td>Pool名称：</td>
				<td id="pn"><input type="text" id="poolName" validate="" maxlength="30" placeholder="请输入Pool名称"></td>
				<td>服务名vs1：</td>
				<td  id="vs1"><input type="text" id="vsName1" validate="" maxlength="30" placeholder="请输入服务名vs1"></td>
			</tr>
			<tr class="tr6" style="display:none;">
				<td>服务名vs2：</td>
				<td id="vs2"><input type="text" id="vsName2" validate="" maxlength="30" placeholder="请输入服务名vs2"></td>
			</tr>
		</table>
	</div>
	<div class="widget-content">
		<hr class="ui-widget-content" style="margin:1px">
		<p style="text-align:center;">
			<button class='btn' id="sn_name">
				<i class="icon-plus"></i> 获取主机名
			</button>
			<button class="btn" onclick="doSave()">
				<i class="icon-ok"></i> 保存
			</button>
			<button id="close" class="btn">
				<i class="icon-remove"></i> 关闭
			</button>
		</p>
	</div>
	
	<!-- 用来存储子页面的list值 -->
	<input type="hidden" id="datastr" />
	<input type="hidden" id="dataedit" />
	<input type="hidden" id="dataedit1" />
	<input type="hidden" id="uid"/>
	<input type="hidden" id="id"/>
	<input type="hidden" id="ids"/>
	<input type="hidden" id="delids"/>
	<input type="hidden" id="index"/>
	<input type="hidden" id="ipNum"/>
	<input type="hidden" id="ibNum"/>
	<input type="hidden" id="ssdNum"/>
	<input type="hidden" id="cipanNum"/>
	<input type="hidden" id="raidNum"/>
	<input type="hidden" id="in_pwd1" value="">
	<input type="hidden" id="in_pwdold" value="">
	<input type="hidden" id="out_pwd1" value="">
	<input type="hidden" id="out_pwdold" value="">
</body>
  <script>
  var in_ip ="";
  var in_username ="";
  var in_password="";
  var modulus;
  var jwt;
  $(function() {
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  sys_id=$.getUrlParam('sys_id');
	  if(id != 'undefined'){
	  	 setDetailValue(id);
	  	 //编辑去掉必填验证
	  	 $("#in_password").attr("validate","");
	  	 $("#out_password").attr("validate","");
	  	 $("#ipdtd ._validate_flag").remove();
	  	 $("#opdtd ._validate_flag").remove();
	  }else{
		  //初始化下拉框的值
		  initCombox();
	  };
	  //类型选择事件
	  $("#devicetype").change(function(){
		  $("#factory option[value!='']").remove();
		  var devicetype = $(this).val();
		  initFactory(devicetype);
		  if(devicetype == "10"){
			  $(".tr6").show();
			  addValidateFzjh();
		  }else{
			  $(".tr6").hide();
			  rmValidateFzjh();
		  }
	  });
	  //厂商改变事件
	  $("#factory").change(function(){
		  //清除select选项
		  $("#model option[value!='']").remove();
		  //根据厂商查询型号
		  var factory = $(this).val();
		  var devicetype = $("#devicetype").val();
		  initModel(devicetype,factory);
	  });
	 $("#sn_name").click(function() {
		 in_ip = $("#in_ip").val();
		 in_username = $("#in_username").val();
		 in_password = $("#in_password").val();
		 if(in_ip==""|| in_username==""||in_password==""){
			 layer.msg("请输入应用IP、应用用户名、应用密码信息！");
			 return;
		 }else{  
			 if(!validateIpField("#in_ip","应用IP",false)){
				 return;
			 }
			 layer.confirm('是否为本机？', {
				  btn: ['是','否'] //按钮
				}, function(){
					cli('是');
				}, function(){
					cli('否');
			 });
		 } 
	  });
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
  });
  
//添加服务器去掉负载均衡字段验证
  function rmValidateFzjh(){
	  $("#poolName").attr("validate","");
  	  $("#vsName1").attr("validate","");
  	  $("#vsName2").attr("validate","");
  	  $("#pn ._validate_flag").remove();
  	  $("#vs1 ._validate_flag").remove();
  	  $("#vs2 ._validate_flag").remove();
  }
  //添加负载均衡字段验证
  function addValidateFzjh(){
	  $("#poolName").attr("validate","nspcl");
  	  $("#vsName1").attr("validate","nspcl");
  	  $("#vsName2").attr("validate","nspcl");
//   	  $("#pn").append("<span class='_validate_flag' style='color: red'>*</span>");
// 	  $("#vs1").append("<span class='_validate_flag' style='color: red'>*</span>");
// 	  $("#vs2").append("<span class='_validate_flag' style='color: red'>*</span>");
  }
  
  function cli(IsOrNot){
	  var index = layer.msg('正在获取中......');
	  //只rsa加密
	  var in_passwordRsa;
	  if(in_password != ""){
		  in_passwordRsa = $.toRsaEncrypt(in_password, modulus);
	  }
	  setTimeout(function () {
		  $.ajax({
		  		url : getContextPath()+"/devicesCon/getHostnameAndSn",
		  		type : "post",
		  		async:false,
		  		data : {
		  			"in_ip" : in_ip,
		  			"in_username" : in_username,
		  			"in_password" : in_passwordRsa,
		  			"IsOrNot":IsOrNot
		  		},
		  		success : function(data) {
		  			layer.close(index);
		  			layer.alert(data[0]);
		  			if(data.length<2){
		  				
		  			}else{
		  				if(data[0]=="节点错误"){
		  					layer.alert(data[0]);
		  					return;
		  				}else if(data[0]=="带内Ping不通"){
		  					layer.alert(data[0]);
		  					return;
		  				}else if(data[0]=="账号密码不匹配"){
		  					layer.alert(data[0]);
		  					return;
		  				}
		  				document.getElementById("name").value=data[0]; 
		  				if(data[1]==""||data[1]==null){
		  					layer.alert("无法获取SN码，请手动输入！");
		  					return;
		  				}else{
		  					document.getElementById("sn").value=data[1];
		  					$("#sn").attr("title",data[1]);
		  				}
		  			}
		  		},
		  		error : function() {
		  			layer.alert("获取失败！");
		  		}
		  	});  
		  }, 1000);
}

  //添加附属设备
  function doInsertInfo(id){
  	layer.open({
  	    type: 2,
  	    title: '附属设备', 
  	    fix: false,
  	    shadeClose: false,
  	    area: ['560px', '340px'],
  	    content: [getContextPath()+"/jsp/pages/configmgt/device/deviceAdd.jsp?id="+id,'no']
  	});
  }
  
  //初始化厂商
  function initFactory(tp){
		var type;
		if(tp == "1"){
			type = "server_factory";
			//服务器
		}else if(tp == "2"){
			//交换机
			type = "switchboard_factory";
		}else if(tp == "10"){
			//负载均衡
			type = "loadB_factory";
		}else{
			type = "";
		}
		//获取数据字典值并显示在下拉框
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data : {
						type : type
					},
			dataType : 'json',
			async:false,
			success : function(data) {
						for (var i = 0; i < data.length; i++) {
							$("#factory").append("<option  value='"+data[i].value+"'>"+ data[i].name + "</option>");
						}
					}
				});
  }
  
  function initCombox(){
		//获取数据字典值并显示在下拉框
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data:{type:'device_type'},
			dataType:'json',
			async:false,
			success:function(data){
					for(var i=0;i<data.length;i++)
					{
						if(data[i].value == "1" || data[i].value == "10"){
							$("#devicetype").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
						}
					}
			}
		});
				
	    //获取设操作系统并显示在下拉框
		$.ajax({
			url : getContextPath() + "/commonCon/search",
			type : 'POST',
			data : {
				type : 'device_opersys'
			},
			dataType : 'json',
			success : function(data) {
				for (var i = 0; i < data.length; i++) {
					$("#opersys").append("<option value='"+data[i].value+"'>"+ data[i].name + "</option>");
					if (opersys == data[i].value) {
						$("#opersys option[value='" + opersys + "']").attr("selected", true);
					}
				}
			}
		});
	}

	var delids = "";
	//删除设备
	function deleteDevice(id) {
		//删除页面显示
		$("#div" + id + "").remove();
		//保存删除id
		delids = $("#delids").val();
		if (delids != "") {
			delids += "," + id;
		} else {
			delids += id;
		}
		$("#delids").val(delids);

		//选择设备列表id
		var ids = $("#ids").val();
		var newids = "";
		if (ids != "") {
			var sids = [ ids.split(",") ];
			for (var i = 0; i < sids[0].length; i++) {
				if (sids[0][i] == id) {
					sids[0].splice(i, 1);
				}
			}
			for (var i = 0; i < sids[0].length; i++) {
				if (i == 0) {
					newids += sids[0][i];
				} else {
					newids += "," + sids[0][i];
				}
			}
		}

		$("#ids").val(newids);
		//删除新增数据
		var str = $("#datastr").val();
		var datat = [ str.split("_") ];
		datat[0].splice(id, 1);
		var strjson = "";
		$.each(datat, function(i, val) {
			if (i == 0) {
				strjson += val;
			} else {
				strjson += "," + val;
			}
		});
		$("#datastr").val(strjson);
	}

	//根据厂商查询型号
	function initModel(devicetype,factory) {
		$.ajax({
			url : getContextPath() + "/commonCon/searchModelByFactory",
			type : 'POST',
			data : {
				devicetype : devicetype,
				factory : factory
			},
			dataType : 'json',
			async:false,
			success : function(data) {
				for (var i = 0; i < data.length; i++) {
					$("#model").append("<option value='"+data[i].model+"'>"+ data[i].model + "</option>");
					if (model == data[i].model) {
						$("#model option[value='"+ data[i].model + "']").attr("selected", true);
					}
				}
			}
		});
	}
	
	  var devicetype;
	  var model;
	  var opersys;
	  var parent_id;
	  var factory;
	//编辑设置value
	function setDetailValue(id) {
		var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
		if (rs.length>0){
			//密码回填
// 			in_password = rs[0].in_password;
// 			out_password = rs[0].out_password;
			$("#id").val(rs[0].id);
			$("#uid").val(rs[0].uid);
			devicetype = rs[0].devicetype;
			initCombox();
			$("#devicetype option[value='"+devicetype+"']").attr("selected",true);
			if(devicetype == 10){
				$(".tr6").show();
				addValidateFzjh();
				//负载均衡字段
				if(rs[0].remark != "" && rs[0].remark != null){
					var remark = rs[0].remark.split(",");
					var poolName = remark[0];
				  	var vsName1 = remark[1];
				  	var vsName2 = remark[2];
					$("#poolName").val(poolName);
				  	$("#vsName1").val(vsName1);
				  	$("#vsName2").val(vsName2);
				}
			}
			parent_id = rs[0].parent_id;
			$("#sn").val(rs[0].sn);
			model = rs[0].model;
			$("#in_ip").val(rs[0].in_ip);
			$("#in_username").val(rs[0].in_username);
// 			var ipd = $.toRsaEncrypt(in_password, modulus);
// 			$("#in_password").val(ipd.substring(0,30));
// 			$("#in_pwd1").val(ipd);
// 			$("#in_pwdold").val(in_password);
			$("#out_ip").val(rs[0].out_ip);
			$("#out_username").val(rs[0].out_username);
// 			var opd = $.toRsaEncrypt(out_password, modulus);
// 			$("#out_password").val(opd.substring(0,30));
// 			$("#out_pwd1").val(opd);
// 			$("#out_pwdold").val(out_password);
			opersys = rs[0].opersys;
			$("#assetno").val(rs[0].assetno);
			factory = rs[0].factory;
			initFactory(devicetype);
			$("#factory option[value='" + factory + "']").attr("selected", true);
			//新增字段
			model = (rs[0].model);
			initModel(devicetype,factory);
			$("#model option[value='"+ model + "']").attr("selected", true);
			$("#name").val(rs[0].name);
			$("#port").val(rs[0].port);
	    }
	}
	//编辑加载数据
	function load(id) {
		var resultvalue;
		$.ajax({
			url : getContextPath() + "/devicesCon/search",
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
</script>
</html>
