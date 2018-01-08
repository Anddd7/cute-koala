## Cute Koala 
[![Build Status](https://travis-ci.org/Anddd7/cute-koala.svg?branch=master)](https://travis-ci.org/Anddd7/cute-koala)
[![Coverage Status](https://coveralls.io/repos/github/Anddd7/cute-koala/badge.svg?branch=master)](https://coveralls.io/github/Anddd7/cute-koala?branch=master)

仿`Spring/ Spring-mvc/ Mybatis/ Dubbo`的框架 ,主要用来研究和学习各大框架原理.


## 20180108 路径扫描
用于在classpath/Jar下扫描class和指定注解 ,为后续Bean的解析做准备
* classpath扫描:
  * 通过classLoader获取当前classpath
  * 遍历classpath的所有文件
  * 筛选出class并通过Class.forname获取class对象
  * 检查指定注解 ,返回符合的class列表

> 适用于class未打包成jar ,比如tomcat运行war包/classpath关联到文件夹(ide运行环境)  

* Jar扫描:
  * 通过classpath获取关联的jar(目前是通过入口类 ,获取业务代码的jar ,避免扫描无关的第三方包)
  * 使用URLConnection扫描jar
  * 筛选出class并通过Class.forname获取class对象
  * 检查指定注解 ,返回符合的class列表
  
> 适用于代码打包成jar ,类似spring-boot所有代码打包在一个可执行jar中.

ps:目前通过入口类去获取被打包的jar ,但实际项目中可能有多个子项目 .因此Jar扫描应该可以扫描多个classpath下的jar包 ,但又要排除第三方jar的影响.

ps2:后续支持basePackage去扫描Koala