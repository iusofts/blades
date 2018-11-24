<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>提供者列表</title>
    <#include 'common/css.ftl' >
</head>
<body class="hold-transition skin-blue sidebar-mini">


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
                            <th>地址</th>
                            <th>端口</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
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

<!-- ./wrapper -->

<#include 'common/js.ftl' >
<script>
    $(function () {

        $('#example').DataTable( {
            "ajax": {
                "url": "manage/getProviderList?serviceName=${serviceName}",
                "type": "POST",
                "dataSrc": ''
            },
            "columns": [
                { "data": "appName" },
                { "data": "address" },
                { "data": "port" },
                { "data": "disable" }
            ]
        } );

    });

</script>
</body>
</html>
