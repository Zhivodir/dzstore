<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForRename" tabindex="-1"
                 role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-standart">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">Переименование</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <input type="hidden" id="selectedContentId" value="">
                                <input type="text" class="form-control" id="newName" name="name" placeholder="Enter new name">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                            <button type="button" class="btn btn-primary" onclick="renameContent()">Сохранить</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

<script>
    function renameContent() {
        var selectedContent = $("tr.selected").find(".choise_checkbox");
        var selectedContentId = selectedContent.attr("value");
        var selectedContentType = selectedContent.hasClass("choise_folder") ? "folder" : "file";
        var newName = $("#newName").val();

        $.ajax({
            url: "/renameContent",
            type: 'POST',
            data: {
                contentType: selectedContentType,
                contentId: selectedContentId,
                newName: newName
            },
            success: function (result) {
                table.ajax.reload();
            },
            error: function (result) {
            }
        })

        $("#modalForRename").modal('hide');
    }
</script>
