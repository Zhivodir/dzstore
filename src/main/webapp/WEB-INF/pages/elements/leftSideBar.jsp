<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-sm-3 col-md-2 sidebar">
    <ul class="nav nav-sidebar">
        <c:choose>
            <c:when test="${typeOfView.equals('index')||typeOfView.equals('folder')}">
                <li><a href="/" class="href_for_sidebar active_menu_item"><span class="glyphicon glyphicon-home"></span> My store</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="/" class="href_for_sidebar"><span class="glyphicon glyphicon-home"></span> My store</a></li>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${typeOfView.equals('shared')}">
                <li><a href="/shared" class="href_for_sidebar active_menu_item"><span class="glyphicon glyphicon-eye-open"></span> Shared with me</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="/shared" class="href_for_sidebar"><span class="glyphicon glyphicon-eye-open"></span> Shared with me</a></li>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${typeOfView.equals('starred')}">
                <li><a href="/starred" class="href_for_sidebar active_menu_item"><span class="glyphicon glyphicon-star"></span> Starred</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="/starred" class="href_for_sidebar"><span class="glyphicon glyphicon-star"></span> Starred</a></li>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${typeOfView.equals('bin')}">
                <li><a href="/bin" class="href_for_sidebar active_menu_item"><span class="glyphicon glyphicon-trash"></span> Bin</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="/bin" class="href_for_sidebar"><span class="glyphicon glyphicon-trash"></span> Bin</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
    <div class = "busySpace" id="busySpace">${user.showBusySize[0]} / ${user.showBusySize[1]} </div>
</div>
