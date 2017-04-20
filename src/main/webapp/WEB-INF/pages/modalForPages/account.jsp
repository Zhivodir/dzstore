<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade" id="modalForAccount" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-Account">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-3 account-left-part">
                                    <a href="/logout" role="button">
                                        <div class="profile-header-img">
                                            <img class="img-circle" src="img/default.jpg" alt="photo"/>
                                        </div>
                                    </a>
                                </div>
                                <div class="col-md-6 account-right-part">
                                    <p><sec:authentication property="principal.username"/></p>
                                    <p><c:out value="${user.email}"></c:out></p>
                                    <a class="btn btn-lg btn-danger" href="/logout" role="button">Logout</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>