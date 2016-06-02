/**
 * Created by inlab-dell on 2016/5/19.
 */
$(document).ready(function () {
    $("#btn-join").click(dispatchJoinBtnClick);
    $("#btn-mt-chk-status").click(checkMtIdValidity);
    $("#btn-mt-join").click(dispatchMturkJoinBtnClick);
    $("#btn-mt-resume").click(function(){
        window.location.href = homeUrl + "task/do/" + $("#val-tid").val();
    });
    $("#img-captcha").click(function () {
        refreshCaptcha();
    });
});

function checkMtIdValidity() {
    var mturkId = $("#inputMTurkId").val();
    var captcha = $("#inputCaptcha").val();
    $("#alert-info").hide();
    if(mturkId && captcha){
        var data = {
            mturkId: mturkId,
            captcha: captcha,
            taskId: $("#val-tid").val()
        };
        $.post({
            url:  homeUrl + "task/check_mt",
            data: data,
            success: function (rcvData) {
                if(rcvData.state == 200){
                    $("#group-mt-id").addClass("has-success");
                    $("#btn-mt-chk-status").hide();
                    $("#btn-mt-join").show();
                } else if(rcvData.state == 201){
                    $("#group-mt-id").addClass("has-success");
                    $("#btn-mt-chk-status").hide();
                    $("#btn-mt-resume").show();
                } else {
                    refreshCaptcha();
                    if(rcvData.state!=401)
                        $("input[type='text']").val("");
                    else
                        $("#inputCaptcha").val("");
                    $("#alert-info").text(rcvData.message).show();
                }
            },
            error: function (error) {
                console.warn(error);
            }
        });
    } else {
        $("#alert-info").text("Please fill in all the blanks and retry.").show();
    }
}

function dispatchMturkJoinBtnClick() {
    $("#btn-mt-join").prop("disabled", true);
    var tid = $("#val-tid").val();
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: homeUrl+"task/claim",
        data: '{"taskId":' + tid + '}',
        error: function (error) {
            console.warn(error);
            alert("An error occurred, please refresh this page." + error);
        },
        success: function (data) {
            switch(data.state){
                case 200:
                    console.log("succ");
                case 300:
                    console.log("redirecting...");
                    window.location.href = homeUrl + "task/do/" + tid;
                    break;
                case 400:
                    console.error("任务状态异常");
                    alert("An error occurred in processing task state, please refresh the page and retry.");
                    break;
                case 500:
                    alert("Your MTurk ID is not valid, please try again!");
                    break;
            }
            $("#btn-mt-join").prop("disabled", true);
        }
    });

}

function dispatchJoinBtnClick() {
    var state = $(this).data("state");
    var tid = $("#val-tid").val();
    if(state == "claimed"){
        //Redirect to current progress
        window.location.href = homeUrl + "task/do/" + tid;
    } else if(state == "joinable"){
        //Claim new usertask
        $("#btn-join").attr("disabled", "disabled");
        $.ajax({
            type: "POST",
            contentType : "application/json; charset=utf-8",
            url: homeUrl+"task/claim",
            data: '{"taskId":' + tid + '}',
            success: function (data) {
                switch(data.state){
                    case 200:
                        console.log("succ");
                    case 300:
                        console.log("redirecting...");
                        window.location.href = homeUrl + "task/do/" + tid;
                        break;
                    case 400:
                        console.error("任务状态异常");
                        alert("任务状态异常，请重试");
                        break;
                    case 500:
                        alert("用户登录失败，请重新登录！");
                        window.location.href = homeUrl + "user/login?next="
                                    + window.location.pathname.split(";")[0];
                        break;
                }
                $("#btn-join").removeAttr("disabled");
            },
            error: function (error) {
                alert("网络异常，请重试一下~");
                console.log(error);
            }
        });
    }
}

function refreshCaptcha() {
    $("#img-captcha").attr("src", homeUrl+"captcha?"+Math.floor(Math.random()*100));
}