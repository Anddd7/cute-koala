package github.koala.core.wrapper;

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
      return new BeanWrapper(isSingleton ? classType.newInstance() : classType, isSingleton);
    } catch (InstantiationException e) {
      log.error(e.getMessage(), e);
    } catch (IllegalAccessException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

}
