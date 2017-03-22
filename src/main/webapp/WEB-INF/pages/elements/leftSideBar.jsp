<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-sm-3 col-md-2 sidebar">
    <div class="btn-group">
        <button type="button" class="btn btn-default tyopdown-toggle" data-toggle="dropdown">
            New <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu">
            <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#modalForNewFolder">New
                Folder</a></li>
            <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal"
                   data-target="#modalForFileUpload">File upload</a></li>
            <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal"
                   data-target="#modalForFolderUpload">Folder upload</a></li>
        </ul>
    </div>

    <ul class="nav nav-sidebar">
        <li><a href="/index">My store</a></li>
        <li><a href="#">Shared with me</a></li>
        <li><a href="/starred">Starred</a></li>
    </ul>
</div>