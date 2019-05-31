<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none">
  <li class=""><a href="#" class="contextHref" onclick="removeContent()"><s:message code="contextmenu.remove"/></a></li>
  <li class="li_download"><a href="#" class="contextHref" onclick="downloadContent()"><s:message code="contextmenu.download"/></a></li>
  <li class="divider"></li>
  <li class="contextHref li_starred"><input type="button" class="contextInput" onclick="changeStarState(true)"
                                            value="<s:message code="contextmenu.add.star"/>"></li>
  <li class="contextHref li_removestar"><input type="button" class="contextInput" onclick="changeStarState(false)"
                                               value="<s:message code="contextmenu.remove.star"/>"></li>
  <li class="divider"></li>
  <li class="li_rename"><a class="contextHref" href="#" data-toggle="modal" data-target="#modalForRename"><s:message code="contextmenu.rename"/></a>
  </li>
  <li class="li_share"><a class="contextHref" href="#" data-toggle="modal" data-target="#modalForShare"><s:message code="contextmenu.share"/></a></li>
  <li class="divider"></li>
  <li class="contextHref li_addtome"><input type="button" class="contextInput" onclick="addToMe()" value="<s:message code="contextmenu.add.to.me"/>">
  </li>
  <li class="li_moveTo"><a class="contextHref" href="#" data-toggle="modal" onclick="initStartingPath()" data-target="#modalForMoveTo"><s:message
      code="contextmenu.move.to"/></a></li>
</ul>

<script>
  function changeStarState(state) {
    $.ajax({
      url: "/changeStarState",
      type: 'POST',
      traditional: true,
      data: {
        selectedFiles: createSelectedFilesMassiv(),
        selectedFolders: createSelectedFoldersMassiv(),
        state: state
      },
      success: function (result) {
        table.ajax.reload();
      },
      error: function (result) {
      }
    })
  }

  function addToMe() {
    $.ajax({
      url: "/addToMe",
      type: 'POST',
      traditional: true,
      data: {
        selectedFiles: createSelectedFilesMassiv(),
        selectedFolders: createSelectedFoldersMassiv(),
      },
      success: function (result) {
        busySpace = result;
        showBusySpace();
      },
      error: function (result) {
      }
    })
  }

  function downloadContent() {
    postToUrl(
        '/download',
        {
          selectedFiles: createSelectedFilesMassiv(),
          selectedFolders: createSelectedFoldersMassiv(),
          typeOfView: '${typeOfView}',
          currentFolderID: currentFolderId
        }
        , 'POST');
  }

  function postToUrl(path, params, method) {
    method = method || "post";

    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);
    for (var key in params) {
      var hiddenField = document.createElement("input");
      hiddenField.setAttribute("type", "hidden");
      hiddenField.setAttribute("name", key);
      hiddenField.setAttribute("value", params[key]);

      form.appendChild(hiddenField);
    }

    document.body.appendChild(form);
    form.submit();
  }

  function removeContent() {
    $.ajax({
      url: "/removeContent",
      type: 'POST',
      traditional: true,
      data: {
        selectedFiles: createSelectedFilesMassiv(),
        selectedFolders: createSelectedFoldersMassiv(),
        typeOfView: '${typeOfView}'
      },
      success: function (result) {
        table.ajax.reload();
      },
      error: function (result) {
      }
    })

    $("#modalForRemove").modal('hide');
  }
</script>