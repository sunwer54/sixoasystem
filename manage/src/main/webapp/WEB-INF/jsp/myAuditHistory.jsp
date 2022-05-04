<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath(); //获取当前工程的根目录
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/"; //项目url根目录
%>
<html>
<head>
    <base href="<%=basePath%>"> <!--这个让此文件下的路径都相对于当前工程开始-->
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>无标题文档</title>
        <link href="css/style.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="js/jquery.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $(".click").click(function() {
                    $(".tip").fadeIn(200);
                });
                $(".tiptop a").click(function() {
                    $(".tip").fadeOut(200);
                });
                $(".sure").click(function() {
                    $(".tip").fadeOut(100);
                });
                $(".cancel").click(function() {
                    $(".tip").fadeOut(100);
                });
            });
            function back() {
                window.location.href="showMyAudit"
            }
        </script>

    </head>

<body>

<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">报销管理</a></li>
        <li><a href="#">查看审核记录</a></li>
    </ul>
</div>

<div class="rightinfo">

    <div class="formtitle1"><span>审核记录</span></div>

    <table class="tablelist">
        <thead>
        <tr>
            <th>
                <input name="" type="checkbox" value="" checked="checked" />
            </th>
            <th>审核人<i class="sort"><img src="../images/px.gif" /></i></th>
            <th>审核结果</th>
            <th>审核意见</th>
            <th>审核时间</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${list}" var="auditing">
        <tr>
            <td>
                <input name="" type="checkbox" value="" />
            </td>
            <td>${auditing.realname}</td>
            <c:if test="${auditing.result ==1}">
                <td>通过</td>
            </c:if>
            <c:if test="${auditing.result ==2}">
                <td>拒绝</td>
            </c:if>
            <c:if test="${auditing.result ==3}">
                <td>驳回</td>
            </c:if>
            <td>${auditing.auditdesc}</td>
            <td>${auditing.time}</td>

        </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
<input name="" type="button" class="btn" value="返回" onclick="back()"/>
<script type="text/javascript">
    $('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>

</html>