/**
 * Created by inlab-dell on 2016/5/9.
 */
jQuery(document).ready(function ($) {
    $("#loginForm").submit(function (event) {
        event.preventDefault();
        postLoginForm();
    });
    $("#img-captcha").click(function () {
        refreshCaptcha();
    });
});

function refreshCaptcha() {
    $("#img-captcha").attr("src", homeUrl+"captcha?"+Math.floor(Math.random()*100));
}

function postLoginForm() {
    var data = {};
    data["email"] = $("#inputEmail").val();
    data["password"] = md5( $("#inputPassword").val());
    data["captcha"] = $("#inputCaptcha").val();
    var succFunc = function (data) {
        console.log("SUCCESS:", data);
        var stateCode = data.state;
        $(".info-input").removeClass("has-error");
        $("#captcha-group").removeClass("has-error");
        $("#inputCaptcha").val("");
        switch (stateCode){
            case 200:
                if(nextUrl.search("logout") != -1){
                    nextUrl = homeUrl;
                }
                location.href = nextUrl;
                break;
            case 400: case 401:
                $(".info-input").addClass("has-error");
                refreshCaptcha();
                break;
            case 403:
                $("#captcha-group").addClass("has-error");
                refreshCaptcha();
                break;
            case 500:
                alert("服务器内部错误#1，请重试");
                refreshCaptcha();
                break;
            case 501:
                alert("服务器内部错误#2，请重试");
                refreshCaptcha();
                break;
        }
    };
    var errorFunc = function (e) {
        console.log("ERROR:", e);
    };

    $.ajax({
        type: "POST",
        contentType : "application/json; charset=utf-8",
        url: homeUrl + "user/login",
        data: JSON.stringify(data),
        dataType: 'json',
        success: succFunc,
        error: errorFunc
    });
}