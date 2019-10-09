function datatableOpts(url, columns) {
    return {
        serverSide: true,
        select: true,
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
        },
        drawCallback:function( settings, json){
            changeRootOfPath();
            showBusySpace();
        }
    }
}

function datatableOpts2(url, columns) {
    return {
        select: {
            style: 'single',
            selector: 'tr.choise_folder'
        },
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
        createdRow: function (row, data, index) {
            $(row).addClass('shareUserRow');
        },
        drawCallback: function (settings) {
            $("#modal_share .amsify-select-tag").remove();
            $("#sharedForUsersTable").hide();
            $("#sharedForUsersTable thead").remove();
            var hasUsersWithAccess = $("#sharedForUsersTable").find(".shareUserRow").length > 0;
            showUsersTableStateInfo(hasUsersWithAccess);
        },
        columns: columns,
        bFilter: false,
        ordering: false,
        order: [[0, 'asc']],
        paging: false,
        bInfo: false
    }
}

function showUsersTableStateInfo(hasUsersWithAccess) {
    var textBlock = $("#usersTableStateInfo")
    textBlock.empty();
    if (hasUsersWithAccess) {
        textBlock.append('<a onclick="showShareForUsers()" class="showInfo">' + shareText['showUsersWithAccess'] + '</a>')
    } else {
        textBlock.append(shareText['noShareUsers']);
    }
}

function formatSize(length) {
    var i = 0, type = ['б', 'Кб', 'Мб', 'Гб'];
    while ((length / 1000 | 0) && i < type.length - 1) {
        length /= 1024;
        i++;
    }
    var numberInString = (length + "");
    var indexOfDecimalPoint = numberInString.indexOf('.');

    if(indexOfDecimalPoint != -1 && numberInString.charAt(indexOfDecimalPoint + 1) != 0) {
        return length.toFixed(1) + ' ' + type[i];
    } else {
        return length.toFixed(0) + ' ' + type[i];
    }

}