<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/9
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:url value="/" var="home" scope="request"/>
<html>
<head>
    <title>登录CreativeCrowd</title>
    <!-- Should be moved to common header, 需要换成相对协议-->
    <meta charset="UTF-8">
    <sec:csrfMetaTags/>
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="<c:url value="/css/login_styles.css"/>">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h1>登录CreativeCrowd</h1>
        </div>
    </div>
    <div class="row">
        <form id="registerForm">
            <div class="form-group">
                <label for="inputEmail">邮箱</label>
                <input type="email" class="form-control" id="inputEmail" name="email" placeholder="Email">
            </div>
            <div class="form-group">
                <label for="inputPassword">密码</label>
                <input type="password" class="form-control" id="inputPassword" name="password" placeholder="密码">
            </div>
            <button type="submit" class="btn btn-default">登录</button>
        </form>
    </div>
</div>


<!-- Should be moved to common header -->
<script src="http://cdn.bootcss.com/jquery/2.2.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="<c:url value="/js/md5.js"/>"></script>
<script src="<c:url value="/js/login.js"/>"></script>
<script>
    var homeUrl = ${home};
</script>
</body>
</html>
