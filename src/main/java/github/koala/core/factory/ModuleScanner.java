package github.koala.core.factory;

import github.koala.core.annotation.Koala;
import github.koala.core.annotation.Koala.ScopeEnum;
import github.koala.core.annotation.Module;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description 扫描module 分析里面bean信息
 */
@Slf4j
class ModuleScanner {

  /**
   * 创建一个module
   */
  Optional<KoalaModule> createModule(Class moduleClass) {
    if (!isModule(moduleClass)) {
      return Optional.empty();
    }
    KoalaModule newModule = new KoalaModule(moduleClass);
    scanModule(newModule);
    return Optional.of(newModule);
  }

  /**
   * 检查注解标记
   */
  private Boolean isModule(Class moduleClass) {
    return !Objects.isNull(moduleClass.getAnnotationsByType(Module.class));
  }

  /**
   * 扫描
   */
  private void scanModule(KoalaModule beanModule) {
    log.info("开始扫描模块:{}", beanModule.getModuleName());
    scanClass(beanModule, beanModule.moduleClass);
  }

  /**
   * 扫描类 ,方便树型操作
   */
  private void scanClass(KoalaModule beanModule, Class moduleClass) {
    log.info("开始扫描目标类[{}],路径[{}]", moduleClass.getSimpleName(), moduleClass.getName());
    Arrays.asList(moduleClass.getDeclaredFields())
        .forEach(field -> scanComponent(beanModule, field));
    log.info("扫描类[{}]完毕", moduleClass.getSimpleName());
  }

  /**
   * 扫描需要注入依赖的字段
   */
  private void scanComponent(KoalaModule beanModule, Field field) {
    if (Objects.isNull(field.getAnnotation(Koala.class))) {
      return;
    }

    Class defineType = field.getType();
    Class implementType = field.getAnnotation(Koala.class).value();
    if (implementType.equals(Koala.class)) {
      implementType = defineType;
    }
    Boolean isSingleton = field.getAnnotation(Koala.class).scope().equals(ScopeEnum.SINGLETON);

    log.info("扫描Bean,声明类型[{}] ,实现类型[{}],是否单例[{}]", defineType.getName(), implementType.getName(),
        isSingleton);

    beanModule.createBean(defineType, implementType, isSingleton);

    //递归扫描子模块
    log.info("开始扫描[{}]字段的依赖类[{}]", field.getName(), implementType.getSimpleName());
    scanClass(beanModule, implementType);
  }
}
