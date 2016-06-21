<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/5
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="jumbotron ">
    <div id="carousel-tasks" class="carousel slide" data-ride="carousel">
        <!-- Indicators -->
        <ol class="carousel-indicators">
            <c:forEach items="${headlines}" var="headline" varStatus="loop">
                <li data-target="#carousel" data-silde-to="${loop.index}"
                    <c:if test="${loop.index == 0}">class="active"</c:if>>
                </li>
            </c:forEach>
            <%--<li data-target="#carousel-tasks" data-slide-to="0" class="active"></li>--%>
            <%--<li data-target="#carousel-tasks" data-slide-to="1" ></li>--%>
        </ol>

        <!-- Wrapper for slides -->
        <div class="carousel-inner" role="listbox">
            <c:forEach items="${headlines}" var="headline" varStatus="loop">
                <div class="item <c:if test="${loop.index==0}">active</c:if>">
                    <a
                    <c:choose>
                        <c:when test="${headline.type eq 0}">
                            href="<c:url value="/task/tid${headline.link}"/>"
                        </c:when>
                        <c:otherwise>
                            href="#"
                        </c:otherwise>
                    </c:choose>
                    >
                        <img class="carousel-inner-img" src="<c:url value="/static/img/upload/${headline.image}"/> " alt="${headline.title}">
                        <div class="carousel-caption">
                            <p>${headline.title}</p>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>

        <!-- Controls -->
        <a class="left carousel-control" href="#carousel-tasks" role="button" data-slide="prev">
            <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="right carousel-control" href="#carousel-tasks" role="button" data-slide="next">
            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
</header>

<main class="container" id="task-container">
    <div class="row" id="task-row">
        <c:forEach items="${tasks}" var="task">
            <a class="col-md-3 btn-task <c:if test="${task.taskInvalid}">task-invalid</c:if>"
               href="<c:url value="/task/tid${task.id}"/>" target="_blank" role="button">
                <div class="thumbnail thumbnail-task">
                    <div class="task-img-container">
                        <span class="helper"></span>
                    <c:choose>
                        <c:when test="${not empty task.image}">
                            <img src="<c:url value="/static/img/upload/task/${task.id}/${task.image}"/> " alt="${task.title}">
                        </c:when>
                        <c:otherwise>
                            <img src="<c:url value="/static/img/task-no-img.png"/> " alt="no-image">
                        </c:otherwise>
                    </c:choose>
                    </div>
                    <div class="caption">
                        <h4>${task.title}</h4>
                        <p>有效时间: <span class="valid-time">${task.durationStr}</span></p>
                        <p>参与人数: <span class="joined">${task.claimedCount}</span>/<span class="total_joined">${task.quota}</span> </p>
                        <p class="text-right task-type">${task.tag}</p>
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>
    <div class="row">
        <div class="col-md-12">
            <a class="btn btn-default btn-full" id="more-tasks" href="#" role="button">
                <span id="btn-more-icon" class="glyphicon glyphicon-chevron-down"></span>
            </a>
        </div>

    </div>
</main>

<script>
    var homeUrl = "<c:url value="/"/>";
</script>