<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>服务列表</title>

    <#include 'common/css.ftl' >

</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <#include 'common/header.ftl' >
    <#include 'common/menu.ftl' >

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                服务管理
                <small>服务列表</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 服务管理</a></li>
                <li class="active">服务列表</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row" style="display: none;">
                <div class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">查询条件</h3>

                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                        class="fa fa-minus"></i>
                                </button>
                            </div>
                            <!-- /.box-tools -->
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">

                            <!-- form start -->
                            <form class="form-horizontal">
                                <div class="box-body">
                                    <div class="form-group">
                                        <label class="col-sm-1 control-label">服务：</label>
                                        <div class="col-sm-2">
                                            <input type="text" class="form-control" id="jobName"
                                                   placeholder="">
                                        </div>

                                        <label class="col-sm-1 control-label">应用：</label>
                                        <div class="col-sm-2">
                                            <input type="text" class="form-control" id="jobGroup"
                                                   placeholder="">
                                        </div>

                                        <div class="col-sm-4">
                                            <button type="button" class="btn btn-primary" id="searchBtn"><i
                                                    class="glyphicon glyphicon-search"></i>查询
                                            </button>
                                            <button type="reset" class="btn btn-default">&nbsp;重置&nbsp;</button>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.box-footer -->
                            </form>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box box-primary">
                        <#--<div class="box-header">-->
                            <#--<h3 class="box-title">查询结果</h3>-->
                        <#--</div>-->
                        <!-- /.box-header -->
                        <div class="box-body">
                            <!-- 定义一个表格元素 -->
                            <table id="example" class="table table-striped table-bordered">
                                <thead>
                                <tr>
                                    <th>服务名称</th>
                                    <th>所属应用</th>
                                    <th>提供者</th>
                                    <th>消费者</th>
                                    <th>成功</th>
                                    <th>失败</th>
                                    <th>平均响应</th>
                                    <th>最大响应</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>1</td>
                                        <td>2</td>
                                        <td>3</td>
                                        <td>4</td>
                                    </tr>
                                </tbody>
                                <!-- tbody是必须的 -->
                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->

                </div>
                <!-- /.col -->
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <#include 'common/footer.ftl' >

</div>
<!-- ./wrapper -->

<#include 'common/js.ftl' >

<script>
    $(function () {
        //load menu
        choiceMenu("services");
        $('#example').DataTable( {
            "ajax": {
                "url": "manage/getServiceList",
                "type": "POST",
                "dataSrc": ''
            },
            "columns": [
                { "data": "name" },
                { "data": "group" },
                { "data": "providerAmount" },
                { "data": "consumerAmount" },
                { "data": "callCount" },
                { "data": "failedCount" },
                { "data": "meanCostTime" },
                { "data": "maxCostTime" },
            ],
            "columnDefs": [
                {
                    "render": function ( data, type, row ) {
                        if(data!=0) {
                            return '<a href="javascript:void(0)" onclick="showProvider(\''+ row['name'] +'\')" title="查看提供者">' + data + '</a>';
                        }
                        return data;
                    },
                    "targets": 2
                },
                {
                    "render": function ( data, type, row ) {
                        if(data!=0) {
                            return '<a href="javascript:void(0)" onclick="showConsumer(\'' + row['name'] + '\')" title="查看消费者">' + data + '</a>';
                        }
                        return data;
                    },
                    "targets": 3
                }
            ]
        } );

    });

    /**
     * 查看提供者
     * @param serviceName
     */
    function showProvider(serviceName) {
        layer.open({
            title: "服务["+serviceName+"]的提供者",
            type: 2,
            shadeClose: true,
            area: ['800px', '500px'],
            fixed: false, //不固定
            maxmin: true,
            content: './getProviderList?serviceName=' + serviceName
        });
    }

    /**
     * 查看消费者
     * @param appName
     */
    function showConsumer(serviceName) {
        layer.open({
            title: "服务["+serviceName+"]的消费者",
            type: 2,
            shadeClose: true,
            area: ['800px', '500px'],
            fixed: false, //不固定
            maxmin: true,
            content: './getConsumerList?serviceName=' + serviceName
        });
    }

</script>
</body>
</html>
