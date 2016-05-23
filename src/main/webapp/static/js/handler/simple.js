/**
 * Created by hebowei on 16/5/21.
 */
$(document).ready(readyFunction);

function readyFunction() {
    $("#btn_save").click(saveClick);
    if(document.getElementById("btn_submit") != null){
        $("#btn_submit").click(submitClick);
    }
    if(document.getElementById("btn_upload") != null){
        $("#btn_file").on("change", function () {
            $("#label_upload_state").text("")
                .removeClass("text-success text-danger");
            if($(this).val().length > 0){
                $("#btn_upload").removeAttr("disabled");
            } else {
                $("#btn_upload").attr("disabled", "disabled");
            }
        });
        $("#btn_upload").click(uploadClick);
    }
}

function submitClick() {
    saveClick();
    $.ajax({
        type: "post",
        url: homeUrl + "task/submit",
        success: function (data) {
            switch(data.state){
                case 200:
                    location.href = homeUrl + "task/done";
                    break;
                case 405:
                    alert("Task not finished.");
                    break;
                case 401:
                    alert("登录超时，请刷新页面重试.");
                    break;
            }
        },
        error: function (err) {
            alert("服务器连接中断，请检查网络连接");
        }
    });
}


function saveClick() {
    var data = {};
    //TODO: validity check
    var inputs = $(".form-ud-input");
    var i, errCount=0;
    for(i=0; i<inputs.length; i++){
        if($(inputs[i]).attr("required") && !$(inputs[i]).val()){
            $(inputs[i]).parent().addClass("has-error");
            errCount++;
        } else {
            $(inputs[i]).parent().removeClass("has-error");
            data[inputs[i].name] = $(inputs[i]).val();
        }
    }

    if(document.getElementById("btn_upload") != null && $("#btn_upload").attr("required")){
        //TODO: Check file upload state
    }

    if(errCount == 0){
        //Ajax upload
        $.ajax({
            type: "POST",
            contentType : "application/json; charset=utf-8",
            url: homeUrl + "task/do",
            data: JSON.stringify(data),
            dataType: 'json',
            success: function (data) {
                $("#btn_save").removeClass("btn-default");
                switch (data.state){
                    case 200:
                        $("#btn_save").addClass("btn-success");
                        break;
                    case 401: case 402:
                        alert("无法获得用户校验结果. 无法保存当前结果,请重试.");
                        $("#btn_save").addClass("btn-danger");
                        break;
                }
            },
            error: function (err) {
                console.log(err);
                alert("无法与服务器取得联络，请稍后重试.");
            }
        });
    }
}

function uploadClick() {
    var formData = new FormData(document.getElementById("fileinfo"));
    $.ajax({
        url: homeUrl + "file/upload",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            switch (data.state){
                case 200:
                    $("#label_upload_state").text("上传成功")
                        .addClass("text-success");
                    $("#btn_upload").attr("disabled", "disabled");
                    break;
                case 401:
                    $("#label_upload_state").text("上传失败:页面等待超时")
                        .addClass("text-danger");
                    break;
                case 402:
                    $("#label_upload_state").text("上传失败:文件大小或格式异常")
                        .addClass("text-danger");
                    break;
            }
        },
        error: function (err) {
            console.log(err);
            alert("服务器状态异常.");
        }
    });
}