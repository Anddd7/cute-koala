package github.koala.bean.pool;

import static github.eddy.common.CompareTool.getOrElse;
import static github.eddy.common.ReflectTool.setFieldOfObject;

import github.koala.bean.Koala;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Store relationship between beans :
 * addRely : if A have field b (type B) ,but B has not add into BeanCache
 */
@Slf4j
public class BeanRelyCache {

  private Map<Class, Map<Object, Field>> relyCache = new HashMap<>();

  /**
   * @param target clazz of the field that instance is waiting for
   * @param instance current process but rely target's instance
   * @param field instance's field which type is target
   */
  void addRely(Class target, Object instance, Field field) {
    Map<Object, Field> tempMap = new HashMap<>();
    tempMap.put(instance, field);

    //if have other need target too ,merge them
    if (relyCache.get(target) != null) {
      tempMap.putAll(relyCache.get(target));
    }
    relyCache.put(target, tempMap);
  }

  /**
   * if koala is needed by other koalas ,set it into their fields
   */
  void doGetByOtherBean(Koala koala) {
    Map<Object, Field> rely = getRely(koala.getImplementClazz());
    if (rely == null) {
      return;
    }

    rely.forEach((instance, field) -> {
      log.info("{} is needed by [{}:{}] ,setting this to that bean.",
          koala.getImplementClazz().getSimpleName(),
          field.getType(),
          field.getName());

      Object targetInstance = getOrElse(koala.getObject(), koala::newInstanceOfObject);
      setFieldOfObject(field, instance, targetInstance);
    });
    clearRely(koala.getImplementClazz());
  }

  /**
   * inner method
   */
  private Map<Object, Field> getRely(Class target) {
    return relyCache.get(target);
  }

  private void clearRely(Class target) {
    relyCache.remove(target);
  }
}
