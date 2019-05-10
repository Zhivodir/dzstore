
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="top-part">
    <div class="navbar navbar-inverse navbar-fixed-top contentmenu-place">
        <div class="col-sm-3 col-md-2">
            <div class="btn-group">
                <button type="button" class="btn btn-default newButton" data-toggle="dropdown">
                    New <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                           data-target="#modalForNewFolder">New Folder</a></li>
                    <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                           data-target="#modalForFileUpload" onclick="prepareModalForUpload()">File upload</a></li>
                    <li><a href="#" class="btn btn-lg href_for_sidebar" data-toggle="modal"
                           data-target="#modalForFolderUpload" onclick="prepareModalForUpload()">Folder upload</a></li>
                </ul>
            </div>
        </div>
        <div class="col-sm-9 col-md-10">
            <div class="currentFolderPath">
                <%--<c:if test="${currentFolderID ne null}">--%>
                    <%--<c:forEach items="${listForRelativePath}" var="folder">--%>
                        <%--<span class="glyphicon glyphicon glyphicon-chevron-right" aria-hidden="true"></span>--%>
                        <%--<c:choose>--%>
                            <%--<c:when test="${typeOfView.equals('shared')}">--%>
                                <%--<a href="/shared?currentFolderID=${folder.id}" class="levelPath">${folder.name}</a>--%>
                            <%--</c:when>--%>
                            <%--<c:otherwise>--%>
                                <%--<a href="/?currentFolderID=${folder.id}" class="levelPath">${folder.name}</a>--%>
                            <%--</c:otherwise>--%>
                        <%--</c:choose>--%>
                    <%--</c:forEach>--%>
                <%--</c:if>--%>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $(".currentFolderPath").append(getPathRoot());
    });

    function getPathRoot(){
        switch('${typeOfView}') {
            case 'index':
                return '<span class="pathElement levelPath" data-current-folder-id="-1">My store</span>';
            case 'shared':
                return '<a href="/${typeOfView}" class="levelPath sharedWithMe">Shared with me</a>';
            case 'starred':
                return '<a href="/${typeOfView}" class="levelPath">Starred</a>';
            case 'bin':
                return '<a href="/${typeOfView}" class="levelPath">Bin</a>';
            case 'search':
                return '<span class="levelPath">Search result</span>';
        }
    }

    function addFolderNameToPath(targetFolderId, targetFolderName){
        $(".currentFolderPath").append('<span class="pathElement glyphicon glyphicon glyphicon-chevron-right" aria-hidden="true"></span>');
        $(".currentFolderPath").append('<span class="pathElement levelPath" data-current-folder-id="' + targetFolderId + '">' + targetFolderName + '</span>');
    }

    function returnFolderPath(targetFolderId){
        var lastFolder = false;

        $(".pathElement ").each(function (index, value){
            if(!lastFolder) {
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
</script>
