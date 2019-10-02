<%@ page import="com.gmail.dzhivchik.domain.enums.Language" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title><s:message code="registration.title"/></title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

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
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>

<body class="hold-transition register-page">
<div class="register-box">
    <div class="register-logo">
        <a href="#"><b>DZ</b>Store</a>
    </div>

    <div class="register-box-body">
        <p class="login-box-msg"><s:message code="registration.form.description"/></p>

        <form action="/createNewUser" method="post" id="createUser">
            <div class="form-group has-feedback">
                <input type="text" class="form-control login" name="login" id="login"
                       placeholder="<s:message code="registration.placeholder.login"/>">
                <span class="glyphicon glyphicon-user form-control-feedback"></span>
            </div>
            <div class="alert alert-danger alert-dismissible"  id="loginError" hidden>
                <i class="icon fa fa-ban"></i><s:message code="registration.error.incorrect.login"/>
            </div>

            <div class="form-group has-feedback">
                <input type="text" class="form-control mail" name="email" id="mail"
                       placeholder="<s:message code="registration.placeholder.email"/>">
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="alert alert-danger alert-dismissible"  id="emailError" hidden>
                <i class="icon fa fa-ban"></i><s:message code="registration.error.incorrect.email"/>
            </div>

            <div class="form-group has-feedback">
                <input type="password" class="form-control password" name="password" id="password"
                       placeholder="<s:message code="registration.placeholder.password"/>">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="alert alert-danger alert-dismissible"  id="pswdError" hidden>
                <i class="icon fa fa-ban"></i><s:message code="registration.error.incorrect.pswd"/>
            </div>

            <div class="form-group has-feedback">
                <select name="language" class="form-control" id="language" style="margin-bottom: 10px;">">
                    <c:forEach items="<%=Language.values()%>" var="language">
                        <option value="${language}"><s:message code="language.${language}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <a href="login.html" class="text-center"><s:message code="registration.already"/></a>
                </div>
                <!-- /.col -->
                <div class="col-xs-6">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">
                        <s:message code="registration.button.register"/></button>
                </div>
                <!-- /.col -->
            </div>
        </form>


    </div>
    <!-- /.form-box -->
</div>
<!-- /.register-box -->

<script src="panel/bower_components/jquery/dist/jquery.min.js"></script>
<script src="panel/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="panel/plugins/iCheck/icheck.min.js"></script>

<script>
    var nameOfLastField;
    var valueOfLastField;
    var emailPattern = /^[a-z0-9_-]+@[a-z0-9-]+\.[a-z]{2,6}$/i;

    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' /* optional */
        });
    });

    $('#createUser').submit(function (e) {
        e.preventDefault();
        $("#emailError").prop("hidden", $("#mail").val().search(emailPattern) == 0);
        $("#pswdError").prop("hidden", $("#password").val().length > 6);
        $("#loginError").prop("hidden", $("#login").val().length > 0);
        var hiddenAlertsQuantity = $(".alert-danger:hidden").length;
        if(hiddenAlertsQuantity == 3) {
            $('#createUser').submit();
        }
    });

    $(document).ready(function () {
        $("input.form-control").focusout(function () {
            nameOfLastField = whatFieldInFocus($(this));
            valueOfLastField = document.getElementById(nameOfLastField).value;

            if (nameOfLastField == "mail") {
                $("#emailError").prop("hidden", valueOfLastField.search(emailPattern) == 0);
            } else if (nameOfLastField == "password") {
                $("#pswdError").prop("hidden", valueOfLastField.length > 6);
            } else if (nameOfLastField == 'login') {
                $("#loginError").prop("hidden", valueOfLastField.length > 0);
            }
        });

        function whatFieldInFocus(field) {
            var fieldsName = "";
            if (field.hasClass("mail")) {
                fieldsName = "mail";
            } else if (field.hasClass("login")) {
                fieldsName = "login";
            } else if (field.hasClass("password")) {
                fieldsName = "password";
            }
            return fieldsName;
        }
    });
</script>

</body>
</html>
