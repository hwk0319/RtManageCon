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
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/jquery-ui.css" />
	<link rel="stylesheet" type="text/css"  href="${pageContext.request.contextPath}/jsLib/jqGrid/css/ui.jqgrid.css" />
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.min.js"></script> 
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/jquery.jqGrid.min.js"></script>
	<script type="text/ecmascript" src="${pageContext.request.contextPath}/jsLib/jqGrid/grid.locale-cn.js"></script>
	<script type="text/javascript"  src="${pageContext.request.contextPath}/jsps/js/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pages/baselinemgt/thresholdmgt/js/thresholdmgt.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsLib/easyui/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/comm/js/general.js"></script>
	<style type="text/css">
	   #model_div table{border-collapse: separate;width: 100%;}
	   #model_div table tr{width: 100%;height: 25px;border-collapse: collapse;margin: 0;padding: 0;}
	   #model_div table tr td{width:calc(100%  - 3px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr .td_item{width:calc(100% / 2 - 3px);border-right: 1px solid #ccc;float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr #td_i {width:calc(100% / 4  - 3px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr #td_i_d {width:calc(100% / 2  - 3px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr #td_val {width:calc(100% / 4  - 3px);float: left;height: 25px;line-height:25px;text-align: center;border-bottom:1px solid #ccc;}
	   #model_div table tr .td_l_v{border-right:1px solid #ccc;}
	   #model_div table tr .td_l_index{width:calc(100% / 4 - 3px);border-right:1px solid #ccc;}
	   #model_div table tr .td_r_index{width:calc(100% / 2  - 3px);border-right:1px solid #ccc;}
	   #model_div table tr .td_r_i{border-right:1px solid #ccc;width:calc(100% / 4  - 3px);}
	   #model_div table tr .td_r_s {width:calc(100% / 4  - 3px);border-right:1px solid #ccc;}
	   #model_div table .td_l{width:calc(100% / 4  - 3px); background-color: #e2f1fb;border-right:1px solid #ccc;}
	   #model_div table .td_model{background-color: #e2f1fb;    border-right: 1px solid #ccc;}
	   #model_div table .td_r{width:calc(100% / 2  - 3px); background-color: #e2f1fb;    border-right: 1px solid #ccc;}
	   .spanbgcol{ background-color:rgb(255, 251, 158);}
	   table,table tr,table tr td{overflow: hidden;}
	</style>
  </head>
  
  <body>
    <div class="div_body" style="width:100%;height: 240px;margin:0 auto;margin-top: 8px; ">
		<div id="model_div" style="overflow-y: auto;width: calc(100%);height: 96%;float: left; border:solid 1px #dddddd;margin-top: 10px;">
			<table id="table_value" style="font-size:13px;">
			   <thead>
			       <tr>
			           <td id="td_val" class="td_l"  >告警等级</td>
			           <td id="td_val" class="td_r">计算方式</td>
			           <td id="td_val" class="td_l"  >条件值</td>
			           <td id="td_val" class="td_r">扣分值</td>
			       </tr>
			       <tr>
		      			<td class="td_r_i">一级</td>
		      			<td class="td_r_s">
		      				 <input readonly="readonly" style=" text-align: center;width: 100%;border: 0; margin-top: -2px;" type="text"  id="method1"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input readonly="readonly"  style="text-align: center; width: 100%;border: 0; margin-top: -2px;" type="text" id="metric_value1"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input  readonly="readonly"  style=" text-align: center;width: 100%;border: 0; margin-top: -2px;" type="text" id="deduct1"></input>
		      			</td>
		      		</tr>
		      		<tr>
		      			<td class="td_r_i">二级</td>
		      			<td class="td_r_s">
		      				<input  readonly="readonly"  style="text-align: center; width: 100%;border: 0; margin-top: -2px;" type="text" id="method2"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input readonly="readonly"  style=" text-align: center;width: 100%;border: 0; margin-top: -2px;" type="text" id="metric_value2"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input readonly="readonly"  style=" text-align: center;width: 100%;border: 0; margin-top: -2px;" type="text" id="deduct2"></input>
		      			</td>
		      		</tr>
		      			<tr><td class="td_r_i">严重</td>
		      			<td class="td_r_s">
		      				 <input readonly="readonly"  style=" text-align: center;width: 100%;border: 0; margin-top: -2px;" type="text" id="method3"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input readonly="readonly"  style=" text-align: center;width: 100%;border: 0; margin-top: -2px;" type="text" id="metric_value3"></input>
		      			</td>
		      			<td class="td_r_s">
		      				<input readonly="readonly"  style=" text-align: center;width: 100%;border: 0; margin-top: -2px;" type="text" id="deduct3"></input>
		      			</td>
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
	</div>
	
	<input type="hidden" id="rule_id"/>
</body>
  <script>
  $(function() {
	  //点击关闭，隐藏表单弹窗
// 	  $("#close").click(function() {
// 		  parent.layer.closeAll();
// 	  });
	  var rule_id=$.getUrlParam('rule_id');
		$.ajax({
			url:getContextPath()+"/thresholdCon/search",
			type:"post",
			async:false,
			data:{"rule_id":rule_id},
			success:function(list){
				 $("#method1").val(list[0].method1)
	    	      $("#method2").val(list[0].method2)
	    	      $("#method3").val(list[0].method3)
	    	      $("#metric_value1").val(list[0].metric_value1)
	    	      $("#metric_value2").val(list[0].metric_value2)
	    	      $("#metric_value3").val(list[0].metric_value3)
	    	      $("#deduct1").val(list[0].deduct1)
	    	      $("#deduct2").val(list[0].deduct2)
	    	      $("#deduct3").val(list[0].deduct3)
			}
		});
  });
  </script>
</html>
