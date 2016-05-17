/**
 * Created by inlab-dell on 2016/5/6.
 */
$(document).ready(function () {
    $("#inputSubmit").attr("disabled", "disabled");
    $('#inputAgreeCheck').click(function () {
       if(this.checked){
           $("#inputSubmit").removeAttr("disabled");
       } else {
           $("#inputSubmit").attr("disabled", "disabled");
       }
    });
    $("#registerForm").submit(function (event) {
        event.preventDefault();
        postRegForm();
    });
    $("#img-captcha").click(function () {
        $(this).attr("src", homeUrl+"captcha?"+Math.floor(Math.random()*100));
    });
});

function refreshCaptcha() {
    $("#img-captcha").attr("src", homeUrl+"captcha?"+Math.floor(Math.random()*100));
}

function postRegForm() {
    var data = {};
    data["email"] = $("#inputEmail").val();
    data["password"] = md5( $("#inputPassword").val() );
    data["captcha"] = $("#inputCaptcha").val();
    var succFunc = function (data) {
        console.log("SUCCESS:", data);
        //var resultObj = JSON.parse(data);
        var stateCode = data.state;
        switch (stateCode){
            //TODO:增加文字提示
            case 200:
                alert("注册成功！即将跳转至登录界面...");
                location.href = homeUrl + "user/login";
                break;
            case 400:
                $(".info-input").addClass("has-error");
                refreshCaptcha();
                break;
            case 403:
                $("#captcha-group").addClass("has-error");
                refreshCaptcha();
                break;
            case 401:
                $("#email-group").addClass("has-error");
                refreshCaptcha();
                break;
            default:
                alert("what happened?!");
        }
    };
    var errorFunc = function (e) {
        console.log("ERROR:", e);
    };

    $.ajax({
        type: "POST",
        contentType : "application/json; charset=utf-8",
        url: homeUrl + "user/register",
        data: JSON.stringify(data),
        dataType: 'json',
        success: succFunc,
        error: errorFunc
    });
}