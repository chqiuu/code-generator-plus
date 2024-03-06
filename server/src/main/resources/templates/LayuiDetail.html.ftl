<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改邮件配置</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="//cdn.staticfile.org/layui/2.8.2/css/layui.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../../static/css/public.css" media="all">
</head>
<body>
<div class="layuimini-container layuimini-page-anim">
    <div class="layuimini-main">
        <form class="layui-form layui-form-pane" action="">
            <div class="layui-form-item">
                <label class="layui-form-label required">邮箱名</label>
                <div class="layui-input-block">
                    <input id="email" type="text" name="email" lay-verify="required" lay-reqtext="邮箱名不能为空"
                           placeholder="请输入电子邮箱，如123@qq.com" value="" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label required">显示姓名</label>
                <div class="layui-input-block">
                    <input id="user" type="text" name="user" lay-verify="required" lay-reqtext="显示姓名不能为空"
                           placeholder="请输入显示姓名，用于邮件中显示发件人姓名" value="" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label required">称谓</label>
                <div class="layui-input-block">
                    <input id="title" type="text" name="title" lay-verify="required" lay-reqtext="称谓不能为空"
                           placeholder="请输入称谓（如：编辑、老师等）" value="" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label required">联系电话</label>
                <div class="layui-input-block">
                    <input id="tel" type="text" name="tel" lay-verify="required" lay-reqtext="联系电话不能为空"
                           placeholder="请输入联系电话" value="" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label required">邮箱密码</label>
                <div class="layui-input-block">
                    <input id="passowrd" type="text" name="passowrd" lay-verify="required" lay-reqtext="邮箱密码不能为空"
                           placeholder="请输入邮箱密码" value="" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label required">SMTP端口</label>
                <div class="layui-input-block">
                    <input id="port" type="number" name="port" lay-verify="required" lay-reqtext="邮件服务器的SMTP端口不能为空"
                           placeholder="请输入邮件服务器的SMTP端口" value="25" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label required">SMTP地址</label>
                <div class="layui-input-block">
                    <input id="host" type="text" name="host" lay-verify="required" lay-reqtext="邮件服务器的SMTP地址不能为空"
                           placeholder="请输入邮件服务器的SMTP地址" value="" class="layui-input">
                </div>
            </div>
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
<script src="//cdn.staticfile.org/layui/2.8.2/layui.min.js"></script>
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

        // 保存
        form.on('submit(updateBtn)', function (data) {
            var dataContent = {
                email: $("#email").val(),
                user: $("#user").val(),
                passowrd: $("#passowrd").val(),
                port: $("#port").val(),
                host: $("#host").val(),
                title: $("#title").val(),
                tel: $("#tel").val(),
            }
            editEmailConfig(dataContent)
            return false;
        });

        function editEmailConfig(dataContent) {
            console.info("editEmailConfig", dataContent)
            $.ajax({
                url: '../../system/config/update',
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