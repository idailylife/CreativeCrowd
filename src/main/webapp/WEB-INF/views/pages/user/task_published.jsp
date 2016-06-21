<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/6/21
  Time: 12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table class="table table-hover table-condensed">
    <thead>
    <tr>
        <th>ID</th>
        <th>任务名称</th>
        <th>任务类型</th>
        <th>配额</th>
        <th>任务进度</th>
        <th>酬金</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${taskList}" var="task">
        <tr>
            <td>${task.id}</td>
            <td>${task.title}</td>
            <td>${task.typeName}</td>
            <td>${task.quota}</td>
            <td>申领:${task.claimedCount},完成${task.finishedCount}</td>
            <td>${task.wage}</td>
            <td>
                <a href="<c:url value="/task/edit/${task.id}"/> " target="_blank">编辑微任务</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>