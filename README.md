## Cute Koala 
[![Build Status](https://travis-ci.org/Anddd7/cute-koala.svg?branch=master)](https://travis-ci.org/Anddd7/cute-koala)
[![Coverage Status](https://coveralls.io/repos/github/Anddd7/cute-koala/badge.svg?branch=master)](https://coveralls.io/github/Anddd7/cute-koala?branch=master)

仿`Spring/ Spring-mvc/ Mybatis/ Dubbo`的框架 ,主要用来研究和学习各大框架原理.

## 20180110 fix 路径bug/准备RPC
* FilePath的path改为final字段 ,切换路径(parent/child)会返回一个新的对象
```java
  private final String path;
  
  public FilePath parent() {
    return new FilePath(PathTool.getParent(path()));
  }
```
* 增加RPC需要的参数和注解



## 20180109 路径工具类/注册/依赖处理/获取/使用
编写了一个路径工具类
* 统一使用'/'作为分隔符
* 通过链式方法切换路径
* 通过方法获取File和URL
```java
FilePath prefix = FilePath.of(MODULE_DIR).child("target", "test-classes");
//prefix.path = $MODULE_PATH/target/test-classes
FilePath filePath = prefix.child("site", "koalazoo", "cutekoala", "model");
//prefix.path = $MODULE_PATH/target/test-classes/site/koalazoo/cutekoala/model
File file = fielPath.getFile();
```


积累了上个版本的经验 ,这次很快就完成了
* 检查是否有已加入的Koala ,冲突则报错退出
* 创建对象
  * 后续通过在annotation中指定一些handler对Koala进行前置处理(构造函数/参数/方法)
* 前置检查 ,检查当前koala内是否有需要import的对象
  * 扫描带@KoalaImport注解的fields
  * 根据类型查询koala缓存
    * 已有koala: 注入到当前koala的field中 (如果是multiple类型的 ,先实例化)
    * 没有koala: 创建callback缓存 ,等带目标koala加载
* 后置检查 ,检查当前koala是否被其他对象import
  * 检查callback列表 ,找出与当前koala相关的
  * 调用回调进行注入
  * 放入koala缓存
  
对比上个版本:
* 处理过程中不设置泛型(没有意义) ,只在获取koala时使用泛型强转
* 注册koala后 ,使用此对象的class/interface都能访问到此对象
* 设置别名align进行快速访问和远程调用

> A 依赖 B(multi) ,B 依赖 C : 假设B已经注册为Koala ,此时A进行前置 ,需要注入B .因为B是Multi类型 ,所以会即时创建实例 ;同时因为B也依赖了C ,所以B也要进行前置检查 .但C此时还未注册 ,所以会形成A=B=C的链式依赖.
* 使用递归处理multiple类型的koala的创建和依赖处理
* 使用函数回调进行依赖关系的解决 ,减少了代码复杂度

* A依赖B
* 取koala-B ,multi类型
  * 实例化对象B
  * 检测B依赖C
    * C未加载 ,存入callback
  * B实例化完成
* A注入B完成
* 注册C
  * C存在被依赖的callback
  * 执行回调 ,C写入到B的实例中
  * C注册完成
* 完成 - A{B{C}}

```java
    /**
     * 检查并为object对象注入其他koala
     */
    public void importKoala(Class clazz, Object object) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.getAnnotation(KoalaImport.class) == null) {
          continue;
        }
        Class targetClass = field.getType();
        if (pool.contains(targetClass)) {
          KoalaWrapper targetKoala = pool.get(targetClass);
          //获取对象实例
          Object targetObject = targetKoala.object();
          //multi类型的直接创建实例
          if (targetKoala.type().equals(KoalaType.Multiple)) {
            targetObject = createMultiKoala(targetKoala.clazz());
          }
          //设置属性
          setVal4Field(field, object, targetObject);
          log.debug("{} 的 {} 字段已经注入koala - {} - {}",
              clazz, field.getName(), targetKoala.type(), targetKoala.clazz());
        } else {
          //使用函数回调进行依赖关系的解决 ,减少了代码复杂度
          relyManager.addRely(targetClass,
              targetObject -> setVal4Field(field, object, targetObject)
          );
          log.debug("{} 的 {} 字段等待注入 {}", clazz, field.getName(), targetClass);
        }
      }
    }
    
    public Object createMultiKoala(Class multiKoalaClass) {
      log.debug("正在创建Multiple Koala实例 - {}", multiKoalaClass);
      try {
        Object object = multiKoalaClass.newInstance();
        //使用递归处理multiple类型的koala的创建和依赖处理
        importKoala(multiKoalaClass, object);
        return object;
      } catch (Exception e) {
        throw new KoalaException(e);
      }
    }

```

  
```java
public void addKoala(Class clazz) {
    Class[] interfaces = clazz.getInterfaces();

    //check
    existKoalas(clazz);
    existKoalas(interfaces);

    //create
    Koala annotation = (Koala) clazz.getAnnotation(Koala.class);
    KoalaWrapper wrapper = KoalaWrapper.createKoala(annotation, clazz);

    //precheck
    precheck(wrapper);

    //aftercheckThenInsert & aftercheck
    aftercheckThenInsert(wrapper, clazz);
    aftercheckThenInsert(wrapper, interfaces);
    if (wrapper.hasAlign()) {
      align2Koala.put(wrapper.align(), wrapper);
    }
  }
```


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