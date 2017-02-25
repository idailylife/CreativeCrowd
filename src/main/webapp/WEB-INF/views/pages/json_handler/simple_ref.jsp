<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  simple_ref渲染模式
  handlerContent.key contains data of reference items,
  handlerContent.value contains data of questions.

  Reference Items:
      [
         {CHILD, {COLUMN_NAME: COLUMN_VALUE, ...},
         *{PARENT, {COLUMN_NAME: COLUMN_VALUE, ...},
         ...
      ]

      If COLUMN_NAME starts with 'image', we interpret it as image, otherwise we interpret it as text.
      Reference items are filled up automatically, no need to put it in microtask template.

  Questions: the templateStr is a `stringified` jsonArray, which contains multiple lines of jsonObjects
      each jsonObject has a key indicates its type, which can be a (lower-cased, * means optional):
      [
       {label,     {id: ID, text: CONTENT, *for: FOR_ID}}                          // labels (for some input FOR_ID)
       {text,      {id: ID, multiline: true/false, *placeholder: TEXT}}            // text input
       {choice,    {id: ID, type: single/multiple, ary_items:[ITEM_1, ITEM_2, ...]}
                                                                                   // single(default)/multiple choice box
       {image,     {src: internal_url}}                                            // image
       {file,      {accept: ALLOWED_TYPES, *text: TEXT_DESCRIPTION}}               // file
       {timer,     {}                                                              // a timer that records the duration between page load and answer submission
      ]

  User: hebowei
  Date: 17/2/5
  Time: 下午2:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Reference Items -->
<div class="radio">
    <label>
        <input type="radio" name="selectedRefId" class="form-ud-input" value="${umtId}" checked>
        Design without reference.
    </label>
</div>

<c:forEach items="${handlerContent.key}" var="item">
    <div class="row row-with-gap col-md-12">
        <p> ${item.tag} </p>
        <%--<p> ${item.contents['image']} </p>--%>
        <%--<p> ${item.contents['text']} </p>--%>
        <input class="">
        <c:forEach items="${item.contentKeys}" var="contentKey">
            <c:choose>
                <c:when test="${contentKey eq 'umtId'}">
                    <input type="radio" name="selectedRefId" class="form-ud-input" value="${item.contents['umtId']}">
                </c:when>
                <c:when test="${fn:startsWith(contentKey, 'image')}">
                    <img class="img-thumbnail" src="<c:url value="/static/img/upload/task/${task.id}/${item.contents[contentKey]}"/> ">
                </c:when>
                <c:otherwise>
                    <p>${item.contents[contentKey]}</p>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</c:forEach>

<!-- Form items -->
<c:forEach items="${handlerContent.value}" var="item">
    <div class="row row-with-gap col-md-12">
            <c:choose>
                <c:when test="${item.tag eq 'label'}">
                    <label id="ud_${item.contents['id']}" class="control-label"
                            <c:if test="${item.contents.containsKey('for')}">
                                for="${item.contents['for']}"
                            </c:if>  >
                            ${item.contents['text']}
                    </label>
                </c:when>
                <c:when test="${item.tag eq 'image'}">
                    <img id="ud_${item.contents['id']}" class="img-responsive center-block img-thumbnail"
                         src="<c:url value="/static/img/upload/task/${task.id}/${item.contents['src']}"/> ">
                </c:when>
                <c:when test="${item.tag eq 'choice'}">
                    <c:forEach items="${item.contents['ary_items']}" var="choiceItem">
                        <c:choose>
                            <c:when test="${item.contents['type'] eq 'single'}">
                                <label class="radio-inline">
                                    <input type="radio" name="${item.contents['id']}" value="${choiceItem}" class="form-ud-input">
                                    ${choiceItem}
                                </label>
                            </c:when>
                            <c:otherwise>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="${item.contents['id']}" value="${choiceItem}" class="form-ud-input">
                                    ${choiceItem}
                                </label>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:when>
                <c:when test="${item.tag eq 'text'}">
                    <c:choose>
                        <c:when test="${item.contents['multiline'] eq 'true'}">
                    <textarea class="form-control form-ud-input" id="ud_${item.contents['id']}" name="${item.contents['id']}" rows="5"
                           <c:if test="${item.contents.containsKey('placeholder')}">
                                placeholder="${item.contents['placeholder']}"
                            </c:if> ><c:if test="${savedResults.containsKey(item.contents['id'])}">${savedResults[item.contents['id']]}</c:if></textarea>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control form-ud-input" id="ud_${item.contents['id']}" name="${item.contents['id']}"
                            <c:if test="${item.contents.containsKey('placeholder')}">
                                   placeholder="${item.contents['placeholder']}"
                            </c:if>
                            <c:if test="${savedResults.containsKey(item.contents['id'])}">
                                value="${savedResults[item.contents['id']]}"
                            </c:if>
                            >
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${item.tag eq 'file'}">
                    <c:if test="${item.contents.containsKey('text')}">
                        <div class="row">
                            <div class="col-md-12">
                                <label for="btn_file" class="ontrol-label">${item.contents['text']}</label>
                            </div>
                        </div>
                    </c:if>
                    <div class="row">
                        <form enctype="multipart/form-data" method="post" name="fileinfo" id="fileinfo">
                            <div class="col-md-6">
                                <input type="file" id="btn_file" class="btn btn-default" name="file" required
                                <c:if test="${item.contents.containsKey('accept')}">
                                       accept="${item.contents['accept']}"
                                </c:if> >
                            </div>
                            <div class="col-md-6">
                                <button class="btn btn-default" id="btn_upload" type="button" disabled="disabled">
                                    <c:choose>
                                        <c:when test="${not empty file}">
                                            ${isMTurkTask ? 'Replace':'替换'}
                                        </c:when>
                                        <c:otherwise>
                                            ${isMTurkTask ? 'Upload':'上传'}
                                        </c:otherwise>
                                    </c:choose>
                                </button>
                                <label id="label_upload_state">
                                    <c:if test="${not empty file}">
                                        <a href="<c:url value="/static/img/upload/user_gen/${task.id}/${file}"/>" target="_blank">
                                        ${isMTurkTask ? 'An image was uploaded before':'之前已上传过一张图'}
                                        </a>
                                    </c:if>
                                </label>
                                <input type="hidden" id="file_upd_state" value="${(empty file)? '0':'1'}"/>
                            </div>
                        </form>
                    </div>
                </c:when>
                <c:when test="${item.tag eq 'timer'}">
                    <input type="hidden" class="form-ud-input" id="timer_start_time">
                    <input type="hidden" class="form-ud-input" id="timer_end_time">
                    <script type="text/javascript">
                        $(document).load(function () {
                            $('#timer_start_time').val((new Date()).valueOf());
                        })
                    </script>
                </c:when>
            </c:choose>

    </div>
</c:forEach>
<!-- Control buttons -->
<div class="row row-with-gap col-md-12">
    <div class="text-right">
        <c:if test="${not empty prev}">
            <input type="button" class="btn btn-default" id="btn_prev" value="${(task.type == 0)? '上一题':'Prev'}">
        </c:if>
        <input type="button" class="btn btn-default" id="btn_save" value="${(task.type == 0)? '保存':'Save'}">
        <c:choose>
            <c:when test="${empty next}">
                <input type="button" class="btn btn-primary" id="btn_submit" value="${(task.type == 0)? '提交':'Submit'}">
            </c:when>
            <c:otherwise>
                <input type="button" class="btn btn-default" id="btn_next" value="${(task.type == 0)? '下一题':'Next'}">
            </c:otherwise>
        </c:choose>
    </div>
</div>
