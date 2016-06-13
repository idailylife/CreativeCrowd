/**
 * Created by inlab-dell on 2016/6/8.
 */
$(document).ready(function () {
   $(".form_datetime").datetimepicker({
       closeOnDateSelect:true
   });

    var ajaxSubmit = function () {
        $("input[name=startTime]").val(
            getUnixTimestamp($("#inputStartTime").val()));
        $("input[name=endTime]").val(
            getUnixTimestamp($("#inputEndTime").val()));

        var extData = {
            desc: $("#textDesc").val(),
            desc_detail: $("#textDescDetail").val(),
            info: $("#textDescOther").val()
        };
        $("input[name=descJson]").val(
            JSON.stringify(extData)
        );

        var formData = new FormData($("#formBasicInfo")[0]);
        formData.append("mode", $("li[role=presentation].active > a").data("mode"));
        formData.append("taskId", "-1");
        $.ajax({
            url: homeUrl + 'task/create',
            type: 'POST',
            data: formData,
            success: function (data) {
                console.log(data);
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

