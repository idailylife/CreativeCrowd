<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/24
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="nav nav-pills nav-stacked">
    <p></p>
    <li <c:if test="${not empty sel_main}">class="active"</c:if>>
        <a href="<c:url value="/user"/>">信息概览</a>
    </li>
    <li <c:if test="${not empty sel_task}">class="active"</c:if>>
        <a href="#">我的任务</a>
    </li>
    <li <c:if test="${not empty sel_pswd}">
        class="active"</c:if>><a href="#">密码变更</a>
    </li>
    <li <c:if test="${not empty sel_info}">class="active"</c:if>>
        <a href="<c:url value="/user/edit/info"/>">信息变更</a>
    </li>
    <li <c:if test="${not empty sel_pay}">class="active"</c:if>>
        <a href="<c:url value="/user/edit/payment"/>">支付相关</a>
    </li>
</ul>
