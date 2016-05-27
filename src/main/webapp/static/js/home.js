/**
 * Created by inlab-dell on 2016/5/18.
 */
var currentPage = 1;
var endOfPage = false;
$(document).ready(function () {
   $("#more-tasks").click(function () {
       $("#more-tasks").attr("disabled", "disabled");
       var data = {"page": currentPage+1};
       var succFunc = function (data) {
           console.log("SUCCESS:", data);
           var stateCode = data.state;
           switch (stateCode){
               case 201:
                   //End of tasks
                   endOfPage = true;
                   break;
               case 200:
                   currentPage++;
                   var contentObj = JSON.parse(data.content);
                   appendPage(contentObj);
                   $("#more-tasks").removeAttr("disabled");
                   if(contentObj.length < 8){
                       endOfPage = true;
                   }
                   $("html, body").animate({
                      scrollTop: $(document).height()
                   }, 1000);
                   break;
           }
           if(endOfPage){
               $("#btn-more-icon").removeClass("glyphicon-chevron-down")
                   .addClass("glyphicon-minus");
               $("#more-tasks").attr("disabled", "disabled");
           }
       };
       var errFunc = function (e) {
           console.log("ERROR:", e);
       };

       $.ajax({
           type: "POST",
           contentType : "application/json; charset=utf-8",
           url: homeUrl + "more",
           data: JSON.stringify(data),
           dataType: 'json',
           success: succFunc,
           error: errFunc
       });
   });
});


function appendPage(jsonAryResult) {
    var i, jsonTaskObj;
    for(i=0; i<jsonAryResult.length; i++){
        jsonTaskObj = jsonAryResult[i];
        var taskBigButton = document.createElement("a");
        var objClass = "col-md-3 btn-task";
        if(jsonTaskObj.expired == true){
            objClass += " task-invalid";
        }
        taskBigButton.setAttribute("class", objClass);
        taskBigButton.setAttribute("href", homeUrl + "task/tid" + jsonTaskObj.id);
        taskBigButton.setAttribute("target", "_blank");
        taskBigButton.setAttribute("role", "button");

        var thumbnailDivObj = document.createElement("div");
        thumbnailDivObj.setAttribute("class", "thumbnail thumbnail-task");

        // var imageObj = document.createElement("img");
        // imageObj.setAttribute("src", homeUrl+"static/img/upload/"+ jsonTaskObj.image);
        // thumbnailDivObj.appendChild(imageObj);
        var imgContainer = document.createElement("div");
        imgContainer.setAttribute("class", "task-img-container");
        imgContainer.innerHTML = '<span class="helper"></span>';
        var imgObj = document.createElement("img");
        if(jsonTaskObj.image != null && jsonTaskObj.image.length > 0){
            imgObj.setAttribute("src", homeUrl+"static/img/upload/"+ jsonTaskObj.image);
        } else {
            imgObj.setAttribute("src", homeUrl+"static/img/task-no-img.png");
        }
        imgContainer.appendChild(imgObj);
        thumbnailDivObj.appendChild(imgContainer);

        var captionDiv = document.createElement("div");
        captionDiv.setAttribute("class", "caption");
        var h4Obj = document.createElement("h4");
        h4Obj.innerHTML = jsonTaskObj.title;
        captionDiv.appendChild(h4Obj);
        var pTimeObj = document.createElement("p");
        pTimeObj.innerHTML = '有效时间: <span class="valid-time">'
                        + jsonTaskObj.durationStr
                        + '</span>';
        captionDiv.appendChild(pTimeObj);
        var pQuotaObj = document.createElement("p");
        pQuotaObj.innerHTML = '<span class="joined">'
                        + jsonTaskObj.claimedCount + '</span>/<span class="total_joined">'
                        + jsonTaskObj.quota + '</span>';
        captionDiv.appendChild(pQuotaObj);
        var pTaskTag = document.createElement("p");
        pTaskTag.setAttribute("class", "text-right task-type");
        pTaskTag.innerHTML = jsonTaskObj.tag;
        captionDiv.appendChild(pTaskTag);

        thumbnailDivObj.appendChild(captionDiv);
        taskBigButton.appendChild(thumbnailDivObj);

        $("#task-row").append(taskBigButton);
    }
}