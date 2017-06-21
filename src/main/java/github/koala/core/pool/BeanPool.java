package github.koala.core.pool;

import github.koala.core.annotation.Scope;
import github.koala.core.wrapper.BeanWrapper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description 处理Bean的存放 ,依赖关系处理的中心类 ,在Pool里面都用BeanWrapper包装 ,对外则直接操作Bean的对象或Class
 */
@Slf4j
public class BeanPool {

  private BeanPool() {
  }

  private static final BeanCache beanCache = new BeanCache();
  private static final RelyCache relyCache = new RelyCache();

  /**
   * 获取对应类型的Bean对象
   */
  public static <T> T getBean(Class<T> classType) {
    BeanWrapper scope = beanCache.get(classType);
    if (scope.getIsSingleton()) {
      log.info("当前Bean是单例,直接返回缓冲池中的对象.");
      return (T) scope.getObject();
    }
    try {
      log.info("当前Bean是非单例的,创建新的对象.");
      T instance = ((Class<T>) scope.getObject()).newInstance();

      //创建对象时查看内部是否有依赖关系 ,有则set到instance里
      checkRelyFrom(classType, scope, (beanWrapper, field) ->
          resolveRely(instance, field, getBean(field.getType()))
      );

      return instance;
    } catch (InstantiationException e) {
      log.error(e.getMessage(), e);
    } catch (IllegalAccessException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * 添加Bean对象
   */
  public static void addBean(Class classType, Boolean isSingleton) {
    BeanWrapper beanWrapper = beanCache.get(classType);
    if (Objects.isNull(beanWrapper)) {
      //创建新的bean放入cache
      beanWrapper = BeanWrapper.of(classType, isSingleton);
      beanCache.put(classType, beanWrapper);
    } else if (!beanWrapper.getIsSingleton().equals(isSingleton)) {
      //和已有的bean的类型冲突 程序直接停止
      log.error("不能存在相同Type的不同作用域的Bean");
      System.exit(0);
    }

    //检查自身是否依赖其他bean
    checkRelyFrom(classType, beanWrapper, BeanPool::waitOrResolveRely);
    //检查自身是否被其他类依赖
    checkRelyTo(classType, beanWrapper);
  }

  /**
   * 解决循环依赖
   * - init时检索是否有set依赖( 构造器依赖使用newInstance会抛出异常 );
   * - 有set依赖时加入到待解决依赖map;
   * - 有被依赖的bean构造成功时,set到对应的bean中;
   */
  private static void checkRelyFrom(Class classType, BeanWrapper beanWrapper,
      BiConsumer<BeanWrapper, Field> biConsumer) {
    log.info("[{}]正在加载 ,检查是否依赖其他对象", classType.getName());
    Arrays.asList(classType.getDeclaredFields()).forEach(field -> {
      if (!Objects.isNull(field.getAnnotation(Scope.class))) {
        biConsumer.accept(beanWrapper, field);
      }
    });
  }

  private static void waitOrResolveRely(BeanWrapper beanWrapper, Field field) {
    BeanWrapper oldBeanWrapper = beanCache.get(field.getType());
    //检查依赖Bean是否已加载 ,未加载则等待加载
    if (Objects.isNull(oldBeanWrapper)) {
      //放入关系池 等待依赖对象
      relyCache.put(field.getType(), beanWrapper);
    } else {
      //发现依赖对象set到自身(非单例缓存的是class ,需要在get的时候再set)
      if (beanWrapper.getIsSingleton()) {
        resolveRely(beanWrapper.getObject(), field, oldBeanWrapper.getObject());
      }
    }
  }

  /**
   * 获取到 #classType 类型的对象 ,开始检索依赖关系并set到对应位置
   */
  private static void checkRelyTo(Class classType, BeanWrapper beanWrapper) {
    log.info("[{}]正在加载,检查是否被其他对象依赖", classType.getName());
    relyCache.get(classType).forEach(parentBeanWrapper -> {
      Object instance = beanWrapper.getObject();
      if (!beanWrapper.getIsSingleton()) {
        try {
          instance = ((Class) instance).newInstance();
        } catch (InstantiationException e) {
          log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
          log.error(e.getMessage(), e);
        }
      }
      Object parent = parentBeanWrapper.getObject();
      Field[] fields = parent.getClass().getDeclaredFields();
      for (Field field : fields) {
        if (field.getType().equals(classType)) {
          resolveRely(parent, field, instance);
          break;
        }
      }
    });
  }

  private static void resolveRely(Object parent, Field field, Object instance) {
    try {
      log.info("[{}]已加载到[{}]的[{}]字段中.", instance.getClass().getSimpleName(),
          parent.getClass().getName(), field.getName());
      field.setAccessible(true);
      field.set(parent, instance);
    } catch (IllegalAccessException e) {
      log.error(e.getMessage(), e);
    }
  }

}
