package github.koala.bean.pool;

import github.koala.bean.Koala;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KoalaZoo {

  /**
   * find bean by implement or interface clazz
   */
  private Map<Class, Koala> implementCache = new ConcurrentHashMap<>();
  private Map<Class, Koala> interfaceCache = new ConcurrentHashMap<>();

  private Map<Class, Koala> getCache(Class clazz) {
    if (clazz.isInterface()) {
      return interfaceCache;
    } else {
      return implementCache;
    }
  }

  /**
   * rely
   */
  private BeanRelyCache relyCache = new BeanRelyCache();

  public void addRely(Class target, Object instance, Field field) {
    relyCache.addRely(target, instance, field);
  }

  /**
   * put/get
   */
  public void put(Class clazz, Koala koala) {
    //check if have rely before put
    relyCache.doGetByOtherBean(koala);
    getCache(clazz).put(clazz, koala);
  }

  public Koala get(Class clazz) {
    return getCache(clazz).get(clazz);
  }

  /**
   * get koala instance
   */
  public <T> T getKoala(Class<T> clazz) {
    Koala bean = get(clazz);
    if (bean.getIsSingleton()) {
      log.info("Get singleton bean of {}.", clazz.getSimpleName());
      return (T) bean.getObject();
    }

    try {
      log.info("{} is not singleton ,create new.", clazz.getSimpleName());
      return (T) bean.newInstanceOfObject();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
