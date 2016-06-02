<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/23
  Time: 18:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:choose>
    <c:when test="${empty refCode}">
        <h2>任务完成,即将跳转至主页...</h2>
        <p>任务酬金等进度请访问个人中心查看.</p>
    </c:when>
    <c:otherwise>
        <h2>Congratulation!</h2>
        <p>Your have successfully finished the task.</p>
        <p class="bg-primary">Please, copy and paste the following reference code back into the task page of MTurk.</p>
        <pre>${refCode}</pre>
    </c:otherwise>
</c:choose>