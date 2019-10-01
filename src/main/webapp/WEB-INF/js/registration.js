/**********************************************/
/* Correct filling of the registration's form */
/**********************************************/

$(document).ready(function () {
    var nameOfLastField;
    var valueOfLastField;
    var emailPattern = /^[a-z0-9_-]+@[a-z0-9-]+\.[a-z]{2,6}$/i;


    $("input.form-control").focusout(function () {

        if ($(this).hasClass("mail")) {
            nameOfLastField = "mail";
        } else if ($(this).hasClass("login")) {

            nameOfLastField = "login";
        } else if ($(this).hasClass("password")) {
            nameOfLastField = "password";
        }

        valueOfLastField = document.getElementById(nameOfLastField).value;

        if (nameOfLastField == "mail") {
            $("#emailError").prop("hidden", valueOfLastField.search(emailPattern) == 0);
        } else if (nameOfLastField == "password") {
            $("#pswdError").prop("hidden", valueOfLastField.length > 6);
        } else if (nameOfLastField == 'login') {
            $("#loginError").prop("hidden", valueOfLastField.length > 0);
        }
    });
});