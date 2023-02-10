<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${comment}管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="//cdn.staticfile.org/layui/2.7.6/css/layui.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../../static/css/public.css" media="all">
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
        <fieldset class="table-search-fieldset">
            <legend>检索条件</legend>
            <div style="margin: 10px 10px 10px 10px">
                <form class="layui-form layui-form-pane" action="">
                    <div class="layui-form-item">
                        <#list columns as column>
                            <#if exclusionShowColumns?contains(column.columnName) || column.columnName == pk.columnName >
                            <#else>
                        <div class="layui-inline">
                            <label class="layui-form-label">${column.commentEscape}</label>
                            <div class="layui-input-inline">
                                        <#if column.attrType == 'String'>
                                            <#if column.charlength?? && column.charlength < 1000>
                                <input id="search-${classNameLowerCase}-${column.attrNameLowerCase}" type="text" name="${column.attrNameLowerCase}" autocomplete="off" class="layui-input">
                                            </#if>
                                        <#elseif column.attrType == 'Integer'>
                                <input id="search-${classNameLowerCase}-${column.attrNameLowerCase}" type="number" name="${column.attrNameLowerCase}" autocomplete="off" class="layui-input">
                                        <#elseif column.attrType == 'Long'>
                                <input id="search-${classNameLowerCase}-${column.attrNameLowerCase}" type="number" name="${column.attrNameLowerCase}" autocomplete="off" class="layui-input">
                                        </#if>
                            </div>
                        </div>
                            </#if>
                        </#list>
                        <div class="layui-inline">
                            <label class="layui-form-label">创建时间</label>
                            <div class="layui-inline" id="search-${classNameLowerCase}-createTime">
                                <div class="layui-input-inline">
                                    <input id="search-${classNameLowerCase}-createTimeStartTime" type="text" placeholder="开始日期"
                                           name="createTimeStartTime" autocomplete="off"
                                           class="layui-input">
                                </div>
                                <div class="layui-form-mid">-</div>
                                <div class="layui-input-inline">
                                    <input id="search-${classNameLowerCase}-createTimeEndTime" type="text" placeholder="截止日期"
                                           name="createTimeEndTime" autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <button id="data-search-${classNameLowerCase}-btn" type="submit"
                                    class="layui-btn layui-btn-primary layui-border-blue"
                                    lay-submit
                                    lay-filter="data-search-${classNameLowerCase}-btn"><i class="layui-icon"></i> 搜 索
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </fieldset>

        <script type="text/html" id="toolbarDemo">
            <div class="layui-btn-container">
                <button class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="add"> 添加</button>
            </div>
        </script>

        <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

        <script type="text/html" id="currentTableBar">
            <div class="layui-btn-group">
                <a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">编辑</a>
                <a class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>
            </div>
        </script>

    </div>
</div>
<script src="//cdn.staticfile.org/layui/2.7.6/layui.min.js"></script>
<script>
    layui.use(['form', 'table', 'laydate'], function () {
        var $ = layui.jquery,
            form = layui.form, laydate = layui.laydate,
            table = layui.table;
        // 日期范围
        laydate.render({
            elem: '#search-${classNameLowerCase}-createTimeStartTime'
        });
        laydate.render({
            elem: '#search-${classNameLowerCase}-createTimeEndTime'
        });

        let tableWhere = {}
        <#list columns as column>
            <#if exclusionShowColumns?contains(column.columnName) || column.columnName == pk.columnName >
            <#else>
        tableWhere.${column.attrNameLowerCase} = $("#search-${classNameLowerCase}-${column.attrNameLowerCase}").val();
            </#if>
        </#list>
        let createTimeStartTime = $("#search-${classNameLowerCase}-createTimeStartTime").val();
        let createTimeEndTime = $("#search-${classNameLowerCase}-createTimeEndTime").val();
        if (createTimeStartTime !== '') {
            tableWhere.createTimeStartTime = createTimeStartTime;
        }
        if (createTimeEndTime !== '') {
            tableWhere.createTimeEndTime = createTimeEndTime;
        }

        table.render({
            elem: '#currentTableId',
            url: '../..${mappingName}/page',
            where: tableWhere,
            toolbar: '#toolbarDemo',
            request: {
                pageName: 'current', //页码的参数名称，默认：page
                limitName: 'size' //每页数据量的参数名，默认：limit
            },
            parseData: function (res) { //res 即为原始返回的数据
                return {
                    "code": res.code === 1 ? 0 : res.code, //解析接口状态
                    "msg": res.message, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.records //解析数据列表
                };
            },
            // lineStyle: 'height: 40px;',
            cols: [[
                {type: "checkbox", width: 50},
                {field: '${pk.attrNameLowerCase}', title: 'ID', hide: true},
                <#list columns as column>
                <#if exclusionShowColumns?contains(column.columnName) || column.columnName == pk.columnName >
                <#else>
                {field: '${column.attrNameLowerCase}', title: '${column.commentEscape}', minWidth: 150, sort: true},
                </#if>
                </#list>
                {field: 'createTime', width: 180, title: '创建时间'},
                {field: 'updateTime', width: 180, title: '修改时间'},
                {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "left"}
            ]],
            limits: [20, 50, 100, 200, 500, 1000],
            limit: 50,
            page: true,
            skin: 'line'
        });

        // 监听搜索操作
        form.on('submit(data-search-${classNameLowerCase}-btn)', function (data) {
            //执行搜索重载
            tableReload()
            return false;
        });

        function tableReload() {
            <#list columns as column>
            <#if exclusionShowColumns?contains(column.columnName) || column.columnName == pk.columnName >
            <#else>
            tableWhere.${column.attrNameLowerCase} = $("#search-${classNameLowerCase}-${column.attrNameLowerCase}").val();
            </#if>
            </#list>
            let createTimeStartTime = $("#search-${classNameLowerCase}-createTimeStartTime").val();
            let createTimeEndTime = $("#search-${classNameLowerCase}-createTimeEndTime").val();
            if (createTimeStartTime !== '') {
                tableWhere.createTimeStartTime = createTimeStartTime;
            }
            if (createTimeEndTime !== '') {
                tableWhere.createTimeEndTime = createTimeEndTime;
            }
            table.reload('currentTableId', {
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
                    content: '../${moduleName}/${classNameLowerCase}Add.html',
                    btn: ['保存', '取消'],
                    yes: function (index, layero) {
                        const body = layer.getChildFrame('body', index);
                        // 点击子页面的对应按钮
                        body.find("#add${classNameUpperCase}Btn").click()
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
                    content: '../${moduleName}/${classNameLowerCase}Edit.html?${pk.attrNameLowerCase}=' + checkData.${pk.attrNameLowerCase},
                    btn: ['保存', '取消'],
                    success: function (layero, index) {
                        console.info("checkData  == ", checkData)
                        const body = layer.getChildFrame('body', index);
                        body.find("#${pk.attrNameLowerCase}").val(checkData.${pk.attrNameLowerCase});
                        <#list columns as column>
                            <#if exclusionShowColumns?contains(column.columnName) || column.columnName == pk.columnName >
                            <#else>
                        body.find("#${column.attrNameLowerCase}").val(checkData.${column.attrNameLowerCase});
                        </#if>
                        </#list>
                    },
                    yes: function (index, layero) {
                        const body = layer.getChildFrame('body', index);
                        // 点击子页面的对应按钮
                        body.find("#save${classNameUpperCase}Btn").click()
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
                        url: '../..${mappingName}/delete/' + checkData.${pk.attrNameLowerCase},
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