<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/16
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<c:url value="/"/> ">${isMTurkTask? 'CreativeCrowd for MTurk':'创意众包平台'}</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <c:choose>
                    <c:when test="${loginState}">
                        <li><a id="li_user_center" href="<c:url value="/user"/>">${displayName}</a></li>
                        <li><a id="logout" href="<c:url value="/user/logout"/> ">注销</a></li>
                    </c:when>
                    <c:when test="${isMTurkTask}">
                        <c:if test="${not empty mturkId}">
                            <li><a href="#">MTurk ID: ${mturkId}</a> </li>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <li><a id="signup" href="<c:url value="/user/register"/> ">注册</a></li>
                        <li><a id="login" href="<c:url value="/user/login?next=${requestScope['javax.servlet.forward.request_uri']}"/> ">登录</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>