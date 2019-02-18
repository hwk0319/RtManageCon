/**
 * 加密类
 * 使用方法
 (1)
 var mm = $.toRsaMd5AllEncrypt(mm,modulus);
 */
(function ($) {
    // 获取加密模
    $.extend({
        // 只转成rsa
        toRsaEncrypt:function (mm,key) {
        	var encrypt = new JSEncrypt();
            // js设置公钥
            encrypt.setPublicKey(key);
            // 生成密文
            var encryptCode = encrypt.encrypt(mm);
            return encryptCode;
        },
        // 转成rsa(md5(mm)+mm)
        toRsaMd5AllEncrypt:function (mm,key) {
            var _mm = hex_md5(mm)+mm;
            var encrypt = new JSEncrypt();
            // js设置公钥
            encrypt.setPublicKey(key);
            // 生成密文
            var encryptCode = encrypt.encrypt(_mm);
            return encryptCode;
        },
        // 转成rsa(md5(mm))
        toRsaMd5Encrypt:function (mm,key) {
            var _mm = hex_md5(mm);
            var encrypt = new JSEncrypt();
            // js设置公钥
            encrypt.setPublicKey(key);
            // 生成密文
            var encryptCode = encrypt.encrypt(_mm);
            return encryptCode;
        },
        // 只转成md5
        toMd5Encrypt:function (mm) {
            var _mm = hex_md5(mm);
            return _mm;
        }
    });
})(jQuery);

// 加载  md5-min.js 和 security.js
$(function () {
	var curPath=window.document.location.href;  
    var pathName=window.document.location.pathname;  
    var pos=curPath.indexOf(pathName);  
    var localhostPaht=curPath.substring(0,pos);  
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
    var _url= localhostPaht+projectName;
	// 自动加载密码相关js
    $("<script>").attr({"type":"text/javascript","src":_url+"/jsLib/md5-min.js"}).appendTo(document.head);
    $("<script>").attr({"type":"text/javascript","src":_url+"/jsLib/jsencrypt.js"}).appendTo(document.head);
});