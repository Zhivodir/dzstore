<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForShare" tabindex="-1"
                 role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel"><s:message code="modal.share.title"/></h4>
                        </div>
                        <div class="modal-body" id="modal_share">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="modal.button.cancel"/></button>
                            <button type="button" class="btn btn-primary" onclick="changeSharedList()"><s:message code="modal.button.done"/></button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

<!-- Amsify Plugin -->
<link rel="stylesheet" href="/taggingSystems/amsify.suggestags.css">
<script src="/taggingSystems/jquery.amsify.suggestags.js"></script>

<script>
    function changeSharedList() {
        var selectedFilesId = [];
        var selectedFoldersId = [];
        var cancelShareForUsers = [];
        $("tr.selected").find(".choise_checkbox.choise_file").each(function() {
            selectedFilesId.push(this.value);
        });
        $("tr.selected").find(".choise_checkbox.choise_folder").each(function() {
            selectedFoldersId.push(this.value);
        });
        $("tr.selected").find("#modalForShare.info_block").each(function() {
            cancelShareForUsers.push(this.value);
        });

        $.ajax({
            url: "/changeSharedList",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId,
                shareForUsers: $("#modalForShare input").val(),
                cancelShareForUsers: cancelShareForUsers
            },
            success: function (result) {
                table.ajax.reload();
            },
            error: function (result) {
            }
        })

        $("#modalForShare").modal('hide');
    }
</script>
