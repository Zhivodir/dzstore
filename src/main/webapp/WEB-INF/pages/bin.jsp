<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 24.04.2017
  Time: 18:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>DZstore</title>

    <!-- Bootstrap core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/index.css" rel="stylesheet">
    <link href="css/forContextMenu.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<c:import url="elements/navbar.jsp"/>
<c:import url="elements/contentMenu.jsp"/>

<div class="container-fluid">
    <div class="row">
        <c:set var="content" value="${content}" scope="request"/>
        <c:set var="f" value="-1" scope="request"/>
        <c:set var="typeOfView" value="bin" scope="request"/>
        <c:import url="elements/leftSideBar.jsp"/>
        <c:import url="elements/contentspace.jsp"/>
    </div>
</div>

<c:set var="f" value="${f}" scope="request"/>
<c:set var="typeOfView" value="index" scope="request"/>
<c:import url="/WEB-INF/pages/modalForPages/createNewFolder.jsp"/>
<c:import url="/WEB-INF/pages/modalForPages/fileUpload.jsp"/>
<c:import url="/WEB-INF/pages/modalForPages/createNewFolder.jsp"/>
<c:import url="/WEB-INF/pages/modalForPages/newImageForProfile.jsp"/>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="//ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"><\/script>')</script>
<script src="js/bootstrap.min.js"></script>
<!-- Just to make our placeholder images work. Don't actually copy the next line! -->
<script src="js/holder.min.js"></script>
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="js/ie10-viewport-bug-workaround.js"></script>
<script src="js/contextMenu.js"></script>
<script>
    $(document).ready(function () {
        $('.record_table tr').click(function (event) {
            if (event.target.type !== 'checkbox') {
                $(':checkbox', this).trigger('click');
            }
        });
    });</script>

</body>
</html>
