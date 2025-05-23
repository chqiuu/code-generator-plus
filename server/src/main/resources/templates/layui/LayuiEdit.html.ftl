
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改${comment}</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="../../layui/css/layui.css"/>
    <link rel="stylesheet" type="text/css" href="../../css/common.css"/>
</head>
<body>

<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">修改${comment}</div>
                <div class="layui-card-body" pad15>
                    <form class="layui-form layui-form-pane" action="">
                        <#list columns as column>
                            <#if column.columnName == pk.columnName>
                            <input id="input-${column.attrNameLowerCase}" type="hidden">
                            <#elseif !exclusionShowColumns?contains(column.columnName)>
                            <div class="layui-form-item">
                                <label class="layui-form-label">${column.commentEscape}</label>
                                <#if column.attrType == 'String'>
                                    <#if column.charlength?? && column.charlength < 1000>
                                        <div class="layui-input-block">
                                            <input id="input-${column.attrNameLowerCase}" type="text" name="${column.attrNameLowerCase}" placeholder="请输入${column.commentEscape}" value="" class="layui-input">
                                        </div>
                                    <#else>
                                        <div class="layui-input-block">
                                            <textarea id="input-${column.attrNameLowerCase}" name="${column.attrNameLowerCase}" class="layui-textarea" placeholder="请输入${column.commentEscape}"></textarea>
                                        </div>
                                    </#if>
                                <#elseif column.attrType == 'Long' || column.attrType == 'Integer' || column.attrType == 'Float' || column.attrType == 'Double' || column.attrType == 'BigDecimal'>
                                    <div class="layui-input-inline" style="width: <#if column.attrType == 'Integer'>80<#else>150</#if>px;">
                                        <input id="input-${column.attrNameLowerCase}" name="${column.attrNameLowerCase}" type="number" lay-verify="number" class="layui-input">
                                    </div>
                                    <div class="layui-input-inline layui-input-company">单位</div>
                                    <div class="layui-form-mid layui-word-aux">提示：---</div>
                                <#elseif column.attrType == 'LocalDateTime'>
                                    <div class="layui-input-inline" style="width: 150px;">
                                        <input id="input-${column.attrNameLowerCase}" type="text" name="${column.attrNameLowerCase}" autocomplete="off" placeholder="请选择${column.commentEscape}" class="layui-input">
                                    </div>
                                    <div class="layui-form-mid layui-word-aux">提示：---</div>
                                </#if>
                            </div>
                            </#if>
                        </#list>
                        <div class="layui-form-item layui-hide">
                            <div class="layui-input-block">
                                <button id="updateBtn" class="layui-btn layui-btn-normal" lay-submit
                                        lay-filter="updateBtn">
                                    保存
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="../../layui/layui.js"></script>
<script>
    layui.use(['jquery', 'form', 'layer', 'table', 'laydate'], function () {
        var $ = layui.jquery,
            form = layui.form,
            layer = layui.layer, laydate = layui.laydate,
            table = layui.table;

        /**
         * 初始化表单，要加上，不然刷新部分组件可能会不加载
         */
        form.render();

        <#list columns as column>
        <#if !exclusionShowColumns?contains(column.columnName)>
        <#if column.attrType == 'LocalDateTime'>
        laydate.render({
            elem: '#input-${column.attrNameLowerCase}'
        });
        </#if>
        </#if>
        </#list>

        // 保存
        form.on('submit(saveBtn)', function (data) {
            let dataContent = {
                <#list columns as column>
                <#if !exclusionShowColumns?contains(column.columnName)>
                ${column.attrNameLowerCase}: $("#input-${column.attrNameLowerCase}").val(),
                </#if>
                </#list>
            }
            edit(dataContent)
            return false;
        });

        function edit(dataContent) {
            console.info("edit", dataContent)
            $.ajax({
                url: '../../..${mappingName}/update',
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