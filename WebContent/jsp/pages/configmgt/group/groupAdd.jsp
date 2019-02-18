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
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/group/js/group.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
	<style>
	</style>
	<title>组</title>
  </head>
  <body>
    <div>
    	<table id="groupInfo" style="align: left; width: 100%; border: solid thin #d1d5de; font-size: 13px;">
			<tr>
				<td colspan="8" style="background-color: #f6f8fa">组</td>
			</tr>
			<tr>
				<td>组类型：</td>
				<td>
					<select id="grouptype" validate="nn" style="width:182px;">
						<option value="">--请选择--</option>
					</select>
				</td>
				<td>具体类型：</td>
				<td>
					<select id="grotype" validate="nn" style="width:182px;">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>名称：</td>
				<td><input type="text" validate="nn nspcl" maxlength="15" id="name" placeholder="请输入名称"></td>
				<td>描述：</td>
				<td><input type="text" id="description" validate="nspcl" maxlength="50" placeholder="请输入描述"></td>
			</tr>
		</table>
		<div style="height:280px;border:solid thin #d1d5de;margin-top: 5px;">
			<div class="title"
				style='background-color: #f6f8fa; font-size: 13px; height: 23px; margin:3px;'>设备
			</div>
			<div style="width:100%;height:250px;overflow-y: auto;">
				<div style="float:left;margin: 3px;" id="deviceInfo">
					<!-- 设备数据 -->
					<div style="float:left;width:130px;height:150px;margin-top:1px;margin-left:1px;border: solid thin #d1d5de;text-align: center;" >
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
</body>
  <script>
  var modulus;
  var jwt;
  $(function() {
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  if(id != 'undefined'){
	  	 setDetailValue(id);
	  	 //查询设备数据
		 searchDevice(id);
	  	 $("#id").val(id);
	  };
	  //初始化下拉框的值
	  initCombox();
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
	  delImg();
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  window.parent.doSearchDev();
		  parent.layer.closeAll();
	  });
  });

  //添加设备
  function doInsertInfo(id){
  	layer.open({
  	    type: 2,
  	    title: '设备', 
  	    fix: false,
  	    shadeClose: false,
  	  	area: ['75%', '90%'],
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
  
  function initCombox(){
		//获取数据字典值并显示在下拉框
		//组类型
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data:{type:'grouptype'},
			dataType:'json',
			success:function(data){
					for(var i=0;i<data.length;i++)
					{
						$("#grouptype").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
						//编辑的时候默认选择
						if(grouptype==data[i].value){
							$("#grouptype option[value='"+grouptype+"']").attr("selected",true);
						}
					}
			}
		});
		//具体类型
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data:{type:'group_type'},
			dataType:'json',
			success:function(data){
					for(var i=0;i<data.length;i++)
					{
						$("#grotype").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
						//编辑的时候默认选择
						if(grotype==data[i].value){
							$("#grotype option[value='"+grotype+"']").attr("selected",true);
						}
					}
			}
		});
		
	}

  //查询设备
   function searchDevice(id){
	  $.ajax({
			url:getContextPath()+"/groupCon/searchGroDev",
			type:'POST',
			data:{group_id:id},
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
							innerHtml += "<div id='div"+data[i].id+"' style='align: left; width: 130px; height:150px; border: solid thin #d1d5de; font-size: 13px;float:left;margin:1px;text-align:center;'>";
							innerHtml += "<a href='javaScript:void(0);' onClick='deleteDevice("+data[i].id+");'><div style='float:right;height:20px;'><img src='imgs/closexx.png' class='delImg' style='width: 20px; height:20px;'/></div></a>";
							innerHtml += "<a href='javaScript:void(0);' onClick='deviceInfo("+data[i].id+");' style='color: #928f8f;text-decoration: none;'>";
							innerHtml += "<div style='margin-top:20px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;'>";
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
	//组类型
  	var grouptype = $("#grouptype").val();
	//具体类型
  	var grotype = $("#grotype").val();
    //名称
    var name = $("#name").val();
    //描述
    var description = $("#description").val();
    var id = $("#id").val();
//     var position = $("#position").val();
    //拼接数据字段
  	var mData = ""+id+grouptype+grotype+name+description+"";
  		mData=encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	//新增设备数据
	var datastr = $("#datastr").val();
  	if(datastr != ""){
  		datastr = "["+datastr+"]";
  	}
  	//删除数据id
  	var delids = $("#delids").val();
  	var index;
  	$.ajax({
  		url : getContextPath()+"/groupCon/update",
  		type : "post",
  		data : {
  			"oper" : "saveOrUpdate",
  			"id" : id,
  			"name" : name,
  			"grotype" :  grotype,
  			"grouptype" :  grouptype,
  			"description" : description,
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
						window.parent.doSearch();
	  		   			parent.layer.closeAll();
					});
  				}else{
  					layer.alert(result[key]);
  				}
  			}
  		},
  		error : function() {
  			layer.alert("保存失败！", {
				title: "提示"
			});
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
