<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>应用配置</title>
<#include 'common/css.ftl' >
</head>
<body class="hold-transition skin-blue sidebar-mini">


<!-- Main content -->
<section class="content">

    <div class="row">
        <div class="col-md-12">
            <!-- /.box-header -->
            <div class="box">
                <#--<div class="box-header with-border">
                    <h3 class="box-title">基本信息</h3>
                </div>-->
                <!-- /.box-header -->
                <div class="box-body">
                    <!-- form start -->
                    <form class="form-horizontal" id="myform" onsubmit="return save()">
                        <div class="form-group">
                            <label class="col-md-2 control-label">执行线程数：</label>
                            <div class="col-md-3">
                                <input class="form-control" name="hystrix.threadpool.${appName}.coreSize" placeholder="默认：30"
                                       type="number">
                            </div>

                            <label class="col-md-2 control-label">队列大小：</label>
                            <div class="col-md-3">
                                <input class="form-control" name="hystrix.threadpool.${appName}.maxQueueSize" placeholder="默认：50000"
                                       type="number">
                            </div>

                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">拒绝队列大小：</label>
                            <div class="col-md-3">
                                <input class="form-control" name="hystrix.threadpool.${appName}.queueSizeRejectionThreshold" placeholder="默认：45000" type="number">
                            </div>
                            <label class="col-md-2 control-label">执行隔离线程超时(以毫秒为单位)：</label>
                            <div class="col-md-3">
                                <input class="form-control" name="hystrix.command.${appName}.execution.isolation.thread.timeoutInMilliseconds" placeholder="默认：10000" type="number">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">滑动窗口内触发熔断的最少错误请求量：</label>
                            <div class="col-md-3">
                                <input class="form-control" name="hystrix.command.${appName}.circuitBreaker.requestVolumeThreshold" placeholder="默认：100" type="number">
                            </div>

                            <label class="col-md-2 control-label executeTime">熔断器错误率阈值(百分比)：</label>
                            <div class="col-md-3 executeTime">
                                <input class="form-control" name="hystrix.command.${appName}.circuitBreaker.errorThresholdPercentage" placeholder="默认：75" type="number">
                            </div>
                        </div>
                        <div class="box-footer clearfix">
                            <div style="text-align: center;">
                                <button type="submit" class="btn btn-primary">保存</button>
                            </div>
                        </div>

                    </form>
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

    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引

    $(function () {

        getDetail();

    });


    /**
     * 查询详情
     */
    function getDetail() {
        //加载层
        var index = layer.load();
        $.ajax({
            type: "GET",
            url: "config/getApplicationConfig?appName=${appName}",
            cache: false, //禁用缓存
            //data: JSON.stringify(param), //传入已封装的参数
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (res) {

                for(var config of res){
                    $("input[name='"+config.property+"']").val(config.value);
                }

                layer.close(index);
            },
            error: function () {
                layer.msg("查询失败");
                //关闭遮罩
                layer.close(index);
            }
        });
    }


    function save() {
        var param = getFormJson("#myform");
        console.log(param)
        var configs = new Array();
        for(var key in param){
            var config = new Object();
            config.property = key;
            config.value = param[key];
            configs.push(config);
        }
        console.log(configs)
        $.ajax({
            type: "POST",
            url: 'config/saveApplicationConfig?appName=${appName}',
            cache: false, //禁用缓存
            data: JSON.stringify(configs), //传入已封装的参数
            contentType: "application/json; charset=utf-8",
            success: function () {
                parent.layer.msg("保存成功");
                parent.layer.close(index);
            },
            error: function () {
                layer.msg("保存失败");
            }
        });
        return false;
    }

</script>
</body>
</html>
