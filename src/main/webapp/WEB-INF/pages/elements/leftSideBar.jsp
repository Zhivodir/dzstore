<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-sm-3 col-md-2 sidebar">
  <ul class="nav nav-sidebar">
    <c:forEach items="${leftMenuPoints}" var="menuPoint">
      <li>
        <a href="${menuPoint.path}" class="href_for_sidebar ${menuPoint.active ? "active_menu_item" : ""}">
          <span class="glyphicon ${menuPoint.icon}"></span> ${menuPoint.name}
        </a>
      </li>
    </c:forEach>
  </ul>
  <div class="progress-group">
    <div class="progress sm" id="busySpaceDisplay">
      <div class="progress-bar progress-bar-aqua" id="busySpaceProgress" style="width: 0%"></div>
    </div>
  </div>
  <div class="busySpace">
    <span id="busySpace">${busySpace}</span>
    <span> / </span>
    <span id="availableSpace">${availableSpace}</span>
  </div>
</div>

<script>
  function showBusySpace(){
    $("#busySpaceProgress").width(busyMemoryPercent());
    $("#busySpace").text(formatSize(busySpace));
    $("#availableSpace").text(formatSize(availableSpace));
  }

  function busyMemoryPercent(){
    return Math.round(busySpace*100/availableSpace).toFixed(0)
  }
</script>