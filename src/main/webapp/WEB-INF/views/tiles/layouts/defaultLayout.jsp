<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/16
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<tiles:importAttribute name="javascripts" ignore="true"/>
<tiles:importAttribute name="stylesheets" ignore="true"/>

<html>
<head>
    <title><tiles:getAsString name="title"/></title>
    <meta charset="UTF-8">
    <sec:csrfMetaTags/>
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <%--<link rel="stylesheet" href="<c:url value="/static/css/defaultHeader-styles.css"/> ">--%>
    <!-- Auto appended stylesheets -->
    <c:forEach var="css" items="${stylesheets}">
        <link rel="stylesheet" type="text/css" href="<c:url value="${css}"/>">
    </c:forEach>
    <!-- End of stylesheets -->
</head>
<body>
    <header id="header">
        <tiles:insertAttribute name="header"/>
    </header>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <section id="site-content">
        <tiles:insertAttribute name="body"/>
    </section>
    <footer id="footer" class="footer">
        <tiles:insertAttribute name="footer"/>
    </footer>

    <!-- scripts -->
    <c:forEach var="script" items="${javascripts}">
        <script src="<c:url value="${script}"/> "></script>
    </c:forEach>
    <!-- end of scripts -->

</body>
</html>
