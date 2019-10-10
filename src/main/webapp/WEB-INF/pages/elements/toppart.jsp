<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header class="main-header">
    <div class="logo">
        <span class="logo-mini"><b>DZS</b></span>
        <span class="logo-lg"><b>DZS</b>tore</span>
    </div>


    <nav class="navbar navbar-static-top">
        <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button" id="leftMenuSwitch">
            <span class="sr-only">Toggle navigation</span>
        </a>

        <div id="addContent" class="btn-group pull-left">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                <s:message code="contentmenu.new"/>
                <span class="caret"></span>
                <span class="sr-only">Toggle Dropdown</span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                       data-target="#modalForNewFolder"><s:message code="contentmenu.new.folder"/></a></li>
                <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                       data-target="#modalForFileUpload" onclick="prepareModalForUpload()"><s:message
                        code="contentmenu.upload.file"/></a></li>
                <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                       data-target="#modalForFolderUpload" onclick="prepareModalForUpload()"><s:message
                        code="contentmenu.upload.folder"/></a></li>
            </ul>
        </div>

        <form id="search-form" class="navbar-form navbar-left">
            <div class="input-group">
                <input type="text" name="whatSearch" id="search-input" class="form-control" placeholder="<s:message code="navbar.search"/>">
                <span class="input-group-btn">
                <button type="button" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
            </div>
        </form>


        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <%--<li class="dropdown messages-menu">--%>
                    <%--<a href="#" class="dropdown-toggle" data-toggle="dropdown">--%>
                        <%--<i class="fa fa-envelope-o"></i>--%>
                        <%--<span class="label label-success">4</span>--%>
                    <%--</a>--%>

                    <%--<ul class="dropdown-menu">--%>
                        <%--<li class="header">You have 4 messages</li>--%>
                        <%--<li>--%>
                            <%--<ul class="menu">--%>
                                <%--<li>--%>
                                    <%--<a href="#">--%>
                                        <%--<div class="pull-left">--%>

                                        <%--</div>--%>
                                        <%--<h4>--%>
                                            <%--Support Team--%>
                                            <%--<small><i class="fa fa-clock-o"></i> 5 mins</small>--%>
                                        <%--</h4>--%>
                                        <%--<p>Why not buy a new awesome theme?</p>--%>
                                    <%--</a>--%>
                                <%--</li>--%>
                            <%--</ul>--%>
                        <%--</li>--%>
                        <%--<li class="footer"><a href="#">See All Messages</a></li>--%>
                    <%--</ul>--%>
                <%--</li>--%>

                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <span class="hidden-xs"><sec:authentication property="principal.username"/></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="user-header" id="user-header-eddited">
                            <p>${user.email}</p>
                        </li>
                        <li class="user-footer">
                            <div class="pull-right">
                                <a href="/logout" class="btn btn-default btn-flat"><s:message code="navbar.logout"/></a>
                            </div>
                        </li>
                    </ul>
                </li>

                <li class="dropdown user-menu tasks-menu" >
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img src="img/lang/${user.language}.png" class="lang-icon">
                    </a>
                    <ul class="dropdown-menu lang_menu">
                        <li>
                            <a href="/settings/UA">
                                <img src="img/lang/UA.png" class="lang-icon">
                                <s:message code="language.UA"/>
                            </a>
                        </li>
                        <li>
                            <a href="/settings/EN">
                                <img src="img/lang/EN.png" class="lang-icon">
                                <s:message code="language.EN"/>
                            </a>
                        </li>
                        <li>
                            <a href="/settings/RU">
                                <img src="img/lang/RU.png" class="lang-icon">
                                <s:message code="language.RU"/>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>

<jsp:include page="/WEB-INF/pages/elements/i18nElements.jsp"/>
