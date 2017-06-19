package github.koala.core.factory;

import com.google.common.collect.Lists;
import github.koala.core.pool.BeanPool;
import github.koala.core.scan.BeanScanner;
import github.koala.core.scope.Scope;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description TODO
 */
@Slf4j
public class BeanFactory {

  List<Class> modules = Lists.newArrayList();
  BeanScanner scanner = new BeanScanner();

  public BeanFactory(Class... modules) {
    this.modules.addAll(Arrays.asList(modules));
  }

  public BeanFactory build() {
    scanner.scanModules(modules);
    return this;
  }

  public <T> T getBean(Class<T> classType) {
    Scope scope = BeanPool.getBean(classType);
    if (scope.getIsSingleton()) {
      log.info("当前Bean是单例,直接返回缓冲池中的对象.");
      return (T) scope.getObject();
    }
    try {
      log.info("当前Bean是非单例的,创建新的对象.");
      return ((Class<T>) scope.getObject()).newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Object getBean(String name) {
    Scope scope = BeanPool.getBean(name);
    if (scope.getIsSingleton()) {
      log.info("当前Bean是单例,直接返回缓冲池中的对象.");
      return scope.getObject();
    }

    try {
      log.info("当前Bean是非单例的,创建新的对象.");
      return ((Class) scope.getObject()).newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }


}
