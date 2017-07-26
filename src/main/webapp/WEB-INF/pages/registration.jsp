<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 09.02.2017
  Time: 10:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Registration</title>
    <!-- Bootstrap core CSS -->
    <link  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <script src="css/registration.css"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>    <![endif]-->
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="js/registration.js"></script>

</head>

<body>

<div class="container-fluid registration" style="width: 300px;">
    <div class="row">
        <div class="col-md-12">
            <h2 style="text-align: center;">Registration</h2>
            <form action="create_new_user" method="post">
                <span hidden style='color: #ff0000'></span>
                <input type="email" name="email" class="form-control mail" id="mail" placeholder="Email">
                <input type="text" name="login" class="form-control login" id="login" placeholder="Login">
                <input type="text" name="password" class="form-control password" id="password" placeholder="Password">
                <input type="text" name="conf_password" class="form-control conf_password" id="conf_password" placeholder="Confirm password">
                <button type="submit"  class="btn btn-lg btn-primary btn-block">Confirm</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
