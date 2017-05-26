<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Nuzhno proverit ne vliyaet li na security-->

<nav class="navbar navbar-inverse navbar-fixed-top top1">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">DZstore</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">

            <ul class="nav navbar-nav navbar-right">
                <button type="button" class="btn btn-lg account" data-toggle="dropdown">
                    <sec:authentication property="principal.username"/><span class="caret"></span>
                </button>
                <div class="dropdown-menu dropdown-menu_profile" role="menu">
                    <div class="row">
                        <div class="col-md-3 account-left-part">
                            <a href="#" data-toggle="modal" data-target="#modalForImageProfileUpload" role="button">
                                <div class="profile-header-img">
                                    <c:if test="${user.avatar eq false}">
                                        <img class="img-circle" src="img/default.jpg" alt="photo"/>
                                    </c:if>
                                    <c:if test="${user.avatar eq true}">
                                        <img class="img-circle" src="<c:url value="ava/${user.login}.jpg"/>" alt="photo"/>
                                    </c:if>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-6 account-right-part">
                            <p><sec:authentication property="principal.username"/></p>
                            <p><c:out value="${user.email}"></c:out></p>
                            <a class="btn btn-lg btn-danger" href="/logout" role="button">Logout</a>
                        </div>
                    </div>
                </div>
            </ul>

            <form class="navbar-form navbar-right" action="/search" method="post">
                <input type="text" name="whatSearch" class="form-control" placeholder="Search...">
                <input type="submit" name="search" value="Search"/>
            </form>
        </div>
    </div>
</nav>
