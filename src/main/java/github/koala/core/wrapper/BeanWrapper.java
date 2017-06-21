package github.koala.core.wrapper;

import github.koala.core.annotation.HttpKoala;
import github.koala.core.rpc.HttpHandler;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/19.
 * @description Bean的统一外包装
 */
@Data
@AllArgsConstructor
@Slf4j
public class BeanWrapper {

  private Object object;
  private Boolean isSingleton;

  public static BeanWrapper of(Class classType, Boolean isSingleton) {
    try {
      if (!isSingleton) {
        return new BeanWrapper(classType, isSingleton);
      }

      if (classType.isInterface() && !Objects.isNull(classType.getAnnotation(HttpKoala.class))) {
        return new BeanWrapper(HttpHandler.getProxyObject(classType), isSingleton);
      }

      log.info("单例Bean ,直接创建Instance");
      return new BeanWrapper(classType.newInstance(), isSingleton);

    } catch (InstantiationException e) {
      log.error(e.getMessage(), e);
    } catch (IllegalAccessException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

}
