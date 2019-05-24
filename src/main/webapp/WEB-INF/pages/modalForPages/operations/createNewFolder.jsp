<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForNewFolder" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-NewFolder">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel"><s:message code="modal.create.folder.title"/></h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <input type="text" class="form-control" id="newFolder"
                                       placeholder="<s:message code="modal.create.folder.placeholder"/>">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="modal.button.cancel"/></button>
                            <button type="button" onclick="createFolder()" class="btn btn-primary"><s:message code="modal.button.create"/></button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

<script>
    function createFolder() {
        var newFolderName = $("#newFolder").val();

        $.ajax({
            url: "/createFolder",
            type: 'POST',
            data: {
                currentFolderId: currentFolderId,
                newFolderName: newFolderName
            },
            success: function (result) {
                table.ajax.reload();
                $("#newFolder").attr(value, "");
            },
            error: function (result) {
            }
        })

        $("#modalForNewFolder").modal('hide');
    }
</script>
