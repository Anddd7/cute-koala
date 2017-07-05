package github.koala.core.factory.pool;

import github.and777.common.CollectionTool;
import github.koala.core.annotation.Koala;
import github.koala.core.bean.BeanWrapper;
import github.koala.webservice.resetful.HttpProxyBeanFactory;
import github.koala.webservice.resetful.annotation.HttpKoala;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description 处理Bean的存放 ,依赖关系处理的中心类 ,在Pool里面都用BeanWrapper包装 ,对外则直接操作Bean的对象或Class
 */
@Slf4j
public class BeanPool {

  private final BeanWrapperCache beanCache = new BeanWrapperCache();
  private final BeanRelyCache relyCache = new BeanRelyCache();

  /**
   * 获取对应类型的Bean对象
   */
  public <T> T getBean(Class<T> classType) {
    BeanWrapper scope = beanCache.get(classType);
    if (scope.getSingleton()) {
      log.info("当前Bean是单例,直接返回缓冲池中的对象.");
      return (T) scope.getInstance();
    }
    try {
      log.info("当前Bean是非单例的,创建新的对象.");
      //如果是远程代理的Bean ,直接生成远程代理
      if (classType.isInterface() && !Objects.isNull(classType.getAnnotation(HttpKoala.class))) {
        return (T) HttpProxyBeanFactory.getProxyInstance(classType);
      }

      T instance = ((Class<T>) scope.getInstance()).newInstance();
      //创建对象时查看内部是否有依赖关系 ,有则set到instance里
      checkRelyFrom(scope, (beanWrapper, field) ->
          resolveRely(instance, field, getBean(field.getType()))
      );
      return instance;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * 添加Bean对象
   */
  public void addBean(Class defineType, Class implementType, Boolean isSingleton) {
    BeanWrapper beanWrapper = beanCache.get(defineType);
    if (Objects.isNull(beanWrapper)) {
      //创建新的bean放入cache
      beanWrapper = BeanWrapper.of(defineType, implementType, isSingleton);
      beanCache.put(beanWrapper);
    } else {
      beanWrapper.checkConflict(implementType, isSingleton);
    }

    if (beanWrapper.getSingleton()) {
      //检查自身是否依赖其他bean
      checkRelyFrom(beanWrapper, this::waitOrResolveRely);
    } else {
      log.info("非单例Bean ,获取Bean时再解决依赖");
    }
    //检查自身是否被其他类依赖
    checkRelyTo(beanWrapper);
  }

  /**
   * 解决循环依赖
   * - init时检索是否有set依赖( 构造器依赖使用newInstance会抛出异常 );
   * - 有set依赖时加入到待解决依赖map;
   * - 有被依赖的bean构造成功时,set到对应的bean中;
   */
  private void checkRelyFrom(BeanWrapper beanWrapper,
      BiConsumer<BeanWrapper, Field> biConsumer) {

    log.info("[{}]正在加载 ,检查是否依赖其他对象", beanWrapper.getImplementType().getName());
    //查看其实现类(也就是对应的实例对象)中是否依赖其他Bean
    Arrays.asList(beanWrapper.getImplementType().getDeclaredFields()).forEach(field -> {
      if (!Objects.isNull(field.getAnnotation(Koala.class))) {
        biConsumer.accept(beanWrapper, field);
      }
    });
  }

  /**
   * 判断是否可以立即解决这个依赖还是等待
   */
  private void waitOrResolveRely(BeanWrapper beanWrapper, Field field) {
    //获取依赖的Bean的实现类型(对应对象的类型)
    Class implementType = field.getAnnotation(Koala.class).value();
    if (implementType.equals(Koala.class)) {
      implementType = field.getType();
    }

    //查看实例是否已存在
    BeanWrapper oldBeanWrapper = beanCache.get(implementType);
    //检查依赖Bean是否已加载 ,未加载则等待加载
    if (Objects.isNull(oldBeanWrapper)) {
      //放入关系池 等待依赖对象
      relyCache.put(implementType, beanWrapper);
    } else {
      //发现依赖对象set到自身(非单例缓存的是class ,需要在get的时候再set)
      if (beanWrapper.getSingleton()) {
        resolveRely(beanWrapper.getInstance(), field, oldBeanWrapper.getInstance());
      }
    }
  }

  /**
   * 获取到 #classType 类型的对象 ,开始检索依赖关系并set到对应位置
   */
  private void checkRelyTo(BeanWrapper beanWrapper) {
    log.info("[{}-{}]正在加载,检查是否被其他对象依赖", beanWrapper.getDefineType(),
        beanWrapper.getImplementType().getName());

    //检查自身的实现类型是否被其他类需要
    relyCache.get(beanWrapper.getImplementType())
        .forEach(parentBeanWrapper -> {
          Object parent = parentBeanWrapper.getInstance();
          CollectionTool.dealFirst(
              parent.getClass().getDeclaredFields(),
              beanWrapper::matchType,
              t -> resolveRely(parent, t, beanWrapper.getObjectOfInstance())
          );
        });

    relyCache.remove(beanWrapper.getImplementType());
  }

  /**
   * 把#instance set到需要它的 #parent 对象的 #filed 字段中
   */
  private void resolveRely(Object parent, Field field, Object instance) {
    try {
      log.info("!!!\t\t处理依赖:[{}]已加载到[{}]的[{}]字段中.", instance.getClass().getSimpleName(),
          parent.getClass().getName(), field.getName());

      field.setAccessible(true);
      field.set(parent, instance);
    } catch (IllegalAccessException e) {
      log.error(e.getMessage(), e);
    }
  }

  public Map<Class, BeanWrapper> getCache4Test() {
    return beanCache.getCache4Test();
  }
}
