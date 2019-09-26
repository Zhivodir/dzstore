<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<link rel="stylesheet" href="/datatables/DataTables-1.10.16/css/dataTables.bootstrap.min.css">

<section class="content">
    <div>
        <form id="content_form" action="/download" method="post">
            <input type="hidden" name="currentFolderID" value="${currentFolderID}">
            <input type="hidden" name="typeOfView" value="${typeOfView}">
            <table id="myTable" class="table table-striped record_table" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th class="checkId"></th>
                    <th class="checkName"><s:message code="contentspace.name"/></th>
                    <th class="checkType"><s:message code="contentspace.type"/></th>
                    <th class="checkOwner"><s:message code="contentspace.owner"/></th>
                    <th class="checkSize"><s:message code="contentspace.size"/></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>

            <c:import url="/WEB-INF/pages/modalForPages/operations/delete.jsp"/>
            <c:import url="/WEB-INF/pages/modalForPages/errors/dataInBin.jsp"/>
            <c:import url="/WEB-INF/pages/elements/contextMenu.jsp"/>
            <c:import url="/WEB-INF/pages/modalForPages/operations/share.jsp"/>
            <c:import url="/WEB-INF/pages/modalForPages/operations/rename.jsp"/>
            <c:import url="/WEB-INF/pages/modalForPages/operations/moveTo.jsp"/>
        </form>
    </div>
</section>

<!-- DataTables -->
<script src="/datatables/DataTables-1.10.16/js/jquery.dataTables.min.js"></script>
<script src="/datatables/DataTables-1.10.16/js/dataTables.bootstrap.min.js"></script>
<script src="/datatables/Select-1.2.5/js/dataTables.select.min.js"></script>
<script src="js/utils/sb-datatables.js"></script>

