package github.koala.core.bean;

import github.koala.rpc.consumer.RpcServiceFactory;
import github.koala.webservice.resetful.HttpProxyBeanFactory;
import github.koala.webservice.resetful.annotation.HttpKoala;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description Bean的统一外包装
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class BeanWrapper {

  private Class defineType;//声明类型
  private Class implementType;//实例类型
  private Object instance;//实例对象
  private Boolean singleton;

  /**
   * 创建新的Bean
   */
  public static BeanWrapper of(Class classType, Class implementType, Boolean isSingleton,
      Boolean isRpc)
      throws Exception {
    log.info("!!!\t\t创建新Bean:{} - {}", classType.getSimpleName(), implementType.getName());

    BeanWrapper beanWrapper = new BeanWrapper();
    beanWrapper.setDefineType(classType);
    beanWrapper.setImplementType(implementType);
    beanWrapper.setSingleton(isSingleton);

    if (isRpc) {
      beanWrapper.setInstance(RpcServiceFactory.getProxyInstance(classType));
    } else {
      beanWrapper.initInstance();
    }
    return beanWrapper;
  }

  /**
   * 根据Bean的条件 创建instance
   */
  void initInstance() throws Exception {
    if (!singleton) {
      //非单例 ,object存放实现类型 ,getBean时自动实例化
      instance = implementType;
      return;
    }
    //单例的代理接口 ,生成代理对象
    if (!Objects.isNull(defineType.getAnnotation(HttpKoala.class))) {
      instance = HttpProxyBeanFactory.getProxyInstance(defineType);
      return;
    }

    //单例的Class 或 接口 ,实现类直接实例化
    instance = implementType.newInstance();
  }

  /**
   * 对比2个bean定义是否有不同
   */
  public void checkConflict(Class implementType, Boolean singleton) {
    if (!this.implementType.equals(implementType) || !this.singleton
        .equals(singleton)) {
      //和已有的bean的类型冲突 程序直接停止
      log.error("不能存在相同Type的不同作用域的Bean");
      System.exit(0);
    }
  }

  /**
   * 检查目标字段的type是否契合当前bean
   */
  public boolean matchType(Field field) {
    return field.getType().equals(implementType) || field.getType()
        .equals(defineType);
  }

  public Object getObjectOfInstance() {
    //本身是非单例的 ,马上实例化作为被依赖的对象
    if (!singleton) {
      try {
        return ((Class) instance).newInstance();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return instance;
  }
}