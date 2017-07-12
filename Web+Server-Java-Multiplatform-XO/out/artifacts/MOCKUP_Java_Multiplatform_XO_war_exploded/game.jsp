<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/stylesheets/stylesheet_game.css" />
    <script src="scripts/script_game.js"></script>
    <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
</head>
<body>

<div id="administrative_panel">
Player Username:
<%=session.getAttribute("playerUsername")%>
<br>
Opponent Username:
<%=session.getAttribute("opponentUsername")%>
<br>
GameSession ID:
<%=session.getAttribute("sessionId")%>
<br>
Player No.:
<%=session.getAttribute("playerNo")%>
<br>
<input id="return_to_menu_button" type="submit" value="Return to Menu"/>
</div>

<section>
    <table>
        <tr class="cont">
            <td>
                <button class="XOButton" id="r0c0">r0c0</button>
            </td>
            <td>
                <button class="XOButton" id="r0c1">r0c1</button>
            </td>
            <td>
                <button class="XOButton" id="r0c2">r0c2</button>
            </td>
        </tr>
        <tr>
            <td>
                <button class="XOButton" id="r1c0">r1c0</button>
            </td>
            <td>
                <button class="XOButton" id="r1c1">r1c1</button>
            </td>
            <td>
                <button class="XOButton" id="r1c2">r1c2</button>
            </td>
        </tr>
        <tr>
            <td>
                <button class="XOButton" id="r2c0">r2c0</button>
            </td>
            <td>
                <button class="XOButton" id="r2c1">r2c1</button>
            </td>
            <td>
                <button class="XOButton" id="r2c2">r2c2</button>
            </td>
        </tr>
    </table>
</section>
</body>
</html>
