package github.koala.core.pool;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import github.koala.core.wrapper.BeanWrapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/21.
 * @description 依赖关系缓存
 */
@Slf4j
public class RelyCache {

  private ListMultimap<Class, BeanWrapper> cache;

   RelyCache() {
    cache = LinkedListMultimap.create();
  }

   List<BeanWrapper> get(Class classType) {
    return cache.get(classType);
  }

   void put(Class classType, BeanWrapper beanWrapper) {
    log.info("[{}]依赖的[{}]还未加载,等待中", beanWrapper.getObject().getClass().getName(),
        classType.getName());
    cache.put(classType, beanWrapper);
  }
}
