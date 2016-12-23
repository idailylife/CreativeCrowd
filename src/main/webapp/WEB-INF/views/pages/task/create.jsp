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
                <input type="hidden" name="ownerId" value="${uid}">
                <div class="panel panel-primary">
                    <div class="panel-heading">基本信息</div>
                    <div class="panel-body">
                        <div class="form-group col-sm-12">
                            <label for="inputTitle">标题</label>
                            <input type="text" class="form-control" id="inputTitle" name="title" placeholder="标题长度在10字符以上，100字以下">
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
                                    <input type="radio" name="repeatable" value="1" >是
                                </label>
                                <label class="checkbox-inline">
                                    <input type="radio" name="repeatable" value="0" checked="checked">否
                                </label>
                            </div>
                        </div>
                        <div class="form-group col-sm-12">
                            <label for="inputImage">图片</label>
                            <input type="file" id="inputImage" name="file">
                            <p class="help-block">支持jpg,png,gif图片，文件最大4MB.</p>
                        </div>
                        <div class="form-group col-sm-12">
                            <label for="inputTag">分类Tag</label>
                            <input class="form-control" type="text" id="inputTag" name="tag" placeholder="多个分类以空格分隔, 不超过3类">
                        </div>
                        <div class="form-group col-sm-12">
                            <label for="textDesc">任务简介</label>
                            <textarea id="textDesc" class="form-control" rows="3"></textarea>
                        </div>
                        <div class="form-group col-sm-12">
                            <label for="textDescDetail">任务内容</label>
                            <textarea id="textDescDetail" class="form-control" rows="5"></textarea>
                        </div>
                        <div class="form-group col-sm-12">
                            <label>其他信息(JSON String)</label>
                            <textarea id="textDescOther" class="form-control" rows="2">{}</textarea>
                        </div>
                    </div>
                    <input type="hidden" name="descJson" value="{}">
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
                                <input id="inputStartTime" class="form-control form_datetime" type="text" placeholder="yyyy/mm/dd hh:ii">
                                <input type="hidden" name="startTime">
                            </div>

                        </div>
                        <div class="form-group col-sm-6">
                            <label for="inputEndTime">任务终止时间</label>
                            <div class="input-group">
                                <input id="inputEndTime" class="form-control form_datetime" type="text" placeholder="yyyy/mm/dd hh:ii">
                                <input type="hidden" name="endTime">
                            </div>

                        </div>
                        <div class="form-group col-sm-12">
                            <label>Microtask(微任务)指派方式</label>
                            <ul class="nav nav-pills" role="tablist">
                                <li role="presentation" class="active">
                                    <a href="#tabSimple" aria-controls="simple" role="tab" data-toggle="tab" data-mode="0">单页</a>
                                </li>
                                <li role="presentation">
                                    <a href="#tabRandom" aria-controls="random" role="tab" data-toggle="tab" data-mode="1">随机</a>
                                </li>
                                <li role="presentation">
                                    <a href="#tabSequence" aria-controls="sequence" role="tab" data-toggle="tab" data-mode="2">顺序</a>
                                </li>
                                <li role="presentation">
                                    <a href="#tabSinglePagedRandom" aria-controls="singlePagedRandom" role="tab" data-toggle="tab" data-mode="3">GridSimilarity</a>
                                </li>
                                <li role="presentation">
                                    <a href="#tabDevelop" aria-controls="develop" role="tab" data-toggle="tab" data-mode="-1">Dev!</a>
                                </li>
                            </ul>
                            <ul class="tab-content">
                                <div role="tabpanel" class="tab-pane fade in active" id="tabSimple">
                                    <div class="row">
                                        <br>
                                        <p>只有一页的任务，适用于调查问卷</p>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="tabRandom">
                                    <div class="row">
                                        <br>
                                        <p>将微任务随机分配给参与者，用户完成指定个数的微任务方可提交。</p>
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <label for="inputRandomParam">每个参与者分配到的微任务个数(不超过微任务总数)</label>
                                                <input type="number" class="form-control" id="inputRandomParam">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="tabSequence">
                                    <div class="row">
                                        <br>
                                        <p>按照指定顺序推送Microtask</p>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="tabSinglePagedRandom">
                                    <div class="row">
                                        <br>
                                        <p>每次从N个备选项中选择K个与参考项最接近的。</p>
                                        <div class="form">
                                            <div class="form-group">
                                                <label for="inputSinglePagedRand_mtaskSize">一次任务的问题量</label>
                                                <input type="number" class="form-control" id="inputSinglePagedRand_mtaskSize">
                                            </div>
                                            <div class="form-group">
                                                <label for="inputSinglePagedRand_N">Grid 备选项数目N</label>
                                                <input type="number" class="form-control" id="inputSinglePagedRand_N">
                                            </div>
                                            <div class="form-group">
                                                <label for="inputSinglePagedRand_K">Grid 选择项数目K</label>
                                                <input type="number" class="form-control" id="inputSinglePagedRand_K">
                                            </div>
                                            <div class="form-group">
                                                <label for="inputSinglePagedRand_GoldenStd">启用黄金标准问题
                                                    <input type="checkbox" class="form-control" id="inputSinglePagedRand_GoldenStd">
                                                </label>
                                                <p class="help-block">在关联Excel文件的第二个Sheet配置</p>
                                            </div>
                                            <div class="form-group">
                                                <label for="inputSinglePagedRand_FreeChoice">启用FreeChoice
                                                    <input type="checkbox" class="form-control" id="inputSinglePagedRand_FreeChoice">
                                                </label>
                                                <p class="help-block">参与者可以选择>k个结果</p>
                                            </div>
                                            <div class="form-group" style="display: none">
                                                <label for="inputSinglePagedRand_nRows">显示行数</label>
                                                <input type="number" class="form-control" id="inputSinglePagedRand_nRows" value="1">
                                            </div>
                                            <div class="form-group">
                                                <label for="inputSinglePagedRand_config">关联文件上传</label>
                                                <input type="file" class="form-control" id="inputSinglePagedRand_config">
                                                <p class="help-block">上传文字描述与方案图片关联的Excel文件.</p>
                                                <p class="help-block" id="pSinglePagedRand_confState">尚未上传</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="tabDevelop">
                                    <br>
                                    <p>Dev!模式：自定义指派方案（Coming soon）</p>
                                </div>
                                <input type="hidden" id="hiddenParams" name="params">
                            </ul>
                        </div>
                        <div class="form-group col-sm-6">
                            <label for="inputWage">任务酬金(MTurk任务不填写)</label>
                            <div class="input-group">
                                <span class="input-group-addon" >￥</span>
                                <input type="number" class="form-control" name="wage" id="inputWage">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel panel-primary">
                    <div class="panel-heading">验证</div>
                    <div class="panel-body form-inline">
                        <div class="form-group col-sm-12" id="formGroupCaptcha">
                            <img id="img-captcha" src="<c:url value="/captcha"/>" style="height: 3rem">
                            <input class="form-control" id="inputCaptcha" name="captcha" placeholder="请输入验证码">
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

<script>
    var homeUrl = "${pageContext.request.contextPath}/";
</script>