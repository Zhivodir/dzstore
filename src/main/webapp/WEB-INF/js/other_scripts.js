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

