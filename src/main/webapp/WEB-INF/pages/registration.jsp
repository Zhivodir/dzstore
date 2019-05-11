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

  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <link href="css/login.css" rel="stylesheet">
  <script src="css/registration.css"></script>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
  <script src="//ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
  <script src="js/registration.js"></script>


</head>

<body>

<div class="container registration">
  <div class="card card-container vert-align">
    <p id="profile-name" class="profile-name-card">DZStore</p>
    <form class="form-signin" action="create_new_user" method="post">
      <input type="email" name="email" class="form-control mail" id="mail" placeholder="Email">
      <input type="text" name="login" class="form-control login" id="login" placeholder="Login">
      <input type="text" name="password" class="form-control password" id="password" placeholder="Password">
      <input type="text" name="conf_password" class="form-control conf_password" id="conf_password" placeholder="Confirm password">
      <button type="submit" class="btn btn-lg btn-primary btn-block">Confirm</button>
    </form>
  </div>
</div>

</body>
</html>
