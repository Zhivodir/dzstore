<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<style>
  #alreadyShareFor {
    padding: 5px;
  }
</style>

<div class="container-fluid">
  <div class="row">
    <div class="col-md-12">
      <!-- start of Modal -->
      <div class="modal fade modal-coordinate" id="modalForShare" tabindex="-1"
           role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content all-modal-change">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
              <h4 class="modal-title" id="myModalLabel"><s:message code="modal.share.title"/></h4>
            </div>
            <div class="modal-body" id="modal_share">
              <input name="blockShareFor" type="text" class="form-control" id="shareFor" placeholder="<s:message code="modal.share.input.placeholder"/>"
                     data-email-pattern="${serverConfig.bot.emailPattern}">
              <div id="showShareForUsers" hidden><a onclick="showShareForUsers()"><s:message code="modal.show.list.href"/></a></div>
              <div id="hideShareForUsers" hidden><a onclick="hideShareForUsers()"><s:message code="modal.hide.list.href"/></a></div>
              <div id="noShared" hidden><s:message code="modal.share.no.users"/></div>
              <table id="sharedForUsersTable" class="table" cellspacing="0" width="100%" hidden>
                <tbody>
                </tbody>
              </table>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="modal.button.cancel"/></button>
              <button type="button" class="btn btn-primary" onclick="changeSharedList()"><s:message code="modal.button.done"/></button>
            </div>
          </div>
        </div>
      </div>
      <!-- end of Modal -->
    </div>
  </div>
</div>

<!-- Amsify Plugin -->
<link rel="stylesheet" href="/taggingSystems/amsify.suggestags.css">
<script src="/taggingSystems/jquery.amsify.suggestags.js"></script>

<script>
  var blockShareFor = $('input[name="blockShareFor"]');

  blockShareFor.amsifySuggestags({
    type: 'amsify',
    type: 'bootstrap',
    validationCorrect: function (value) {
    }
  });

  function changeSharedList() {
    var isUsersWithAccessTableHidden = $("#sharedForUsersTable").attr("hidden") == true;

    var selectedFiles = createSelectedFilesMassiv();
    var selectedFolders = createSelectedFoldersMassiv();
    var cancelShareForUsers = [];
    if(!isUsersWithAccessTableHidden) {
      cancelShareForUsers = getSelectedUsersList();
    }

    $.ajax({
      url: "/changeSharedList",
      type: 'POST',
      traditional: true,
      data: {
        selectedFiles: selectedFiles,
        selectedFolders: selectedFolders,
        shareForUsers: $("#modalForShare input").val(),
        cancelShareForUsers: cancelShareForUsers
      },
      success: function (result) {
        table.ajax.reload();
      },
      error: function (result) {
      }
    })

    $("#modalForShare").modal('hide');
  }


  $(document).ready(function () {
    usersWithAccess = $('#sharedForUsersTable').DataTable(datatableOpts3(
      '/getUsersWithAccess/noSelect/-1',
      [
        {
          data: null,
          render: function (data, type, full) {
            return '<strong><span class="userLogin">' + full.login + '</span></strong> <span>(<span class="userEmail">' + full.email + '</span>)</span>';
          }
        }
      ]
    ));

    $(".li_share").click(function () {
      prepareShareModal();
    });

    function prepareShareModal() {
      var selectedFiles = createSelectedFilesMassiv();
      var selectedFolders = createSelectedFoldersMassiv();
      var isSelectedOnlyOneObjects = selectedFolders.length + selectedFiles.length == 1;
      usersWithAccess.clear().draw();
      if (isSelectedOnlyOneObjects) {
        usersWithAccess.ajax.url(formUrlForUsersWithAccessTable());
        usersWithAccess.ajax.reload();
      }
    }
  });


  function formUrlForUsersWithAccessTable() {
    var url = '/getUsersWithAccess/';
    var selectedContent = $("#myTable tr.selected");
    var selectedContentId = selectedContent.find(".choise_checkbox").attr("value");
    var selectedContentType = selectedContent.hasClass("choise_folder") ? "folder" : "file";
    return url + selectedContentType + '/' + selectedContentId;
  }

  $("#sharedForUsersTable").on("click", "tr", function () {
    $(this).toggleClass("selected");
  })

  function getSelectedUsersList() {
    var selectedUsersList = [];
    $("#sharedForUsersTable").find("tr.selected").each(function () {
      selectedUsersList.push($(this).find(".userEmail").text());
    });
    return selectedUsersList;
  }

  function showShareForUsers() {
    $("#sharedForUsersTable").show();
    $("#hideShareForUsers").show();
    $("#showShareForUsers").hide();
  }

  function hideShareForUsers() {
    $("#sharedForUsersTable").hide();
    $("#hideShareForUsers").hide();
    $("#showShareForUsers").show();
  }
</script>