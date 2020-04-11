<%--
  Created by IntelliJ IDEA.
  User: laicreasy
  Date: 2020/3/9
  Time: 20:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>列表</title>
</head>
<body>

<div align="center" >
    <h2>列表</h2>
    <a href="${pageContext.request.contextPath}/resume/add">新增</a>
    <table border="1">
        <tr class="success">
            <th>编号</th>
            <th>姓名</th>
            <th>地址</th>
            <th>手机号</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${resumes}" var="resume" varStatus="s">
            <tr>
                <td>${s.count}</td>
                <td>${resume.name}</td>
                <td>${resume.address}</td>
                <td>${resume.phone}</td>
                <td><a href="${pageContext.request.contextPath}/resume/update?id=${resume.id}">修改</a>&nbsp;<a href="${pageContext.request.contextPath}/resume/delete?id=${resume.id}">删除</a></td>
            </tr>
        </c:forEach>

    </table>

    <br>
    <h2>我是服务器：${pageContext.request.localPort}</h2>
    <h2>当前sessionId：${pageContext.session.id}</h2>
</div>


</body>
</html>
