<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Autorization</title>

  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
  <link href="css/registration.css" rel="stylesheet">
  <link href="css/login.css" rel="stylesheet">
</head>

<body>

<div class="container">
  <div class="card card-container">
    <p id="profile-name" class="profile-name-card">DZStore</p>
    <form class="form-signin" action="/j_spring_security_check" method="post">
      <span id="reauth-email" class="reauth-email"></span>
      <input type="text" id="inputEmail" class="form-control" name="j_username" placeholder="Email address" required autofocus value="Guest">
      <input type="password" id="inputPassword" class="form-control" name="j_password" placeholder="Password" required value="123">
      <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Sign in</button>
    </form>
    <a href="#" class="pull-left">Forgot the password?</a>
    <a href="/registration" class="pull-right">Registration</a>
  </div>
</div>

</body>
</html>
