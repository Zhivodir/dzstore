<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<section class="content-header">
    <div class="pull-left">
        <ol class="breadcrumb currentFolderPath">
            <li id="pathRoot">

            </li>
        </ol>
    </div>
</section>

<script>
    function getPathRoot(){
        switch(typeOfView) {
            case 'mydisk':
                return '<span class="pathElement levelPath" data-current-folder-id="-1">' + typeOfViewNames['myspaceView'] + '</span>';
            case 'shared':
                return '<a href="' + '\' + typeOfView + " class="levelPath sharedWithMe">' + typeOfViewNames['sharedView'] + '</a>';
            case 'starred':
                return '<a href="' + '\' + typeOfView + " class="levelPath">' + typeOfViewNames['starredView'] + '</a>';
            case 'bin':
                return '<a href="' + '\' + typeOfView + " class="levelPath">' + typeOfViewNames['binView'] + '</a>';
            case 'search':
                return '<span class="levelPath">Search result</span>';
        }
    }

    function addFolderNameToPath(targetFolderId, targetFolderName) {
        $(".currentFolderPath").append('<span class="pathElement glyphicon glyphicon glyphicon-chevron-right" aria-hidden="true"></span>');
        $(".currentFolderPath").append('<span class="pathElement levelPath" data-current-folder-id="' + targetFolderId + '">' + targetFolderName + '</span>');
    }

    function returnFolderPath(targetFolderId) {
        var lastFolder = false;

        $(".pathElement ").each(function (index, value) {
            if (!lastFolder) {
                if (targetFolderId == $(this).data("current-folder-id")) {
                    lastFolder = true;
                }
            } else {
                $(this)
                $(this).remove();
            }
        });
    }

    function prepareModalForUpload() {
        $(".uploadCurrentFolderId").val(currentFolderId);
    }
</script>
