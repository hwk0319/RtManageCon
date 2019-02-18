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
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/configmgt/warnrule/js/warnrulee.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsps/js/encryptedCode.js"></script>
	<style>
		table tr {
		    height: 40px;
		}
	</style>
	<title>告警规则</title>
  </head>
  <body>
	<div style="margin: 1%; font-size: 13px;">
		<table style="width: 100%; font-size: 13px;">
			<tr>
				<td>指标分类：</td>
				<td>
					<select  validate="nn" id="indextype">
					  <option value="">--请选择--</option>
					</select>
				</td>
				<td>指标项：</td>
				<td>
					<select id="indexitem" validate="nn">
					  <option value="">--请选择--</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>阈值上限：</td>
				<td><input type="number" id="upper_limit" min="0" validate="" callback="validateNum();" placeholder="请输入阈值上限"></td>
				<td>阈值下限：</td>
				<td><input type="number" id="lower_limit" min="0" validate="" callback="validateNum1();" placeholder="请输入阈值下限"></td>
			</tr>
			<tr>
				<td>标准值：</td>
				<td><input type="text" validate="nspcl" id="std_value" maxlength="15" placeholder="请输入标准值"></td>
				<td>指定对象：</td>
				<td><input type="text" id="devuid" onclick="deviceList()" style="cursor: pointer;"  readonly class="layui-input" placeholder="点击指定对象(默认所有)"></td>
			</tr>
			<tr>
				<td>告警等级：</td>
				<td>
					<select id="warnlevel" validate="nn">
						<option value="">--请选择--</option>
					</select>
				</td>
				<td>告警类型：</td>
				<td>
					<select id="warntype" validate="nn">
					  <option value="">--请选择--</option>
					</select>
				</td>
			</tr>
		</table>
		<input type="hidden" id="sid" />
		<div class="widget-content" style="margin-top: 200px;">
			<hr class="ui-widget-content" style="margin:1px;border: 1px solid #dddddd;">
			<p  style="text-align:center">
				<button class="btn" onclick="doSave()">
					<i class="icon-ok"></i> 保存
				</button>
				<button id="close" class="btn">
					<i class="icon-remove"></i> 关闭
				</button>
			</p>
		</div>
	</div>
