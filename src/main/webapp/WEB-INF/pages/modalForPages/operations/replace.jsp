<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForReplace" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-FileUpload">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">Перенести в ...</h4>
                        </div>
                        <div class="modal-body">
                            <ul class="Container" id="tree">
                                <input id="replaceTarget" class="radio_move_to" type="radio" name="move_to" value="tree"/>
                                <li class="Node IsRoot IsLast ExpandClosed">
                                    <div class="Expand"></div>
                                    <div class="Content">Каталог</div>
                                    <ul class="Container"></ul>
                                </li>
                            </ul>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                            <button type="button" class="btn btn-primary" onclick="replaceContent()">Перенести</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

<script>
    function replaceContent() {
        var selectedFilesId = [];
        var selectedFoldersId = [];
        $("tr.selected").find(".choise_checkbox.choise_file").each(function() {
            selectedFilesId.push(this.value);
        });
        $("tr.selected").find(".choise_checkbox.choise_folder").each(function() {
            selectedFoldersId.push(this.value);
        });
        var moveTo = $("#replaceTarget").val();

        $.ajax({
            url: "/replaceContent",
            type: 'POST',
            data: {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId,
                moveTo: moveTo
            },
            success: function (result) {
                table.ajax.reload();
            },
            error: function (result) {
            }
        })

        $("#modalForReplace").modal('hide');
    }
</script>
