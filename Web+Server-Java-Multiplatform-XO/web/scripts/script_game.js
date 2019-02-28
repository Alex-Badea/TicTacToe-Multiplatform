/**
 * Created by balex on 12.05.2017.
 */
window.onload = executeScript;
var pollInterval;

function executeScript() {
    var tableButtons = [];

    for (var row = 0; row < 3; row++) {
        (function (i) {
            tableButtons[i] = [];
        }(row));
    }

    for (row = 0; row < 3; row++) {
        for (var col = 0; col < 3; col++) {
            (function (i, j) {
                tableButtons[i][j] = document.getElementById("r" + i + "c" + j);
            }(row, col));
        }
    }

    document.getElementById("return_to_menu_button").onclick = function () {
        //$.post("/rest/onDisconnect/");
        window.location.href = "index.jsp";
    };

    for (row = 0; row < 3; row++) {
        for (col = 0; col < 3; col++) {
            (function (i, j) {
                tableButtons[i][j].onclick = function () {
                    $.post("/rest/selectedTableCell/", {row: i, col: j});
                };
            }(row, col));
        }
    }

    startPolling(tableButtons);
}

function startPolling(tableButtons) {
    pollInterval = setInterval(function () {
        update(tableButtons);
    }, 1000);
}

//  Sincronizeaza datele curente afisate si le actualizeaza cu cele obtinute de pe Server
function update(tableButtons) {
    $.get("/rest/pollIsGameSessionAvailable", function(gameSessionAvailable){
        if(gameSessionAvailable === "TRUE") {
            $.get("/rest/pollTableInSessionJson/", function (tableJson) {
                var table = JSON.parse(tableJson);
                $.get("/rest/pollIsCurrentPlayerTurn/", function (isCurrentPlayerTurn) {
                    $.get("/rest/pollWinningPlayer/", function (winningPlayer) {
                        for (var row = 0; row < 3; row++) {
                            for (var col = 0; col < 3; col++) {
                                (function (i, j) {
                                    tableButtons[i][j].textContent = table[i][j];
                                    tableButtons[i][j].disabled = (isCurrentPlayerTurn === "false") || table[i][j] !== "_" || winningPlayer !== "NULL";
                                }(row, col));
                            }
                        }

                        if (winningPlayer !== "NULL" && winningPlayer !== "TIE") {
                            clearInterval(pollInterval);
                            alert(winningPlayer + " won!");
                        }
                        else if (winningPlayer === "TIE") {
                            clearInterval(pollInterval);
                            alert("Tie");
                        }
                    });
                });
            });
        }
        else {
            alert("Session expired, you will be redirected.");
            window.location.href = "/index.jsp";
        }
    });
}
