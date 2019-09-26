/**********************************************/
/* Correct filling of the registration's form */
/**********************************************/

$(document).ready(function(){
    var amountOfEmptyFields;
    var numOfLastField;
    var nameOfLastField;
    var valueOfLastField;
    var emailPattern = /^[a-z0-9_-]+@[a-z0-9-]+\.[a-z]{2,6}$/i;


    $("input.form-control").focusout(function(){

        if($(this).hasClass("mail")) {
            nameOfLastField = "mail";  numOfLastField = 0;
        }else if($(this).hasClass("login")) {
            nameOfLastField = "login";  numOfLastField = 1;
        }else if($(this).hasClass("password")) {
            nameOfLastField = "password";  numOfLastField = 2;
        }else if($(this).hasClass("conf_password")) {
            nameOfLastField = "conf_password";  numOfLastField = 3;
        }

        valueOfLastField = document.getElementById(nameOfLastField).value;

        if(nameOfLastField == "mail" && valueOfLastField.search(emailPattern) != 0){
            $("span." + nameOfLastField).remove();
            $("<span class=\'error " + nameOfLastField + "\'  style='color: #ff0000'>Неправильный формат E-mail.</span>")
                .insertAfter("form input:eq(" + numOfLastField + ")");
            $('#' + nameOfLastField).addClass(nameOfLastField + '-error');
        }else if((nameOfLastField == "conf_password" || nameOfLastField == "password") &&
            (document.getElementById("password").value != "" && document.getElementById("conf_password").value != "") &&
            (document.getElementById("conf_password").value != document.getElementById("password").value)){
            $("span." + nameOfLastField).remove();
            $("<span class=\'error " + nameOfLastField + "\'  style='color: #ff0000'>Пароли не совпадают. Повторите попытку.</span>")
                .insertAfter("form input:eq(" + numOfLastField + ")");
            $('#' + nameOfLastField).addClass(nameOfLastField + '-error');
        }else if(valueOfLastField == '' && (!$('#' + nameOfLastField).hasClass(nameOfLastField + '-error'))){
            $("<span class=\'error " + nameOfLastField + "\'  style='color: #ff0000'>Это поле должно быть заполнено.</span>")
                .insertAfter("form input:eq(" + numOfLastField + ")");
            $('#' + nameOfLastField).addClass(nameOfLastField + '-error');
        }else if(valueOfLastField == '' && ($('#' + nameOfLastField).hasClass(nameOfLastField + '-error'))){
        }else {
            $("span." + nameOfLastField).remove();
            $('#' + nameOfLastField).removeClass(nameOfLastField + '-error');
        }
    });
});