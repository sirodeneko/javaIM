<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2020/5/27
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<div class="top">
    <p>账户登陆</p>
</div>

<div class="middle">
    <form action="/Login" method="post">

        <span class="erro">${msg}</span>
        <span class="s1"></span>
        <span class="s2"></span>
        <input type="text" name="name" value=""  class="iputs"/>
        <input type="password" name="pwd" value="" class="iputs"/>
        <input type="submit" value="登陆"/>
    </form>
</div>
</body>
</html>
