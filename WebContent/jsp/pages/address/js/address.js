var modulus;
var jwt;
$(function(){
	//公钥
	modulus = $("#publicKey",parent.document).val();
	jwt = $("#jwt",parent.document).val();
	createAddressGrid();
	$("#srch").click(doSearch);
	
	var id=$.getUrlParam('id');
	if(id != 'undefined'){
	  	 setDetailValue(id);
	};
	//点击关闭，隐藏表单弹窗
	$("#close").click(function() {
		parent.layer.closeAll();
	});
});
function createAddressGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListAddress").jqGrid({
		url:getContextPath()+"/addressCon/search",
		datatype:'json',//请求数据返回的类型
		postData:{
			 'name': $("#name_search").val(),
			 'phone': $("#phone_search").val(),
			 'email': $("#email_search").val(),
			 'address': $("#address_search").val()
		},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'姓名',name:'name',index:'name',width:100,align:'center'},
				   {label:'手机号',name:'phone',index:'phone',width:100,align:'center'},
				   {label:'邮箱',name:'email',index:'email',width:100,align:'center'},
				   {label:'地址',name:'address',index:'address',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
		rownumbers: true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		multiselect: false,//复选框
		caption:"<div style='width:95%;'>联系人<span id='add' style='cursor:pointer;float:right;' onclick='AddressAdd()'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/addressCon/update",//编辑地址
		pager: "#jqGridPagerListAddress", 
		gridComplete: function(){
            var ids = $("#jqGridListAddress").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                ed = "<a href=# title='编辑' style='text-decoration: none;' onclick='AddressAdd("+cl+")'>编辑 </a>";
                vi = "<a href=# title='删除' style='text-decoration: none;' onclick='doDelete("+cl+")'>删除</a>"; 
                jQuery("#jqGridListAddress").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            } 
        },
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListAddress").setGridWidth(GridWidth);
});
//重新加载页面
function doSearch(){
	$.jgrid.gridUnload('#jqGridListAddress');
	createAddressGrid();
}

//新增
function AddressAdd(id){
	getJWT();
    layer.open({
        type: 2,
        title: '联系人',
        fix: false,
        shadeClose: false,
        moveOut: true,
        area: ['550px','350px'],
//        area: ['40%', '55%'],
        content: [getContextPath()+"/jsp/pages/address/addressAdd.jsp?id="+id,'no']
    });
}

//保存
function doSave() {
	if(!validateHandler()){
		return;
	}
	var id = $("#id").val();
	var name = $("#name").val();
	var phone = $("#phone").val();
	var email = $("#email").val();
	var address = $("#address").val();
	//拼接数据字段
  	var mData = ""+id+name+phone+email+address+"";
  		mData = encodeURIComponent(mData);//中文转义
  		mData = $.toRsaMd5Encrypt(mData, modulus);
	var oper = "add";
	var id = $("#id").val();
	if(id != ""){
		oper = "edit";
	}
	//把数据保存到数据库中
	var index;
	var jwt2 = $("#jwt",parent.document).val();
	$.ajax({
  		url : getContextPath()+"/addressCon/update?oper="+oper,
  		type : "post",
  		dataType : 'json',
  		data : {
  			"id" : $("#id").val(),
  			"name" : $("#name").val(),
  			"phone" : $("#phone").val(),
  			"email" : $("#email").val(),
  			"address" : $("#address").val(),
  			"mData" : mData,
  			"jwt" : jwt2
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
  			for (key in result) {
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

function setDetailValue(id) {
	var rs = load(id);//此方法为同步、阻塞式，异步实现方法见userManager.js
	if (rs.length>0){
		$("#id").val(rs[0].id);
		$("#name").val(rs[0].name);
		$("#phone").val(rs[0].phone);
		$("#email").val(rs[0].email);
		$("#address").val(rs[0].address);
    }
}
//编辑加载数据
function load(id) {
	var resultvalue;
	$.ajax({
		url : getContextPath() + "/addressCon/search",
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
			layer.msg("加载失败");
		}
	});
	return resultvalue;
}

//删除
function doDelete(id){
	var jwt1 = getJWT();
	layer.confirm("确定删除这条数据？", {
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/addressCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt1
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
	  			window.parent.doSearch();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}