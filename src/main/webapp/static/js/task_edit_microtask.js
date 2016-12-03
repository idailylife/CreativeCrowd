/**
 * Created by inlab-dell on 2016/6/15.
 */
var insertActionCount = 0;
var editorList = {};
$(document).ready(function () {
    Dropzone.options.formFileBatch = {
        maxFilesize: 2,
        paramName: "file",
        headers: { token: $('#hiddenUploadToken').val() },
        acceptedFiles: 'image/*'
    };

    $(".hiddenJsonTemplate").each(function () {
        console.log("parsing "+ this.id);
        if(this.value)
            parseJsonEditor(this);
    });

    if($("#hiddenRandTaskParams").length > 0){
        (function () {
            var randParam = JSON.parse($("#hiddenRandTaskParams").val());
            $("#inputTaskParams").val(randParam.randSize);
        })();
    }

    $("#btnTabAdd").click(function () {
        insertActionCount++;
        appendMicroTask(null, "新建_"+insertActionCount);
    });

    $(".btnInsertNext").each(function () {
        setInsertNext(this);
    });

    $(".btnCopyNext").each(function () {
        setCopyNext(this);
    });

    $(".btnMoveBackward").each(function () {
        setMoveBackward(this)
    });

    $(".btnMoveForward").each(function () {
        setMoveForward(this);
    });

    $(".btnSave").each(function () {
        setSave(this);
    });

    $(".btnDelete").each(function () {
        setDelete(this);
    });

    $("#btnSeqConfigSave").click(function () {
        var seqText = parseMicrotaskSequenceStr();
        if(!seqText){
            alert("有新建的微任务尚未保存，请先保存再执行此操作");
            return;
        }
        setTaskParams(seqText);
    });

    $("#btnRandConfigSave").click(function () {
        updateRandomTaskParams();
    });

    $('#btnSaveAll').click(function () {
        //保存所有当前配置并提交
        $(this).prop("disabled", true);
        setProgBarStateAndText(10, "Init...");
        //Save all tabs
        var li, li_id, tab_id, i;
        var liArray = $("ul.nav-tabs>li");
        var saveCount = 0, maxSaveCount = liArray.length-2;
        var incrSaveCount = function () {
            saveCount++;
            var prog = 100*saveCount/maxSaveCount;
            setProgBarStateAndText(prog, "Saving item " + saveCount + " of " + maxSaveCount);
            if(saveCount >= maxSaveCount){
                $.ajax({
                    type: "GET",
                    url: homeUrl + 'file/unregister',
                    data: {
                        token: $("#hiddenUploadToken").val(),
                        clearAll: false
                    },
                    success: function (data) {
                        if(data.state == 200){
                            window.location.href = homeUrl + 'user/task/published';
                        }
                    },
                    error: function () {
                        alert("提交发生错误");
                    }
                })
            }
        };


        for(i=0; i<liArray.length; i++){
            li_id = liArray[i].id;
            tab_id = li_id.match(/\S+(?=-li)/);
            if(tab_id=='control' || tab_id==null)
                continue;
            var editorText = editorList[tab_id].getText();
            var savedText = $("#textTemplate_"+tab_id).val();
            if(editorText != savedText){
                //本地变更未保存，需要上传
                saveTabContent(tab_id, incrSaveCount);
            } else {
                incrSaveCount();
            }
        }
        if(liArray.length <= 2)
            incrSaveCount();

        //task params
        updatePossibleParams(incrSaveCount);
    });
    
    var setProgBarStateAndText = function(state, text) {
        if(state > 0){
            $(".progress").show();
            $("#progBarSaveAll")
                .css("width", state + "%");
            $("#progBarSaveAll>span").text(text);
        } else {
            $(".progress").hide();
        }
    }
});

var setTaskParams = function(params, informFunc){
    $.ajax({
        method: 'post',
        url: homeUrl+'task/updparams/'+taskId,
        data: JSON.stringify({params: params}),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if(data.state == 200){
                $(".paramSaveState").text("配置保存成功.");
                setTimeout(function () {
                    $(".paramSaveState").text("");
                }, 5000);
                if(informFunc)
                    informFunc();
            } else if(data.start == 403){
                alert("登录超时");
            }
        }
    })
};

