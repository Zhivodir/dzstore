<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 05.04.2017
  Time: 23:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="top-part">
<div class="navbar navbar-inverse navbar-fixed-top contentmenu-place">
    <div class="col-sm-3 col-md-2">
        <div class="btn-group">
            <button type="button" class="btn btn-default newButton" data-toggle="dropdown">
                New <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                       data-target="#modalForNewFolder">New Folder</a></li>
                <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                       data-target="#modalForFileUpload">File upload</a></li>
                <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                       data-target="#modalForFolderUpload">Folder upload</a></li>
            </ul>
        </div>
    </div>
    <div class="col-sm-9 col-md-10">
        <div class="currentFolderPath">
            <a href="/index" class="levelPath">My store</a>
            <c:if test="${f ne null}">
                <c:forEach items="${listForRelativePath}" var="folder">
                    <span class="glyphicon glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                    <a href="/folder?f=${folder.id}" class="levelPath">${folder.name}</a>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>
</div>
