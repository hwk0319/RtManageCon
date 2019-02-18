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
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/device/js/device.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/systemmgt/js/systemmgtList.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
	<style>
	</style>
	<title>软件系统</title>
  </head>
  <body>
    <div>
    	<table id="groupInfo" style="align: left; width: 100%; border: solid thin #d1d5de; font-size: 13px;">
			<tr>
				<td colspan="8" style="background-color: #f6f8fa">软件系统</td>
			</tr>
			<tr>
				<td>系统类别：</td>
				<td>
					<select id="systype" validate="nn" style="width:182px;">
						<option value="">--请选择--</option>
						<option value="1">数据库系统</option>
					</select>
				</td>
				<td>具体类型：</td>
				<td>
					<select id="type" validate="nn" style="width:182px;">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>名称：</td>
				<td><input type="text" validate="nn nspcl" id="name" maxlength="15" placeholder="请输入名称"/></td>
				<td>管理IP：</td>
				<td><input type="text" validate="nn ip" id="ip" maxlength="20" placeholder="请输入管理IP"/></td>
			</tr>
			<tr>
				<td>用户名：</td>
				<td><input type="text" validate="nn nspclcn" id="username" maxlength="15" placeholder="请输入用户名"/></td>
				<td>密码：</td>
				<td id="mtd"><input type="password" validate="nn" id="mm" maxlength="20" placeholder="请输入密码"/></td>
			</tr>
			<tr id="">
				<td>数据库实例名：</td>
				<td><input type="text" validate="nn nspclcn" id="reserver1" maxlength="15" placeholder="请输入数据库实例名"/></td>
				<td>端口：</td>
				<td><input type="text" id="port" validate="nn num" maxlength="15" placeholder="请输入端口"/></td>
			</tr>
			<tr>
				<td>设备账号：</td>
				<td><input type="text" id="reserver2" validate="nspclcn" maxlength="15" placeholder="请输入设备账号"/></td>
				<td>设备密码：</td>
				<td><input type="password" id="reserver3" maxlength="20" placeholder="请输入设备密码"/></td>
			</tr>
			<!-- <tr>
				<td>位置信息：</td>
				<td>
					<select id="position" style="width:182px;" validate="nn">
						<option value="">--请选择--</option>
						<option value="0">内网</option>
						<option value="1">外网</option>
					</select>
				</td>
			</tr> -->
		</table>
		<div style="min-height:200px;border:solid thin #d1d5de;margin-top: 5px;">
			<div class="title"
				style='background-color: #f6f8fa; font-size: 13px; height: 23px; margin:3px;'>设备
			</div>
			<div style="width:100%;height:165px;overflow-y: auto;">
				<div style="float:left;margin: 3px;" id="deviceInfo">
					<!-- 设备数据 -->
					<div style="float:left;width:130px;height:150px;margin-top:2px;margin-left:2px;border: solid thin #d1d5de;text-align: center;" >
						<img src="imgs/add.png" style="width:30px;height:30px;margin-top: 55px;cursor:pointer;"
							onClick="doInsertInfo();" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="widget-content">
		<hr class="ui-widget-content" style="margin:1px">
		<p  style="text-align:center">
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
	<input type="hidden" id="uid"/>
	<input type="hidden" id="id"/>
	<input type="hidden" id="ids"/>
	<input type="hidden" id="delids"/>
	<input type="hidden" id="mmold"/>
	<input type="hidden" id="reserver3old"/>
	<input type="hidden" id="mm1"/>
	<input type="hidden" id="reserver31"/>
