<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hebowei
  Date: 16/5/21
  Time: 下午2:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form id="form-mtask" enctype="multipart/form-data">
<c:forEach items="${handlerContent}" var="item">
    <div class="row row-with-gap">
        <c:choose>
            <c:when test="${item.tag eq 'button'}">
        <div class="col-md-12 text-right">
            </c:when>
            <c:otherwise>
        <div class="col-md-12">
            </c:otherwise>
        </c:choose>

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
                <c:when test="${item.tag eq 'text'}">
                    <c:choose>
                        <c:when test="${item.contents['multiline'] eq 'true'}">
                    <textarea class="form-control" id="ud_${item.contents['id']}" rows="5"
                            <c:if test="${item.contents.containsKey('placeholder')}">
                                placeholder="${item.contents['placeholder']}"
                            </c:if> >
                    </textarea>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" id="ud_${item.contents['id']}"
                            <c:if test="${item.contents.containsKey('placeholder')}">
                                   placeholder="${item.contents['placeholder']}"
                            </c:if> >
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${item.tag eq 'file'}">
                    <c:if test="${item.contents.containsKey('text')}">
                        <label for="btn_file" class="ontrol-label">${item.contents['text']}</label>
                    </c:if>
                    <input type="file" id="btn_file" class="btn btn-default"
                    <c:if test="${item.contents.containsKey('accept')}">
                           accept="${item.contents['accept']}"
                    </c:if> >
                    <a class="btn btn-default" href="#" role="button">上传</a>
                    <label id="label_upload_state"></label>
                </c:when>
                <c:when test="${item.tag eq 'button'}">
                    <c:choose>
                        <c:when test="${item.contents['type'] eq 'int'}">
                            <input type="button" id="btn_${item.contents['target']}"
                            <c:choose>
                            <c:when test="${item.contents['target'] eq 'submit'}">
                                   class="btn btn-primary" value="提交"
                            </c:when>
                            <c:when test="${item.contents['target'] eq 'prev'}">
                                   class="btn btn-default" value="上一项"
                            </c:when>
                            <c:when test="${item.contents['target'] eq 'next'}">
                                   class="btn btn-default" value="下一项"
                            </c:when>
                            </c:choose>
                            >
                        </c:when>
                    </c:choose>
                </c:when>

            </c:choose>
        </div>
    </div>
</c:forEach>
</form>
<script src="<c:url value="/static/js/handler/simple.js"/>"></script>