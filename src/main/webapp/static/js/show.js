/**
 * Created by inlab-dell on 2016/5/19.
 */
$(document).ready(function () {
    $("#btn-join").click(dispatchJoinBtnClick);
});

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
            error: function () {
                alert("网络异常，请重试一下~");
            }
        });
    }
}