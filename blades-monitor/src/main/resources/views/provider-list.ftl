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
            ],
            "columnDefs": [
                {
                    "render": function ( data, type, row ) {
                        var str;
                        if(row.disable){
                            str = [
                                '<a class="like" href="javascript:enableService(\''+row.id+'\')" title="Like">',
                                '启用',
                                '</a>  '
                            ].join('');
                        }else{
                            str = [
                                '<a class="like" href="javascript:disableService(\''+row.id+'\')" title="Like">',
                                '禁用',
                                '</a>  '
                            ].join('');
                        }
                        var appUrl = 'http://'+row.address+':'+row.port +'/'+row.appName;
                        str += '<a href="javascript:void(0)" onclick="showMonitor(\''+ appUrl +'\')" title="查看提供者">监控</a>';
                        return str;
                    },
                    "targets": 4
                },
                {
                    "render": function ( data, type, row ) {
                        if(data){
                            return [
                                '<font color="red">禁用</font>'
                            ].join('');
                        }else{
                            return [
                                '<font color="green">正常</font>'
                            ].join('');
                        }
                    },
                    "targets": 3
                }
            ]
        } );

    });



    /**
     * 打开监控
     * @param appUrl
     */
    function showMonitor(appUrl) {
        var url = ['http://localhost:7979/hystrix-dashboard/monitor/monitor.html?streams=[{"name":"","stream":"',
            appUrl,
            '/hystrix.stream","auth":"","delay":""}]'
        ].join('');
        parent.layer.open({
            title: "调用监控",
            type: 2,
            shadeClose: true,
            area: ['1024px', '600px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url)
        });
    }


    /**
     * 禁用服务提供者
     */
    function disableService(id) {
        $.ajax({
            url:"manage/disableService?serviceName=${serviceName}&serviceId="+id,
            type:'post',
            dataType:'json',
            success:function(msg){
                layer.msg(msg==true?"操作成功":"操作失败", {
                    icon: 1,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function(){
                    window.location.reload();
                });
            },
            error:function (e) {
                layer.msg("服务器繁忙");
            }
        })
    }

    /**
     * 启用服务提供者
     */
    function enableService(id) {
        $.ajax({
            url:"manage/enableService?serviceName=${serviceName}&serviceId="+id,
            type:'post',
            dataType:'json',
            success:function(msg){
                layer.msg(msg==true?"操作成功":"操作失败", {
                    icon: 1,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function(){
                    window.location.reload();
                });
            },
            error:function (e) {
                layer.msg("服务器繁忙");
            }
        })
    }

</script>
</body>
</html>
