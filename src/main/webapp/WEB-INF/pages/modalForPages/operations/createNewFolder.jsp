<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForNewFolder" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-NewFolder">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                            <h4 class="modal-title" id="myModalLabel">Create new folder</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-inline" action="create_folder" method="post">
                                <div class="form-group">
                                    <label class="sr-only" for="newFolder">newFolder</label>
                                    <input type="hidden" name="currentFolder" value="${f}">
                                    <input type="hidden" name="typeOfView" value="${typeOfView}">
                                    <input type="text" class="form-control" id="newFolder" name="nameOfFolder"
                                           placeholder="Enter new folder name">
                                </div>
                                <button type="submit" class="btn btn-primary">Create</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>
