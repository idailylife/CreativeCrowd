<%@ taglib prefix="tile" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/24
  Time: 10:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
    <div class="row">
        <div class="col-sm-3">
            <!-- Left content-->
            <tile:insertAttribute name="left"/>
        </div>
        <div class="col-sm-9">
            <!-- Right content-->
            <tile:insertAttribute name="content"/>
        </div>
    </div>

</div>
<script>
    var homeUrl = "<c:url value="/"/>";
</script>