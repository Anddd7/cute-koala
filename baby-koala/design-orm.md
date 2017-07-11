## ORM 实践
ORM 即 Object Relational Mapping ,把数据库的表结构对应到程序的模型中.比如Java ,数据库的表就像一个对象 ,里面的字段也就是对象的属性 ,在程序中直接处理对象显然比数据库表要更容易 .

在JDBC的基础上 ,基本已经完成了字段column sql type -> java type的映射 ,我们所做的就是根据实际操作 (增删改查) ,把返回的 JavaType 打包成对象.

### 生成POJO类
> ORM主要是生成程序可用的/易用的POJO类 ,实际上属于代码生成的功能 ,而不是数据库存取.

ResultSet中包含了数据库表的各种信息 : `java.sql.ResultSetMetaData`中有各Column的Name/Type/JavaType/长度等等 ,通过收集这些信息就能生成一个可用的POJO类.

### 生成调用方法
> 根据POJO类自动生成的SQL (Mybatis) ;或者自定义语言模型 ,结合配置文件生成最后的sql (Hibernate)

这里使用直接拼接SQL语句 ,判断里面的属性 ,预先设置好模板 ;同时也可以对sql进行后期修改.

### 组装POJO类
> 调用方法生成SQL ,交给JDBC实施查询 ,将结果通过反射的Field.set放置到POJO类中 ,然后返回 .因为POJO类中属性的类型是由JDBC在生成时指定的 ,所以类型一一对应 ,按顺序set即可.

返回的POJO类就可以在程序中使用了

### 优化
像Mybatis /Hibernate 等优秀的框架 ,在提供基本功能 ,还添加了很多保障性的设置和事务处理 ,这些也是非常重要的.