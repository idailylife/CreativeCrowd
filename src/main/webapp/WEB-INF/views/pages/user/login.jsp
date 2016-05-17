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

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h1>登录CreativeCrowd</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form id="loginForm">
                <div class="form-group info-input" id="email-group">
                    <label for="inputEmail">邮箱</label>
                    <input type="email" class="form-control" id="inputEmail" name="email" placeholder="Email">
                </div>
                <div class="form-group info-input">
                    <label for="inputPassword">密码</label>
                    <input type="password" class="form-control" id="inputPassword" name="password" placeholder="密码">
                </div>
                <div class="form-group" id="captcha-group">
                    <label for="inputCaptcha">验证码</label>
                    <div class="row">
                        <div class="col-md-6">
                            <input type="text" class="form-control" id="inputCaptcha" name="captcha" placeholder="验证码">
                        </div>
                        <div class="col-md-6">
                            <img id="img-captcha" src="<c:url value="/captcha"/>" style="height: 3rem">
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary" id="inputSubmit">登录</button>
            </form>
        </div>

    </div>
</div>



<script src="<c:url value="/static/js/md5.js"/>"></script>
<script src="<c:url value="/static/js/login.js"/>"></script>
<script>
    var homeUrl = "${home}";
    var nextUrl = "${nextUrl}";
</script>
