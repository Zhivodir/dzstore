$(document).ready(function () {
    var menu = $('#contextMenu');//get the menu
    var contentTable = $("#myTable tbody");

    contentTable.on('contextmenu', function (e) {
        e.preventDefault();
        var countContentRow = contentTable.find(".choise_field").length;

        if (countContentRow == 0) return;

        var allOfSelected = contentTable.find(".selected");
        var countOfSelected = contentTable.length;
        var countOfSelectedWithStar = allOfSelected.find(".glyphicon-star").length;
        var countOfSelectedForShare = allOfSelected.find(".glyphicon-eye-open").length;

        if (typeOfView == "shared") {
            $(".for_remove").attr("hidden", false);
            $(".li_rename").attr("hidden", true);
            $(".li_moveTo").attr("hidden", true);
            $(".for_bin").attr("hidden", true);
            $(".li_makeCopy").attr("hidden", createSelectedFilesMassiv().length == 0 && createSelectedFoldersMassiv().length > 0);

        } else if (typeOfView == "bin") {
            $(".for_bin").attr("hidden", false);
            $(".general").attr("hidden", true);
        } else {
            $(".for_remove").attr("hidden", false);
            $(".for_bin").attr("hidden", true);
            $(".li_rename").attr("hidden", countOfSelected != 1);
            $(".li_share").attr("hidden", !isSelectedContent());
            $(".li_starred").attr("hidden", !isAmongSelectedContentNotStarred());
            $(".li_removestar").attr("hidden", isSelectedContent() && !isAmongSelectedContentStarred());
            $(".li_addtome").attr("hidden", $("input[name='typeOfView']").val() != "shared");
            $(".li_makeCopy").attr("hidden", createSelectedFilesMassiv().length == 0 && createSelectedFoldersMassiv().length > 0);
            $(".li_moveTo").attr("hidden", !isSelectedContent());
        }

        menu.css({
            display: 'block',//show the menu
            top: e.pageY,
            left: e.pageX
        });

        function isSelectedContent() {
            return countOfSelected > 0;
        }

        function isAmongSelectedContentNotStarred() {
            return countOfSelected - countOfSelectedWithStar > 0;
        }

        function isAmongSelectedContentStarred() {
            return countOfSelectedWithStar > 0;
        }
    });


    $(document).click(function () { //When you left-click
        menu.css({display: 'none'});//Hide the menu
    });
});


function restoreContentHref() {
    var selectedFilesId = createSelectedFilesMassiv();
    var selectedFoldersId = createSelectedFoldersMassiv();
    restoreContent(selectedFiles, selectedFoldersId)
}

function modalRestoreContent() {
    var selectedFilesId = [];
    var selectedFoldersId = [$('#idFolderForRestore').val()];
    restoreContent(selectedFiles, selectedFoldersId)
    $("#modalForOpenDataInBin").modal("hide");
}

function restoreContent(selectedFiles, selectedFoldersId) {
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
        }
    })
}

