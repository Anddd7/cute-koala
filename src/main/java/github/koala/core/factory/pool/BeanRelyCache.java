package github.koala.core.factory.pool;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import github.koala.core.bean.BeanWrapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/21.
 * @description 依赖关系缓存 ,按照[被依赖对象类型 , 需要注入依赖的Bean]形式存放
 */
@Slf4j
class BeanRelyCache {

  private ListMultimap<Class, BeanWrapper> cache = LinkedListMultimap.create();

  /**
   * 获取指定对象的依赖列表
   */
  List<BeanWrapper> get(Class classType) {
    return cache.get(classType);
  }

  /**
   * #beanWrapper 依赖 #wait4ClassType 类型的实例 ,等待中
   */
  void put(Class wait4ClassType, BeanWrapper beanWrapper) {
    log.info("!!!\t\t新增依赖:[{}]依赖的[{}]还未加载,等待中", beanWrapper.getInstance().getClass().getName(),
        wait4ClassType.getName());

    cache.put(wait4ClassType, beanWrapper);
  }

  /**
   * 依赖已解决
   */
  void remove(Class wait4ClassType) {
    cache.removeAll(wait4ClassType);
  }
}
