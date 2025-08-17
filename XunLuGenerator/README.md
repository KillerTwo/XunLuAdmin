## 项目说明

- xunlu-generator是一款低代码生成器，可根据自定义模板内容，快速生成代码，可实现项目的快速开发、上线，减少重复的代码编写，开发人员只需专注业务逻辑即可。


## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 支持spring boot starter，能很方便集成到第三方项目
- 支持通过配置数据源，快速生成CRUD代码，减少重复工作
- 支持MySQL、Oracle、SQLServer、PostgreSQL、达梦8、人大金仓等主流的数据库
- 支持第三方Java项目包名修改，修改包名变得简单快速
- 支持批量导入表、批量生成代码以及同步表结构等功能

## 本地启动

- 通过git下载源码
- 如使用MySQL8.0（其他数据库类似），则创建数据库xunlu_generator，数据库编码为utf8mb4
- 执行db/mysql.sql文件，初始化数据
- 修改application.yml，更新MySQL账号和密码、数据库名称
- 运行GeneratorApplication.java，则可启动项目
- 项目访问路径：http://localhost:8088/xunlu-generator/index.html