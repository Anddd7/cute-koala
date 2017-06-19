package github.koala.core.pool;

import github.koala.core.scope.Scope;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description 存放Bean的缓冲池
 */
@Slf4j
public class BeanPool {

  private static final Map<String, Scope> name2BeanMap = new ConcurrentHashMap();
  private static final Map<Class, Scope> type2BeanMap = new ConcurrentHashMap();

  public static Scope getBean(Class classType) {
    log.info("通过类型[{}],获取Bean", classType.getName());
    return type2BeanMap.get(classType);
  }

  public static Scope getBean(String name) {
    log.info("通过名称[{}],获取Bean", name);
    return name2BeanMap.get(name);
  }

  public static void setBean(String name, Class classType, Scope instance) {
    type2BeanMap.putIfAbsent(classType, instance);
    name2BeanMap.putIfAbsent(name, instance);
  }

}
