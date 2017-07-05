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


/******************************/
/* Upload with save structure */
/******************************/

var input = document.getElementById('files');
var structure = document.getElementById('structure');
var structureValue = "";

input.onchange = function(e) {
    var files = e.target.files; // FileList
    for (var i = 0, f; f = files[i]; ++i){
        console.debug(files[i].webkitRelativePath);
        structureValue  = structureValue + files[i].webkitRelativePath + ";";
    }
    var input = document.createElement("input");
    input.setAttribute("type", "hidden");
    input.setAttribute("name", "structure");
    input.setAttribute("value", structureValue);
//append to form element that you want .
    document.getElementById("folder_upload").appendChild(input);

    // $('#form').append('<input type="hidden" name="fieldname" value="fieldvalue" />');
    // or other way
    //
    // $('<input>').attr({
    //     type: 'hidden',
    //     id: 'fieldId',
    //     name: 'fieldname'
    // }).appendTo('form')
}
