(function ($) {
    $.extend({
        "movies":function (ui,array,times) {
            for(var i=1;i<array.length;i++){
                (function (index) {
                    $(ui).animate(array[index],{
                            "easing":"swing", //只支持 linear swing
                            "duration":times[index-1],  //一定是int类型的数字，不能是字符串
                            "complete":function () { //动画完成事件
                                if(index == array.length - 1){
                                    $(ui).css(array[0]);
                                    $.movies(ui,array,times);
                                }
                            }
                        }
                    );
                })(i);
            }
        }
    })
})(jQuery);