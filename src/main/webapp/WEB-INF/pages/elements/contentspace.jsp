<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<link rel="stylesheet" href="/datatables/DataTables-1.10.16/css/dataTables.bootstrap.min.css">

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
  <div>
    <form id="content_form" action="/download" method="post">
      <input type="hidden" name="currentFolderID" value="${currentFolderID}">
      <input type="hidden" name="typeOfView" value="${typeOfView}">
      <table id="myTable" class="table table-striped record_table" cellspacing="0" width="100%">
        <thead>
        <tr>
          <th class="checkId"></th>
          <th class="checkName"><s:message code="contentspace.name"/></th>
          <th class="checkType"><s:message code="contentspace.type"/></th>
          <th class="checkOwner"><s:message code="contentspace.owner"/></th>
          <th class="checkSize"><s:message code="contentspace.size"/></th>
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
              if (full.type == "folder" || full.size == 0) {
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
      } else if (typeOfView == "search") {
        currentFolderId = $(this).find("input").val();
        window.location.href='/?currentFolderID=' + currentFolderId
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
      table.ajax.url('/getContent/' + getUrlForDataTables(typeOfView));
      table.ajax.reload();
    }
  });

  function getUrlForDataTables(typeOfView) {
    if (${typeOfView.equals("shared")}) {
      return 'shared/' + currentFolderId;
    }
    if (${typeOfView.equals("bin")}) {
      return 'bin';
    }
    if (${typeOfView.equals("search")}) {
      return 'search/' + '${whatSearch}';
    }
    return ${!typeOfView.equals("index")} ? '${typeOfView}/' + currentFolderId : currentFolderId;
  }

  function createSelectedFilesMassiv() {
    var selectedFilesId = [];
    $("tr.selected").find(".choise_checkbox.choise_file").each(function () {
      selectedFilesId.push(this.value);
    });
    return selectedFilesId;
  }

  function createSelectedFoldersMassiv() {
    var selectedFoldersId = [];
    $("tr.selected").find(".choise_checkbox.choise_folder").each(function () {
      selectedFoldersId.push(this.value);
    });
    return selectedFoldersId;
  }
</script>