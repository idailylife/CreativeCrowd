/**
 * Created by inlab-dell on 2016/6/15.
 */
var insertActionCount = 0;
var editorList = {};
$(document).ready(function () {
    $(".hiddenJsonTemplate").each(function () {
        console.log("parsing "+ this.id);
        if(this.value)
            parseJsonEditor(this);
    });

    $("#btnTabAdd").click(function () {
        insertActionCount++;
        appendMicroTask(null, "new_"+insertActionCount);
    });

    $(".btnInsertNext").each(function () {
        setInsertNext(this);
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

    $("#btnSeqConfigSave").click(function () {
        var seqText = parseMicrotaskSequenceStr();
        if(!seqText){
            alert("有新建的微任务尚未保存，请先保存再执行此操作");
            return;
        }
        setTaskParams(seqText);
    });

    var setTaskParams = function(params){
        $.ajax({
            method: 'post',
            url: homeUrl+'task/updparams/'+taskId,
            data: JSON.stringify({params: params}),
            contentType : "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                console.log(data);
                //TODO
            }
        })
    }
});

function setSave(element) {
    $(element).click(function () {
        var tableId = $(element).currentBSTabID();
        saveTabContent(tableId);;
    })
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
        appendMicroTask(currTabId, "new_"+insertActionCount);
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
 * @param appendAfterId 如果为null,则在最末插入
 */
function appendMicroTask(appendAfterId, newId){
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
    registerButtonActions(newId);
}

function registerButtonActions(newId){
    parseJsonEditor(document.getElementById("textTemplate_"+newId));
    setInsertNext(document.getElementById("btnInsertNext_"+newId));
    setMoveBackward(document.getElementById("btnMoveBackward_"+newId));
    setMoveForward(document.getElementById("btnMoveForward_"+newId));
    setSave(document.getElementById("btnSave_"+newId));
}

/**
 * 准备Json上传数据
 * @param tabContentId
 */
function prepareJsonString(tabContentId) {
    //Update json editor data
    var editorText = editorList[tabContentId].getText();
    $("#textTemplate_"+tabContentId).val(editorText);
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

function saveTabContent(tabContentId) {
    var jsonText = prepareJsonString(tabContentId);
    setSaveButtonState("disabled");
    $.ajax({
        url: homeUrl + '/task/edit/'+taskId,
        type: 'post',
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
    }
    return JSON.stringify(idList);
}