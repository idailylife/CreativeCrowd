<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/16
  Time: 14:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:choose>
    <c:when test="${handlerType eq 'grid_choice'}">
        <input type="hidden" id="tid" value="${task.id}"/>
        <input type="hidden" id="mturkId" value="${mturkId}"/>
        <tiles:insertTemplate template="/WEB-INF/views/pages/json_handler/${handlerType}.jsp"/>
    </c:when>
    <c:otherwise>
        <div class="container" id="question_container">
            <c:if test="${not empty taskProgress}">
                <div class="progress">
                    <div class="progress-bar" role="progressbar" style="width: ${taskProgress}%;">
                    </div>
                </div>
            </c:if>

            <div class="row">
                <div class="col-md-12">
                    <h1>${task.title}</h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-8">
                    <!-- Specified view -->
                    <input type="hidden" id="tid" value="${task.id}"/>
                    <input type="hidden" id="mturkId" value="${mturkId}"/>
                    <tiles:insertTemplate template="/WEB-INF/views/pages/json_handler/${handlerType}.jsp"/>
                </div>
                <div class="col-md-4">
                    <!-- TODO: Task properties here -->
                    <c:if test="${not empty task.timeLimit}">
                        <div class="alert alert-info" id="timeContainer">
                            <h4>Time Left: <span id="timeRemaining">-:-</span></h4>
                            <p>Please submit before timeout.</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<script>
    var homeUrl = "<c:url value="/"/>";
    var handlerType = "${handlerType}";
    var taskType = ${task.type};
</script>
