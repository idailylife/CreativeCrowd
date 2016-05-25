/**
 * Created by inlab-dell on 2016/5/24.
 */
$(document).ready(function () {
   switchSaveButtonState("disabled");

   $("input[type!='submit']").on("change", function () {
      switchSaveButtonState("normal");
   });

   $("#formInfoEdit").submit(function (e) {
       switchSaveButtonState("disabled");
       e.preventDefault();
       var formData = $(this).serialize();
       if(formValidate()){
           clearErrorStetes();
           $.post({
               url: homeUrl + "user/edit/info",
               method: "post",
               data: formData,
               success: function (data) {
                   switch(data.state){
                       case 200:
                           switchSaveButtonState("saved");
                           break;
                       case 400:
                           switchSaveButtonState("fail");
                           var failNames = JSON.parse(data.content);
                           var i;
                           for(i=0; i<failNames.length; i++){
                               var item = $("input[name='" + failNames[i] + "']");
                               if(item != null && item.length == 1){
                                   item = item.parent().parent();
                                   item.addClass("has-error");
                               }
                           }
                           break;
                   }
               },
               error: function (error) {
                   console.error(error);
                   alert("服务器响应异常，请刷新重试");
               }
           });
       }
   });
});

function formValidate() {
    //TODO: 表单验证
    return true;
}

function clearErrorStetes() {
    var formGroups = $(".form-group");
    var i;
    for(i=0; i<formGroups.length; i++){
        $(formGroups[i]).removeClass("has-error");
    }
}

function switchSaveButtonState(state) {
    var btn = $("#btn_save");
    switch (state){
        case "saved":
            btn.attr("disabled", "disabled")
                .val("已保存");
            break;
        case "disabled":
            btn.attr("disabled", "disabled");
            break;
        case "fail":
            btn.removeClass("btn-primary")
                .addClass("btn-warning")
                .val("重试");
            break;
        case "normal":
            btn.removeClass("btn-warning")
                .addClass("btn-primary")
                .val("保存")
                .removeAttr("disabled");
            break;
    }
}