var ajaxUrl = "ajax/content/";

function remove() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "remove",
        data: $("#content_form").serialize(),
        success: function () {
            $('#modalForRemove').modal('hide');
            $("input.choise_checkbox:checked").parent().parent().remove();
        }
    });

}

function deleteContent() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "delete",
        data: $("#content_form").serialize(),
        success: function () {
            $('#modalForDelete').modal('hide');
            $("input.choise_checkbox:checked").parent().parent().remove();
        }
    });
}

function restore() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "restore",
        data: $("#content_form").serialize(),
        success: function () {
            $("input.choise_checkbox:checked").parent().parent().remove();
        }
    });
}

function createFolder() {
    alert("CreateFolder");
    $.ajax({
        type: "POST",
        url: ajaxUrl + "createFolder",
        data: $("#create_folder").serialize(),
        success: function () {
            $("#modalForNewFolder").modal("hide");
            var typeOfView = $("#typeOfView").val();
            var newTr = "";
            $("#myTable").find('tbody')
                .append($('<tr>')
                    .append($('<td>').append("text"))
                    .append($('<td>').append("text"))
                    .append($('<td>').append("text"))
                    .append($('<td>').append("text"))
                    .append($('<td>').append("text"))
                );
        }
    });
}

function rename() {
    var newName = $("#content_form input#newName").val();
    $.ajax({
        type: "POST",
        url: ajaxUrl + "rename",
        data: $("#content_form").serialize(),
        success: function () {
            $('#modalForRename').modal('hide');
            var checkbox = $("input.choise_checkbox:checked");
            var tr = checkbox.parent().parent();

            if (tr.hasClass("choise_folder")) {
                checkbox.parent().next().find("strong")
                    .replaceWith('<strong><span class="name_of_content">' + newName + '</span></strong>');
            } else {
                checkbox.parent().next().find("span")
                    .replaceWith('<span class="name_of_content">' + newName + '</span>');
            }
        }
    });
}

function replace() {
    var currentPlace = $("input#currentFolderID").val();
    var newPlace = $("input#move_to").val();
    // alert(currentPlace + " " + newPlace);
    $.ajax({
        type: "POST",
        url: ajaxUrl + "replace",
        data: $("#content_form").serialize()
    });
}

function addStar() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "addStar",
        data: $("#content_form").serialize(),
        success: function () {
            var checkbox = $("input.choise_checkbox:checked");
            if (!checkbox.parent().next().find("span.glyphicon").hasClass("glyphicon-star")) {
                checkbox.parent().next().append('<span class="glyphicon glyphicon-star"></span>');
            }
        }
    });
}

function removeStar() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "removeStar",
        data: $("#content_form").serialize(),
        success: function () {
            var checkbox = $("input.choise_checkbox:checked");
            if (checkbox.parent().next().find("span.glyphicon").hasClass("glyphicon-star")) {
                checkbox.parent().next().find("span.glyphicon-star").remove();
            }
        }
    });
}

function share() {
    alert($(div.share_for).length);
    $.ajax({
        type: "POST",
        url: ajaxUrl + "share",
        data: $("#content_form").serialize(),
        success: function () {
            $('#modalForShare').modal('hide');
            var checkbox = $("input.choise_checkbox:checked");
            if (!checkbox.parent().next().find("span.glyphicon").hasClass("glyphicon-eye-open")) {
                checkbox.parent().next().append('<span class="glyphicon glyphicon-eye-open"></span>');
            }
        }
    });
}

function addToMe() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "addToMe",
        data: $("#content_form").serialize()
    });
}