/**
 * 表单验证支持input select
 *
 * 使用方法
 * 1.引入jQuery.js, layer.js
 * 2.引入validate.js
 * 3.需要验证的input框加入 validate="nn callback" callback="test()"等
 * 4.如果动态生成的元素添加验证需要在新增完成后调用 Validatas.reLoad();重新加载校验
 * 5.在提交方法中调用validateHandler
 * 如: if(!validateHandler()){
 		  //input验证不过！！！！
 		  return;
 	   }else{
 	      //校验完成,继续提交的方法
 	      ...
 	   }
 *
 * input框校验,举个栗子：
 email<input validate="email" type="text"/>
 nspcl<input validate="nspclcn nn" minlength="3" maxlength="10" type="text" direction="t"/>
 nn_num<input validate=" num nn " type="text" />
 cback<input validate="callback nn" callback="vh()" type="text" />
 <select validate="nn callback nspcl" callback="vh()">
 <option value="0">请选择...</option>
 <option value="1">aaa</option>
 <option value="2">bbb</option>
 </select>
 <script>
 function vh(){
		        if(xxxx == xxx){
		            return "输入框不能是xxxx"; //是否是符合校验
		        }
		    }
 </script>
 *
 * 目前支持的方式：最多支持3种 minlength + maxlength + validate, 如：validate="nspclcn nn" minlength="3" maxlength="10"
 * 其中validatenn最多支持2种  nn(必填) + 任意验证，如:  validate="nn callback"
 * 支持某个提示单独方向给定方向"t","b","l","r", 如： direction="t"
 "nn", // 非空
 "email", // 邮箱
 "phone", //手机
 "num", //纯数字类型
 "char", //纯字符
 "nspcl", //非特殊字符
 "nspclcn", //非特殊字符和中文
 "mm", //密码
 "ip", // ip
 "sn", //SN
 "callback" //自定义方法
 *
 */
// 错误提示框颜色
var errorTipsColor = "red";
// * 必填项颜色
var validate_flagColor = "red";
// 实例化的对象
var validateInfo;
/**
 * 初始化方法
 * 1.添加 *
 * 2.添加纯数字
 */
$(function () {
    // 实例化Validatas 反复创建对象会导致内存泄漏？
    validateInfo = Validatas.create();
    // 先删除所有*
    $("span._validate_flag").remove();
    // 添加 * 非空
    $("[validate~=" + validateInfo.VALIDATE_TYPES._notnull + "]").each(function (a, b) {
        $(this).after("<span class='_validate_flag' style='color: " + validate_flagColor + "'>*</span>");
    });
    // 纯数字,此处只支持html5版
    $("input[validate~=" + validateInfo.VALIDATE_TYPES._num + "]").each(function (a, b) {
        $(this).attr({"type": "number"});
    });
    // 密码框
    $("input[validate~=" + validateInfo.VALIDATE_TYPES._pwd + "]").each(function (a, b) {
        $(this).attr({"type": "password"});
    });
});

/**
 * 验证需要调用的方法
 * _direction //提示方向  'R','L','T','B'
 * _height // 提示框高度 '15px'
 * _deviation // 提示框距离 支持:false(默认)//不启用,true//启用默认偏移,"55px"//自定义距离
 * _showTriangle // 是否显示提示三角 支持:true(默认)//启用,false//不启用
 * @returns 返回是否验证通过
 */
