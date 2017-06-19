package github.koala.core.scan;

import static com.google.common.base.Preconditions.checkNotNull;

import github.koala.core.annotation.Module;
import github.koala.core.annotation.NoSingleton;
import github.koala.core.pool.BeanPool;
import github.koala.core.scope.Scope;
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
    checkNotNull(module.getAnnotationsByType(Module.class));

    log.info("扫描模块[{}],路径[{}]", module.getSimpleName(), module.getName());
    Arrays.asList(module.getDeclaredFields()).forEach(this::scanField);
  }

  private void scanField(Field field) {
    Boolean isSingleton = Objects.isNull(field.getAnnotation(NoSingleton.class));
    Class classType = field.getType();
    String name = field.getName();

    log.info("扫描Bean,名称[{}],类型[{}],是否单例[{}]", name, classType.getName(), isSingleton);

    createBean(name, classType, isSingleton);
  }

  private void createBean(String name, Class classType, Boolean isSingleton) {
    log.info("添加Bean到缓冲池[{}]", name);
    BeanPool.setBean(name, classType, Scope.get(classType, isSingleton));
  }
}
