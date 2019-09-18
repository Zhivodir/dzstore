<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForFolderUpload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-DirectoryUpload">
                    <div class="modal-content all-modal-change">
                        <form class="form-inline" enctype="multipart/form-data" id="folderUploadForm">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">
                                    <span aria-hidden="true">&times;</span>
                                    <span class="sr-only">Close</span>
                                </button>
                                <h4 class="modal-title"><s:message code="modal.upload.folder.title"/></h4>
                            </div>
                            <div class="modal-body">
                                <div class="form-group" style="float: left" id="folder_upload">
                                    <input type="hidden" name="currentFolderID" value="${currentFolderID}" class="uploadCurrentFolderId">
                                    <input type="hidden" name="structure" id="structure" value=""/>
                                    <input type="file" name="files" id="files" webkitdirectory directory multiple mozdirectory/>
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
            <!-- end of Modal -->
        </div>
    </div>
</div>


<script>
    $('#folderUploadForm').submit(function (e) {
        $.ajax({
            url: '/upload',
            type: 'POST',
            data: new FormData(this),
            processData: false,
            contentType: false,
            success: function () {
                table.ajax.reload();
            }
        });
        e.preventDefault();
        $("#modalForFolderUpload").modal("hide");
    });
</script>
