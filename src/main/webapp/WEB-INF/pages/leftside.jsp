<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu" data-widget="tree">
            <c:forEach items="${leftMenuPoints}" var="menuPoint">
                <li>
                    <a href="${menuPoint.path}">
                        <i class="fa ${menuPoint.icon}"></i> <span>${menuPoint.name}</span>
                        <span class="pull-right-container"></span>
                    </a>
                </li>
            </c:forEach>
            <li>
                <a href="javascript:void(0)">
                    <h4 class="control-sidebar-subheading">
                        <span id="busySpace">${busySpace}</span>
                        <span> / </span>
                        <span id="availableSpace">${availableSpace}</span>
                        <span class="label label-success pull-right">95%</span>
                    </h4>

                    <div class="progress progress-xxs">
                        <div id="busySpaceProgress" class="progress-bar progress-bar-success" style="width: 95%"></div>
                    </div>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>

<script>
    function showBusySpace() {
        $("#busySpaceProgress").width(busyMemoryPercent());
        $("#busySpace").text(formatSize(busySpace));
        $("#availableSpace").text(formatSize(availableSpace));
    }

    function busyMemoryPercent() {
        return Math.round(busySpace * 100 / availableSpace).toFixed(0)
    }
</script>