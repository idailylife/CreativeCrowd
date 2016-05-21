/**
 * Created by hebowei on 16/5/22.
 */
//根据handler类型动态加载对应的css及js文件
$(document).ready(function () {
    $.getScript(homeUrl + "/static/js/handler/" + handlerType + ".js", function () {
       console.log("handler js loaded.");
    });
    $("<link/>", {
        rel: "stylesheet",
        type: "text/css",
        href: homeUrl + "static/css/handler/" + handlerType + ".css"
    }).appendTo("head");
});