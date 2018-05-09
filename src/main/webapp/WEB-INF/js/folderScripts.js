var ajaxUrl = "ajax/content/";

function createFolder() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "createFolder",
        data: $("#createFolder").serialize(),
        success: function(){

        }
    });
}
