package github.koala.bean;

import github.koala.core.AbstractRegistry;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Koala is a wrapper of instance
 */
@Slf4j
@Data
@NoArgsConstructor
public class Koala {

  Class implementClazz;
  Boolean isSingleton;
  Object object;//instance

  AbstractRegistry registry;


  public Koala(Class implementClazz, Boolean isSingleton, AbstractRegistry registry) {
    this.implementClazz = implementClazz;
    this.isSingleton = isSingleton;
    //singleton create instance when register
    if (isSingleton) {
      this.object = newInstanceOfObject();
    }
    this.registry = registry;
  }

  void checkConflict(Class implementClazz, Boolean isSingleton) {
    if (!this.implementClazz.equals(implementClazz) || !this.isSingleton.equals(isSingleton)) {
      System.exit(0);
    }
  }

  /**
   * Create a instance of implementClazz ,it's implementation defined by register
   */
  public Object newInstanceOfObject() {
    log.info("New a instance of {}", implementClazz.getName());
    return registry.createInstance(implementClazz);
  }
}
