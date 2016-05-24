<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/24
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h2>基本信息变更</h2>
<form:form id="formInfoEdit" method="post" class="form-horizontal" modelAttribute="user">
    <form:hidden path="id"/>
    <div class="form-group">
        <label class="col-sm-2 control-label">邮箱</label>
        <div class="col-sm-10">
            <label class="control-label">${user.email}</label>
        </div>
    </div>

    <spring:bind path="nickname">
        <div class="form-group ${status.error? 'has-error': ''}">
            <label class="col-sm-2 control-label">昵称</label>
            <div class="col-sm-10">
                <form:input path="nickname" type="text" class="form-control" id="inputNickname" placeholder="昵称"/>
                <form:errors path="nickname" class="control-label"/>
            </div>
        </div>
    </spring:bind>

    <spring:bind path="email">
        <div class="form-group ${status.error? 'has-error' : ''}">
            <label class="col-sm-2 control-label">性别</label>
            <div class="col-sm-10">
                <label class="radio-inline">
                    <form:radiobutton path="gender" value="M"/>男
                </label>
                <label class="radio-inline">
                    <form:radiobutton path="gender" value="F"/>女
                </label>
            </div>
        </div>
    </spring:bind>

    <spring:bind path="age">
        <div class="form-group ${status.error? 'has-error' : ''}">
            <label class="col-sm-2 control-label" for="inputAge">年龄</label>
            <div class="col-sm-10">
                <form:input id="inputAge" path="age" type="number" class="form-control" placeholder="年龄"/>
                <form:errors path="age" class="control-label"/>
            </div>
        </div>
    </spring:bind>

    <spring:bind path="phoneNumber">
        <div class="form-group ${status.error? 'has-error' : ''}">
            <label class="col-sm-2 control-label" for="inputPhoneNumber">手机</label>
            <div class="col-sm-10">
                <form:input id="inputPhoneNumber" path="phoneNumber" type="number" class="form-control" placeholder="手机号码"/>
                <form:errors path="phoneNumber" class="control-label"/>
            </div>
        </div>
    </spring:bind>

    <input id="btn_save" type="submit" class="btn btn-primary pull-right" value="保存">

</form:form>