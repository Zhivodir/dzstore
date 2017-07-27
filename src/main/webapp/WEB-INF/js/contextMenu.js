/**
 * Created by User on 21.03.2017.
 */

(function ($, window) {

    $.fn.contextMenu = function (settings) {

        return this.each(function () {

            // Open context menu
            $(this).on("contextmenu", function (e) {
                // return native menu if pressing control
                if (e.ctrlKey) return;
                var allOfSelected = $(".selected");
                var countOfSelected = allOfSelected.length;
                var countOfSelectedWithStar = $(".selected .glyphicon-star").length;
                var countOfSelectedForShare = $(".selected .glyphicon-eye-open").length;

                if(countOfSelected != 1){
                    $(".li_rename").attr("hidden", true);
                }else{
                    $(".li_rename").attr("hidden", false);
                }

                if(countOfSelected - countOfSelectedWithStar > 0){
                    $(".li_starred").attr("hidden", false);
                    $(".li_removestar").attr("hidden", true);
                }else{
                    $(".li_starred").attr("hidden", true);
                    $(".li_removestar").attr("hidden", false);
                }

                if(countOfSelected - countOfSelectedForShare > 0){
                    $(".li_share").attr("hidden", false);
                }else{
                    $(".li_share").attr("hidden", true);
                }

                if($(".currentFolderPath a.sharedWithMe").length != 0){
                    $(".li_replace").attr("hidden", true);
                }else{
                    $(".li_replace").attr("hidden", false);
                }

                if($("input[name='typeOfView']").val() != "shared"){
                    $(".li_addtome").attr("hidden", true);
                }else{
                    $(".li_addtome").attr("hidden", false);
                }

                //open menu
                var $menu = $(settings.menuSelector)
                    .data("invokedOn", $(e.target))
                    .show()
                    .css({
                        position: "absolute",
                        left: getMenuPosition(e.clientX - $("div.sidebar").outerWidth(), 'width', 'scrollLeft'),
                        top: getMenuPosition(e.clientY - $("div.navbar").outerHeight()*2, 'height', 'scrollTop')
                    })
                    .off('click')
                    .on('click', 'a', function (e) {
                        $menu.hide();

                        var $invokedOn = $menu.data("invokedOn");
                        var $selectedMenu = $(e.target);

                        settings.menuSelected.call(this, $invokedOn, $selectedMenu);
                    });

                return false;
            });

            //make sure menu closes on any click
            $('body').click(function () {
                $(settings.menuSelector).hide();
            });
        });

        function getMenuPosition(mouse, direction, scrollDir) {
            var win = $(window)[direction](),
                scroll = $(window)[scrollDir](),
                menu = $(settings.menuSelector)[direction](),
                position = mouse + scroll;

            // opening menu would pass the side of the page
            if (mouse + menu > win && menu < mouse)
                position -= menu;

            return position;
        }

    };
})(jQuery, window);

$("#myTable td").contextMenu({
    menuSelector: "#contextMenu",
    menuSelected: function (invokedOn, selectedMenu) {
        var msg = "You selected the menu item '" + selectedMenu.text() +
            "' on the value '" + invokedOn.text() + "'";
    }
});