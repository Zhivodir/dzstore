<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade" id="modalForImageProfileUpload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-FileUpload">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                            <h4 class="modal-title">Operations above photo</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-inline" enctype="multipart/form-data" action="newImageForProfile" method="post">
                                <input type="hidden" name="typeOfView" value="${typeOfView}">
                                <div class="form-group" style="float: left">
                                    <input type="file" name="file" placeholder="Choice file">
                                </div>
                                <button style="float: left; margin-top: -9px" type="submit"  class="btn btn-primary">Upload</button>
                            </form>
                        </div>
                        <div class="modal-body">
                            <form class="form-inline" enctype="multipart/form-data" action="deleteCurrentProfileImage" method="post">
                                <input type="hidden" name="typeOfView" value="${typeOfView}">
                                <button style="float: left; margin-top: -9px" type="submit"  class="btn btn-primary">Delete current photo</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

