package site.koalazoo.cutekoala.bean;

import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import site.koalazoo.cutekoala.annotation.Koala;
import site.koalazoo.cutekoala.annotation.KoalaImport;
import site.koalazoo.cutekoala.common.KoalaException;
import site.koalazoo.cutekoala.common.KoalaType;
import site.koalazoo.cutekoala.common.KoalaWrapper;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean管理中心
 */
@Slf4j
public class KoalaManager {

  Map<String, KoalaWrapper> align2Koala = new HashMap<>();

  KoalaPool pool = new KoalaPool();
  RelyCallBackManager relyManager = new RelyCallBackManager();

  /**
   * Koala缓存区
   */
  class KoalaPool {

    Map<String, KoalaWrapper> cache = new HashMap<>();

    /**
     * class - classname的映射
     */
    public String keySerilizer(Class clazz) {
      return clazz.getCanonicalName();
    }

    /**
     * 是否已存在关联如下class的koala
     */
    public boolean contains(Class clazz) {
      return cache.containsKey(keySerilizer(clazz));
    }

    public KoalaWrapper get(Class clazz) {
      return cache.get(keySerilizer(clazz));
    }

    public void put(Class clazz, KoalaWrapper koala) {
      cache.put(keySerilizer(clazz), koala);
    }
  }

  /**
   * Koala依赖关系callback缓存
   */
  class RelyCallBackManager {

    Map<Class, List<Consumer<Object>>> relyTable = new HashMap<>();

    public void addRely(Class targetClass, Consumer<Object> callback) {
      if (!relyTable.containsKey(targetClass)) {
        relyTable.put(targetClass, new ArrayList<>());
      }
      relyTable.get(targetClass).add(callback);
    }

    public List<Consumer<Object>> getRelys(Class targetClass) {
      return relyTable.containsKey(targetClass) ?
          relyTable.get(targetClass) : Collections.emptyList();
    }
  }


  /*----------------------------------------------------------------*/

  /**
   * 获取koala
   *
   * @param clazz 目标class
   * @param <T>   泛型
   */
  public <T> T getKoala(Class<T> clazz) {
    KoalaWrapper current = pool.get(clazz);
    Object object = current.object();
    if (current.isMultiple()) {
      object = createMultiKoala(current.clazz());
    }
    return (T) object;
  }


  /**
   * 添加koala
   */
  public void addKoala(Class clazz) {
    Class[] interfaces = clazz.getInterfaces();

    //check
    existKoalas(clazz);
    existKoalas(interfaces);

    //create
    Koala annotation = (Koala) clazz.getAnnotation(Koala.class);
    KoalaWrapper wrapper = KoalaWrapper.createKoala(annotation, clazz);

    //precheck
    precheck(wrapper);

    //aftercheckThenInsert & aftercheck
    aftercheckThenInsert(wrapper, clazz);
    aftercheckThenInsert(wrapper, interfaces);
    if (wrapper.hasAlign()) {
      align2Koala.put(wrapper.align(), wrapper);
    }
  }

  /**
   * 判断koala是否重复注册
   */
  public void existKoalas(Class... classes) {
    for (Class clazz : classes) {
      Preconditions.checkArgument(!pool.contains(clazz), "已存在Koala-%s", clazz);
    }
  }

  /**
   * 检查依赖情况 ,放入缓存池
   *
   * @param current 当前koala
   * @param classes 关联的classes
   */
  public void aftercheckThenInsert(KoalaWrapper current, Class... classes) {
    for (Class clazz : classes) {
      //aftercheck
      for (Consumer<Object> callback : relyManager.getRelys(clazz)) {
        Object object = current.object();
        if (current.type().equals(KoalaType.Multiple)) {
          object = createMultiKoala(current.clazz());
        }
        callback.accept(object);
      }
      //insert
      pool.put(clazz, current);
    }
  }

  /**
   * 检查当前的koala是否依赖其他koala
   *
   * @param current 当前创建的koala
   */
  public void precheck(KoalaWrapper current) {
    //多实例Koala延迟到创建时处理依赖
    if (current.type().equals(KoalaType.Multiple)) {
      return;
    }
    importKoala(current.clazz(), current.object());
  }

  /**
   * 检查并为object对象注入其他koala
   */
  public void importKoala(Class clazz, Object object) {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.getAnnotation(KoalaImport.class) == null) {
        continue;
      }
      Class targetClass = field.getType();
      if (pool.contains(targetClass)) {
        KoalaWrapper targetKoala = pool.get(targetClass);
        //获取对象实例
        Object targetObject = targetKoala.object();
        //multi类型的直接创建实例
        if (targetKoala.type().equals(KoalaType.Multiple)) {
          targetObject = createMultiKoala(targetKoala.clazz());
        }
        //设置属性
        setVal4Field(field, object, targetObject);
        log.debug("{} 的 {} 字段已经注入koala - {} - {}",
            clazz, field.getName(), targetKoala.type(), targetKoala.clazz());
      } else {
        relyManager.addRely(targetClass,
            targetObject -> setVal4Field(field, object, targetObject)
        );
        log.debug("{} 的 {} 字段等待注入 {}", clazz, field.getName(), targetClass);
      }
    }
  }

  public void setVal4Field(Field field, Object o, Object val) {
    field.setAccessible(true);
    try {
      field.set(o, val);
    } catch (IllegalAccessException e) {
      throw new KoalaException(e);
    }
  }

  public Object createMultiKoala(Class multiKoalaClass) {
    log.debug("正在创建Multiple Koala实例 - {}", multiKoalaClass);
    try {
      Object object = multiKoalaClass.newInstance();
      importKoala(multiKoalaClass, object);
      return object;
    } catch (Exception e) {
      throw new KoalaException(e);
    }
  }
}
