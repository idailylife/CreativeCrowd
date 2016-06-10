<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/10
  Time: 12:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <h1>创建众包任务</h1>
            <form id="formBasicInfo">
                <div class="panel panel-primary">
                    <div class="panel-heading">基本信息</div>
                    <div class="panel-body">
                        <div class="form-group col-sm-12">
                            <label for="inputTitle">标题</label>
                            <input type="text" class="form-control" id="inputTitle" name="title" placeholder="输入标题">
                        </div>
                        <div class="form-group col-sm-6">
                            <label class="">任务类型</label>
                            <div class="">
                                <label class="checkbox-inline">
                                    <input type="radio" name="type" value="0" checked="checked">标准任务
                                </label>
                                <label class="checkbox-inline">
                                    <input type="radio" name="type" value="1">MTurk任务
                                </label>
                            </div>
                        </div>
                        <div class="form-group col-sm-6">
                            <label>允许同一用户多次申领</label>
                            <div class="">
                                <label class="checkbox-inline">
                                    <input type="radio" name="repeatable" value="0" >是
                                </label>
                                <label class="checkbox-inline">
                                    <input type="radio" name="repeatable" value="1" checked="checked">否
                                </label>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label for="inputImage">图片</label>
                            <input type="file" id="inputImage" name="image">
                            <p class="help-block">支持jpg,png,gif图片，文件最大4MB.</p>
                        </div>
                        <div class="form-group col-sm-12">
                            <label for="inputTag">分类Tag</label>
                            <input class="form-control" type="text" id="inputTag" name="tag" placeholder="多个分类以空格分隔, 不超过3类">
                        </div>

                    </div>
                </div>

                <div class="panel panel-primary">
                    <div class="panel-heading">流程控制</div>
                    <div class="panel-body">
                        <div class="form-group col-sm-6">
                            <label for="inputQuota">任务发放数量</label>
                            <div class="input-group">
                                <input type="number" name="quota" id="inputQuota" class="form-control">
                            </div>
                        </div>
                        <div class="form-group col-sm-6">
                            <label>任务限时</label>
                            <div>
                                <div class="input-group">
                                    <span class="input-group-addon" aria-label="set-time-limit">
                                        <input type="checkbox" id="checkTimeLimit">
                                    </span>
                                    <input type="number" class="form-control" name="timeLimit">
                                    <span class="input-group-addon">分钟</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-sm-6">
                            <label for="inputStartTime">任务开始时间</label>
                            <div class="input-group">
                                <input id="inputStartTime" class="form-control" type="datetime" name="endTime" placeholder="yyyy-mm-dd hh:ii">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="button">+</button>
                                </span>
                            </div>

                        </div>
                        <div class="form-group col-sm-6">
                            <label for="inputEndTime">任务终止时间</label>
                            <div class="input-group">
                                <input id="inputEndTime" class="form-control" type="datetime" name="endTime" placeholder="yyyy-mm-dd hh:ii">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="button">+</button>
                                </span>
                            </div>

                        </div>
                        <div class="form-group col-sm-12">
                            <label>Microtask(微任务)指派方式</label>
                            <ul class="nav nav-pills" role="tablist">
                                <li role="presentation" class="active">
                                    <a href="#tabSimple" aria-controls="simple" role="tab" data-toggle="tab">顺序</a>
                                </li>
                                <li role="presentation">
                                    <a href="#tabRandom" aria-controls="random" role="tab" data-toggle="tab">随机</a>
                                </li>
                                <li role="presentation">
                                    <a href="#tabDevelop" aria-controls="develop" role="tab" data-toggle="tab">Dev!</a>
                                </li>
                            </ul>
                            <ul class="tab-content">
                                <div role="tabpanel" class="tab-pane fade in active" id="tabSimple">
                                    <div class="row">
                                        <br>
                                        <p>将微任务按照设计顺序逐个呈现，用户需要完成所有的微任务才能提交。</p>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="tabRandom">
                                    <div class="row">
                                        <br>
                                        <p>将微任务随机分配给参与者，用户完成指定个数的微任务方可提交。</p>
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <label for="inputRandomParam">每个任务分配的微任务个数</label>
                                                <input type="number" class="form-control" id="inputRandomParam">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="tabDevelop">
                                    <br>
                                    <p>Dev!模式：自定义指派方案（待实现）</p>
                                </div>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="panel panel-primary">
                    <div class="panel-heading">酬金发放</div>
                    <div class="panel-body">
                        <div class="form-group col-sm-6">
                            <label for="inputWage">任务酬金</label>
                            <div class="input-group">
                                <span class="input-group-addon" >￥</span>
                                <input type="number" class="form-control" name="timeLimit" id="inputWage">
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <nav>
                <ul class="pager">
                    <li><a id="btnNext" href="#"><span class="glyphicon glyphicon-chevron-right"></span><b>下一步</b></a> </li>
                </ul>
            </nav>
        </div>
    </div>
</div>