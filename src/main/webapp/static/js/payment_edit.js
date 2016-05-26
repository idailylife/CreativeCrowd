/**
 * Created by inlab-dell on 2016/5/25.
 */
$(document).ready(function () {
    switchSaveBtnState("disabled");
    $("input[type=radio][name=payMethod]").change(function () {
       if(this.value == 'alipay_other'){
           switchOtherPaymentInput(true);
       } else {
           switchOtherPaymentInput(false);
       }
        switchSaveBtnState("normal");
    });

    $("#btn_save").click(function () {
        switchSaveBtnState("saving");
        var selectedVal = null;
        if($("input[type='radio'][name='payMethod']:checked").length > 0){
            selectedVal = $("input[type='radio'][name='payMethod']:checked").val();
        }
        if(selectedVal != null){
            var data = {
                payMethod: selectedVal
            };
            if(selectedVal == 'alipay_other'){
                data['payAccount'] = $("#payAccount").val();
            }
            $.ajax({
                method: "POST",
                url:    homeUrl + "user/edit/payment",
                data:   JSON.stringify(data),
                contentType : "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    switch(data.state){
                        case 200:
                            switchSaveBtnState("saved");
                            break;
                        case 400:
                            alert("数据异常，请重试");
                            switchSaveBtnState("normal");
                            break;
                    }
                },
                error: function (error) {
                    console.log(error);
                    alert("网络异常，服务器连接失败" + error);
                }
            })
        }
    });
});

function switchOtherPaymentInput(enabled){
    $("#payAccount").prop("disabled", !enabled);
}

function switchSaveBtnState(state) {
    var btn = $("#btn_save");
    if(state == "saved"){
        btn.text("已保存").prop("disabled", true);
    } else if(state == "normal"){
        btn.text("保存").prop("disabled", false);
    } else if(state == "saving"){
        btn.text("保存中").prop("disabled", true);
    } else if(state == 'disabled'){
        btn.prop("disabled", true);
    }
}