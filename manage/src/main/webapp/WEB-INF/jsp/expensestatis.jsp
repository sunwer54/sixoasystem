<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath(); //获取当前工程的根目录
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/"; //项目url根目录
%>
<html>
<head>
    <base href="<%=basePath%>"> <!--这个让此文件下的路径都相对于当前工程开始-->
    <title>Title</title>
</head>
<body style="height: 100%; margin: 0">
<div id="container" style="height: 100%"></div>

<script src="js/jquery.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts@5.3.2/dist/echarts.min.js"></script>
<script type="text/javascript">
    $(function () {
        //发送ajax请求后台数据
        $.get("getPayDatas",function (data) {
            /*var datas =[
                { value: 1048, name: '出差补贴' },
                { value: 735, name: 'Direct' },
                { value: 580, name: 'Email' },
                { value: 484, name: 'Union Ads' },
                { value: 300, name: 'Video Ads' }
            ];*/
            eval("var datas ="+data);
            var dom = document.getElementById("container");
            var myChart = echarts.init(dom);
            var app = {};

            var option;
            option = {
                title: {
                    text: '公司财务支出统计',
                    subtext: '来自财务部',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        name: '支出来自',
                        type: 'pie',
                        radius: '50%',
                        data: datas,
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };

            if (option && typeof option === 'object') {
                myChart.setOption(option);
            }
        })
    })
</script>
</body>
</html>
