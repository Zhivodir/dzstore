<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
    <li class=""><a class="contextHref" href="#" data-toggle="modal" data-target="#modalForRemove">Remove</a></li>
    <li class="contextHref"><input type="submit" class="contextInput" name="action" value="Download"></li>
    <li class="divider"></li>
    <li class="contextHref li_starred"><input type="button" class="contextInput" value="Add star" onclick="changeStarState(true)"></li>
    <li class="contextHref li_removestar"><input type="button" class="contextInput" value="Remove star" onclick="changeStarState(false)"></li>
    <li class="divider"></li>
    <li class="li_rename"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForRename">Rename</a></li>
    <li class="li_share"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForShare">Share</a></li>
    <li class="divider"></li>
    <li class="contextHref li_addtome"><input type="button" class="contextInput" onclick="addToMe()"></li>
    <li class="li_replace"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForReplace">Replace</a></li>
</ul>

<script>
    function changeStarState(state) {
        var selectedFilesId = [];
        var selectedFoldersId = [];
        $("tr.selected").find(".choise_checkbox.choise_file").each(function() {
            selectedFilesId.push(this.value);
        });
        $("tr.selected").find(".choise_checkbox.choise_folder").each(function() {
            selectedFoldersId.push(this.value);
        });

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
        var selectedFilesId = [];
        var selectedFoldersId = [];
        $("tr.selected").find(".choise_checkbox.choise_file").each(function() {
            selectedFilesId.push(this.value);
        });
        $("tr.selected").find(".choise_checkbox.choise_folder").each(function() {
            selectedFoldersId.push(this.value);
        });

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
</script>