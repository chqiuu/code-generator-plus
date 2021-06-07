# code-generator-plus
JAVA代码生成工具
## 功能介绍
code-generator-plus是基于Springboot开发的SSM代码生成器，使用 <a target="_blank" href="https://freemarker.apache.org/">Apache FreeMarker</a> 
作为代码文件的模板，用户可以一键将数据库中的表生成entity、xml、dao、service、html、js、sql代码文件，并集成表的基础操作接口及前端功能实现（数据表的增加、删除、修改、根据ID获取信息以及分页查询功能），减少60%以上的开发任务；<br>
该工具支持所有实现JDBC规范的数据库;默认集成了MySQL数据库脚本;<br>
该工具提供了两种数据库表导入方式，连接数据库或直接导入SQL创建表脚本；<br>

## 操作说明

### 1. 设置数据源

​	本工具提供了两种数据库表导入方式，连接数据库和导入SQL创建表脚本。具体操作步骤见下图：

![连接数据库](https://img-blog.csdnimg.cn/20201126165359570.png)

数据库连接配置界面

![导入SQL脚本](https://img-blog.csdnimg.cn/20201126165832650.png)

​	导入SQL脚本界面，录入表CREATE脚本，支持多表导入，多个脚本以英文分号隔开。

### 2. 生成代码

​	数据源设置完成后及可进行代码生成了。代码生成界面展示了数据库中所有表，可通过多选的方式选中你想要生成代码的表，并分别为表指定功能模块（这里必须生成代码后会将代码分发到指定模块下），设置完成后点击“生成代码”即可一键生成你选中表的初始代码了。

![生成代码主界面](https://img-blog.csdnimg.cn/20210607172641264.png)

​	除此之外，提供了查看表字段信息和生成的代码预览功能，下图为选中表字段信息展示

![查看表字段信息](https://img-blog.csdnimg.cn/20201126170112585.png)

下图为生成的代码预览功能

![代码预览功能](https://img-blog.csdnimg.cn/20201126170222603.png)