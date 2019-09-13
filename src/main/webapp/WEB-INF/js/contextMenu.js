$(document).ready(function () {
    var menu = $('#contextMenu');//get the menu
    $("#myTable").on('contextmenu', function (e) {
        e.preventDefault();
        var allOfSelected = $(".selected");
        var countOfSelected = allOfSelected.length;
        var countOfSelectedWithStar = $(".selected .glyphicon-star").length;
        var countOfSelectedForShare = $(".selected .glyphicon-eye-open").length;

        $(".li_rename").attr("hidden", countOfSelected != 1);
        $(".li_share").attr("hidden", !(countOfSelected > 0));
        $(".li_starred").attr("hidden", !(countOfSelected - countOfSelectedWithStar > 0));
        $(".li_removestar").attr("hidden", countOfSelected - countOfSelectedWithStar > 0);
        $(".li_addtome").attr("hidden", $("input[name='typeOfView']").val() != "shared");
        $(".li_makeCopy").attr("hidden", createSelectedFilesMassiv().length == 0 || createSelectedFoldersMassiv().length > 0);

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