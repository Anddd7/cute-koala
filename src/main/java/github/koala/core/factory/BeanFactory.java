package github.koala.core.factory;

import com.google.common.collect.Lists;
import github.koala.core.pool.BeanPool;
import github.koala.core.scan.BeanScanner;
import github.koala.core.scope.BeanWrapper;
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
    return BeanPool.getBean(classType);
  }


}
