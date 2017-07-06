package github.koala.core.factory;

import github.koala.core.bean.BeanWrapper;
import github.koala.core.factory.pool.BeanPool;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/22.
 * @description 一个Module的Bean
 */
@Slf4j
@Data
class KoalaModule {

  String moduleName;
  Class moduleClass;
  BeanPool beanPool;

  KoalaModule(Class moduleClass) {
    this.moduleName = moduleClass.getSimpleName();
    this.moduleClass = moduleClass;
    this.beanPool = new BeanPool();
  }

  <T> T getBean(Class<T> classType) {
    return beanPool.getBean(classType);
  }

  /**
   * 循环创建bean和引用的bean
   */
  void createBean(Class defineType, Class implementType, Boolean isSingleton,Boolean isRpc) {
    log.info("添加Bean[{} - {}]到缓冲池.", defineType.getSimpleName(), implementType.getSimpleName());
    try {
      beanPool.addBean(defineType, implementType, isSingleton,isRpc);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      System.exit(0);
    }
  }

  Map<Class, BeanWrapper> getCache4Test() {
    return beanPool.getCache4Test();
  }
}
