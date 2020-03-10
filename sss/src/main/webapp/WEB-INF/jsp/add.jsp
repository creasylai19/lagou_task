<%--
  Created by IntelliJ IDEA.
  User: laicreasy
  Date: 2020/3/9
  Time: 23:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>新增</title>
</head>
<body>

<div align="center">
    <h2>新增</h2>
    <form method="post" action="/resume/save">
        用户名：<input type="text" name="name">
        地址：<input type="text" name="address">
        手机号：<input type="text" name="phone">
        <input type="submit" value="提交">
    </form>
</div>
</body>
</html>
