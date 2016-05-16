/**
 * Created by inlab-dell on 2016/5/6.
 */
jQuery(document).ready(function ($) {
   $("#registerForm").submit(function (event) {
       //TODO: Disable submit button for now

       event.preventDefault();
       postRegForm();
   })
});

function postRegForm() {
    var data = {};
    data["email"] = $("#inputEmail").val();
    data["password"] = md5( $("#inputPassword").val() );
    var succFunc = function (data) {
        console.log("SUCCESS:", data);
    };
    var errorFunc = function (e) {
        console.log("ERROR:", e);
    };

    $.ajax({
        type: "POST",
        contentType : "application/json; charset=utf-8",
        url: homeUrl + "user/register",
        data: JSON.stringify(data),
        dataType: 'json',
        success: succFunc,
        error: errorFunc
    });
}