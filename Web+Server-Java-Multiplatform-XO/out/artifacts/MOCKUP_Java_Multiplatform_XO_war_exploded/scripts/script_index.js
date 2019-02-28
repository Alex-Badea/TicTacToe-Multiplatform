window.onload = executeScript;

function executeScript() {
    var usernameField = document.getElementById("username_field");
    var passwordField = document.getElementById("password_field");
    var loginButton = document.getElementById("login_button");
    var signupButton = document.getElementById("signup_button");

    var enterLobbyButton = document.getElementById("enter_lobby_button");
    var logoutButton = document.getElementById("logout_button");

    if (loginButton !== null)
        loginButton.onclick = function () {
            $.post("/rest/login", {
                username: usernameField.value,
                password: passwordField.value
            }, function (successful) {
                if (successful === "TRUE") {
                    window.location.href = "/index.jsp";
                }
                else if (successful === "FALSE") {
                    //TODO probabil prea vag
                    alert("Login unsuccessful");
                }
            });
        };

    if (signupButton !== null)
        signupButton.onclick = function () {
            window.location.href = "/signup.jsp"
        };

    if (enterLobbyButton !== null)
        enterLobbyButton.onclick = function () {
            $.get("/rest/isPlayerAlreadyConnected", function (playerAlreadyConnected) {
                if(playerAlreadyConnected === "FALSE"){
                    $.post("/rest/onConnect/");
                    startPolling();
                }
                else
                    alert("Already connected!");
            })
        };

    if (logoutButton !== null)
        logoutButton.onclick = function () {
            $.post("/rest/logout/");
            window.location.href = "/index.jsp";
        };
}

function startPolling() {
    setInterval(function () {
        update();
    }, 1000);
}

// Preia disponibilitatea lobby-ului curent si distinge intre 3 cazuri, cand jucatorul curent este in sesiune si lobby-ul
// devine null(in urma conectarii la sesiune), cand jucatorul curent este inca in lobby, iar lobby-ul inca exista si
// cand jucatorul este in lobby, dar acesta a expirat
function update() {
    $.get("/rest/pollIsLobbyAvailable", function (lobbyAvailable) {
        $.get("/rest/pollCurrentPlayerStatus", function (currentPlayerStatus) {
            if (currentPlayerStatus === "IN_SESSION" && lobbyAvailable === "FALSE") {
                $.post("rest/setCurrentPlayerSessionIdAndOpponentUsername");
                window.location.href = "/game.jsp";
            }
            else if (currentPlayerStatus === "IN_LOBBY" && lobbyAvailable === "TRUE") {
                document.getElementById("enter_lobby_button").disabled = true;
            }
            //n-o să se întâmple vreodată chestia asta...
            else if (currentPlayerStatus === "IN_LOBBY" && lobbyAvailable === "FALSE"){
                alert("Lobby expired, you will be redirected.");
                window.location.href = "/index.jsp";
            }
        });
    });
}