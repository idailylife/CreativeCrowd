/**
 * Created by hebowei on 16/5/22.
 */
//根据handler类型动态加载对应的css及js文件

// Replace the normal jQuery getScript function with one that supports
// debugging and which references the script files as external resources
// rather than inline.
// Ref: http://stackoverflow.com/questions/690781/debugging-scripts-added-via-jquery-getscript-function
jQuery.extend({
    getScript: function (url, callback) {
        var head = document.getElementsByTagName("head")[0];
        var script = document.createElement("script");
        script.src = url;

        // Handle Script loading
        {
            var done = false;

            // Attach handlers for all browsers
            script.onload = script.onreadystatechange = function(){
                if ( !done && (!this.readyState ||
                    this.readyState == "loaded" || this.readyState == "complete") ) {
                    done = true;
                    if (callback)
                        callback();

                    // Handle memory leak in IE
                    script.onload = script.onreadystatechange = null;
                }
            };
        }

        head.appendChild(script);

        // We handle everything using the script element injection
        return undefined;
    }
});

$(document).ready(function () {
    $.getScript(homeUrl + "static/js/handler/" + handlerType + ".js", function (data) {
       console.log("handler " + handlerType + ".js loaded.");
    });
    $("<link/>", {
        rel: "stylesheet",
        type: "text/css",
        href: homeUrl + "static/css/handler/" + handlerType + ".css"
    }).appendTo("head");
    if(($("#timeRemaining") != null) && (taskType == 1)){
        var timeRemaining = 0;

        var echoData = {
            taskType : taskType,
            taskId  : $("#tid").val(),
            mturkId : $("#mturkId").val()
        };
        var echoService = function () {
            $.ajax({
                type: "post",
                url: homeUrl + 'echo/mturk',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(echoData),
                dataType: 'json',
                success: function (data) {
                    switch (data.state){
                        case 200:
                            //console.log(data.content);
                            timeRemaining = data.content;
                            setRemainTime(data.content);
                            break;
                        case 400:case 401:case 415: case 402:
                            console.error(data.message);
                            break;
                        case 403:
                            showOvertime();
                            break;
                        default:
                            alert("Unknown return state " + data.state);
                    }
                },
                error : function (jqXHR, textStatus, errorThrown) {
                    alert("Cannot connect to server: "+ textStatus);
                    console.error(errorThrown);
                    console.error(jqXHR);
                }
            });
        };
        echoService();
        setInterval(echoService, 10000); //10 seconds beat
        setInterval(function () {
            timeRemaining = timeRemaining - 1;
            setRemainTime(timeRemaining);
            if(timeRemaining < 300){
                warnOvertime();
            }
            if(timeRemaining == 60){
                alert("Please submit your work before timeout, or the task will be abolished");
            }
            
            
        },1000);
    }
});

function setRemainTime(timeInSeconds){
    var minutes = Math.floor(timeInSeconds / 60);
    var seconds = timeInSeconds % 60;
    if(seconds < 10){
        seconds = '0' + seconds;
    }
    var text = minutes + ':' + seconds;
    $("#timeRemaining").text(text);
}

function warnOvertime() {
    //警告超时
    $("#timeContainer").removeClass("alert-info")
        .addClass("alert-danger");
}

function showOvertime(){
    alert("Oh, sorry, time is up! Task aborted.");
}