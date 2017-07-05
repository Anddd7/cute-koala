## Koala




## 项目目标
搭建一个后端的服务器集群框架
> 运行在 Tomcat/Jetty/端口直连 上 ,对外使用Http协议提供服务
> 内部服务由集群提供 ,并注册在ZooKeeper上 ; 服务传输由Protobuf序列化
> 使用ORM 和 自动化的代码/文件生成 简化数据库的操作
> ...

## 项目进程
| 阶段 | 描述 | 完成度 |待完成|
| -------| -------| -------|-------|
|Learning|自己实现DI / RPC / Auto Generate / ORM / Micro Service 等常用的服务端技术 ,探究原理和使用场景|50%|Micro Service/ORM/自动化|
|Merge|查看实现的东西是否有可替代的/更优秀的轮子存在 ,并替换组合|0%||
|Use|使用新的框架做一些实质性的'产品'|0%||


## 使用框架
* 基础工具
    * Google. guava : 大量的实用性工具
    * Alibaba. fastjson : 支持国产 :)
    * snakeyaml : 读取yaml文件
    * ibeetl : 模版生成
    * Slf4j : 日志
    * Junit : 测试
    * projectlombok : 简化一些不影响逻辑的set/get代码

* 第一阶段 : 主要是了解原理 ,使用了尽量少的框架
    * okhttp : 简洁易用的http连接工具 ,作为http外部服务的交互工具
    * (优化) cglib : 字节码增强 ,替代JDK实现



