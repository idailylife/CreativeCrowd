<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/27
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table class="table table-hover">
    <thead>
        <tr>
            <th>ID</th>
            <th>任务名称</th>
            <th>任务状态</th>
            <th>酬金</th>
            <th>酬金状态</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${userTasks}" var="userTask">
            <c:choose>
                <c:when test="${userTask.state == 0}">
                    <%-- Claimed--%>
                    <tr class="success">
                        <c:set scope="page" var="taskStateStr" value="已申领"/>
                </c:when>
                <c:when test="${userTask.state == 1}">
                    <%-- Finished--%>
                    <tr>
                    <c:set scope="page" var="taskStateStr" value="已完成"/>
                </c:when>
                <c:when test="${userTask.state == 2}">
                    <%-- Expired--%>
                    <tr class="active">
                    <c:set scope="page" var="taskStateStr" value="已失效"/>
                </c:when>
            </c:choose>
                        <td>
                            ${userTask.id}
                        </td>
                        <td>
                            ${mappedTasks[userTask.taskId].title}
                        </td>
                        <td>
                            ${taskStateStr}
                        </td>
                        <td>
                            ${userTask.remuneration}
                        </td>
                        <td>
                            ${(empty userTask.transactionId)? '未发放':'已发放'}
                        </td>
                        <td>
                            <a href="<c:url value="/task/tid"/>${userTask.taskId}" target="_blank">查看任务</a>
                        </td>
                    </tr>
        </c:forEach>
    </tbody>
</table>