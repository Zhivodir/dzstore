<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu" data-widget="tree">
            <c:forEach items="${leftMenuPoints}" var="menuPoint">
                <li ${menuPoint.active ? 'class="active"' : ""}>
                        <a href="#" class="purposeOfTransit" data-view-type="${menuPoint.path.replace("/", "")}">

                        <i class="fa ${menuPoint.icon}"></i> <span>${menuPoint.name}</span>
                        <span class="pull-right-container"></span>
                    </a>
                </li>
            </c:forEach>
            <li>
                <a href="javascript:void(0)">
                    <h4 class="control-sidebar-subheading">
                        <span class="visibleState" id="busySpace">${busySpace}</span>
                        <span class="visibleState"> / </span>
                        <span class="visibleState" id="availableSpace">${availableSpace}</span>
                        <span class="label label-success pull-right" id="busyMemoryPercent"></span>
                    </h4>

                    <div class="progress progress-xxs">
                        <div id="busySpaceProgress" class="progress-bar progress-bar-success" style="width: 0%"></div>
                    </div>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>

<script>
    $(document).ready(function () {
        $("#busyMemoryPercent").append(Math.floor(busySpace/availableSpace * 100) + "%");
    });

    function showBusySpace() {
        $("#busySpaceProgress").width(busyMemoryPercent());
        $("#busySpace").text(formatSize(busySpace));
        $("#availableSpace").text(formatSize(availableSpace));
    }

    function busyMemoryPercent() {
        return Math.round(busySpace * 100 / availableSpace).toFixed(0)
    }
</script>