(function(){
	$.fn.extend({
		/**
		 * 文本框检索（可能还有优化之处...）
		 * eg:  $("#iframe").contents()._PubQuery(getContextPath()+"/A/search","uid","");
		 *      $("#editmodjqGridList")._PubQuery(getContextPath()+"/A/search","uid","");
		 * @param url  查询地址
		 * @param colName  字段名
		 * @param pubjson  blaLine：是否有空行,默认true[true/false] ； isClick：是否有点击事件,默认true[true/false]  ； isFields：是否填充其他文本框值,默认true[true/false]  isRead：是否不可编辑,不包含当前colName字段,默认true[true/false]  
		 *                 getCol：数据库取值字段 ；     ...待定
		 *       例：[{'blaLine':'true','isClick':'true','isFields':'true','isRead':'true','getCol':'uid'}]
		 * @author：ldj
		 */
		_PubQuery:function(url,colName,pubjson){
			var obj=$(this);
			var rejson="";
			var blaLine=true,isClick=true,isFields=true,isRead=true;
			var _getCol=colName;
			try{rejson=eval(pubjson)[0];}catch(err){rejson="";}
			if(rejson!=="" || typeof(rejson)!="undefined"){
				blaLine=rejson.blaLine;
				isClick=rejson.isClick;
				isFields=rejson.isFields;
				isRead=rejson.isRead;
				_getCol=_rejson.getCol;
				blaLine=blaLine===""||typeof(blaLine)==="undefined"?true:blaLine;
				isClick=isClick===""||typeof(isClick)==="undefined"?true:isClick;
				isFields=isFields===""||typeof(isFields)==="undefined"?true:isFields;
				isRead=isRead===""||typeof(isRead)==="undefined"?true:isRead;
				_getCol=_getCol===""||typeof(_getCol)==="undefined"?colName:_getCol;
			}
			var colNameObj=$(obj).find("#"+colName);
			var colNameWid=$(colNameObj).width()+6;
			$(colNameObj).css({marginBottom:"0"}).attr("autocomplete","off");//去除input输入的缓存
			$(colNameObj).after("<div id=\"_PubQuery\" style=\"width:"+colNameWid+"px;position: absolute;border:1px solid #cccccc;border-top: 0;background: #fff;margin-left:3px;overflow: hidden;z-index:100;display:none;\"><ul style=\"width:"+colNameWid+"px;list-style:none;padding:0;margin:0;overflow: hidden;\"></ul></div>");
			$(colNameObj).keyup(function(e){
				e=e||window.event;
				var colVal=$(this).val();
				if(colVal!==""){
					$.ajax({
						type:"post",
						url:url+"?"+colName+"="+colVal,
						async:false,
						success:function(data){
							$(obj).find("#_PubQuery ul").html("");
							if(typeof(data)!=="undefined" && data!==""){
								$(obj).find("#_PubQuery").css({display:"block"});
								if(blaLine==true){//是否有空行
									$(obj).find("#_PubQuery ul").append("<li read='false' style=\"width:100%;float:left;line-height:22px;cursor:pointer;\">&nbsp;&nbsp;</li>");
								}
								var columns="";
								for(var i=0,alllen=data.length;i<alllen;i++){
									var res=data[i];
									var attr_col="",columns="";
									for(var key in res){
										columns+=key+",";
										var val=res[key];
										val=val===null?"":val;
										attr_col+=key+"='"+val+"' ";
									}
									$(obj).find("#_PubQuery ul").append("<li "+attr_col+" style=\"width:100%;float:left;line-height:22px;cursor:pointer;\">"+data[i][_getCol]+"</li>");
								}
								if(isClick==true){//是否有点击事件
									$(obj).find("#_PubQuery ul li").on("click",function(e2){
										e2=e2||window.event;
										if($(this).attr("read")==="false" && isRead==true){
											$(obj).find("input").removeAttr("readonly").val("");
											$(obj).find("select").removeAttr("readonly").removeAttr("disabled").val("");
											$(obj).find("textarea").removeAttr("readonly").val("");
										}else{
											if(isFields==true){//是否填充其他文本框值
												var ar_col=columns.split(",");
												for(var j=0,colSize=ar_col.length-1;j<colSize;j++){
													var key=ar_col[j];
													$(obj).find("#"+key).val($(this).attr(key));
												}
											}else{
												$(colNameObj).val($(this).html());
											}
											if(isRead==true){//是否不可编辑
												$(obj).find("input:not(#"+colName+")").attr("readonly","readonly");
												$(obj).find("select").attr("readonly","readonly").attr("disabled","disabled");
												$(obj).find("textarea").attr("readonly","readonly");
											}
										}
										$(obj).find("#_PubQuery").css({display:"none"});
										e2.stopPropagation();
									});
							   }
							}else{
								$(obj).find("#_PubQuery").css({display:"none"});
							}
						}
					});
				}else{
					$(obj).find("#_PubQuery").css({display:"none"});
				}
				e.stopPropagation();
			});
			$(obj).click(function(){$(obj).find("#_PubQuery").css({display:"none"});});
		}
	});
	$.extend({
		/**
		 * 下拉取值（目前支持的select）
		 * @param obj  obj对象
		 * @param url  查询地址
		 * @param pubjson  blaLine：是否有空行,默认true[true/false] ； isedHtml：是否拼接填充内容,默认true ；retType：返回的数据格式，默认arr[arr/json/obj]   
		 *                 getKey：数据库取值字段1 ；  getVal：数据库取值字段2     ...待定
		 *       例：[{'blaLine':'true','isedHtml':'true','retType':'arr','getKey':'id','getVal':'name','attr':'type'}]
		 * @author：ldj
		 */
		_PubSelect:function(obj,url,pubjson){
			var _rejson="";
			var _blaLine=true,_isedHtml=true,_retType="arr";
			var _getKey="id",_getVal="name",_attr="";
			try{_rejson=eval(pubjson)[0];}catch(err){_rejson="";}
			if(_rejson!=="" || typeof(_rejson)!="undefined"){
				_blaLine=_rejson.blaLine;
				_retType=_rejson.retType;
				_isedHtml=_rejson.isedHtml;
				_getKey=_rejson.getKey;
				_getVal=_rejson.getVal;
				_attr=_rejson.attr;
				_blaLine=_blaLine===""||typeof(_blaLine)==="undefined"?true:_blaLine;
				_isedHtml=_isedHtml===""||typeof(_isedHtml)==="undefined"?true:_isedHtml;
				_retType=_retType===""||typeof(_retType)==="undefined"?"arr":_retType;
				_getKey=_getKey===""||typeof(_getKey)==="undefined"?"id":_getKey;
				_getVal=_getVal===""||typeof(_getVal)==="undefined"?"name":_getVal;
				_attr=_attr===""||typeof(_attr)==="undefined"?"":_attr;
			}
			var _rHtml="";
			var rData;
			$.ajax({
				url:url,
				type:"post",
				async:false,
				success:function(res){
					var r_arr={};
					var r_json="[";
					if(_blaLine==true || _blaLine==="true"){
						r_arr[" "]="--请选择--";
						_rHtml+="<option value=\"\" selected>--请选择--</option>";
					}
					if(typeof(res)!=="undefined" && res!==""){
						for(var i=0,allsize=res.length;i<allsize;i++){
							try{
								var key="";
								var A=_getKey.split(",");
								for(var j=0,arSize=A.length;j<arSize;j++){
									var a=A[j];
									key+=res[i][a]+",";
								}
								key=key.substring(0,key.length-1);
								var val=res[i][_getVal];
								var attr=_attr+"=";
								try{attr+=res[i][_attr]}catch(e){attr="";}
								r_arr[key]=val;
								r_json+="{'"+key+"':'"+val+"'},";
								_rHtml+="<option "+attr+" value=\""+key+"\">"+val+"</option>";
							}catch(er){}
						 }
						r_json+="]";
						if(_retType==="arr"){
							rData=r_arr;
						}else if(_retType==="json"){
							rData=r_json;
						}else{
							rData=res;
						}
					}
				}
			});
			if(_isedHtml==true || _isedHtml==="true"){
				$(obj).html(_rHtml);
			}
			return rData;
		},_PubisNull:function(val){
			if(val==null){
				val="";
			}else if(typeof(val)==="undefined"){
				val="";
			}
			return val;
		}
	});
	
})(jQuery);
