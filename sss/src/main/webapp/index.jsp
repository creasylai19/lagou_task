<%--
  Created by IntelliJ IDEA.
  User: laicreasy
  Date: 2020/3/9
  Time: 20:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>登录</title>
</head>
<body>

<div align="center">
    <h2>登录</h2>
    ${requestScope.message}
    <form action="${pageContext.request.contextPath}/user/login" method="post">
        用户名：<input type="text" name="username">
        密码：<input type="text" name="password">
        <input type="submit" value="提交">
    </form>
    <br>
    <h2>我是服务器：${pageContext.request.localPort}</h2>
    <h2>当前sessionId：${pageContext.session.id}</h2>
</div>
</body>
</html>
