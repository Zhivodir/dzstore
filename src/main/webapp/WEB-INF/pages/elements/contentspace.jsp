<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
    <div class="table-responsive">
        <form action="/actions_above_checked_files" method="post">
            <input type="hidden" name="currentFolderID" value="${currentFolderID}">
            <input type="hidden" name="typeOfView" value="${typeOfView}">
            <table id="myTable" class="table table-striped record_table">
                <thead>
                    <tr>
                        <th class="checkId"></th>
                        <th class="checkName" >Name</th>
                        <th class="checkType">Type</th>
                        <th class="checkOwner">Owner</th>
                        <th class="checkSize">Size</th>
                    </tr>
                </thead>
                <tbody>
                <c:if test="${typeOfView.equals('folder')}">
                    <tr>
                        <td></td>
                        <td><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:if>
                <c:forEach items="${content[1]}" var="currentFolder">
                    <tr class="choise_field choise_folder">
                        <td><input hidden class="choise_checkbox choise_folder" type="checkbox" name="checked_folders_id" value="${currentFolder.id}"/></td>
                        <td class="forContextMenu">
                            <c:choose>
                                <c:when test="${typeOfView.equals('bin')}">
                                    <a class="folder_href" href="#?currentFolderID=${currentFolder.id}" ondblclick="$('#modalForOpenDataInBin').modal('show')"><strong>${currentFolder.name}</strong></a>
                                </c:when>
                                <c:when test="${typeOfView.equals('shared')}">
                                    <a class="folder_href" href="/shared?currentFolderID=${currentFolder.id}"><strong>${currentFolder.name}</strong></a>
                                </c:when>
                                <c:otherwise>
                                    <a class="folder_href" href="/index?currentFolderID=${currentFolder.id}"><strong>${currentFolder.name}</strong></a>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${!typeOfView.equals('shared') && currentFolder.shareFor.size() ne 0}">
                                <span class="glyphicon glyphicon-eye-open"></span>
                            </c:if>
                            <c:if test="${currentFolder.starred eq true}">
                                <span class="glyphicon glyphicon-star"></span>
                            </c:if>
                        </td>
                        <td><span class="glyphicon glyphicon-folder-close" aria-hidden="true"></span></td>
                        <td>${currentFolder.user.login}</td>
                        <td>  -  </td>
                    </tr>
                </c:forEach>
                <c:forEach items="${content[0]}" var="currentFile">
                    <tr class="choise_field">
                        <td><input hidden class="choise_checkbox choise_folder" type="checkbox" name="checked_files_id" value="${currentFile.id}"/></td>
                        <td>${currentFile.name}
                            <c:if test="${!typeOfView.equals('shared') && currentFile.shareFor.size() ne 0}">
                                <span class="glyphicon glyphicon-eye-open"></span>
                            </c:if>
                            <c:if test="${currentFile.starred eq true}">
                                <span class="glyphicon glyphicon-star"></span>
                            </c:if>
                        </td>
                        <td>${currentFile.type}</td>
                        <td>${currentFile.user.login}</td>
                        <td>
                            <c:choose>
                                <c:when test="${currentFile.size/1073741824 gt 1}">
                                    <fmt:formatNumber type="number"
                                                      pattern="###.##" value="${currentFile.size/1073741824}" /> Gb
                                </c:when>
                                <c:when test="${currentFile.size/1048576 gt 1}">
                                    <fmt:formatNumber type="number"
                                                      pattern="###.##" value="${currentFile.size/1048576}" /> Mb
                                </c:when>
                                <c:when test="${currentFile.size/1024 ge 1}">
                                    <fmt:formatNumber type="number"
                                                      pattern="###.##" value="${currentFile.size/1024}" /> Kb
                                </c:when>
                                <c:otherwise>
                                    ${currentFile.size} bytes
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:choose>
                <c:when test="${typeOfView.equals('bin')}">
                    <c:import url="/WEB-INF/pages/context_menu/for_bin.jsp"/>
                    <c:import url="/WEB-INF/pages/modalForPages/operations/delete.jsp"/>
                    <c:import url="/WEB-INF/pages/modalForPages/errors/dataInBin.jsp"/>
                </c:when>
                <c:otherwise>
                    <c:import url="/WEB-INF/pages/context_menu/general.jsp"/>
                    <c:import url="/WEB-INF/pages/modalForPages/operations/remove.jsp"/>
                    <c:import url="/WEB-INF/pages/modalForPages/operations/share.jsp"/>
                    <c:import url="/WEB-INF/pages/modalForPages/operations/rename.jsp"/>
                    <c:import url="/WEB-INF/pages/modalForPages/operations/replace.jsp"/>
                </c:otherwise>
            </c:choose>
        </form>
    </div>
</div>
