<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-inverse navbar-fixed-top top1">
    <div class="container-fluid">
        <div class="col-xs-3">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">DZstore</a>
            </div>
        </div>
        <div class="col-xs-6 search_form">
            <div id="navbar" class="navbar-collapse collapse">
                <form class="navbar-form" action="/search" method="post">
                    <input type="text" name="whatSearch" class="search_field" placeholder="Search...">
                    <input type="submit" name="search" class="search_submit" value="Search"/>
                </form>
            </div>
        </div>
        <div class="col-xs-3">
            <ul class="nav navbar-nav navbar-right">
                <button type="button" class="btn btn-lg account" data-toggle="dropdown">
                    <sec:authentication property="principal.username"/><span class="caret"></span>
                </button>
                <div class="dropdown-menu dropdown-menu_profile" role="menu">
                    <div class="row">
                        <div class="col-md-6 account-right-part">
                            <p><sec:authentication property="principal.username"/></p>
                            <p><c:out value="${user.email}"></c:out></p>
                            <a class="btn btn-lg btn-danger" href="/logout" role="button">Logout</a>
                        </div>
                    </div>
                </div>
            </ul>
        </div>
    </div>
</nav>
