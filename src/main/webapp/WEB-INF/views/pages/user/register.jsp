
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/5
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:url value="/" var="home" scope="request"/>

<c:choose>
    <c:when test="${allowRegister}">
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
                        <div class="form-group info-input password-input-group">
                            <label for="inputPassword">密码</label>
                            <input type="password" class="form-control" id="inputPassword" name="password" placeholder="密码">
                        </div>
                        <div class="form-group info-input password-input-group" >
                            <label for="inputPasswordConfirm">密码确认</label>
                            <input type="password" class="form-control" id="inputPasswordConfirm" name="password_confirm" placeholder="密码确认">
                            <p class="err-message text-danger">两次输入的密码不一致.</p>
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
    </c:when>
    <c:otherwise>
        <div class="container">
            <h3>很抱歉，网站暂不开放新用户注册。</h3>
            <h3>We are sorry, user registration is temporarily closed.</h3>
        </div>
    </c:otherwise>
</c:choose>





<script src="<c:url value="/static/js/md5.js"/>"></script>
<script src="<c:url value="/static/js/register.js"/>"></script>
<script>
    var homeUrl = "<c:url value="/"/>";
</script>