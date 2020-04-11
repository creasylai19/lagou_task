<%--
  Created by IntelliJ IDEA.
  User: laicreasy
  Date: 2020/3/10
  Time: 12:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>修改</title>
</head>
<body>
<div align="center">
    <h2>修改</h2>
    <form method="post" action="${pageContext.request.contextPath}/resume/save">
        <input type="text" name="id" value="${resume.id}" hidden>
        用户名：<input type="text" name="name" value="${resume.name}">
        地址：<input type="text" name="address" value="${resume.address}">
        手机号：<input type="text" name="phone" value="${resume.phone}">
        <input type="submit" value="提交">
    </form>
</div>
</body>
</html>
