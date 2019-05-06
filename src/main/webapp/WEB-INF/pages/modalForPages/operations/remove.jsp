<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForRemove" tabindex="-1"
                 role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-standart">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">Перенести в корзину</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group"></div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                            <button type="button" class="btn btn-primary" onclick="removeContent()">В корзину</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

<script>
    function removeContent() {
        var selectedFilesId = [];
        var selectedFoldersId = [];
        $("tr.selected").find(".choise_checkbox.choise_file").each(function() {
            selectedFilesId.push(this.value);
        });
        $("tr.selected").find(".choise_checkbox.choise_folder").each(function() {
            selectedFoldersId.push(this.value);
        });

        $.ajax({
            url: "/removeContent",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId,
                typeOfView: '${typeOfView}'
            },
            success: function (result) {
                table.ajax.reload();
            },
            error: function (result) {
            }
        })

        $("#modalForRemove").modal('hide');
    }
</script>

