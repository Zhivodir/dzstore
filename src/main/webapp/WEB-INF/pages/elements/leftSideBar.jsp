<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-sm-3 col-md-2 sidebar">
  <ul class="nav nav-sidebar">
    <c:forEach items="${leftMenuPoints}" var="menuPoint">
      <li><a href="${menuPoint.path}" class="href_for_sidebar ${menuPoint.active ? "active_menu_item" : ""}"><span class="glyphicon ${menuPoint.icon}"></span> ${menuPoint.name}</a></li>
    </c:forEach>
  </ul>
  <div class="busySpace">${busySpace[0]} / ${busySpace[1]} </div>
</div>
