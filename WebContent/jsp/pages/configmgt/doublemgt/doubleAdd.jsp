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
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/groupComm.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/doublemgt/js/doublemgt.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
	<style type="text/css">
		::-webkit-input-placeholder { /* WebKit browsers */  
		    color:    #ccc;  
		    font-size: 14px;
		}  
		:-ms-input-placeholder { /* Internet Explorer 10+ */  
		   color:    #ccc;  
		    font-size: 14px;
		}  
		/* 页面滚动条样式 */
		*::-webkit-scrollbar-track-piece {
		    background: #fff;
		}
		*::-webkit-scrollbar-track
		{
		  -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
		  background-color: #F5F5F5;
		}
		*::-webkit-scrollbar
		{
		  width: 5px;
		  background-color: #F5F5F5;
		}
		*::-webkit-scrollbar-thumb
		{
		  background-color: #e8eaea;
		}
	</style>
	<title>双活</title>
  </head>
  <body>
    <div>
    	<table id="doubleInfo" style="align: left; width: 100%; border: solid thin #d1d5de; font-size: 13px;">
			<tr>
				<td colspan="8" style="background-color: #f6f8fa">双活</td>
			</tr>
			<tr>
				<td>名称：</td>
				<td><input type="text" id="name" placeholder="请输入名称" validate="nn nspcl" maxlength="15"></td>
				<td>具体类型：</td>
				<td>
					<select id="doubtype" style="width:182px;" validate = "nn">
						<option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>A中心名称：</td>
				<td><input type="text" placeholder="请输入A中心名称" id="core_l" validate="nn nspcl" maxlength="15" /></td>
				<td>B中心名称：</td>
				<td>
					<input type="text" placeholder="请输入B中心名称" id="core_r" validate="nn nspcl" maxlength="15" />
				</td>
			</tr>
			<tr>
				<td>描述：</td>
				<td><input type="text" id="description" maxlength="50" placeholder="请输入描述"></input></td>
			</tr>
		</table>
		<div class="div_core" style="height: 55%;overflow:hidden;border:1px solid #ccc;margin:10px 0 10px 0;font-size:15px;">
			<div class="div_left"
				style="width:50%;height:100%;float:left;border-right:1px solid #ccc ">
				<span id="groupa"
					style="width:99.9%;background-color: #f6f8fa;line-height:25px;overflow:hidden;float:left;text-indent:5px; ">A中心组</span>
				<div id="group_l" class="groupdiv"
					style="width:100%;overflow:hidden;float:left;overflow-y:auto;height: 90%;">
					<span
						style="margin-left:2px;margin-top:1px;border:solid thin #d1d5de;display:inline-block;width:129px;height:150px;float:left;">
						<img src="imgs/add.png"
						style="margin-top:60px;margin-left:50px;width:25px;height:25px;cursor: pointer;"
						onClick="doInsertInfo('L');" />
					</span>
				</div>
			</div>
			<div class="div_right" style="width:calc(50% - 2px);height:100%;float:left; ">
				<span id="groupb"
					style="width:100%;background-color: #f6f8fa;line-height:25px;overflow:hidden;float:left;text-indent:5px; ">B中心组</span>
				<div id="group_r" class="groupdiv"
					style="width:100%;overflow:hidden;float:left;overflow-y:auto;height: 90%;">
					<span
						style="margin-left:2px;margin-top:1px;border:solid thin #d1d5de;display:inline-block;width:129px;height:150px;float:left;">
						<img src="imgs/add.png"
						style="margin-top:60px;margin-left:50px;width:25px;height:25px;cursor: pointer;"
						onClick="doInsertInfo('R');" />
					</span>
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
  	<input type="hidden" id="id_R"/>	
    <input type="hidden" id="id_L"/>
	<input type="hidden" id="uid"/>
	<input type="hidden" id="id"/>
	<input type="hidden" id="ids"/>
	<input type="hidden" id="delids"/>
	<input type="hidden" id="About_L"/>
	<input type="hidden" id="About_R"/>
</body>
  <script>
  var modulus;
  var jwt;
  $(function() {
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
	
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  if(id != 'undefined'){
		  setDetailValue(id);
	  	 //查询设备数据
		 searchGroup(id);
	  	 $("#id").val(id);
	  };
	  
	  //初始化下拉框的值
	  initCombox();

	  delImg();
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
  });

  //添加组
  function doInsertInfo(About){
  	layer.open({
  	    type: 2,
  	    title: '组', 
  	    fix: false,
  	    shadeClose: false,
  	  	area: ['75%', '85%'],
	    btn: ['确定', '取消'],
	 	yes: function(index, layero){
	         var iframeWin = window[layero.find('iframe')[0]['name']];
	         iframeWin.doSave();
	      },
	      btn2: function(){
	       	 layer.closeAll();
	      },
  	    content: [getContextPath()+"/jsp/comm/jsp/groupComm.jsp?About="+About,'no']
  	});
  }
