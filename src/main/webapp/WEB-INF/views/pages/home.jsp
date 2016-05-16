<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/5
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="jumbotron ">
    <div id="carousel-tasks" class="carousel slide" data-ride="carousel">
        <!-- Indicators -->
        <ol class="carousel-indicators">
            <li data-target="#carousel-tasks" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-tasks" data-slide-to="1" ></li>
        </ol>

        <!-- Wrapper for slides -->
        <div class="carousel-inner" role="listbox">
            <div class="item active">
                <img src="http://www.baidu.com/img/bd_logo1.png" alt="baidu logo">
                <div class="carousel-caption">
                    <p>Baidu Logo</p>
                </div>
            </div>
            <div class="item">
                <img src="http://www.baidu.com/img/bd_logo1.png" alt="baidu logo2">
                <div class="carousel-caption">
                    <p>Baidu Logo 2</p>
                </div>
            </div>
        </div>

        <!-- Controls -->
        <a class="left carousel-control" href="#carousel-tasks" role="button" data-slide="prev">
            <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="right carousel-control" href="#carousel-tasks" role="button" data-slide="next">
            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
</header>

<main class="container">
    <div class="row">
        <a class="col-md-3 btn-task" href="#blabla" role="button">
            <div class="thumbnail thumbnail-task">
                <img src="http://www.baidu.com/img/bd_logo1.png" alt="yet another baidu logo">
                <div class="caption">
                    <h4>灯具的创意设计</h4>
                    <p>有效时间: <span class="valid-time">4/20-4/25</span></p>
                    <p>参与人数: <span class="joined">20</span>/<span class="total_joined">80</span> </p>
                    <p class="text-right task-type">创意任务</p>
                </div>
            </div>
        </a>

        <a class="col-md-3 btn-task" href="#blabla" role="button">
            <div class="thumbnail thumbnail-task">
                <img src="http://www.baidu.com/img/bd_logo1.png" alt="yet another baidu logo">
                <div class="caption">
                    <h4>灯具的创意设计</h4>
                    <p>有效时间: <span class="valid-time">4/20-4/25</span></p>
                    <p>参与人数: <span class="joined">20</span>/<span class="total_joined">80</span> </p>
                    <p class="text-right task-type">创意任务</p>
                </div>
            </div>
        </a>

        <a class="col-md-3 btn-task" href="#blabla" role="button">
            <div class="thumbnail thumbnail-task">
                <img src="http://www.baidu.com/img/bd_logo1.png" alt="yet another baidu logo">
                <div class="caption">
                    <h4>灯具的创意设计</h4>
                    <p>有效时间: <span class="valid-time">4/20-4/25</span></p>
                    <p>参与人数: <span class="joined">20</span>/<span class="total_joined">80</span> </p>
                    <p class="text-right task-type">创意任务</p>
                </div>
            </div>
        </a>

        <a class="col-md-3 btn-task task-invalid" href="#blabla" role="button">
            <div class="thumbnail thumbnail-task">
                <img src="http://www.baidu.com/img/bd_logo1.png" alt="yet another baidu logo">
                <div class="caption">
                    <h4>灯具的创意设计</h4>
                    <p>有效时间: <span class="valid-time">4/20-4/25</span></p>
                    <p>参与人数: <span class="joined">20</span>/<span class="total_joined">80</span> </p>
                    <p class="text-right task-type">创意任务</p>
                </div>
            </div>
        </a>
    </div>
</main>