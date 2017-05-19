<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <!-- start of Modal -->
            <div class="modal fade" id="modalForReplace" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-FileUpload">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                                <span class="sr-only">Close</span>
                            </button>
                            <h4 class="modal-title">Move to...</h4>
                        </div>
                        <div class="modal-body">
                            <ul class="Container" id="tree">
                                <input class="radio_move_to" type="radio" name="move_to" value="tree"/>
                                <li class="Node IsRoot IsLast ExpandClosed">
                                    <div class="Expand"></div>
                                    <div class="Content">Каталог</div>
                                    <ul class="Container"></ul>
                                </li>
                            </ul>
                            <input type="submit" name="replace" value="replace"/>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end of Modal -->
        </div>
    </div>
</div>
