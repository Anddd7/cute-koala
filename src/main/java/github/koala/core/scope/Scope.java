package github.koala.core.scope;

import lombok.Data;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
@Data
public class Scope {

  private Object object;
  private Boolean isSingleton;

  private Scope(Object object, Boolean isSingleton) {
    this.object = object;
    this.isSingleton = isSingleton;
  }

  public static Scope get(Class classType, Boolean isSingleton) {
    if (isSingleton) {
      try {
        return new Scope(classType.newInstance(), isSingleton);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return new Scope(classType, isSingleton);
  }

}
