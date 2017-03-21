<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 15.03.2017
  Time: 16:54
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
    <link  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/index.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">DZstore</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Settings</a></li>
                <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#modalForAccount"><sec:authentication property="principal.username"/></a></li>
                <li><a href="#">Help</a></li>
            </ul>
            <form class="navbar-form navbar-right" action="/search" method="post">
                <input type="text" name="whatSearch" class="form-control" placeholder="Search...">
                <input type="submit" name="search" value="Search"/>
            </form>
        </div>
    </div>
</nav>


<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="btn-group">
                <button type="button" class="btn btn-default tyopdown-toggle" data-toggle="dropdown">
                    New <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#modalForNewFolder">New Folder</a></li>
                    <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#modalForFileUpload">File upload</a></li>
                    <li><a href="#" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#modalForFolderUpload">Folder upload</a></li>
                </ul>
            </div>

            <ul class="nav nav-sidebar">
                <li><a href="/index">My store</a></li>
                <li><a href="#">Shared with me</a></li>
                <li><a href="/starred">Starred</a></li>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="table-responsive">
                <form action="/actions_above_checked_files" method="post">
                    <input type="hidden" name="currentFolder" value="${f}">

                    <table class="table table-striped record_table">
                        <thead>
                        <tr>
                            <th></th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Size</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${content[1]}" var="currentFolder">
                            <tr>
                                <td><input type="checkbox" name="checked_folders_id" value="${currentFolder.id}" /></td>
                                <td><a href = "/folder?f=${currentFolder.id}">${currentFolder.name}</a></td>
                                <td><span class="glyphicon glyphicon-folder-close" aria-hidden="true"></span></td>
                                <td></td>
                            </tr>
                        </c:forEach>
                        <c:forEach items="${content[0]}" var="currentFile">
                            <tr>
                                <td><input type="checkbox" name="checked_files_id" value="${currentFile.id}" /></td>
                                <td>${currentFile.name}</td>
                                <td>${currentFile.type}</td>
                                <td>${currentFile.size}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
                        <li><input type="submit" name="delete" value="Delete"/></li>
                        <li><input type="submit" name="download" value="Download"/></li>
                        <li class="divider"></li>
                        <li><input type="submit" name="starred" value="Starred"/></li>
                        <li><input type="submit" name="removestar" value="Remove star"/></li>
                        <li class="divider"></li>
                        <li><input type="submit" name="rename" value="Rename"/></li>
                    </ul>

                </form>
            </div>
        </div>
    </div>
</div>


<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade" id="modalForNewFolder" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                            <h4 class="modal-title" id="myModalLabel">Create new folder</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-inline" action="create_folder" method="post">
                                <div class="form-group">
                                    <label class="sr-only" for="newFolder">newFolder</label>
                                    <input type="hidden" name="currentFolder" value="${f}">
                                    <input type="text" class="form-control" id="newFolder" name="nameOfFolder" placeholder="Enter new folder name">
                                </div>
                                <button type="submit" class="btn btn-primary">Create</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade" id="modalForFileUpload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                            <h4 class="modal-title">Upload file</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-inline" enctype="multipart/form-data"  action="upload" method="post">
                                <div class="form-group">
                                    <input type="hidden" name="currentFolder" value="${f}">
                                    <input type="file" name="file" placeholder="Choice file">
                                </div>
                                <button type="submit" class="btn btn-primary">Upload</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>


<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade" id="modalForFolderUpload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                            <h4 class="modal-title">Upload folder</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-inline" enctype="multipart/form-data"  action="upload" method="post">
                                <input type="hidden" name="currentFolder" value="${f}">
                                <input type="hidden" name="uploaded" value="1" />
                                <label>Выберите директорию:</label>
                                <input type="file" name="files" webkitdirectory directory multiple mozdirectory/>
                                <input type="submit" value="Загрузить" />
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>


<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade" id="modalForAccount" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <a class="btn btn-lg btn-danger" href="/logout" role="button">Logout</a>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

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
    $(document).ready(function() {
        $('.record_table tr').click(function(event) {
            if (event.target.type !== 'checkbox') {
                $(':checkbox', this).trigger('click');
            }
        });
    });</script>

</body>
</html>

