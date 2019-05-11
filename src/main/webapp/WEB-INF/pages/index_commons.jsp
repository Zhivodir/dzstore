<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>DZstore</title>

  <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
  <link href="css/index.css" rel="stylesheet">
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <script>window.jQuery || document.write('<script src="//ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"><\/script>')</script>
</head>

<body>
<sec:authorize access="isAuthenticated()">
  <c:import url="elements/navbar.jsp"/>
  <c:import url="elements/contentMenu.jsp"/>

  <div class="container-fluid">
    <div class="row">
      <c:import url="elements/leftSideBar.jsp"/>
      <c:import url="elements/contentspace.jsp"/>
    </div>
  </div>

  <c:import url="/WEB-INF/pages/modalForPages/operations/createNewFolder.jsp"/>
  <c:import url="/WEB-INF/pages/modalForPages/operations/fileUpload.jsp"/>
  <c:import url="/WEB-INF/pages/modalForPages/operations/folderUpload.jsp"/>
  <c:import url="/WEB-INF/pages/modalForPages/operations/newImageForProfile.jsp"/>

  <script src="js/bootstrap.min.js"></script>
  <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
  <script src="js/holder.min.js"></script>
  <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  <script src="js/ie10-viewport-bug-workaround.js"></script>
  <script src="js/contextMenu.js"></script>
  <script src="js/other_scripts.js"></script>
</sec:authorize>

</body>
</html>