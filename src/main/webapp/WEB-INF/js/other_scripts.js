/**
 * Created by User on 21.06.2017.
 */

$(document).ready(function () {
    $('.record_table tr').click(function (event) {
        if (event.target.type !== 'checkbox') {
            $(':checkbox', this).trigger('click');
        }
    });
});


onload = function() { tree("tree", "/ajax/load_tree_of_catalog") }

document.oncontextmenu = function (){return false};


$('#myTable input[type="checkbox"]').click(function(){
    if($(this).prop('checked'))
        $(this).parent().parent().addClass('selected');
    else
        $(this).parent().parent().removeClass('selected');
});

jQuery(function($) {
    $('.folder_href').click(function() {
        return false;
    }).dblclick(function() {
        window.location = this.href;
        return false;
    });
});

