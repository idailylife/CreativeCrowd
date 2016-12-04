/**
 * Created by inlab-dell on 2016/12/3.
 */
$(document).ready(function(){
    //Data binding
    var bind_ref = new Vue({
        el: '#div-ref',
        data: ref_item
    });

    var next_btn_disabled = true;

    var bind_candidate = new Vue({
        el: '#div-candidate',
        data: {
            items: candidate_items,
            select_count : 0
        },
        methods: {
            selectToggle: function (msg, index) {
                console.log("item clicked "+ msg + ", index= "+index);

                if(!this.items[index].selected && this.select_count>=grid_params.K){
                    //when trying to select more than K items, warn and stop
                    alert('You are going to choose more than ' + grid_params.K + ' items. Please DE-SELECT one before alternating your choice.');
                    return;
                }

                this.items[index].selected = !this.items[index].selected;
                if(this.items[index].selected){
                    this.select_count++;
                } else {
                    this.select_count--;
                }
                if(this.select_count >= grid_params.K){
                    //next_btn_disabled = false;
                    bind_next_btn.next_btn_disabled = false;
                } else {
                    //next_btn_disabled = true;
                    bind_next_btn.next_btn_disabled = true;
                }
                this.items[index].click_time = (new Date()).valueOf();
            }
        }
    });
    
    var bind_next_btn = new Vue({
        el: '#btn-next',
        data: {
            next_btn_disabled: next_btn_disabled
        },
        methods: {
            clickFunc : function (e) {
                //Generate results and submit
                //Result format:
                // {  ref: {id: ID},
                //    candidates: [{id: ID, selected: IS_SELECTED, timestamp: CLICK_TIMESTAMP},...]
                // }
                var upd_data = {
                    tid: $("#tid").val()
                };
                var item = {
                    id: ref_item.id
                };
                upd_data.ref = item;

                var cand_ary = [];
                var i;
                for(i=0; i<candidate_items.length; i++){
                    cand_ary.push({
                        id: candidate_items[i].id,
                        selected : candidate_items[i].selected,
                        timestamp : candidate_items[i].click_time
                    });
                }
                upd_data.candidates = cand_ary;

                this.next_btn_disabled = true;

                var saveSuccCallback = function () {
                    $.ajax({
                        type: "post",
                        url: homeUrl + "task/submit",
                        success: function (data) {
                            switch(data.state){
                                case 200:
                                    var redirUrl = homeUrl + "task/done";
                                    if(taskType == 1){
                                        redirUrl += "?refCode=" + data.content;
                                    }
                                    window.location.href = redirUrl;
                                    break;
                                case 201:
                                    window.location.reload();
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
                            setSubmitButtonState("normal");
                        }
                    });
                };

                $.ajax({
                    type: "POST",
                    contentType : "application/json; charset=utf-8",
                    url: homeUrl + "task/do",
                    data: JSON.stringify(upd_data),
                    dataType: 'json',
                    success: function(data) {
                        next_btn_disabled = false;
                        switch (data.state){
                            case 200:
                                saveSuccCallback();
                                break;
                            case 401: case 402:
                                alert('Error'+ data.state + 'Cannot verify current user, please try again or refresh the webpage.');
                                break;
                        }
                    },
                    error: function (err) {
                        console.log(err);
                        alert(messageResources.AJAX_CONN_FAIL);
                    }
                });
            }
        }
    });

    var bind_num_K = new Vue({
        el: '#num_K',
        data: {
           K : grid_params.K
        }
    });

    //Popup config
    $('.single-popup-link').magnificPopup({
        type: 'image',
        image: {
            titleSrc: function(item){
                return "<small>" + $("#ref-image-title").text() + "</small>";
            } //"title" //$("#ref-image-title").text()
        }
    });

    $('.group-popup-link').magnificPopup({
        type: 'image',
        gallery: {
            enabled: true
        }
    });
});

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
                SAVE_FORM_IMCOMPLETE: "表单尚未填写完整，无法保存",
                BTN_STATE_SAVING: "正在保存",
                BTN_STATE_SAVE: "保存",
                BTN_STATE_RETRY: "重试",
                BTN_STATE_SAVED: "已保存",
                BTN_STATE_SUBMITTING: "正在提交",
                BTN_STATE_SUBMIT: "提交"
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
                SAVE_FORM_IMCOMPLETE: "Please complete all the required fields before submitting",
                BTN_STATE_SAVING: "Saving",
                BTN_STATE_SAVE: "Save",
                BTN_STATE_RETRY: "Retry ",
                BTN_STATE_SAVED: "Saved",
                BTN_STATE_SUBMITTING: "Submitting",
                BTN_STATE_SUBMIT: "Submit"
            };
            break;
    }
    return resource;
}();