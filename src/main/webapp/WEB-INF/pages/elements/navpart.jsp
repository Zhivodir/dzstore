<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<section class="content-header">
    <div class="pull-left">
        <ol class="breadcrumb currentFolderPath">
            <li id="pathRoot">
                <span class="pathElement levelPath" data-current-folder-id="-1"></span>
            </li>
        </ol>
    </div>
</section>

<script>
    function getPathRoot(){
        return typeOfViewNames[typeOfView];
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
                $(this).remove();
            }
        });
    }

    function prepareModalForUpload() {
        $(".uploadCurrentFolderId").val(currentFolderId);
    }


    function getPathToFolder(folderId){
        $.ajax({
            url: "/getPathToFolder",
            type: 'POST',
            traditional: true,
            data: {
                folderId: folderId
            },
            success: function (result) {
                formAndShowPath(result["response"]);
            }
        })
    }

    function formAndShowPath(stringPath){
        changeRootOfPath()
        var folders = stringPath.split("/");
        for(i = 0; i < folders.length; i++){
            addFolderNameToPath(0, folders[i]);
        }
    }

    function changeRootOfPath(){
        $("#pathRoot .pathElement").text(getPathRoot());
    }
</script>