//组详情
  function groupInfo(id){
  	layer.open({
  	    type: 2,
  	    title: '组详情', 
  	    fix: false,
  	    shadeClose: false,
  	    area: ['60%', '55%'],
  	    content: [getContextPath()+"/jsp/comm/jsp/groupinfo.jsp?id="+id,'no']
  	});
  }
  function initCombox(){
		//获取数据字典值并显示在下拉框
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data:{type:'double_type'},
			dataType:'json',
			success:function(data){
					for(var i=0;i<data.length;i++)
					{
						if(data[i].value == "1" || data[i].value == "2"){
							$("#doubtype").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
							//编辑的时候默认选择
							if(doubtype==data[i].value){
								$("#doubtype option[value='"+doubtype+"']").attr("selected",true);
							}
						}
					}
			}
		});
		
	}

    //查询组
    function searchGroup(id){
	  $.ajax({
			url:getContextPath()+"/doublemgtCon/search_group_core",
			type:'POST',
			data:{double_id:id},
			dataType:'json',
			async:false,
			success:function(data){
				if(data.length != null){
					var ids = "";
					for(var i=0;i<data.length;i++){
						if(data[i].core_tagging=='L'){
							$("#core_l").val(data[i].core);
							$("#id_L").val(data[i].core_id);
						}
						if(data[i].core_tagging=='R'){
							$("#core_r").val(data[i].core);
							$("#id_R").val(data[i].core_id);
						}
						var group_id=data[i].group_id;
						var group_name=data[i].group_name;
						var innerHtml = "";
						innerHtml += "<div id='div"+group_id+"' group_id='"+group_id+"' class=\"group_div\" style='align: left; width: 130px; height:150px; border: solid thin #d1d5de; font-size: 13px;float:left;margin:1px;text-align:center;'>";
						innerHtml += "<a href='javaScript:void(0);' onClick='deleteGroup("+group_id+");'><div style='float:right;height:20px;'><img src='imgs/closexx.png' class='delImg' style='width: 20px; height:20px;'/></div></a>";
						innerHtml += "<a href='javaScript:void(0);' onClick='groupInfo("+group_id+");' style='text-decoration: none;color: #928f8f;'>";
						innerHtml += "<div style='margin-top:30px;'>";
						innerHtml += "<img src='jsps/img/group.png' style='width:120px;height:66px;margin:5px;'/>";
						innerHtml += "</br></br>"+group_name+"<br/>";
						innerHtml += "</div>";
						innerHtml += "</a>";
						innerHtml += "</div>";
						$("#groupInfo").prepend(innerHtml);
						if(data[i].core_tagging=='L'){
				  			$("#group_l").prepend(innerHtml);
				  		}
				  		if(data[i].core_tagging=='R'){
				  			$("#group_r").prepend(innerHtml);
				  		}
						if(i==0){
							ids += data[i].group_id;
						}else{
							ids += ","+data[i].group_id;
						}
						$("#ids").val(ids);
					}
				}
			}
		});
  } 
  
 	//保存
   function doSave(){
 	 if(!validateHandler()){
		return;
	 }
	 var l_group= $("#group_l .group_div").length;
	 var r_group= $("#group_r .group_div").length;
	 if(l_group<=0||r_group<=0){
	 		layer.msg("请选择组！");
	  		return;
	 }
	 var group_ids_L="";
	 var group_ids_R="";
		//名称
		var name = $("#name").val();
		//类型
	  	var doubtype = $("#doubtype").val();
	  	var description = $("#description").val();
		var core_l = $("#core_l").val();
		var core_r = $("#core_r").val();
		var id = $("#id").val();
 	 	
 		 $("#group_l .group_div").each(function(){
 	 		  var id=$(this).attr("group_id");
 	 		group_ids_L+=id+",";
 	 	  });
	 
 		 $("#group_r .group_div").each(function(){
 	 		  var id=$(this).attr("group_id");
 	 		group_ids_R+=id+",";
 	 	  });
 		 
 		//拼接数据字段
 	  	var mData = ""+id+name+doubtype+description+core_l+core_r+group_ids_L+group_ids_R+"";
 	  		mData=encodeURIComponent(mData);//中文转义
 	  		mData = $.toRsaMd5Encrypt(mData, modulus);
 	  var index;
 	  $.ajax({
 	  		url : getContextPath()+"/doublemgtCon/update?oper=saveOrUpdate",
 	  		type : "post",
 	  		data : {
 	  			"id" : id,
 	  			"name" : $("#name").val(),
 	  			"doubtype" :  $("#doubtype").val(),
 	  			"description" : $("#description").val(),
 	  			"core_l":$("#core_l").val(),
 	  			"core_r":$("#core_r").val(),
 	  			"id_L":$("#id_L").val(),
 	  			"id_R":$("#id_R").val(),
 	  			"mData":mData,
 	  			"jwt" : jwt,
 	  			group_ids_R:group_ids_R,
 	  			group_ids_L:group_ids_L
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
 	  			for (key in result){
 	  				if(key == "success"){
 		  				layer.alert(result[key], {
 							title: "提示"
 						},function(){
 							layer.close(index);
 							window.parent.doSearchDou();
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
  function deleteGroup(id){
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
