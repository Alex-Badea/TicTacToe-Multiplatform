<%--
  Created by IntelliJ IDEA.
  User: balex
  Date: 13.05.2017
  Time: 17:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="scripts/script_signup.js"></script>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/stylesheets/stylesheet_signup.css" />
<html>
<head>
    <title>X & O Signup</title>
</head>
<body>
<section>
Username:<input id="username_field" type="text"/>
<br>
<br>
Password:<input id="password_field" type="password"/>
<br>
<br>
<button id="create_account_button" class="forwardButton">Create account</button>
</section>
</body>
</html>
