/**
 * Created by hebowei on 16/5/21.
 */
$(document).ready(readyFunction);

function readyFunction() {
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
    var data = {};
    
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