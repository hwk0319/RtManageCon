<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;X-Content-Type-Options=nosniff" />
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jsps/css/style.css"/>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/thresholdmgt/js/thresholdmgt.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
	<style type="text/css">
	   #model_div table{border-collapse: separate;width: 100%;}
	   #model_div table tr{width: 100%;height: 25px;border-collapse: collapse;margin: 0;padding: 0;}
	   #model_div table tr td{width:calc(100%  - 3px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr .td_item{width:calc(100% / 2 - 3px);border-right: 1px solid #ccc;float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr #td_i {width:calc(100% / 3  - 21px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr #td_i_d {width:calc(100% / 2  - 15px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr #td_val {width:calc(100% / 4  - 3px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr .td_l_v{border-right:1px solid #ccc;}
	   #model_div table tr .td_l_index{width:calc(100% / 3 - 21px);border-right:1px solid #ccc;}
	   #model_div table tr .td_r_index{width:calc(100% / 2  - 15px);border-right:1px solid #ccc;text-align:left;}
	   #model_div table tr .td_r_i{border-right:1px solid #ccc;width:calc(100% / 4  - 3px);}
	   #model_div table tr .td_r_s {width:calc(100% / 4  - 3px);border-right:1px solid #ccc;}
	   #model_div table .td_l{width:calc(100% / 4  - 3px); background-color: #e2f1fb;border-right:1px solid #ccc;}
	   #model_div table .td_model{background-color: #e2f1fb;    border-right: 1px solid #ccc;}
	   #model_div table .td_r{width:calc(100% / 2  - 3px); background-color: #e2f1fb;    border-right: 1px solid #ccc;}
	   .spanbgcol{ background-color:rgb(255, 251, 158);}
	   table,table tr,table tr td{overflow: hidden;}
	   table {
		    font-size: 13px;
		}
	</style>
  </head>
  <body>
    <div class="div_body" style="width:100%;height: calc(100% - 100px);overflow: hidden;margin:0 auto;margin-top: 8px; ">
	    <div id="model_div" style="overflow-y: auto;width: calc(100% / 6 - 50px);height: 96%;float: left; border:solid 1px #d2cbcb;margin-top: 10px;border-right: 0">
			<div style="text-align: center;line-height: 30px;font-size: 16px;background-color: #a5cdeb;">模型</div>
			<table id="table_model">
			   <thead>
			       <tr>
			           <td class="td_model">名称</td>
			       </tr>
			   </thead>
			   <tbody>
			     <!--   <tr id="tr">
			           <td id="td_id" class="td_l_v"></td>
			           <td id="td_name"  class="td_r_v"></td>
			       </tr> -->
			   </tbody>
			</table>
		</div >
		<div id="model_div" style="overflow-y: auto;width:calc(100% / 4 - 20px);height: 96%;float: left; border:solid 1px #d2cbcb;margin-top: 10px;border-right: 0">
			<div   style="text-align: center;line-height: 30px;font-size: 16px;background-color: #a5cdeb;">模型分项</div>
			<table id="table_item">
			   <thead>
			       <tr>
			           <td class="td_r">名称</td>
			           <td class="td_r">总分</td>
			       </tr>
			   </thead>
			   <tbody>
			     <!--   <tr id="tr">
			           <td id="td_id" class="td_l_v"></td>
			           <td id="td_name"  class="td_r_v"></td>
			       </tr> -->
			   </tbody>
			</table>
		</div>
		<div id="model_div" style="overflow-y: auto;width: calc(100% / 3 - 5px);height: 96%;float: left; border:solid 1px #d2cbcb;margin-top: 10px;">
			<div   style="text-align: center;line-height: 30px;font-size: 16px;background-color: #a5cdeb;">指标项</div>
			<table id="table_index">
			   <thead>
			       <tr>
			           <td id="td_i" class="td_l"  >ID</td>
			           <td id="td_i_d" class="td_r">描述</td>
			           <td id="td_i" class="td_l">总分</td>
			           
			       </tr>
			   </thead>
			   <tbody>
			     <!--   <tr id="tr">
			           <td id="td_id" class="td_l_v"></td>
			           <td id="td_name"  class="td_r_v"></td>
			       </tr> -->
			   </tbody>
			</table>
		</div>
		<div id="model_div" style="margin-left: -1px;overflow-y: auto;width: calc(100% / 3 - 5px);height: 96%;float: left; border:solid 1px #d2cbcb;margin-top: 10px;">
			<div   style="text-align: center;line-height: 30px;font-size: 16px;background-color: #a5cdeb;">参数</div>
			<table id="table_value">
			   <thead>
			       <tr>
			           <td id="td_val" class="td_l"  >告警等级</td>
			           <td id="td_val" class="td_r">计算方式</td>
			           <td id="td_val" class="td_l"  >条件值</td>
			           <td id="td_val" class="td_r">扣分值</td>
			       </tr>
			      </thead>
			       <tbody >
			       <tr>
		      			<td class="td_r_i">一级</td>
		      			<td class="td_r_s">
		      				<select id="method1" style="padding: 0 2%;width:auto;height: 100%;border: 0;">
		      				    <option value="">请选择</option>
		      				    <option value=">" style="text-indent: 40%;">&gt</option>
		      				    <option value="<" style="text-indent: 40%;">&lt</option>
		      					<option value="=" style="text-indent: 40%;">=</option>
		      					<option value=">=" style="text-indent: 40%;">&lt=</option>
		      					<option value="<=" style="text-indent: 40%;">&gt=</option>
		      				</select>
		      			</td>
		      			<td class="td_r_s">
		      				<input style=" width: 100%;border: 0; margin-top: -2px;" type="text" id="metric_value1"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input style=" width: 100%;border: 0; margin-top: -2px;" type="text" id="deduct1"></input>
		      			</td>
		      		</tr>
		      		<tr>
		      			<td class="td_r_i">二级</td>
		      			<td class="td_r_s">
		      				<select id="method2" style="padding: 0 2%;width:auto;height: 100%;border: 0;">
		      					<option value="">请选择</option>
		      				    <option value=">" style="text-indent: 40%;">&gt</option>
		      				    <option value="<" style="text-indent: 40%;">&lt</option>
		      					<option value="=" style="text-indent: 40%;">=</option>
		      					<option value=">=" style="text-indent: 40%;">&lt=</option>
		      					<option value="<=" style="text-indent: 40%;">&gt=</option>
		      				</select>
		      			</td>
		      			<td class="td_r_s">
		      				<input style=" width: 100%;border: 0; margin-top: -2px;" type="text" id="metric_value2"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input style=" width: 100%;border: 0; margin-top: -2px;" type="text" id="deduct2"></input>
		      			</td>
		      		</tr>
		      			<tr><td class="td_r_i">严重</td>
		      			<td class="td_r_s">
		      				<select id="method3" style="padding: 0 2%;width:auto;height: 100%;border: 0;">
		      				    <option value="">请选择</option>
		      				    <option value=">" style="text-indent: 40%;">&gt</option>
		      				    <option value="<" style="text-indent: 40%;">&lt</option>
		      					<option value="=" style="text-indent: 40%;">=</option>
		      					<option value=">=" style="text-indent: 40%;">&lt=</option>
		      					<option value="<=" style="text-indent: 40%;">&gt=</option>
		      				</select>
		      			</td>
		      			<td class="td_r_s">
		      				<input style=" width: 100%;border: 0; margin-top: -2px;" type="text" id="metric_value3"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input style=" width: 100%;border: 0; margin-top: -2px;" type="text" id="deduct3"></input>
		      			</td>
		      		</tr>
		           	<tr>
		           		<th style="font-size: 13px;color: rgb(205, 10, 10);font-weight: 100;">***每行数据必须填写完整或不填***</th>
		           	</tr>
			   </tbody>
			</table>
		</div>
	</div>
	
    <div class="widget-content" style="margin-top: 10px;">
		<hr class="ui-widget-content" style="margin:1px">
		<p  style="text-align:center">
			<button class="btn" onclick="doSave(1)">
				<i class="icon-ok"></i> 保存并退出
			</button>
			<button class="btn" id="add" onclick="doSave(2)">
				<i class="icon-ok"></i> 保存并添加
			</button>
			<button id="close" class="btn">
				<i class="icon-remove"></i> 关闭
			</button>
		</p>
	</div>
    	
	<div id="showDiv" style="position: absolute; background-color: white; border: 1px solid black;"></div>

	<input type="hidden" id="rule_id"/>
