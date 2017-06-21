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
public class BeanCache {

  private Map<Class, BeanWrapper> cache;

  public BeanCache() {
    this.cache = new ConcurrentHashMap<>();
  }

  public BeanWrapper get(Class classType) {
    log.info("通过类型[{}],获取Bean", classType.getName());
    return cache.get(classType);
  }

  public BeanWrapper put(Class classType, BeanWrapper beanWrapper) {
    return cache.putIfAbsent(classType, beanWrapper);
  }
}
