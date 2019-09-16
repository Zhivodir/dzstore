<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>DZStore</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="css/myStyles.css">
    <link rel="stylesheet" href="adminLTE/css/adminLTE2.min.css">
    <link rel="stylesheet" href="adminLTE/css/skins/_all-skins.min.css">
    <%--<link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">--%>

    <script>window.jQuery || document.write('<script src="//ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"><\/script>')</script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Google Font -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>

<body class="hold-transition skin-blue sidebar-mini">
<sec:authorize access="isAuthenticated()">
    <div class="wrapper">

        <c:import url="elements/toppart.jsp"/>
        <c:import url="elements/leftSideBar.jsp"/>

        <div class="content-wrapper">
            <c:import url="elements/navpart.jsp"/>
            <c:import url="elements/contentspace.jsp"/>
        </div>
        <!-- /.content-wrapper -->
        <footer class="main-footer">
            <div class="pull-right hidden-xs">
                <b>Version</b> 2.4.13
            </div>
        </footer>
    </div>

    <c:import url="/WEB-INF/pages/modalForPages/operations/createNewFolder.jsp"/>
    <c:import url="/WEB-INF/pages/modalForPages/operations/fileUpload.jsp"/>
    <c:import url="/WEB-INF/pages/modalForPages/operations/folderUpload.jsp"/>
    <c:import url="/WEB-INF/pages/modalForPages/operations/newImageForProfile.jsp"/>
    <c:import url="/WEB-INF/pages/modalForPages/settings.jsp"/>

    <!-- jQuery UI 1.11.4 -->
    <%--<script src="bower_components/jquery-ui/jquery-ui.min.js"></script>--%>
    <!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
    <%--<script>--%>
    <%--$.widget.bridge('uibutton', $.ui.button);--%>
    <%--</script>--%>
    <script src="js/bootstrap.min.js"></script>
    <%--<script src="js/ie10-viewport-bug-workaround.js"></script>--%>
    <script src="js/contextMenu.js"></script>
    <script src="js/other_scripts.js"></script>
    <!-- Bootstrap WYSIHTML5 -->
    <%--<script src="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>--%>
    <!-- AdminLTE App -->
    <script src="adminLTE/js/adminlte.min.js"></script>
</sec:authorize>

</body>
</html>
