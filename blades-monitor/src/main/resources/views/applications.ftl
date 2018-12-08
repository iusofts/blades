<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>应用列表</title>

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
                <small>应用列表</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 服务管理</a></li>
                <li class="active">应用列表</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-12">
                    <div class="box box-primary">
                        <!-- /.box-header -->
                        <div class="box-body">
                            <!-- 定义一个表格元素 -->
                            <table id="example" class="table table-striped table-bordered">
                                <thead>
                                <tr>
                                    <th>应用名称</th>
                                    <th>类型</th>
                                    <th>发布服务数</th>
                                    <th>订阅服务数</th>
                                    <th>状态</th>
                                    <th>操作</th>
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
        choiceMenu("applications");
        $('#example').DataTable( {
            "ajax": {
                "url": "manage/getApplicationList",
                "type": "POST",
                "dataSrc": ''
            },
            "columns": [
                { "data": "appName" },
                { "data": "type" },
                { "data": "provideServiceAmount" },
                { "data": "consumeServiceAmount" },
                { "data": "inactive" }
            ],
            "columnDefs": [
                {
                    "render": function (data, type, row) {
                        if (data == "PROVIDER_AND_CONSUMER") {
                            return '<font color="#006400">提供者</font>、<font color="#008b8b">消费者</font>';
                        } else if (data == "PROVIDER") {
                            return '<font color="#006400">提供者</font>';
                        } else {
                            return '<font color="#008b8b">消费者</font>';
                        }
                    },
                    "targets": 1
                },
                {
                    "render": function ( data, type, row ) {
                        return '<font color="#006400">正常</font>';
                    },
                    "targets": 4
                },
                {
                    "render": function ( data, type, row ) {
                        return '<a href="javascript:void(0)" onclick="showConfig(\'' + row['appName'] + '\')" title="配置">配置</a>';
                    },
                    "targets": 5
                }
            ]
        } );

    });


    /**
     * 查看应用配置
     * @param appName
     */
    function showConfig(appName) {
        layer.open({
            title: "应用["+appName+"]的配置",
            type: 2,
            shadeClose: true,
            area: ['800px', '600px'],
            fixed: false, //不固定
            maxmin: true,
            content: './applicationConfig?appName=' + appName
        });
    }

</script>
</body>
</html>
