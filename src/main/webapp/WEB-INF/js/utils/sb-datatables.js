function datatableOpts(url, columns) {
  return {
    serverSide: true,
    ajax: {
      url: url,
      type: 'POST',
      contentType: 'application/json; charset=utf-8',
      data: function (d) {
        return JSON.stringify(d);
      }
    },
    columns: columns,
    bFilter: false,
    ordering: false,
    order: [[0, 'asc']],
    paging: false,
    bInfo: false,
    createdRow: function (row, data, index) {
      $(row).addClass('choise_field');
      if (data.type == "folder") {
        $(row).addClass('choise_folder');
      }
    }
  }
}

function datatableOpts2(url, columns) {
  return {
    ajax: {
      url: url,
      type: 'POST',
      contentType: 'application/json; charset=utf-8',
      data: function (d) {
        return JSON.stringify(d);
      }
    },
    columns: columns,
    bFilter: false,
    ordering: false,
    order: [[0, 'asc']],
    paging: false,
    bInfo: false,
    createdRow: function (row, data, index) {
      $(row).addClass('choise_field');
      if (data.type == "folder") {
        $(row).addClass('choise_folder');
      }
    }
  }
}


function datatableOpts3(url, columns) {
  return {
    ajax: {
      url: url,
      type: 'POST',
      contentType: 'application/json; charset=utf-8',
      data: function (d) {
        return JSON.stringify(d);
      }
    },
    columns: columns,
    bFilter: false,
    ordering: false,
    order: [[0, 'asc']],
    paging: false,
    bInfo: false
  }
}


function formatSize(length) {
  var i = 0, type = ['б', 'Кб', 'Мб', 'Гб', 'Тб', 'Пб'];
  while ((length / 1000 | 0) && i < type.length - 1) {
    length /= 1024;
    i++;
  }
  return length.toFixed(2) + ' ' + type[i];
}
