<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Log in</title>

    <link href="panel/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="panel/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="panel/bower_components/Ionicons/css/ionicons.min.css" rel="stylesheet">
    <link href="panel/dist/css/AdminLTE.min.css" rel="stylesheet">
    <link href="panel/plugins/iCheck/square/blue.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Google Font -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a href="#"><b>DZ</b>Store</a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
        <p class="login-box-msg"><s:message code="login.form.description"/></p>

        <form action="/j_spring_security_check" method="post">
            <div class="alert alert-danger alert-dismissible"  id="logInError" hidden>
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <i class="icon fa fa-ban"></i> <s:message code="login.error"/>
            </div>
            <div class="form-group has-feedback">
                <input type="text" class="form-control" id="inputEmail" name="j_username" required autofocus value="Guest"
                       placeholder="<s:message code="login.placeholder.email"/>">
                <span class="glyphicon glyphicon-user form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" class="form-control" id="inputPassword" name="j_password" required value="1234567"
                       placeholder="<s:message code="login.placeholder.password"/>">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <a href="/registration" class="text-center"><s:message code="login.registration"/></a>
                </div>
                <!-- /.col -->
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat"><s:message code="login.button.sign.in"/></button>
                </div>
                <!-- /.col -->
            </div>
        </form>

    </div>
    <!-- /.login-box-body -->
</div>
<!-- /.login-box -->

<script src="panel/bower_components/jquery/dist/jquery.min.js"></script>
<script src="panel/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="panel/plugins/iCheck/icheck.min.js"></script>
<script>
    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' /* optional */
        });
    });

    $(document).ready(function () {
        if(location.toString().indexOf('error') !== -1) {
           $("#logInError").prop("hidden", false);
        }
    });
</script>
</body>
</html>