</body>
  <script>
  var model_id;
  var model_item_id;
  var index_id=null;
  var model_name;
  var model_item_name;
  var oper="add";
  $(function() {
	  //点击关闭，隐藏表单弹窗
	  $("#close").click(function() {
// 		  window.parent.doSearch();
		  parent.layer.closeAll();
	  });
	  var rule_id=$.getUrlParam('rule_id');
	 if(rule_id != 'undefined'){
		 $("#add").css({"display":"none"});
		 oper="up";
		 index_id=$.getUrlParam('index_id');
		 model_id=$.getUrlParam('model_id');
		 model_item_id=$.getUrlParam('model_item_id');
		  $("#rule_id").val(rule_id);
	 }
	  modellist();
	  
	  $("#table_value tbody").css({"display":"none"});
  });
  function search(){
	  $("#table_model tbody").html("");
	  $("#table_item tbody").html("");
	  $("#table_index tbody").html("");
	  modellist();
  }
  function modellist(){
	  $.ajax({
			url:getContextPath()+"/thresholdCon/searchmodel",
			type:'POST',
			data:{'model_name':$('#name_search').val(),'model_id':model_id},
			dataType:'json',
			success:function(data){
				var str="";
				for(var i=0;i<data.length;i++)
				{
				//	$("#table_model tbody").append("<tr id=\""+data[i].model_id+"\"  onclick=\"changeitem(this)\" ><td  class=td_r_v>"+data[i].model_name+"</td></tr>");
					str+="<tr id=\""+data[i].model_id+"\"  onclick=\"changeitem(this)\" ><td  class=td_r_v>"+data[i].model_name+"</td></tr>";
				}
				$("#table_model tbody").html(str);
				if(model_id!=null&&model_id!=""){
					$("#table_model #"+model_id).click();
				}
			}
		});
  }

  function changeitem(obj) {
	  $("#table_value tbody").css({"display":"none"});
	  $("#table_index tbody").html("");
	  $("#table_item tbody").html("");
	  $("#table_model tbody tr").removeClass("spanbgcol");
      $(obj).addClass("spanbgcol");
      var model_id= $(obj).attr("id");
//       var model_name=$(obj).find("td").eq(0).text();
      $.ajax({
			url:getContextPath()+"/thresholdCon/searchmodelitem",
			type:'POST',
			data:{model_id:model_id,model_item_id:model_item_id},
			dataType:'json',
			success:function(data){
				var str="";
				for(var i=0;i<data.length;i++)
				{
					str+="<tr id=\""+data[i].model_item_id+"\" onclick=\"changeindex(this)\" ><td  class=td_item style='text-align:left;'>"+data[i].model_item_name+"</td><td  class=td_item>"+data[i].total_score+"</td></tr>";
				}
				$("#table_item tbody").html(str);
				
				if(model_item_id!=null&&model_item_id!=""){
					$("#table_item #"+model_item_id).click();
				}
				  
			}
		});
      
	}
  function changeindex(obj) {
	  $("#table_value tbody").css({"display":"none"});
	  $("#table_item tbody tr").removeClass("spanbgcol");
      $(obj).addClass("spanbgcol");
      var model_item_id =$(obj).attr("id");
//       var model_item_name=$(obj).find("td").eq(0).text();
     	$.ajax({
			url:getContextPath()+"/thresholdCon/searchindex?oper="+oper,
			type:'POST',
			data:{
					model_item_id : model_item_id,
					index_id : index_id
				},
			dataType:'json',
			success:function(data){
				var str="";
				if(data.length=='0'){
					str +=  "<tr> <th style=\"font-size: 14px;color: rgb(205, 10, 10);\">***该模型分项下指标项已全部添加过***</th></tr>";
				}else{
					for(var i=0;i<data.length;i++)
					{
						str+="<tr id=\""+data[i].metric_id+"\" onclick=\"Choice(this)\" ><td  class=td_l_index>"+data[i].metric_id+"</td><td  title=\""+data[i].description+"\"  class=td_r_index>"+data[i].description+"</td><td  class=td_l_index>"+data[i].total_score+"</td></tr>";
					}
				}
				$("#table_index tbody").html(str);
			
				if(index_id!=null&&index_id!=""){
					$("#table_index #"+index_id).click();
				}
				 
			}
		}); 
      
	}
  function Choice(obj) {
	  $("#table_index tbody tr").removeClass("spanbgcol");
      $(obj).addClass("spanbgcol");
//       var index_id =$(obj).attr("id");
      var rule_id=$("#rule_id").val();
      var rule_name=$.getUrlParam('rule_name');
      var method1=$.getUrlParam('method1');
      var method2=$.getUrlParam('method2');
      var method3=$.getUrlParam('method3');
      var metric_value1=$.getUrlParam('metric_value1');
      var metric_value2=$.getUrlParam('metric_value2');
      var metric_value3=$.getUrlParam('metric_value3');
      var deduct1=$.getUrlParam('deduct1');
      var deduct2=$.getUrlParam('deduct2');
      var deduct3=$.getUrlParam('deduct3');
	    $("#method1").val("");
	      $("#method2").val("");
	      $("#method3").val("");
	      $("#metric_value1").val("");
	      $("#metric_value2").val("");
	      $("#metric_value3").val("");
	      $("#deduct1").val("");
	      $("#deduct2").val("");
	      $("#deduct3").val("");
      if(rule_id != null&&rule_id!=""){
    	  $("#rule_name").val(rule_name);
    	    $("#method1").val(method1);
    	      $("#method2").val(method2);
    	      $("#method3").val(method3);
    	      $("#metric_value1").val(metric_value1);
    	      $("#metric_value2").val(metric_value2);
    	      $("#metric_value3").val(metric_value3);
    	      $("#deduct1").val(deduct1);
    	      $("#deduct2").val(deduct2);
    	      $("#deduct3").val(deduct3);
	 }
      $("#table_value tbody").css({"display":"inline"});
     
   }
  function doSave(btn) {
	 	 var model_id= $("#table_model tbody .spanbgcol").attr("id");
	 	 var model_item_id= $("#table_item tbody .spanbgcol").attr("id");
	 	 var index_id= $("#table_index tbody .spanbgcol").attr("id");
	  	var rule_id=$("#rule_id").val();
		var rule_name=""; 
	  	var method1=$("#method1").val();
	  	var method2=$("#method2").val();
	  	var method3=$("#method3").val();
	  	var metric_value1=$("#metric_value1").val();
	  	var metric_value2=$("#metric_value2").val();
	  	var metric_value3=$("#metric_value3").val();
	  	var deduct1=$("#deduct1").val();
	  	var deduct2=$("#deduct2").val();
	  	var deduct3=$("#deduct3").val();
	 	 if(model_id==""||model_id==null){
		  		layer.alert("请选择模型！");
		  		return;
		  	}
			if(model_item_id==""||model_item_id==null){
		  		layer.alert("请选择模型分项！");
		  		return;
		  	}
			if(index_id==""||index_id==null){
		  		layer.alert("请选择指标项！");
		  		return;
		  	} 
		/* 	if(rule_name==""||rule_name==null){
		  		layer.alert("请填写指标阈值名称！");
		  		return;
		  	}  */
			var variable =  false;
			if(method1!=""||metric_value1!=""||deduct1!=""){
				if(method1!=""&&metric_value1!=""&&deduct1!=""){
					variable = true;
				}else{
					layer.alert("第一行未填写完整！");
					return;
				}
			}
			if(method2!=""||metric_value2!=""||deduct2!=""){
				if(method2!=""&&metric_value2!=""&&deduct2!=""){
					variable = true;
				}else{
					layer.alert("第二行未填写完整！");
					return;
				}
			}
			if(method3!=""||metric_value3!=""||deduct3!=""){
				if(method3!=""&&metric_value3!=""&&deduct3!=""){
					variable = true;
				}else{
					layer.alert("第三行未填写完整！");
					return;
				}
			}
			if(variable){
			}else{
				layer.alert("请至少填写一行完整数据");
				return;
			}
	  	$("#model_name",parent.document).val(model_name);
		$("#model_id",parent.document).val(model_id);
		$("#model_item_id",parent.document).val(model_item_id);
	 	$("#model_item_name",parent.document).val(model_item_name);
	 	$("#metric_id",parent.document).val(index_id);
	 	$("#method1",parent.document).val(method1);
	 	$("#method2",parent.document).val(method2);
	 	$("#method3",parent.document).val(method3);
	 	$("#metric_value1",parent.document).val(metric_value1);
	 	$("#metric_value2",parent.document).val(metric_value2);
	 	$("#metric_value3",parent.document).val(metric_value3);
	 	$("#deduct1",parent.document).val(deduct1);
	 	$("#deduct2",parent.document).val(deduct2);
	 	$("#deduct3",parent.document).val(deduct3);
	 	  $.ajax({
		  		url : getContextPath()+"/thresholdCon/update?oper=saveOrUpdate",
		  		type : "post",
		  		data : {
		  			"rule_id" : rule_id,
		  			"rule_name" : rule_name,
		  			"method1" : method1,
		  			"method2" : method2,
		  			"method3" : method3,
		  			"metric_value1" : metric_value1,
		  			"metric_value2" : metric_value2,
		  			"metric_value3" : metric_value3,
		  			"deduct1" : deduct1,
		  			"deduct2" : deduct2,
		  			"deduct3" : deduct3,
		  			"model_id" :  model_id,
		  			"model_item_id":model_item_id,
		  			"metric_id":index_id
		  			
		  		},
		  		success : function(result) {
		  			layer.alert("保存成功！");
		  			if(btn==1){
		  				window.parent.doSearch();
			  			parent.layer.closeAll();
		  			}else{
		  				var model_item_id= $("#table_item tbody .spanbgcol").attr("id");
		  				$("#table_item #"+model_item_id).click();
		  			}
		  			
		  		},
		  		error : function() {
		  			layer.alert("保存失败！");
		  		}
		  	});
		//隐藏表单弹窗
	//	parent.layer.closeAll();
	}
  
  </script>
  
</html>