</body>
  <script>
  var modulus;
  var jwt;
  $(function() {
	  //系统类别改变事件
	  $("#systype").change(function(){
		  var value = $(this).val();
		  var type = "";
		  if(value == "1"){
			  type = "db_type";
			  noReadonly();
		  }
		  //清除select下拉项
		  $("#type option[value!='']").remove();
		  //初始化下拉框的值
		  stype = "";
		  initCombox(type);
	  });
	  $("#type").change(function(){
		  var systype = $("#systype").val();
		  var val = $(this).val();
		  if(systype == "2" && val == "1"){
			  doReadonly();
		  }else if(systype == "3" && val == "1"){
			  doBackupsReadonly();
		  }else{
			  noReadonly();
		  }
	  });
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  var uid=$.getUrlParam('uid');
	  if(id != 'undefined'){
		 //编辑去掉必填验证
	  	 $("#mm").attr("validate","");
	  	 $("#mtd ._validate_flag").remove();
	  	 setDetailValue(id);
	  	 //设置select的选中
		 $("#systype option[value='"+systype+"']").attr("selected",true);
		 if(systype != "" ){
			 var value = systype;
			  var type = "";
			  if(value == 1){
				  type = "db_type";
			  }
			  //清除select下拉项
			  $("#type option[value!='']").remove();
			  //初始化下拉框的值
			  initCombox(type);
		 }
		  var systypes = systype;
		  var val = stype;
		  if(systypes == "2" && val == "1"){
			  doReadonly();
		  }else if(systypes == "3" && val == "1"){
			  doBackupsReadonly();
		  }else{
			  noReadonly();
		  }
	  	 //查询设备数据
		 searchDevice(id);
	  	 $("#id").val(id);
	  	$("#uid").val(uid);
	  };
	  
	  delImg();
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
  });
  
  //如果是文件系统，设置部分输入框不可编辑
  function doReadonly(){
	  $("#name").attr("readonly","readonly");
	  $("#ip").attr("readonly","readonly");
	  $("#username").attr("readonly","readonly");
	  $("#mm").attr("readonly","readonly");
	  $("#reserver1").attr("readonly","readonly");
	  $("#reserver2").attr("readonly","readonly");
	  $("#reserver3").attr("readonly","readonly");
  }
	//如果是备份系统，设置部分输入框不可编辑
  function doBackupsReadonly(){
	  $("#name").attr("readonly","readonly");
	  $("#ip").attr("readonly","readonly");
	  $("#reserver1").attr("readonly","readonly");
	  $("#reserver2").attr("readonly","readonly");
	  $("#reserver3").attr("readonly","readonly");
  }
  //设置输入框可编辑
  function noReadonly(){
	  $("#name").attr("readonly",false);
	  $("#ip").attr("readonly",false);
	  $("#username").attr("readonly",false);
	  $("#mm").attr("readonly",false);
	  $("#reserver1").attr("readonly",false);
	  $("#reserver2").attr("readonly",false);
	  $("#reserver3").attr("readonly",false);
  }

  //添加设备
  function doInsertInfo(id){
  	layer.open({
  	    type: 2,
  	    title: '设备', 
  	    fix: false,
  	    shadeClose: false,
  	  	area: ['75%', '85%'],
  	    btn: ['确定', '取消'],
  	 	yes: function(index, layero){
	         var iframeWin = window[layero.find('iframe')[0]['name']];
	         iframeWin.doSaveDev();
        },
        btn2: function(){
         	 layer.closeAll();
        },
  	    content: [getContextPath()+"/jsp/comm/jsp/deviceComm.jsp",'no']
  	});
  }
  
  function initCombox(type){
		//获取数据字典值并显示在下拉框
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data:{type:type},
			dataType:'json',
			success:function(data){
					for(var i=0;i<data.length;i++)
					{
						$("#type").append("<option  value='"+data[i].value+"' text='"+data[i].name+"'>"+data[i].name+"</option>");
						//编辑的时候默认选择
						if(stype==data[i].name){
							$("#type option:contains('"+stype+"')").attr("selected",true);
						}
					}
			}
		});
	}

  //查询设备
   function searchDevice(id){
	  $.ajax({
			url:getContextPath()+"/systemmgtCon/searchSysDev",
			type:'POST',
			data:{sys_id:id},
			dataType:'json',
			async:false,
			success:function(data){
				if(data.length != null){
					var ids = "";
					for(var i=0;i<data.length;i++)
					{
						var name = data[i].name;
						var in_ip = data[i].in_ip;
						var innerHtml = "";
							innerHtml += "<div id='div"+data[i].id+"' style='align: left; width: 130px; height:150px; border: solid thin #d1d5de; font-size: 13px;float:left;margin:2px;text-align:center;'>";
							innerHtml += "<a href='javaScript:void(0);' onClick='deleteDevice("+data[i].id+");'><div style='float:right;height:20px;'><img src='imgs/closexx.png' class='delImg' style='width: 20px; height:20px;'/></div></a>";
							innerHtml += "<a href='javaScript:void(0);' onClick='deviceInfo("+data[i].id+");' style='color: #928f8f;text-decoration: none;'>";
							innerHtml += "<div style='margin-top:20px;margin-left:0px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;'>";
							innerHtml += "<img src='imgs/mon/app.png' style='width:59px;height:66px;margin:10px;'/>";
							innerHtml += "</br>"+name+"<br/>"+in_ip+"";
							innerHtml += "</div>";
							innerHtml += "</a>";
							innerHtml += "</div>";
						$("#deviceInfo").prepend(innerHtml);
						
						//获取id
						if(i == 0){
							ids += data[i].id;
						}else{
							ids += ","+data[i].id;
						}
					}
					$("#ids").val(ids);
				}
			}
		});
  }
  
  //保存
  function doSave(){
    if(!validateHandler()){
		return;
	}
	//系统类别
  	var systype = $("#systype").val();
	//具体类型
  	var type = $("#type").val();
	//名称
	var name = $("#name").val();
	//管理IP
	var ip = $("#ip").val();
	//用户名
	var username = $("#username").val();
	//密码
	var mm = $("#mm").val();
	//数据库实例名
	var reserver1 = $("#reserver1").val();
	//设备账号
	var reserver2 = $("#reserver2").val();
	//设备密码
	var device_mm = $("#reserver3").val();
	//端口
	var port = $("#port").val();
// 	var position = $("#position").val();
	if(mm != ""){
  		mm = $.toRsaEncrypt(mm, modulus);//只rsa加密
	}
	if(device_mm != ""){
 		device_mm = $.toRsaEncrypt(device_mm, modulus);
	}
		
	var id = $("#id").val();
	var uid = $("#uid").val();
	
	//拼接数据字段
  	var mData = ""+id+uid+systype+type+name+ip+username+mm+reserver1+reserver2+device_mm+"";
	  	mData = encodeURIComponent(mData);//中文转义	
		mData = $.toRsaMd5Encrypt(mData, modulus);
	
// 	username = $.toRsaEncrypt(username, modulus);
// 	if(reserver2 != ""){
// 		reserver2 = $.toRsaEncrypt(reserver2, modulus);
// 	}
	//新增设备数据
	var datastr = $("#datastr").val();
  	if(datastr != ""){
  		datastr = "["+datastr+"]";
  	}
  	//删除数据id
  	var delids = $("#delids").val();
  	var index;
  	$.ajax({
  		url : getContextPath()+"/systemmgtCon/update",
  		type : "post",
  		data : {
  			"oper":"saveOrUpdate",
  			"uid":uid,
  			"id" : id,
  			"systype" : systype,
  			"type" : type,
  			"name" : name,
  			"ip" :  ip,
  			"username" : username,
  			"mm" : mm,
  			"reserver1" : reserver1,
  			"reserver2" : reserver2,
  			"reserver3" : device_mm,
  			"port" : port,
  			"position" : "0",//默认为0
  			"ids" : delids,
  			"mData" : mData,
  			//设备数据
  			"adddata" : datastr,
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
						window.parent.doSearchSys();
	  	  	  		    parent.layer.closeAll();
					});
  				}else{
  					layer.alert(result[key]);
  				}
  			}
  		},
  		error : function() {
  			layer.alert("保存失败！");
  		}
  	});
  }
  
  var delids = "";
  //删除设备
  function deleteDevice(id){
	  //删除页面显示
	  $("#div"+id+"").remove();
	  //保存删除id
	  delids = $("#delids").val();
	  if(delids != ""){
		  delids += ","+id; 
	  }else{
		  delids += id; 
	  }
	  $("#delids").val(delids);
	  
	  //选择设备列表id
	  var ids = $("#ids").val();
	  var newids = "";
	  if(ids != ""){
		  var sids =[ids.split(",")];
		  for(var i = 0; i<sids[0].length;i++){
			  if(sids[0][i] == id){
				  sids[0].splice(i,1);
			  }
		  }
		  for(var i = 0; i<sids[0].length;i++){
			  if(i == 0){
				  newids += sids[0][i];
			  }else{
				  newids += ","+sids[0][i];
			  }
		  }
	  }
	  $("#ids").val(newids);
  }
  
  //服务器右上角关闭图标
  function delImg(){
	  $(".delImg").mouseover(function(){
		  $(this).attr("src","imgs/closexxred.png");
	  });
	  $(".delImg").mouseout(function(){
		  $(this).attr("src","imgs/closexx.png");
	  });
  }
  </script>
</html>
