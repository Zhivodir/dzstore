<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<link rel="stylesheet" href="/datatables/DataTables-1.10.16/css/dataTables.bootstrap.min.css">

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
  <div>
    <form id="content_form" action="/actions_above_checked_files" method="post">
      <input type="hidden" name="currentFolderID" value="${currentFolderID}">
      <input type="hidden" name="typeOfView" value="${typeOfView}">
      <table id="myTable" class="table table-striped record_table" cellspacing="0" width="100%">
        <thead>
        <tr>
          <th class="checkId"></th>
          <th class="checkName">Name</th>
          <th class="checkType">Type</th>
          <th class="checkOwner">Owner</th>
          <th class="checkSize">Size</th>
        </tr>
        </thead>
        <tbody>
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

<!-- DataTables -->
<script src="/datatables/DataTables-1.10.16/js/jquery.dataTables.min.js"></script>
<script src="/datatables/DataTables-1.10.16/js/dataTables.bootstrap.min.js"></script>
<script src="js/utils/sb-datatables.js"></script>

<script type="text/javascript">
  currentFolderId = ${currentFolderID != null ? currentFolderID : -1};
  typeOfView = '${typeOfView}';
  targetContent = getUrlForDataTables(typeOfView);

  $(document).ready(function () {

    table = $('#myTable').DataTable(datatableOpts(
        '/getContent/' + targetContent,
        [
          {
            data: null,
            render: function (data, type, full) {
              if (full.type == "folder") {
                return '<input hidden class="choise_checkbox choise_folder" type="checkbox" name="checked_folders_id" value="' + full.id + '"/>';
              }
              return '<input hidden class="choise_checkbox choise_file" type="checkbox" name="checked_files_id" value="' + full.id + '"/>';
            }
          },
          {
            data: 'name',
            class: 'forContextMenu',
            render: function (data, type, full) {
              var icons = full.starred ? '<span class="glyphicon glyphicon-star"></span>' : '';
              icons += full.shared ? '<span class="glyphicon glyphicon-eye-open"></span>' : '';

              if (full.type == "folder") {
                return '<strong><span class="name_of_content">' + data + '</span></strong>' + icons;
              }
              return '<span class="name_of_content">' + data + '</span>' + icons;
            }
          },
          {
            data: 'type',
            render: function (data, type, full) {
              if (data == "folder") {
                return '<span class="glyphicon glyphicon-folder-close" aria-hidden="true"></span>';
              }
              return data;
            }
          },
          {
            data: 'owner'
          },
          {
            data: 'size',
            render: function (data, type, full) {
              if (full.type == "folder") {
                return " - ";
              }
              return formatSize(data);
            }
          }
        ]
    ));

    selection = {
      single: function (el) {
        $('#myTable .choise_field').not(el).removeClass(this.cl);
        $(el).addClass(this.cl);
      },
      shift: function (el) {
        var tr = $('#myTable .choise_field');
        if (typeof this.last !== 'number') {
          return this.single(el);
        }
        var till = $(el).index(this.slcr),
            from = this.last;
        if (from > till) till = [from, from = till][0];
        tr.slice(from, till).add(el).addClass(this.cl);
      },
      ctrl: function (el) {
        $(el).toggleClass('selected');
        this.last = $(el).index(this.slcr);
      },
      slcr: '#myTable .choise_field',
      cl: 'selected',
      last: null
    };

    $('#myTable').on('click', '.choise_field', function (e) {
      method = !e.shiftKey && !e.ctrlKey ? 'single' : (e.shiftKey ? 'shift' : 'ctrl');
      selection[method](this);
      $('#myTable tr').each(function (indx, el) {
        $("input:checkbox").removeAttr("checked");
      })
      $('#myTable tr.selected').each(function (indx, el) {
        var inp = $("input:checkbox", el)[0];
        event.target != inp && (inp.checked = !inp.checked)
      })
    });

    $("#myTable").on('dblclick', '.choise_folder', function (e) {
      if (typeOfView == "bin") {
        $('#modalForOpenDataInBin').modal('show');
      } else {
        currentFolderId = $(this).find("input").val();
        var currentFolderName = $(this).find(".name_of_content").text();
        reloadContentForFolder(currentFolderId);
        addFolderNameToPath(currentFolderId, currentFolderName);
      }
    });

    $(".currentFolderPath").on('dblclick', '.levelPath', function (e) {
      currentFolderId = $(this).data("current-folder-id");
      reloadContentForFolder(currentFolderId);
      returnFolderPath(currentFolderId);
    });

    function reloadContentForFolder(currentFolder) {
      table.ajax.url('/getContent/' + currentFolder);
      table.ajax.reload();
    }
  });

  function getUrlForDataTables(typeOfView) {
    if (${typeOfView.equals("shared")}) {
      return 'shared/' + currentFolderId;
    }
    return ${!typeOfView.equals("index")} ? '${typeOfView}' : currentFolderId;
  }

  function createFolder() {
    var newFolderName = $("#newFolder").val();

    $.ajax({
      url: "/createFolder",
      type: 'POST',
      data: {
        currentFolderId: currentFolderId,
        newFolderName: newFolderName
      },
      success: function (result) {
        table.ajax.reload();
        $("#newFolder").attr(value, "");
      },
      error: function (result) {
      }
    })

    $("#modalForNewFolder").modal('hide');
  }

  function renameContent() {
    var selectedContent = $("tr.selected").find(".choise_checkbox");
    var selectedContentId = selectedContent.attr("value");
    var selectedContentType = selectedContent.hasClass("choise_folder") ? "folder" : "file";
    var newName = $("#newName").val();

    $.ajax({
      url: "/renameContent",
      type: 'POST',
      data: {
        contentType: selectedContentType,
        contentId: selectedContentId,
        newName: newName
      },
      success: function (result) {
        table.ajax.reload();
      },
      error: function (result) {
      }
    })

    $("#modalForRename").modal('hide');
  }

</script>
<%--<script src="js/operations_under_content.js"/>--%>