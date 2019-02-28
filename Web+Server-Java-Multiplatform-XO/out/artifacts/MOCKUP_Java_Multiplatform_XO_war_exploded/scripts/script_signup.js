/**
 * Created by balex on 17.05.2017.
 */
window.onload = executeScript;

function executeScript() {
    var usernameField = document.getElementById("username_field");
    var passwordField = document.getElementById("password_field");
    var createAccountButton = document.getElementById("create_account_button");

    createAccountButton.onclick = function () {
        $.post("/rest/createAccount", {
            username: usernameField.value,
            password: passwordField.value
        }, function (successful) {
            if (successful === "TRUE") {
                window.location.href = "/index.jsp";
            }
            else if (successful === "FALSE") {
                alert("Error creating account");
            }
        });
    };
}