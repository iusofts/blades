<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>服务授权</title>

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
                权限管理
                <small>服务授权</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 权限管理</a></li>
                <li class="active">服务授权</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
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
                    <div class="box box-primary box-solid">
                        <div class="box-header">
                            <h3 class="box-title">查询结果</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <!-- 定义一个表格元素 -->
                            <table id="example" class="table table-striped table-bordered">
                                <thead>
                                <tr>
                                    <th>应用名称</th>
                                    <th>授权应用</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
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
        choiceMenu("authorizations");
        $('#example').DataTable( {
            "ajax": {
                "url": "manage/getAuthorizationList",
                "type": "POST",
                "dataSrc": ''
            },
            "columns": [
                { "data": "appName" },
                { "data": "allowAppNames" }
            ]
        } );

    });

</script>
</body>
</html>
