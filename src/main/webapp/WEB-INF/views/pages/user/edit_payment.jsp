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
    <div class="panel-heading">通过支付宝支付酬金</div>
    <div class="panel-body form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label">绑定到账户</label>
            <div class="col-sm-10">
                <div class="radio">
                    <label>
                        <form:radiobutton path="user.payMethod" value="alipay_email"/>
                        <span class="glyphicon glyphicon-envelope"> ${user.email}</span>
                    </label>
                </div>
                <div class="radio">
                    <label>
                        <form:radiobutton path="user.payMethod" value="alipay_phone" disabled="${(empty user.phoneNumber)? 'true':'false'}"/>
                        <span class="glyphicon glyphicon-earphone"> ${(empty user.phoneNumber)? '未填写':user.phoneNumber}</span>
                    </label>
                </div>
                <div class="row col-sm-12">
                    <div class="radio">
                        <label>
                            <form:radiobutton path="user.payMethod" value="alipay_other" disabled="${(empty user.phoneNumber)? 'true':'false'}"/>
                            <span class="glyphicon glyphicon-asterisk"> 其他账户</span>
                        </label>
                    </div>
                    <form:input path="user.payAccount" class="form-control"
                                disabled="${(user.payMethod eq 'alipay_other')? 'false':'true'}"/>

                </div>

            </div>
        </div>
    </div>
    <div class="panel-footer">
        邮箱不可更改，手机信息请在<a href="<c:url value="/user/edit/info"/>">信息变更</a>中修改。
    </div>
</div>

<button id="btn_save" type="button" class="btn btn-primary pull-right">保存</button>