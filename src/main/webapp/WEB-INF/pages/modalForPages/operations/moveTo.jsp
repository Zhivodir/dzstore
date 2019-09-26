<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForMoveTo" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel"><s:message code="modal.move.to.title"/></h4>
                        </div>
                        <div class="modal-body">
                            <div class="currentFolderPath"></div>

                            <table id="tableForMoveTo" class="table table-striped record_table" cellspacing="0"
                                   width="100%">
                                <tbody>
                                </tbody>
                            </table>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal"><s:message
                                    code="modal.button.cancel"/></button>
                            <button type="button" class="btn btn-primary" id="okMoveTo" onclick="moveToByButton()" disabled><s:message
                                    code="modal.button.move"/></button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>


<script>
    pathBlock = $("#modalForMoveTo .currentFolderPath");
    nestingLevel = 0;

    filesForMove = [];
    foldersForMove = [];
    typeOfMoving = '';

    function initStartingPath() {
        levelPaths = $(".currentFolderPath .levelPath");
        nestingLevel = levelPaths.length;
        pathBlock.empty();
        pathBlock.append('<span class="glyphicon glyphicon-circle-arrow-left" aria-hidden="true"></span>');
        levelPaths.clone().removeClass("levelPath").addClass("levelPathMoveTo").appendTo("#modalForMoveTo .currentFolderPath");
        $("#modalForMoveTo .pathElement").not(":last").hide();
    }

    function moveToByButton() {
        var moveToFolderId = $('#tableForMoveTo').find("tr.selected .name_of_content").data("current-folder-id");
        var selectedFiles = createSelectedFilesMassiv();
        var selectedFolders = createSelectedFoldersMassiv();
        ajaxMoveTo(moveToFolderId, selectedFiles, selectedFolders);
    }

    function moveToByKeys() {
        var moveToFolderId = $(".currentFolderPath .levelPath:last").data("current-folder-id");

        if (filesForMove.length != 0 || foldersForMove != 0) {
            if (typeOfMoving == 'x') {
                ajaxMoveTo(moveToFolderId, filesForMove, foldersForMove)
            }
            if (typeOfMoving == 'c') {
                ajaxMoveTo(moveToFolderId, filesForMove, foldersForMove)
            }
        }
    }

    function ajaxMoveTo(moveToFolderId, selectedFiles, selectedFolders) {
        $.ajax({
            url: "/moveTo",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: selectedFiles,
                selectedFolders: selectedFolders,
                moveTo: moveToFolderId
            },
            success: function (result) {
                table.ajax.reload();
                $("#modalForMoveTo").modal("hide");
            },
            error: function (result) {
            }
        })
    }

    $(document).ready(function () {
        table2 = $('#tableForMoveTo').DataTable(datatableOpts2(
            '/getContent/mydisk/' + currentFolderId,
            [
                {
                    data: 'name',
                    render: function (data, type, full) {
                        if (full.type == "folder") {
                            return '<strong><span class="name_of_content" data-current-folder-id="' + full.id + '">' + data + '</span></strong>';
                        }
                        return '<span class="name_of_content">' + data + '</span>';
                    }
                }
            ]
        ));

        $('#modalForMoveTo').on('click', 'tr.choise_folder', function (e) {
            if($(this).hasClass("selected")){
                $("#okMoveTo").prop("disabled", false);
            } else {
                $("#okMoveTo").prop("disabled", true);
            }
        });

        $('#modalForMoveTo').on('click', '.glyphicon-circle-arrow-left', function (e) {
            if (nestingLevel > 1) {
                moveTo_RemoveFolderFromPath();
                comeBackToPrevFolder();
            } else {
                pathBlock.empty();
                pathBlock.append('<span class="pathElement levelPathMoveTo">' + typeOfViewNames['space'] + '</span>');
                table2.rows().remove().draw(false);
                table2.row.add({
                    "name": "<strong>" + typeOfViewNames['mydisk'] + "<strong>"
                }).draw();
                $('#tableForMoveTo tr .name_of_content').data("current-folder-id", -1);
                $('#tableForMoveTo tr').addClass("choise_field").addClass("choise_folder");
            }
            nestingLevel--;
        });


        $("#tableForMoveTo").on('dblclick', '.choise_folder', function (e) {
            if (nestingLevel == 0) {
                pathBlock.append('<span class="glyphicon glyphicon-circle-arrow-left" aria-hidden="true"></span>')
                levelPaths.clone().removeClass("levelPath").addClass("levelPathMoveTo").appendTo("#modalForMoveTo .currentFolderPath");
            }
            var content = $(this).find(".name_of_content");
            currentFolderId = content.data("current-folder-id");
            var currentFolderName = content.text();
            reloadMoveToContent(currentFolderId);
            moveTo_AddFolderNameToPath(currentFolderId, currentFolderName);
            nestingLevel++;
        });

        $(".li_moveTo").on("click", function () {
            reloadMoveToContent(currentFolderId);
        })

        function reloadMoveToContent(currentFolderId) {
            table2.ajax.url('/getContent/mydisk/' + currentFolderId);
            table2.ajax.reload();
        }

        function moveTo_AddFolderNameToPath(targetFolderId, targetFolderName) {
            pathBlock.append('<span class="pathElement levelPathMoveTo" data-current-folder-id="' + targetFolderId + '">' + targetFolderName + '</span>');
            $("#modalForMoveTo .pathElement").not(":last").hide();
        }

        function moveTo_RemoveFolderFromPath() {
            $("#modalForMoveTo .pathElement:last").remove();
            $("#modalForMoveTo .pathElement:last").show();
        }

        function comeBackToPrevFolder() {
            prevFolderId = $("#modalForMoveTo .pathElement:last").data("current-folder-id");
            reloadMoveToContent(prevFolderId);
        }
    })
</script>