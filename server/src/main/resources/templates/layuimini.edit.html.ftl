<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改${comment}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="//cdn.staticfile.org/layui/2.7.6/css/layui.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../../static/css/public.css" media="all">
</head>
<body>
<div class="layuimini-container layuimini-page-anim">
    <div class="layuimini-main">
        <form class="layui-form layui-form-pane" action="">
            <input id="${pk.attrNameLowerCase}" type="text" name="${pk.attrNameLowerCase}" class="layui-hide">
            <#list columns as column>
                <#if exclusionShowColumns?contains(column.columnName) || column.columnName == pk.columnName >
                <#else>
            <div class="layui-form-item">
                <label class="layui-form-label required">${column.commentEscape}</label>
                <div class="layui-input-block">
                            <#if column.attrType == 'String'>
                                <#if column.charlength?? && column.charlength < 1000>
                    <input id="${column.attrNameLowerCase}" type="text" name="${column.attrNameLowerCase}" lay-verify="required" lay-reqtext="${column.commentEscape}不能为空" placeholder="请输入${column.commentEscape}" value="" class="layui-input">
                                <#else>
                    <textarea id="${column.attrNameLowerCase}" name="${column.attrNameLowerCase}" placeholder="请输入${column.commentEscape}" class="layui-textarea"></textarea>
                                </#if>
                            <#elseif column.attrType == 'Integer'>
                    <input id="${column.attrNameLowerCase}" type="number" name="${column.attrNameLowerCase}" lay-verify="required" lay-reqtext="${column.commentEscape}不能为空" placeholder="请输入${column.commentEscape}" value="" class="layui-input">
                            <#elseif column.attrType == 'Long'>
                    <input id="${column.attrNameLowerCase}" type="number" name="${column.attrNameLowerCase}" lay-verify="required" lay-reqtext="${column.commentEscape}不能为空" placeholder="请输入${column.commentEscape}" value="" class="layui-input">
                            </#if>
                </div>
            </div>
                </#if>
            </#list>
            <div class="layui-form-item layui-hide">
                <div class="layui-input-block">
                    <button id="save${classNameUpperCase}Btn" class="layui-btn layui-btn-normal" lay-submit lay-filter="save${classNameUpperCase}Btn">
                        保存草稿
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="//cdn.staticfile.org/layui/2.7.6/layui.min.js"></script>
<script src="../../static/js/public.js" charset="utf-8"></script>
<script>
    layui.use(['jquery', 'form', 'layer', 'table'], function () {
        var $ = layui.jquery,
            form = layui.form,
            layer = layui.layer,
            table = layui.table;

        /**
         * 初始化表单，要加上，不然刷新部分组件可能会不加载
         */
        form.render();

        // 保存草稿
        form.on('submit(save${classNameUpperCase}Btn)', function (data) {
            var dataContent = {
                ${pk.attrNameLowerCase}: $("#${pk.attrNameLowerCase}").val(),
                <#list columns as column>
                    <#if exclusionShowColumns?contains(column.columnName) || column.columnName == pk.columnName >
                    <#else>
                ${column.attrNameLowerCase}: $("#${column.attrNameLowerCase}").val(),
                    </#if>
                </#list>
            }
            edit${classNameUpperCase}(dataContent)
            return false;
        });

        function edit${classNameUpperCase}(dataContent) {
            console.info("edit${classNameUpperCase}", dataContent)
            $.ajax({
                url: '../..${mappingName}/update',
                async: false,
                type: "post",
                dataType: "json",
                headers: {'Content-Type': 'application/json;charset=utf-8'},
                data: JSON.stringify(dataContent),
                success: function (res) {
                    console.log("保存信息", res)
                    if (res.code === 1) {
                        layer.msg("保存成功", {time: 2000}, function () {
                            const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index);
                        });
                    } else {
                        layer.msg('保存失败！【' + res.message + '】');
                    }
                },
                error: function (e) {
                    layer.msg('保存失败！【' + e + '】');
                }
            });
        }
    });
</script>
</body>
</html>