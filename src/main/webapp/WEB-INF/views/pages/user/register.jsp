<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/5
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:url value="/" var="home" scope="request"/>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h1>欢迎加入CreativeCrowd</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form id="registerForm">
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
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="inputAgreeCheck"> 我已经认真阅读并同意CreativeCrowd的《
                            <a href="<c:url value="/agreement"/>" target="_blank">使用协议</a>
                        》.
                    </label>
                </div>
                <button type="submit" class="btn btn-primary" id="inputSubmit">注册</button>
            </form>
        </div>
    </div>
</div>



<script src="<c:url value="/static/js/md5.js"/>"></script>
<script src="<c:url value="/static/js/register.js"/>"></script>
<script>
    var homeUrl = "<c:url value="/"/>";
</script>