<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <div class="modal fade modal-coordinate" id="modalForFileUpload" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-FileUpload">
                    <div class="modal-content">
                        <form class="form-inline" enctype="multipart/form-data" id="fileUploadForm">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;
                                </button>
                                <h4 class="modal-title" id="myModalLabel"><s:message code="modal.upload.file.title"/></h4>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <input type="hidden" name="currentFolderID" value="${currentFolderID}"
                                           class="uploadCurrentFolderId">
                                    <input type="file" name="file">
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="modal.button.cancel"/></button>
                                <button type="submit" class="btn btn-primary"><s:message code="modal.button.upload"/></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $('#fileUploadForm').submit(function (e) {
        $.ajax({
            url: '/uploadFile',
            type: 'POST',
            data: new FormData(this),
            processData: false,
            contentType: false,
            success: function (result) {
                busySpace = result;
                table.ajax.reload();
            }
        });
        e.preventDefault();
        $("#modalForFileUpload").modal("hide");
    });
</script>