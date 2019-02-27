# Rest Boot
Rest Boot 是一个轻量的基于 Jetty 的 Restful API 框架，默认只包含必要依赖，编译后的程序大概在 6MB 左右，你可以基于 restboot 直接开发业务需求，免去拼接各个框架的过程。

## 快速开始
### 1.下载和编译
```
git clone https://github.com/sycki/restboot.git
cd ./restboot
./restboot.sh build
```

此时在`target`目录中会生成打包好的`*.tar.gz`文件，你可以拿去发布给其他人，也可以忽略它。

### 2.运行
```
./restboot.sh start
```

### 3.测试
```
curl -X POST localhost:8080/v1/echo -d 'hello world!'
```

## 配置你的数据库
1. 在数据库中创建一个表：
    ```
    create database test;
    create table test.article (title varchar(255), content text);
    insert into test.article values ("restboot", "hello world!");
    ```
1. 在`application.properties`文件中配置你的数据库地址，然后重启程序：
    ```
    ./restboot.sh stop
    ./restboot.sh start
    ```
1. 发送一个测试请求：
    ```
    curl -X GET 'localhost:8080/v1/article?title=restboot'
    ```

## 在IDEA中运行
1. 打开`com.sycki.restboot.server.Server`，在编辑器中点击`右键->运行`，这时IDEA会记录下这个`Main class`
1. 在IDEA中打开设置面板：`Run->Edit Configurations`，然后在`Environment variables`中添加以下环境变量。
    ```
    RESTBOOT_HOME=${projet_home}/src/main/resources
    LOG4J_CONFIGURATION_FILE=${projet_home}/src/main/resources/config/log4j2.properties
    ```
1. 重新执行步骤`1`

## 编写业务
1. 在`com.sycki.restboot.controller`包中增加一个`Servlet`。
1. 在`com.sycki.restboot.router.RouterV1`类中添加路由。
1. 重启程序使生效。
