/**
 * Created by inlab-dell on 2016/5/24.
 */
$(document).ready(function () {
   $("#formInfoEdit").submit(function (e) {
       e.preventDefault();
       if(formValidate()){
           $.post({

           };
       }
   });
});

function formValidate() {
    //TODO: 表单验证
    return true;
}