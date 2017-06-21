package github.koala.core.factory;

import com.google.common.collect.Lists;
import github.koala.core.pool.BeanPool;
import github.koala.core.scan.BeanScanner;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description 系统入口和管理中心 ,扫描并加载bean ,对外可以获取bean
 */
@Slf4j
public class BeanFactory {

  private List<Class> modules = Lists.newArrayList();
  private BeanScanner scanner = new BeanScanner();


  public BeanFactory(Class... modules) {
    this.modules.addAll(Arrays.asList(modules));
  }

  public static BeanFactory of(Class... modules) {
    return new BeanFactory(modules).build();
  }

  public BeanFactory build() {
    scanner.scanModules(modules);
    return this;
  }

  public <T> T getBean(Class<T> classType) {
    return BeanPool.getBean(classType);
  }
}
