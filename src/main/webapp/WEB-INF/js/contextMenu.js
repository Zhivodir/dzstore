$(document).ready(function () {
    var menu = $('#contextMenu');//get the menu
    $("#myTable").on('contextmenu', function (e) {
        e.preventDefault();
        var allOfSelected = $(".selected");
        var countOfSelected = allOfSelected.length;
        var countOfSelectedWithStar = $(".selected .glyphicon-star").length;
        var countOfSelectedForShare = $(".selected .glyphicon-eye-open").length;

        if(typeOfView == "bin"){
            $(".general").attr("hidden", true);
            $(".for_bin").attr("hidden", false);
        } else {
            $(".for_bin").attr("hidden", true);
            $(".general").attr("hidden", false);
            $(".li_rename").attr("hidden", countOfSelected != 1);
            $(".li_share").attr("hidden", !(countOfSelected > 0));
            $(".li_starred").attr("hidden", !(countOfSelected - countOfSelectedWithStar > 0));
            $(".li_removestar").attr("hidden", countOfSelected - countOfSelectedWithStar > 0);
            $(".li_addtome").attr("hidden", $("input[name='typeOfView']").val() != "shared");
            $(".li_makeCopy").attr("hidden", createSelectedFilesMassiv().length == 0 || createSelectedFoldersMassiv().length > 0);
        }

        menu.css({
            display: 'block',//show the menu
            top: e.pageY,
            left: e.pageX
        });
    });

    $(document).click(function () { //When you left-click
        menu.css({display: 'none'});//Hide the menu
    });
});

function restoreContent() {
    var selectedFilesId = createSelectedFilesMassiv();
    var selectedFoldersId = createSelectedFoldersMassiv();

    $.ajax({
        url: "/restoreContent",
        type: 'POST',
        traditional: true,
        data: {
            selectedFiles: selectedFilesId,
            selectedFolders: selectedFoldersId
        },
        success: function (result) {
            table.ajax.reload();
        },
        error: function (result) {
        }
    })
}

function modalRestoreContent(){
    var selectedFilesId = [];
    var selectedFoldersId = [$('#idFolderForRestore').val()];

    $.ajax({
        url: "/restoreContent",
        type: 'POST',
        traditional: true,
        data: {
            selectedFiles: selectedFilesId,
            selectedFolders: selectedFoldersId
        },
        success: function (result) {
            $("#modalForOpenDataInBin").modal("hide");
            table.ajax.reload();
        }
    })
}

