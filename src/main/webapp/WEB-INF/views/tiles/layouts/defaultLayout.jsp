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
<html>
<head>
    <title><tiles:getAsString name="title"/></title>
    <meta charset="UTF-8">
    <sec:csrfMetaTags/>
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/static/css/defaultHeader-styles.css"/> ">
    <tiles:insertAttribute name="head"/>
</head>
<body>
    <header id="header">
        <tiles:insertAttribute name="header"/>
    </header>
    <script src="http://cdn.bootcss.com/jquery/2.2.1/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <section id="site-content" class="container">
        <tiles:insertAttribute name="body"/>
    </section>
    <footer id="footer" class="footer">
        <tiles:insertAttribute name="footer"/>
    </footer>

</body>
</html>
