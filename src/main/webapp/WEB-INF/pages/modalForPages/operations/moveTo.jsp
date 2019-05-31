<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
  <div class="row">
    <div class="col-md-12">
      <!-- start of Modal -->
      <div class="modal fade modal-coordinate" id="modalForMoveTo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
           aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content all-modal-change">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
              <h4 class="modal-title" id="myModalLabel"><s:message code="modal.move.to.title"/></h4>
            </div>
            <div class="modal-body">
              <div class="currentFolderPath"></div>

              <table id="tableForMoveTo" class="table table-striped record_table" cellspacing="0" width="100%">
                <tbody>
                </tbody>
              </table>

            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="modal.button.cancel"/></button>
              <button type="button" class="btn btn-primary" onclick="moveTo()"><s:message code="modal.button.move"/></button>
            </div>
          </div>
        </div>
      </div>
      <!-- end of Modal -->
    </div>
  </div>
</div>


<script>
  pathBlock = $("#modalForMoveTo .currentFolderPath");

  function initStartingPath() {
    pathBlock.empty();
    pathBlock.append('<span class="glyphicon glyphicon-circle-arrow-left" aria-hidden="true"></span>')
    $(".contentmenu-place .currentFolderPath .levelPath").clone()
        .removeClass("levelPath").addClass("levelPathMoveTo").appendTo("#modalForMoveTo .currentFolderPath");
    $("#modalForMoveTo .pathElement").not(":last").hide();
  }

  function moveTo() {
    var moveToFolderId = $('#tableForMoveTo').find("tr.selected").find(".moveto_checkbox")[0].value;

    $.ajax({
      url: "/moveTo",
      type: 'POST',
      traditional: true,
      data: {
        selectedFiles: createSelectedFilesMassiv(),
        selectedFolders: createSelectedFoldersMassiv(),
        moveTo: moveToFolderId
      },
      success: function (result) {
        table.ajax.reload();
        $("#modalForMoveTo").modal("hide");
      },
      error: function (result) {
      }
    })
  }

  $(document).ready(function () {
    table2 = $('#tableForMoveTo').DataTable(datatableOpts(
        '/getContent/' + currentFolderId,
        [
          {
            data: null,
            render: function (data, type, full) {
              if (full.type == "folder") {
                return '<input hidden class="moveto_checkbox choise_folder" type="checkbox" name="checked_folders_id" value="' + full.id + '"/>';
              }
              return "";
            }
          },
          {
            data: 'name',
            class: 'forContextMenu',
            render: function (data, type, full) {
              if (full.type == "folder") {
                return '<strong><span class="name_of_content">' + data + '</span></strong>';
              }
              return '<span class="name_of_content">' + data + '</span>';
            }
          }
        ]
    ));

    selectionMoveTo = {
      single: function (el) {
        $('#tableForMoveTo .choise_field').not(el).removeClass(this.cl);
        $(el).addClass(this.cl);
      },
      slcr: '#tableForMoveTo .choise_field',
      cl: 'selected',
      last: null
    };

    $('#tableForMoveTo').on('click', '.choise_field', function (e) {
      if ($(this).hasClass("choise_folder")) {
        method = !e.shiftKey && !e.ctrlKey ? 'single' : (e.shiftKey ? 'shift' : 'ctrl');
        selectionMoveTo[method](this);
        $('#tableForMoveTo tr').each(function (indx, el) {
          $("input:checkbox").removeAttr("checked");
        })
        $('#tableForMoveTo tr.selected').each(function (indx, el) {
          var inp = $("input:checkbox", el)[0];
          event.target != inp && (inp.checked = !inp.checked)
        })
      }
    });

    $('#modalForMoveTo').on('click', '.glyphicon-circle-arrow-left', function (e) {
      if ($("#modalForMoveTo .pathElement").length > 1) {
        moveTo_RemoveFolderFromPath();
        comeBackToPrevFolder();
//        $('#tableForMoveTo').show();
//        $('#rootTable').hide();
      } else {
//        $('#tableForMoveTo').hide();
//        $('#rootTable').show();
      }
    });


    $("#tableForMoveTo").on('dblclick', '.choise_folder', function (e) {
      currentFolderId = $(this).find("input").val();
      var currentFolderName = $(this).find(".name_of_content").text();
      reloadMoveToContent(currentFolderId);
      moveTo_AddFolderNameToPath(currentFolderId, currentFolderName)
    });

    $(".li_moveTo").on("click", function () {
      reloadMoveToContent(currentFolderId);
    })

    function reloadMoveToContent(currentFolderId) {
      table2.ajax.url('/getContent/' + currentFolderId);
      table2.ajax.reload();
    }

    function changePrevFolderId(folderId) {
      $("#prevFolderId").val(folderId)
    }

    function moveTo_AddFolderNameToPath(targetFolderId, targetFolderName) {
      pathBlock.append('<span class="pathElement levelPathMoveTo" data-current-folder-id="' + targetFolderId + '">' + targetFolderName + '</span>');
      $("#modalForMoveTo .pathElement").not(":last").hide();
    }

    function moveTo_RemoveFolderFromPath() {
      $("#modalForMoveTo .pathElement:last").remove();
      $("#modalForMoveTo .pathElement:last").show();
    }

    function comeBackToPrevFolder() {
      prevFolderId = $("#modalForMoveTo .pathElement:last").data("current-folder-id");
      reloadMoveToContent(prevFolderId);
    }
  })
</script>