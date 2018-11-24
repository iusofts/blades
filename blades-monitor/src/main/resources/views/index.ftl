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
                            <h3 id="status_1">-/-</h3>

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
                            <h3><span id="status_0">-</span>/<span id="status_3">-</span></h3>

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
                            <h3>-</h3>

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
                            <h3 id="status_2">-</h3>

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
                    <li class="active"><a href="#revenue-chart" data-toggle="tab">调用</a></li>
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
<!-- Morris.js charts -->
<script src="resource/admin/plugins/ajax/libs/raphael/raphael-min.js"></script>
<script src="resource/admin/plugins/morris/morris.min.js"></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src="resource/admin/dist/js/pages/dashboard.js"></script>

<!-- page script -->
<script>
    $(function () {
        //load menu
        choiceMenu("main-index");
        getCount(0);
        getCount(1);
        getCount(2);
        getCount(3);
        getCount(4);
        getCount(6);

        getErrorList();
    });


    /**
     * 获取数量
     */
    function getCount(status) {
        var param = new Object();
        if(status!=0){
            param.status = status;
        }
        $.ajax({
            type: "POST",
            url: countApi,
            cache: false, //禁用缓存
            data: JSON.stringify(param), //传入已封装的参数
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (res) {
                $("#status_"+status).text(res.data.count);
            },
            error: function () {
                layer.msg("获取任务数失败");
            }
        });
    }

    /**
     * 获取错误列表
     */
    function getErrorList() {
        var param = {
            "pagination": {
                "currentPage": 1,
                "pageSize": 5
            },
            "status": 4
        };
        $.ajax({
            type: "POST",
            url: jobListApi,
            cache: false, //禁用缓存
            data: JSON.stringify(param), //传入已封装的参数
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (res) {
                console.log(res)
                $("#errorList").html("");
                res.data.dataList.forEach(function(value, index, array) {
                    var str = '';
                    str += '<div class="callout callout-danger">';
                    str +=    '<p><i class="icon fa fa-times-circle"></i>　[错误]：任务ID('+value.id+') 任务名称('+value.jobName+') 任务分组('+value.jobGroup+') , 错误重试3次全部失败！<a href="#">详细</a></p>';
                    str += '</div>';
                    $("#errorList").append(str);
                });
            },
            error: function () {
                layer.msg("获取数据失败");
            }
        });
    }

</script>
</body>
</html>
