<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<section class="content-header">
    <div class="pull-left">
        <ol class="breadcrumb currentFolderPath"></ol>
    </div>
</section>

<script>
    $(document).ready(function () {
        $(".currentFolderPath").append(getPathRoot());
    });

    function getPathRoot() {
        switch ('${typeOfView}') {
            case 'index':
                return '<li><span class="pathElement levelPath" data-current-folder-id="-1">' + typeOfViewNames['myspaceView'] + '</span></li>';
            case 'shared':
                return '<li><a href="/${typeOfView}" class="levelPath sharedWithMe">' + typeOfViewNames['sharedView'] + '</a></li>';
            case 'starred':
                return '<li><a href="/${typeOfView}" class="levelPath">' + typeOfViewNames['starredView'] + '</a></li>';
            case 'bin':
                return '<li><a href="/${typeOfView}" class="levelPath">' + typeOfViewNames['binView'] + '</a></li>';
            case 'search':
                return '<li><span class="levelPath">Search result</span></li>';
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
