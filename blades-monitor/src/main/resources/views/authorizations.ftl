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
                <div class="col-xs-12">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <div class="col-sm-2">
                                <select class="form-control" id="appSelect">
                                </select>
                            </div>
                            <div class="col-sm-1">
                                <button type="button" class="btn btn-primary" onclick="doAdd()"><i
                                        class="glyphicon"></i>新增权限
                                </button>
                            </div>
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

    layer.config({extend: 'extend/layer.ext.js'})

    $(function () {
        //load menu
        choiceMenu("authorizations");
        $('#example').DataTable({
            "ajax": {
                "url": "manage/getAuthorizationList",
                "type": "POST",
                "dataSrc": ''
            },
            "columns": [
                {"data": "appName"},
                {"data": "allowAppNames"}
            ],
            "columnDefs": [
                {
                    "render": function (data, type, row) {
                        var str = '<a href="javascript:void(0)" onclick="doEdite(\'' + row['appName'] + '\',\'' + row['allowAppNames'] + '\')" title="编辑">编辑</a>';
                        str += '&nbsp;&nbsp;<a href="javascript:void(0)" onclick="doReomve(\'' + row['appName'] + '\')" title="删除">删除</a>';
                        return str;
                    },
                    "targets": 2
                }
            ]
        });


        $.ajax({
            type: "POST",
            url: 'manage/getApplicationList',
            cache: false, //禁用缓存
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                for (var app of res) {
                    $("#appSelect").append("<option>" + app.appName + "</option>");
                }
            }
        });

    });


    /**
     * 编辑权限
     * @param appName
     */
    function doEdite(appName, allowAppNames) {
        layer.prompt({
            formType: 2,
            value: allowAppNames,
            title: appName + ' 授权',
            area: ['400px', '300px'] //自定义文本域宽高
        }, function (value, index, elem) {
            var param = new Object();
            param.allowAppNames = value.split(",");
            param.appName = appName;
            console.log(param)
            $.ajax({
                type: "POST",
                url: 'manage/editAuthorization',
                cache: false, //禁用缓存
                data: JSON.stringify(param), //传入已封装的参数
                contentType: "application/json; charset=utf-8",
                success: function () {
                    layer.msg("保存成功");
                    layer.close(index);
                    window.location.reload();
                },
                error: function () {
                    layer.msg("保存失败");
                }
            });
        });
    }

    /**
     * 添加权限
     */
    function doAdd() {
        var appName = $("#appSelect").val();
        layer.prompt({
            formType: 2,
            value: '',
            title: appName + ' 授权',
            area: ['400px', '300px'] //自定义文本域宽高
        }, function (value, index, elem) {
            var param = new Object();
            param.allowAppNames = value.split(",");
            param.appName = appName;
            console.log(param)
            $.ajax({
                type: "POST",
                url: 'manage/addAuthorization',
                cache: false, //禁用缓存
                data: JSON.stringify(param), //传入已封装的参数
                contentType: "application/json; charset=utf-8",
                success: function () {
                    layer.msg("保存成功");
                    layer.close(index);
                    window.location.reload();
                },
                error: function () {
                    layer.msg("保存失败");
                }
            });
        });
    }

    /**
     * 删除权限
     * @param appName
     */
    function doReomve(appName) {
        //询问框
        layer.confirm('确定清除' + appName + '的权限控制吗？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                type: "POST",
                url: 'manage/deleteAuthorization?appName=' + appName,
                cache: false, //禁用缓存
                contentType: "application/json; charset=utf-8",
                success: function () {
                    layer.msg("删除成功");
                    window.location.reload();
                },
                error: function () {
                    layer.msg("删除失败");
                }
            });
        });
    }


</script>
</body>
</html>
