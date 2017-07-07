package github.koala.core.factory;

import github.koala.core.bean.BeanWrapper;
import github.koala.rpc.RpcServiceRegistry;
import github.koala.rpc.provider.RpcServiceServer;
import java.time.Duration;
import java.time.Instant;
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
  private List<Class> moduleList;

  private Map<Class, KoalaModule> moduleMap = new HashMap<>();
  private ModuleScanner scanner = new ModuleScanner();

  //可以切换当前module
  private int index;
  private KoalaModule currentModule;

  public static KoalaFactory of(Class... modules) {
    return new KoalaFactory().build(modules);
  }

  public static KoalaFactory of(Integer port, Class... modules) {
    return new KoalaFactory().startRPC(port).build(modules);
  }

  /**
   * 构建
   */
  private KoalaFactory build(Class... moduleClasses) {
    Instant start = Instant.now();
    log.info("----------------------------------------------");
    log.info("Factory启动~");

    moduleList = Arrays.asList(moduleClasses);
    scanAndBuild(moduleClasses);
    first();

    log.info("Factory加载完毕,耗时[{}]ms", Duration.between(start, Instant.now()).toMillis());
    log.info("----------------------------------------------\n");
    return this;
  }

  private KoalaFactory startRPC(Integer port) {
    RpcServiceServer server = new RpcServiceServer(port);
    RpcServiceRegistry.initRegistry(this,server);
    return this;
  }

  /**
   * 扫描器扫描并生成 module ,默认设置第一个module为当前的操作module
   */
  private void scanAndBuild(Class... moduleClasses) {
    for (Class moduleClass : moduleClasses) {
      scanner.createModule(moduleClass)
          .ifPresent(beanModule -> moduleMap.put(moduleClass, beanModule));
    }
  }

  /**
   * 对外的唯一入口方法
   */
  public <T> T getBean(Class<T> classType) {
    return currentModule.getBean(classType);
  }

  /**
   * 测试方法
   */
  public Map<Class, BeanWrapper> getCache4Test() {
    return currentModule.getCache4Test();
  }

  /**
   * 切换当前运转的module
   */
  public KoalaFactory setCurrentModule(Class moduleClass) {
    currentModule = moduleMap.get(moduleClass);
    return this;
  }

  public void setCurrentModule(int inputIndex) {
    if (inputIndex < 1 || inputIndex > moduleList.size()) {
      log.error("需要切换的模块编号需要在1-{}之间", moduleList.size());
      return;
    }
    setCurrentModule(moduleList.get(inputIndex - 1));
  }

  public KoalaFactory first() {
    index = 1;
    setCurrentModule(index);
    return this;
  }

  public KoalaFactory nextModule() {
    setCurrentModule(++index);
    return this;
  }

  public KoalaFactory previousModule() {
    setCurrentModule(--index);
    return this;
  }

}
