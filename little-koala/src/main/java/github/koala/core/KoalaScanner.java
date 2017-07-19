package github.koala.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class KoalaScanner {

  /**
   * registry list
   */
  private List<AbstractRegistry> registries = new ArrayList<>();

  void addRegistry(AbstractRegistry registry) {
    registries.add(registry);
  }

  /**
   * scan module class
   */
  void findKoala(Class moduleClass) {
    if (moduleClass == null) {
      return;
    }
    log.info("SCAN: class[{}]", moduleClass.getName());
    Arrays.asList(moduleClass.getDeclaredFields()).forEach(this::findKoala);
    log.info("SCAN: scanning done for [{}]", moduleClass.getSimpleName());
  }

  /**
   * scan implementClazz field
   */
  private void findKoala(Field field) {
    for (AbstractRegistry registry : registries) {
      //recursively scan
      findKoala(registry.registerKoala(field));
    }
  }
}
