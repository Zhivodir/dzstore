<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForFileUpload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-FileUpload">
                    <div class="modal-content">
                        <form class="form-inline" enctype="multipart/form-data" action="upload" method="post">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">Загрузить файл</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <input type="hidden" name="typeOfView" value="${typeOfView}">
                                <input type="hidden" name="currentFolderID" value="${currentFolderID}">
                                <input type="file" name="file">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                            <button type="submit" class="btn btn-primary">Upload</button>
                        </div>
                        </form>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>
