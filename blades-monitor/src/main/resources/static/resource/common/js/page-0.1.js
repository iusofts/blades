/**
 * 分页组件
 * @author Ivan
 * @date：2017年6月4日 下午2:07:20
 */
function Page() {

    /* tableID */
    var tableId;
    var pageSize = 10;
    var url;
    var param;
    var columns;
    var columnDefs;
    var table;
    var pageNo;//用于记录当前页

    function initialise(params) {

        //填充初始化数据
        if (typeof params != "undefined") {
            if (typeof params.tableId != "undefined") {
                tableId = params.tableId;
            }
            if (typeof params.pageSize != "undefined") {
                pageSize = params.pageSize;
            }
            if (typeof params.url != "undefined") {
                url = params.url;
            }
            if (typeof params.param != "undefined") {
                param = params.param;
            } else {
                param = new Object();
            }
            if (typeof params.columns != "undefined") {
                columns = params.columns;
            }
            if (typeof params.columnDefs != "undefined") {
                columnDefs = params.columnDefs;
            }
        }

        table = dataTable()
        return this;
    }

    this.init = initialise;

    function dataTable() {
        /*分页查询开始*/
        var table = $('#' + tableId).DataTable({
            "serverSide": true,//开启服务器模式
            "iDisplayLength": pageSize,//默认每页数量
            //"bPaginate": true, //翻页功能
            "bLengthChange": true, //改变每页显示数据数量
            "paging": true,
            "lengthChange": false,
            "searching": false,
            "ordering": false,
            "info": true,
            "autoWidth": false,
            "language": {//国际化
                "zeroRecords": "没有找到记录",
                "processing": "正在加载数据...",
                "lengthMenu": "每页显示 _MENU_ 条记录",
                "info": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
                "infoEmpty": "当前显示 0 到 0 条，共 0 条记录",
                "infoFiltered": "(从 _MAX_ 条记录中筛选)",
                "search": "搜索",
                "searchPlaceholder": "请输入搜索内容",
                "paginate": {
                    "first": "第一页",
                    "previous": " 上一页 ",
                    "next": " 下一页 ",
                    "last": " 最后一页 "
                }
            },
            ajax: function (data, callback,
                            settings) {//ajax配置为function,手动调用异步查询
                //加载层
                var index = layer.load(); //0代表加载的风格，支持0-2
                var pagination = new Object();
                
                pagination.currentPage = data.start / data.length + 1;
                pagination.pageSize = data.length;
                pageNo = pagination.currentPage;
                param.pagination = pagination;
                $.ajax({
                    type: "POST",
                    url: url,
                    cache: false, //禁用缓存
                    data: JSON.stringify(param), //传入已封装的参数
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (res) {
                        //setTimeout仅为测试遮罩效果
                        setTimeout(
                            function () {
                                //封装返回数据，这里仅演示了修改属性名
                                var returnData = {};
                                //returnData.draw = res.pageNo;//这里直接自行返回了draw计数器,应该由后台返回
                                returnData.recordsTotal = res.data.totalCount;
                                returnData.recordsFiltered = res.data.totalCount;//后台不实现过滤功能，每次查询均视作全部结果
                                returnData.data = res.data.dataList;
                                //关闭遮罩
                                layer.close(index);
                                //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                                //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                                callback(returnData);
                            },
                            200);
                    },
                    error: function (XMLHttpRequest,
                                     textStatus,
                                     errorThrown) {
                        layer.msg("查询失败");
                        //关闭遮罩
                        layer.close(index);
                    }
                });
            },
            columns: columns,
            "columnDefs": columnDefs
        });
        /*分页查询结束*/
        return table;
    }

    function doSearch(param2) {
        if (typeof param2 != "undefined") {
            param = param2;
        }
        //搜索重绘表格
        //判断是否停留当前页
        if (pageNo) {
            table.page(pageNo-1).draw(false);
        } else {
            table.draw();
        }
    }

    this.search = doSearch;
    
    this.clear = function () {
        //清空条件
        pageNo=null;
    }

}
