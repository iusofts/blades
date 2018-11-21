<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="resource/admin/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>admin</p>
                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
            </div>
        </div>
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="header">任务管理系统</li>
            <li id="menu-1" code="main-index">
                <a href="index.html">
                    <i class="fa fa-dashboard"></i> <span>首页</span></i>
                </a>
            </li>
            <li class="treeview" id="menu-2" code="storeroom-manage">
                <a href="#">
                    <i class="fa fa-clock-o"></i>
                    <span>任务管理</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li id="menu-21" code="job-list" pid="menu-2"><a href="job-list.html"><i class="fa fa-circle-o"></i> 任务列表</a></li>
                </ul>
            </li>
            <li id="menu-3" code="intf-doc">
                <a href="http://dev.sks.org/job/swagger-ui.html" target="_blank">
                    <i class="fa fa-book"></i> <span>接口文档</span></i>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>