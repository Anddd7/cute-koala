package github.koala.core.scan;

import github.koala.core.annotation.Module;
import github.koala.core.annotation.Scope;
import github.koala.core.annotation.Scope.ScopeEnum;
import github.koala.core.pool.BeanPool;
import github.koala.core.scope.BeanWrapper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description 扫描module 分析里面bean信息
 */
@Slf4j
public class BeanScanner {

  public void scanModules(List<Class> modules) {
    modules.forEach(this::scanModule);
  }

  public void scanModule(Class module) {
    if (Objects.isNull(module.getAnnotationsByType(Module.class))) {
      return;
    }

    log.info("扫描模块[{}],路径[{}]", module.getSimpleName(), module.getName());
    Arrays.asList(module.getDeclaredFields()).forEach(this::scanModuleField);
  }

  private void scanModuleField(Field field) {
    if (Objects.isNull(field.getAnnotation(Scope.class))) {
      return;
    }

    Boolean isSingleton = field.getAnnotation(Scope.class).type().equals(ScopeEnum.SINGLETON);
    Class classType = field.getType();
    String name = field.getName();

    log.info("扫描Bean,名称[{}],类型[{}],是否单例[{}]", name, classType.getName(), isSingleton);

    createBean(name, classType, isSingleton);

    //递归扫描子模块
    scanModule(classType);
  }

  /**
   * 循环创建bean和引用的bean
   */
  private void createBean(String name, Class classType, Boolean isSingleton) {
    log.info("添加Bean到缓冲池[{}]", name);
    try {
      BeanPool.addBean(name, classType, BeanWrapper.get(classType, isSingleton));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