<script type="text/javascript">
    currentFolderId = ${currentFolderID != null ? currentFolderID : -1};
    typeOfView = '${typeOfView}';
    targetContent = getUrlForDataTables(typeOfView);
    busySpace = ${busySpace};
    availableSpace = ${availableSpace};

    $(document).ready(function () {
        showBusySpace();

        table = $('#myTable').DataTable(datatableOpts(
            '/getContent/' + targetContent,
            [
                {
                    data: null,
                    render: function (data, type, full) {
                        if (full.type == "folder") {
                            return '<input hidden class="choise_checkbox choise_folder" type="checkbox" name="checked_folders_id" value="' + full.id + '"/>';
                        }
                        return '<input hidden class="choise_checkbox choise_file" type="checkbox" name="checked_files_id" value="' + full.id + '"/>';
                    }
                },
                {
                    data: 'name',
                    class: 'forContextMenu',
                    render: function (data, type, full) {
                        var icons = full.starred ? '<span class="glyphicon glyphicon-star"></span>' : '';
                        icons += full.shared ? '<span class="glyphicon glyphicon-eye-open"></span>' : '';

                        if (full.type == "folder") {
                            return '<strong><span class="name_of_content">' + data + '</span></strong>' + icons;
                        }
                        return '<span class="name_of_content">' + data + '</span>' + icons;
                    }
                },
                {
                    data: 'type',
                    render: function (data, type, full) {
                        if (data == "folder") {
                            return '<span class="glyphicon glyphicon-folder-close" aria-hidden="true"></span>';
                        }
                        return data;
                    }
                },
                {
                    data: 'owner'
                },
                {
                    data: 'size',
                    render: function (data, type, full) {
                        if (full.type == "folder" || full.size == 0) {
                            return " - ";
                        }
                        return formatSize(data);
                    }
                }
            ]
        ));


        $(".sidebar-menu").on('click', '.purposeOfTransit', function (e) {
            $(".purposeOfTransit").parent().removeClass("active");
            $(this).parent().addClass("active");
            typeOfView = $(this).data("view-type");
            currentFolderId = -1;
            reloadContentForFolder();
            $('.currentFolderPath').children().not('#pathRoot').remove();
        });

        $("#myTable").on('dblclick', '.choise_folder', function (e) {
            currentFolderId = $(this).find("input").val();

            if (typeOfView == "bin") {
                $('#idFolderForRestore').val(currentFolderId);
                $('#modalForOpenDataInBin').modal('show');
            } else if (typeOfView == "search" || typeOfView == "starred") {
                reloadContentWithChangeViewForFolder(currentFolderId);
                typeOfView = "mydisk";
                getPathToFolder(currentFolderId);
            } else {
                if (typeOfView != "shared") {
                    typeOfView = "mydisk";
                }
                var currentFolderName = $(this).find(".name_of_content").text();
                reloadContentForFolder();
                addFolderNameToPath(currentFolderId, currentFolderName);
            }
        });

        function reloadContentWithChangeViewForFolder(folderId){
            $.ajax({
                url: "/isCurrentUserOwner",
                type: 'POST',
                traditional: true,
                data: {
                    folderId: folderId
                },
                success: function (result) {
                    if(result["isOwner"]) {
                        typeOfView = "mydisk";
                    } else {
                        typeOfView = "shared";
                    }

                    reloadContentForFolder();
                }
            })
        }

        $(".currentFolderPath").on('dblclick', '.levelPath', function (e) {
            currentFolderId = $(this).data("current-folder-id");
            reloadContentForFolder();
            returnFolderPath(currentFolderId);
        });

        function reloadContentForFolder() {
            table.ajax.url('/getContent/' + getUrlForDataTables(typeOfView));
            table.ajax.reload();
        }

        $("#search-btn").on('click', function (e) {
            var searchString = $.trim($("#search-input").val());
            if (searchString) {
                typeOfView = "search";
                $('.currentFolderPath').children().not('#pathRoot').remove();
                reloadContentForSearch(searchString);
            }
        });

        function reloadContentForSearch(searchString) {
            table.ajax.url('/getContent/search/' + searchString);
            table.ajax.reload();
        }
    });

    function getUrlForDataTables(typeOfView) {
        if (typeOfView == "bin") {
            return 'bin';
        }
        if (typeOfView == "search") {
            return 'search/' + $("#search-input").val();
        }
        return typeOfView + '/' + currentFolderId;
    }

    function createSelectedFilesMassiv() {
        var selectedFilesId = [];
        $("tr.selected").find(".choise_checkbox.choise_file").each(function () {
            selectedFilesId.push(this.value);
        });
        return selectedFilesId;
    }

    function createSelectedFoldersMassiv() {
        var selectedFoldersId = [];
        $("tr.selected").find(".choise_checkbox.choise_folder").each(function () {
            selectedFoldersId.push(this.value);
        });
        return selectedFoldersId;
    }

    function increaseBusySpace(sizeOfChange) {
        busySpace += sizeOfChange;
    }

    function reduceBusySpace(sizeOfChange) {
        busySpace -= sizeOfChange;
    }

    $(document).keydown(function (e) {
        if (((e.ctrlKey || e.metaKey) && (e.keyCode == 67 || e.keyCode == 88))) {
            filesForMove = createSelectedFilesMassiv();
            foldersForMove = createSelectedFoldersMassiv();

            if (e.keyCode == 67) {
                typeOfMoving = 'c';
            }
            if (e.keyCode == 88) {
                typeOfMoving = 'x';
            }
        }

        //ctr+V
        if (e.keyCode == 86 && e.ctrlKey) {
            moveToByKeys();
        }

        //Delete
        if (e.keyCode == 46) {
            filesForMove = createSelectedFilesMassiv();
            foldersForMove = createSelectedFoldersMassiv();
            if (typeOfView != "bin") {
                removeContentByKeys();
            } else {
                $("#modalForDelete").modal("show");
            }
        }

        //Enter
        if (e.keyCode == 13) {
            filesForMove = createSelectedFilesMassiv();
            foldersForMove = createSelectedFoldersMassiv();
            if (filesForMove.length == 0 && foldersForMove.length == 1) {
                alert($("#modalForMoveTo").hasClass('in'));
            }
        }
    });
</script>