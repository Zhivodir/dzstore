<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Nuzhno proverit ne vliyaet li na security-->

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">DZstore</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Settings</a></li>
                <!-- sec:authentication -->
                <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal"
                       data-target="#modalForAccount"><sec:authentication property="principal.username"/></a></li>
                <li><a href="#">Help</a></li>
            </ul>
            <form class="navbar-form navbar-right" action="/search" method="post">
                <input type="text" name="whatSearch" class="form-control" placeholder="Search...">
                <input type="submit" name="search" value="Search"/>
            </form>
        </div>
    </div>
</nav>
