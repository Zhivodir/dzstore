<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none">
    <li class="li_remove general"><a href="#" class="contextHref" onclick="removeContent()"><s:message
            code="contextmenu.remove"/></a></li>
    <li class="li_download general"><a href="#" class="contextHref" onclick="downloadContent()"><s:message
            code="contextmenu.download"/></a></li>
    <li class="divider general"></li>
    <li class="li_starred general"><a href="#" class="contextHref" onclick="changeStarState(true)"><s:message
            code="contextmenu.add.star"/></a></li>
    <li class="li_removestar general"><a href="#" class="contextHref" onclick="changeStarState(false)"><s:message
            code="contextmenu.remove.star"/></a></li>
    <li class="divider general"></li>
    <li class="li_rename general"><a class="contextHref" href="#" data-toggle="modal"
                                     data-target="#modalForRename"><s:message code="contextmenu.rename"/></a></li>
    <li class="li_share general"><a class="contextHref" href="#" data-toggle="modal"
                                    data-target="#modalForShare"><s:message code="contextmenu.share"/></a></li>
    <li class="divider general"></li>
    <li class="li_addtome general"><a class="contextHref" href="" onclick="addToMe()"><s:message
            code="contextmenu.add.to.me"/></a></li>
    <li class="li_moveTo general"><a class="contextHref" href="#" data-toggle="modal" onclick="initStartingPath()"
                                     data-target="#modalForMoveTo"><s:message
            code="contextmenu.move.to"/></a></li>
    <li class="li_makeCopy general"><a class="contextHref" href="#" onclick="makeCopy()"><s:message
            code="contextmenu.make.copy"/></a></li>

    <li class="for_bin"><a href="#" class="contextHref" onclick="restoreContent()"><s:message
            code="contextmenu.restore"/></a></li>
    <li class="for_bin"><a href="#" class="contextHref" data-toggle="modal" data-target="#modalForDelete"><s:message
            code="contextmenu.delete"/></a></li>
</ul>

<script>
    function makeCopy() {
        $.ajax({
            url: "/makeCopy",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: createSelectedFilesMassiv(),
                selectedFolders: []
            },
            success: function (result) {
                busySpace = result;
                table.ajax.reload();
            }
        })
    }

    function changeStarState(state) {
        $.ajax({
            url: "/changeStarState",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: createSelectedFilesMassiv(),
                selectedFolders: createSelectedFoldersMassiv(),
                state: state
            },
            success: function (result) {
                table.ajax.reload();
            },
            error: function (result) {
            }
        })
    }

    function addToMe() {
        $.ajax({
            url: "/addToMe",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: createSelectedFilesMassiv(),
                selectedFolders: createSelectedFoldersMassiv(),
            },
            success: function (result) {
                busySpace = result;
                showBusySpace();
            },
            error: function (result) {
            }
        })
    }

    function downloadContent() {
        postToUrl(
            '/download',
            {
                selectedFiles: createSelectedFilesMassiv(),
                selectedFolders: createSelectedFoldersMassiv(),
                typeOfView: '${typeOfView}',
                currentFolderID: currentFolderId
            }
            , 'POST');
    }

    function postToUrl(path, params, method) {
        method = method || "post";

        var form = document.createElement("form");
        form.setAttribute("method", method);
        form.setAttribute("action", path);
        for (var key in params) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
        }

        document.body.appendChild(form);
        form.submit();
    }

    function removeContent() {
        $.ajax({
            url: "/removeContent",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: createSelectedFilesMassiv(),
                selectedFolders: createSelectedFoldersMassiv(),
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

    function removeContentByKeys() {
        $.ajax({
            url: "/removeContent",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: filesForMove,
                selectedFolders: foldersForMove,
                typeOfView: '${typeOfView}'
            },
            success: function (result) {
                table.ajax.reload();
            },
            error: function (result) {
            }
        })
    }
</script>