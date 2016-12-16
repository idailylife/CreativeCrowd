<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/10
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
    <div class="row">
        <div class="col-md-8">
            <h2>${title}</h2>
            <p>${desc}</p>
            <c:if test="${not empty image}">
                <img class="desc-img" src="<s:url value="/static/img/upload/task/${task.id}/${image}"/>">
            </c:if>
            <p>${descDetail}</p>
            <c:choose>
                <c:when test="${task.type == 0}">
                    <!-- Normal task -->
                    <c:choose>
                        <c:when test="${taskState eq 'EXPIRED'}">
                            <button class="btn btn-default" id="btn-join" disabled="disabled" type="button" data-state="expired">已结束</button>
                        </c:when>
                        <c:when test="${taskState eq 'FINISHED'}">
                            <button class="btn btn-default" id="btn-join" disabled="disabled" type="button" data-state="finished">已参与</button>
                        </c:when>
                        <c:when test="${taskState eq 'CLAIMED'}">
                            <button class="btn btn-info" id="btn-join" type="button" data-state="claimed">继续先前任务</button>
                        </c:when>
                        <c:when test="${taskState eq 'JOINABLE'}">
                            <button class="btn btn-info" id="btn-join" type="button" data-state="joinable">参与</button>
                        </c:when>
                        <c:when test="${taskState eq 'NEED_LOGIN'}">
                            <button class="btn btn-warning" id="btn-join" type="button" data-state="need_login">请先登录</button>
                        </c:when>
                    </c:choose>
                </c:when>
                <c:when test="${task.type == 1}">
                    <!-- MTurk task -->

                    <div class="form-group" id="group-mt-id">
                        <label for="inputMTurkId">MTurk ID</label>
                        <input type="text" class="form-control" id="inputMTurkId">
                    </div>
                    <div class="form-group" id="group-mt-captcha">
                        <label for="inputCaptcha">Verify Code</label>
                        <div class="form-inline">
                            <input type="text" class="form-control" id="inputCaptcha">
                            <img id="img-captcha" src="<c:url value="/captcha"/>" style="height: 3rem">
                        </div>
                    </div>
                    <div class="form-group">
                        <div id="alert-info" class="alert alert-danger" role="alert"></div>
                    </div>
                    <c:choose>
                        <c:when test="${taskState eq 'EXPIRED'}">
                            <div class="alert alert-warning" role="alert">Task is expired or fully claimed. You may check your reference code if you finished this task.</div>
                            <div class="form-group">
                                <button type="button" class="btn btn-default" id="btn-mt-chk-status">Check Reference Code</button>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="form-group">
                                <button type="button" class="btn btn-default" id="btn-mt-chk-status">Check Validity</button>
                                <button type="button" class="btn btn-success" id="btn-mt-resume">Resume Task</button>
                                <button type="button" class="btn btn-success" id="btn-mt-join">Start Task</button>
                            </div>
                        </c:otherwise>
                    </c:choose>


                </c:when>
            </c:choose>


            <input type="hidden" id="val-tid" value="${tid}"/>
        </div>
        <div class="col-md-4">
            <div id="info-container">
                <c:choose>
                    <c:when test="${task.type == 0}">
                        <h4>任务相关信息</h4>
                        <ul>
                            <li>有效期: ${startEndTime}</li>
                            <li>已参与人数: ${task.finishedCount}/${task.quota}</li>
                            <li>可重复参与: ${(task.repeatable == 1)?'是':'否'}</li>
                            <c:if test="${not empty estTime}">
                                <li>预期任务时长: ${estTime}</li>
                            </c:if>
                            <c:forEach var="info" items="${infoMap}">
                                <li>
                                        ${info.key}:&nbsp;${info.value}
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:when test="${task.type == 1}">
                        <h4>Related information:</h4>
                        <ul>
                            <li>Valid on: ${startEndTime}</li>
                            <li>Claimed: ${task.claimedCount}/${task.quota}</li>
                            <li>Finished: ${task.finishedCount}/${task.quota}</li>
                            <li>${(task.repeatable == 1)?'Can be joined more than once':'Claimable for only once'}</li>
                            <c:if test="${not empty estTime}">
                                <li>Est. duration: ${estTime}</li>
                            </c:if>
                            <c:forEach var="info" items="${infoMap}">
                                <li>
                                        ${info.key}:&nbsp;${info.value}
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                </c:choose>

            </div>
        </div>
    </div>
</div>
<script>
    var homeUrl = "<c:url value="/"/>";
</script>