function setSave(element) {
    $(element).click(function () {
        var tableId = $(element).currentBSTabID();
        saveTabContent(tableId);;
    });
}

function setDelete(element) {
    $(element).click(function () {
        deleteTabContent($(element).currentBSTabID());
    });
}

function setMoveBackward(element) {
    $(element).click(function () {
        var currTab = $(this).currentBSTab();
        currTab.moveBackward("control-li");
    });
}

function setMoveForward(element) {
    $(element).click(function () {
        var currTab = $(this).currentBSTab();
        currTab.moveForward("tabInsert");
    });
}

function setInsertNext(element) {
    $(element).click(function () {
        var currTabId = $(this).currentBSTabID();
        insertActionCount++;
        appendMicroTask(currTabId, "新建_"+insertActionCount);
    });
}

function setCopyNext(element) {
    $(element).click(function () {
        var currTabId = $(this).currentBSTabID();
        insertActionCount++;
        var newId = "新建_"+insertActionCount;
        appendMicroTask(currTabId, newId, true);
    });
}

/**
 * 对于新加入/变更的json文字的hidden input元素，生成可以显示的JSON Editor
 * For JSON Editor, see
 *          https://github.com/josdejong/jsoneditor
 * @param inputDOM
 */
function parseJsonEditor(inputDOM) {
    var mtaskId = $(inputDOM).data("id");
    var container = document.getElementById("jsonEditor_"+mtaskId);
    var editor;
    if(editorList.hasOwnProperty(mtaskId)){
        editor = editorList[mtaskId];
    } else {
        editor = new JSONEditor(container, {
            modes: ["tree", "form", "code", "text"]
        });
        editorList[mtaskId] = editor;
    }
    if(inputDOM.value.length < 1){
        inputDOM.value = "[]";
    }
    editor.setText(inputDOM.value);
    editor.expandAll();
}

/**
 * 在appendAfterId后插入一页
 * @param appendAfterId 如果为null,则在最末插入(tab的id, 后缀有-li)
 * @param newId 新建tab的id
 * @param copySourceContent 是否复制数据到新页
 */
function appendMicroTask(appendAfterId, newId, copySourceContent){
    var templateHtml = $("#panelTemplate").html();
    templateHtml = templateHtml.replace(/template/g, newId);
    if(appendAfterId == null){
        appendAfterId = $($("#tabInsert").prev()).attr("id");
    } else {
        appendAfterId += "-li";
    }
    var tabs = $("ul.nav-tabs");
    var appendAfterItem = $("#"+appendAfterId);
    tabs.addBSTab(newId, "#"+newId, templateHtml, appendAfterItem);
    var originId = appendAfterId.match(/\S+(?=-li)/);
    if(originId.length>0 && copySourceContent){
        if($('#'+originId).length > 0){
            $('#textTemplate_'+newId).val($('#textTemplate_'+originId).val());
        }
    }
    registerButtonActions(newId);
}

function registerButtonActions(newId){
    parseJsonEditor(document.getElementById("textTemplate_"+newId));
    setInsertNext(document.getElementById("btnInsertNext_"+newId));
    setCopyNext(document.getElementById("btnCopyNext_"+newId));
    setMoveBackward(document.getElementById("btnMoveBackward_"+newId));
    setMoveForward(document.getElementById("btnMoveForward_"+newId));
    setSave(document.getElementById("btnSave_"+newId));
    setDelete(document.getElementById("btnDelete_"+newId));
}

/**
 * 准备Json上传数据
 * @param tabContentId
 */
function prepareJsonString(tabContentId) {
    //Update json editor data
    var editorText = editorList[tabContentId].getText();
    //$("#textTemplate_"+tabContentId).val(editorText);
    var data = {
        handlerType: $("#inputHandlerType_"+tabContentId).val(),
        template: editorText,
        id: null,
        prevId: null,
        nextId: null
    };
    var actualId = $("form[id='"+tabContentId+"-form']>input[type=hidden]").val();
    if($.isNumeric(tabContentId)){
        data['id'] = tabContentId;
    } else if(actualId){
        data['id'] = actualId;
    }
    // if($("#"+tabContentId+"-li").prev().attr("id") != "control-li"){
    //     data['prevId'] =  $("#"+tabContentId+"-li").prev().attr("id").match(/\S+(?=-li)/)[0];
    // }
    // if($("#"+tabContentId+"-li").next().attr("id") != "tabInsert"){
    //     data['nextId'] =  $("#"+tabContentId+"-li").next().attr("id").match(/\S+(?=-li)/)[0];
    // }
    return JSON.stringify(data);
}

