<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>sks-job</title>
<#include 'common/css.ftl' >
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<#include 'common/header.ftl'>
<#include 'common/menu.ftl'>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                首页
                <small>概况</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 首页</a></li>
                <li class="active">概况</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Small boxes (Stat box) -->
            <div class="row">
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-aqua">
                        <div class="inner">
                            <h3 id="count_1">-/-</h3>

                            <p>服务/应用</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-grid"></i>
                        </div>
                        <a href="#" class="small-box-footer">更多 <i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
                <!-- ./col -->
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-green">
                        <div class="inner">
                            <h3 id="count_2">-/-</h3>
                            <p>提供者/消费者</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-clock"></i>
                        </div>
                        <a href="#" class="small-box-footer">更多 <i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
                <!-- ./col -->
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-red">
                        <div class="inner">
                            <h3 id="count_3">-</h3>
                            <p>错误</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-close-circled"></i>
                        </div>
                        <a href="#" class="small-box-footer">更多 <i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
                <!-- ./col -->
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-yellow">
                        <div class="inner">
                            <h3 id="count_4">-</h3>
                            <p>警告</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-pause"></i>
                        </div>
                        <a href="#" class="small-box-footer">更多 <i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
                <!-- ./col -->
            </div>

            <!-- Custom tabs (Charts with tabs)-->
            <div class="nav-tabs-custom">
                <!-- Tabs within a box -->
                <ul class="nav nav-tabs pull-right">
                    <li><a href="#revenue-chart" onclick="changeType(3)" data-toggle="tab">24小时</a></li>
                    <li class="active"><a href="#revenue-chart" onclick="changeType(2)" data-toggle="tab">30分钟</a></li>
                    <li><a href="#revenue-chart" onclick="changeType(1)" data-toggle="tab">现在</a></li>
                    <li class="pull-left header"><i class="fa fa-inbox"></i> Count</li>
                </ul>
                <div class="tab-content no-padding">
                    <!-- Morris chart - Sales -->
                    <div class="chart tab-pane active" id="revenue-chart"
                         style="position: relative; height: 300px;"></div>
                </div>
            </div>
            <!-- /.nav-tabs-custom -->

            <div id="errorList">
                <div class="callout callout-danger">
                    <p><i class="icon fa fa-times-circle"></i>　[错误]：服务(product.gt.add) , 缺少提供者！<a href="#">详细</a></p>
                </div>
                <div class="callout callout-warning">
                    <p><i class="icon fa fa-warning"></i>　[警告]：服务(product.gt.online) , 执行失败！<a href="#">详细</a></p>
                </div>
            </div>
            <div class="callout">
                <p>注：以上为测试数据</p>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

<#include 'common/footer.ftl' >

</div>
<!-- ./wrapper -->

<#include 'common/js.ftl'>
<!-- 引入 ECharts 文件 -->
<script src="resource/plugins/echarts/echarts.min.js"></script>

<!-- page script -->
<script>
    var myChart;
    var option;
    var clock;
    $(function () {
        //load menu
        choiceMenu("main-index");

        // 初始化概况统计
        $.getJSON("monitor/overviewCount", "", function (data) {
            if (data.appCount && data.serviceCount) {
                $("#count_1").html(data.serviceCount + "/" + data.appCount);
            }
            if (data.providerCount && data.consumerCount) {
                $("#count_2").html(data.providerCount + "/" + data.consumerCount);
            } else if(data.providerCount) {
                $("#count_2").html(data.providerCount + "/-");
            }
            if (data.errorCount) {
                $("#count_3").html(data.errorCount);
            }
            if (data.warningCount) {
                $("#count_4").html(data.warningCount);
            }
        });

        init();

        clock = window.setInterval(function () {
            refreshData();
        }, 30000);
    });

    function init() {
        echarts.dispose(document.getElementById('revenue-chart'));
        // 基于准备好的dom，初始化echarts实例
        myChart = echarts.init(document.getElementById('revenue-chart'));
        // 初始化应用调用量统计图
        $.getJSON("resource/plugins/echarts/data_templet.json", "", function (data) {
            option = data;
            refreshData();
        });
    }

    // 默认查看30分钟
    var type = 2;
    function changeType(type) {
        clearInterval(clock); //清除js定时器
        //type 1:现在 2:30分钟 3:24小时
        this.type = type;
        init();
        var time = 30000;
        switch (type) {
            case 1 :
                time = 3000;
                break;
            case 2 :
                time = 30000;
                break;
            case 3 :
                time = 60000;
        }
        clock = window.setInterval(function () {
            refreshData();
        }, time);
    }

    /**
     * 刷新应用调用量
     */
    function refreshData() {
        if (!myChart) {
            return;
        }

        $.getJSON("monitor/getAllApplicationCount/" + type, "", function (data) {
            option.legend.data = data.appNames;
            option.xAxis[0].data = data.times;
            var arraySeries = new Array();
            data.appNames.forEach(function (appName, index) {
                var series = {
                    name: appName,
                    type: "line",
                    areaStyle: {},
                    smooth: true,
                    data: data.unitCountList[index]
                };
                arraySeries.push(series);
            });
            option.series = arraySeries;
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        });
    }
</script>
</body>
</html>
