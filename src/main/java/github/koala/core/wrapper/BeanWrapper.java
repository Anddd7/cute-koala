package github.koala.core.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author edliao on 2017/6/19.
 * @description Bean的统一外包装
 */
@Data
@AllArgsConstructor
public class BeanWrapper {

  private Object object;
  private Boolean isSingleton;

  public static BeanWrapper of(Class classType, Boolean isSingleton) {
    if (isSingleton) {
      try {
        return new BeanWrapper(classType.newInstance(), isSingleton);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return new BeanWrapper(classType, isSingleton);
  }

}
