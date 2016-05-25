<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/25
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h2>支付信息变更</h2>
<div class="panel panel-default">
    <div class="panel-heading">支付宝绑定</div>
    <div class="panel-body">
        <div class="radio">
            <label>
                <form:radiobutton path="user.payMethod" value="alipay_email"/>
                <span class="glyphicon glyphicon-envelope">${user.email}</span>
            </label>
        </div>
        <div class="radio">
            <label>
                <form:radiobutton path="user.payMethod" value="alipay_phone" disabled="${(empty user.phoneNumber)? 'true':'false'}"/>
                <span class="glyphicon glyphicon-earphone">
                <c:choose>
                    <c:when test="${not empty user.phoneNumber}">${user.phoneNumber}</c:when>
                    <c:otherwise>未填写手机信息</c:otherwise>
                </c:choose>
                </span>
            </label>
        </div>
    </div>
    <div class="panel-footer">
        邮箱/手机信息与用户基本信息绑定
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading">储蓄卡绑定</div>
    <div class="panel-body">
        暂未开放
    </div>
</div>
<button id="btn_save" type="button" class="btn btn-primary pull-right">保存</button>