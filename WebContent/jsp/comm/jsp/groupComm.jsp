<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
response.setHeader("Pragma","No-cache");    
response.setHeader("Cache-Control","no-cache");    
response.setDateHeader("Expires", -10);   
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
<meta http-equiv="expires" content="0">  
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/comm/css/search.css"/>
<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsp/plugins/jquery/css/jquery-ui-redmod.css">
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/groupComm.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/plugins/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
<style>
</style>
<title>组</title>
</head>
<body>
		<div class="srch_con">
			<div class="prom-3" style="font-size: 13px;margin-left: 0px;width: 100%;margin-top: 0px;">
				<div class="w_p30">
					<span class="txt">名称：</span>
					<input type="text" id="name_search" >
				</div>
				<div class="w_p30">
					<span class="txt">类型：</span>
					<select id="grotype" validate="nn" style="width:132px;">
						<option value="">--请选择--</option>
						<option value="1">主从</option>
						<option value="2">并列</option>
					</select>
				</div>
				<div class="w_p30">
					<button id="srch" class="btn"><i class="icon-search"></i>查询</button>
				</div>
			</div>
		</div>
	<div id="main_list"  style="width:98%;margin:1%;">
		<table id="jqGridListGro"></table>
		<div id="jqGridPaperListGro"></div>
	</div>
</body>
 <script type="text/javascript">
 var About;
  $(function(){
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
		  parent.layer.closeAll();
	  });
	  About=$.getUrlParam('About');
	  initGrofunction();
  });

  //保存
  function doSave() {
  	//获取勾选的数据
  	//获取选中行的id
  	var ids=$('#jqGridListGro').jqGrid('getGridParam','selarrrow');
  	if(ids.length == 0){
		layer.msg("请选择组！");
		return;
	}
  	var str = "";
	var idStr = "";
	var oldStr = $("#datastr",parent.document).val();
	var oldidStr = $("#ids",parent.document).val();
  	$(ids).each(function(index,id){
  		rowData = $("#jqGridListGro").jqGrid('getRowData',id);
  		var name = rowData.name;
  		var innerHtml = "";
			innerHtml += "<div id='div"+id+"' group_id='"+id+"' class=\"group_div\" style='align: left; width: 130px; height:150px; border: solid thin #d1d5de; font-size: 13px;float:left;margin:1px;text-align:center;'>";
			innerHtml += "<a href='javaScript:void(0);' onClick='deleteGroup("+id+");'><div style='float:right;height:20px;'><img src='imgs/closexx.png' class='delImg' style='width: 20px; height:20px;'/></div></a>";
			innerHtml += "<a href='javaScript:void(0);' onClick='groupInfo("+id+");' style='color: #928f8f;text-decoration: none;'>";
			innerHtml += "<div style='margin-top:30px;'>";
			innerHtml += "<img src='jsps/img/group.png' style='width:120px;height:66px;margin:5px;'/>";
			innerHtml += "</br></br>"+name+"<br/>";
			innerHtml += "</div>";
			innerHtml += "</a>";
			innerHtml += "</div>";
			$("#groupInfo",parent.document).prepend(innerHtml);
  		if(About!=null&&About=='L'){
  			$("#group_l",parent.document).prepend(innerHtml);
  			$("#About_L",parent.document).val(About);
  		}
  		if(About!=null&&About=='R'){
  			$("#group_r",parent.document).prepend(innerHtml);
  			$("#About_R",parent.document).val(About);
  		}
  		if(index == 0){
			str +="{id:'"+id+"'}";
			//获取id
			idStr += id;
		}else{
			str +=",{id:'"+id+"'}";
			//获取id
			idStr += ","+id;
		}
  	});
  	parent.delImg();
  	if(oldidStr != ""){
		idStr += ","+oldidStr;
	}
  	if(oldStr != ""){
  		str += ","+oldStr;
  	}
  	//组
	$("#datastr",parent.document).val(str);
	$("#ids",parent.document).val(idStr);
	
  	//隐藏表单弹窗
  	parent.layer.closeAll();
  }
</script>
</html>
