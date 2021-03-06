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

    var singlePagedRand_candidates = [];
    var singlePagedRand_goldenStdQuestions = [];
    $("#inputSinglePagedRand_config").change(function(e){
        handleXlsFile(e, singlePagedRand_candidates, singlePagedRand_goldenStdQuestions);
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
        } else if(mode == 3){
            params = {
                mtask_size: $("#inputSinglePagedRand_mtaskSize").val(),
                N : $("#inputSinglePagedRand_N").val(),
                K : $("#inputSinglePagedRand_K").val(),
                nRows: $("#inputSinglePagedRand_nRows").val(),
                freeChoice: false,
                candidates: singlePagedRand_candidates,
                use_gold_std: false
            };
            if (singlePagedRand_goldenStdQuestions.length > 0){
                params.gold_std_q = singlePagedRand_goldenStdQuestions;
                params.use_gold_std = true;
            }
            if ($('#inputSinglePagedRand_FreeChoice').is(':checked')){
                params.freeChoice = true;
            }
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
                    showErrorMsg(data.content);
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
        clearErrorMsg();
        ajaxSubmit();
    });
});

function showErrorMsg(content) {
    var jsonContent = JSON.parse(content);
    var prop, formGroupObj;
    for(prop in jsonContent){
        formGroupObj = $('div>input[name="' + prop + '"]');
        if(formGroupObj.length > 0){
            formGroupObj = formGroupObj.parent()[0];
            $(formGroupObj).addClass('has-error');
        }
    }
}

function clearErrorMsg() {
    var errorGroups = $('.form-group.has-error');
    var i;
    for(i=0; i<errorGroups.length; i++){
        $(errorGroups[i]).removeClass("has-error");
    }
}

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

function handleXlsFile(e, dataAry, goldStdAry) {
    //处理需要导入的Excel文件
    //多个文件导入支持，数据统一保存在一个列表里
    var files = e.target.files;
    var i, f;
    dataAry.length = 0;
    for(i=0, f=files[i]; i!=files.length; ++i) {
        var reader = new FileReader();
        //var name = f.name;
        reader.onload = function (e) {
            var data = e.target.result;
            var workbook = XLSX.read(data, {type: 'binary'});
            var sheet_name = workbook.SheetNames[0];  //只读取第一个工作表
            var worksheet = workbook.Sheets[sheet_name];
            var row_array = XLSX.utils.sheet_to_row_object_array(worksheet);
            row_array.forEach(function(rowItem){
                var tempItem = {};
                if('image' in rowItem){
                    tempItem.image = rowItem.image;
                }
                if('text' in rowItem){
                    tempItem.text = rowItem.text.replace(/<\/?[^>]+(>|$)/g, "");    //strip html tags
                }
                if(!isEmpty(tempItem)){
                    dataAry.push(tempItem);
                }
            });
            console.log("Excel file: read " + dataAry.length + " valid objects");
            var dispMsg = "共计已导入"+ dataAry.length + "条目.";
            //Golden standard question read
            if($("#inputSinglePagedRand_GoldenStd").is(':checked')){
                goldStdAry.length = 0;
                var n = $('#inputSinglePagedRand_N').val();
                var k = $('#inputSinglePagedRand_K').val();
                sheet_name = workbook.SheetNames[1];    //Read second worksheet
                worksheet = workbook.Sheets[sheet_name];
                row_array = XLSX.utils.sheet_to_row_object_array(worksheet);
                row_array.forEach(function(rowItem){
                    var gstd_item = {ref: rowItem.ref, cand:[], ans:[]};
                    var i;
                    for (i=0; i<n; i++){
                        gstd_item.cand.push(rowItem['cand'+(i+1)]);
                    }
                    for (i=0; i<k; i++){
                        gstd_item.ans.push(rowItem['ans'+(i+1)]);
                    }
                    goldStdAry.push(gstd_item);
                });
                console.log("Excel file: read "+ goldStdAry.length + " golden standard questions.");
                dispMsg += "\n共计已导入" + goldStdAry.length + "黄金标准问题.";
            }
            //
            $("#pSinglePagedRand_confState").text(dispMsg);
        };
        reader.readAsBinaryString(f);
    }
}

function isEmpty(obj){
    for (var x in obj) { return false; }
    return true;
}