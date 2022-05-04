<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath(); //获取当前工程的根目录
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/"; //项目url根目录
%>
<html>
<head>
    <base href="<%=basePath%>"> <!--这个让此文件下的路径都相对于当前工程开始-->
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>信息管理系统界面</title>
    </head>
    <frameset rows="88,*,31" cols="*" frameborder="no" border="0" framespacing="0">
        <frame src="top" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" title="topFrame" />
        <frameset cols="187,*" frameborder="no" border="0" framespacing="0">
            <frame src="left" name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame" title="leftFrame" />
            <frame src="index" name="rightFrame" id="rightFrame" title="rightFrame" />
        </frameset>
        <frame src="footer" name="bottomFrame" scrolling="No" noresize="noresize" id="bottomFrame" title="bottomFrame" />
    </frameset>
    <noframes><body>
</body></noframes>
</html>
