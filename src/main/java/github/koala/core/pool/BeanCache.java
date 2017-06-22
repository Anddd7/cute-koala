package github.koala.core.pool;

import github.koala.core.wrapper.BeanWrapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/21.
 * @description Bean的缓冲池
 */
@Slf4j
class BeanCache {

  private Map<Class, BeanWrapper> cache = new ConcurrentHashMap<>();

  /**
   * 获取Bean
   */
  BeanWrapper get(Class classType) {
    log.info("通过类型[{}],获取Bean", classType.getName());
    return cache.get(classType);
  }

  /**
   * 添加Bean ,同时添加接口和实现类 ,方便通过接口添加的依赖
   */
  void put(BeanWrapper beanWrapper) {
    cache.putIfAbsent(beanWrapper.getDefineType(), beanWrapper);
    cache.putIfAbsent(beanWrapper.getImplementType(), beanWrapper);
  }

  /**
   * 获取cacheMap
   */
  Map<Class, BeanWrapper> getCache4Test() {
    return cache;
  }
}
