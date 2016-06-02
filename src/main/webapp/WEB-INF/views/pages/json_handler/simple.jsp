<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hebowei
  Date: 16/5/21
  Time: 下午2:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<c:forEach items="${handlerContent}" var="item">
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
                         src="<c:url value="/static/img/upload/"/>${item.contents['src']} ">
                </c:when>
                <c:when test="${item.tag eq 'choice'}">
                    <c:forEach items="${item.contents['ary_items']}" var="choiceItem">
                        <c:choose>
                            <c:when test="${item.contents['type'] eq 'single'}">
                                <label class="radio-inline">
                                    <input type="radio" name="ud_${item.contents['id']}" value="${choiceItem}">
                                    ${choiceItem}
                                </label>
                            </c:when>
                            <c:otherwise>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="ud_${item.contents['id']}" value="${choiceItem}">
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
                                        <a href="<c:url value="/static/img/upload/${file}"/>" target="_blank">
                                        ${isMTurkTask ? 'An image was uploaded before':'之前已上传过一张图'}
                                        </a>
                                    </c:if>
                                </label>
                                <input type="hidden" id="file_upd_state" value="${(empty file)? '0':'1'}"/>
                            </div>
                        </form>
                    </div>
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
