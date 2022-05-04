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
        </script>

    </head>

<body>

<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">报销管理</a></li>
        <li><a href="#">待审报销</a></li>
    </ul>
</div>

<div class="rightinfo">

    <div class="formtitle1"><span>待审报销</span></div>

    <table class="tablelist">
        <thead>
        <tr>
            <th>
                <input name="" type="checkbox" value="" checked="checked" />
            </th>
            <th>报销人<i class="sort"><img src="../images/px.gif" /></i></th>
            <th>报销总额</th>
            <th>报销时间</th>
            <th>总备注信息</th>
            <th>查看具体报销项</th>
            <th>查看所附图片</th>
            <th>审核</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${expenses}" var="exp">
        <tr>
            <td>
                <input name="" type="checkbox" value="" />
            </td>
            <td>${exp.employee.realname}</td>
            <td>${exp.totalamount}</td>
            <td>${exp.sdate}</td>
            <td>${exp.expdesc}</td>
            <td><a href="showDetail/${exp.expid}" class="tablelink"> 查看具体报销项</a></td>
            <td> <a href="showImg/${exp.expid}" class="tablelink">查看所附图片</a></td>
            <td><a href="javascript:open('audit?expid=${exp.expid}','','width=500,height=230,left=300,top=300,location=0,scrollbars=0,resizable=0')" class="tablelink">审核</a>  <a href="showAuditHistory/${exp.expid}" class="tablelink">查看审核记录</a></td>
        </tr>
        </c:forEach>

        </tbody>
    </table>

</div>

<script type="text/javascript">
    $('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>

</html>
