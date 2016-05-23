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
});