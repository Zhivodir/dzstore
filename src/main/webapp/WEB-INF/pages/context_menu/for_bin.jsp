<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
    <li class="contextHref"><input type="button" class="contextInput" onclick="restoreContent()" value="<s:message code="contextmenu.restore"/>"></li>
    <li><a class="contextHref" href="#" data-toggle="modal" data-target="#modalForDelete"><s:message code="contextmenu.delete"/></a></li>
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
