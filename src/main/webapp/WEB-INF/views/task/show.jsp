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
<html>
<head>
    <title>创意众包平台：</title>
    <meta charset="UTF-8">
    <sec:csrfMetaTags/>
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="<s:url value="/css/task_show_styles.css"/>">
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-8">
                <h2>${title}</h2>
                <p>${desc}</p>
                <c:if test="${not empty image}">
                    <img class="desc-img" src="<s:url value="/img/upload/${image}"/>">
                </c:if>
                <p>${descDetail}</p>
                <c:choose>
                    <c:when test="${loginState == 0}">
                        <button class="btn btn-info" type="button">参与</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-warning" type="button">请先登录</button>
                    </c:otherwise>
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
</body>
</html>
