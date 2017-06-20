package github.koala.core.scope;

import lombok.Data;

/**
 * @author edliao on 2017/6/19.
 * @description Bean的统一外包装
 */
@Data
public class BeanWrapper {

  private Object object;
  private Boolean isSingleton;

  private BeanWrapper(Object object, Boolean isSingleton) {
    this.object = object;
    this.isSingleton = isSingleton;
  }

  public static BeanWrapper get(Class classType, Boolean isSingleton) {
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
