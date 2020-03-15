<%--
  Created by IntelliJ IDEA.
  User: curry
  Date: 2020/3/11
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
</head>
<body>
<p style="color: red;">${errorMsg}</p>
<form action="/login" method="post">
    <input name="username" type="text" />
    <br>
    <input name="password" type="password" />
    <br>
    <button type="submit">登录</button>
</form>

</body>
</html>
