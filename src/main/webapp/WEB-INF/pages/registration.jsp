<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 09.02.2017
  Time: 10:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <link href="css/bootstrap.css" rel="stylesheet">

    <!-- Bootstrap core CSS -->
    <link  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<h1>Регистрация</h1>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <form action="create_new_user" method="post">
                <div class="form-group">
                    <label for="mail" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-10">
                        <input type="email" name="email" class="form-control" id="mail" placeholder="Email">
                    </div>
                </div>
                <div class="form-group">
                    <label for="mail" class="col-sm-2 control-label">Login</label>
                    <div class="col-sm-10">
                        <input type="text" name="login" class="form-control" id="login" placeholder="Login">
                    </div>
                </div>
                <div class="form-group">
                    <label for="mail" class="col-sm-2 control-label">Password</label>
                    <div class="col-sm-10">
                        <input type="text" name="password" class="form-control" id="password" placeholder="Password">
                    </div>
                </div>
                <div class="form-group">
                    <label for="mail" class="col-sm-2 control-label">Password</label>
                    <div class="col-sm-10">
                        <input type="text" name="conf_password" class="form-control" placeholder="Confirm password">
                    </div>
                </div>
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-success">Подтвердить</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
