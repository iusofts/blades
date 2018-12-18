<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>依赖关系</title>
<#include 'common/css.ftl' >
    <link href="resource/plugins/d3/css/style.css" rel="stylesheet"/>

</head>
<body class="hold-transition skin-blue sidebar-mini" style="overflow: hidden;">
<div class="wrapper">
<#include 'common/header.ftl' >
<#include 'common/menu.ftl' >
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- 画板 -->
        <div id="watermark">
            <div id="mysvg"></div>
            <div id="pos" style="position: absolute;"></div>
        </div>
        <!-- 遮罩层 -->
        <div id="zzc">
        </div>
    </div>
</div>
<#include 'common/js.ftl' >

<script type="text/javascript">
    var appRoot = '';
    //初始化关系图
    var width = 5000;//画板宽度
    var height = 5000;//画板高度
    var img_w = 50;//顶点图片宽度
    var img_h = 50;//顶点图片高度
    var link_d = parseInt('200', 10);//边距
    var charge = parseInt('-2000', 10);//电荷数
    var map_n = 1;//顶点个数（用于模拟测试）
    var map_g = 1;//分组数（用于模拟测试）
    var delNum = 0;//拓展前删除个数
    var zoom_num = 1;//放大倍数
    var zoom_xy = null;//svg位置
    var relation_show = 1;//关系展示类型：1.显示、2.隐藏、3.悬浮
</script>
<script src="resource/plugins/d3/d3.js" charset="utf-8"></script>
<script type="text/javascript" src="resource/plugins/d3/js/d3_graph.js"></script>

<script>

    function getNodeIcon(id) {
        var result = ''
        if (id == 'blades-monitor') {
            result = 'resource/plugins/d3/icon/2.png'
        }
        if (id == 'blades-job') {
            result = 'resource/plugins/d3/icon/5.png'
        }
        if (result == '') {
            return;
        }
        $("#" + id).attr("href", result);
    }


    $(window).ready(function () {
        //load menu
        choiceMenu("services-relationship");
        $.getJSON("monitor/getApplicationServiceDependency", function (data, status) {
            initGraph('blades-monitor,blades-job', data)
        });
    });
</script>
</body>
</html>
