<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
    <li class=""><a class="contextHref" href="#" data-toggle="modal" data-target="#modalForRemove">Remove</a></li>
    <li class="li_download"><a href="#" class="contextHref" onclick="downloadContent()">Download</a></li>
    <li class="divider"></li>
    <li class="contextHref li_starred"><input type="button" class="contextInput" value="Add star" onclick="changeStarState(true)"></li>
    <li class="contextHref li_removestar"><input type="button" class="contextInput" value="Remove star" onclick="changeStarState(false)"></li>
    <li class="divider"></li>
    <li class="li_rename"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForRename">Rename</a></li>
    <li class="li_share"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForShare">Share</a></li>
    <li class="divider"></li>
    <li class="contextHref li_addtome"><input type="button" class="contextInput" value="Add to me" onclick="addToMe()"></li>
</ul>

<script>
    function changeStarState(state) {
        var selectedFilesId = createSelectedFilesMassiv();
        var selectedFoldersId = createSelectedFoldersMassiv();

        $.ajax({
            url: "/changeStarState",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId,
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
        var selectedFilesId = createSelectedFilesMassiv();
        var selectedFoldersId = createSelectedFoldersMassiv();

        $.ajax({
            url: "/addToMe",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId
            },
            success: function (result) {
            },
            error: function (result) {
            }
        })
    }

    function downloadContent() {
        var selectedFilesId = createSelectedFilesMassiv();
        var selectedFoldersId = createSelectedFoldersMassiv();

        postToUrl(
            '/download',
            {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId,
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
        for(var key in params) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
        }

        document.body.appendChild(form);
        form.submit();
    }
</script>