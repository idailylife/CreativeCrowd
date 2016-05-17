<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/10
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
    <div class="row">
        <div class="col-md-8">
            <h2>${title}</h2>
            <p>${desc}</p>
            <c:if test="${not empty image}">
                <img class="desc-img" src="<s:url value="/static/img/upload/${image}"/>">
            </c:if>
            <p>${descDetail}</p>
            <c:choose>
                <c:when test="${taskState eq 'EXPIRED'}">
                    <button class="btn btn-default" disabled="disabled" type="button" data-state="expired">已结束</button>
                </c:when>
                <c:when test="${taskState eq 'FINISHED'}">
                    <button class="btn btn-default" disabled="disabled" type="button" data-state="finished">已参与</button>
                </c:when>
                <c:when test="${taskState eq 'CLAIMED'}">
                    <button class="btn btn-info" type="button" data-state="claimed">继续先前任务</button>
                </c:when>
                <c:when test="${taskState eq 'JOINABLE'}">
                    <button class="btn btn-info" type="button" data-state="joinable">参与</button>
                </c:when>
                <c:when test="${taskState eq 'NEED_LOGIN'}">
                    <button class="btn btn-warning" type="button" data-state="need_login">请先登录</button>
                </c:when>
            </c:choose>
        </div>
        <div class="col-md-4">
            <div id="info-container">
                <p>任务相关信息</p>
                <ul>
                    <li>任务时限: ${startEndTime}</li>
                    <li>已参与人数: ${task.finishedCount}/${task.quota}</li>
                    <c:if test="${not empty estTime}">
                        <li>预期任务时长: ${estTime}</li>
                    </c:if>
                    <c:forEach var="info" items="${infoMap}">
                        <li>
                                ${info.key}:&nbsp;${info.value}
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>
