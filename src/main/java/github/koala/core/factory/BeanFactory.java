package github.koala.core.factory;

import com.google.common.collect.Lists;
import github.koala.core.annotation.HttpKoala;
import github.koala.core.pool.BeanPool;
import github.koala.core.rpc.HttpHandler;
import github.koala.core.scan.BeanScanner;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    Instant start = Instant.now();
    log.info("Factory启动~\n----------------------------------------------");
    scanner.scanModules(modules);
    log.info("Factory加载完毕,耗时[{}]\n----------------------------------------------",
        Duration.between(start, Instant.now()));
    return this;
  }

  public <T> T getBean(Class<T> classType) {
    return BeanPool.getBean(classType);
  }
}