</body>
  <script>
  var modulus;
  var jwt;
  var indextype_arr={},index_arr={};
  $(function() {
	  Initselect();
	  initCombox();
	  //判断是新增还是编辑
	  var id=$.getUrlParam('id');
	  if(id != 'undefined'){
	  	 setDetailValue(id);
	  };
	  //指标分类关联指标项下拉
	  $("#indextype").change(function(){
			getindexinfo(this,$("#indexitem"));
	  });
	  //当指标项发生改变时，相关阈值框置灰
	  $("#indexitem").change(function(){
			var index_warn_id=$(this).val();
			setread(index_warn_id);
	  });
	  //公钥
	  modulus = $("#publicKey",parent.document).val();
	  jwt = $("#jwt",parent.document).val();
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
  });
  
  //获取数据字典值并显示在下拉框
  function initCombox(){
		$.ajax({
			url:getContextPath()+"/commonCon/search",
			type:'POST',
			data:{
				type:'warn_type'
			},
			dataType:'json',
			success:function(data){
					for(var i=0;i<data.length;i++){
						$("#warntype").append("<option  value='"+data[i].value+"'>"+data[i].name+"</option>");
					}
			}
		});
		$.ajax({
			url : getContextPath() + "/commonCon/search",
			type : 'POST',
			data : {
				type : 'warn_level'
			},
			dataType : 'json',
			success : function(data) {
				for (var i = 0; i < data.length; i++) {
					$("#warnlevel").append("<option value='"+data[i].value+"'>"+ data[i].name + "</option>");
				}
			}
		});
	}
  //初始化下拉框
  function Initselect(){
	 indextype_arr=$._PubSelect("#indextype_search",getContextPath()+"/commonCon/searchIndexType","[{'getKey':'indextype_id','getVal':'name'}]");
  	//初始化指标分类下拉项
  	$("#indextype").children('option').remove();
  	for(key in indextype_arr){
  		if(key ==" "){
  			$("#indextype").append("<option value='"+key+"' selected>"+indextype_arr[key]+"</option>");
  		}else{
  			$("#indextype").append("<option value='"+key+"'>"+indextype_arr[key]+"</option>");
  		}
  	}
  }
  //显示的设备类型
  var indexType;
  function setread(index_id){
  	if(index_id!=""){
//   		var warn_rule=0;
  		var upper_limit="";
  		var lower_limit="";
  		var std_value=null;
  		$.ajax({
  			url:getContextPath()+"/indexCon/search?index_id="+index_id,
  			type:"post",
  			async:false,
  			success:function(res){
  				var warn_rule=res[0]["warn_rule"];
  				if(parseInt(warn_rule)==1){
  					$("#upper_limit").val(upper_limit).removeAttr("readonly").css({"background":"#fff"});
  					$("#lower_limit").val(lower_limit).removeAttr("readonly").css({"background":"#fff"});
  					$("#std_value").val("").attr("readonly","readonly").css({"background":"#F5F5F5"});
  					$("#upper_limit").attr("validate",'callback nn');
  					$("#lower_limit").attr("validate",'callback nn');
  					$("#std_value").removeAttr("validate");
  					Validatas.reLoad();
  				}else if(parseInt(warn_rule)==2){
  					$("#upper_limit").val("").attr("readonly","readonly").css({"background":"#F5F5F5"});
  					$("#lower_limit").val("").attr("readonly","readonly").css({"background":"#F5F5F5"});
  					$("#std_value").val(std_value).removeAttr("readonly").css({"background":"#fff"});
  					$("#std_value").attr("validate",'nn nspcl');
  					$("#upper_limit").removeAttr("validate");
  					$("#lower_limit").removeAttr("validate");
  					Validatas.reLoad();
  				}else{
  					$("#upper_limit").val("").removeAttr("readonly").css({"background":"#fff"});
  					$("#lower_limit").val("").removeAttr("readonly").css({"background":"#fff"});
  					$("#std_value").val("").removeAttr("readonly").css({"background":"#fff"});
  					$("#upper_limit").removeAttr("validate");
  					$("#lower_limit").removeAttr("validate");
  					$("#std_value").attr("validate",'nspcl');
  					Validatas.reLoad();
  				}
  			}
  		});
  	}
  }
  //根据指标分类筛选指标项
  function getindexinfo(obj,str){
    var index_type=$(obj).val();
    $(str).html("<option value=' ' selected>--请选择--</option>");
    if(index_type != " "){
  	 	$._PubSelect(str,getContextPath()+"/indexCon/searchindex?index_type="+index_type,"[{'getKey':'index_id','getVal':'description','attr':'warn_rule'}]");
    }else{
    	$("#indexitem").children('option').remove();
    	$("#indexitem").append("<option value=''>--请选择--</option>");
    }
  }
  /**
   * 显示设备
   */
  function deviceList(){
  	if($("#indextype").val().trim() == ""){
  		layer.msg("请选择指标分类！");
  	}else{
  		var layurl = getContextPath()+"/jsp/comm/jsp/deviceComm.jsp";
  		var title = "设备";
  		var uidd = "";
  		var indextype = $("#indextype").val();
  		if(indextype != null){
  			uidd = indextype.split(",")[0].substr(0, 1);
  			if(uidd=='2'){
  				layurl = getContextPath()+"/jsp/comm/jsp/SystemComm.jsp";
  				title = "软件系统";
  			}
  		}
  		callbackShowParmas.alarmSelects = $("#devuid").val();
  		callbackShowParmas.type = "warnlog";
  		callbackShowParmas.indexType = $("#indextype").val().substring(0,1);
//   		var indextype = $("#indextype").val();
  		if(indextype==null || indextype==" " || indextype==""){
  			obj_type = null;
  		}else{
  			var cname = $($("#indextype option[value="+indextype+"]")[0]).html();
  			searchobj(cname);
  		}
  		callbackShowParmas.obj_type = obj_type; //查询对应的对象类型
  		layer.open({
  	  	    type: 2,
  	  	    title: title, 
// 	  	  	title: '', 
// 	  	    closeBtn: 0,
  	  	    fix: false,
  	  	    id:'dev_sys_list',
  	  	    shadeClose: false,
  	  	    area: ['75%', '90%'],
  		    btn: ['确定', '取消'],
  		 	yes: function(index, layero){
  		         var iframeWin = window[layero.find('iframe')[0]['name']];
  		         if(uidd=='2'){
  		        	 iframeWin.doSaveSys();
  				 }else if(uidd=='1'){
  					 iframeWin.doSaveDev();
  				 }else{
  					iframeWin.doSaveDev();
  				 }
  	      },
  	      btn2: function(){
  	       	 layer.close();
  	      },
  	  	    content: [layurl,'no']
  	  	});
  	}
  }
  function setDetailValue(id) {
		$("#indextype").attr("disabled","disabled").css({"background":"#F5F5F5"});
		$("#indexitem").attr("disabled","disabled").css({"background":"#F5F5F5"});
// 		$("#devuid").attr("disabled","disabled").css({"background":"#F5F5F5"});
		$.ajax({
			url:getContextPath()+"/warnruleCon/search?id="+id,
			type:"post",
// 			async:false,
			success:function(data){
				//查询值的回填
				$("#indextype option[value='"+data[0].indextype_id+"']").attr("selected", true);
				getindexinfo("#indextype",$("#indexitem"));
				$("#indexitem option[value='"+data[0].index_warn_id+"']").attr("selected", true);
				setread(data[0].index_warn_id);
				$("#warntype option[value='"+data[0].type+"']").attr("selected", true);
				$("#warnlevel option[value='"+data[0].level+"']").attr("selected", true);
				$("#upper_limit").val(data[0].upper_limit);
				$("#lower_limit").val(data[0].lower_limit);
				$("#std_value").val(data[0].std_value);
				$("#devuid").val(data[0].uid);
				$("#sid").val(data[0].id);
				}
		});
	}
	//验证数字位数
	function validateNum(){
		var val = $("#upper_limit").val();
		if(val.length > 5){
			return "位数不能超过5位";
		}
	}
	function validateNum1(){
		var val = $("#lower_limit").val();
		if(val.length > 5){
			return "位数不能超过5位";
		}
	}
  </script>
</html>
