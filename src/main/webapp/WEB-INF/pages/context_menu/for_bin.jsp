<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
    <li class="contextHref"><input type="button" class="contextInput" onclick="restoreContent()" value="Restore"></li>
    <li><a class="contextHref" href="#" data-toggle="modal" data-target="#modalForDelete">Delete</a></li>
</ul>

<script>
    function restoreContent() {
        var selectedFilesId = createSelectedFilesMassiv();
        var selectedFoldersId = createSelectedFoldersMassiv();

        $.ajax({
            url: "/restoreContent",
            type: 'POST',
            traditional: true,
            data: {
                selectedFiles: selectedFilesId,
                selectedFolders: selectedFoldersId
            },
            success: function (result) {
                table.ajax.reload();
            },
            error: function (result) {
            }
        })
    }
</script>
