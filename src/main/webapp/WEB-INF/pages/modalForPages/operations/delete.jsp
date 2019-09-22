<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForDelete" tabindex="-1"
                 role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-standart">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel"><s:message code="modal.delete.title"/></h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="modal.button.cancel"/></button>
                            <button type="button" class="btn btn-primary" onclick="deleteContent()"><s:message code="modal.button.delete"/></button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

<script>
    function deleteContent() {
        var selectedFilesId = createSelectedFilesMassiv();
        var selectedFoldersId = createSelectedFoldersMassiv();

        $.ajax({
            url: "/deleteContent",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId
            },
            success: function (result) {
                busySpace = result;
                // showBusySpace();
                table.ajax.reload();
            },
            error: function (result) {
            }
        })

        $("#modalForDelete").modal('hide');
    }
</script>
