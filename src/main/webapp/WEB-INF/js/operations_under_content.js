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