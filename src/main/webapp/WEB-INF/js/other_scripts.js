// document.oncontextmenu = function () {
//     return false
// };

/******************************/
/* Upload with save structure */
/******************************/

var input = document.getElementById('files');
var structure = document.getElementById('structure');
var structureValue = "";

input.onchange = function (e) {
    var files = e.target.files;
    for (var i = 0, f; f = files[i]; ++i) {
        console.debug(files[i].webkitRelativePath);
        structureValue = structureValue + files[i].webkitRelativePath + ";";
    }
    var input = document.createElement("input");
    input.setAttribute("type", "hidden");
    input.setAttribute("name", "structure");
    input.setAttribute("value", structureValue);
    document.getElementById("folder_upload").appendChild(input);

}


/********************************/
/*       Ajax's scripts         */
/* Operations with modal windows*/
/* for share content            */

/********************************/

function onLoaded(data) {
    showFormForNewShare(data);

    $('.share_for_account').click(function () {
        showFormForEditOfShare(data)
    });
}


function showFormForNewShareForManySelect() {
    $('#modal_share').empty();
    var modalContent = $("#modal_share").first();
    modalContent.append('<div class="form-group"><input class="form-control" type="text" name="shareFor" placeholder="Укажите логин или почту человека"></div>' +
        '<div class="share_for_account"></div>');
}


function showFormForNewShare(data) {
    $('#modal_share').empty();
    var modalContent = $("#modal_share").first();
    modalContent.append('<div class="form-group"><input class="form-control" type="text" name="shareFor" placeholder="Укажите логин или почту человека"></div>' +
        '<div class="share_for_account"></div>');

    var modalContent = $(".share_for_account").first();

    modalContent.append('<span class="btn">Доступ открыт для: </span>');
    for (var i = 0; i < data.length; i++) {
        modalContent.append('<span class="info_block">' + data[i].login + '</span>');
    }
}


function showFormForEditOfShare(data) {
    $('#modal_share').empty();
    var modalContent = $("#modal_share").first();

    modalContent.append('<div class="form-group">');
    for (var i = 0; i < data.length; i++) {
        modalContent.append('<div class="share_for"><span>' + data[i].login + '</span>' +
            '<input hidden class="choise_checkbox choise_folder cancel_share" type="checkbox" name="cancel_share_for_users" value="' + data[i].id + '"/></div>');
    }
    modalContent.append('</div>');
    modalContent.append('<div class="form-group"><input type="submit" name="share" value="Готово"/>');
    selectShareUserForEdit();
}


function selectShareUserForEdit() {
    $(".share_for").click(function () {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            $(this)[0].getElementsByTagName('input')[0].checked = false;
        } else {
            $(this).addClass('selected');
            $(this)[0].getElementsByTagName('input')[0].checked = true;
        }
    });
}


/*Show old name of file or folder for rename*/


$(".li_rename").click(function () {
    var selectedContent = $("tr.selected");
    var oldName = selectedContent.find('td:nth-child(2)').find('span').html();
    var selectedContentId = selectedContent.find('td:nth-child(1)').attr("value");

    $("#newName").prop("value", oldName);
    $("#selectedContentId").prop("value", selectedContentId);
});

/*Show/hide memory graph on left side bar*/

$("#leftMenuSwitch").click(function () {
    $(".visibleState").toggleClass("hidden");
});


