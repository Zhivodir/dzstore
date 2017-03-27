<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
    <div>
        <a href="/index">My store</a>
        <c:if test="${f ne null}"></c:if>
        <c:forEach items="${listForRelativePath}" var="folder">
            <a href="/folder?f=${folder.id}">${folder.name}</a>
            <span class="glyphicon glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        </c:forEach>
    </div>
    <div class="table-responsive">

        <form action="/actions_above_checked_files" method="post">
            <input type="hidden" name="currentFolder" value="${f}">

            <table id="myTable" class="table table-striped record_table">
                <thead>
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Size</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${f ne null}">
                    <tr>
                        <td></td>
                        <td><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:if>
                <c:forEach items="${content[1]}" var="currentFolder">
                    <tr>
                        <td><input type="checkbox" name="checked_folders_id" value="${currentFolder.id}"/></td>
                        <td><a href="/folder?f=${currentFolder.id}">${currentFolder.name}</a></td>
                        <td><span class="glyphicon glyphicon-folder-close" aria-hidden="true"></span></td>
                        <td></td>
                    </tr>
                </c:forEach>
                <c:forEach items="${content[0]}" var="currentFile">
                    <tr>
                        <td><input type="checkbox" name="checked_files_id" value="${currentFile.id}"/></td>
                        <td>${currentFile.name}</td>
                        <td>${currentFile.type}</td>
                        <td>${currentFile.size}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
                <!--<li><input type="submit" name="delete" value="Delete"/></li>-->
                <li><a href="#" data-toggle="modal" data-target="#modalForDelete">Delete</a></li>
                <li><input type="submit" name="download" value="Download"/></li>
                <li class="divider"></li>
                <li><input type="submit" name="starred" value="Starred"/></li>
                <li><input type="submit" name="removestar" value="Remove star"/></li>
                <li class="divider"></li>
                <li><a href="#" data-toggle="modal" data-target="#modalForRename">Rename</a></li>
            </ul>

            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <!-- start of Modal -->
                        <div class="modal fade" id="modalForDelete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                             aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">
                                            <span aria-hidden="true">&times;</span>
                                            <span class="sr-only">Close</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <input type="hidden" name="currentFolder" value="${f}">
                                        </div>
                                        <input type="submit" name="delete" value="Delete"/>
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
                        <div class="modal fade" id="modalForRename" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                             aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">
                                            <span aria-hidden="true">&times;</span>
                                            <span class="sr-only">Close</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                            <div class="form-group">
                                                <input type="hidden" name="currentFolder" value="${f}">
                                                <input type="text" class="form-control" id="newName" name="name"
                                                       placeholder="Enter new name">
                                            </div>
                                            <input type="submit" name="rename" value="Rename"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- end of Modal -->
                    </div>
                </div>
            </div>

        </form>
    </div>
</div>
