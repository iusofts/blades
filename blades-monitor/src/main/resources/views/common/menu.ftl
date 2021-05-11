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
            <li class="header">Blades Admin</li>
            <li id="menu-1" code="main-index">
                <a href="index">
                    <i class="fa fa-dashboard"></i> <span>首页</span></i>
                </a>
            </li>
            <li class="treeview" id="menu-2" code="storeroom-manage">
                <a href="#">
                    <i class="fa fa-server"></i>
                    <span>服务管理</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li id="menu-21" code="services" pid="menu-2"><a href="services"><i class="fa fa-circle-o"></i> 服务列表</a></li>
                    <li id="menu-22" code="applications" pid="menu-2"><a href="applications"><i class="fa fa-circle-o"></i> 应用列表</a></li>
                    <li id="menu-23" code="relationship" pid="menu-2"><a href="relationship"><i class="fa fa-circle-o"></i> 依赖关系</a></li>
                    <li id="menu-24" code="services-relationship" pid="menu-2"><a href="servicesRelationship"><i class="fa fa-circle-o"></i> 服务关系</a></li>
                </ul>
            </li>
            <li class="treeview" id="menu-3" code="storeroom-manage">
                <a href="#">
                    <i class="fa fa-key"></i>
                    <span>权限管理</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <li id="menu-31" code="authorizations" pid="menu-3"><a href="authorizations"><i class="fa fa-circle-o"></i> 服务授权</a></li>
                </ul>
            </li>
            <li id="menu-4" code="intf-doc">
                <a href="swagger-ui.html" target="_blank">
                    <i class="fa fa-book"></i> <span>接口文档</span></i>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>