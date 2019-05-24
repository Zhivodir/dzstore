<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container-fluid">
    <div class="col-xs-3">
      <div class="navbar-header">
        <a class="navbar-brand" href="#">DZstore</a>
      </div>
    </div>
    <div class="col-xs-6 search_form">
      <div id="navbar" class="navbar-collapse collapse">
        <form action="/search" method="post" class="search-form">
          <div class="input-group">
            <input type="text" name="whatSearch" class="form-control search_field" placeholder="<s:message code="navbar.search"/>">
            <div class="input-group-btn">
              <button type="submit" name="search" id="searchButton" class="btn btn-warning btn-flat">
                <i class="fa fa-search"></i>
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
    <div class="col-xs-3">
      <ul class="nav navbar-nav navbar-right">
        <li>
          <a href="#" class="dropdown-toggle" data-toggle="modal" data-target="#settings">
            <i class="fa fa-gears"></i>
          </a>
        </li>

        <li>
          <button type="button" class="btn btn-lg account" data-toggle="dropdown">
            <sec:authentication property="principal.username"/><span class="caret"></span>
          </button>
          <div class="dropdown-menu dropdown-menu_profile" role="menu">
            <div class="row">
              <div class="col-md-6 account-right-part">
                <p><sec:authentication property="principal.username"/></p>
                <p><c:out value="${user.email}"></c:out></p>
                <a class="btn btn-lg btn-danger" href="/logout" role="button"><s:message code="navbar.logout"/></a>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</nav>
