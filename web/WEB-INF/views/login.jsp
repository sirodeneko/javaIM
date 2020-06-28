<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2020/5/27
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <link href="http://cdn.xiaosiro.cn/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="http://cdn.xiaosiro.cn/css/style.css">
    <title>Login</title>
</head>
<body>
    <img src="http://cdn.xiaosiro.cn/img/back.png" alt="" class="wave">
    <div class="container">
        <div class="img">

        </div>
        <div class="login-box">
            <form action="/Login" method="post">
                <img src="http://cdn.xiaosiro.cn/img/11.png" alt="" class="avatar">
                <h2>Welcome to IM</h2>
                <div class="input-group">
                    <div class="icon">
                        <i class="fa fa-user"></i>
                    </div>
                    <div>
                        <h5>Username</h5>
                        <input type="text" class="input" name="name">
                    </div>
                </div>
                <div class="input-group">
                    <div class="icon">
                        <i class="fa fa-lock"></i>
                    </div>
                    <div>
                        <h5>Password</h5>
                        <input type="password" class="input" name="pwd">
                    </div>
                </div>
                <div style="color:#ff4d51;font-size: 4px">${msg}</div>
                <input type="submit" class="btn" value="Login">
            </form>
        </div>
    </div>
<script src="/js/app.js" type="text/javascript"></script>
</body>
</html>