function validateHandler(_direction, _height, _deviation, _showTriangle) {
    if(_direction != "" && _direction != null && typeof(_direction) != "undefined"){
        if (_direction.toLowerCase() == "r") {
            validateInfo.direction = 2;
        } else if (_direction.toLowerCase() == "l") {
            validateInfo.direction = 4;
        } else if (_direction.toLowerCase() == "t") {
            validateInfo.direction = 1;
        } else if (_direction.toLowerCase() == "b") {
            validateInfo.direction = 3;
        }
    }
    if (_height && _height.indexOf("px") != -1) {
        validateInfo.tipHeight = _height;
    }else {
        validateInfo.tipHeight = "10px";
    }
    // 转换bool到string
    _deviation += "";
    if(_deviation != "" && _deviation != "null" && _deviation != "undefined"){
        if(_deviation.indexOf("px") != -1){
            validateInfo.deviation = _deviation;
        }else{
            validateInfo.deviation = _deviation == "true" ? "true" : "false";
        }
    }
    // 转换bool到string
    _showTriangle += "";
    if(_showTriangle != "" && _showTriangle != "null" && _showTriangle != "undefined"){
        validateInfo.showTriangle = _showTriangle == "true" ? "true" : "false";
    }

// 每次初始化checkBcak 和 _okCount
// validateInfo.checkBcak = false;
    validateInfo._okCount = 0;
    // 关闭所有标签
    layer.closeAll('tips');
    var allTypes = 0;
    // 最小长度校验
    $("[minlength]").each(function (a, b) {
        validateInfo.vaildateMinlengthFn($(this), validateInfo);
    });
    // 其他格式校验
    $("[validate]").each(function (a, b) {
        var errorMsg = "";
        if ($(this).attr("validate") != "") {
            var v_type = $(this).attr("validate").trim();
            var types = [];
            if (v_type.indexOf(" ") != -1 || v_type.indexOf(",") != -1) {
                if (v_type.indexOf(validateInfo.VALIDATE_TYPES._notnull) != -1) {
                    v_type = v_type.replace(validateInfo.VALIDATE_TYPES._notnull, "").trim();
                    types[0] = validateInfo.VALIDATE_TYPES._notnull;
                    types[1] = v_type.split(" ")[0];
                    allTypes += 2;
                } else {
                    var _types = v_type.split(" ");
                    if(_types.length >= 2){
                        if(_types[0] != "" ){
                            allTypes++;
                        }
                        if(_types[1] != ""){
                            allTypes++;
                        }
                        types.push(_types[0],_types[1]);
                    }else{
                        types = _types[0];
                        if(types != ""){
                            allTypes++;
                        }
                    }
                }
            } else {
                types[0] = v_type;
                allTypes++;
            }
            for (var i = 0; i < types.length; i++) {
                validateInfo._okCount++;
                var type = types[i];
                // 非空验证
                if (type == validateInfo.VALIDATE_TYPES._notnull) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        // 输入框非空
                        errorMsg = validateInfo.notnullFn($(this), validateInfo);
                    }
                }
                // 非特殊字符验证
                if (type == validateInfo.VALIDATE_TYPES._nspcl) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        errorMsg = validateInfo.vaildateSpecialFn($(this), validateInfo);
                    }
                }
                // 非特殊字符和中文验证
                if (type == validateInfo.VALIDATE_TYPES._nspclcn) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        if (validateInfo.vaildateSpecialCnFn($(this), validateInfo) == "") {
                            errorMsg = validateInfo.vaildateSpecialFn($(this), validateInfo);
                        } else {
                            errorMsg = "err_10";
                        }
                    }
                }
                // 纯字符验证
                if (type == validateInfo.VALIDATE_TYPES._char) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        errorMsg = validateInfo.vaildatCharFn($(this), validateInfo);
                    }
                }
                // email验证
                if (type == validateInfo.VALIDATE_TYPES._email) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        errorMsg = validateInfo.vaildatEmailFn($(this), validateInfo);
                    }
                }
                // 手机验证
                if (type == validateInfo.VALIDATE_TYPES._phone) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        errorMsg = validateInfo.vaildatePhoneFn($(this), validateInfo);
                    }
                }
                // 密码验证
                if (type == validateInfo.VALIDATE_TYPES._pwd) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        errorMsg = validateInfo.vaildatePWDFn($(this), validateInfo);
                    }
                }
                // IP验证
                if (type == validateInfo.VALIDATE_TYPES._ip) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        errorMsg = validateInfo.vaildateIPFn($(this), validateInfo);
                    }
                }
                // SN验证
                if (type == validateInfo.VALIDATE_TYPES._sn) {
                    //  没有其他错误时
                    if (errorMsg == "") {
                        errorMsg = validateInfo.vaildateSNFn($(this), validateInfo);
                    }
                }
                // 自定义方法 !!!
                if (type == validateInfo.VALIDATE_TYPES._self) {
                    var _back = $(this).attr("callback")
                    if (_back != "" && _back != null) {
                        // 执行callback,且有返回值
                        var _backReturn = eval(_back);
                        if ($(this).attr("validate").indexOf(validateInfo.VALIDATE_TYPES._notnull) != -1) {
                            // 输入框非空
                            errorMsg = validateInfo.notnullFn($(this), validateInfo);
                        }
                        if (errorMsg == "") {
                            if (_backReturn != null && typeof(_backReturn) != "undefined" && _backReturn != "") {
                                validateInfo._okCount--;
                                layer.tips(_backReturn, this, {
                                    tips: [validateInfo.direction, errorTipsColor],
                                    tipsMore: true,
                                    success: function (layero, index) {
                                        if (validateInfo.tipHeight != 0) {
                                            $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    });

    if(validateInfo.showTriangle == "false"){
        $(".layui-layer-tips i.layui-layer-TipsL, .layui-layer-tips i.layui-layer-TipsR").css({'display': 'none'});
    }
    if(validateInfo.deviation.indexOf("px") != -1){
        if(validateInfo.direction == 2){
            $(".layui-layer-tips .layui-layer-content").css({'left' : '-'+validateInfo.deviation});
        }else if(validateInfo.direction == 4){
            $(".layui-layer-tips .layui-layer-content").css({'right' : '-'+validateInfo.deviation});
        }
    }else if(validateInfo.deviation == "true"){
        if(validateInfo.direction == 2){
            $(".layui-layer-tips .layui-layer-content").css({'left' : 'calc(-100% - 20px)'});
        }else if(validateInfo.direction == 4){
            $(".layui-layer-tips .layui-layer-content").css({'right' : 'calc(-100% - 20px)'});
        }
    }

//    console.log("总共需要验证过个数" + allTypes + "，验证过个数" + validateInfo._okCount);
    if (validateInfo._okCount == allTypes) {
        return true;
    } else {
        return false;
    }
}

/**
 * 定义Validates类
 */
var Validatas = {
    // 构造方法
    create: function () {
        return {
            _okCount: 0, // 验证过的次数
            direction: 2, // 提示方向  'R','L','T','B'
            tipHeight: 0,
            deviation: "false", // 提示框距离
            showTriangle: "true", // 是否显示提示的三角形
            checkBcak: false, // 每次构建函数后重置，返回验证是否成功
            VALIDATE_TYPES: {
                _notnull: "nn", // 非空
                _email: "email", // 邮箱
                _phone: "phone", //手机
                _num: "num", //纯数字类型
                _char: "char", //纯字符
                _nspcl: "nspcl", //非特殊字符
                _nspclcn: "nspclcn", //非特殊字符和中文字符
//                _mm: "mm", //密码
                _ip: "ip", // ip
                _sn: "sn", // SN
                _self: "callback" //自定义方法
            },
            REGEXP: { // 验证的正则表达式
                _email: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                _phone: /^1[3|4|5|7|8][0-9]{9}$/,
                _ip: /^(?:(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:1[0-9][0-9]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:2[0-5][0-5])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$/,
                _char: /^[a-zA-Z]+$/,
//                _pwd: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$/, //密码 需包含字母和数字8-20位
                _special_en: /[`@~!#$%^&*()+<>?:"{},.\/;'[\]]/im, //英文特殊字符
                _special_cn: /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im,  //中文特殊字符
                _chart_cn: /[\u4E00-\u9FA5\uF900-\uFA2D]/, //中文字符
                _sn: /^\d{5}-\d{5}-\d{5}-\d{5}$/, //sn校验
                _num: /^[0-9]*$/
            },
            ERRORMSG: {
                _err_0: "不能为空",
                _err_1: "不能含有特殊字符",
                _err_2: "不能含有特殊字符",
                _err_3: "email不合法",
                _err_4: "手机不合法",
                _err_5: "仅限英文字符",
                _err_6: "请选择下拉列表",
//                _err_7: "密码包含8-20位字母数字组合",
                _err_8: "格式:255.255.255.255",
                _err_9: "格式:12345-12345-12345-12345",
                _err_10: "不能含有中文",
                _err_11: "内容太短"
            },
            /**
             * 非空验证
             * @param jq_ui jquery对象
             * @returns {*}
             */
            notnullFn: function (jq_ui, validateInfo) {
                if (jq_ui.is("input")) {
                    if (jq_ui.val().trim() == "" || jq_ui.val() == null) {
                        var _tmpDirection = validateInfo.direction;
                        if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                            if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                                _tmpDirection = 2;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                                _tmpDirection = 4;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                                _tmpDirection = 1;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                                _tmpDirection = 3;
                            }
                        }
                        layer.tips(validateInfo.ERRORMSG._err_0, jq_ui, {
                            tips: [_tmpDirection, errorTipsColor],
                            tipsMore: true,
                            success: function (layero, index) {
                                if (validateInfo.tipHeight != 0) {
                                    $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                                }
                            }
                        });
                        validateInfo._okCount--;
                        return "err_0";
                    } else {
                        // validateInfo.checkBcak = true;
                        return "";
                    }
                } else if (jq_ui.is("select")) {
                    if (jq_ui.find("option:selected").text().indexOf("选择") != -1
                        || jq_ui.find("option:selected").text().trim() == ""
                        || jq_ui.find("option:selected").text() == null) {
                        var _tmpDirection = validateInfo.direction;
                        if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                            if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                                _tmpDirection = 2;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                                _tmpDirection = 4;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                                _tmpDirection = 1;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                                _tmpDirection = 3;
                            }
                        }
                        layer.tips(validateInfo.ERRORMSG._err_6, jq_ui, {
                            tips: [_tmpDirection, errorTipsColor],
                            tipsMore: true,
                            success: function (layero, index) {
                                if (validateInfo.tipHeight != 0) {
                                    $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                                }
                            }
                        });
                        validateInfo._okCount--;
                        return "err_6";
                    } else {
                        // validateInfo.checkBcak = true;
                        return "";
                    }
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * 特殊字符验证
             * @param jq_ui
             * @returns {*}
             */
            vaildateSpecialFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && validateInfo.REGEXP._special_en.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_1, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_1";
                } else if (jq_ui.val() != "" && validateInfo.REGEXP._special_cn.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_2, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_2";
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * 特殊字符和中文验证
             * @param jq_ui
             * @returns {*}
             */
            vaildateSpecialCnFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && validateInfo.REGEXP._chart_cn.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_10, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_10";
                } else {
                    return "";
                }
            },
            /**
             * 纯字符验证
             * @param jq_ui
             * @returns {*}
             */
            vaildatCharFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && !validateInfo.REGEXP._char.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_5, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_5";
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * 邮箱验证
             * @param jq_ui
             * @returns {*}
             */
            vaildatEmailFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && !validateInfo.REGEXP._email.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_3, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_3";
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * IP验证
             * @param jq_ui
             * @returns {*}
             */
            vaildateIPFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && !validateInfo.REGEXP._ip.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_8, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_8";
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * SN验证
             * @param jq_ui
             * @returns {*}
             */
            vaildateSNFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && !validateInfo.REGEXP._sn.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_9, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_9";
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * 手机验证
             * @param jq_ui
             * @returns {*}
             */
            vaildatePhoneFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && !validateInfo.REGEXP._phone.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_4, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_4";
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * 密码验证
             * @param jq_ui
             * @returns {*}
             */
            vaildatePWDFn: function (jq_ui, validateInfo) {
                if (jq_ui.val() != "" && !validateInfo.REGEXP._pwd.test(jq_ui.val())) {
                    var _tmpDirection = validateInfo.direction;
                    if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                        if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                            _tmpDirection = 2;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                            _tmpDirection = 4;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                            _tmpDirection = 1;
                        }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                            _tmpDirection = 3;
                        }
                    }
                    layer.tips(validateInfo.ERRORMSG._err_7, jq_ui, {
                        tips: [_tmpDirection, errorTipsColor],
                        tipsMore: true,
                        success: function (layero, index) {
                            if (validateInfo.tipHeight != 0) {
                                $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                            }
                        }
                    });
                    validateInfo._okCount--;
                    return "err_7";
                } else {
                    // validateInfo.checkBcak = true;
                    return "";
                }
            },
            /**
             * 输入最小长度验证
             * @param jq_ui
             * @returns {*}
             */
            vaildateMinlengthFn: function (jq_ui, validateInfo) {
                var minNum = jq_ui.attr("minlength").trim();
                if (jq_ui.val() != "" && validateInfo.REGEXP._num.test(minNum)) {
                    if (jq_ui.val().length < parseInt(minNum)) {
                        var _tmpDirection = validateInfo.direction;
                        if(jq_ui.attr("direction") != "" && jq_ui.attr("direction") != null && typeof(jq_ui.attr("direction")) != "undefined"){
                            if(jq_ui.attr("direction").trim().toLowerCase() == "r"){
                                _tmpDirection = 2;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "l"){
                                _tmpDirection = 4;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "t"){
                                _tmpDirection = 1;
                            }else if(jq_ui.attr("direction").trim().toLowerCase()  == "b"){
                                _tmpDirection = 3;
                            }
                        }
                        layer.tips(validateInfo.ERRORMSG._err_11, jq_ui, {
                            tips: [_tmpDirection, errorTipsColor],
                            tipsMore: true,
                            success: function (layero, index) {
                                if (validateInfo.tipHeight != 0) {
                                    $(".layui-layer-tips .layui-layer-content").css("line-height", validateInfo.tipHeight);
                                }
                            }
                        });
                        validateInfo._okCount--;
                        return "err_11";
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            }
        };
    },
    // 动态生成的元素配置验证需要调用重置方法
    reLoad : function () {
        // 实例化Validatas 反复创建对象会导致内存泄漏？
        validateInfo = Validatas.create();
        // 先删除所有*
        $("span._validate_flag").remove();
        // 添加 * 非空
        $("[validate~=" + validateInfo.VALIDATE_TYPES._notnull + "]").each(function (a, b) {
            $(this).after("<span class='_validate_flag' style='color: " + validate_flagColor + "'>*</span>");
        });
        // 纯数字,此处只支持html5版
        $("input[validate~=" + validateInfo.VALIDATE_TYPES._num + "]").each(function (a, b) {
            $(this).attr({"type": "number"});
        });
        // 密码框
        $("input[validate~=" + validateInfo.VALIDATE_TYPES._pwd + "]").each(function (a, b) {
            $(this).attr({"type": "password"});
        });
    }

};