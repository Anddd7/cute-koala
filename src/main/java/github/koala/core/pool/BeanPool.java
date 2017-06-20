package github.koala.core.pool;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import github.koala.core.annotation.Scope;
import github.koala.core.scope.BeanWrapper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description 存放Bean的缓冲池
 */
@Slf4j
public class BeanPool {

  private static final Map<Class, BeanWrapper> type2BeanMap = new ConcurrentHashMap();

  public static BeanWrapper getBeanWrapper(Class classType) {
    log.info("通过类型[{}],获取Bean", classType.getName());
    return type2BeanMap.get(classType);
  }

  public static <T> T getBean(Class<T> classType) {
    BeanWrapper scope = getBeanWrapper(classType);
    if (scope.getIsSingleton()) {
      log.info("当前Bean是单例,直接返回缓冲池中的对象.");
      return (T) scope.getObject();
    }
    try {
      log.info("当前Bean是非单例的,创建新的对象.");
      T instance = ((Class<T>) scope.getObject()).newInstance();

      checkRelyFrom(classType, scope, (beanWrapper, field) ->
          resolveRely(instance, field, getBean(field.getType()))
      );

      return instance;
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void addBean(String name, Class classType, BeanWrapper beanWrapper)
      throws Exception {
    //检查自身是否依赖其他bean
    checkRelyFrom(classType, beanWrapper, BeanPool::waitOrSetField);
    //检查自身是否被其他类依赖
    checkRelyTo(classType, beanWrapper);

    BeanWrapper instance = getBeanWrapper(classType);
    if (instance != null && !instance.getIsSingleton().equals(beanWrapper.getIsSingleton())) {
      throw new Exception("不能存在相同Type的不同作用域的Bean");
    }

    type2BeanMap.putIfAbsent(classType, beanWrapper);
  }

  /**
   * 解决循环依赖
   * - init时检索是否有set依赖( 构造器依赖使用newInstance会抛出异常 )
   * - 有set依赖时加入到待解决依赖map
   * - 有被依赖的bean构造成功时 ,set到对应的bean中
   */
  private static final ListMultimap<Class, BeanWrapper> type2RelyBeanMap = LinkedListMultimap
      .create();

  /**
   * #instance 依赖 #classType 类型的对象 ,添加到依赖关系中
   */
  public static void checkRelyFrom(Class classType, BeanWrapper beanWrapper,
      BiConsumer<BeanWrapper, Field> biConsumer) {
    log.info("[{}]正在加载 ,检查是否依赖其他对象", classType.getName());
    Arrays.asList(classType.getDeclaredFields()).forEach(field -> {
      if (!Objects.isNull(field.getAnnotation(Scope.class))) {
        biConsumer.accept(beanWrapper, field);
      }
    });
  }

  private static void waitOrSetField(BeanWrapper beanWrapper, Field field) {
    BeanWrapper instance = getBeanWrapper(field.getType());
    //检查依赖Bean是否已加载 ,未加载则等待加载
    if (Objects.isNull(instance)) {
      addRely(field.getType(), beanWrapper);
    } else {
      //如果是单例 ,加载时直接处理里面的依赖
      if (beanWrapper.getIsSingleton()) {
        resolveRely(beanWrapper.getObject(), field, instance.getObject());
      }
    }
  }

  /**
   * 获取到 #classType 类型的对象 ,开始检索依赖关系并set到对应位置
   */
  public static void checkRelyTo(Class classType, BeanWrapper beanWrapper) {
    log.info("[{}]正在加载,检查是否被其他对象依赖", classType.getName());
    type2RelyBeanMap.get(classType).forEach(scope -> {
      Object instance = beanWrapper.getObject();
      if (!beanWrapper.getIsSingleton()) {
        try {
          instance = ((Class) instance).newInstance();
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      Object parent = scope.getObject();
      Field[] fields = parent.getClass().getDeclaredFields();
      for (Field field : fields) {
        if (field.getType().equals(classType)) {
          resolveRely(parent, field, instance);
          break;
        }
      }
    });
  }

  public static void addRely(Class classType, BeanWrapper instance) {
    log.info("[{}]依赖的[{}]还未加载,等待中", instance.getObject().getClass().getName(), classType.getName());
    type2RelyBeanMap.put(classType, instance);
  }

  public static void resolveRely(Object parent, Field field, Object instance) {
    try {
      log.info("[{}]已加载到[{}]的[{}]字段中.", instance.getClass().getSimpleName(),
          parent.getClass().getName(), field.getName());
      field.setAccessible(true);
      field.set(parent, instance);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }


}
