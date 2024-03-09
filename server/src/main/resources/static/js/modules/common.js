/**
 * common demo
 */
/**
 * 按钮提示信息框
 * @param t
 * @param msg
 */
window.openTips = function (t, msg) {
    layer.tips(msg, t, {
        tips: [3, '#0FA6D8']
    });
}
layui.define(function(exports){
    var $ = layui.$
        ,layer = layui.layer
        ,laytpl = layui.laytpl
        ,setter = layui.setter
        ,view = layui.view
        ,admin = layui.admin

    //公共业务的逻辑处理可以写在此处，切换任何页面都会执行
    //……

    //对外暴露的接口
    exports('common', {});
});