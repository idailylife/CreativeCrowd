<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/16
  Time: 14:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container" id="question_container">
    <div class="row">
        <div class="col-md-12">
            <h1>${task.title}</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            ${htmlStr}
        </div>
        <div class="col-md-4">

        </div>
    </div>

</div>
