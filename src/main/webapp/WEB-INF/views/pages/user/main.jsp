<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/25
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<p></p>
<div class="panel panel-primary">
    <div class="panel-heading">
        基本信息
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-3">邮箱</div>
            <div class="col-sm-9">${user.email}</div>
        </div>
        <div class="row">
            <div class="col-sm-3">昵称</div>
            <div class="col-sm-9">${(empty user.nickname)? '-':user.nickname}</div>
        </div>
        <div class="row">
            <div class="col-sm-3">性别</div>
            <div class="col-sm-9">
                <c:choose>
                    <c:when test="${empty user.gender}">
                        -
                    </c:when>
                    <c:when test="${user.gender eq 'M'}">
                        男
                    </c:when>
                    <c:when test="${user.gender eq 'F'}">
                        女
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-3">手机</div>
            <div class="col-sm-9">${(empty user.phoneNumber)? '-':user.phoneNumber}</div>
        </div>
        <div class="row">
            <div class="col-sm-3">年龄</div>
            <div class="col-sm-9">${(empty user.age)? '-':user.age}</div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading">
        支付信息
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-3">绑定状态</div>
            <div class="col-sm-9">
                <c:choose>
                    <c:when test="${empty user.payMethod}">
                        <span class="text-danger">未绑定</span>
                    </c:when>
                    <c:when test="${user.payMethod eq 'alipay_phone'}">
                        支付宝-手机号码
                    </c:when>
                    <c:when test="${user.payMethod eq 'alipay_email'}">
                        支付宝-邮箱
                    </c:when>
                    <c:otherwise>
                        其他方式
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading">
        任务信息
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-3">申领数量</div>
            <div class="col-sm-9">${claimedCount}</div>
        </div>
        <div class="row">
            <div class="col-sm-3">完成数量</div>
            <div class="col-sm-9">${finishedCount}</div>
        </div>
        <div class="row">
            <div class="col-sm-3">Accept Rate</div>
            <div class="col-sm-9">${user.acceptRate}</div>
        </div>
    </div>
</div>