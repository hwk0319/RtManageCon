var in_pwd;
var out_pwd;
//设置一个值用来标记服务器是否被使用，默认未被使用
var isused = 0;
function initDevfunction(){
	//页面加载完成之后执行
	creatDeviceGrid();
	$("#deviceSrch").click(doSearchDev);
	$("#tabs").tabs();
	//获取数据字典值并显示在下拉框
	$.ajax({
		url:getContextPath()+"/commonCon/search",
		type:'POST',
		data:{type:'device_type'},
		dataType:'json',
		success:function(data){
			for(var i=0;i<data.length;i++)
			{
				//只显示服务器和交换机
				if(data[i].value == "1" || data[i].value == "10"){
					$("#devicetype_search").append("<option value='"+data[i].value+"'>"+data[i].name+"</option>");
				}
			}
		}
	});
}
function creatDeviceGrid(){
	//创建jqGrid组件
	jQuery("#jqGridListDev").jqGrid({
		altRows:true,
		altclass:'jqgridRowColor',
		url:getContextPath()+"/devicesCon/search",
		datatype:'json',//请求数据返回的类型
		postData : {
			'devicetype' : $("#devicetype_search").val(),
			'name' : $("#name_search").val(),
			'factory' : $("#factory_search").val(),
			'in_ip' : $("#ip_search").val()
		},
		colModel:[
				   {label:'id',name:'id',width:0,key:true,hidden:true},
				   {label:'统一ID',name:'uid',index:'uid',width:100,align:'center'},
				   {label:'类型',name:'devicetypename',index:'devicetypename',width:100,align:'center'},
				   {label:'厂商',name:'factoryname',index:'factoryname',width:100,align:'center'},
				   {label:'设备型号',name:'model',index:'model',width:100,align:'center'},
//				   {label:'SN码',name:'sn',index:'sn',width:100,align:'center'},
				   {label:'操作系统',name:'opersysname',index:'opersysname',width:100,align:'center'},
				   {label:'主机名',name:'name',index:'name',width:100,align:'center'},
				   {label:'应用IP',name:'in_ip',index:'in_ip',width:100,align:'center'},
//				   {label:'带外IP',name:'out_ip',index:'out_ip',width:100,align:'center'},
				   {label:'端口',name:'port',index:'port',width:100,align:'center'},
//				   {label:'资产编号',name:'assetno',index:'assetno',width:100,align:'center'},
//				   {label:'位置信息',name:'positionName',index:'positionName',width:100,align:'center'},
				   {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
				],
		loadonce:true,
		rownumbers: true,
		rowNum:100,//一页显示多少条
		rowList:[100,200,300],//可供用户选择一页显示多少条
		sortname:'id',//初始化的时候排序的字段
		sortorder:'desc',//排序方式
		mtype:'post',//向后台请求数据的Ajax类型
		viewrecords:true,//定义是否要显示总记录数
		caption:"<div style='color:#000;background-color: #7ab8dd;width:95%;height:100%;'>设备管理<span  id='deviceAdd' style='float:right;cursor:pointer;' onclick='doInsert()'> <i class='icon-plus-sign'></i>&nbsp;新增</span></div>",
		height:'auto',
		autowidth:true,
		editurl:getContextPath()+"/devicesCon/update",//编辑地址
		pager : "#jqGridPaperListDev",
		gridComplete: function(){
            var ids = $("#jqGridListDev").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                ed = "<a href=# title='编辑' style='text-decoration: none;' onclick='doInsert("+ids[i]+");'>编辑 </a>";
                vi = "<a href=# title='删除' style='text-decoration: none;' onclick='doDelete("+cl+");'>删除</a>"; 
                jQuery("#jqGridListDev").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            } 
        }
	});
}
//删除
function doDelete(id){
	var jwt = getJWT();
	layer.confirm("确定删除这条数据？",{
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/devicesCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : id,
	  			"jwt" : jwt
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
				window.parent.doSearchDev();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	});
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
	var width = document.body.clientWidth;
	var GridWidth = (width-250);
	$("#jqGridListDev").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});

