/**
 * Created by hebowei on 16/5/21.
 */
$(document).ready(readyFunction);

function readyFunction() {
    $("#btn_save").click(saveClick);
    setSaveButtonState("disabled");
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
    $("input[type!=button]").change(function () {
       setSaveButtonState("normal");
    });
    $("input[type=file]").change(function () {
        if(this.value.length > 0){
            $("#file_upd_state").val(0);
        }
    });
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
                    alert(messageResources.SUBMIT_405);
                    break;
                case 401:
                    alert(messageResources.SUBMIT_401);
                    break;
            }
        },
        error: function (err) {
            alert(messageResources.AJAX_CONN_FAIL + err);
        }
    });
}


function saveClick() {
    var data = {
        tid: $("#tid").val()
    };
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

    if(document.getElementById("btn_upload") != null && $("#btn_file").attr("required")){
        if($("#file_upd_state").val() == 0){
            errCount++;
            alert(messageResources.UPLOAD_FILE_NOT_READY);
        }
    }

    if(errCount == 0){
        //Ajax upload
        setSaveButtonState("saving");
        $.ajax({
            type: "POST",
            contentType : "application/json; charset=utf-8",
            url: homeUrl + "task/do",
            data: JSON.stringify(data),
            dataType: 'json',
            success: function (data) {
                //$("#btn_save").removeClass("btn-default");
                switch (data.state){
                    case 200:
                        //$("#btn_save").addClass("btn-success");
                        setSaveButtonState("saved");
                        break;
                    case 401: case 402:
                        alert(messageResources.SAVE_401_402);
                        //$("#btn_save").addClass("btn-danger");
                        setSaveButtonState("fail");
                        break;
                }
            },
            error: function (err) {
                console.log(err);
                alert(messageResources.AJAX_CONN_FAIL);
            }
        });
    }
    return errCount;
}

function setSaveButtonState(state){
    var btn = $("#btn_save");
    btn.removeClass("btn-warning").addClass("btn-default");
    if(state == "saving"){
        btn.val(messageResources.BTN_STATE_SAVING).prop("disabled", true);
    } else if(state == "normal"){
        btn.val(messageResources.BTN_STATE_SAVE).prop("disabled", false);
    } else if(state == "fail"){
        btn.val(messageResources.BTN_STATE_RETRY).prop("disabled", false)
            .addClass("btn-warning")
            .removeClass("btn-default");
    } else if(state == "disabled"){
        btn.prop("disabled", true);
    } else if(state == "saved"){
        btn.val(messageResources.BTN_STATE_SAVED).prop("disabled", true);
    }
}

function uploadClick() {
    $("#file_upd_state").val(0);
    var formData = new FormData(document.getElementById("fileinfo"));
    formData.append("taskId", $("#tid").val());
    $.ajax({
        url: homeUrl + "file/upload",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            switch (data.state){
                case 200:
                    $("#label_upload_state").text(messageResources.UPLOAD_SUCCEED)
                        .addClass("text-success");
                    $("#btn_upload").attr("disabled", "disabled");
                    $("#file_upd_state").val(1);
                    break;
                case 401:
                    $("#label_upload_state").text(messageResources.UPLOAD_TIMEOUT)
                        .addClass("text-danger");
                    break;
                case 402:
                    $("#label_upload_state").text(messageResources.UPLOAD_FILE_ERROR)
                        .addClass("text-danger");
                    break;
            }
        },
        error: function (err) {
            console.log(err);
            alert(messageResources.AJAX_CONN_FAIL);
        }
    });
}

var messageResources = function () {
    var resource;
    switch(taskType){
        case 0:
            resource = {
                SUBMIT_405: "错误405:任务尚未完成",
                SUBMIT_401: "错误401:登录超时,请刷新页面重试",
                AJAX_CONN_FAIL: "服务器连接中断,请检查网络连接\n",
                UPLOAD_FILE_NOT_READY: "请先上传文件再保存或提交",
                UPLOAD_SUCCEED: "上传成功",
                UPLOAD_TIMEOUT: "上传失败:页面等待超时",
                UPLOAD_FILE_ERROR: "上传失败:文件大小或格式异常",
                SAVE_401_402: "无法获得用户校验结果. 无法保存当前结果,请刷新页面重试.",
                BTN_STATE_SAVING: "正在保存",
                BTN_STATE_SAVE: "保存",
                BTN_STATE_RETRY: "重试",
                BTN_STATE_SAVED: "已保存"
            };
            break;
        case 1:
            resource = {
                SUBMIT_405: "Error 405: Task submitted but not finished.",
                SUBMIT_401: "错误401: Login time out, please click the origin link from MTurk.",
                AJAX_CONN_FAIL: "Cannot connect to server, please try again.\n",
                UPLOAD_FILE_NOT_READY: "Please `Upload` your file before save or submit.",
                UPLOAD_SUCCEED: "File uploaded successfully.",
                UPLOAD_TIMEOUT: "Fail to upload, request time out.",
                UPLOAD_FILE_ERROR: "Fail to upload: Illegal file size or format.",
                SAVE_401_402: "Cannot verify current user, please try again.",
                BTN_STATE_SAVING: "Saving",
                BTN_STATE_SAVE: "Save",
                BTN_STATE_RETRY: "Retry ",
                BTN_STATE_SAVED: "Saved"
            };
            break;
    }
    return resource;
}();