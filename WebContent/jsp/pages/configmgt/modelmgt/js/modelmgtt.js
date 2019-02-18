var type_arr={};
factory_type="";
$(function(){
    type_arr=$._PubSelect("#factory_select",getContextPath()+"/commonCon/customSearch?type=switchboard_factory,server_factory,loadB_factory","[{'getKey':'value','getVal':'name'}]");
    deviceInit();
    $("#srch").click(doSearch);
    $("#reset").click(doClear);
    $("#reflash").click(doSearch);
    $(factory_select).html("<option value=''>--请选择--</option>");
    $.ajax({
        url:getContextPath()+"/commonCon/search",
        type:'POST',
        data:{type:'device_type'},
        dataType:'json',
        success:function(data){

            for(var i=0;i<data.length;i++)
            {
                $("#selectId").append("<option value='"+data[i].value+"'>"+data[i].name+"</option>");
            }
        }
    });

    $("#main_edit #close").click(function(){
        $("#main_list").css({display:"block"});
        $("#main_edit").css({display:"none"});
    });
});
function deviceInit(){
    //创建jqGrid组件
    jQuery("#jqGridList").jqGrid({
        url : getContextPath()+"/modelCon/search",
        datatype:'json',//请求数据返回的类型
        postData:{
            'devicetype':$("#selectId").val(),
            'factory':$("#factory_select").val(),
        },
        colModel:[
             {
                 label:'编号',
                 name:'id',
                 width:0,
                 key:true,
                 hidden:true,
                 editable:true
             },
             {
                 label:'类型',
                 name:'devicetype',
                 index:'devicetype',
                 width:100,
                 align:'center',
            	 formatter:function(cellvalue, options, rowObject){
                   var indexname;
                   switch(cellvalue){
                      case 0:indexname='请选择';break;
                      case 1:indexname='服务器';break;
                      case 2:indexname='交换机';break;
                      case 3:indexname='IB卡';break;
                      case 4:indexname='IP卡';break;
                      case 5:indexname='SSD';break;
                      case 6:indexname='磁盘';break;
                      case 7:indexname='RAID卡';break;
                      case 10:indexname='负载均衡器';break;
                      }
                   return indexname;
                 }
             },
             {
                 label:'厂商',
                 name:'factory',
                 index:'factory',
                 width:100,
                 align:'center',
                 formatter:function(cellvalue, options, rowObject){
                        var indexname="";
                        for (key in type_arr){
                            if(cellvalue==key){
                                indexname=type_arr[key];
                                break;
                            }
                        }
                        return indexname;
                 }
             },
             {
                 label:'型号',
                 name:'model',
                 index:'model',
                 width:100,
                 align:'center'
             },
             {
                 label:'端口数',
                 name:'portnum',
                 index:'portnum',
                 width:100,
                 align:'center'
             },
             {
                 label:'容量',
                 name:'capacity',
                 index:'capacity',
                 width:100,
                 align:'center'
             },
             {label:'操作',name:'act',index:'act',index:'operate',width:100,align:'center'}
        ],
        //loadonce:true,
        rownumbers: true,
        rowNum:100,
        rowList:[100,200,300],
        altRows:true,
        altclass:'jqgridRowColor',
        mtype:'post',//向后台请求数据的Ajax类型
        viewrecords:true,//显示总记录数据
        caption:"<div style='color:#000;width:95%;'>设备型号管理<span onclick='doInsert()' style='float:right;cursor:pointer'><i id='add' class='icon-plus-sign'></i>&nbsp;新增</span></div>",
        height:'auto',
        autowidth:true,
        editurl:getContextPath()+"/modelCon/update",
        pager:"#jqGridPager",
        gridComplete: function(){
            var ids = $("#jqGridList").getDataIDs();
            for(var i=0;i<ids.length;i++){
                var cl = ids[i];
                ed = "<a href=# title='编辑' style='text-decoration: none;' onclick='doInsert("+ids[i]+");'>编辑 </a>";
                vi = "<a href=# title='删除' style='text-decoration: none;' onclick='doDelete("+ids[i]+");'>删除 </a>";
                jQuery("#jqGridList").jqGrid('setRowData',ids[i],{act:ed+"  "+vi});
            }
        },
    });
    $("#gview_jqGridList table:eq(0) thead ")
}
function afterShowForm(){
    $("#FrmGrid_jqGridList #TblGrid_jqGridList #devicetype").change(function(){
        getfactory($(this));
    });
    var type=$("#FrmGrid_jqGridList #TblGrid_jqGridList #factory").val();
    getfactory($("#FrmGrid_jqGridList #TblGrid_jqGridList #devicetype"),$("#FrmGrid_jqGridList #TblGrid_jqGridList #factory"));
    $("#FrmGrid_jqGridList #TblGrid_jqGridList #factory").val(type);
}
//窗口改变大小时重新设置Grid的宽度
$(window).resize(function(){
    var width = document.body.clientWidth;
    var GridWidth = (width-250);
    $("#jqGridList").setGridWidth(GridWidth);
	var height = document.body.clientHeight;
	$('#tt')[0].style.height = height - 50  + "px";
});
//删除
function doDelete(ids){
	var jwt = getJWT();
	layer.confirm("确定删除这条数据？", {
		btn:['确定','取消']
	},function(){
		$.ajax({
	  		url : getContextPath()+"/modelCon/update?oper=del",
	  		type : "post",
	  		dataType : 'json',
	  		async: false ,
	  		data : {
	  			"id" : ids,
	  			"jwt" : jwt
	  		},
	  		success : function(result) {
	  			layer.msg('删除成功！');
				window.parent.doSearch();
	  		},
	  		error : function() {
	  			layer.msg('删除失败！');
	  		}
	  	});	
	},function(){
		layer.closeAll();
	});
	
}
//新增
function doInsert(ids){
	getJWT();
    layer.open({
        type: 2,
        title: '设备型号',
        fix: false,
        shadeClose: false,
        moveOut: true,
        area: ['550px','350px'],
//        area: ['40%', '55%'],
        content: [getContextPath()+"/jsp/pages/configmgt/modelmgt/modeladd.jsp?id="+ids,'no']
    });
}
function getfactory(obj){
    var factory_type=$(obj).val();
    if(factory_type=='1'){
        factory_type='server_factory';
    }
    if(factory_type=='2'){
        factory_type='switchboard_factory';
    }
    if(factory_type=='3'){
        factory_type='IBcard_factory';
    }
    if(factory_type=='4'){
        factory_type='IPcard_factory';
    }
    if(factory_type=='5'){
        factory_type='SSD_factory';
    }
    if(factory_type=='6'){
        factory_type='disk_factory';
    }
    if(factory_type=='7'){
        factory_type='RAIDcard_factory';
    }
    $("#FrmGrid_jqGridList #TblGrid_jqGridList #factory").html("<option value=''>--请选择--</option>");
    $._PubSelect("#FrmGrid_jqGridList #TblGrid_jqGridList #factory",getContextPath()+"/commonCon/customSearch?type="+factory_type,"[{'getKey':'value','getVal':'name'}]");
    $._PubSelect("#factory_select",getContextPath()+"/commonCon/customSearch?type="+factory_type,"[{'getKey':'value','getVal':'name'}]");
}
//重新加载页面
function doSearch(){
    $.jgrid.gridUnload('#jqGridList');
    deviceInit();
}

/*** 查看 ***/
function doView(id){
    alert(id);
}
/*** 重置 ***/
function doClear(ids){
    $(".prom input").val('');
}
/*** 删除 ***/
function doDel(ids){
    $.jgrid.gridUnload('#jqGridList');
    doSearch();
}
