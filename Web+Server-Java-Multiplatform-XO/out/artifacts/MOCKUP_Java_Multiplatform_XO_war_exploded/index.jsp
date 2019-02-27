<%@ page import="classes.handlers.database.DatabaseHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>X & O</title>
    <script src="scripts/script_index.js"></script>
    <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/stylesheets/stylesheet_index.css" />
</head>

<%if (session.getAttribute("playerUsername") == null) {%>

<body>
<header><center>~X & O~</center></header>
<br>
<section>
    Username: <input id="username_field" type="text"/>
    <br>
    <br>
    Password: <input id="password_field" type="password"/>
    <br>
    <br>
    <button id="login_button" class="forwardButton"><span>Login</span></button>
<%System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");%>
    <button id="signup_button" class="forwardButton"><span>Signup</span></button>
</section>
</body>

<%} else if (session.getAttribute("playerUsername") != null) {%>

<body>
<section>
    Bine ai venit, <%=session.getAttribute("playerUsername")%>!
    <br>
    <br>
    <button id="logout_button" class="cancelButton"><span>Logout</span></button>

    <button id="enter_lobby_button" class="forwardButton"><span>Enter Lobby</span></button>
</section>
</body>

<%}%>

</html>
