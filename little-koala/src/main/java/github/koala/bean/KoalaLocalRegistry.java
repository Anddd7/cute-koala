package github.koala.bean;

import github.eddy.common.ReflectTool;
import github.koala.annotation.KoalaLocal;
import github.koala.annotation.KoalaLocal.ScopeEnum;
import github.koala.bean.pool.KoalaZoo;
import github.koala.core.AbstractRegistry;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * Scan {@link KoalaLocal} and register it into zoo
 */
@Slf4j
public class KoalaLocalRegistry extends AbstractRegistry {

  public KoalaLocalRegistry(KoalaZoo zoo) {
    super(zoo);
  }

  /**
   * create and deal rely
   */
  @Override
  public <T> T createInstance(Class<T> implementClazz) {
    try {
      T instance = implementClazz.newInstance();
      doSetFromOtherBean(implementClazz, instance);
      return instance;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * scan field
   */
  @Override
  public Class registerKoala(Field field) {
    if (Objects.isNull(field.getAnnotation(KoalaLocal.class))) {
      return null;
    }

    //扫描实现类型
    Class implementType = field.getAnnotation(KoalaLocal.class).value();
    if (implementType.equals(KoalaLocal.class)) {
      implementType = field.getType();
    }
    ScopeEnum scope = field.getAnnotation(KoalaLocal.class).scope();
    Boolean isSingleton = scope.equals(ScopeEnum.SINGLETON);

    log.info("BEAN: find {} {}[{}] {} ", scope, field.getType(), implementType, field.getName());

    //register into zoo
    registerAsLocalBean(field.getType(), implementType, isSingleton);

    //return implementClazz and scan it next round ,to found nested beans
    return implementType;
  }

  private void registerAsLocalBean(Class defineType, Class implementType, Boolean isSingleton) {
    Koala koala = zoo.get(implementType);
    if (Objects.isNull(koala)) {
      koala = new Koala(implementType, isSingleton, this);
      zoo.put(implementType, koala);
      zoo.put(defineType, koala);
    } else {
      //check if have different define
      koala.checkConflict(implementType, isSingleton);
    }
  }

  /**
   * deal rely
   */
  private void doSetFromOtherBean(Class implementClazz, Object instance) {
    Stream.of(implementClazz.getDeclaredFields())
        .filter(field -> !Objects.isNull(field.getAnnotation(KoalaLocal.class)))
        .forEach(field -> {
          Class implementType = field.getAnnotation(KoalaLocal.class).value();
          if (implementType.equals(KoalaLocal.class)) {
            implementType = field.getType();
          }

          Koala existKoala = zoo.get(implementType);
          //检查依赖Bean是否已加载 ,未加载则等待加载
          if (Objects.isNull(existKoala)) {
            //放入关系池 等待依赖对象 : instance:field 依赖 implementType
            log.info("{}:{} need [{}] ,waiting for.",
                implementClazz.getSimpleName(),
                field.getName(),
                field.getType());
            zoo.addRely(implementType, instance, field);
          } else {
            //发现依赖对象set到自身(非单例缓存的是class ,需要在get的时候再set)
            log.info("{}:{} need [{}] ,setting it.",
                implementClazz.getSimpleName(),
                field.getName(),
                field.getType());

            ReflectTool.setFieldOfObject(field, instance, existKoala.getObject());
          }
        });
  }
}