function deleteTabContent(tabContentId) {
    var id = $("form[id='"+tabContentId+"-form']>input[type=hidden]").val();
    if(id){
        //Saved microtask: delete from server first
        $.ajax({
            url: homeUrl + '/task/edit/'+taskId,
            type: 'DELETE',
            contentType : "application/json; charset=utf-8",
            dataType: 'json',
            data: JSON.stringify({mtaskId:id}),
            success: function (data) {
                console.log(data);
                if(data.state == 200){
                    removeTabContent(tabContentId);
                    updateSequenceTaskParams();
                } else if(data.state == 403){
                    alert("无法删除，鉴权失败");
                } else if(data.state == 400){
                    alert("请求格式错误:"+data.message)
                }
            },
            error: function (err) {
                alert("服务器响应异常，请重试");
                setSaveButtonState("normal");
            }
        });
    } else {
        //Clear tab directly
        removeTabContent(tabContentId);
    }
}

function updatePossibleParams(informFunc) {
    updateRandomTaskParams(informFunc);
    updateSequenceTaskParams(informFunc);
}

function updateSequenceTaskParams(informFunc) {
    if($("#btnSeqConfigSave").length > 0){
        //顺序任务，同步更新参数
        setTaskParams(parseMicrotaskSequenceStr(), informFunc);
    }
}

function updateRandomTaskParams(informFunc) {
    if($('#btnRandConfigSave').length > 0){
        var randParam = JSON.parse($("#hiddenRandTaskParams").val());
        randParam.randSize = $("#inputTaskParams").val();
        randParam = JSON.stringify(randParam);
        $("#hiddenRandTaskParams").val(randParam);
        setTaskParams($("#hiddenRandTaskParams").val(), informFunc);
    }
}

function removeTabContent(tabContentId) {
    var tabs = $("ul.nav-tabs");
    var tab = $(tabs).getBSTabByID(tabContentId);
    tab.removeBSTab();
}

function saveTabContent(tabContentId, informFunc) {
    var jsonText = prepareJsonString(tabContentId);
    setSaveButtonState(tabContentId, "disabled");
    $.ajax({
        url: homeUrl + '/task/edit/'+taskId,
        type: 'PUT',
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data: jsonText,
        success: function (data) {
            console.log(data);
            if(data.state == 200){
                $("#labelSaveState_"+tabContentId).text("修改已保存.");
                $("form[id='"+tabContentId+"-form']>input[type=hidden]").val(data.content);
                setTimeout(function () {
                    $("#labelSaveState_"+tabContentId).text("");
                }, 5000);
                $("#textTemplate_"+tabContentId).val(jsonText.template);//Update hidden value
                updateSequenceTaskParams();
                if(informFunc)
                    informFunc();
            } else if(data.state == 403){
                alert("无法保存，鉴权失败");
                setSaveButtonState("normal");
            }
        },
        error: function (err) {
            alert("服务器响应异常，请重试");
            setSaveButtonState("normal");
        }
    });
}

function setSaveButtonState(tabContentId, state){
    var button = $("#btnSave_"+tabContentId);
    if(state == "disabled"){
        button.prop("disabled", true);
    } else if(state == "normal") {
        button.prop("disabled", false);
    }
}

/**
 * 微任务顺序输出
 * 如果存在新建任务尚未保存，返回null
 */
function parseMicrotaskSequenceStr() {
    var li, li_id, tab_id, i;
    var idList = [], currId;
    var liArray = $("ul.nav-tabs>li");
    for(var i=0; i<liArray.length; i++){
        li_id = liArray[i].id;
        tab_id = li_id.match(/\S+(?=-li)/);
        if(tab_id=='control' || tab_id==null)
            continue;
        currId = $("form[id='"+tab_id+"-form']>input[type=hidden]").val();
        if(!currId){
            console.warn("Fail to parse: un-saved element(s) detected.");
            return null;
        }
        idList.push(currId);
    }
    return JSON.stringify(idList);
}
