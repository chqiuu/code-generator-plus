<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${comment}管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css" href="../../libs/layui/css/layui.css"/>
    <link rel="stylesheet" type="text/css" href="../../libs/layui/adminui/dist/css/admin.css" media="all">
    <link rel="stylesheet" type="text/css" href="../../css/common.css"/>
</head>
<body>

<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-header">${comment}管理</div>
        <div class="layui-card-body">
            <div class="layui-form layuiadmin-card-header-auto">
                <div class="layui-form-item">
                    <#list columns as column>
                        <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName)>
                        <div class="layui-inline">
                            <label class="layui-form-label">${column.commentEscape}</label>
                            <div class="layui-input-inline">
                                <input id="search-${column.attrNameLowerCase}" type="text" name="${column.attrNameLowerCase}"
                                       placeholder="请输入${column.commentEscape}" autocomplete="off"
                                       class="layui-input">
                            </div>
                        </div>
                        <#elseif column.attrNameLowerCase=='createTime'>
                        <div class="layui-inline">
                            <label class="layui-form-label">创建时间</label>
                            <div class="layui-inline">
                                <div class="layui-input-inline">
                                    <input id="search-createTimeStartTime" type="text" placeholder="开始日期"
                                           name="createTimeStartTime" autocomplete="off"
                                           class="layui-input">
                                </div>
                                <div class="layui-form-mid">-</div>
                                <div class="layui-input-inline">
                                    <input id="search-createTimeEndTime" type="text" placeholder="截止日期"
                                           name="createTimeEndTime" autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>
                        </div>
                        </#if>
                    </#list>


                    <div class="layui-inline">
                        <button class="layui-btn layui-bg-blue acm-btn-list" lay-submit
                                lay-filter="LAY-search">
                            <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="table-container">
                <script type="text/html" id="currentTableToolbar">
                    <div class="layui-btn-container">
                        <a class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="add">添加</a>
                    </div>
                </script>

                <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

                <script type="text/html" id="currentTableBar">
                    <div class="layui-btn-group">
                        <a class="layui-btn layui-btn-normal layui-btn-sm data-count-edit" lay-event="edit">编辑</a>
                        <a class="layui-btn layui-btn-sm layui-btn-danger data-count-delete" lay-event="delete">删除</a>
                    </div>
                </script>
            </div>
        </div>
    </div>
    <script src="../../libs/layui/layui.js"></script>
    <script>
        layui.use(['form', 'table', 'laydate'], function () {
            var $ = layui.jquery,
                form = layui.form, laydate = layui.laydate,
                table = layui.table;

            let tableWhere = {}
            <#list columns as column>
            <#if !exclusionShowColumns?contains(column.columnName)>
            tableWhere.${column.attrNameLowerCase} = $("#search-${column.attrNameLowerCase}").val();
            <#elseif column.attrNameLowerCase=='createTime'>
            // 日期范围
            laydate.render({
                elem: '#search-createTimeStartTime'
            });
            laydate.render({
                elem: '#search-createTimeEndTime'
            });
            let createTimeStartTime = $("#search-createTimeStartTime").val();
            let createTimeEndTime = $("#search-createTimeEndTime").val();
            if (createTimeStartTime !== '') {
                tableWhere.createTimeStartTime = createTimeStartTime;
            }
            if (createTimeEndTime !== '') {
                tableWhere.createTimeEndTime = createTimeEndTime;
            }
            </#if>
            </#list>

            table.render({
                elem: '#currentTableId',
                url: '../../..${mappingName}/page',
                where: tableWhere,
                toolbar: '#currentTableToolbar',
                defaultToolbar: [],
                request: {
                    pageName: 'current', //页码的参数名称，默认：page
                    limitName: 'size' //每页数据量的参数名，默认：limit
                }, parseData: function (res) { //res 即为原始返回的数据
                    return {
                        "code": res.code === 1 ? 0 : res.code, //解析接口状态
                        "msg": res.message, //解析提示文本
                        "count": res.data.total, //解析数据长度
                        "data": res.data.records //解析数据列表
                    };
                },
                height: 'full-156',
                lineStyle: 'height: 45px;',
                text: {none: '暂无数据'},
                cols: [[
                    {type: "checkbox", width: 50, hide: true},
                    <#list columns as column>
                    {field: '${column.attrNameLowerCase}', title: '${column.commentEscape}', minWidth: 150},
                    </#list>
                    {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "left", fixed: 'right'}
                ]],
                limits: [10, 15, 20, 25, 50, 100],
                limit: 15,
                page: true,
                autoSort: false, // 禁用前端自动排序
                skin: 'grid'
            });

            // 监听搜索操作
            form.on('submit(data-search-btn)', function (data) {
                //执行搜索重载
                tableReload()
                return false;
            });

            function tableReload() {
                <#list columns as column>
                <#if !exclusionShowColumns?contains(column.columnName)>
                tableWhere.${column.attrNameLowerCase} = $("#search-${column.attrNameLowerCase}").val();
                <#elseif column.attrNameLowerCase=='createTime'>
                let createTimeStartTime = $("#search-createTimeStartTime").val();
                let createTimeEndTime = $("#search-createTimeEndTime").val();
                if (createTimeStartTime !== '') {
                    tableWhere.createTimeStartTime = createTimeStartTime;
                }
                if (createTimeEndTime !== '') {
                    tableWhere.createTimeEndTime = createTimeEndTime;
                }
                </#if>
                </#list>
                table.reloadData('currentTableId', {
                    page: {
                        curr: 1
                    }, where: tableWhere,
                }, 'data');
            }

            /**
             * toolbar事件监听
             */
            table.on('toolbar(currentTableFilter)', function (obj) {
                if (obj.event === 'add') {   // 监听添加操作
                    var index = layer.open({
                        title: '添加${comment}',
                        type: 2,
                        shade: 0.2,
                        maxmin: true,
                        shadeClose: true,
                        btnAlign: "c",
                        area: ['90%', '95%'],
                        content: '..${mappingName}Add.html',
                        btn: ['保存', '取消'],
                        yes: function (index, layero) {
                            const body = layer.getChildFrame('body', index);
                            // 点击子页面的对应按钮
                            body.find("#addBtn").click()
                            //重载表格
                            tableReload()
                            //最后关闭弹出层
                            layer.close(index); //如果设定了yes回调，需进行手工关闭
                        },
                        btn3: function (index, layero) {
                            //最后关闭弹出层
                            layer.close(index); //如果设定了yes回调，需进行手工关闭
                        }
                    });
                    $(window).on("resize", function () {
                        layer.full(index);
                    });
                } else if (obj.event === 'delete') {  // 监听删除操作
                    var checkStatus = table.checkStatus('currentTableId')
                        , data = checkStatus.data;
                    layer.alert(JSON.stringify(data));
                }
            });

            //监听表格复选框选择
            table.on('checkbox(currentTableFilter)', function (obj) {
                console.log(obj)
            });
            table.on('tool(currentTableFilter)', function (obj) {
                const checkData = obj.data;
                if (obj.event === 'edit') {
                    var index = layer.open({
                        title: '编辑${comment}',
                        type: 2,
                        shade: 0.2,
                        maxmin: true,
                        shadeClose: true,
                        btnAlign: "c",
                        area: ['90%', '95%'],
                        content: '..${mappingName}Edit.html?${pk.attrNameLowerCase}=' + checkData.${pk.attrNameLowerCase},
                        btn: ['保存', '取消'],
                        success: function (layero, index) {
                            console.info("checkData  == ", checkData)
                            const body = layer.getChildFrame('body', index);
                            <#list columns as column>
                            <#if !exclusionShowColumns?contains(column.columnName)>
                            body.find("#input-${column.attrNameLowerCase}").val(checkData.${column.attrNameLowerCase});
                            </#if>
                            </#list>
                        },
                        yes: function (index, layero) {
                            const body = layer.getChildFrame('body', index);
                            // 点击子页面的对应按钮
                            body.find("#updateBtn").click()
                            //重载表格
                            tableReload()
                            //最后关闭弹出层
                            layer.close(index); //如果设定了yes回调，需进行手工关闭
                        },
                        btn3: function (index, layero) {
                            //最后关闭弹出层
                            layer.close(index); //如果设定了yes回调，需进行手工关闭
                        }
                    });
                    $(window).on("resize", function () {
                        layer.full(index);
                    });
                    return false;
                } else if (obj.event === 'delete') {
                    layer.confirm('真的删除行么', function (index) {
                        obj.del();
                        $.ajax({
                            url: '../../..${mappingName}/delete/' + checkData.${pk.attrNameLowerCase},
                            type: "post",
                            dataType: "json",
                            headers: {'Content-Type': 'application/json;charset=utf-8'},
                            success: function (res) {
                                if (res.code === 1) {
                                    tableReload()
                                    layer.msg('删除成功！');
                                } else {
                                    layer.msg('删除失败！【' + res.message + '】');
                                }
                            },
                            error: function (e) {
                                layer.msg('删除失败！【' + e + '】');
                            }
                        });
                        layer.close(index);
                    });
                }
            });
        });
    </script>

</body>
</html>