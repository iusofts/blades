<!-- jQuery 2.2.0 -->
<script src="resource/admin/plugins/jQuery/jQuery-2.2.0.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="resource/admin/bootstrap/js/bootstrap.min.js"></script>
<!-- DataTables -->
<script src="resource/admin/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="resource/admin/plugins/datatables/dataTables.bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="resource/admin/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="resource/admin/plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="resource/admin/dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="resource/admin/dist/js/demo.js"></script>
<!-- layer ui -->
<script src="resource/plugins/layer-v2.1/layer/layer.js"></script>
<!-- 菜单 -->
<script src="resource/admin/menu.js"></script>
<!-- 返回顶部 -->
<script src="resource/plugins/backtop/material-scrolltop.js"></script>
<!-- Select2 -->
<script src="resource/admin/plugins/select2/select2.full.min.js"></script>
<!-- My97DatePicker -->
<script src="resource/plugins/calendar/WdatePicker.js"></script>

<!-- 全局变量 -->
<script type="text/javascript">
    var ctx = '';
</script>

<!-- 返回顶部 -->
<script>
    $(document).ready(function() {
        try {
            $('body').materialScrollTop({
                revealElement: 'header',
                revealPosition: 'bottom',
                onScrollEnd: function () {
                    console.log('Scrolling End');
                }
            });
        } catch (e) {
        }

        // 刷新服务缓存
        refreshCache();
        function refreshCache() {
            $.get("manage/reload", function(result){
                console.log(result)
            });
        }

    });


</script>
<!-- common script -->
<script type="text/javascript" src="config/config.js"></script>
<script type="text/javascript" src="resource/common/js/common.js"></script>
