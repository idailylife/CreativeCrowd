/**
 * Created by inlab-dell on 2016/6/8.
 */
$(document).ready(function () {
   $(".form_datetime").datetimepicker({
       closeOnDateSelect:true
   });

    $("#img-captcha").click(function () {
        refreshCaptchaImage();
    });

    var ajaxSubmit = function () {
        $("input[name=startTime]").val(
            getUnixTimestamp($("#inputStartTime").val()));
        $("input[name=endTime]").val(
            getUnixTimestamp($("#inputEndTime").val()));
        var mode = $("li[role=presentation].active > a").data("mode");
        var params;
        //Insert mode params
        if(mode == 1){
            //Random
            params = {
                randSize: $("#inputRandomParam").val(),
                finishedMTaskId: [],
                finishedCount: 0
            };
        } else if(mode == 2){
            //Sequence
            params = [];
        }
        params = JSON.stringify(params);

        var extData = {
            desc: $("#textDesc").val(),
            desc_detail: $("#textDescDetail").val(),
            info: $("#textDescOther").val()
        };
        $("input[name=descJson]").val(
            JSON.stringify(extData)
        );

        var formData = new FormData($("#formBasicInfo")[0]);
        formData.append("mode", mode);
        formData.append("params", params);
        formData.append("taskId", "-1");
        $.ajax({
            url: homeUrl + 'task/create',
            type: 'POST',
            data: formData,
            success: function (data) {
                console.log(data);
                if(data.state == 200){
                    window.location.href = homeUrl + "task/edit/" + data.content;
                } else if(data.state == 400){
                    alert("表单输入不完整，请重试");
                    refreshCaptchaImage();
                } else if(data.state == 402){
                    alert("验证码错误，请重试");
                    $("#formGroupCaptcha").addClass("has-error");
                    refreshCaptchaImage();
                } else if(data.state == 403){
                    alert("出现了奇怪的错误...");
                }
            },
            cache: false,
            contentType: false,
            processData: false
        });
    };
    $("#btnNext").click(function () {
       ajaxSubmit();
    });
});

function getUnixTimestamp(str){
    if(!str){
        return "";
    }
    var dateObj = new Date(str);
    return Math.floor(dateObj.getTime()/1000);
}

function refreshCaptchaImage() {
    $("#img-captcha").attr("src", homeUrl+"captcha?rand=" + Math.random());
    $("#inputCaptcha").val("");
}