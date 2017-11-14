/**
 * Created by User on 21.06.2017.
 */

onload = function() { tree("tree", "/ajax/load_tree_of_catalog") }


document.oncontextmenu = function (){return false};


var tr = $('#myTable .choise_field'),
    selection = {
        single: function(el) {
            tr.not(el).removeClass(this.cl);
            this.ctrl(el);
        },
        shift: function(el) {

            if (typeof this.last !== 'number') {
                return this.single(el);
            }
            var till = $(el).index(this.slcr),
                from = this.last;
            if (from > till) till = [from, from = till][0];
            tr.not(tr.eq(this.last)).removeClass(this.cl);
            tr.slice(from, till).add(el).addClass(this.cl);
        },
        ctrl: function(el) {
            if($(el).hasClass('selected')){
                $(el).removeClass(this.cl);
            }else{
                $(el).addClass(this.cl);
            }
            this.last = $(el).index(this.slcr);
            console.log(this.last);
        },
        slcr: '#myTable .choise_field',
        cl: 'selected',
        last: null
    };


tr.on('click', function(e) {
     method = !e.shiftKey && !e.ctrlKey ? 'single' : (e.shiftKey ? 'shift' : 'ctrl');
    selection[method](this);
    $('#myTable tr').each(function(indx, el) {
        $("input:checkbox").removeAttr("checked");
    })
    $('#myTable tr.selected').each(function(indx, el) {
        var inp = $("input:checkbox", el)[0];
        event.target != inp && (inp.checked = !inp.checked)
    })
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
    var files = e.target.files;
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

}


/********************************/
/*       Ajax's scripts         */
/* Operations with modal windows*/
/* for share content            */
/********************************/

$(".li_share").click(function(){
    var folders = new Array();
    var files = new Array();

    var i = 0;
    $("#content_form input:checkbox:checked.choise_folder").each(function( index ) {
        folders[i] = $(this).val();
        i++;
    });

    var i = 0;
    $("#content_form input:checkbox:checked.choise_file").each(function( index ) {
        files[i] = $(this).val();
        i++;
    });

    //var modalContent = $(".share_for_account").first();
    //Если выбран только один элемент контента - то можно увидеть
    // , для каких пользователей он расшарен
    if(folders.length + files.length == 1){
        var res = $.post(
            "/ajax/load_share_account_for_content",
            {folders: folders.toString(), files: files.toString()},
            onLoaded,
            'json'
        );
    }
});


function onLoaded(data) {
    var modalContent = $(".share_for_account").first();

    for(var i = 0; i < data.length; i++){
        modalContent.append('<span class="show_login_for_share">' + data[i].login + '</span>');
    }

    $('.share_for_account').click(function(){
        $('#modal_share').empty();
        var modalContent = $("#modal_share").first();

        modalContent.append('<div class="form-group">');
        for(var i = 0; i < data.length; i++){
            modalContent.append('<div><span>' + data[i].login + '</span>' +
            '<input class="choise_checkbox choise_folder" type="checkbox" name="users" value="' + data[i].id + '"/></div>');
        }
        modalContent.append('</div>');
        modalContent.append('<div class="form-group"><input type="submit" name="share" value="Готово"/></div>')
    });
}


// удаляет данные из модального окна при его закрытии
// $('a[data-toggle="modal"]').on('hidden', function() {
//     // $(this).data('modal').$element.removeData();
//     $('#modal_share').empty();
// });

