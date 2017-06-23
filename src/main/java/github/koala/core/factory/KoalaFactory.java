package github.koala.core.factory;

import github.koala.core.wrapper.BeanWrapper;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description 系统入口和管理中心 ,扫描并加载bean ,对外可以获取bean
 */
@Slf4j
public class KoalaFactory {

  @Getter
  private List<Class> moduleClasses = new ArrayList<>();

  private Map<Class, KoalaModule> moduleMap = new HashMap<>();
  private ModuleScanner scanner = new ModuleScanner();
  private KoalaModule currentModule;

  public static KoalaFactory of(Class... modules) {
    return new KoalaFactory(modules).build();
  }

  private KoalaFactory(Class... moduleClasses) {
    this.moduleClasses = Arrays.asList(moduleClasses);
  }

  /**
   * 构建
   */
  private KoalaFactory build() {
    Instant start = Instant.now();
    log.info("----------------------------------------------");
    log.info("Factory启动~");

    scanAndBuild();

    log.info("Factory加载完毕,耗时[{}]ms", Duration.between(start, Instant.now()).toMillis());
    log.info("----------------------------------------------\n");
    return this;
  }

  /**
   * 扫描器扫描并生成 module ,默认设置第一个module为当前的操作module
   */
  private void scanAndBuild() {
    for (Class moduleClass : moduleClasses) {
      scanner.createModule(moduleClass)
          .ifPresent(beanModule -> {
            moduleMap.put(moduleClass, beanModule);
            if (currentModule == null) {
              currentModule = beanModule;
            }
          });
    }
  }

  /**
   * 对外的唯一入口方法
   */
  public <T> T getBean(Class<T> classType) {
    return currentModule.getBean(classType);
  }

  /**
   * TODO 测试方法
   */
  public Map<Class, BeanWrapper> getCache4Test() {
    return currentModule.getCache4Test();
  }

  /**
   * 切换当前运转的module
   */
  public void setCurrentModule(Class moduleClass) {
    currentModule = moduleMap.get(moduleClass);
  }

}
