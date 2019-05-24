<%@ page import="com.gmail.dzhivchik.domain.enums.Language" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
  <div class="row">
    <div class="col-md-12">
      <!-- start of Modal -->
      <div class="modal fade modal-coordinate" id="settings" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
           aria-hidden="true">
        <div class="modal-dialog modal-NewFolder">
          <div class="modal-content all-modal-change">
            <form action="settings/changeLanguage" method="post">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Создание папки</h4>
              </div>
              <div class="modal-body">
                <div class="form-group">
                  <label class="sr-only" for="language">Выбрать язык</label>
                  <input name="typeOfView" value="${typeOfView}" type="hidden"/>
                  <select name="language" class="form-control" id="language" style="margin-bottom: 10px;">">
                    <c:forEach items="<%=Language.values()%>" var="language">
                      <option value="${language}"><s:message code="language.${language}"/></option>
                    </c:forEach>
                  </select>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                <button type="submit" class="btn btn-primary">Сохранить</button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <!-- end of Modal -->
    </div>
  </div>
</div>