//重新加载页面
function doSearchDev(){
	$.jgrid.gridUnload('#jqGridListDev');
	creatDeviceGrid();
}

//添加
function doInsert(id){
	getJWT();
	layer.open({
	    type: 2,
	    title: '设备', 
	    fix: false,
	    shadeClose: false,
	    area: ['880px','550px'],
//	    area: ['65%','83%'],
	    moveOut: true,
	    content: [getContextPath()+"/jsp/pages/configmgt/device/deviceAdd2.jsp?id="+id,'no'],
	});
}

//保存
function doSave() {
	if(!validateHandler()){
		return;
	}
  	//类型
  	var devicetype = $("#devicetype").val();
    //厂商
  	var factory = $("#factory").val();
    //型号
  	var model = $("#model option:selected").text();
  	//位置
//  	var position = $("#position").val();
  	//资产编号
  	var assetno = $("#assetno").val();
  	//应用IP
  	var in_ip = $("#in_ip").val();
	//应用用户名
	var in_username = $("#in_username").val();
	var out_username = $("#out_username").val();
	//操作系统
	var opersys = $("#opersys").val();
	//带外IP
	var out_ip = $("#out_ip").val();
  	//主机名
  	var name =$("#name").val();
  	//sn码
  	var sn = $("#sn").val();
  	//端口
  	var port = $("#port").val();
  	in_pwd = $("#in_password").val();
  	out_pwd = $("#out_password").val();
  	//只rsa加密
  	if(in_pwd != ""){
  		in_pwd = $.toRsaEncrypt(in_pwd, modulus);
  	}
  	if(out_pwd != ""){
  		out_pwd = $.toRsaEncrypt(out_pwd,modulus);
  	}
  	
	var id = $("#id").val();
  	var uid = $("#uid").val();
	//拼接数据字段
  	var mData = ""+id+uid+devicetype+factory+model+assetno+in_ip+in_username+opersys+out_ip+out_username+name+sn+in_pwd+out_pwd+"";
	  	mData = encodeURIComponent(mData);//中文转义	
	  	mData = $.toRsaMd5Encrypt(mData, modulus);
  	//数据加密
  	var parent_id = "0";
  	in_username = $.toRsaEncrypt(in_username, modulus);
  	out_username = $.toRsaEncrypt(out_username, modulus);
  	
  	//修改数据
  	var dataedit = $("#dataedit").val();
  	if(dataedit!=""){
  		dataedit = "["+dataedit+"]";
  	}
  	//新增数据
  	var datastr = $("#datastr").val();
  	if(datastr!=""){
  		datastr = datastr.replace(/_/g, ",");
  		datastr = "["+datastr+"]";
  	}
    //删除数据id
  	var delids = $("#delids").val();
  	var index;
  	$.ajax({
  		url : getContextPath()+"/devicesCon/update",
  		type : "post",
  		data : {
  			"oper" : "saveOrUpdate",
  			"id" : id,
  			"uid" : uid,
  			"devicetype" : devicetype,
  			"parent_id" : parent_id,
  			"sn" : sn,
  			"model" : model,
  			"in_ip" : in_ip,
  			"in_username" : in_username,
  			"in_password" : in_pwd,
  			"out_ip" : out_ip,
  			"out_username" : out_username,
  			"out_password" : out_pwd,
  			"opersys" : opersys,
  			"assetno" : assetno,
  			"factory" : factory,
  			"name" : name,//新增字段
  			"port" : port,//新增字段
  			"ids" : delids,
  			"adddata" : datastr,//附属设备数据
  			"editdata" : dataedit,
  			"mData" : mData,
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
  			for (key in result) {
  				if(key == "success"){
	  				layer.alert(result[key], {
						title: "提示"
					},function(){
						layer.close(index);
						window.parent.doSearchDev();
						parent.layer.closeAll();
					});
  				}else{
  					layer.alert(result[key]);
  				}
  			}
  		},
  		error : function() {
  			layer.alert('保存失败！');
  			layer.close(index);
  		}
  	});
  }
