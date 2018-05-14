<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade modal-coordinate" id="modalForRemove" tabindex="-1"
                 role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-standart">
                    <div class="modal-content all-modal-change">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">Перенести в корзину</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <input type="hidden" name="currentFolder" value="${f}">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                            <%--На ContentController--%>
                            <%--<button type="submit" name="action" value="remove" class="btn btn-primary">В корзину</button>--%>
                            <button type="submit" onclick="remove()" class="btn btn-primary">В корзину</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>

